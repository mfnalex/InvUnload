package de.jeff_media.InvUnload;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerListener implements Listener {

    Main main;

    PlayerListener(Main main) {
        this.main=main;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        main.updateChecker.sendUpdateMessage(e.getPlayer());
    }
}
