package de.jeff_media.InvUnload;

import com.jeff_media.updatechecker.UpdateCheckSource;
import com.jeff_media.updatechecker.UpdateChecker;
import de.jeff_media.InvUnload.Hooks.*;
import de.jeff_media.InvUnload.utils.EnchantmentUtils;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Main extends JavaPlugin implements Listener {

	private static Main instance;

	@Nullable
	public boolean useChestSort;
    CoreProtectHook coreProtectHook;

    String mcVersion; // 1.13.2 = 1_13_R2
						// 1.14.4 = 1_14_R1
						// 1.8.0 = 1_8_R1
	int mcMinorVersion; // 14 for 1.14, 13 for 1.13, ...

	@SuppressWarnings("FieldCanBeLocal")
	private final int currentConfigVersion = 37;

	public Messages messages;
	protected BlockUtils blockUtils;

	protected int defaultChestRadius = 10;
	protected int maxChestRadius = 20;

	public boolean usingMatchingConfig = true;

	protected ChestSortHook chestSortHook;
	protected PlotSquaredHook plotSquaredHook;
	protected InventoryPagesHook inventoryPagesHook;
	protected Visualizer visualizer;
	protected GroupUtils groupUtils;

	private UpdateChecker updateChecker;

	CommandUnload commandUnload;
	CommandUnloadinfo commandUnloadInfo;
	CommandSearchitem commandSearchitem;
	CommandBlacklist commandBlacklist;
	MaterialTabCompleter materialTabCompleter;

	public EnchantmentUtils getEnchantmentUtils() {
		return enchantmentUtils;
	}

	private EnchantmentUtils enchantmentUtils;

	private double updateCheckInterval = 4;
	HashMap<UUID, PlayerSetting> playerSettings;
	private ItemsAdderWrapper itemsAdderWrapper;

	public static Main getInstance() {
		return instance;
	}

	public ItemsAdderWrapper getItemsAdderWrapper() {
		if (itemsAdderWrapper == null) {

			itemsAdderWrapper = ItemsAdderWrapper.init(this);
		}

		return itemsAdderWrapper;
	}

	@Override
	public void onDisable() {
		saveAllPlayerSettings();
	}

	@Override
	public void onEnable() {

		instance = this;

		Metrics metrics = new Metrics(this, 3156);

		String tmpVersion = getServer().getClass().getPackage().getName();
		mcVersion = tmpVersion.substring(tmpVersion.lastIndexOf('.') + 1);
		tmpVersion = mcVersion.substring(mcVersion.indexOf("_") + 1);
		mcMinorVersion = Integer.parseInt(tmpVersion.substring(0, tmpVersion.indexOf("_")));

		reloadCompleteConfig(false);

		if (!getConfig().getBoolean("use-chestsort") || Bukkit.getPluginManager().getPlugin("ChestSort") == null) {
			useChestSort = false;
		} else {
			try {
				Class.forName("de.jeff_media.chestsort.api.ChestSortAPI");
				useChestSort = true;
				getLogger().info("Succesfully hooked into ChestSort");
			} catch (ClassNotFoundException e) {
				getLogger().warning("Your version of ChestSort is too old, disabling ChestSort integration. Please upgrade ChestSort to version 11.0.0 or later.");
			}
		}

		chestSortHook = new ChestSortHook(this);
		plotSquaredHook = new PlotSquaredHook(this);
		coreProtectHook = new CoreProtectHook(this);
		inventoryPagesHook = new InventoryPagesHook(this);
		enchantmentUtils = new EnchantmentUtils(this);

		registerCommands();
	}

	private void createConfig() {
		
		saveResource("groups.example.yml", true);

		// This saves the config.yml included in the .jar file, but it will not
		// overwrite an existing config.yml
		this.saveDefaultConfig();
		reloadConfig();
		//System.out.println("DEBUG: Current config version: "+getConfig().getInt("config-version",0));
		if (getConfig().getInt("config-version", 0) != currentConfigVersion) {
			//System.out.println("DEBUG: Current config version: "+getConfig().getInt("config-version",0));

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

		getConfig().addDefault("unload-before-dumping", true);

		getConfig().addDefault("check-interval", 4);
		updateCheckInterval = getConfig().getDouble("check-interval");

		getConfig().addDefault("use-chestsort", true);
		getConfig().addDefault("force-chestsort", false);
		getConfig().addDefault("use-itemsadder", true);
		getConfig().addDefault("match-enchantments-on-books",false);
		getConfig().addDefault("match-enchantments",false);

		getConfig().addDefault("use-playerinteractevent", true);
		getConfig().addDefault("use-coreprotect", true);
		getConfig().addDefault("use-plotsquared", true);
		getConfig().addDefault("plotsquared-allow-when-trusted", true);
		getConfig().addDefault("plotsquared-allow-outside-plots", true);

		getConfig().addDefault("spawn-particles", true);
		getConfig().addDefault("particle-type", "SPELL_WITCH");
		getConfig().addDefault("particle-count", 100);
		
		getConfig().addDefault("always-show-summary", true);
		getConfig().addDefault("show-coordinates", "default");
		
		getConfig().addDefault("laser-animation", true);
		getConfig().addDefault("laser-default-duration", 5);
		getConfig().addDefault("laser-max-distance", 30);
		getConfig().addDefault("laser-max-distance", 50);
		getConfig().addDefault("laser-moves-with-player", false);

		getConfig().addDefault("strict-tabcomplete",true);
		
		if(!EnumUtils.particleExists(getConfig().getString("particle-type"))) {
			getLogger().warning("Specified particle type \"" + getConfig().getString("particle-type") + "\" does not exist! Please check your config.yml");
			getConfig().set("error-particles",true);
		}
		if(!EnumUtils.soundExists(getConfig().getString("sound-effect"))) {
			getLogger().warning("Specified sound effect \"" + getConfig().getString("sound-effect") + "\" does not exist! Please check your config.yml");
			getConfig().set("error-sound", true);
		}
	
	}

	private void showOldConfigWarning() {
		getLogger().warning("==============================================");
		getLogger().warning("You were using an old config file. InvUnload");
		getLogger().warning("has updated the file to the newest version.");
		getLogger().warning("Your changes have been kept.");
		getLogger().warning("==============================================");
	}

	private void registerCommands() {
		commandUnload = new CommandUnload(this);
		commandUnloadInfo = new CommandUnloadinfo(this);
		commandSearchitem = new CommandSearchitem(this);
		commandBlacklist = new CommandBlacklist(this);
		materialTabCompleter= new MaterialTabCompleter(this);
		getCommand("unload").setExecutor(commandUnload);
		getCommand("dump").setExecutor(commandUnload);
		getCommand("unloadinfo").setExecutor(commandUnloadInfo);
		getCommand("searchitem").setExecutor(commandSearchitem);
		getCommand("searchitem").setTabCompleter(materialTabCompleter);
		getCommand("blacklist").setExecutor(commandBlacklist);
		getCommand("blacklist").setTabCompleter(commandBlacklist);
	}
	
	private void initUpdateChecker() {

		if(updateChecker != null) updateChecker.stop();

		// Check for updates (async, of course)
		// When set to true, we check for updates right now, and every X hours (see
		// updateCheckInterval)
		updateChecker = new UpdateChecker(this, UpdateCheckSource.CUSTOM_URL,"https://api.jeff-media.com/invunload/latest-version.txt")
				.suppressUpToDateMessage(true)
				.setDonationLink("https://paypal.me/mfnalex")
				.setDownloadLink(60095)
				.setChangelogLink(60095);
		if (getConfig().getString("check-for-updates", "true").equalsIgnoreCase("true")) {
			updateChecker.checkNow().checkEveryXHours(updateCheckInterval);

		} // When set to on-startup, we check right now (delay 0)
		else if (getConfig().getString("check-for-updates", "true").equalsIgnoreCase("on-startup")) {
			updateChecker.checkNow();
		}
	}

	PlayerSetting getPlayerSetting(Player p) {
		if(playerSettings.containsKey(p.getUniqueId())) {
			return playerSettings.get(p.getUniqueId());
		}

		PlayerSetting setting;
		if(getPlayerFile(p.getUniqueId()).exists()) {
			setting = new PlayerSetting(getPlayerFile(p.getUniqueId()));
		} else {
			setting = new PlayerSetting();
		}

		playerSettings.put(p.getUniqueId(),setting);

		return setting;
	}

	File getPlayerFile(UUID uuid) {
		return new File(getDataFolder()+File.separator+"playerdata"+File.separator+uuid.toString()+".yml");
	}

	public void reloadCompleteConfig(boolean reload) {
		reloadConfig();
		createConfig();
		new File(getDataFolder()+File.separator+"playerdata").mkdirs();
		if(reload) {
			if (updateChecker != null) {
				updateChecker.stop();
			}
			saveAllPlayerSettings();
		}
		messages = new Messages(this);
		initUpdateChecker();
		blockUtils = new BlockUtils(this);
		visualizer = new Visualizer(this);
		File groupsFile = new File(this.getDataFolder()+File.separator+"groups.yml");
		groupUtils = new GroupUtils(this,groupsFile);
		getServer().getPluginManager().registerEvents(new PlayerListener(this),this);
		playerSettings = new HashMap<>();
	}

	private void saveAllPlayerSettings() {
		for(Map.Entry<UUID,PlayerSetting> entry : playerSettings.entrySet()) {
			entry.getValue().save(getPlayerFile(entry.getKey()),this);
		}
	}

	/**
	 * Determines if given command sender can see coordinates of the chests in command output.
	 */
	public boolean canSeeCoordinates(CommandSender commandSender) {
		if (commandSender.hasPermission("invunload.coordinates")) {
			return true;
		}

		// Get reducedDebugInfo gamerule value.
		boolean reducedDebugInfo = false;
		if (commandSender instanceof Player) {
			reducedDebugInfo = ((Player) commandSender).getWorld().getGameRuleValue(GameRule.REDUCED_DEBUG_INFO);
		}

		// By default, use reducedDebugInfo gamerule to decide if coordinates should be displayed.
		if (this.getConfig().getString("show-coordinates").equals("default")) {
			return !reducedDebugInfo;
		}

		// If show-coordinates config value is not set to 'default'
		// ignore the gamerule and use configured boolean value.
		return this.getConfig().getBoolean("show-coordinates");
	}

}
