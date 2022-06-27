package de.jeff_media.InvUnload.Hooks;

import org.bukkit.event.player.PlayerInteractEvent;

public class SpartanHook {

	private static boolean spartanInstalled = false;

	static {
		try {
			Class.forName("me.vagdedes.spartan.api.CheckCancelEvent");
			spartanInstalled = true;
		} catch (ClassNotFoundException ex) {

		}
	}
	
	// Spartan cancels the PlayerInteractEvent that InvUnload generates to check if a player may use chest
	// if the player is more than a few blocks away from that chest, so we have to cancel the cancellation event
	
	public static void cancelSpartanEventCancel(PlayerInteractEvent e) {
		
		//System.out.println("Trying to cancel Spartan Event...");
		
		if(spartanInstalled) {
			e.setCancelled(false);
		}
		
		//System.out.println("Cancelled!");

	}

}
