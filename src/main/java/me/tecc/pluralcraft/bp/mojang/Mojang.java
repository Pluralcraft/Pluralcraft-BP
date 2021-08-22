/*
 * Copyright (c) 2021 tecc
 * This code is licenced under the MIT licence.
 */

package me.tecc.pluralcraft.bp.mojang;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import kong.unirest.Unirest;
import me.tecc.pluralcraft.bp.PluralcraftBP;
import me.tecc.pluralcraft.bp.storage.Storage;
import me.tecc.pluralcraft.bp.systems.Skin;

import java.util.concurrent.Future;

public class Mojang {
    public static JsonElement request(String url) {
        PluralcraftBP.get().getLogger().info("GET " + url);
        return new JsonParser().parse(Unirest.request("GET", url).asString().getBody());
    }

    public static String getUUIDByName(String name) {
        return request("https://api.mojang.com/users/profiles/minecraft/" + name).getAsJsonObject().get("id").getAsString();
    }
    public static PlayerProfile getProfile(String uuid) {
        JsonObject el = request("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid + "?unsigned=false").getAsJsonObject();
        return new PlayerProfile(el);
    }
}
