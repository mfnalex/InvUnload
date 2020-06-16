package de.jeff_media.InvUnload.Hooks;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.plotsquared.bukkit.util.BukkitUtil;
import com.plotsquared.core.PlotSquared;
import com.plotsquared.core.location.Location;
import com.plotsquared.core.plot.Plot;
import com.plotsquared.core.plot.PlotManager;

import de.jeff_media.InvUnload.Main;

public class PlotSquaredHook {
	
	Main main;
	
	public PlotSquaredHook(Main main) {
		this.main=main;
	}
	
	public boolean isBlockedByPlotSquared(Block block, Player player) {
		
		if(!(Bukkit.getPluginManager().getPlugin("PlotSquared") instanceof PlotSquared)) {
			//System.out.println("PlotSquared not installed");
			return false;
		}
		
		
		
		Plot plot = BukkitUtil.getLocation(block.getLocation()).getPlotAbs();
		
		// Do not allow outside of Plots
		if(main.getConfig().getBoolean("plotsquared-forbid-outside-of-plots")
				&& plot == null) return true;
		
		// Do not allow in foreign Plots
		if(main.getConfig().getBoolean("plotsquared-forbid-foreign-plots")
				&& !plot.isOwner(player.getUniqueId())) return true;
		
		return false;
	}

}
