package com.ktar.dragonbot.commands;

import com.ktar.dragonbot.Bot;
import com.ktar.dragonbot.Const;
import com.ktar.dragonbot.commands.system.RegisterCommand;
import com.ktar.dragonbot.commands.system.RoleAndChannelCommand;
import com.ktar.dragonbot.dnd.Party;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

@RegisterCommand
public class CompleteDMRegistrationCommand extends RoleAndChannelCommand {

    public CompleteDMRegistrationCommand() {
        super("register", new String[]{Const.DM_CHANNEL}, new RoleName[]{RoleName.DM});
    }

    @Override
    public String getHelp() {
        return "Complete registration for a weekly DM session [only works in the DM channel]";
    }

    @Override
    public void act(MessageReceivedEvent event, String content) {
        Guild guildById = Bot.get().getDiscord().getGuildById(Const.GUILD_ID);

        if (guildById == null) {
            throw new RuntimeException("Guild does not exist: " + Const.GUILD_ID);
        }

        if (event.getMember() == null) {
            return;
        }

        Party party = Bot.get().getGroupHandler().getGroups().get(event.getMember().getId());
//        if (party.isRegistrationComplete()) {
//            event.getMessage().reply("You already registered your group.").queue();
//            return;
//        }

        String[] split = content.split(" ", 2);
        if (split.length != 2) {
            event.getMessage().reply("This is an invalid command, please re-read the syntax in your welcome message.").queue();
            return;
        }

        if (split[0].length() > 2) {
            event.getMessage().reply("Please choose a level less (or equal to) 20, and greater than 0. Given: " + split[0] + ".").queue();
            return;
        }

        Integer level = getInteger(split[0]);
        if (level == null || level < 1 || level > 20) {
            event.getMessage().reply("Please choose a level less (or equal to) 20, and greater than 0. Given: " + split[0] + ".").queue();
            return;
        }

        if (!party.isRegistrationComplete()) {
            boolean complete = party.completeGroupRegistration(level, split[1]);
            if (complete) {
                event.getMessage().addReaction("U+2705").queue();
            }
        } else {
            party.editLevelAndDescription(level, split[1]);
            event.getMessage().addReaction("U+2705").queue();
        }

    }

    public Integer getInteger(String str) {
        if (str == null) {
            return null;
        }
        int length = str.length();
        if (length == 0) {
            return null;
        }
        int i = 0;
        if (str.charAt(0) == '-') {
            if (length == 1) {
                return null;
            }
            i = 1;
        }
        for (; i < length; i++) {
            char c = str.charAt(i);
            if (c < '0' || c > '9') {
                return null;
            }
        }
        return Integer.parseInt(str);
    }
}
