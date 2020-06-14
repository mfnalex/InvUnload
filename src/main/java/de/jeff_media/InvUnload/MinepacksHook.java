package de.jeff_media.InvUnload;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import at.pcgamingfreaks.Minepacks.Bukkit.API.MinepacksPlugin;

public class MinepacksHook {
	
	static boolean isMinepacksBackpack(ItemStack item) {
	    Plugin bukkitPlugin = Bukkit.getPluginManager().getPlugin("Minepacks");
	    if(!(bukkitPlugin instanceof MinepacksPlugin)) {
	    	// Do something if Minepacks is not available
	        return false;
	    }
	    MinepacksPlugin minepacks = (MinepacksPlugin) bukkitPlugin;
	    return minepacks.isBackpackItem(item);
	}

}
