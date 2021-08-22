/*
 * Copyright (c) 2021 tecc
 * This code is licenced under the MIT licence.
 */

package me.tecc.pluralcraft.bp.events;

import me.tecc.pluralcraft.bp.systems.PcSystem;
import me.tecc.pluralcraft.bp.systems.SystemManager;
import me.tecc.pluralcraft.bp.util.IBP;
import me.tecc.pluralcraft.bp.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.world.WorldSaveEvent;

public class EventManager implements Listener, IBP {
    public static final EventManager INSTANCE = new EventManager();
    private EventManager() {}

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerJoin(PlayerJoinEvent ev) {
        PcSystem system = SystemManager.getOrCreateSystem(ev.getPlayer());
        String name = system.getFronter().getInformation().getName();
        ev.setJoinMessage(name + " joined the game");
        Util.fix(ev.getPlayer());
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerQuit(PlayerQuitEvent ev) {
        PcSystem system = SystemManager.getOrCreateSystem(ev.getPlayer());
        SystemManager.saveSystem(system);
        String name = system.getFronter().getInformation().getName();
        ev.setQuitMessage(name + " left the game");
    }
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerMove(PlayerMoveEvent ev) {
        Util.fix(ev.getPlayer());
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerDie(PlayerDeathEvent ev) {
        Player player = ev.getEntity();
        Util.fix(player);
        ev.setDeathMessage((ev.getDeathMessage() == null ? player.getDisplayName() + " died" : ev.getDeathMessage()).replaceAll(player.getName(), player.getDisplayName()));
    }

    public void onWorldSave(WorldSaveEvent event) {
        SystemManager.saveAll();
    }
}
