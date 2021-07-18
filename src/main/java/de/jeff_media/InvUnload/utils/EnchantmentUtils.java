package de.jeff_media.InvUnload.utils;

import de.jeff_media.InvUnload.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Map;

public class EnchantmentUtils {

    private final Main main;

    public EnchantmentUtils(Main main) {
        this.main=main;
    }

    public boolean hasMatchingEnchantments(ItemStack first, ItemStack second) {

        if(!main.getConfig().getBoolean("match-enchantments") && !main.getConfig().getBoolean("match-enchantments-on-books")) {
            //System.out.println(1);
            return true;
        }

        if(!main.getConfig().getBoolean("match-enchantments") && main.getConfig().getBoolean("match-enchantments-on-books")) {
            if(first.getType() != Material.ENCHANTED_BOOK) {
                //System.out.println(2);
                return true;
            }
        }


        //System.out.println(4);
        if(!first.hasItemMeta() && !second.hasItemMeta()) return true;
        //System.out.println(5);
        ItemMeta firstMeta = first.hasItemMeta() ? first.getItemMeta() : Bukkit.getItemFactory().getItemMeta(first.getType());
        ItemMeta secondMeta = second.hasItemMeta() ? second.getItemMeta() : Bukkit.getItemFactory().getItemMeta(second.getType());

        if(firstMeta instanceof EnchantmentStorageMeta && secondMeta instanceof EnchantmentStorageMeta) {
            EnchantmentStorageMeta firstStorage = (EnchantmentStorageMeta) firstMeta;
            EnchantmentStorageMeta secondStorage = (EnchantmentStorageMeta) secondMeta;
            if(firstStorage.getStoredEnchants().size() != secondStorage.getStoredEnchants().size()) {
                return false;
            }
            for(Map.Entry<Enchantment,Integer> enchantment : firstStorage.getStoredEnchants().entrySet()) {
                if(!secondStorage.getStoredEnchants().containsKey(enchantment.getKey())) {
                    return false;
                }
                if(secondStorage.getStoredEnchantLevel(enchantment.getKey()) != enchantment.getValue()) {
                    return false;
                }
            }
            return true;
        }


        //System.out.println(6);
        if(!firstMeta.hasEnchants() && !secondMeta.hasEnchants()) return true;
        //System.out.println(7);
        if(firstMeta.hasEnchants() && !secondMeta.hasEnchants()) return false;
        //System.out.println(8);
        if(!firstMeta.hasEnchants() && secondMeta.hasEnchants()) return false;
        //System.out.println(9);

        return firstMeta.getEnchants().equals(secondMeta.getEnchants());
    }

}
