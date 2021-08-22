/*
 * Copyright (c) 2021 tecc
 * This code is licenced under the MIT licence.
 */

package me.tecc.pluralcraft.bp.systems.switcher;

import de.tr7zw.changeme.nbtapi.*;
import me.tecc.pluralcraft.bp.systems.Member;
import me.tecc.pluralcraft.bp.util.IBP;
import me.tecc.pluralcraft.bp.util.NBTInventory;
import me.tecc.pluralcraft.bp.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.PlayerInventory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Switcher {

    public static void doSwitch(Player player, Member previous, Member next) {
        player.saveData();

        Util.fix(player);
        // File currentFile = Util.getPlayerDataFile(player.getUniqueId());
        try {
            if (!previous.getNBTFile().exists()) previous.getNBTFile().createNewFile();
            new NBTEntity(player).writeCompound(new FileOutputStream(previous.getNBTFile()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Bukkit.getScheduler().runTaskLater(IBP.getPBP(), () -> {
            Util.fix(player);
            applyNBT(player, next.getNBTFile());
            Util.fix(player);
            // player.saveData();

            IBP.getLogger().info("Switch commenced");
        }, 1);
    }

    private static void applyNBT(Player player, File nbtFile) {
        PlayerNBT p = null;
        if (nbtFile != null && nbtFile.exists()) {
            try {
                NBTFile compound = new NBTFile(nbtFile);
                p = new PlayerNBT(compound);
                IBP.getLogger().info("Loaded file: " + nbtFile.getName() + " (" + compound.hashCode() + ")");
            } catch (Throwable t) {
                t.printStackTrace();
                p = null;
            }
        }
        if (p == null) {
            p = new DefaultPlayerNBT();
            IBP.getLogger().info("Using default");
        }

        player.teleport(p.getLocation(), PlayerTeleportEvent.TeleportCause.PLUGIN);
        player.setGameMode(p.getGameMode());

        // inventory merging
        NBTInventory nextInv = p.getInventory();
        PlayerInventory inv = player.getInventory();
        // contents
        inv.setContents(nextInv.getContents());
        // armour
        inv.setHelmet(nextInv.getHelmet());
        inv.setChestplate(nextInv.getChestplate());
        inv.setLeggings(nextInv.getLeggings());
        inv.setBoots(nextInv.getBoots());
        // extra contents
        // inv.setExtraContents(nextInv.getExtraContents());
        inv.setHeldItemSlot(nextInv.getHeldItemSlot());

        // enderchest
        Inventory enderInv = player.getEnderChest();
        enderInv.setContents(nextInv.getEnderContents());

        // stats
        player.setHealth(p.getHealth());
        player.setTotalExperience(p.getTotalExperience());
        player.setExp(p.getExp());
        player.setLevel(p.getLevel());
        player.setBedSpawnLocation(p.getSpawnLocation());

        // abilities
        PlayerNBT.Abilities abilities = p.getAbilities();
        // player.setWalkSpeed(abilities.getWalkSpeed());
        player.setAllowFlight(abilities.mayFly());
        // player.setFlySpeed(abilities.getFlySpeed());
        player.setFlying(abilities.isFlying());

        // TODO: the other stats
        // p.getStatistics().forEach();
    }
}
