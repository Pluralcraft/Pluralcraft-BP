/*
 * Copyright (c) 2021 tecc
 * This code is licenced under the MIT licence.
 */

package me.tecc.pluralcraft.bp.commands;

import me.tecc.pluralcraft.bp.systems.PcSystem;
import me.tecc.pluralcraft.bp.util.IBP;
import me.tecc.pluralcraft.bp.util.Util;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public abstract class AbstractCommand implements IBP {
    public abstract String name();
    public String[] aliases() {
        return new String[0];
    }
    public String[] names() {
        return Util.join(new String[]{name()}, aliases());
    }

    public abstract void execute(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull List<String> args);

    public List<String> complete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, List<String> argList) {
        return Collections.emptyList();
    }

    public static List<String> autocompleteMembers(PcSystem system, String current) {
        return Util.autocomplete(
                system.getMembers().values().stream()
                        .reduce(new ArrayList<>(), (list, member) -> {
                            list.addAll(Arrays.asList(
                                    member.getInformation().getName(),
                                    member.getID().toString()
                            ));
                            return list;
                        }, (a, b) -> {
                            a.addAll(b);
                            return a;
                        }),
                current);
    }
}
