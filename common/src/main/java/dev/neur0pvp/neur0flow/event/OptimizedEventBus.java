package dev.neur0pvp.neur0flow.event;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class OptimizedEventBus implements EventBus {
    // ordinarily this would just be a hashmap since we don't modify it on different threads, but Folia forces us to make it concurrent
    private final Map<Class<? extends Event>, List<OptimizedListener>> listenerMap = new ConcurrentHashMap<>();
    private final MethodHandles.Lookup lookup = MethodHandles.lookup();

    @Override
    public void registerListeners(Object listener) {
        registerMethods(listener, listener.getClass());
    }

    @Override
    public void registerStaticListeners(Class<?> clazz) {
        registerMethods(null, clazz);
    }

    private void registerMethods(Object instance, Class<?> clazz) {
        for (Method method : clazz.getDeclaredMethods()) {
            KBSyncEventHandler annotation = method.getAnnotation(KBSyncEventHandler.class);
            if (annotation != null && method.getParameterCount() == 1) {
                Class<?> eventType = method.getParameterTypes()[0];
                if (Event.class.isAssignableFrom(eventType)) {
                    try {
                        // Skip instance methods when registering static listeners
                        if (instance == null && !Modifier.isStatic(method.getModifiers())) {
                            continue;
                        }

                        MethodHandle handle = lookup.unreflect(method);
                        OptimizedListener optimizedListener = new OptimizedListener(instance, handle, annotation.priority(), method.getDeclaringClass());
                        // Yes the CopyOnWriteArrayList is slow, yes we need to for thread safety on Folia, we'll replace when performance problems actually crop up
                        listenerMap.computeIfAbsent((Class<? extends Event>) eventType, k -> new CopyOnWriteArrayList<>()).add(optimizedListener);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        // Sort listeners by priority
        listenerMap.values().forEach(list -> list.sort((a, b) -> Integer.compare(b.priority, a.priority)));
    }

    @Override
    public void unregisterListeners(Object listener) {
        listenerMap.values().forEach(list -> list.removeIf(l -> l.instance == listener));
    }

    @Override
    public void unregisterStaticListeners(Class<?> clazz) {
        listenerMap.values().forEach(list -> list.removeIf(l -> l.instance == null && l.declaringClass == clazz));
    }

    @Override
    public void post(Event event) {
        List<OptimizedListener> listeners = listenerMap.get(event.getClass());
        if (listeners != null) {
            for (OptimizedListener listener : listeners) {
                try {
                    if (listener.instance != null) {
                        // Instance method
                        listener.handle.invoke(listener.instance, event);
                    } else {
                        // Static method
                        listener.handle.invoke(event);
                    }
                    if (event.isCancelled()) {
                        break;
                    }
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        }
    }

    private static class OptimizedListener {
        final Object instance; // null for static methods
        final MethodHandle handle;
        final int priority;
        final Class<?> declaringClass;

        OptimizedListener(Object instance, MethodHandle handle, int priority, Class<?> declaringClass) {
            this.instance = instance;
            this.handle = handle;
            this.priority = priority;
            this.declaringClass = declaringClass;
        }
    }
}