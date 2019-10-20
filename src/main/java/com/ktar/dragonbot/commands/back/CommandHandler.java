package com.ktar.dragonbot.commands.back;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.tinylog.Logger;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

public class CommandHandler {
    private final HashMap<String, Command> commands;

    public CommandHandler() {
        commands = new HashMap<>();
    }

    public void registerCommand(Command command) {
        if (commands.containsKey(command.command.toLowerCase())) {
            Logger.error("Command already exists for string: " + command.command.toLowerCase());
            return;
        }
        commands.put(command.command.toLowerCase(), command);
        Logger.info("Added command: " + command.command.toLowerCase());
    }

    public void handleCommand(MessageReceivedEvent event) {
        String contentDisplay = event.getMessage().getContentDisplay();
        contentDisplay = contentDisplay.substring(6);
        String[] splits = contentDisplay.split(" ", 2);
        String command = splits[0].toLowerCase();

        if (commands.get(command) != null) {
            Logger.info("Handling command: " + command);
            if (splits.length > 1) {
                commands.get(command).attemptUse(event, splits[1]);
            } else {
                commands.get(command).attemptUse(event, "");
            }
        }
    }

    public void autoRegisterCommands() {
        String pkg = "com.ktar.dragonbot.commands";
        String annotation = pkg + ".back.RegisterCommand";
        try (ScanResult scanResult = new ClassGraph()
                .enableAnnotationInfo()
                .enableClassInfo()
                .whitelistPackages(pkg)
                .scan()) {
            for (ClassInfo routeClassInfo : scanResult.getClassesWithAnnotation(annotation)) {
                Class<?> aClass = Class.forName(routeClassInfo.getName());
                for (Constructor constructor : aClass.getConstructors()) {
                    if (constructor.getParameters().length == 0) {
                        constructor.newInstance();
                    }
                }
            }
        } catch (InstantiationException | InvocationTargetException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
