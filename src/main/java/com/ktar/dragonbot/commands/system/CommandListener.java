package com.ktar.dragonbot.commands.system;

import com.ktar.dragonbot.Bot;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;

/**
 * Listens to all messages received from any chat source and delegates the information to
 * the CommandHandler for processing
 */
public class CommandListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        if (event.getMessage().getContentDisplay().startsWith(".dbot ")) {
            Bot.get().getCommandHandler().handleCommand(event);
        }
    }
}
