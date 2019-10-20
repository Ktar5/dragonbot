package com.ktar.dragonbot.commands.back;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;

public abstract class RoleAndChannelCommand extends Command{
    public final String[] channels;
    public final RoleName[] roleNames;

    public RoleAndChannelCommand(String command, boolean autoRegisterCommand, String[] channels, RoleName[] roleNames) {
        super(command, autoRegisterCommand);
        this.roleNames = roleNames;
        this.channels = channels;
    }

    public RoleAndChannelCommand(String command, String[] channels, RoleName[] roleNames) {
        this(command, true, channels, roleNames);
    }

    @Override
    public boolean canUse(MessageReceivedEvent event) {
        return channelCheck(event) && roleCheck(event);
    }

    private boolean roleCheck(MessageReceivedEvent event){
        if(roleNames[0].equals(RoleName.EVERYONE)){
            return true;
        }
        List<net.dv8tion.jda.api.entities.Role> serverRoles = event.getMember().getRoles();
        for (net.dv8tion.jda.api.entities.Role role : serverRoles) {
            for (RoleName accepted : roleNames) {
                if(role.getName().equalsIgnoreCase(accepted.roleName)){
                    return true;
                }
            }
        }
        return false;
    }

    private boolean channelCheck(MessageReceivedEvent event){
        String channelName = event.getChannel().getName();
        for (String allowedChannelName : channels) {
            if(channelName.equalsIgnoreCase(allowedChannelName)){
                return true;
            }
        }
        return false;
    }
}
