package com.ktar.dragonbot.commands;

import com.ktar.dragonbot.commands.system.RegisterCommand;
import com.ktar.dragonbot.commands.system.RoleCommand;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

/**
 * Simply post the invite link to the Discord chat
 */
@RegisterCommand
public class Invite extends RoleCommand {
    public Invite() {
        super("invite", RoleName.EVERYONE);
    }

    @Override
    public String getHelp() {
        return "Displays the invite link";
    }

    @Override
    public void act(MessageReceivedEvent event, String content) {
        event.getMessage().reply("Invite link to discord: https://discordapp.com/invite/tWeUm69").queue();
    }
}
