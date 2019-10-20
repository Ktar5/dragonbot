package com.ktar.dragonbot.commands.back;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public abstract class ChannelCommand extends Command {
    public final String[] channelsAllowed;

    public ChannelCommand(String command, boolean autoRegisterCommand, String... channelsAllowed) {
        super(command, autoRegisterCommand);
        this.channelsAllowed = channelsAllowed;
    }

    public ChannelCommand(String command, String... channelsAllowed) {
        this(command, true, channelsAllowed);
    }

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
