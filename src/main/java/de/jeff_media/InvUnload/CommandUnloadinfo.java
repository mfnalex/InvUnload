package de.jeff_media.InvUnload;

import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import com.sun.tools.javac.code.TypeTag.NumericClasses;

public class CommandUnloadinfo implements CommandExecutor {
	
	Main main;
	
	CommandUnloadinfo(Main main) {
		this.main=main;
	}

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
			@NotNull String[] args) {
		
		if(!(sender instanceof Player)) {
			return false;
		}
		
		Player p = (Player) sender;
		
		int duration = main.getConfig().getInt("laser-default-duration");
		if(args.length>0 && StringUtils.isNumeric(args[0])) {
			duration = Integer.parseInt(args[0]);
			if(duration > main.getConfig().getInt("laser-max-duration")) {
				duration = main.getConfig().getInt("laser-max-duration");
			}
		}

		ArrayList<Block> affectedChests = main.visualizer.lastUnloads.get(p.getUniqueId());
		if(affectedChests==null || affectedChests.size()==0) {
			return true;
		}
		ArrayList<Laser> lasers = main.visualizer.activeLasers.get(p.getUniqueId());
		if(lasers != null) {
			for(Laser laser : lasers) {
				if(laser.isStarted()) laser.stop();
			}
		}
		main.visualizer.activeLasers.remove(p.getUniqueId());
		for(Block block : affectedChests) {
			main.visualizer.chestAnimation(block, p);
		}
		main.visualizer.playLaser(affectedChests,p,duration);
		/*boolean newVisualizer=true;
		
		if(newVisualizer) {
			main.visualizer.toggleLaser(p);
		}
		else {
			if(main.visualizer.activeVisualizations.containsKey(p.getUniqueId())) {
				p.sendMessage("Experimental: Laser stopped");
				main.visualizer.cancelVisualization(p);
			} else {
				p.sendMessage("Experimental: Laser started");
				main.visualizer.play(p);
			}
		}*/
		
		
		
		return true;
	}

}
