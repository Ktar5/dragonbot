package com.ktar.dragonbot.commands.system;

import com.ktar.dragonbot.Bot;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.tinylog.Logger;

/**
 * This represents the command structure for executing commands from the bot
 */
public abstract class Command {
    /** this is the argument of the command, it should not have any spaces */
    public final String command;

    /**
     * @param command this is the argument of the command, it should not have any spaces
     */
    public Command(String command) {
        this.command = command;
    }

    /**
     * Register the command to the CommandHandler to let it be known to the system
     */
    private void registerCommand(){
        Logger.info("Registering command: " + command);
        Bot.get().getCommandHandler().registerCommand(this);
    }

    /**
     * @param event the MessageReceivedEvent that triggered the usage of this command
     * @return true if the command should execute based off this message event
     */
    public abstract boolean canUse(MessageReceivedEvent event);

    /**
     * @param event the MessageReceivedEvent that triggered the usage of this command
     * @param content the extra data included after the command, an empty string if none
     * @return true if the command was successfully used
     */
    public boolean attemptUse(MessageReceivedEvent event, String content){
        if(canUse(event)){
            act(event, content);
            return true;
        }
        return false;
    }

    /**
     * @param event the MessageReceivedEvent that triggered the usage of this command
     * @param content the extra data included after the command, an empty string if none
     * @return true if the command was successfully used
     */
    public abstract void act(MessageReceivedEvent event, String content);

    public static enum RoleName {
        ADMIN("admin"),
        DM("dm"),
        PLAYER("player"),
        EVERYONE("everyone");

        public final String roleName;
        RoleName(String roleName) {
            this.roleName = roleName;
        }
    }

}
