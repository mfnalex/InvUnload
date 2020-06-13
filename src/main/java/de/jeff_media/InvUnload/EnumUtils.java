package de.jeff_media.InvUnload;

import org.bukkit.Particle;
import org.bukkit.Sound;

public class EnumUtils {
	
	static boolean soundExists(String value) {
		for(Sound sound: Sound.values()) {
			if(sound.name().equalsIgnoreCase(value)) return true;
		}
		return false;
	}
	
	static boolean particleExists(String value) {
		for(Particle particle: Particle.values()) {
			if(particle.name().equalsIgnoreCase(value)) return true;
		}
		return false;
	}

}
