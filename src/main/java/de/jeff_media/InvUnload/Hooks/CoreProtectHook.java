package de.jeff_media.InvUnload.Hooks;

import de.jeff_media.InvUnload.Main;
import net.coreprotect.CoreProtect;
import org.bukkit.Location;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;

public class CoreProtectHook {

    Main main;

    CoreProtectHook(Main main) {
        this.main = main;
    }

    public static void logCoreProtect(Main main, String user, Inventory destination) {

        // TODO: This needs a CoreProtect APi update since its not possible to log what someone put into a chest with the current CP API


        Plugin coreProtectPlugin = main.getServer().getPluginManager().getPlugin("CoreProtect");

        if(coreProtectPlugin==null || !(coreProtectPlugin instanceof CoreProtect)) return;

        Location location = destination.getLocation();

        CoreProtect cp = (CoreProtect) coreProtectPlugin;

        cp.getAPI().logContainerTransaction(user, location);


    }

}
