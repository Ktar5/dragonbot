package com.ktar.dragonbot.listeners;

import com.ktar.dragonbot.commands.SendMemberAnnouncement;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionRemoveAllEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class ReactionListener extends ListenerAdapter {
    @Override
    public void onGuildMessageReactionAdd(@NotNull GuildMessageReactionAddEvent event) {
        if (!SendMemberAnnouncement.messagesForWeeklyAnnouncements.contains(event.getMessageIdLong())) {
            return;
        }

        if (event.getReactionEmote().isEmoji()) {
            String format = String.format("U+%04X", event.getReactionEmote().getEmoji().codePointAt(0));
            if (!format.equals("U+2B50")) {
                event.getReaction().removeReaction().queue();
            }
        } else if (event.getReactionEmote().isEmote()) {
            if (!event.getReactionEmote().getEmote().getId().equals("682879741270687796")) {
                event.getReaction().removeReaction().queue();
            }
            event.getMember().getUser().openPrivateChannel().queue(t -> {
               t.sendMessage("Please only react with the `:yes:` emoji or the `:star:` emoji. Thank you.").queue();
            });
        }
    }

    @Override
    public void onGuildMessageReactionRemove(@NotNull GuildMessageReactionRemoveEvent event) {
        super.onGuildMessageReactionRemove(event);
    }

    @Override
    public void onGuildMessageReactionRemoveAll(@NotNull GuildMessageReactionRemoveAllEvent event) {
        super.onGuildMessageReactionRemoveAll(event);
    }

}
