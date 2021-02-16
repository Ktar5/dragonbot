package com.ktar.dragonbot.listeners;

import com.ktar.dragonbot.Const;
import com.ktar.dragonbot.commands.SendMemberAnnouncement;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.GuildMessageDeleteEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionRemoveAllEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.tinylog.Logger;

import java.util.List;

public class WeeklyDmAssignmentListener extends ListenerAdapter {
    @Override
    public void onGuildMessageReactionAdd(@NotNull GuildMessageReactionAddEvent event) {
        if (SendMemberAnnouncement.weeklyAnnouncementMessage != event.getMessageIdLong()) {
            return;
        }

        if (event.getReactionEmote().isEmoji()) {
            String format = String.format("U+%04X", event.getReactionEmote().getEmoji().codePointAt(0));
            if (!format.equals("U+2B50")) {
                event.getReaction().removeReaction().queue();
            } else {
                Role dmRole = event.getGuild().getRoleById(Const.DM_ROLE);
                if (dmRole == null) {
                    Logger.error("DM_ROLE Const is not working with id: " + Const.DM_ROLE);
                    return;
                }
                event.getGuild().addRoleToMember(event.getMember(), dmRole).queue();
                event.getMember().getUser().openPrivateChannel().queue(t -> {
                    t.sendMessage("Thank you for signing up to be a dungeon master this week. Please make sure to notify" +
                        " the president of club *as soon as possible* if you end up not being able to make it, though please try your" +
                        " best to make it. \n\n" +
                        "Please make sure to post your one-shot level, real first name, and a short one-sentence description of your one-shot as soon" +
                        " as possible in the #dm-chat. " +
                        "\n\nThanks again!").queue();
                });
            }
        } else if (event.getReactionEmote().isEmote()) {
            if (!event.getReactionEmote().getEmote().getId().equals("682879741270687796")) {
                event.getReaction().removeReaction().queue();
                event.getMember().getUser().openPrivateChannel().queue(t -> {
                    t.sendMessage("Please only react with the `:yes:` emoji or the `:star:` emoji. Thank you.").queue();
                });
            }
        }
    }

    @Override
    public void onGuildMessageReactionRemove(@NotNull GuildMessageReactionRemoveEvent event) {
        if (SendMemberAnnouncement.weeklyAnnouncementMessage != event.getMessageIdLong()) {
            return;
        }

        if (event.getReactionEmote().isEmoji()) {
            String format = String.format("U+%04X", event.getReactionEmote().getEmoji().codePointAt(0));
            if (format.equals("U+2B50")) {
                if (event.getMember() == null) {
                    return;
                }
                Role dmRole = event.getGuild().getRoleById(Const.DM_ROLE);
                if (dmRole == null) {
                    Logger.error("DM_ROLE Const is not working with id: " + Const.DM_ROLE);
                    return;
                }
                event.getGuild().removeRoleFromMember(event.getMember(), dmRole).queue();
            }
        }
    }

    @Override
    public void onGuildMessageReactionRemoveAll(@NotNull GuildMessageReactionRemoveAllEvent event) {
        Role role = event.getGuild().getRoleById(Const.DM_ROLE);
        List<Member> membersWithRoles = event.getGuild().getMembersWithRoles(role);
        for (Member member : membersWithRoles) {
            event.getGuild().removeRoleFromMember(member, role).queue();
            member.getUser().openPrivateChannel().queue(privateChannel -> {
                privateChannel.sendMessage("Thanks for participating in club as a DM this past week, it's super appreciated!\n" +
                    "If you're up for doing it again next week, hit us up in the Discord!\n\n" +
                    "Once again, thanks!").queue();
            });
        }
    }

    @Override
    public void onGuildMessageDelete(@NotNull GuildMessageDeleteEvent event) {
        if (SendMemberAnnouncement.weeklyAnnouncementMessage == event.getMessageIdLong()) {
            Role role = event.getGuild().getRoleById(Const.DM_ROLE);
            List<Member> membersWithRoles = event.getGuild().getMembersWithRoles(role);
            for (Member member : membersWithRoles) {
                event.getGuild().removeRoleFromMember(member, role).queue();
                member.getUser().openPrivateChannel().queue(privateChannel -> {
                    privateChannel.sendMessage("Thanks for participating in club as a DM this past week, it's super appreciated!\n" +
                        "If you're up for doing it again next week, hit us up in the Discord!\n\n" +
                        "Once again, thanks!").queue();
                });
            }
        }
    }

}
