/*
 * Copyright (c) 2021 tecc
 * This code is licenced under the MIT licence.
 */

package me.tecc.pluralcraft.bp.systems;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Selections {
    private static Map<UUID, UUID> memberSelections = new HashMap<>();

    public static void setSelection(UUID player, UUID member) {
        memberSelections.put(player, member);
    }
    public static UUID getSelection(UUID player) {
        return memberSelections.get(player);
    }
}
