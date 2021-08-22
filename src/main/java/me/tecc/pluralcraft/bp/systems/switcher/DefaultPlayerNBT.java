/*
 * Copyright (c) 2021 tecc
 * This code is licenced under the MIT licence.
 */

package me.tecc.pluralcraft.bp.systems.switcher;

import me.tecc.pluralcraft.bp.util.NBTInventory;
import me.tecc.pluralcraft.bp.util.ServerProperties;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.PlayerInventory;

import java.util.Objects;

public class DefaultPlayerNBT extends PlayerNBT {
    public DefaultPlayerNBT() {
        super(null);
    }

    @Override
    public GameMode getGameMode() {
        return ServerProperties.getGameMode();
    }

    @Override
    public String getWorldName() {
        return ServerProperties.getLevelName();
    }

    @Override
    public Location getLocation() {
        return Objects.requireNonNull(Bukkit.getWorld(getWorldName())).getSpawnLocation();
    }

    private final NBTInventory inv = new NBTInventory();
    @Override
    public NBTInventory getInventory() {
        return inv;
    }


    @Override
    public Location getSpawnLocation() {
        return null;
    }

    @Override
    public double getHealth() {
        return 20f;
    }

    @Override
    public int getTotalExperience() {
        return 0;
    }

    @Override
    public int getLevel() {
        return 0;
    }

    @Override
    public float getExp() {
        return 0;
    }

    @Override
    public Abilities getAbilities() {
        return new Abilities() {
            @Override
            public boolean isFlying() {
                return false;
            }

            @Override
            public float getFlySpeed() {
                return 0.05f;
            }

            @Override
            public boolean canInstabuild() {
                return false;
            }

            @Override
            public boolean isInvulnerable() {
                return false;
            }

            @Override
            public boolean mayBuild() {
                return true;
            }

            @Override
            public boolean mayFly() {
                return false;
            }

            @Override
            public float getWalkSpeed() {
                return 0.1f;
            }
        };
    }


}
