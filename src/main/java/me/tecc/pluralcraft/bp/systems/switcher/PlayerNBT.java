/*
 * Copyright (c) 2021 tecc
 * This code is licenced under the MIT licence.
 */

package me.tecc.pluralcraft.bp.systems.switcher;

import de.tr7zw.changeme.nbtapi.*;
import me.tecc.pluralcraft.bp.util.IBP;
import me.tecc.pluralcraft.bp.util.NBTInventory;
import me.tecc.pluralcraft.bp.util.ServerProperties;
import me.tecc.pluralcraft.bp.util.Util;
import org.bukkit.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class PlayerNBT {
    private NBTCompound nbt;

    public PlayerNBT(NBTCompound nbt) {
        if (nbt == null) return;
        this.nbt = nbt;
        if (!nbt.hasKey("UUID")
                || getLocation() == null
                || getGameMode() == null) {
            throw new RuntimeException("Invalid compound!");
        }
    }

    public GameMode getGameMode() {
        if (!nbt.hasKey("playerGameMode")) return null;
        GameMode gameMode;
        byte rawMode = nbt.getByte("playerGameMode");
        switch (rawMode) {
            case 0:
                gameMode = GameMode.SURVIVAL;
                break;
            case 1:
                gameMode = GameMode.CREATIVE;
                break;
            case 2:
                gameMode = GameMode.ADVENTURE;
                break;
            case 3:
                gameMode = GameMode.SPECTATOR;
                break;
            default:
                throw new RuntimeException("Mode value supplied is invalid: " + rawMode);
        }
        return gameMode;
    }

    public String getWorldName() {
        return Util.worldName(nbt.getString("Dimension"));
    }

    public Location getLocation() {
        if (!nbt.hasKey("Pos")) return null;
        NBTList<Double> nbtPos = nbt.getDoubleList("Pos");
        double
                x = nbtPos.get(0),
                y = nbtPos.get(1),
                z = nbtPos.get(2);
        // rotation
        NBTList<Float> nbtRotation = nbt.getFloatList("Rotation");
        float
                yaw = nbtRotation.get(0),
                pitch = nbtRotation.get(1);

        World world = null;
        try {
            world = Bukkit.getWorld(getWorldName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        Location loc = new Location(world, x, y, z, yaw, pitch);
        // IBP.getLogger().info("getLocation: " + loc);
        return loc;
    }

    public Location getSpawnLocation() {
        if (!nbt.hasKey("SpawnX")) return null;
        int
                x = nbt.getInteger("SpawnX"),
                y = nbt.getInteger("SpawnY"),
                z = nbt.getInteger("SpawnZ");

        World world = null;
        try {
            String dim = nbt.getString("SpawnDimension");
            if (dim != null) world = Bukkit.getWorld(Util.worldName(dim));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new Location(world, x, y, z);
    }

    public NBTInventory getInventory() {
        NBTCompoundList list = nbt.getCompoundList("Inventory");

        NBTInventory inv = new NBTInventory();
        for (NBTCompound compound : list) {
            PlayerItem item = PlayerItem.of(compound);
            if (item.getInventorySlot() != -1) {
                inv.setItem(item.getInventorySlot(), item.getStack());
            } else if (item.getEquipmentSlot() != null) {
                switch (item.getEquipmentSlot()) {
                    case FEET -> inv.setBoots(item.getStack());
                    case LEGS -> inv.setLeggings(item.getStack());
                    case CHEST -> inv.setChestplate(item.getStack());
                    case HEAD -> inv.setHelmet(item.getStack());
                }
            } else if (item.isOffhand()) {
                inv.setItemInOffHand(item.getStack());
            }
        }

        NBTCompoundList enderList = nbt.getCompoundList("EnderItems");
        for (NBTCompound compound : enderList) {
            ItemStack stack = NBTItem.convertNBTtoItem(compound);
            inv.setEnderItem(compound.getInteger("Slot"), stack);
        }

        inv.setHeldItemSlot(nbt.getInteger("SelectedItemSlot"));

        return inv;
    }

    public double getHealth() {
        return nbt.getDouble("Health");
    }

    public int getTotalExperience() {
        return nbt.getInteger("XpTotal");
    }

    public int getLevel() {
        return nbt.getInteger("XpLevel");
    }

    public float getExp() {
        return nbt.getFloat("XpP");
    }

    public Abilities getAbilities() {
        return Abilities.of(nbt.getCompound("abilities"));
    }

    private interface PlayerItem {
        static PlayerItem of(NBTCompound compound) {
            ItemStack is = NBTItem.convertNBTtoItem(compound);
            int slot = compound.getInteger("Slot");
            return new PlayerItem() {
                @Override
                public ItemStack getStack() {
                    return is;
                }

                @Override
                public int getRawSlot() {
                    return slot;
                }
            };
        }

        ItemStack getStack();

        int getRawSlot();

        default int getInventorySlot() {
            if (getRawSlot() < 0 || getRawSlot() > 35) return -1;
            return getRawSlot();
        }

        default boolean isOffhand() {
            return getRawSlot() == -106;
        }

        default ArmorSlot getEquipmentSlot() {
            if (getRawSlot() < 100 || getRawSlot() > 103) return null;
            switch (getRawSlot()) {
                case 100:
                    return ArmorSlot.FEET;
                case 101:
                    return ArmorSlot.LEGS;
                case 102:
                    return ArmorSlot.CHEST;
                case 103:
                    return ArmorSlot.HEAD;
            }
            throw new IllegalStateException("what?!");
        }
    }

    enum ArmorSlot {
        LEGS, CHEST, HEAD, FEET

    }

    public interface Abilities {
        static Abilities of(NBTCompound abilities) {
            return new Abilities() {
                @Override
                public boolean isFlying() {
                    return abilities.getBoolean("flying");
                }

                @Override
                public float getFlySpeed() {
                    return abilities.getFloat("flySpeed");
                }

                @Override
                public boolean canInstabuild() {
                    return abilities.getBoolean("instabuild");
                }

                @Override
                public boolean isInvulnerable() {
                    return abilities.getBoolean("invulnerable");
                }

                @Override
                public boolean mayBuild() {
                    return abilities.getBoolean("mayBuild");
                }

                @Override
                public boolean mayFly() {
                    return abilities.getBoolean("mayfly");
                }

                @Override
                public float getWalkSpeed() {
                    return abilities.getFloat("walkSpeed");
                }
            };
        }

        boolean isFlying();

        float getFlySpeed();

        boolean canInstabuild();

        boolean isInvulnerable();

        boolean mayBuild();

        boolean mayFly();

        float getWalkSpeed();
    }
}
