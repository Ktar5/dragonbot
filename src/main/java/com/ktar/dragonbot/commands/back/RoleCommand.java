package com.ktar.dragonbot.commands.back;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;

public abstract class RoleCommand extends Command {
    public final RoleName[] rolesAccepted;

    public RoleCommand(String command, boolean autoRegisterCommand, RoleName... rolesAccepted) {
        super(command, autoRegisterCommand);
        this.rolesAccepted = rolesAccepted;
    }

    public RoleCommand(String command, RoleName... rolesAccepted) {
        this(command, true, rolesAccepted);
    }

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
