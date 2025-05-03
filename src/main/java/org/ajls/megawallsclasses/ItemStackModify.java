package org.ajls.megawallsclasses;

import net.kyori.adventure.key.Key;
import org.ajls.lib.utils.ItemStackU;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.persistence.PersistentDataType;
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

    public static String getDisplayName(ItemStack itemstack) {
        ItemMeta meta = itemstack.getItemMeta();
        return meta.getDisplayName();
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
        ItemStackU.setStringPersistentData(itemStack, NameSpacedKeys.CLASS_ITEM, "classItem");
        return itemStack;
    }

    public static boolean isClassItem(ItemStack itemStack) {
        return ItemStackU.hasStringPersistentData(itemStack, NameSpacedKeys.CLASS_ITEM);
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

    public static ItemStack setAttributeAttackDamage(ItemStack itemStack, int amountPts) {
//        ItemMeta meta = itemstack.getItemMeta();
//        meta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, new AttributeModifier("generic.attackDamage", amountPts, AttributeModifier.Operation.ADD_NUMBER));
//        itemstack.setItemMeta(meta);
//        return itemstack;
        return ItemStackU.setAttributeAttackDamage(itemStack, amountPts);
    }

    public static ItemStack setAttributePlayerBlockRange(ItemStack itemStack, int amountPts) {
        return ItemStackU.setAttributePlayerBlockRange(itemStack, amountPts);
        /*
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.addAttributeModifier(Attribute.BLOCK_INTERACTION_RANGE, new AttributeModifier("generic.playerBlockRange", amountPts, AttributeModifier.Operation.ADD_NUMBER));  //PLAYER_BLOCK_INTERACTION_RANGE
        itemStack.setItemMeta(itemMeta);
        return itemStack;

         */
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

    public static ItemStack setStringPersistentData(ItemStack itemStack, NamespacedKey namespacedKey, String value) {
        if (itemStack == null) return itemStack;
        if (!itemStack.hasItemMeta()) return itemStack;



        ItemMeta itemMeta = itemStack.getItemMeta();


        itemMeta.getPersistentDataContainer().set(namespacedKey, PersistentDataType.STRING, value);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static String getStringPersistentData(ItemStack itemStack, NamespacedKey namespacedKey) {
        if (itemStack == null) return null;
        if (!itemStack.hasItemMeta()) return null;

        ItemMeta itemMeta = itemStack.getItemMeta();


        return itemMeta.getPersistentDataContainer().get(namespacedKey, PersistentDataType.STRING);
//        return itemStack;
    }

    public static ItemStack setPersistentData(ItemStack itemStack, NamespacedKey namespacedKey, PersistentDataType persistentDataType, Object value) {
        if (itemStack == null) return itemStack;
//        if (!itemStack.hasItemMeta()) return itemStack;

        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.getPersistentDataContainer().set(namespacedKey, persistentDataType, value);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static boolean containsPersistentData(ItemStack itemStack, NamespacedKey namespacedKey, PersistentDataType persistentDataType) {
        if (itemStack == null) return false;
        if (!itemStack.hasItemMeta()) return false;

        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta.getPersistentDataContainer().get(namespacedKey, persistentDataType) == null) return false;
//        itemMeta.getPersistentDataContainer().getKeys().contains()
        return true;

//        itemStack.setItemMeta(itemMeta);
//        return itemStack;
    }



//    public static PersistentDataType translateClassToPersistentDataType(Object value) {
//        return PersistentDataType
//    }


}
