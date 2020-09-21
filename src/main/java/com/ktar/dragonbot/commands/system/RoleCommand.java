package com.ktar.dragonbot.commands.system;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;

/**
 * Represents a command that requires the user to have been assigned one of the roles included in {@link RoleCommand#rolesAccepted}
 */
public abstract class RoleCommand extends Command {
    /**A list of role NAMES that are able to execute this command*/
    public final RoleName[] rolesAccepted;

    public RoleCommand(String command, RoleName... rolesAccepted) {
        super(command);
        this.rolesAccepted = rolesAccepted;
    }

    /**
     * Checks if the user who sent the message in the MessageReceivedEvent (event) has
     * the proper role to use this command
     * @param event the MessageReceivedEvent that caused this command to be triggered
     * @return true if the user in the MessageReceivedEvent (event) has one of the roles from {@link RoleCommand#rolesAccepted}
     */
    @Override
    public boolean canUse(MessageReceivedEvent event) {
        if(rolesAccepted == null || rolesAccepted[0].equals(RoleName.EVERYONE)){
            return true;
        }

        List<net.dv8tion.jda.api.entities.Role> roles = event.getMember().getRoles();
        for (net.dv8tion.jda.api.entities.Role role : roles) {
            for (RoleName accepted : rolesAccepted) {
                if(role.getName().equalsIgnoreCase(accepted.roleName)){
                    return true;
                }
            }
        }
        return false;
    }
}
