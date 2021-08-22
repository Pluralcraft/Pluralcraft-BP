/*
 * Copyright (c) 2021 tecc
 * This code is licenced under the MIT licence.
 */

package me.tecc.pluralcraft.bp.mojang;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.tecc.pluralcraft.bp.systems.Skin;

import java.util.Objects;

public class PlayerProfile {
    private JsonObject object;

    public PlayerProfile(JsonObject obj) {
        this.object = obj;
    }

    public String getName() {
        return this.object.get("name").getAsString();
    }

    public String getID() {
        return this.object.get("id").getAsString();
    }

    private JsonObject findProperty(String name) {
        JsonObject found = null;
        for (JsonElement el : object.getAsJsonArray("properties")) {
            if (!el.isJsonObject()) continue;
            JsonObject obj = el.getAsJsonObject();
            if (!Objects.equals(obj.get("name").getAsString(), name)) continue;
            found = obj;
            break;
        }
        return found;
    }

    public Skin getSkin() {
        JsonObject prop = findProperty("textures");
        return new Skin(prop.get("value").getAsString(), prop.get("signature").getAsString());
    }
}
