package de.jeff_media.InvUnload.Hooks;

import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import de.jeff_media.InvUnload.BlockUtils;
import de.jeff_media.InvUnload.Main;

public class ChestSortHook {
	
	final Main main;
	
	public ChestSortHook(Main main) {
		this.main=main;
	}
	
	public boolean shouldSort(Player p) {
		if(!main.useChestSort) return false;
		if(main.getConfig().getBoolean("force-chestsort")) return true;
		return de.jeff_media.chestsort.api.ChestSortAPI.hasSortingEnabled(p);
	}
	
	public void sort(Block block) {
		if(!main.useChestSort) return;
		if(!BlockUtils.isChestLikeBlock(block.getType())) return;
		Inventory inv = ((Container) block.getState()).getInventory();
		de.jeff_media.chestsort.api.ChestSortAPI.sortInventory(inv);
	}

}
