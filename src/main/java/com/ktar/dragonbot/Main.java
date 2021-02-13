package com.ktar.dragonbot;

import com.ktar.dragonbot.listeners.ReadyListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import javax.security.auth.login.LoginException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.EnumSet;

/**
 * Main entrypoint for the starting of the program. This creates the Discord bot and loads the token from the
 * token.txt file (which is not included for obvious reasons).
 */
public class Main {
    public static Database database;
    public static boolean enabled;

    public static void main(String[] args) {
        database = new Database();
        try {
            database.openConnection().createStatement().execute("CREATE TABLE IF NOT EXISTS data_store (" +
                "  id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "  key VARCHAR(48) NOT NULL UNIQUE," +
                "  value INTEGER" +
                ")");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        JDA discord;
        ReadyListener readyListener;
        try {
            discord = JDABuilder.create(getToken(), GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_MESSAGE_REACTIONS, GatewayIntent.GUILD_MESSAGE_TYPING, GatewayIntent.DIRECT_MESSAGES, GatewayIntent.DIRECT_MESSAGE_REACTIONS, GatewayIntent.GUILD_EMOJIS)
                .setActivity(Activity.playing("Dungeons & Dragons"))
                .disableCache(EnumSet.of(CacheFlag.VOICE_STATE, CacheFlag.CLIENT_STATUS, CacheFlag.ACTIVITY))
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

    private static String getToken() {
        File file = new File("token.txt");
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
