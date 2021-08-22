/*
 * Copyright (c) 2021 tecc
 * This code is licenced under the MIT licence.
 */

package me.tecc.pluralcraft.bp.commands.util;

import me.tecc.pluralcraft.bp.commands.AbstractCommand;
import me.tecc.pluralcraft.bp.messages.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class InfoCommand extends AbstractCommand {
    @Override
    public String name() {
        return "info";
    }

    @Override
    public void execute(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull List<String> args) {
        sender.spigot().sendMessage(Messages.pluginInfo());
    }
}
