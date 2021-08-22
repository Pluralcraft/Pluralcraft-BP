/*
 * Copyright (c) 2021 tecc
 * This code is licenced under the MIT licence.
 */

package me.tecc.pluralcraft.bp.events;

import com.comphenix.packetwrapper.WrapperPlayServerPlayerInfo;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.*;
import me.tecc.pluralcraft.bp.PluralcraftBP;
import me.tecc.pluralcraft.bp.storage.Storage;
import me.tecc.pluralcraft.bp.systems.Information;
import me.tecc.pluralcraft.bp.systems.PcSystem;
import me.tecc.pluralcraft.bp.systems.Skin;
import me.tecc.pluralcraft.bp.systems.SystemManager;
import me.tecc.pluralcraft.bp.util.DisguiseUtil;
import me.tecc.pluralcraft.bp.util.IBP;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Disguiser extends PacketAdapter implements Listener, IBP {
    public static final Disguiser INSTANCE = new Disguiser();
    private Disguiser() {
        super(PluralcraftBP.get(), PacketType.Play.Server.PLAYER_INFO);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerJoin(PlayerJoinEvent ev) {
        updateDisguise(ev.getPlayer());
    }

    public void updateDisguise(Player player) {
        if (player == null || !player.isOnline()) return;

        logger().info("Updating disguise for player " + player.getName());
        PcSystem system = SystemManager.getSystem(player);
        DisguiseUtil.hidePlayer(player);
        player.setDisplayName(system.getFronter().getInformation().getName());
        player.spigot().respawn();
        WrapperPlayServerPlayerInfo packet = new WrapperPlayServerPlayerInfo();
        packet.setData(new ArrayList<>(List.of(getData(player, null))));
        packet.broadcastPacket();
        DisguiseUtil.showPlayer(player);
    }

    public PlayerInfoData getData(Player player, PlayerInfoData pid) {
        logger().info("Remaking profile for player " + player.getUniqueId());
        WrappedGameProfile profile;
        if (pid != null) profile = pid.getProfile();
        else profile = WrappedGameProfile.fromPlayer(player);
        PcSystem system = SystemManager.getSystem(player.getUniqueId());
        Information fronterInfo = system.getFronter().getInformation();

        WrappedGameProfile newProfile = profile.withName(fronterInfo.getName());
        newProfile.getProperties().putAll(profile.getProperties());
        Skin skin = fronterInfo.getSkin();
        DisguiseUtil.setSkin(newProfile, skin);
        logger().info("Skin: " + Storage.GSON.toJson(skin));

        int latency;
        EnumWrappers.NativeGameMode gameMode;
        if (pid != null) {
            latency = pid.getLatency();
            gameMode = pid.getGameMode();
        } else {
            latency = 0;
            switch (player.getGameMode()) {
                case CREATIVE:
                    gameMode = EnumWrappers.NativeGameMode.CREATIVE;
                    break;
                case SPECTATOR:
                    gameMode = EnumWrappers.NativeGameMode.SPECTATOR;
                    break;
                case SURVIVAL:
                    gameMode = EnumWrappers.NativeGameMode.SURVIVAL;
                    break;
                case ADVENTURE:
                    gameMode = EnumWrappers.NativeGameMode.ADVENTURE;
                    break;
                default:
                    throw new RuntimeException("Unknown gamemode " + player.getGameMode().name());
            }
        }
        return new PlayerInfoData(newProfile, latency, gameMode, WrappedChatComponent.fromText(fronterInfo.getName()));
    }

    private Random random = new Random();
    @Override
    public void onPacketSending(PacketEvent event) {
        if (event.getPacketType() != PacketType.Play.Server.PLAYER_INFO) return;
        WrapperPlayServerPlayerInfo wrapper = new WrapperPlayServerPlayerInfo(event.getPacket());

        if (wrapper.getAction() != EnumWrappers.PlayerInfoAction.ADD_PLAYER) {
            return;
        }

        Player target = event.getPlayer();

        logger().info("Disguising packets");
        List<PlayerInfoData> current = wrapper.getData();
        List<PlayerInfoData> remade = new ArrayList<>();
        for (PlayerInfoData pid : current) {
            if (pid == null || pid.getProfile() == null) continue;
            Player player = Bukkit.getPlayer(pid.getProfile().getUUID());
            if (player == null || !player.isOnline()) continue;
            remade.add(getData(player, pid));
        }
        wrapper.setData(remade);
        logger().info(Storage.GSON.toJson(remade));
    }
}
