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
		
		if(!main.getConfig().getBoolean("use-plotsquared")) return false;
		
		System.out.println(Bukkit.getPluginManager().getPlugin("PlotSquared").getClass().getName());
		
		if(Bukkit.getPluginManager().getPlugin("PlotSquared") == null
				|| !Bukkit.getPluginManager().getPlugin("PlotSquared")
				.getClass().getName().equalsIgnoreCase("com.github.intellectualsites.plotsquared.bukkit.BukkitMain")
				|| !(Bukkit.getPluginManager().getPlugin("PlotSquared") instanceof PlotSquared)) {
			//System.out.println("PlotSquared not installed");
			return false;
		}
		
		
		
		Plot plot = BukkitUtil.getLocation(block.getLocation()).getPlotAbs();
		
		if(!main.getConfig().getBoolean("plotsquared-allow-outside-plots")
				&& plot == null) return true;
		
		if(plot.getTrusted().contains(player.getUniqueId())
				&& main.getConfig().getBoolean("plotsquared-allow-when-trusted")) return false;
		
		if(!plot.isOwner(player.getUniqueId())) return true;
		
		return false;
	}

}
