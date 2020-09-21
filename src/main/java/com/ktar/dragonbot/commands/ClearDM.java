package com.ktar.dragonbot.commands;

import com.ktar.dragonbot.Const;
import com.ktar.dragonbot.commands.system.RegisterCommand;
import com.ktar.dragonbot.commands.system.RoleCommand;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;

@RegisterCommand
public class ClearDM extends RoleCommand {
    public ClearDM() {
        super("cleardm", RoleName.ADMIN);
    }

    @Override
    public void act(MessageReceivedEvent event, String content) {
        Role role = event.getGuild().getRoleById(Const.DM_ROLE);
        List<Member> membersWithRoles = event.getGuild().getMembersWithRoles(role);
        for (Member member : membersWithRoles) {
            event.getGuild().removeRoleFromMember(member, role).queue();
            member.getUser().openPrivateChannel().queue(privateChannel -> {
                privateChannel.sendMessage("Thanks for participating in club as a DM this past week, it's super appreciated!\n" +
                        "If you're up for doing it again next week, hit up Ktar / Carter in Discord!\n\n" +
                        "Once again, thanks!").queue();
            });
        }
    }
}
