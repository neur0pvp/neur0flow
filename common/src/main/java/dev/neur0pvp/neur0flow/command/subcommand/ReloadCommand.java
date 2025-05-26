package dev.neur0pvp.neur0flow.command.subcommand;

import dev.neur0pvp.neur0flow.Base;
import dev.neur0pvp.neur0flow.command.generic.BuilderCommand;
import dev.neur0pvp.neur0flow.event.events.ConfigReloadEvent;
import dev.neur0pvp.neur0flow.event.KBSyncEventHandler;
import dev.neur0pvp.neur0flow.manager.ConfigManager;
import dev.neur0pvp.neur0flow.sender.Sender;
import dev.neur0pvp.neur0flow.util.ChatUtil;
import org.incendo.cloud.CommandManager;
import org.incendo.cloud.permission.PredicatePermission;

import java.util.function.Predicate;

public class ReloadCommand implements BuilderCommand {

    private static final ConfigManager configManager = Base.INSTANCE.getConfigManager();
    private String rawReloadMessage = configManager.getConfigWrapper().getString("messages.reload.success", "&aSuccessfully reloaded neur0flow.");

    public void register(CommandManager<Sender> manager) {
        manager.command(
            manager.commandBuilder("knockbacksync", "kbsync", "kbs")
                    .literal("reload")
                    .permission((sender -> {
                        final String permission = "knockbacksync.reload";
                        Predicate<Sender> senderPredicate = (s) -> {
                            return s.hasPermission(permission, false);
                        };

                            return PredicatePermission.of(senderPredicate).testPermission(sender);
                        }))
                        .handler(context -> {
                            configManager.loadConfig(true);

                            // Fire the ConfigReloadEvent
                            new ConfigReloadEvent(configManager).post();

                            String reloadMessage = ChatUtil.translateAlternateColorCodes('&', rawReloadMessage);

                            context.sender().sendMessage(reloadMessage);
                        })
        );
        Base.INSTANCE.getEventBus().registerListeners(this);
    }

    @KBSyncEventHandler
    public void onConfigReload(ConfigReloadEvent event) {
        rawReloadMessage = event.getConfigManager().getConfigWrapper().getString("messages.reload.success", "&aSuccessfully reloaded neur0flow.");
    }
}