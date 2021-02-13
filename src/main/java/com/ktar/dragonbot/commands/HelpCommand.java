package com.ktar.dragonbot.commands;

import com.ktar.dragonbot.Bot;
import com.ktar.dragonbot.commands.system.Command;
import com.ktar.dragonbot.commands.system.RegisterCommand;
import com.ktar.dragonbot.commands.system.RoleCommand;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.HashMap;
import java.util.Map;

@RegisterCommand
public class HelpCommand extends RoleCommand {
    public HelpCommand() {
        super("help", RoleName.OFFICER, RoleName.FOUNDER, RoleName.ADMIN);
    }

    @Override
    public String getHelp() {
        return "Display the help message";
    }

    @Override
    public void act(MessageReceivedEvent event, String content) {
        StringBuilder commandString = new StringBuilder("The Following commands are available: \n");

        HashMap<String, Command> commands = Bot.get().getCommandHandler().getCommands();
        for (Map.Entry<String, Command> stringCommandEntry : commands.entrySet()) {
            commandString.append(stringCommandEntry.getKey()).append(" - ").append(stringCommandEntry.getValue().getHelp())
                .append("\n");
        }

        event.getMessage().reply(commandString.toString()).queue();
    }
}
