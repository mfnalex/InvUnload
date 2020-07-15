package de.jeff_media.InvUnload;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PlayerSetting {

    BlackList blacklist;

    PlayerSetting() {
        blacklist = new BlackList();
    }

    BlackList getBlacklist() {
        return blacklist;
    }

    PlayerSetting(File file) {
        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
        blacklist = new BlackList(yaml.getStringList("blacklist"));
    }

    void save(File file,Main main) {
        YamlConfiguration yaml = new YamlConfiguration();
        yaml.set("blacklist",blacklist.toStringList());
        try {
            yaml.save(file);
        } catch (IOException e) {
            main.getLogger().warning("Could not save playerdata file "+file.getPath());
            //e.printStackTrace();
        }
    }

}
