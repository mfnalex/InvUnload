package de.jeff_media.InvUnload.Hooks;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import de.jeff_media.InvUnload.Main;

public interface PlotSquaredUniversalHook {
	boolean isBlockedByPlotSquared(Block block, Player player, Main main);
}
