package com.ktar.dragonbot.commands.system;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

/**
 * Represents a command that requires the message to be sent from one of the specified channels {@link ChannelCommand#channelsAllowed}
 */
public abstract class ChannelCommand extends Command {
    /** A string list of channel NAMES that are allowed to have this command used in them by anyone*/
    public final String[] channelsAllowed;

    public ChannelCommand(String command, String... channelsAllowed) {
        super(command);
        this.channelsAllowed = channelsAllowed;
    }

    /**
     *
     * @param event the MessageReceivedEvent that triggered the usage of this command
     * @return true if the user is using the command from one of the allowed channels
     */
    @Override
    public boolean canUse(MessageReceivedEvent event) {
        String channelName = event.getChannel().getName();
        if(channelsAllowed == null){
            return true;
        }
        for (String allowedChannelName : channelsAllowed) {
            if(channelName.equalsIgnoreCase(allowedChannelName)){
                return true;
            }
        }
        return false;
    }
}
