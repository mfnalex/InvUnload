package de.jeff_media.InvUnload;

import de.jeff_media.InvUnload.Hooks.ItemsAdderWrapper;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.Container;
import org.bukkit.block.DoubleChest;
import org.bukkit.inventory.DoubleChestInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.ArrayList;

public class BlockUtils {
	
	Main main;
	
	BlockUtils(Main main) {
		this.main=main;
	}
	
	static ArrayList<Block> findBlocksInRadius(Location loc, int radius) {
		ArrayList<Block> blocks = new ArrayList<>();
		for (int x = loc.getBlockX()-radius; x<= loc.getBlockX()+radius;x++) {
			for (int y = loc.getBlockY()-radius; y<= loc.getBlockY()+radius;y++) {
				for (int z = loc.getBlockZ()-radius; z<= loc.getBlockZ()+radius;z++) {
					Block block = loc.getWorld().getBlockAt(x, y, z);
					blocks.add(block);
				}
			}
		}
		return blocks;
	}
	
	static ArrayList<Block> findChestsInRadius(Location loc, int radius) {
		ArrayList<Block> chests = new ArrayList<>();
		for(Block block : findBlocksInRadius(loc, radius)) {
			if(isChestLikeBlock(block)) {
				chests.add(block);
			}
		}
		return chests;
	}
	
	public static boolean isChestLikeBlock(Block block) {
		if(!(block.getState() instanceof Container)) return false;
		String name = block.getType().name();
		switch(name) {
			case "BLAST_FURNACE":
			case "BREWING_STAND":
			case "FURNACE":
			case "HOPPER":
			case "SMOKER":
			case "DROPPER":
			case "DISPENSER":
				return false;
			default:
				return true;
		}
	}

	static boolean doesChestContain(Inventory inv, ItemStack item) {
		ItemsAdderWrapper itemsAdder = Main.getInstance().getItemsAdderWrapper();
		boolean useItemsAdder = Main.getInstance().getConfig().getBoolean("use-itemsadder");
		for (ItemStack otherItem : inv.getContents()) {
			if (otherItem == null) continue;
			if (otherItem.getType() == item.getType()) {

				if (!useItemsAdder) return true;

				// Item ist NOT ItemsAdder item
				if (!itemsAdder.isItemsAdderItem(item)) {

					// Only return true if otherItem also is NOT ItemsAdder item
					if (itemsAdder.isItemsAdderItem(otherItem)) {
						continue;
					} else {
						return true;
					}
				}

				// Item IS ItemsAdder item
				else {
					// But other Item is not
					if (!itemsAdder.isItemsAdderItem(otherItem)) {
						continue;
					}
					// Both are ItemsAdder items
					else {
						if (itemsAdder.getItemsAdderName(item).equals(itemsAdder.getItemsAdderName(otherItem))) {
							return true;
						} else {
							continue;
						}
					}
				}
			}
		}
		return false;
	}

	static void sortBlockListByDistance(ArrayList<Block> blocks, Location loc) {
		blocks.sort((b1, b2)->{
			if (b1.getLocation().distance(loc) > b2.getLocation().distance(loc)) {
				return 1;
			}
			return -1;
		});
	}
	
	static Location getCenterOfBlock(Block block) {
		Location loc = block.getLocation();
		if(block.getState() instanceof Chest
				&& ((Chest)block.getState()).getInventory().getHolder() instanceof DoubleChest) {
			DoubleChest doubleChest = (DoubleChest) ((Chest)block.getState()).getInventory().getHolder();
			DoubleChestInventory doubleChestInv = (DoubleChestInventory) doubleChest.getInventory();
			loc = doubleChestInv.getLeftSide().getLocation().add(doubleChestInv.getRightSide().getLocation()).multiply(0.5);
		}
		loc.add(new Vector(0.5,1,0.5));
		return loc;
	}

	static int doesChestContainCount(Inventory inv, Material mat) {
		int count = 0;
		for(ItemStack item : inv.getContents()) {
			if(item==null) continue;
			if(item.getType() == mat) {
				count += item.getAmount();
			}
		}
		return count;
	}
}
