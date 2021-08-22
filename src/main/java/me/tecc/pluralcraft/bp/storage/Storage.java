/*
 * Copyright (c) 2021 tecc
 * This code is licenced under the MIT licence.
 */

package me.tecc.pluralcraft.bp.storage;

import com.comphenix.protocol.wrappers.PlayerInfoData;
import com.google.common.collect.Multimap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.tecc.pluralcraft.bp.PluralcraftBP;
import me.tecc.pluralcraft.bp.mojang.PlayerProfile;
import me.tecc.pluralcraft.bp.systems.Member;
import me.tecc.pluralcraft.bp.systems.PcSystem;

import java.io.*;
import java.util.UUID;

public class Storage {
    public static Gson GSON = new GsonBuilder()
            .registerTypeAdapter(PlayerProfile.class, new PlayerProfileSerde())
            .registerTypeAdapter(PlayerInfoData.class, new PlayerInfoDataSerde())
            .create();

    public static final String FILE_TYPE_NBT = "dat";

    private static <T> void saveJson(File file, T t) {
        try {
            FileWriter writer = new FileWriter(file);
            GSON.toJson(t, writer);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static <T> T loadJson(File file, Class<T> clz) {
        try {
            if (file == null) return null;
            if (!file.exists()) return null;
            FileReader reader = new FileReader(file);
            T result = GSON.fromJson(reader, clz);
            reader.close();
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static PcSystem loadSystem(UUID systemID) {
        return loadJson(getSystemFile(systemID), PcSystem.class);
    }
    public static void saveSystem(PcSystem system) {
        saveJson(getSystemFile(system.getID()), system);
    }

    public static File getMemberFile(UUID systemID, UUID memberID, String type) {
        return new File(getSystemDir(systemID), memberID.toString() + "." + type);
    }
    public static File getSystemFile(UUID systemID) {
        return new File(getSystemDir(systemID), "info.json");
    }
    public static File getSystemDir(UUID systemID) {
        return gcineDir(getDataDir(), systemID.toString());
    }
    public static File getDataDir() {
        return gcineDir(PluralcraftBP.get().getDataFolder(), "data");
    }

    private static File gcineDir(File base, String name) {
        File f = new File(base, name);
        if (!f.exists()) {
            if (!f.mkdirs()) {
                throw new RuntimeException("Can't create dir");
            }
        }
        return f;
    }
}
