/*
 * Copyright (c) 2021 tecc
 * This code is licenced under the MIT licence.
 */

package me.tecc.pluralcraft.bp.commands.system;

import me.tecc.pluralcraft.bp.commands.AbstractCommand;
import me.tecc.pluralcraft.bp.events.Disguiser;
import me.tecc.pluralcraft.bp.messages.Components;
import me.tecc.pluralcraft.bp.messages.Messages;
import me.tecc.pluralcraft.bp.systems.Member;
import me.tecc.pluralcraft.bp.systems.PcSystem;
import me.tecc.pluralcraft.bp.systems.Selections;
import me.tecc.pluralcraft.bp.systems.SystemManager;
import me.tecc.pluralcraft.bp.util.DisguiseUtil;
import me.tecc.pluralcraft.bp.util.Util;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public class MemberCommand extends AbstractCommand {
    private static final List<String> properties = Arrays.asList("name", "skin", "pronouns");

    @Override
    public String name() {
        return "member";
    }

    @Override
    public void execute(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull List<String> args) {
        if (!(sender instanceof Player)) {
            sender.spigot().sendMessage(Messages.mustBe("player", "command"));
            return;
        }
        Player player = (Player) sender;
        PcSystem system = SystemManager.getSystem(player);
        if (args.size() < 1) {
            sender.spigot().sendMessage(Messages.notEnoughArguments("command", 1));
            return;
        }

        String action = args.get(0);
        switch (action) {
            case "list": {
                player.spigot().sendMessage(Messages.memberList(system.getMembers().values()));
                return;
            }
            case "select": {
                if (args.size() < 2) {
                    sender.spigot().sendMessage(Messages.notEnoughArguments("action", 1));
                    return;
                }
                String selection = args.get(1);

                Member newSelection = null;

                List<Member> found = system.findMember(selection);
                if (!found.isEmpty()) newSelection = found.get(0);

                if (newSelection == null) {
                    player.spigot().sendMessage(Messages.notAMember(selection));
                    return;
                }
                Selections.setSelection(player.getUniqueId(), newSelection.getID());

                player.spigot().sendMessage(Messages.selected("member", Components.member(newSelection)));
                return;
            }
            case "info": {
                UUID selection = Selections.getSelection(player.getUniqueId());
                if (selection == null) {
                    player.spigot().sendMessage(Messages.noSelection("member"));
                    return;
                }
                Member member = system.getMember(selection);
                player.spigot().sendMessage(Messages.member(member));
                return;
            }
            case "set": {
                UUID selection = Selections.getSelection(player.getUniqueId());
                if (selection == null) {
                    player.spigot().sendMessage(Messages.noSelection("member"));
                    return;
                }
                Member member = system.getMember(selection);

                if (args.size() < 2) {
                    player.spigot().sendMessage(Messages.notEnoughArguments("action", 1));
                    return;
                }
                String toSet = args.get(1);
                TextComponent value;
                switch (toSet) {
                    case "skin": {
                        if (args.size() < 3) {
                            player.spigot().sendMessage(Messages.notEnoughArguments("setter", 1));
                            return;
                        }
                        String name = args.get(2);
                        member.getInformation().setSkin(DisguiseUtil.getSkin(name));
                        value = new TextComponent(name);
                        value.setColor(Components.HIGHLIGHT_1);
                        break;
                    }
                    case "name": {
                        if (args.size() < 3) {
                            player.spigot().sendMessage(Messages.notEnoughArguments("setter", 1));
                            return;
                        }
                        String name = args.get(2);
                        member.getInformation().setName(name);
                        value = new TextComponent(name);
                        value.setColor(Components.HIGHLIGHT_1);
                        break;
                    }
                    case "pronouns": {
                        if (args.size() < 3) {
                            player.spigot().sendMessage(Messages.notEnoughArguments("setter", 1));
                            return;
                        }
                        String pronouns = args.get(2);
                        member.getInformation().setPronouns(pronouns);
                        value = new TextComponent(pronouns);
                        value.setColor(Components.HIGHLIGHT_1);
                        break;
                    }
                    default: {
                        player.spigot().sendMessage(Messages.invalid("property", toSet));
                        return;
                    }
                }
                player.spigot().sendMessage(Messages.set(toSet, value));
                Disguiser.INSTANCE.updateDisguise(player);
                return;
            }
            case "add": {
                String name = "Member" + system.getMembers().size();
                if (args.size() > 1) {
                    name = args.get(1);
                }
                Member member = system.createMember(name);
                Selections.setSelection(player.getUniqueId(), member.getID());
                player.spigot().sendMessage(Messages.addMember(name));
                return;
            }
            case "remove": {
            }
            default: {
                player.spigot().sendMessage(Messages.invalid("action", action));
                return;
            }
        }
    }

    @Override
    public List<String> complete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, List<String> argList) {
        if (!(sender instanceof Player)) {
            return null;
        }
        Player player = (Player) sender;
        PcSystem system = SystemManager.getSystem(player);
        switch (argList.size()) {
            case 1:
                return Util.autocomplete(
                        Arrays.asList(
                                "list",
                                "select", "info", "set",
                                "add", "remove"),
                        argList.get(0));
            case 2: {
                String action = argList.get(0);
                switch (action) {
                    case "select": {
                        return autocompleteMembers(system, argList.get(1));
                    }
                    case "set": {
                        return Util.autocomplete(properties, argList.get(1));
                    }
                }
            }
            case 3: {
                String action = argList.get(0);
                switch (action) {
                    case "set": {
                        String property = argList.get(1);
                        switch (property) {
                            case "name":
                                return Util.autocomplete(Arrays.asList("Member1", "SomeName", "Notch"), argList.get(2));
                            case "pronouns":
                                return Util.autocomplete(Arrays.asList(
                                        "She/her", "They/them", "He/him", // basic
                                        "She/they", "He/they", "He/she", // combinations
                                        "It/its", // more
                                        "Any", "Ask"
                                ), argList.get(2));
                            case "skin":
                                return Util.autocomplete(Arrays.asList(
                                        "Notch", "Technotype", "1pe", "jeb_"
                                ), argList.get(2));
                        }
                    }
                }
            }
        }
        return null;
    }
}
