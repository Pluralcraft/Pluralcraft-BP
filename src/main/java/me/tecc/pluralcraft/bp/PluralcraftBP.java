/*
 * Copyright (c) 2021 tecc
 * This code is licenced under the MIT licence.
 */

package me.tecc.pluralcraft.bp;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import me.tecc.pluralcraft.bp.commands.CommandManager;
import me.tecc.pluralcraft.bp.events.CommandHandler;
import me.tecc.pluralcraft.bp.events.Disguiser;
import me.tecc.pluralcraft.bp.events.EventManager;
import me.tecc.pluralcraft.bp.messages.Messages;
import me.tecc.pluralcraft.bp.systems.SystemManager;
import me.tecc.pluralcraft.bp.systems.switcher.SwitcherWorld;
import me.tecc.pluralcraft.bp.util.IBP;
import me.tecc.pluralcraft.bp.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class PluralcraftBP extends JavaPlugin implements IBP {
    private ProtocolManager protocolManager;

    @Override
    public void onLoad() {
        CommandManager.registerDefaults();
        // initialise protocols
        this.protocolManager = ProtocolLibrary.getProtocolManager();
        for (Player player : Bukkit.getOnlinePlayers()) {
            SystemManager.getOrCreateSystem(player);
        }
        for (World world : Bukkit.getWorlds()) {
            world.setGameRule(GameRule.DISABLE_ELYTRA_MOVEMENT_CHECK, true);
        }
        SwitcherWorld.get();
    }

    @Override
    public void onEnable() {
        // initialise commands
        for (String key : getDescription().getCommands().keySet()) {
            PluginCommand command = getCommand(key);
            if (command == null) break;
            command.setExecutor(CommandHandler.INSTANCE);
            command.setTabCompleter(CommandHandler.INSTANCE);
        }

        PluginManager pm = getServer().getPluginManager();
        // initialise event manager
        pm.registerEvents(EventManager.INSTANCE, this);
        // initialise disguiser
        pm.registerEvents(Disguiser.INSTANCE, this);
        protocolManager.addPacketListener(Disguiser.INSTANCE);

        getServer().spigot().broadcast(Messages.pluginInfo());
        for (Player player : Bukkit.getOnlinePlayers()) {
            Util.fix(player);
            Disguiser.INSTANCE.updateDisguise(player);
        }
    }

    @Override
    public void onDisable() {
        SystemManager.saveAll();
        protocolManager().removePacketListener(Disguiser.INSTANCE);
    }

    @Override
    public ProtocolManager protocolManager() {
        return this.protocolManager;
    }

    public static PluralcraftBP get() {
        return PluralcraftBP.getPlugin(PluralcraftBP.class);
    }
}
