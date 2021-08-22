/*
 * Copyright (c) 2021 tecc
 * This code is licenced under the MIT licence.
 */

package me.tecc.pluralcraft.bp.commands.system;

import me.tecc.pluralcraft.bp.commands.AbstractCommand;
import me.tecc.pluralcraft.bp.events.Disguiser;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class UpdateCommand extends AbstractCommand {
    @Override
    public String name() {
        return "update";
    }

    @Override
    public void execute(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull List<String> args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("no");
            return;
        }
        Player p = (Player) sender;
        Disguiser.INSTANCE.updateDisguise(p);
    }
}
