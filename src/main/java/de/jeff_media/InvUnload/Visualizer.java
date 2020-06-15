package de.jeff_media.InvUnload;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class Visualizer {
	
	private Main main;
	private HashMap<UUID,ArrayList<Block>> lastUnloads;
	HashMap<UUID,Integer> activeVisualizations;

	//ArrayList<Location> destinations;
	//Player p;
	
	// Barrier: okay

	
	protected Visualizer(Main main) {
		this.main = main;
		lastUnloads = new HashMap<UUID,ArrayList<Block>>();
		activeVisualizations = new HashMap<UUID,Integer>();
	}
	
	void cancelVisualization(int id) {
		Bukkit.getScheduler().cancelTask(id);
	}
	
	void cancelVisualization(Player p) {
		if(activeVisualizations.containsKey(p.getUniqueId())) {
			cancelVisualization(activeVisualizations.get(p.getUniqueId()));
		}
		activeVisualizations.remove(p.getUniqueId());
	}
	
	void save(Player p, ArrayList<Block> affectedChests) {
		lastUnloads.put(p.getUniqueId(), affectedChests);
		if(activeVisualizations.containsKey(p.getUniqueId())) {
			cancelVisualization(activeVisualizations.get(p.getUniqueId()));
			play(p);
		}
	}
	
	void play(Player p) {
		if(lastUnloads.containsKey(p.getUniqueId())) {
			play(lastUnloads.get(p.getUniqueId()),p);
		}
	}

	void play(ArrayList<Block> destinations, Player p, double interval, int count, Particle particle,double speed,int maxDistance) {
		for(Block destination : destinations) {
			Location start = p.getLocation();
			Vector vec = getDirectionBetweenLocations(start, BlockUtils.getCenterOfBlock(destination).add(0, -0.5, 0));
			if(start.distance(destination.getLocation())<maxDistance) {
	            for (double i = 1; i <= start.distance(destination.getLocation()); i += interval) {
	                vec.multiply(i);
	                start.add(vec);
	                p.spawnParticle(particle, start, count, 0, 0, 0, speed);
	                start.subtract(vec);
	                vec.normalize();
	            }
            }
		}
	}
	
	void play(ArrayList<Block> affectedChests, Player p) {
		// Visualize
				int task = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(main, new Runnable() {
				    public void run() {
				    	Particle particle = Particle.CRIT;
				    	int count = 1;
				    	int maxDistance = 128;
				    	double interval = 0.3;
				    	double speed = 0.001;
				    	play(affectedChests, p, interval, count, particle,speed,maxDistance);
				    }
				}, 0, 2);
				
				activeVisualizations.put(p.getUniqueId(), task);
				
				/*new BukkitRunnable() {
					public void run() {
						Bukkit.getServer().getScheduler().cancelTask(task);
					}
				}.runTaskLater(main, 100);*/
	}
	
    private static Vector getDirectionBetweenLocations(Location start, Location end) {
        return end.toVector().subtract(start.toVector());
    }

}
