package dev.neur0pvp.neur0flow.command.subcommand;
import com.github.retrooper.packetevents.protocol.player.User;
import dev.neur0pvp.neur0flow.Base;
import dev.neur0pvp.neur0flow.ConfigWrapper;
import dev.neur0pvp.neur0flow.command.generic.BuilderCommand;
import dev.neur0pvp.neur0flow.command.generic.PlayerSelector;
import dev.neur0pvp.neur0flow.event.events.ToggleOnOffEvent;
import dev.neur0pvp.neur0flow.manager.CombatManager;
import dev.neur0pvp.neur0flow.manager.ConfigManager;
import dev.neur0pvp.neur0flow.manager.PlayerDataManager;
import dev.neur0pvp.neur0flow.permission.PermissionChecker;
import dev.neur0pvp.neur0flow.player.PlatformPlayer;
import dev.neur0pvp.neur0flow.sender.Sender;
import dev.neur0pvp.neur0flow.util.ChatUtil;
import org.incendo.cloud.CommandManager;
import org.incendo.cloud.permission.PredicatePermission;

import java.util.function.Predicate;

public class ToggleCommand implements BuilderCommand {

    private static final ConfigManager configManager = Base.INSTANCE.getConfigManager();
    private static final PermissionChecker permissionChecker = Base.INSTANCE.getPermissionChecker();
    private static final String TOGGLE_GLOBAL_PERMISSION = "neur0flow.toggle.global";
    private static final String TOGGLE_SELF_PERMISSION = "neur0flow.toggle.self";
    private static final String TOGGLE_OTHER_PERMISSION = "neur0flow.toggle.other";

    private String noGlobalPermissionMessage;
    private String noSelfPermissionMessage;
    private String noOtherPermissionMessage;
    private String serverDisabledMessage;

    public ToggleCommand() {
        loadConfig();
    }

    private void loadConfig() {
        ConfigWrapper configWrapper = configManager.getConfigWrapper();
        noGlobalPermissionMessage = configWrapper.getString("messages.toggle.permission.no_global",
                "&cYou don't have permission to toggle the global setting.");
        noSelfPermissionMessage = configWrapper.getString("messages.toggle.permission.no_self",
                "&cYou do not have permission to toggle your knockback.");
        noOtherPermissionMessage = configWrapper.getString("messages.toggle.permission.no_other",
                "&cYou do not have permission to toggle the knockback of other player's.");
        serverDisabledMessage = configWrapper.getString("messages.toggle.server_disabled",
                "&cneur0flow is currently disabled on this server. Contact your server administrator for more information.");
    }

    @Override
    public void register(CommandManager<Sender> manager) {
        manager.command(
            manager.commandBuilder("neur0flow", "kbsync", "kbs")
                .literal("toggle")
                .optional("target", Base.INSTANCE.getPlayerSelectorParser().descriptor())
                    .permission((sender -> {
                        Predicate<Sender> senderPredicate = (s) -> {
                            return s.hasPermission(TOGGLE_GLOBAL_PERMISSION, false)
                                    || sender.hasPermission(TOGGLE_SELF_PERMISSION, false)
                                    || sender.hasPermission(TOGGLE_OTHER_PERMISSION, false);
                        };

                        return PredicatePermission.of(senderPredicate).testPermission(sender);
                    }))
                .handler(context -> {
                    Sender sender = context.sender();
                    PlayerSelector targetSelector = context.getOrDefault("target", null);
                    if (targetSelector == null) {
                        // Global toggle
                        if (permissionChecker.hasPermission(sender, TOGGLE_GLOBAL_PERMISSION, false)) {
                            toggleGlobalKnockback(sender);
                        } else {
                            sender.sendMessage(ChatUtil.translateAlternateColorCodes('&', noGlobalPermissionMessage));
                        }
                    } else {
                        PlatformPlayer target = targetSelector.getSinglePlayer();
                        boolean senderIsTarget = sender.getUniqueId() == target.getUUID();
                        if (!senderIsTarget && !permissionChecker.hasPermission(sender, TOGGLE_OTHER_PERMISSION, false)) {
                            sender.sendMessage(ChatUtil.translateAlternateColorCodes('&', noOtherPermissionMessage));
                            return;
                        } else if (senderIsTarget && !permissionChecker.hasPermission(sender, TOGGLE_SELF_PERMISSION, true)) {
                            sender.sendMessage(ChatUtil.translateAlternateColorCodes('&', noSelfPermissionMessage));
                            return;
                        }

                        if (!configManager.isToggled()) {
                            sender.sendMessage(ChatUtil.translateAlternateColorCodes('&', serverDisabledMessage));
                        } else {
                            togglePlayerKnockback(target, sender);
                        }
                    }
                })
        );
    }


    private static void toggleGlobalKnockback(Sender sender) {
        boolean toggledState = !configManager.isToggled();
        ToggleOnOffEvent toggleOnOffEvent = new ToggleOnOffEvent(toggledState);
        toggleOnOffEvent.post();
        if (toggleOnOffEvent.isCancelled())
            return;

        toggledState = toggleOnOffEvent.getStatus();
        configManager.setToggled(toggledState);

        configManager.getConfigWrapper().set("enabled", toggledState);
        configManager.saveConfig();

        String message = ChatUtil.translateAlternateColorCodes('&',
                toggledState ? configManager.getEnableMessage() : configManager.getDisableMessage()
        );
        sender.sendMessage(ChatUtil.translateAlternateColorCodes('&', message));
    }

    private static void togglePlayerKnockback(PlatformPlayer target, Sender sender) {
        User user = target.getUser();
        if (user == null) {
            String message = ChatUtil.translateAlternateColorCodes('&',
                    configManager.getPlayerDisconnectedWhileExecutingCommand()
            ).replace("%player%", target.getName());
            sender.sendMessage(message);
        }

        if (PlayerDataManager.shouldExempt(target.getUUID())) {
            String message = ChatUtil.translateAlternateColorCodes('&',
                    configManager.getPlayerIneligibleMessage()
            ).replace("%player%", target.getName());

            sender.sendMessage(message);
            return;
        }

        boolean hasPlayerData = PlayerDataManager.containsPlayerData(user);
        if (hasPlayerData) {
            if (CombatManager.getPlayers().contains(user)) {
                CombatManager.removePlayer(user);
            }
            PlayerDataManager.removePlayerData(user);
        } else {
            PlayerDataManager.addPlayerData(user, target);
        }

        String message = ChatUtil.translateAlternateColorCodes('&',
                hasPlayerData ? configManager.getPlayerDisableMessage() : configManager.
                        getPlayerEnableMessage()
        ).replace("%player%", target.getName());

        sender.sendMessage(message);
    }
}