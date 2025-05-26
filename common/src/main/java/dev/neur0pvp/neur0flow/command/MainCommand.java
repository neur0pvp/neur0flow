package dev.neur0pvp.neur0flow.command;

import dev.neur0pvp.neur0flow.command.generic.BuilderCommand;
import dev.neur0pvp.neur0flow.sender.Sender;
import dev.neur0pvp.neur0flow.util.ChatUtil;
import org.incendo.cloud.CommandManager;

public class MainCommand implements BuilderCommand {
    public void register(CommandManager<Sender> manager) {
        manager.command(
                manager.commandBuilder("neur0flow", "kbsync", "kbs")
                        .handler(context -> {
                            context.sender().sendMessage(
                                    ChatUtil.translateAlternateColorCodes(
                                            '&',
                                            "&6This server is running the &eneur0flow &6plugin. &bhttps://github.com/CASELOAD7000/knockback-sync"
                                    )
                            );
                        })
        );
    }
}
