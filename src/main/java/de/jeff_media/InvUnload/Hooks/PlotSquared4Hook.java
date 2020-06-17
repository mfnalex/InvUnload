package de.jeff_media.InvUnload.Hooks;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.github.intellectualsites.plotsquared.bukkit.util.BukkitUtil;
import com.github.intellectualsites.plotsquared.plot.object.Plot;

import de.jeff_media.InvUnload.Main;

public class PlotSquared4Hook implements PlotSquaredUniversalHook {



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
