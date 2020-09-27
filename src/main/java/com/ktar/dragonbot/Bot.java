package com.ktar.dragonbot;

import com.ktar.dragonbot.commands.system.CommandHandler;
import com.ktar.dragonbot.commands.system.CommandListener;
import com.ktar.dragonbot.dnd.GroupHandler;
import com.ktar.dragonbot.listeners.MaybeDMRoleAssignment;
import com.ktar.dragonbot.listeners.WeeklyDmAssignmentListener;
import com.ktar.dragonbot.listeners.ReadyListener;
import com.ktar.dragonbot.listeners.RoleAssignmentListener;
import lombok.Getter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import org.tinylog.Logger;

/**
 * This serves as the headquarters for the logic of the Bot. Basically everything except creating the bot itself is
 * done from this class or from a field in this class.
 */

@Getter
public class Bot {
    private static Bot instance;

    private final JDA discord;
    private final ReadyListener readyListener;
    private final CommandListener commandListener;
    private final CommandHandler commandHandler;
    private final GroupHandler groupHandler;

    public Bot(JDA discord, ReadyListener readyListener) {
        this.discord = discord;
        this.readyListener = readyListener;

        instance = this;

        this.commandListener = new CommandListener();
        discord.addEventListener(commandListener);
        discord.addEventListener(new WeeklyDmAssignmentListener());
        discord.addEventListener(new RoleAssignmentListener());
        discord.addEventListener(new MaybeDMRoleAssignment());

        this.commandHandler = new CommandHandler();
        this.groupHandler = new GroupHandler();

        commandHandler.autoRegisterCommands();
    }

    public static Bot get(){
        return instance;
    }

    public void sendLogMessage(String message){
        Guild guildById = discord.getGuildById(Const.GUILD_ID);
        if(guildById == null){
            Logger.error("Spartan D&D discord channel is null, id: " + Const.GUILD_ID);
            return;
        }
        TextChannel textChannelById = guildById.getTextChannelById(Const.BOT_LOG_CHANNEL);
        if (textChannelById == null) {
            Logger.error("Bot log channel is null, id: " + Const.BOT_LOG_CHANNEL);
            return;
        }
        textChannelById.sendMessage(message).queue();
    }

}
