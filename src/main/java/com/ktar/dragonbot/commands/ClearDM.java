package com.ktar.dragonbot.commands;

import com.ktar.dragonbot.Bot;
import com.ktar.dragonbot.commands.system.RegisterCommand;
import com.ktar.dragonbot.commands.system.RoleCommand;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

@RegisterCommand
public class ClearDM extends RoleCommand {
    public ClearDM() {
        super("cleardm", RoleName.FOUNDER, RoleName.OFFICER, RoleName.ADMIN);
    }

    @Override
    public String getHelp() {
        return "Clears the DM role and sends thank you messages";
    }

    @Override
    public void act(MessageReceivedEvent event, String content) {
        Bot.get().getGroupHandler().clearDMs();
        event.getMessage().reply("Cleared DM Role successfully").queue();
    }
}
