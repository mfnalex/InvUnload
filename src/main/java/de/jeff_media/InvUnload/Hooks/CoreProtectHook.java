package de.jeff_media.InvUnload.Hooks;

import de.jeff_media.InvUnload.Main;
import net.coreprotect.CoreProtect;
import org.bukkit.plugin.Plugin;

public class CoreProtectHook {

    public static void logCoreProtect(Main main) {

        // TODO: This needs a CoreProtect APi update since its not possible to log what someone put into a chest with the current CP API
    }

        /*Plugin coreProtectPlugin = main.getServer().getPluginManager().getPlugin("CoreProtect");

        if(coreProtectPlugin==null || !(coreProtectPlugin instanceof CoreProtect)) return;

        CoreProtect cp = (CoreProtect) coreProtectPlugin;

        cp.getAPI().logContainerTransaction(String user, Location location)
                cp.getAPI().logPlacement(String user,Location loc, Material type, BlockData data)


    }*/

}
