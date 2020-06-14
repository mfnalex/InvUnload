package de.jeff_media.InvUnload;

import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

public class CommandUnload implements CommandExecutor {
	
	Main main;
	
	public CommandUnload(Main main) {
		this.main=main;
	}

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
			@NotNull String[] args) {
		
		int radius = main.defaultChestRadius;
		int startSlot = 9;
		int endSlot = 35;
		
		if(!(sender instanceof Player)) {
			return true;
		}
		Player p = (Player) sender;
		
		if(args.length>0) {
			if(!StringUtils.isNumeric(args[0])) {
				p.sendMessage(main.messages.MSG_NOT_A_NUMBER);
				return true;
			}
			int customRadius = Integer.parseInt(args[0]);
			if(customRadius > main.maxChestRadius) {
				p.sendMessage(main.messages.MSG_RADIUS_TOO_HIGH);
				return true;
			}
			radius = customRadius;
		}
		
		boolean onlyMatchingStuff = false;
		startSlot=9;
		endSlot=35;
		if(command.getName().equalsIgnoreCase("unload")) {
			onlyMatchingStuff = true;
		} else if(command.getName().equalsIgnoreCase("dump")) {
			onlyMatchingStuff = false;
		}
		
		ArrayList<Block> chests = BlockUtils.findChestsInRadius(p.getLocation(), radius);
		if(chests.size()==0) {
			p.sendMessage(main.messages.MSG_NO_CHESTS_NEARBY);
			return true;
		}
		BlockUtils.sortBlockListByDistance(chests, p.getLocation());
		
		ArrayList<Block> affectedChests = new ArrayList<Block>();
		
		for(Block block : chests) {
			if(!PlayerUtils.canPlayerUseChest(block, p)) continue;
			Inventory inv = ((Container) block.getState()).getInventory();
			if(onlyMatchingStuff) {
				if(InvUtils.stuffInventoryIntoAnother(p.getInventory(), inv, onlyMatchingStuff,startSlot,endSlot)) {
					affectedChests.add(block);
				}
			} else {
				if(InvUtils.stuffInventoryIntoAnother(p.getInventory(), inv, false,startSlot,endSlot)
						|| InvUtils.stuffInventoryIntoAnother(p.getInventory(), inv, true,startSlot,endSlot)) {
					affectedChests.add(block);
				}
			}
		}
		
		if(affectedChests.size()==0) {
			p.sendMessage(main.messages.MSG_COULD_NOT_UNLOAD);
			return true;
		} 
			
		for(Block block : affectedChests) {
			main.blockUtils.chestAnimation(block,p);
			if(main.chestSortHook.shouldSort(p)) {
				main.chestSortHook.sort(block);
			}
		}
		
		return true;
	}

}
