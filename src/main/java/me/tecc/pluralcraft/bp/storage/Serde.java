/*
 * Copyright (c) 2021 tecc
 * This code is licenced under the MIT licence.
 */

package me.tecc.pluralcraft.bp.storage;

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;

public interface Serde<T> extends JsonSerializer<T>, JsonDeserializer<T> {
}
