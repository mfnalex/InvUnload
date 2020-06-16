package de.jeff_media.InvUnload;

import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import de.jeff_media.InvUnload.UnloadSummary.PrintRecipient;
import net.md_5.bungee.api.ChatColor;

public class CommandUnload implements CommandExecutor {
	
	Main main;
	
	public CommandUnload(Main main) {
		this.main=main;
	}

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
			@NotNull String[] args) {
		
		if(args.length>0 && args[0].equalsIgnoreCase("reload")) {
			if(sender.hasPermission("invunload.reload")) {
				main.reloadCompleteConfig();
				sender.sendMessage(ChatColor.GREEN+"InvUnload has been reloaded.");
			} else {
				sender.sendMessage(main.getCommand("unload").getPermissionMessage());
			}
			return true;
		}
		
		if(!(sender instanceof Player)) {
			return true;
		}
		Player p = (Player) sender;
		
		int radius = main.groupUtils.getDefaultRadiusPerPlayer(p);
		int startSlot = 9;
		int endSlot = 35;
		boolean onlyMatchingStuff = false;

		
		if(args.length>0) {
			if(!StringUtils.isNumeric(args[0])) {
				p.sendMessage(main.messages.MSG_NOT_A_NUMBER);
				return true;
			}
			int customRadius = Integer.parseInt(args[0]);
			if(customRadius > main.groupUtils.getMaxRadiusPerPlayer(p)) {
				p.sendMessage(String.format(main.messages.MSG_RADIUS_TOO_HIGH,main.groupUtils.getMaxRadiusPerPlayer(p)));
				return true;
			}
			radius = customRadius;
		}
		
		
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
		
		ArrayList<Block> useableChests = new ArrayList<Block>();
		for(Block block : chests) {
			if(PlayerUtils.canPlayerUseChest(block, p, main)) {
				useableChests.add(block);
			}
		}
		chests = null;
		
		ArrayList<Block> affectedChests = new ArrayList<Block>();
		
		/*for(Block block : chests) {
			if(!PlayerUtils.canPlayerUseChest(block, p)) continue;
			Inventory inv = ((Container) block.getState()).getInventory();
			if(onlyMatchingStuff) {
				if(InvUtils.stuffInventoryIntoAnother(p, inv, true,startSlot,endSlot)) {
					affectedChests.add(block);
				}
			} else {
				if(InvUtils.stuffInventoryIntoAnother(p, inv, true,startSlot,endSlot)
						| InvUtils.stuffInventoryIntoAnother(p, inv, false,startSlot,endSlot)) {
					affectedChests.add(block);
				}
			}
		}*/
		UnloadSummary summary = new UnloadSummary();
		for(Block block : useableChests) {
			Inventory inv = ((Container) block.getState()).getInventory();
			if(InvUtils.stuffInventoryIntoAnother(p, inv, true,startSlot,endSlot,summary)) {
				affectedChests.add(block);
			}
		}
		if(!onlyMatchingStuff) {
			for(Block block : useableChests) {
				Inventory inv = ((Container) block.getState()).getInventory();
				if(InvUtils.stuffInventoryIntoAnother(p, inv, false,startSlot,endSlot,summary)) {
					affectedChests.add(block);
				}
			}
		}
		if(main.getConfig().getBoolean("always-show-summary")) {
			summary.print(PrintRecipient.PLAYER, p);
		}
		
		if(affectedChests.size()==0) {
			p.sendMessage(main.messages.MSG_COULD_NOT_UNLOAD);
			return true;
		} 
		
		main.visualizer.save(p, affectedChests,summary);
		
		for(Block block : affectedChests) {
			main.visualizer.chestAnimation(block,p);
			if(main.getConfig().getBoolean("laser-animation")) {
				main.visualizer.playLaser(affectedChests, p, main.getConfig().getInt("laser-default-duration"));
			}
			if(main.chestSortHook.shouldSort(p)) {
				main.chestSortHook.sort(block);
			}
		}
		
		
		
		
		return true;
	}
}
