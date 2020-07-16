package de.jeff_media.InvUnload;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PlayerSetting {

    BlackList blacklist;
    boolean unloadHotbar;
    boolean dumpHotbar;

    PlayerSetting() {
        blacklist = new BlackList();
    }

    BlackList getBlacklist() {
        return blacklist;
    }

    PlayerSetting(File file) {
        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
        blacklist = new BlackList(yaml.getStringList("blacklist"));
        unloadHotbar = yaml.getBoolean("unloadHotbar",false);
        dumpHotbar = yaml.getBoolean("dumpHotbar",false);
    }

    void save(File file,Main main) {
        YamlConfiguration yaml = new YamlConfiguration();
        yaml.set("blacklist",blacklist.toStringList());
        yaml.set("unloadHotbar",unloadHotbar);
        yaml.set("dumpHotbar",dumpHotbar);
        try {
            yaml.save(file);
        } catch (IOException e) {
            main.getLogger().warning("Could not save playerdata file "+file.getPath());
            //e.printStackTrace();
        }
    }

}
