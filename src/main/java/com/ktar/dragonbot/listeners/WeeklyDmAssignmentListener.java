package com.ktar.dragonbot.listeners;

import com.ktar.dragonbot.Bot;
import com.ktar.dragonbot.Const;
import com.ktar.dragonbot.commands.SendPlayerDMSurvey;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.GuildMessageDeleteEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionRemoveAllEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.tinylog.Logger;

public class WeeklyDmAssignmentListener extends ListenerAdapter {
    @Override
    public void onGuildMessageReactionAdd(@NotNull GuildMessageReactionAddEvent event) {
        if (SendPlayerDMSurvey.weeklyAnnouncementMessage != event.getMessageIdLong()) {
            return;
        }

        if (event.getReactionEmote().isEmoji()) {
            String format = String.format("U+%04X", event.getReactionEmote().getEmoji().codePointAt(0));
            if (!format.equals("U+2B50")) {
                event.getReaction().removeReaction().queue();
            } else {
                Bot.get().getGroupHandler().assignWeeklyDM(event.getMember().getId());
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
        if (SendPlayerDMSurvey.weeklyAnnouncementMessage != event.getMessageIdLong()) {
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
                Bot.get().getGroupHandler().unregisterGroup(event.getMember().getId());
            }
        }
    }

    @Override
    public void onGuildMessageReactionRemoveAll(@NotNull GuildMessageReactionRemoveAllEvent event) {
//        Role role = event.getGuild().getRoleById(Const.DM_ROLE);
//        List<Member> membersWithRoles = event.getGuild().getMembersWithRoles(role);
//        for (Member member : membersWithRoles) {
//            event.getGuild().removeRoleFromMember(member, role).queue();
//            member.getUser().openPrivateChannel().queue(privateChannel -> {
//                privateChannel.sendMessage("Thanks for participating in club as a DM this past week, it's super appreciated!\n" +
//                    "If you're up for doing it again next week, hit us up in the Discord!\n\n" +
//                    "Once again, thanks!").queue();
//            });
//        }
    }

    @Override
    public void onGuildMessageDelete(@NotNull GuildMessageDeleteEvent event) {
//        if (SendMemberAnnouncement.weeklyAnnouncementMessage == event.getMessageIdLong()) {
//            Role role = event.getGuild().getRoleById(Const.DM_ROLE);
//            List<Member> membersWithRoles = event.getGuild().getMembersWithRoles(role);
//            for (Member member : membersWithRoles) {
//                event.getGuild().removeRoleFromMember(member, role).queue();
//                member.getUser().openPrivateChannel().queue(privateChannel -> {
//                    privateChannel.sendMessage("Thanks for participating in club as a DM this past week, it's super appreciated!\n" +
//                        "If you're up for doing it again next week, hit us up in the Discord!\n\n" +
//                        "Once again, thanks!").queue();
//                });
//            }
//        }
    }

}
