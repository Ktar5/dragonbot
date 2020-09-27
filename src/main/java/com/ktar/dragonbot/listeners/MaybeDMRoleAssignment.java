package com.ktar.dragonbot.listeners;

import com.ktar.dragonbot.Const;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class MaybeDMRoleAssignment extends ListenerAdapter {
    private long roleMessageId = -1;

    @Override
    public void onGuildMessageReactionAdd(@NotNull GuildMessageReactionAddEvent event) {
        if (event.getReaction().getTextChannel() == null) {
            return;
        }

        if (roleMessageId == -1) {
            event.getReaction().getTextChannel().retrieveMessageById(event.getMessageId()).queue(message -> {
                Member poster = event.getGuild().getMember(message.getAuthor());
                if (poster != null && poster.getRoles().contains(event.getGuild().getRoleById(Const.ADMIN_ROLE))) {
                    if (message.getContentDisplay().contains(".dbot roleassign")) {
                        roleMessageId = message.getIdLong();
                        onGuildMessageReactionAdd(event);
                    }
                }
            });
        } else if (roleMessageId == event.getMessageIdLong()) {
            if (event.getReactionEmote().isEmoji() || !event.getReactionEmote().getEmote().getId().equals("682879741270687796")) {
                event.getReaction().removeReaction().queue();
                return;
            }

            Role roleById = event.getGuild().getRoleById(Const.MAYBE_DM_ROLE);
            if (roleById == null) {
                return;
            }
            event.getGuild().addRoleToMember(event.getMember(), roleById).queue();
        }
    }

    @Override
    public void onGuildMessageReactionRemove(@NotNull GuildMessageReactionRemoveEvent event) {
        if (event.getReaction().getTextChannel() == null) {
            return;
        }

        if (roleMessageId != -1) {
            Role roleById = event.getGuild().getRoleById(Const.MAYBE_DM_ROLE);
            if (roleById == null) {
                return;
            }
            if (event.getMember() == null) {
                return;
            }
            event.getGuild().removeRoleFromMember(event.getMember(), roleById).queue();
        }
    }

}
