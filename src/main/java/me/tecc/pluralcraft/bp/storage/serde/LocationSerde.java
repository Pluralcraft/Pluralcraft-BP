/*
 * Copyright (c) 2021 tecc
 * This code is licenced under the MIT licence.
 */

package me.tecc.pluralcraft.bp.storage.serde;

import com.google.gson.*;
import me.tecc.pluralcraft.bp.storage.Serde;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.lang.reflect.Type;
import java.util.UUID;

public class LocationSerde implements Serde<Location> {
    @Override
    public Location deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        if (!jsonElement.isJsonObject()) throw new JsonParseException("Expected object");
        JsonObject obj = jsonElement.getAsJsonObject();
        JsonElement worldEl = obj.get("world");

        // world
        World world = worldEl == null ? null : Bukkit.getWorld(UUID.fromString(worldEl.getAsString()));
        double
                // position
                x = obj.get("x").getAsDouble(),
                y = obj.get("y").getAsDouble(),
                z = obj.get("z").getAsDouble();
        float
                // rotation
                yaw = obj.get("yaw").getAsFloat(),
                pitch = obj.get("pitch").getAsFloat();

        return new Location(world, x, y, z, yaw, pitch);
    }

    @Override
    public JsonElement serialize(Location location, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject obj = new JsonObject();
        // position
        obj.addProperty("x", location.getX());
        obj.addProperty("y", location.getY());
        obj.addProperty("z", location.getZ());

        // rotation
        obj.addProperty("yaw", location.getYaw());
        obj.addProperty("pitch", location.getPitch());

        // world if necessary
        if (location.isWorldLoaded()) obj.addProperty("world", location.getWorld().getUID().toString());
        return obj;
    }
}
