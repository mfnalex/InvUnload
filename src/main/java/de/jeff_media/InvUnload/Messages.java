package de.jeff_media.InvUnload;

import org.bukkit.ChatColor;

public class Messages {
	Main plugin;

	final String PREFIX, MSG_COULD_NOT_UNLOAD, MSG_RADIUS_TOO_HIGH, MSG_NOT_A_NUMBER, MSG_NO_CHESTS_NEARBY;

	Messages(Main plugin) {
		this.plugin = plugin;
		
		PREFIX = ChatColor.GOLD + "[InvUnload] "+ChatColor.RESET;

		MSG_COULD_NOT_UNLOAD = PREFIX + ChatColor.translateAlternateColorCodes('&', plugin.getConfig()
				.getString("message-could-not-unload", "&7There was nothing to unload. Maybe you have no chests for the remaining items?"));
		
		MSG_RADIUS_TOO_HIGH = PREFIX + ChatColor.translateAlternateColorCodes('&', String.format(plugin.getConfig()
				.getString("message-radius-too-high","&cError:&7 The radius cannot be higher than %d blocks."),plugin.getConfig().getInt("max-chest-radius",20)));
		
		MSG_NOT_A_NUMBER = PREFIX + ChatColor.translateAlternateColorCodes('&', plugin.getConfig()
				.getString("message-error-not-a-number", "&cError:&7 '%s' is not a valid number."));
		
		MSG_NO_CHESTS_NEARBY = PREFIX+ ChatColor.translateAlternateColorCodes('&', plugin.getConfig()
				.getString("message-no-chests-nearby","&7There are no chests nearby. Adjust the radius or walk closer to your chests."));
		
		//MSG_INVENTORY_EMPTY = PREFIX+ ChatColor.translateAlternateColorCodes('&', plugin.getConfig()
		//		.getString("message-inventory-empty","&7Nothing to dump: Your inventory was already empty."));
		
		
	}
}
