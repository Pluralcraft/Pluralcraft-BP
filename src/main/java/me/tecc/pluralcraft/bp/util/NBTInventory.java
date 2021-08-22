/*
 * Copyright (c) 2021 tecc
 * This code is licenced under the MIT licence.
 */

package me.tecc.pluralcraft.bp.util;

import org.bukkit.inventory.ItemStack;

public class NBTInventory {
    private ItemStack[] items = new ItemStack[36];
    private ItemStack boots;
    private ItemStack leggings;
    private ItemStack chestplate;
    private ItemStack helmet;
    private ItemStack itemInOffHand;
    private int heldItemSlot;
    private ItemStack[] enderContents = new ItemStack[27];

    public void setItem(int inventorySlot, ItemStack stack) {
        items[inventorySlot] = stack;
    }

    public ItemStack[] getContents() {
        return items;
    }

    public void setBoots(ItemStack boots) {
        this.boots = boots;
    }

    public ItemStack getBoots() {
        return boots;
    }

    public void setLeggings(ItemStack leggings) {
        this.leggings = leggings;
    }

    public ItemStack getLeggings() {
        return leggings;
    }

    public void setChestplate(ItemStack chestplate) {
        this.chestplate = chestplate;
    }

    public ItemStack getChestplate() {
        return chestplate;
    }

    public void setHelmet(ItemStack helmet) {
        this.helmet = helmet;
    }

    public ItemStack getHelmet() {
        return helmet;
    }

    public void setItemInOffHand(ItemStack itemInOffHand) {
        this.itemInOffHand = itemInOffHand;
    }

    public ItemStack getItemInOffHand() {
        return itemInOffHand;
    }

    public int getHeldItemSlot() {
        return heldItemSlot;
    }

    public void setHeldItemSlot(int heldItemSlot) {
        this.heldItemSlot = heldItemSlot;
    }

    public void setEnderItem(int slot, ItemStack itemStack) {
        enderContents[slot] = itemStack;
    }
    public ItemStack[] getEnderContents() {
        return enderContents;
    }
}
