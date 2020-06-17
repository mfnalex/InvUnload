package de.jeff_media.InvUnload.Hooks;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import com.plotsquared.bukkit.util.BukkitUtil;
import com.plotsquared.core.plot.Plot;

import de.jeff_media.InvUnload.Main;

public class PlotSquared5Hook implements PlotSquaredUniversalHook {

	@Override
	public boolean isBlockedByPlotSquared(Block block, Player player, Main main) {
		
		Plot plot = BukkitUtil.getLocation(block.getLocation()).getPlotAbs();
		
		if(plot == null) return !main.getConfig().getBoolean("plotsquared-allow-outside-plots");
		
		if(plot.getTrusted().contains(player.getUniqueId())
				&& main.getConfig().getBoolean("plotsquared-allow-when-trusted")) return false;
		
		if(!plot.isOwner(player.getUniqueId())) return true;
		
		return false;
	}
	
	

}
