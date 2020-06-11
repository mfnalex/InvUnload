package de.jeff_media.InvUnload;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import de.jeff_media.ChestSort.utils.Utils;

public class ConfigUpdater {

	Main main;

	public ConfigUpdater(Main plugin) {
		this.main = plugin;
	}

	// Admins hate config updates. Just relax and let ChestSort update to the newest
	// config version
	// Don't worry! Your changes will be kept

	void updateConfig() {
		
		FileUtils.renameFileInPluginDir(main, "config.yml", "config.old.yml");
		main.saveDefaultConfig();

		File oldConfigFile = new File(main.getDataFolder().getAbsolutePath() + File.separator + "config.old.yml");
		FileConfiguration oldConfig = new YamlConfiguration();

		try {
			oldConfig.load(oldConfigFile);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}

		Map<String, Object> oldValues = oldConfig.getValues(false);

		// Read default config to keep comments
		ArrayList<String> linesInDefaultConfig = new ArrayList<String>();
		try {

			Scanner scanner = new Scanner(
					new File(main.getDataFolder().getAbsolutePath() + File.separator + "config.yml"));
			while (scanner.hasNextLine()) {
				linesInDefaultConfig.add(scanner.nextLine() + "");
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		ArrayList<String> newLines = new ArrayList<String>();
		for (String line : linesInDefaultConfig) {
			String newline = line;
			if (line.startsWith("config-version:")) {
				// dont replace config-version
			} else {
				for (String node : oldValues.keySet()) {
					if (line.startsWith(node + ":")) {

						String quotes = "";

						if (node.startsWith("message-")) // needs double quotes
							quotes = "\"";

						newline = node + ": " + quotes + oldValues.get(node).toString() + quotes;
						break;
					}
				}
			}
			if (newline != null)
				newLines.add(newline);
		}

		FileWriter fw;
		String[] linesArray = newLines.toArray(new String[linesInDefaultConfig.size()]);
		try {
			fw = new FileWriter(main.getDataFolder().getAbsolutePath() + File.separator + "config.yml");
			for (int i = 0; i < linesArray.length; i++) {
				fw.write(linesArray[i] + "\n");
			}
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Utils.renameFileInPluginDir(plugin, "config.yml.default", "config.yml");

	}

}
