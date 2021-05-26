package de.jeff_media.InvUnload;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class InvUtils {
	static boolean searchItemInContainers(Material mat, Inventory destination, UnloadSummary summary) {
		if (BlockUtils.doesChestContain(destination, new ItemStack(mat))) {
			int amount = BlockUtils.doesChestContainCount(destination, mat);

			summary.protocolUnload(destination.getLocation(), mat, amount);
			return true;
		}
		return false;
	}
	
	static ArrayList<ItemStack> inventoryToArrayList(Inventory source) {
		ArrayList<ItemStack> sourceItems = new ArrayList<>();
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

	/**
	 * Part of API. Puts everything from the player inventory inside the destination inventory.
	 * @param p Player from whom to take the items
	 * @param destination Destination inventory
	 * @param onlyMatchingStuff When true, only move items that already are inside the destination inventory
	 * @param startSlot Do not modify player inventory before this slot
	 * @param endSlot Do not modify player inventory after this slot
	 * @param summary UnloadSummary object. Can be null
	 * @return
	 */
		public static boolean stuffInventoryIntoAnother(@NotNull Main main, @NotNull  Player p, @NotNull Inventory destination, @NotNull boolean onlyMatchingStuff, @NotNull int startSlot, @NotNull int endSlot, @Nullable UnloadSummary summary) {

		Inventory source = p.getInventory();
		BlackList blackList = main.getPlayerSetting(p).getBlacklist();

		int start = countInventoryContents(source);
		for(int i = startSlot; i<=endSlot; i++) {
			ItemStack item = source.getItem(i);
			if (item == null) continue;
			if (MinepacksHook.isMinepacksBackpack(item)) continue;
			if (main.inventoryPagesHook.isButton(item)) continue;
			if (blackList.contains(item.getType())) continue;
			source.clear(i);
			int amount = item.getAmount();
			if (!onlyMatchingStuff || BlockUtils.doesChestContain(destination, item)) {
				main.coreProtectHook.logCoreProtect(p.getName(), destination);
				for (ItemStack leftover : destination.addItem(item).values()) {
					amount = amount - leftover.getAmount();
					source.setItem(i, leftover);
				}
				if (summary != null) summary.protocolUnload(destination.getLocation(), item.getType(), amount);
			} else {
				source.setItem(i, item);
			}
		}
		return start != countInventoryContents(source);
	}
}
