package com.ktar.dragonbot.commands;

import com.ktar.dragonbot.Main;
import com.ktar.dragonbot.commands.system.RegisterCommand;
import com.ktar.dragonbot.commands.system.RoleCommand;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

@RegisterCommand
public class EnableBot extends RoleCommand {
    public EnableBot() {
        super("enable", RoleName.FOUNDER, RoleName.OFFICER, RoleName.ADMIN);
    }

    @Override
    public String getHelp() {
        return "Re-enable sending weekly announcements";
    }

    @Override
    public void act(MessageReceivedEvent event, String content) {
        Main.enabled = true;
        event.getMessage().reply("Bot is enabled and will now send weekly announcements until you disable it with `.dbot disable`").queue();
    }
}
