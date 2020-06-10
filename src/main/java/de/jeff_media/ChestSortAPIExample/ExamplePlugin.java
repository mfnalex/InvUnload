package de.jeff_media.ChestSortAPIExample;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;

import de.jeff_media.ChestSort.ChestSortAPI;
import de.jeff_media.ChestSort.ChestSortEvent;
import de.jeff_media.ChestSort.ChestSortPlugin;

public class ExamplePlugin extends JavaPlugin implements Listener {
	
	ChestSortAPI chestSortAPI;
	
	public void onEnable() {
		
		ChestSortPlugin chestSort = (ChestSortPlugin) getServer().getPluginManager().getPlugin("ChestSort");
		if(chestSort==null || !(chestSort instanceof ChestSortPlugin)) {
			getLogger().severe("Error: ChestSort is not installed.");
			return;
		}
		
		chestSortAPI = chestSort.getAPI();
		
		getServer().getPluginManager().registerEvents(this, this);
	}
	
	@EventHandler
	public void onChestSortEvent(ChestSortEvent event) {
		getServer().broadcastMessage("\nCancellable ChestSortEvent invoked!");
		getServer().broadcastMessage("- Inventory: " + event.getInventory());
		if(event.getPlayer()!=null) getServer().broadcastMessage("- Player: " + event.getPlayer().getName());
		if(event.getLocation()!=null) getServer().broadcastMessage("- Location: " + event.getLocation());
		getServer().broadcastMessage("To avoid having this inventory sorted, simply cancel this event.");
	}
	
	// Sort player inventory every time he moves.
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		chestSortAPI.sortInventory(event.getPlayer().getInventory());
	}

}
