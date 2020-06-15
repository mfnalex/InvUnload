package de.jeff_media.InvUnload;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.Container;
import org.bukkit.block.DoubleChest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.DoubleChestInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class BlockUtils {
	
	Main main;
	
	BlockUtils(Main main) {
		this.main=main;
	}
	
	static ArrayList<Block> findBlocksInRadius(Location loc, int radius) {
		ArrayList<Block> blocks = new ArrayList<Block>();
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
		ArrayList<Block> chests = new ArrayList<Block>();
		for(Block block : findBlocksInRadius(loc, radius)) {
			if(isChestLikeBlock(block)) {
				chests.add(block);
			}
		}
		return chests;
	}
	
	public static boolean isChestLikeBlock(Block block) {
		if(!(block.getState() instanceof Container)) return false;
		switch(block.getType()) {
		case BLAST_FURNACE:
		case BREWING_STAND:
		case FURNACE:
		case HOPPER:
		case SMOKER:
			return false;
		default:
			return true;
		}
	}
	
	static boolean doesChestContain(Inventory inv, Material mat) {
		for(ItemStack item : inv.getContents()) {
			if(item==null) continue;
			if(item.getType() == mat) {
				return true;
			}
		}
		return false;
	}
	
	static void sortBlockListByDistance(ArrayList<Block> blocks, Location loc) {
		Collections.sort(blocks, new Comparator<Block>() {
			@Override
			public int compare(Block b1, Block b2) {
				if(b1.getLocation().distance(loc) > b2.getLocation().distance(loc)) {
					return 1;
				}
				return -1;
			}
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
}
