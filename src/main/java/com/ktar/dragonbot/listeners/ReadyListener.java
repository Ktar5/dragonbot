package com.ktar.dragonbot.listeners;

import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import org.tinylog.Logger;

import javax.annotation.Nonnull;

/**
 * Just for logging purposes. It prints out once the first ReadyEvent is received
 */
public class ReadyListener implements EventListener {

    public ReadyListener(){
        Logger.info("Created command listener");
    }

    @Override
    public void onEvent(@Nonnull GenericEvent event) {
        if(event instanceof ReadyEvent){
            Logger.info("Bot is connected and ready!");
        }
    }
}
