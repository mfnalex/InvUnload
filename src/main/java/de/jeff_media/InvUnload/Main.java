package de.jeff_media.InvUnload;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

import de.jeff_media.ChestSort.ChestSortAPI;
import de.jeff_media.ChestSort.ChestSortPlugin;

public class Main extends JavaPlugin implements Listener {

	@Nullable
	ChestSortAPI chestSortAPI;

	String mcVersion; // 1.13.2 = 1_13_R2
						// 1.14.4 = 1_14_R1
						// 1.8.0 = 1_8_R1
	int mcMinorVersion; // 14 for 1.14, 13 for 1.13, ...

	private int currentConfigVersion = 3;

	protected Messages messages;

	protected int defaultChestRadius = 10;
	protected int maxChestRadius = 20;

	public boolean usingMatchingConfig = true;

	public void onEnable() {

		String tmpVersion = getServer().getClass().getPackage().getName();
		mcVersion = tmpVersion.substring(tmpVersion.lastIndexOf('.') + 1);
		tmpVersion = mcVersion.substring(mcVersion.indexOf("_") + 1);
		mcMinorVersion = Integer.parseInt(tmpVersion.substring(0, tmpVersion.indexOf("_")));

		createConfig();

		messages = new Messages(this);

		ChestSortPlugin chestSort = (ChestSortPlugin) getServer().getPluginManager().getPlugin("ChestSort");
		if (chestSort == null || !(chestSort instanceof ChestSortPlugin)) {
			getLogger().warning("Warning: ChestSort is not installed.");
		} else {
			chestSortAPI = chestSort.getAPI();
		}
		registerCommands();
		// registerListener();
	}

	private void createConfig() {

		// This saves the config.yml included in the .jar file, but it will not
		// overwrite an existing config.yml
		this.saveDefaultConfig();

		if (getConfig().getInt("config-version", 0) != currentConfigVersion) {
			showOldConfigWarning();
			ConfigUpdater configUpdater = new ConfigUpdater(this);
			configUpdater.updateConfig();
			configUpdater = null;
			usingMatchingConfig = true;
			// createConfig();
		}

		setDefaultConfigValues();

		
		

	}

	private void setDefaultConfigValues() {
		// If you use an old config file with missing options, the following default
		// values will be used instead
		// for every missing option.
		getConfig().addDefault("max-chest-radius", 20);
		maxChestRadius = getConfig().getInt("max-chest-radius");
		getConfig().addDefault("default-chest-radius", 10);
		defaultChestRadius = getConfig().getInt("default-chest-radius");
	}

	private void showOldConfigWarning() {
		getLogger().warning("==============================================");
		getLogger().warning("You were using an old config file. InvUnload");
		getLogger().warning("has updated the file to the newest version.");
		getLogger().warning("Your changes have been kept.");
		getLogger().warning("==============================================");
	}

	private void registerCommands() {
		CommandUnload commandUnload = new CommandUnload(this);
		getCommand("unload").setExecutor(commandUnload);
		getCommand("dump").setExecutor(commandUnload);
	}

}
