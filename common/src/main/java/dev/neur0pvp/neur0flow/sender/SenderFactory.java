package dev.neur0pvp.neur0flow.sender;

import dev.neur0pvp.neur0flow.Base;

import java.util.Objects;
import java.util.UUID;

/**
 * Factory class to make a thread-safe sender instance
 *
 * @param <P> the plugin type
 * @param <T> the command sender type
 */
public abstract class SenderFactory<P extends Base, T> implements AutoCloseable {
    private final P plugin;

    public SenderFactory(P plugin) {
        this.plugin = plugin;
    }

    protected P getPlugin() {
        return this.plugin;
    }

    protected abstract UUID getUniqueId(T sender);

    protected abstract String getName(T sender);

    protected abstract void sendMessage(T sender, String message);

    protected abstract boolean hasPermission(T sender, String node);

    protected abstract boolean hasPermission(T sender, String node, boolean defaultIfUnset);

    protected abstract void performCommand(T sender, String command);

    protected abstract boolean isConsole(T sender);

    protected boolean shouldSplitNewlines(T sender) {
        return isConsole(sender);
    }

    public final Sender wrap(T sender) {
        Objects.requireNonNull(sender, "sender");
        return new AbstractSender<>(this.plugin, this, sender);
    }

    @SuppressWarnings("unchecked")
    public final T unwrap(Sender sender) {
        Objects.requireNonNull(sender, "sender");
        return (T) sender.getSender();
    }
}
