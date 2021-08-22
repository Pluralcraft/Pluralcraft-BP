/*
 * Copyright (c) 2021 tecc
 * This code is licenced under the MIT licence.
 */

package me.tecc.pluralcraft.bp.systems.switcher;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;

public class SwitcherWorld {
    public static String NAME = "pcbps-switch";

    public static World create() {
        WorldCreator creator = new WorldCreator(NAME)
                .environment(World.Environment.NORMAL)
                .type(WorldType.FLAT);
        return Bukkit.createWorld(creator);
    }
    public static World get() {
        World world = Bukkit.getWorld(NAME);
        if (world == null) world = create();
        return world;
    }
}
