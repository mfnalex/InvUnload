package de.jeff_media.InvUnload;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.InventoryView;

import de.jeff_media.InvUnload.Hooks.PlotSquaredHook;

public class PlayerUtils {

	// Calls PlayerInteractEvent to see if access is blocked by 3rd party plugins
	static boolean canPlayerUseChest(Block block, Player player, Main main) {
		PlayerInteractEvent event = new PlayerInteractEvent(player,
				Action.RIGHT_CLICK_BLOCK, null, block, BlockFace.UP);
		Bukkit.getPluginManager().callEvent(event);
		if(event.useInteractedBlock() == Event.Result.DENY) {
			return false;
		}
		if(main.plotSquaredHook.isBlockedByPlotSquared(block, player)) {
			return false;
		}
		return true;
	}
}
