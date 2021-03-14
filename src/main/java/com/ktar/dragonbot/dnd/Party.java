package com.ktar.dragonbot.dnd;

import com.ktar.dragonbot.Bot;
import com.ktar.dragonbot.Const;
import lombok.Getter;
import net.dv8tion.jda.api.entities.*;

import java.util.HashSet;
import java.util.Set;

@Getter
public class Party {
    private int level;
    private String adventureDescription;
    private String textChannelId;
    private String voiceChannelId;
    private boolean registrationComplete = false;

    private final String dm_id;
    private final Set<String> partyMemberIds;

    public Party(String dm_id) {
        this.dm_id = dm_id;
        partyMemberIds = new HashSet<>();
    }

    public void addPartyMember(String userId) {
        User userById = Bot.get().getDiscord().getUserById(userId);
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
    }

    public void editLevelAndDescription(int level, String adventureDescription) {
        this.level = level;
        this.adventureDescription = adventureDescription;
    }

    public boolean completeGroupRegistration(int level, String adventureDescription) {
        this.level = level;
        this.adventureDescription = adventureDescription;

        Guild guild = Bot.get().getDiscord().getGuildById(Const.GUILD_ID);
        if (guild == null) {
            throw new NullPointerException();
        }

        Member member = guild.retrieveMemberById(dm_id).complete();
        if (member == null) {
            Bot.get().sendLogMessage("Yikes, for some reason we can't find a member with the ID: " + dm_id);
            return false;
        }

        Category category = guild.getCategoryById(Const.GROUPS_CATEGORY);

        guild.createTextChannel(member.getEffectiveName() + "'s Group", category).queue(ret -> {
            this.textChannelId = ret.getId();
        });
        guild.createVoiceChannel(member.getEffectiveName() + "'s VC", category).queue(ret -> {
            this.voiceChannelId = ret.getId();
        });

        registrationComplete = true;
        return true;
    }

    public void preDelete() {
        Guild guild = Bot.get().getDiscord().getGuildById(Const.GUILD_ID);
        if (guild == null) {
            throw new NullPointerException();
        }
        if (textChannelId != null) {
            TextChannel textChannel = guild.getTextChannelById(textChannelId);
            if (textChannel != null) {
                textChannel.delete().queue();
            }
        }
        if (voiceChannelId != null) {
            VoiceChannel voiceChannel = guild.getVoiceChannelById(voiceChannelId);
            if (voiceChannel != null) {
                voiceChannel.delete().queue();
            }
        }
    }
}
