package de.jeff_media.InvUnload.Hooks;

import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import de.jeff_media.InvUnload.BlockUtils;
import de.jeff_media.InvUnload.Main;

public class ChestSortHook {
	
	Main main;
	
	public ChestSortHook(Main main) {
		this.main=main;
	}
	
	public boolean shouldSort(Player p) {
		if(main.chestSortAPI == null) return false;
		return main.chestSortAPI.sortingEnabled(p);
	}
	
	public void sort(Block block) {
		if(main.chestSortAPI == null) return;
		if(!BlockUtils.isChestLikeBlock(block)) return;
		Inventory inv = ((Container) block.getState()).getInventory();
		main.chestSortAPI.sortInventory(inv);
	}

}
