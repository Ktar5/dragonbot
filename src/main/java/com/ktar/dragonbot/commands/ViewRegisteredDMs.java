package com.ktar.dragonbot.commands;

import com.ktar.dragonbot.Bot;
import com.ktar.dragonbot.Const;
import com.ktar.dragonbot.Main;
import com.ktar.dragonbot.commands.system.RegisterCommand;
import com.ktar.dragonbot.commands.system.RoleAndChannelCommand;
import com.ktar.dragonbot.dnd.GroupHandler;
import com.ktar.dragonbot.dnd.Party;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.AbstractMap;
import java.util.Map;

@RegisterCommand
public class ViewRegisteredDMs extends RoleAndChannelCommand {

    public ViewRegisteredDMs() {
        super("displayregister", new String[]{Const.DM_CHANNEL}, new RoleName[]{RoleName.DM, RoleName.ADMIN, RoleName.OFFICER, RoleName.FOUNDER});
    }

    @Override
    public String getHelp() {
        return "Display the group-picking survey in this chat ";
    }

    private final Map<Integer, String> integerToWordMap = Map.ofEntries(
        new AbstractMap.SimpleEntry<>(0, "zero"),
        new AbstractMap.SimpleEntry<>(1, "one"),
        new AbstractMap.SimpleEntry<>(2, "two"),
        new AbstractMap.SimpleEntry<>(3, "three"),
        new AbstractMap.SimpleEntry<>(4, "four"),
        new AbstractMap.SimpleEntry<>(5, "five"),
        new AbstractMap.SimpleEntry<>(6, "six"),
        new AbstractMap.SimpleEntry<>(7, "seven"),
        new AbstractMap.SimpleEntry<>(8, "eight"),
        new AbstractMap.SimpleEntry<>(9, "nine")
    );

    @Override
    public void act(MessageReceivedEvent event, String content) {
        Guild guildById = Bot.get().getDiscord().getGuildById(Const.GUILD_ID);
        if (guildById == null) {
            throw new RuntimeException("Guild does not exist: " + Const.GUILD_ID);
        }

        TextChannel textChannelById = guildById.getTextChannelById(Const.DM_CHANNEL);
        if (textChannelById == null) {
            throw new RuntimeException("Channel does not exist: " + Const.DM_CHANNEL);
        }

        //Create the message
        StringBuilder builder = new StringBuilder();

        GroupHandler groupHandler = Bot.get().getGroupHandler();
        for (int i = 0; i < groupHandler.getGroupNumberToDmMap().size(); i++) {
            builder.append("\n:").append(integerToWordMap.get(i)).append(": ");
            String dmUserId = groupHandler.getGroupNumberToDmMap().get(i);
            Member memberById = guildById.getMemberById(dmUserId);
            if (memberById == null) {
                Bot.get().sendLogMessage("There was an error, member: " + dmUserId + " does not exist in group handling");
                continue;
            }
            builder.append("DM'd by ").append(memberById.getAsMention()).append(". Level ");
            Party party = groupHandler.getGroups().get(dmUserId);
            if (party == null) {
                Bot.get().sendLogMessage("There was an error, party for member: " + dmUserId + " does not exist in party map");
                continue;
            }
            if (!party.isRegistrationComplete()) {
                builder.append("No level provided by the DM. No description provided by the DM.");
            } else {
                builder.append(party.getLevel()).append(". ");
                builder.append("Description: ").append(party.getAdventureDescription());
            }
        }

        event.getMessage().reply(builder.toString()).queue();
    }

}
