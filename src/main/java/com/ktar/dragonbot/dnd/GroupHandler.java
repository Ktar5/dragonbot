package com.ktar.dragonbot.dnd;

import com.ktar.dragonbot.Bot;
import com.ktar.dragonbot.Const;
import lombok.Getter;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.util.HashMap;
import java.util.Map;

@Getter
public class GroupHandler {
    //DM id, Party
    private final Map<String, Party> groups;
//    private final Set<String> removedRoleProgramatically;

    public GroupHandler() {
        groups = new HashMap<>();
//        removedRoleProgramatically = new HashSet<>();
    }

    public void setWeekDM(String id) {
        if (groups.containsKey(id)) {
            return;
        }
        groups.put(id, new Party(id));

        User userById = Bot.get().getDiscord().getUserById(id);

        Guild guild = Bot.get().getDiscord().getGuildById(Const.GUILD_ID);
        Member member = guild.getMember(userById);
        guild.addRoleToMember(member, guild.getRoleById(Const.DM_ROLE));
//        TextChannel dm_channel = guild.getTextChannelById(Const.DM_CHANNEL);
        userById.openPrivateChannel().queue((channel) -> {
            channel.sendMessage(new MessageBuilder()
                    .append("Hey ").append(member)
                    .append("! Welcome to being a weekly DM, this bot is still a work in progress, so it can't do much yet. " +
                            "Please make sure that you bring all the materials that you have, and tag @Ktar in Discord if you need any materials (DM screen, dice, markers, paper, etc.). " +
                            "Also, make sure to have your one-shot level, two-sentence summary, and RL name posted to **#dm-chat**, not #on-topic **before** Friday. " +
                            "So yeah, have fun come Monday!").build()).queue();
        });


        if (userById == null) {
            Bot.get().sendLogMessage("Created group with user: '" + id + "' as the DM.");
        } else
            Bot.get().sendLogMessage("Created group with user: '" + userById.getName() + "' as the DM.");
    }

}
