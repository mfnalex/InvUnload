package de.jeff_media.InvUnload;

import java.util.ArrayList;

import org.bukkit.block.Container;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import de.jeff_media.InvUnload.UnloadSummary.PrintRecipient;

public class InvUtils {
	// TODO: When using /dump, first use /unload and then /dump
	static boolean stuffInventoryIntoAnother(Player p, Inventory destination, boolean onlyMatchingStuff, int startSlot, int endSlot, UnloadSummary summary) {

		Inventory source = p.getInventory();
		
		int start = countInventoryContents(source);
		for(int i = startSlot; i<=endSlot; i++) {
			ItemStack item = source.getItem(i);
			if(MinepacksHook.isMinepacksBackpack(item)) continue;
			if(item == null) continue;
			source.clear(i);
			int amount = item.getAmount();
			if(onlyMatchingStuff==false || BlockUtils.doesChestContain(destination,item.getType())) {
				for(ItemStack leftover : destination.addItem(item).values()) {
					amount = amount - leftover.getAmount();			
					source.setItem(i,leftover);
				}	
			summary.protocolUnload(destination.getLocation(), item.getType(), amount);
			} else {
				source.setItem(i,item);
			}
		}
		return start != countInventoryContents(source);
	}
	
	static ArrayList<ItemStack> inventoryToArrayList(Inventory source) {
		ArrayList<ItemStack> sourceItems = new ArrayList<ItemStack>();
		for(ItemStack item : source.getContents()) {
			if(item==null) continue;
			sourceItems.add(item);
		}
		source.clear();
		return sourceItems;
	}
	
	static int countInventoryContents(Inventory inv) {
		int count = 0;
		for(ItemStack item : inv.getContents()) {
			if(item==null) continue;
			count+=item.getAmount();
		}
		return count;
	}
}
