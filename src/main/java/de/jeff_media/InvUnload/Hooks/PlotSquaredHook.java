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
	PlotSquaredUniversalHook hook;
	//Integer version = null;
	
	public PlotSquaredHook(Main main) {
		this.main=main;
		
		try {
			Class.forName("com.github.intellectualsites.plotsquared.bukkit.util.BukkitUtil");
			//version = 4;
			main.getLogger().info("PlotSquared 4 detected, using old API");
			hook = new PlotSquared4Hook();
		} catch(ClassNotFoundException e) {}
		try {
			Class.forName("com.plotsquared.bukkit.util.BukkitUtil");
			//version = 5;
			main.getLogger().info("PlotSquared 5 detected, using new API");
			hook = new PlotSquared5Hook();
		} catch(ClassNotFoundException e) {}
		
	}
	
	public boolean isBlockedByPlotSquared(Block block, Player player) {
		
		if(hook==null) return false;
		
		if(!main.getConfig().getBoolean("use-plotsquared")) return false;
		
		if(Bukkit.getPluginManager().getPlugin("PlotSquared") == null) {
			//System.out.println("PlotSquared not installed");
			return false;
		}
		
		return hook.isBlockedByPlotSquared(block, player, main);
		
	}
}
