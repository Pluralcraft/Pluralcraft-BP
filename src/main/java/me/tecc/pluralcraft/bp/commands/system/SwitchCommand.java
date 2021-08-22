/*
 * Copyright (c) 2021 tecc
 * This code is licenced under the MIT licence.
 */

package me.tecc.pluralcraft.bp.commands.system;

import me.tecc.pluralcraft.bp.commands.AbstractCommand;
import me.tecc.pluralcraft.bp.messages.Messages;
import me.tecc.pluralcraft.bp.systems.Member;
import me.tecc.pluralcraft.bp.systems.PcSystem;
import me.tecc.pluralcraft.bp.systems.SystemManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class SwitchCommand extends AbstractCommand {
    @Override
    public String name() {
        return "switch";
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

        PcSystem system = SystemManager.getSystem(player);

        String next = args.get(0);
        List<Member> found = system.findMember(next);

        if (found.isEmpty()) {
            player.spigot().sendMessage(Messages.notAMember(next));
            return;
        }
        Member nextMember = found.get(0);
        if (Objects.equals(nextMember.getID(), system.getFronter().getID())) {
            player.spigot().sendMessage(Messages.alreadyFronter(nextMember));
            return;
        }
        system.switchFronter(nextMember.getID());
        player.spigot().sendMessage(Messages.switched());
    }

    @Override
    public List<String> complete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, List<String> argList) {
        if (!(sender instanceof Player)) {
            return null;
        }
        Player player = (Player) sender;
        PcSystem system = SystemManager.getSystem(player);
        if (argList.size() == 1) {
            return autocompleteMembers(system, argList.get(0));
        }
        return null;
    }
}
