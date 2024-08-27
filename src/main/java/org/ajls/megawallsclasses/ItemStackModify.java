package org.ajls.megawallsclasses;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import java.util.ArrayList;
import java.util.List;

public class ItemStackModify {
    //unbreakable
    public static ItemStack setUnbreakable(org.bukkit.inventory.ItemStack itemStack){
        // set unbreakable
        ItemMeta meta = itemStack.getItemMeta();
        meta.setUnbreakable(true);
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public static ItemStack setDisplayName(ItemStack itemstack, String name) {
        ItemMeta meta = itemstack.getItemMeta();
        meta.setDisplayName(name);
        itemstack.setItemMeta(meta);
        return itemstack;
    }

    public static ItemStack setLore(ItemStack itemStack, String lore) {
        ItemMeta meta = itemStack.getItemMeta();
        List<String> loresList = new ArrayList<String>();
        loresList.add(lore);
        meta.setLore(loresList);
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public static ItemStack addLore(ItemStack itemStack, String lore) {
        ItemMeta meta = itemStack.getItemMeta();
        List<String> loresList = new ArrayList<>();
        if (meta.getLore() != null) {
            loresList = meta.getLore();
        }
        loresList.add(lore);
        meta.setLore(loresList);
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public static ItemStack setMaxStackSize(ItemStack itemStack, int maxStackSize) {
        if (itemStack.hasItemMeta()) {
            ItemMeta meta = itemStack.getItemMeta();
            meta.setMaxStackSize(maxStackSize);
            itemStack.setItemMeta(meta);
        }
        return itemStack;
    }

    public static ItemStack setClassItem(ItemStack itemStack) {
        addLore(itemStack, "classItem");
        return itemStack;
    }

    public static boolean containsLore(ItemStack itemStack, String lore) {
        if (itemStack == null) return false;
        ItemMeta meta = itemStack.getItemMeta();
        List<String> loresList = meta.getLore();
        if (loresList == null) return false;
        return loresList.contains(lore);
    }

    public static ItemStack setEffect(ItemStack itemstack, PotionEffectType effectType, int duration, int amplifier) {
        PotionMeta meta = (PotionMeta) itemstack.getItemMeta();
        meta.addCustomEffect(new PotionEffect(effectType, duration, amplifier), true);
        itemstack.setItemMeta(meta);
        return itemstack;
    }

    public static ItemStack setBasePotionTye(ItemStack itemstack, PotionType potionType) {
        PotionMeta meta = (PotionMeta) itemstack.getItemMeta();
        meta.setBasePotionType(potionType);
        itemstack.setItemMeta(meta);
        return itemstack;
    }

    public static ItemStack setAttributeAttackDamage(ItemStack itemstack, int amountPts) {
        ItemMeta meta = itemstack.getItemMeta();
        meta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, new AttributeModifier("generic.attackDamage", amountPts, AttributeModifier.Operation.ADD_NUMBER));
        itemstack.setItemMeta(meta);
        return itemstack;
    }

    public static ItemStack removeAmount(ItemStack itemStack, int amountPts) {
        if (itemStack != null) {
            int itemStackAmount = itemStack.getAmount();
            itemStackAmount -= amountPts;
            if (itemStackAmount < 0) {
                itemStackAmount = 0;
            }
            itemStack.setAmount(itemStackAmount);  //itemStackAmount > 0 ? itemStackAmount : 0
        }
        return itemStack;
    }

}
