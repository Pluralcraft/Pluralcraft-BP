/*
 * Copyright (c) 2021 tecc
 * This code is licenced under the MIT licence.
 */

package me.tecc.pluralcraft.bp.util;

import me.tecc.pluralcraft.bp.PluralcraftBP;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;

public class Util {
    @SuppressWarnings("unchecked")
    public static <T> T[] join(T[] a, T[] b) {
        int length = a.length + b.length;
        T[] c = (T[]) Array.newInstance(a.getClass().getComponentType(), length);
        System.arraycopy(a, 0, c, 0, a.length);
        System.arraycopy(b, 0, c, a.length, b.length);
        return c;
    }

    public static <T> List<T> join(Collection<T> a, Collection<T> b) {
        List<T> c = new ArrayList<>();
        c.addAll(a);
        c.addAll(b);
        return c;
    }

    public static File getServerFolder() {
        return PluralcraftBP.get().getDataFolder().getParentFile().getParentFile();
    }

    public static File getPlayerDataFile(UUID uuid) {
        String worldName = ServerProperties.getLevelName();
        World world = Bukkit.getWorld(worldName);
        if (world == null) throw new RuntimeException("not gonna bother with this");
        return new File(world.getWorldFolder(), "playerdata/" + uuid + ".dat");
    }

    public static void copyFile(File source, File target) {
        try {
            if (!source.exists()) {
                target.delete();
            } else {
                InputStream inputStream = new FileInputStream(source);
                OutputStream outputStream = new FileOutputStream(target);
                int val;
                while ((val = inputStream.read()) != -1) {
                    outputStream.write(val);
                }
                inputStream.close();
                outputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void fix(Player player) {
        Object ep = Rutil.call(player, "getHandle");
        if (Rutil.get(ep, "cI")) {
            Rutil.set(ep, "cI", true);
            IBP.getLogger().info("Disabled check movement for player " + player.getUniqueId());
        }  // IBP.getLogger().info("Player " + player.getUniqueId() + " doesn't need fixing");

    }

    public static List<String> autocomplete(@NotNull List<String> available, @Nullable String current) {
        if (current == null) current = "";
        final String finalCurrent = current;
        return available.stream()
                .filter((s) -> s.startsWith(finalCurrent))
                .sorted(String::compareToIgnoreCase)
                .collect(Collectors.toList());
    }

    public static String worldName(String nbtName) {
        String name = NamespacedKey.fromString(nbtName).getKey();
        switch (name.toLowerCase()) {
            case "overworld":
                return ServerProperties.getLevelName();
            case "the_nether":
                return ServerProperties.getLevelName() + "_nether";
            case "the_end":
                return ServerProperties.getLevelName() + "the_end";
            default:
                return name;
        }
    }
}
