package de.jeff_media.InvUnload.utils;

import org.bukkit.inventory.ItemStack;

public class ShulkerUtils {

    public static boolean isShulkerBox(ItemStack itemStack) {
        if(itemStack==null) return false;
        return itemStack.getType().name().contains("SHULKER_BOX");
    }
}
