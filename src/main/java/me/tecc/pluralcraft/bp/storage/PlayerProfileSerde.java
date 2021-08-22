/*
 * Copyright (c) 2021 tecc
 * This code is licenced under the MIT licence.
 */

package me.tecc.pluralcraft.bp.storage;

import com.google.gson.*;
import me.tecc.pluralcraft.bp.mojang.PlayerProfile;

import java.lang.reflect.Type;

public class PlayerProfileSerde implements Serde<PlayerProfile> {
    @Override
    public PlayerProfile deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        throw new UnsupportedOperationException("whoops");
    }

    @Override
    public JsonElement serialize(PlayerProfile src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject obj = new JsonObject();
        obj.addProperty("name", src.getName());
        obj.addProperty("id", src.getID());
        obj.add("skin", context.serialize(src.getSkin()));
        return obj;
    }
}
