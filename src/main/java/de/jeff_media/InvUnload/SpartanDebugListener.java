package de.jeff_media.InvUnload;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.vagdedes.spartan.api.PlayerViolationEvent;

public class SpartanDebugListener implements Listener {
	
	@EventHandler
	void onSpartan(PlayerViolationEvent e) {
		
		//System.out.println(e.getMessage());
		
	}

}
