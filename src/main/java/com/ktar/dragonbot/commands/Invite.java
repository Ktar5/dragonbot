package com.ktar.dragonbot.commands;

import com.ktar.dragonbot.commands.back.RegisterCommand;
import com.ktar.dragonbot.commands.back.RoleCommand;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

@RegisterCommand
public class Invite extends RoleCommand {
    public Invite() {
        super("invite", RoleName.EVERYONE);
    }

    @Override
    public void act(MessageReceivedEvent event, String content) {
        event.getChannel().sendMessage("Invite link to discord: https://discordapp.com/invite/tWeUm69").queue();
    }
}
