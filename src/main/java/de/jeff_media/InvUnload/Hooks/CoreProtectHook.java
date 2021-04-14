package de.jeff_media.InvUnload.Hooks;

import de.jeff_media.InvUnload.Main;
import net.coreprotect.CoreProtect;
import org.bukkit.Location;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;

public class CoreProtectHook {

    Main main;
    boolean skipReflection = false;
    boolean disabled = false;

    public CoreProtectHook(Main main) {
        this.main = main;
    }

    public void logCoreProtect(String user, Inventory destination) {

        if(disabled) return;

        if(!main.getConfig().getBoolean("use-coreprotect")) return;

        if(!skipReflection) {
            try {
                Class.forName("net.coreprotect.CoreProtectAPI").getMethod("logContainerTransaction", String.class, Location.class);
                skipReflection = true;

            } catch (ClassNotFoundException | NoSuchMethodException e) {
                main.getLogger().warning("Could not log to CoreProtect because your version of CoreProtect is too old.");
                disabled = true;
                return;
            }
        }

        Plugin coreProtectPlugin = main.getServer().getPluginManager().getPlugin("CoreProtect");

        if(coreProtectPlugin==null || !(coreProtectPlugin instanceof CoreProtect)) {
            disabled=true;
            return;
        }

        Location location = destination.getLocation();

        CoreProtect cp = (CoreProtect) coreProtectPlugin;

        cp.getAPI().logContainerTransaction(user, location);




    }

}
