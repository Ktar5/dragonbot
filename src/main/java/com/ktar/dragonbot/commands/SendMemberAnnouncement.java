package com.ktar.dragonbot.commands;

import com.ktar.dragonbot.Const;
import com.ktar.dragonbot.commands.back.RegisterCommand;
import com.ktar.dragonbot.commands.back.RoleCommand;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

@RegisterCommand
public class SendMemberAnnouncement extends RoleCommand {
    public SendMemberAnnouncement() {
        super("membercount", RoleName.ADMIN);
    }

    private static String announcement = "@everyone If you'll be attending next Monday's meeting, please add a reaction (please only one :broken_heart: ) to this post. " +
            "We're going to assign groups before-hand so that you know the DM, the level, and a summary of the one-shot you'll be participating in before hand! " +
            "And if you plan on bringing people from outside club, add a reaction with the number of people you'll be approximately bringing (ex :three: ).\n\n" +
            "Of course we also need DMs for next Monday, so if you'd like to be a DM for next week tag @Ktar! " +
            "We need approximately 3-4 DMs each week, so don't be shy!";

    @Override
    public void act(MessageReceivedEvent event, String content) {
        event.getGuild().getTextChannelById(Const.WEEKLY_CHANNEL).sendMessage(announcement).queue();
        event.getMessage().delete().queue();
    }
}
