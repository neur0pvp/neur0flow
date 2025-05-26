package dev.neur0pvp.neur0flow.command.subcommand;

import dev.neur0pvp.neur0flow.Base;
import dev.neur0pvp.neur0flow.ConfigWrapper;
import dev.neur0pvp.neur0flow.command.generic.BuilderCommand;
import dev.neur0pvp.neur0flow.event.KBSyncEventHandler;
import dev.neur0pvp.neur0flow.event.events.ConfigReloadEvent;
import dev.neur0pvp.neur0flow.sender.Sender;
import dev.neur0pvp.neur0flow.util.ChatUtil;
import org.incendo.cloud.CommandManager;
import org.incendo.cloud.permission.PredicatePermission;

import java.util.function.Predicate;

public class ToggleOffGroundSubcommand implements BuilderCommand {

    public static boolean offGroundSyncEnabled = true;
    private String offGroundSyncEnableMessage;
    private String offGroundSyncDisableMessage;

    public ToggleOffGroundSubcommand() {
        loadConfigSettings();
    }

    public void register(CommandManager<Sender> manager) {
        manager.command(
            manager.commandBuilder("knockbacksync", "kbsync", "kbs")
                .literal("toggleoffground")
                .permission((sender -> {
                    final String permission = "knockbacksync.toggleoffground";
                    Predicate<Sender> senderPredicate = (s) -> {
                        return s.hasPermission(permission, false);
                    };

                    return PredicatePermission.of(senderPredicate).testPermission(sender);
                }))
                .handler(commandContext -> {
                    offGroundSyncEnabled = !offGroundSyncEnabled;
                    Base.INSTANCE.getConfigManager().getConfigWrapper().set("enable_offground_synchronization", offGroundSyncEnabled);
                    Base.INSTANCE.getConfigManager().saveConfig();
                    String message = ChatUtil.translateAlternateColorCodes('&',
                            offGroundSyncEnabled ? offGroundSyncEnableMessage : offGroundSyncDisableMessage);
                    commandContext.sender().sendMessage(message);
                }));
    }

    @KBSyncEventHandler
    public void onConfigReloadEvent(ConfigReloadEvent event) {
        loadConfigSettings();
    }

    private void loadConfigSettings() {
        ConfigWrapper configWrapper = Base.INSTANCE.getConfigManager().getConfigWrapper();
        offGroundSyncEnabled = configWrapper.getBoolean("settings.offground.enabled", true);
        this.offGroundSyncEnableMessage = configWrapper.getString("messages.offground.enable",
                "&aSuccessfully enabled offground synchronization.");
        this.offGroundSyncDisableMessage = configWrapper.getString("messages.offground.disable",
                "&cSuccessfully disabled offground synchronization.");
    }
}