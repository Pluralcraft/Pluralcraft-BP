/*
 * Copyright (c) 2021 tecc
 * This code is licenced under the MIT licence.
 */

package me.tecc.pluralcraft.bp.systems;

import com.comphenix.protocol.wrappers.WrappedGameProfile;
import me.tecc.pluralcraft.bp.events.Disguiser;
import me.tecc.pluralcraft.bp.systems.switcher.Switcher;
import me.tecc.pluralcraft.bp.util.DisguiseUtil;
import me.tecc.pluralcraft.bp.util.IBP;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.*;

public class PcSystem implements IBP {
    private Map<UUID, Member> members = new HashMap<>();
    private UUID player;
    private UUID fronter;

    PcSystem(UUID uuid) {
        this.player = uuid;
        Member m = createMember(getOffline().getName());
        this.fronter = m.getID();
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(player);
    }

    public OfflinePlayer getOffline() {
        return Bukkit.getOfflinePlayer(player);
    }

    public Member createMember(String name) {
        Member member = new Member(this, name);
        member.getInformation().setSkin(DisguiseUtil.getSkin(WrappedGameProfile.fromPlayer(getPlayer())));
        members.put(member.getID(), member);
        return member;
    }

    public List<Member> findMemberByName(String name) {
        List<Member> available = new ArrayList<>();
        for (Member member : members.values()) {
            if (Objects.equals(member.getInformation().getName(), name)) available.add(member);
        }
        return available;
    }

    public List<Member> findMember(String query) {
        List<Member> found = findMemberByName(query);
        if (!found.isEmpty()) return found;

        // then by uuid
        try {
            UUID uuid = UUID.fromString(query);
            return List.of(getMember(uuid));
        } catch (IllegalArgumentException ex) {
            // ignore
        }
        return Collections.emptyList();
    }

    public Member getMember(UUID uuid) {
        return members.get(uuid);
    }

    public Member getFronter() {
        return members.get(fronter);
    }

    public void switchFronter(UUID uuid) {
        Member current = getFronter();
        Member next = getMember(uuid);
        if (next == null) return;
        Player player = getPlayer();
        logger().info("Switching from member " + current.getID() + " to " + next.getID());
        this.fronter = next.getID();
        update(null);
        if (player != null && player.isOnline()) {
            Disguiser.INSTANCE.updateDisguise(player);
            Switcher.doSwitch(player, current, next);
        }
    }

    public UUID getID() {
        return this.player;
    }

    public Map<UUID, Member> getMembers() {
        return new HashMap<>(members);
    }

    public void update(Member member) {
        logger().info("Updating system " + getID() + (member != null ? " because of " + member.getID() : ""));
        if (member != null) this.members.put(member.getID(), member);
        SystemManager.saveSystem(this);
    }

    public boolean isOnline() {
        return Bukkit.getOnlinePlayers().stream().anyMatch(p -> p.getUniqueId().equals(getID()));
    }
}
