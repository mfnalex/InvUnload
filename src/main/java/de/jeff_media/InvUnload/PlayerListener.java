package de.jeff_media.InvUnload;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class PlayerListener implements Listener {

    Main main;

    PlayerListener(Main main) {
        this.main=main;
    }

    void test() {
        Player p = Bukkit.getPlayer("asd");
        p.openInventory(p.getInventory());
    }

}
