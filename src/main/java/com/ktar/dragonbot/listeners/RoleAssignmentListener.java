package com.ktar.dragonbot.listeners;

import com.ktar.dragonbot.Bot;
import com.ktar.dragonbot.Const;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;

/**
 * This listener looks for if we have a guild member receive the WEEKLY_DM_ROLE role, and if they do
 * remove it and create a weekly DM group for that user
 */
public class RoleAssignmentListener extends ListenerAdapter {

    @Override
    public void onGuildMemberRoleAdd(@Nonnull GuildMemberRoleAddEvent event) {
//        for (Role role : event.getRoles()) {
//            if(role.getId().equalsIgnoreCase(Const.WEEKLY_DM_ROLE)){
//                Bot.get().getGroupHandler().assignWeeklyDM(event.getMember().getId());
//                return;
//            }
//        }
    }

    @Override
    public void onGuildMemberRoleRemove(@Nonnull GuildMemberRoleRemoveEvent event) {

    }
}
