package de.jeff_media.InvUnload.Hooks;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import de.jeff_media.InvUnload.Main;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.UUID;

/*
Doing this via Reflection because the PlotSquared guys think it's funny to change the API in every new major release
 */
public class PlotSquared6Hook implements PlotSquaredUniversalHook {

    private Class bukkitUtilClass;
    private Method adaptMethod;
    private Class plotClass;
    private Method getTrustedMethod;
    private Method isOwnerMethod;
    private Class locationClass;
    private Method getPlotAbsMethod;

    public PlotSquared6Hook() {
        try {
            bukkitUtilClass = Class.forName("com.plotsquared.bukkit.util.BukkitUtil");
            adaptMethod = bukkitUtilClass.getMethod("adapt", Location.class);
            plotClass = Class.forName("com.plotsquared.core.plot.Plot");
            getTrustedMethod = plotClass.getMethod("getTrusted");
            isOwnerMethod = plotClass.getMethod("isOwner", UUID.class);
            locationClass = Class.forName("com.plotsquared.core.location.Location");
            getPlotAbsMethod = locationClass.getMethod("getPlotAbs");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isBlockedByPlotSquared(Block block, Player player, Main main) {
        try {
            Object location = adaptMethod.invoke(null,block.getLocation());
            Object plot = getPlotAbsMethod.invoke(location);
            if(plot == null) return !main.getConfig().getBoolean("plotsquared-allow-outside-plots");
            HashSet<UUID> trustedUUIDs = (HashSet<UUID>) getTrustedMethod.invoke(plot);
            if(trustedUUIDs.contains(player.getUniqueId()) && main.getConfig().getBoolean("plotsquared-allow-when-trusted")) return false;
            boolean isOwner = (boolean) isOwnerMethod.invoke(plot, player.getUniqueId());
            if(isOwner) return true;
            return false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
