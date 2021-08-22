/*
 * Copyright (c) 2021 tecc
 * This code is licenced under the MIT licence.
 */

package me.tecc.pluralcraft.bp.util;

import org.bukkit.GameMode;

import java.io.*;
import java.util.Properties;

public class ServerProperties {
    private static Properties CACHED;
    public static Properties getRawServerProps() {
        if (CACHED != null) return CACHED;
        CACHED = new Properties();
        try {
            File file = new File(Util.getServerFolder(), "server.properties");
            InputStream inputStream = new FileInputStream(file);
            CACHED.load(inputStream);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            CACHED = null;
        }
        return CACHED;
    }

    public static GameMode getGameMode() {
        return GameMode.valueOf(getRawServerProps().getProperty("gamemode"));
    }
    public static String getLevelName() {
        return getRawServerProps().getProperty("level-name");
    }
}
