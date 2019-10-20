package com.ktar.dragonbot;

import com.ktar.dragonbot.listeners.ReadyListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import javax.security.auth.login.LoginException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.EnumSet;

public class Main {

    public static void main(String[] args) {
        JDA discord;
        ReadyListener readyListener;
        try {
            discord = new JDABuilder("NjM0OTg2MzQ5MTcwMTMwOTY0.XaqrCA.lD71Ad-1ytVhg4Kyr-9TMRlCRXQ")
                    .setActivity(Activity.playing("Dungeons & Dragons"))
                    .setDisabledCacheFlags(EnumSet.of(CacheFlag.VOICE_STATE, CacheFlag.EMOTE, CacheFlag.CLIENT_STATUS, CacheFlag.ACTIVITY))
                    .setStatus(OnlineStatus.ONLINE)
                    .addEventListeners(readyListener = new ReadyListener())
                    .build();
        } catch (LoginException e) {
            e.printStackTrace();
            return;
        }
        try {
            discord.awaitReady();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return;
        }
        new Bot(discord, readyListener);
    }

    public static String getToken() {
        File file = new File("token");
        System.out.println(file.getAbsolutePath());
        if (!file.exists()) {
            System.out.println("File doesn't exist at location: " + file.getAbsolutePath());
            return null;
        }

        String token = null;
        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            token = bufferedReader.readLine();
            bufferedReader.close();
            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (token == null) {
            System.out.println("Token was null from file at location: " + file.getAbsolutePath());
        }

        return token;
    }

}
