package dev.neur0pvp.neur0flow.sender;

import dev.neur0pvp.neur0flow.Base;

import java.util.UUID;

/**
 * Simple implementation of {@link Sender} using a {@link SenderFactory}
 *
 * @param <T> the command sender type
 */
public final class AbstractSender<T> implements Sender {
    private final Base plugin;
    private final SenderFactory<?, T> factory;
    private final T sender;

    private final UUID uniqueId;
    private final String name;
    private final boolean isConsole;

    AbstractSender(Base plugin, SenderFactory<?, T> factory, T sender) {
        this.plugin = plugin;
        this.factory = factory;
        this.sender = sender;
        this.uniqueId = factory.getUniqueId(this.sender);
        this.name = factory.getName(this.sender);
        this.isConsole = this.factory.isConsole(this.sender);
    }

    @Override
    public Base getPlugin() {
        return this.plugin;
    }

    @Override
    public UUID getUniqueId() {
        return this.uniqueId;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void sendMessage(String message) {
        this.factory.sendMessage(this.sender, message);
    }

    @Override
    public boolean hasPermission(String permission) {
        return isConsole() || this.factory.hasPermission(this.sender, permission);
    }

    @Override
    public boolean hasPermission(String permission, boolean defaultIfUnset) {
        return isConsole() || this.factory.hasPermission(this.sender, permission, defaultIfUnset);
    }

    @Override
    public boolean isConsole() {
        return this.isConsole;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof AbstractSender)) return false;
        final AbstractSender<?> that = (AbstractSender<?>) o;
        return this.getUniqueId().equals(that.getUniqueId());
    }

    @Override
    public int hashCode() {
        return this.uniqueId.hashCode();
    }

    @Override
    public T getSender() {
        return sender;
    }
}
