package dev.neur0pvp.neur0flow.command.generic;

import dev.neur0pvp.neur0flow.sender.Sender;
import org.incendo.cloud.CommandManager;

public interface BuilderCommand {

    void register(CommandManager<Sender> manager);
}
