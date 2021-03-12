package com.ktar.dragonbot.dnd;

import com.ktar.dragonbot.Bot;
import com.ktar.dragonbot.Const;
import lombok.Getter;
import net.dv8tion.jda.api.entities.*;

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

    public void assignWeeklyDM(String id) {
        if (groups.containsKey(id)) {
            return;
        }

        User userById = Bot.get().getDiscord().getUserById(id);
        if (userById == null) {
            throw new NullPointerException();
        }

        Guild guild = Bot.get().getDiscord().getGuildById(Const.GUILD_ID);
        if (guild == null) {
            throw new NullPointerException();
        }

        Member member = guild.getMember(userById);
        if (member == null) {
            throw new NullPointerException();
        }

        groups.put(id, new Party(id));
        groupNumberToDmMap.add(id);

        guild.addRoleToMember(member, guild.getRoleById(Const.DM_ROLE)).queue();
        TextChannel dm_channel = guild.getTextChannelById(Const.DM_CHANNEL);
        dm_channel.sendMessage("Thanks for DMing this week, " + member.getAsMention() + "! " +
            "Please register your adventure ***before Monday*** using the following DragonBot command in this channel:\n" +
            "`.dbot register <character level> <adventure description>`\n" +
            "Example Command: `.dbot register 7 The party finds themselves on an adventure`").queue();

        Bot.get().sendLogMessage("Created group with user: '" + userById.getName() + "' as the DM.");
    }

}
