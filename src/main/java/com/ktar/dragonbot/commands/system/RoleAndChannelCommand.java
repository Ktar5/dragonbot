package com.ktar.dragonbot.commands.system;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;

/**
 * A combination of RoleCommand and ChannelCommand such that you need to have a specific role and
 * send the command from a specific Channel
 */
public abstract class RoleAndChannelCommand extends Command {
    /**
     * An array of Strings representing the channel NAMES allowed
     */
    public final String[] channels;
    /**
     * An array of Strings representing the role NAMES allowed
     */
    public final RoleName[] roleNames;

    public RoleAndChannelCommand(String command, String[] channels, RoleName[] roleNames) {
        super(command);
        this.roleNames = roleNames;
        this.channels = channels;
    }

    /**
     * @param event the MessageReceivedEvent that triggered the usage of this command
     * @return true if the command should execute based off this message event
     */
    @Override
    public boolean canUse(MessageReceivedEvent event) {
        return channelCheck(event) && roleCheck(event);
    }

    /**
     * Checks if the user who sent the message in the MessageReceivedEvent (event) has
     * the proper role to use this command
     *
     * @param event the MessageReceivedEvent that caused this command to be triggered
     * @return true if the user in the MessageReceivedEvent (event) has one of the roles from {@link RoleAndChannelCommand#roleNames}
     */
    private boolean roleCheck(MessageReceivedEvent event) {
        if (roleNames[0].equals(RoleName.EVERYONE)) {
            return true;
        }
        List<net.dv8tion.jda.api.entities.Role> serverRoles = event.getMember().getRoles();
        for (net.dv8tion.jda.api.entities.Role role : serverRoles) {
            for (RoleName accepted : roleNames) {
                if (role.getName().equalsIgnoreCase(accepted.roleName)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Checks if the user who sent the message in the MessageReceivedEvent (event) sent the message from
     * a channel allowed by {@link RoleAndChannelCommand#channels}
     *
     * @param event the MessageReceivedEvent that caused this command to be triggered
     * @return true if the MessageReceivedEvent (event) came from a channel included in {@link RoleAndChannelCommand#channels}
     */
    private boolean channelCheck(MessageReceivedEvent event) {
        String channelName = event.getChannel().getName();
        for (String allowedChannelName : channels) {
            if (channelName.equalsIgnoreCase(allowedChannelName)) {
                return true;
            }
        }
        return false;
    }
}
