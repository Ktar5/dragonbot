package com.ktar.dragonbot.commands;

import com.ktar.dragonbot.Bot;
import com.ktar.dragonbot.Const;
import com.ktar.dragonbot.Main;
import com.ktar.dragonbot.commands.system.RegisterCommand;
import com.ktar.dragonbot.commands.system.RoleCommand;
import com.ktar.dragonbot.dnd.GroupHandler;
import com.ktar.dragonbot.dnd.Party;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.ErrorHandler;
import net.dv8tion.jda.api.requests.ErrorResponse;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

@RegisterCommand
public class SendGroupSurvey extends RoleCommand {
    public static long groupSurveyMessage = -1;

    public SendGroupSurvey() {
        super("groupsurvey", RoleName.FOUNDER, RoleName.OFFICER, RoleName.ADMIN);

        //Grab the latest weekly message id from the SQL database
        try {
            ResultSet resultSet = Main.database.querySQL("SELECT value FROM data_store WHERE key = 'group_survey_message'");
            while (resultSet.next()) {
                groupSurveyMessage = resultSet.getLong("value");
                break;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        //If the message id is NOT -1, check if message exists, if it does not, set to -1
        if (groupSurveyMessage != -1) {
            Guild guildById = Bot.get().getDiscord().getGuildById(Const.GUILD_ID);
            if (guildById != null) {
                TextChannel textChannelById = guildById.getTextChannelById(Const.WEEKLY_CHANNEL);
                if (textChannelById == null) {
                    return;
                }
                textChannelById.retrieveMessageById(groupSurveyMessage).queue(r -> {
                }, new ErrorHandler()
                    .handle(ErrorResponse.UNKNOWN_MESSAGE, e -> groupSurveyMessage = -1));
            }
        }


        Timer timer = new Timer();
        schedule(timer);
    }

    public void schedule(Timer timer) {
        LocalDateTime nextTime = LocalDateTime.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY)).with(LocalTime.of(10, 0));
        Date nextTimeDate = Date.from(nextTime.atZone(ZoneId.systemDefault()).toInstant());

        timer.schedule(
            new TimerTask() {
                @Override
                public void run() {
                    sendAnnouncement();
                    schedule(timer);
                }
            },
            nextTimeDate.getTime() - System.currentTimeMillis()
        );
    }

    private static final String YES_STRING = "<:yes:682879741270687796>";

    private static String announcement =
        "@everyone ***Group Picker for {{DATE}}***\n" +
            "**If you want to play this week, you have to react.**\n" +
            "(*Even if you are continuing a session from last week*)\n\n" +
            "Please react with the emoji corresponding to the group number in order to register for that group\n";

    @Override
    public String getHelp() {
        return "Display the group-picking survey";
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

    private void sendAnnouncement() {
        Guild guildById = Bot.get().getDiscord().getGuildById(Const.GUILD_ID);
        if (guildById == null) {
            throw new RuntimeException("Guild does not exist: " + Const.GUILD_ID);
        }

        TextChannel textChannelById = guildById.getTextChannelById(Const.WEEKLY_CHANNEL);
        if (textChannelById == null) {
            throw new RuntimeException("Channel does not exist: " + Const.WEEKLY_CHANNEL);
        }

        //Create the message
        StringBuilder builder = new StringBuilder();

        //Get date for the replacement string
        final ZonedDateTime nextClubMeeting = ZonedDateTime.now(ZoneId.of("America/Los_Angeles"))
            .with(TemporalAdjusters.next(DayOfWeek.THURSDAY));
        builder.append(announcement.replace("{{DATE}}", nextClubMeeting.format(DateTimeFormatter.ofPattern("E, LLL dd"))));

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

        //Delete old message
        textChannelById.deleteMessageById(groupSurveyMessage).queue();

        //Send new one and update
        Message complete = textChannelById.sendMessage(builder.toString()).complete();
        groupSurveyMessage = complete.getIdLong();
        try {
            Main.database.updateSQL("INSERT OR REPLACE INTO data_store (key, value) " +
                "VALUES ('group_survey_message', " + groupSurveyMessage + ")");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void act(MessageReceivedEvent event, String content) {
        sendAnnouncement();
    }
}
