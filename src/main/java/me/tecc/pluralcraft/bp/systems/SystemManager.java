/*
 * Copyright (c) 2021 tecc
 * This code is licenced under the MIT licence.
 */

package me.tecc.pluralcraft.bp.systems;

import de.tr7zw.changeme.nbtapi.NBTEntity;
import me.tecc.pluralcraft.bp.messages.Messages;
import me.tecc.pluralcraft.bp.storage.Storage;
import me.tecc.pluralcraft.bp.util.Util;
import org.bukkit.entity.Player;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SystemManager {
    private static final Map<UUID, PcSystem> systems = new HashMap<>();

    public static PcSystem getSystem(UUID id) {
        PcSystem system = systems.get(id);
        if (system == null) {
            system = Storage.loadSystem(id);
        }
        return system;
    }
    public static PcSystem getSystem(Player player) {
        return getSystem(player.getUniqueId());
    }
    public static PcSystem getOrCreateSystem(Player player) {
        PcSystem system = getSystem(player);
        if (system == null) {
            system = createSystem(player);
        }
        return system;
    }
    public static PcSystem createSystem(Player player) {
        if (systems.containsKey(player.getUniqueId())) return systems.get(player.getUniqueId());
        PcSystem system = new PcSystem(player.getUniqueId());
        systems.put(system.getID(), system);
        Storage.saveSystem(system);
        player.spigot().sendMessage(Messages.createSystem("you"));
        return getSystem(player);
    }

    public static void saveSystem(PcSystem system) {
        systems.put(system.getID(), system);
        Storage.saveSystem(system);
        if (system.isOnline()) {
            try {
                Member fronter = system.getFronter();
                if (!fronter.getNBTFile().exists()) fronter.getNBTFile().createNewFile();
                new NBTEntity(system.getPlayer()).writeCompound(new FileOutputStream(fronter.getNBTFile()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void saveAll() {
        for (PcSystem system : systems.values()) {
            saveSystem(system);
        }
    }
}
