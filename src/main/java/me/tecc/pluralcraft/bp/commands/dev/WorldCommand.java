/*
 * Copyright (c) 2021 tecc
 * This code is licenced under the MIT licence.
 */

package me.tecc.pluralcraft.bp.commands.dev;

import me.tecc.pluralcraft.bp.commands.AbstractCommand;
import me.tecc.pluralcraft.bp.messages.Messages;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class WorldCommand extends AbstractCommand {
    @Override
    public String name() {
        return "world";
    }

    @Override
    public void execute(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull List<String> args) {
        if (!(sender instanceof Player)) {
            sender.spigot().sendMessage(Messages.mustBe("player", "command"));
            return;
        }
        Player player = (Player) sender;
        if (args.size() < 1) {
            player.spigot().sendMessage(Messages.notEnoughArguments("command", 1));
            return;
        }

        String worldName = args.get(0);
        World world = Bukkit.getWorld(worldName);

        if (world == null) {
            player.spigot().sendMessage(Messages.invalid("world name", worldName));
            world = Bukkit.createWorld(new WorldCreator(worldName));
        }

        player.teleport(world.getSpawnLocation(), PlayerTeleportEvent.TeleportCause.PLUGIN);
        player.spigot().sendMessage(
                new TextComponent("done")
        );
    }

    @Override
    public List<String> complete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, List<String> argList) {
        if (argList.size() < 2) {
            return Bukkit.getWorlds().stream()
                    .map((w) -> w != null ? w.getName() : null)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        }
        return null;
    }
}
