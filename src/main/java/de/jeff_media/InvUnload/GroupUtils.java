package de.jeff_media.InvUnload;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class GroupUtils {
	
	Main main;
	YamlConfiguration yaml;
	LinkedHashMap<String,Group> groups;
	GroupUtils(Main main,File yamlFile) {
		this.main=main;
		if(!yamlFile.exists()) {
			main.getLogger().info("groups.yml does not exist, skipping custom group settings.");
			return;
		}
		this.yaml=YamlConfiguration.loadConfiguration(yamlFile);
		groups = new LinkedHashMap<String,Group>();
		
		for(String groupName : yaml.getKeys(true)) {
			int defaultRadius = yaml.getInt(groupName+".default-chest-radius",-1);
			int maxRadius = yaml.getInt(groupName+".max-chest-radius",-1);
			groups.put(groupName, new Group(defaultRadius,maxRadius));
		}
	}
	
	int getDefaultRadiusPerPlayer(Player p) {
		if(yaml==null) return main.getConfig().getInt("default-chest-radius");
		Iterator<String> it = groups.keySet().iterator();
		int bestValueFound = -1;
		while(it.hasNext()) {
			String group = it.next();
			if(!p.hasPermission("invunload.group."+group)) continue;
			int defaultRadius = groups.get(group).defaultRadius;
			bestValueFound = (defaultRadius>bestValueFound) ? defaultRadius : bestValueFound;
		}
		return bestValueFound == -1 ? main.getConfig().getInt("default-chest-radius") : bestValueFound;
	}
	
	int getMaxRadiusPerPlayer(Player p) {
		if(yaml==null) return main.getConfig().getInt("max-chest-radius");
		Iterator<String> it = groups.keySet().iterator();
		int bestValueFound = -1;
		while(it.hasNext()) {
			String group = it.next();
			if(!p.hasPermission("invunload.group."+group)) continue;
			int maxRadius = groups.get(group).maxRadius;
			bestValueFound = (maxRadius>bestValueFound) ? maxRadius : bestValueFound;
		}
		return bestValueFound == -1 ? main.getConfig().getInt("max-chest-radius") : bestValueFound;
	}
	
	class Group {
		int defaultRadius;
		int maxRadius;
		
		Group(int defaultRadius, int maxRadius) {
			this.defaultRadius = defaultRadius;
			this.maxRadius = maxRadius;
		}
	}
}
