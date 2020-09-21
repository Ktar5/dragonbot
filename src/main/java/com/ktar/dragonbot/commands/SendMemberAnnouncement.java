package com.ktar.dragonbot.commands;

import com.ktar.dragonbot.Const;
import com.ktar.dragonbot.commands.system.RegisterCommand;
import com.ktar.dragonbot.commands.system.RoleCommand;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.HashSet;

@RegisterCommand
public class SendMemberAnnouncement extends RoleCommand {
    public static final HashSet<Long> messagesForWeeklyAnnouncements = new HashSet<>();

    public SendMemberAnnouncement() {
        super("membercount", RoleName.ADMIN);
    }

    private static final String YES_STRING = "<:yes:682879741270687796>";

    private static String announcement =
        "@everyone ***Weekly Survey***\n" +
            "> Every week we put out this survey for figuring out how many DMs we need in comparison with how many players we will have. Please read the instructions below.\n" +
            "If you would like to register as a DM please react with :star: or if you would like to be a player please react with <:yes:682879741270687796> Any reactions other than :star: or <:yes:682879741270687796> will be removed automatically.\n" +
            "Be nice, DM once or twice. If you see significantly more players than ~5/DM, consider DMing!\n" +
            "We need approximately 3-4 DMs each week, so don't be shy!\n";

    @Override
    public void act(MessageReceivedEvent event, String content) {
        TextChannel textChannelById = event.getGuild().getTextChannelById(Const.WEEKLY_CHANNEL);
        if (textChannelById == null) {
            throw new RuntimeException("Channel does not exist: " + Const.WEEKLY_CHANNEL);
        }
        Message complete = textChannelById.sendMessage(announcement).complete();
        long idLong = complete.getIdLong();
        messagesForWeeklyAnnouncements.add(idLong);
        event.getMessage().delete().queue();
    }
}
