package com.ktar.dragonbot.commands.back;

import com.ktar.dragonbot.Bot;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.tinylog.Logger;

public abstract class Command {
    public final String command;

    public Command(String command, boolean autoRegisterCommand) {
        this.command = command;
        if(autoRegisterCommand){
            registerCommand();
        }
    }

    public Command(String command){
        this(command, true);
    }

    private void registerCommand(){
        Logger.info("Registering command: " + command);
        Bot.get().getCommandHandler().registerCommand(this);
    }

    public abstract boolean canUse(MessageReceivedEvent event);

    public boolean attemptUse(MessageReceivedEvent event, String content){
        if(canUse(event)){
            act(event, content);
            return true;
        }
        return false;
    }

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
