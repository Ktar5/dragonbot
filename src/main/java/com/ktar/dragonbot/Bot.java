package com.ktar.dragonbot;

import com.ktar.dragonbot.commands.Invite;
import com.ktar.dragonbot.commands.SendMemberAnnouncement;
import com.ktar.dragonbot.commands.back.CommandHandler;
import com.ktar.dragonbot.commands.back.CommandListener;
import com.ktar.dragonbot.dnd.GroupHandler;
import com.ktar.dragonbot.listeners.ReadyListener;
import com.ktar.dragonbot.listeners.RoleAssignmentListener;
import lombok.Getter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import org.tinylog.Logger;

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
        discord.addEventListener(new RoleAssignmentListener());

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
