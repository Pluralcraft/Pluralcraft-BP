/*
 * Copyright (c) 2021 tecc
 * This code is licenced under the MIT licence.
 */

package me.tecc.pluralcraft.bp.commands;

import me.tecc.pluralcraft.bp.commands.dev.WorldCommand;
import me.tecc.pluralcraft.bp.commands.system.MemberCommand;
import me.tecc.pluralcraft.bp.commands.system.SwitchCommand;
import me.tecc.pluralcraft.bp.commands.system.UpdateCommand;
import me.tecc.pluralcraft.bp.commands.util.InfoCommand;

import java.util.*;
import java.util.function.Function;

public class CommandManager {
    private static final Map<String, AbstractCommand> commands = new HashMap<>();
    private static final Map<String, String> aliases = new HashMap<>();

    public static void registerDefaults() {
        registerCommand(new InfoCommand());
        registerCommand(new UpdateCommand());
        // registerCommand(new SystemCommand());
        registerCommand(new MemberCommand());
        registerCommand(new SwitchCommand());
        registerCommand(new WorldCommand());
    }
    public static void registerCommand(AbstractCommand command) {
        commands.put(command.name(), command);
        for (String s : command.names()) aliases.put(s, command.name());
    }

    public static AbstractCommand getCommand(String pseudonym) {
        String name = aliases.get(pseudonym);
        if (name == null) return null;
        return commands.get(name);
    }

    public static <T> List<T> mapCommands(Function<AbstractCommand, T[]> converter) {
        List<T> list = new ArrayList<>();
        for (AbstractCommand command : getCommands()) {
            list.addAll(List.of(converter.apply(command)));
        }
        return list;
    }

    private static List<AbstractCommand> getCommands() {
        return new ArrayList<>(commands.values());
    }
}
