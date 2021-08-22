/*
 * Copyright (c) 2021 tecc
 * This code is licenced under the MIT licence.
 */

package me.tecc.pluralcraft.bp.util;

import com.comphenix.protocol.ProtocolManager;
import me.tecc.pluralcraft.bp.PluralcraftBP;

import java.util.logging.Logger;

public interface IBP {
    default PluralcraftBP pbp() {
        return PluralcraftBP.get();
    }
    static PluralcraftBP getPBP() {
        return PluralcraftBP.get();
    }
    default ProtocolManager protocolManager() {
        return pbp().protocolManager();
    }
    static ProtocolManager getProtocolManager() {
        return getPBP().protocolManager();
    }
    default Logger logger() {
        return pbp().getLogger();
    }
    static Logger getLogger() {
        return getPBP().logger();
    }
}
