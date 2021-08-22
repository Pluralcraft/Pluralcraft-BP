/*
 * Copyright (c) 2021 tecc
 * This code is licenced under the MIT licence.
 */

package me.tecc.pluralcraft.bp.events;

import me.tecc.pluralcraft.bp.commands.AbstractCommand;
import me.tecc.pluralcraft.bp.commands.CommandManager;
import me.tecc.pluralcraft.bp.messages.Messages;
import me.tecc.pluralcraft.bp.util.IBP;
import me.tecc.pluralcraft.bp.util.Util;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

public class CommandHandler implements CommandExecutor, TabCompleter {
    public static CommandHandler INSTANCE = new CommandHandler();

    public static String alias(String s) {
        switch (s) {
            case "switch":
                return "switch";
            case "pluralcraft-bp":
            default:
                return null;
        }
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> argList = Arrays.asList(args);
        String alias = alias(command.getName());

        if (alias == null) {
            List<String> substituted;
            if (argList.size() < 2) substituted = Collections.emptyList();
            else substituted = argList.subList(1, argList.size());
            String name = "info";
            if (argList.size() > 0) name = argList.get(0);
            pass(sender, command, label, substituted, name);
        } else {
            pass(sender, command, label, argList, "switch");
        }

        return true;
    }

    public void pass(CommandSender sender, Command command, String label, List<String> args, String commandName) {
        AbstractCommand bpCommand = CommandManager.getCommand(commandName);
        if (bpCommand == null) {
            sender.spigot().sendMessage(Messages.invalid("command", commandName));
            return;
        }
        try {
            bpCommand.execute(sender, command, label, args);
        } catch (Throwable t) {
            sender.spigot().sendMessage(Messages.errorWhilst("executing command", t));
            t.printStackTrace();
        }
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> argList = Arrays.asList(args);
        String commandName = alias(command.getName());
        if (commandName == null) {
            if (argList.size() > 1) commandName = argList.get(0);
            else return Util.autocomplete(CommandManager.mapCommands(AbstractCommand::names), argList.size() == 1 ? argList.get(0) : null);
        } else argList = Util.join(Collections.singleton(commandName), argList);

        AbstractCommand aCommand = CommandManager.getCommand(commandName);
        if (aCommand == null) return Collections.emptyList();
        List<String> list = argList.subList(1, argList.size());
        List<String> completed = null;
        try {
            completed = aCommand.complete(sender, command, label, list);
        } catch (Throwable t) {
            IBP.getLogger().log(Level.WARNING, "Uncaught throwable", t);
        }

        if (completed == null) return Collections.emptyList();
        else return completed;
    }
}
