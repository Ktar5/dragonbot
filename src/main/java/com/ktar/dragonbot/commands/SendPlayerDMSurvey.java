package com.ktar.dragonbot.commands;

import com.ktar.dragonbot.Bot;
import com.ktar.dragonbot.Const;
import com.ktar.dragonbot.Main;
import com.ktar.dragonbot.commands.system.RegisterCommand;
import com.ktar.dragonbot.commands.system.RoleCommand;
import net.dv8tion.jda.api.entities.Guild;
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
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

@RegisterCommand
public class SendPlayerDMSurvey extends RoleCommand {
    public static long weeklyAnnouncementMessage = -1;

    public SendPlayerDMSurvey() {
        super("playercount", RoleName.FOUNDER, RoleName.OFFICER, RoleName.ADMIN);

        //Grab the latest weekly message id from the SQL database
        try {
            ResultSet resultSet = Main.database.querySQL("SELECT value FROM data_store WHERE key = 'weekly_announcement_message'");
            while (resultSet.next()) {
                weeklyAnnouncementMessage = resultSet.getLong("value");
                break;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        //If the message id is NOT -1, check if message exists, if it does not, set to -1
        if (weeklyAnnouncementMessage != -1) {
            Guild guildById = Bot.get().getDiscord().getGuildById(Const.GUILD_ID);
            if (guildById != null) {
                TextChannel textChannelById = guildById.getTextChannelById(Const.WEEKLY_CHANNEL);
                if (textChannelById == null) {
                    return;
                }
                textChannelById.retrieveMessageById(weeklyAnnouncementMessage).queue(r -> {
                }, new ErrorHandler()
                    .handle(ErrorResponse.UNKNOWN_MESSAGE, e -> weeklyAnnouncementMessage = -1));
            }
        }


        Timer timer = new Timer();
        schedule(timer);
    }

    public void schedule(Timer timer) {
        LocalDateTime nextTime = LocalDateTime.now().with(TemporalAdjusters.next(DayOfWeek.FRIDAY)).with(LocalTime.of(10, 0));
        Date nextTimeDate = Date.from(nextTime.atZone(ZoneId.systemDefault()).toInstant());

        timer.schedule(
            new TimerTask() {
                @Override
                public void run() {
                    Bot.get().getGroupHandler().clearDMs();
                    sendAnnouncement();
                    schedule(timer);
                }
            },
            nextTimeDate.getTime() - System.currentTimeMillis()
        );
    }

    private static final String YES_STRING = "<:yes:682879741270687796>";

    private static String announcement =
        "@everzyone ***Weekly Survey for {{DATE}}***\n" +
            "**If you want to play this week, you have to react.**\n" +
            "(*Even if you are continuing a session from last week*)\n\n" +
            "> Every week we put out this survey for figuring out how many DMs we need in comparison with how many " +
            "players we will have, for specifically the next week. Please read the instructions below.\n" +
            "If you would like to register as a DM (for this upcoming week) please react with :star: or if you " +
            "would like to be a player please react with <:yes:682879741270687796> Any reactions other than :star: " +
            "or <:yes:682879741270687796> will be removed automatically.\n" +
            "Be nice, DM once or twice. If you see significantly more players than ~5/DM, consider DMing!\n" +
            "We need approximately 2-4 DMs each week, so don't be shy!\n";

    @Override
    public String getHelp() {
        return "Display the weekly register message for players and DMs";
    }

    private void sendAnnouncement() {
        Guild guildById = Bot.get().getDiscord().getGuildById(Const.GUILD_ID);
        if (guildById == null) {
            throw new RuntimeException("Guild does not exist: " + Const.GUILD_ID);
        }

        TextChannel textChannelById = guildById.getTextChannelById(Const.WEEKLY_CHANNEL);
        if (textChannelById == null) {
            throw new RuntimeException("Channel does not exist: " + Const.WEEKLY_CHANNEL);
        }

        //Get date for the replacement string
        final ZonedDateTime nextClubMeeting = ZonedDateTime.now(ZoneId.of("America/Los_Angeles"))
            .with(TemporalAdjusters.next(DayOfWeek.THURSDAY));
        String weeklyAnnouncement = announcement.replace("{{DATE}}", nextClubMeeting.format(DateTimeFormatter.ofPattern("E, LLL dd")));

        //Delete old message
        textChannelById.deleteMessageById(weeklyAnnouncementMessage).queue();
        //Delete old message
        textChannelById.deleteMessageById(SendGroupSurvey.groupSurveyMessage).queue();

        //Send new one and update
        Message complete = textChannelById.sendMessage(weeklyAnnouncement).complete();
        weeklyAnnouncementMessage = complete.getIdLong();
        try {
            Main.database.updateSQL("INSERT OR REPLACE INTO data_store (key, value) " +
                "VALUES ('weekly_announcement_message', " + weeklyAnnouncementMessage + ")");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void act(MessageReceivedEvent event, String content) {
        sendAnnouncement();
    }
}
