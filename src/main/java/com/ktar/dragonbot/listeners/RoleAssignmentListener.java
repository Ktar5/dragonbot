package com.ktar.dragonbot.listeners;

import com.ktar.dragonbot.Bot;
import com.ktar.dragonbot.Const;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;

public class RoleAssignmentListener extends ListenerAdapter {

    @Override
    public void onGuildMemberRoleAdd(@Nonnull GuildMemberRoleAddEvent event) {
        for (Role role : event.getRoles()) {
            if(role.getId().equalsIgnoreCase(Const.WEEKLY_DM_ROLE)){
                Bot.get().getGroupHandler().setWeekDM(event.getMember().getId());
                event.getMember().getGuild().removeRoleFromMember(event.getMember(), role).queue();
                Bot.get().sendLogMessage("Weekly DM assigned to: " + event.getMember().getEffectiveName());
                return;
            }
        }
    }

    @Override
    public void onGuildMemberRoleRemove(@Nonnull GuildMemberRoleRemoveEvent event) {

    }
}
