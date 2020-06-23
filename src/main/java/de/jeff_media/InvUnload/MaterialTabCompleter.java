package de.jeff_media.InvUnload;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MaterialTabCompleter implements TabCompleter {

    ArrayList<String> mats;

    MaterialTabCompleter() {
        mats = new ArrayList<>();
        for(Material mat : Material.values()) {
            mats.add(mat.name());
        }
    }


    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        if(args.length==0) return null;

        ArrayList<String> results = new ArrayList<>();
        String lastArg = args[args.length-1];

        for(String mat : mats) {
            if(mat.toLowerCase().contains(lastArg.toLowerCase())) {
                results.add(mat);
            }
        }

        return results;
    }
}
