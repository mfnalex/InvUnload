package de.jeff_media.InvUnload;

import java.util.ArrayList;
import java.util.List;

import de.jeff_media.InvUnload.utils.CoolDown;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import de.jeff_media.InvUnload.UnloadSummary.PrintRecipient;
import net.md_5.bungee.api.ChatColor;
import org.jetbrains.annotations.Nullable;

public class CommandUnload implements CommandExecutor , TabCompleter {
	
	final Main main;
	
	public CommandUnload(Main main) {
		this.main=main;
	}

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
			@NotNull String[] args) {

/*		if(args.length > 1 && sender.hasPermission("invunload.others")) {
			Player player = Bukkit.getPlayer(args[1]);
			if(player == null) {
				sender.sendMessage("Could not find player " + args[1]);
				Bukkit.dispatchCommand(player,command.getName() + " " + args[0]);
				return true;
			}
		}*/
		
		//long startTime = System.nanoTime();
		
		if(args.length>0 && args[0].equalsIgnoreCase("reload")) {
			if(sender.hasPermission("invunload.reload")) {
				main.reloadCompleteConfig(true);
				sender.sendMessage(ChatColor.GREEN+"InvUnload has been reloaded.");
			} else {
				sender.sendMessage(main.getCommand("unload").getPermissionMessage());
			}
			return true;
		}

		if(!(sender instanceof Player)) {
			return true;
		}

		if(!CoolDown.check(sender)) {
			return true;
		}

		Player p = (Player) sender;
		PlayerSetting setting = main.getPlayerSetting(p);

		if(args.length>0 && args[0].equalsIgnoreCase("hotbar")) {
			if(command.getName().equals("unload")) {
				setting.unloadHotbar = !setting.unloadHotbar;
				if(setting.unloadHotbar) { p.sendMessage(String.format(main.messages.MSG_WILL_USE_HOTBAR,"/"+label.toLowerCase())); }
								else { p.sendMessage(String.format(main.messages.MSG_WILL_NOT_USE_HOTBAR,"/"+label.toLowerCase())); }
			} else if(command.getName().equals("dump")) {
				setting.dumpHotbar = !setting.dumpHotbar;
				if(setting.dumpHotbar) { p.sendMessage(String.format(main.messages.MSG_WILL_USE_HOTBAR,"/"+label.toLowerCase())); }
				else { p.sendMessage(String.format(main.messages.MSG_WILL_NOT_USE_HOTBAR,"/"+label.toLowerCase())); }
			}
			return true;
		}
		

		
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
			startSlot = setting.unloadHotbar ? 0 : 9;
		} else if(command.getName().equalsIgnoreCase("dump")) {
			onlyMatchingStuff = false;
			startSlot = setting.dumpHotbar ? 0 : 9;
		}
		
		List<Block> chests = BlockUtils.findChestsInRadius(p.getLocation(), radius);
		if(chests.size()==0) {
			p.sendMessage(main.messages.MSG_NO_CHESTS_NEARBY);
			return true;
		}
		BlockUtils.sortBlockListByDistance(chests, p.getLocation());
		
		ArrayList<Block> useableChests = new ArrayList<>();
		for(Block block : chests) {
			if(PlayerUtils.canPlayerUseChest(block, p, main)) {
				useableChests.add(block);
			}
		}
		chests = null;
		
		ArrayList<Block> affectedChests = new ArrayList<>();
		UnloadSummary summary = new UnloadSummary();

		// Unload
		for(Block block : useableChests) {
			Inventory inv = ((Container) block.getState()).getInventory();
			if(InvUtils.stuffInventoryIntoAnother(main,p, inv, true,startSlot,endSlot,summary)) {
				affectedChests.add(block);
			}
		}

		//Dump
		if(!onlyMatchingStuff) {
			for(Block block : useableChests) {
				Inventory inv = ((Container) block.getState()).getInventory();
				if(InvUtils.stuffInventoryIntoAnother(main,p, inv, false,startSlot,endSlot,summary)) {
					affectedChests.add(block);
				}
			}
		}
		if(main.getConfig().getBoolean("always-show-summary") && main.canSeeCoordinates(p)) {
			summary.print(PrintRecipient.PLAYER, p);
		}
		
		if(affectedChests.size()==0) {
			BlackList blackList = main.getPlayerSetting(p).getBlacklist();
			// TODO: Fix this. Right now the blacklist message is disabled
			//boolean everythingBlackListed = true;
			boolean everythingBlackListed = false;
			for(int i = startSlot; i <= endSlot; i++) {
				ItemStack item = p.getInventory().getItem(i);
				if(item==null || item.getAmount()==0 || item.getType()== Material.AIR) continue;
				if(!blackList.contains(item.getType())) {
					everythingBlackListed=false;
				}
			}
			p.sendMessage(everythingBlackListed ? main.messages.MSG_COULD_NOT_UNLOAD_BLACKLIST : main.messages.MSG_COULD_NOT_UNLOAD);
			return true;
		}

		main.visualizer.save(p, affectedChests,summary);
		
		for(Block block : affectedChests) {
			main.visualizer.chestAnimation(block,p);
			if(main.getConfig().getBoolean("laser-animation")) {
				main.visualizer.play(p);
			}
			if(main.chestSortHook.shouldSort(p)) {
				main.chestSortHook.sort(block);
			}
		}

		if(main.getConfig().getBoolean("play-sound")) {
			if(main.getConfig().getBoolean("error-sound")) {
				main.getLogger().warning("Cannot play sound, because sound effect \""+main.getConfig().getString("sound-effect")+"\" does not exist! Please check your config.yml");
			}
			else {
				final Sound sound = Sound.valueOf(main.getConfig().getString("sound-effect").toUpperCase());
				p.playSound(p.getLocation(), sound, (float) main.getConfig().getDouble("sound-volume",1.0), 1);
			}
		}

		return true;
	}

	@Override
	public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
		if(strings.length>1) return null;
		List<String> list = new ArrayList<>();
		list.add("hotbar");
		if(strings.length==0) return list;
		if("hotbar".startsWith(strings[0].toLowerCase())) return list;
		return null;
	}
}
