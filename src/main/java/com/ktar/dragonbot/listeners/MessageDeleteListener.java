package com.ktar.dragonbot.listeners;

import com.ktar.dragonbot.commands.SendMemberAnnouncement;
import net.dv8tion.jda.api.events.message.guild.GuildMessageDeleteEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class MessageDeleteListener extends ListenerAdapter {
    @Override
    public void onGuildMessageDelete(@NotNull GuildMessageDeleteEvent event) {
        SendMemberAnnouncement.messagesForWeeklyAnnouncements.remove(event.getMessageIdLong());
    }
}
