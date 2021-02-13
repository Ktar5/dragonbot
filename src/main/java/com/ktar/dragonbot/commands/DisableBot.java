package com.ktar.dragonbot.commands;

import com.ktar.dragonbot.Main;
import com.ktar.dragonbot.commands.system.RegisterCommand;
import com.ktar.dragonbot.commands.system.RoleCommand;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

@RegisterCommand
public class DisableBot extends RoleCommand {
    public DisableBot() {
        super("disable", RoleName.FOUNDER, RoleName.OFFICER, RoleName.ADMIN);
    }

    @Override
    public String getHelp() {
        return "Prevents the bot from automatically sending messages, for use at various school breaks";
    }

    @Override
    public void act(MessageReceivedEvent event, String content) {
        Main.enabled = false;
        event.getMessage().reply("Bot is disabled from sending weekly announcements until you re-enable it with `.dbot enable`").queue();
    }
}
