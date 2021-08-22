/*
 * Copyright (c) 2021 tecc
 * This code is licenced under the MIT licence.
 */

package me.tecc.pluralcraft.bp.util;

import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.comphenix.protocol.wrappers.WrappedSignedProperty;
import me.tecc.pluralcraft.bp.PluralcraftBP;
import me.tecc.pluralcraft.bp.mojang.Mojang;
import me.tecc.pluralcraft.bp.mojang.PlayerProfile;
import me.tecc.pluralcraft.bp.storage.Storage;
import me.tecc.pluralcraft.bp.systems.Skin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.mineskin.MineskinClient;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class DisguiseUtil {
    public static final MineskinClient CLIENT = new MineskinClient();

    @Contract("_, _ -> param1")
    public static WrappedGameProfile setSkin(WrappedGameProfile profile, Skin skin) {
        profile.getProperties().removeAll("textures");
        profile.getProperties().put("textures", skin.toProperty());

        return profile;
    }

    public static Skin getSkin(String playerName) {
        Future<Skin> skinFuture = CompletableFuture.supplyAsync(() -> {
            String uuid = Mojang.getUUIDByName(playerName);
            PlayerProfile profile = Mojang.getProfile(uuid);
            return profile.getSkin();
        });
        try {
            return skinFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Skin getSkin(WrappedGameProfile profile) {
        Collection<WrappedSignedProperty> props = profile.getProperties().get("textures");
        for (WrappedSignedProperty prop : props) {
            if (!Objects.equals(prop.getName(), "textures")) continue;
            return new Skin(prop.getValue(), prop.getSignature());
        }
        return null;
    }

    public static void hidePlayer(Player player) {
        for (Player target : Bukkit.getOnlinePlayers()) {
            target.hidePlayer(PluralcraftBP.get(), player);
        }
    }
    public static void showPlayer(Player player) {
        for (Player target : Bukkit.getOnlinePlayers()) {
            target.showPlayer(PluralcraftBP.get(), player);
        }
    }
}
