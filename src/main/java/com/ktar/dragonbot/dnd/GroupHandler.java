package com.ktar.dragonbot.dnd;

import com.ktar.dragonbot.Bot;
import com.ktar.dragonbot.Const;
import lombok.Getter;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class GroupHandler {
    //DM user id, Party
    private final Map<String, Party> groups;

    //DM user id, the index is the group #
    private final List<String> groupNumberToDmMap;

    public GroupHandler() {
        groups = new HashMap<>();
        groupNumberToDmMap = new ArrayList<>();
    }

    public void clearDMs() {
        for (Party value : groups.values()) {
            value.preDelete();
        }

        Guild guild = Bot.get().getDiscord().getGuildById(Const.GUILD_ID);
        if (guild == null) {
            throw new NullPointerException("Guild cannot be null, guild id: " + Const.GUILD_ID);
        }

        Role role = guild.getRoleById(Const.DM_ROLE);
        List<Member> membersWithRoles = guild.getMembersWithRoles(role);
        for (Member member : membersWithRoles) {
            guild.removeRoleFromMember(member, role).queue();
            member.getUser().openPrivateChannel().queue(privateChannel -> {
                privateChannel.sendMessage("Thanks for participating in club as a DM this past week, it's super appreciated!\n" +
                    "If you're up for doing it again next week, hit us up in Discord!\n\n" +
                    "Once again, thanks!").queue();
            });
        }

        groupNumberToDmMap.clear();
        groups.clear();
    }

    public void unregisterGroup(String dm_id) {
        Party party = groups.get(dm_id);
        if (party == null) {
            return;
        }
        party.preDelete();
        groups.remove(dm_id);
        groupNumberToDmMap.remove(dm_id);
    }

    public void assignWeeklyDM(String userId) {
        if (groups.containsKey(userId)) {
            return;
        }

        Guild guild = Bot.get().getDiscord().getGuildById(Const.GUILD_ID);
        if (guild == null) {
            throw new NullPointerException();
        }

        guild.retrieveMemberById(userId).queue(ret -> {
            groups.put(userId, new Party(userId));
            groupNumberToDmMap.add(userId);

            guild.addRoleToMember(ret.getUser().getId(), guild.getRoleById(Const.DM_ROLE)).queue();
            TextChannel dm_channel = guild.getTextChannelById(Const.DM_CHANNEL);
            if (dm_channel == null) {
                Bot.get().sendLogMessage("The DM Channel was null");
                return;
            }
            dm_channel.sendMessage("Thanks for DMing this week, " + ret.getUser().getAsMention() + "! " +
                "Please register your adventure ***before Monday*** using the following DragonBot command in this channel:\n" +
                "`.dbot register <character level> <adventure description>`. If you would like to edit your level and description," +
                " simply run the command again before the next survey goes out on Monday.\n" +
                "Example Command: `.dbot register 7 The party finds themselves on an adventure`").queue();

            Bot.get().sendLogMessage("Created group with user: '" + ret.getUser().getName() + "' as the DM.");
        });

    }

}
