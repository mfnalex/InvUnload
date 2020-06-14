package de.jeff_media.InvUnload;

import java.util.ArrayList;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class Visualizer {
	
	//ArrayList<Location> destinations;
	//Player p;
	static Particle particle = Particle.SPELL_WITCH;
	static double interval = 0.1;
	
	static void play(ArrayList<Block> destinations, Player p) {
		for(Block destination : destinations) {
			Location start = p.getLocation();
			Vector vec = getDirectionBetweenLocations(start, destination.getLocation());
            for (double i = 1; i <= start.distance(destination.getLocation()); i += interval) {
                vec.multiply(i);
                start.add(vec);
                start.getWorld().spawnParticle(particle, start, 1, 0, 0, 0, 0.1);
                start.subtract(vec);
                vec.normalize();
            }
		}
	}
	
    private static Vector getDirectionBetweenLocations(Location start, Location end) {
        return end.toVector().subtract(start.toVector());
    }

}
