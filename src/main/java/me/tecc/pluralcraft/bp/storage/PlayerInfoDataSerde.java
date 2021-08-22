/*
 * Copyright (c) 2021 tecc
 * This code is licenced under the MIT licence.
 */

package me.tecc.pluralcraft.bp.storage;

import com.comphenix.protocol.wrappers.PlayerInfoData;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.google.gson.*;

import java.lang.reflect.Type;

public class PlayerInfoDataSerde implements Serde<PlayerInfoData> {
    @Override
    public PlayerInfoData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public JsonElement serialize(PlayerInfoData src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject obj = new JsonObject();

        WrappedGameProfile profile = src.getProfile();
        JsonObject profileObj = new JsonObject();
        profileObj.addProperty("name", profile.getName());
        profileObj.addProperty("uuid", profile.getUUID().toString());
        profileObj.add("properties", context.serialize(profile.getProperties()));
        obj.add("profile", profileObj);

        return obj;
    }
}
