package de.jeff_media.InvUnload.utils;

import de.jeff_media.InvUnload.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class EnchantmentUtils {

    private final Main main;

    public EnchantmentUtils(Main main) {
        this.main=main;
    }

    public boolean hasMatchingEnchantments(ItemStack first, ItemStack second) {

        if(!main.getConfig().getBoolean("match-enchantments")) {
            if(first.getType() == Material.ENCHANTED_BOOK) {
                if(!main.getConfig().getBoolean("match-enchantments-on-books")) {
                    return true;
                }
            }
        }

        if(!first.hasItemMeta() && !second.hasItemMeta()) return true;

        ItemMeta firstMeta = first.hasItemMeta() ? first.getItemMeta() : Bukkit.getItemFactory().getItemMeta(first.getType());
        ItemMeta secondMeta = second.hasItemMeta() ? second.getItemMeta() : Bukkit.getItemFactory().getItemMeta(second.getType());

        if(!firstMeta.hasEnchants() && !secondMeta.hasEnchants()) return true;

        if(firstMeta.hasEnchants() && !secondMeta.hasEnchants()) return false;
        if(!firstMeta.hasEnchants() && secondMeta.hasEnchants()) return false;

        return firstMeta.getEnchants().equals(secondMeta.getEnchants());
    }

}
