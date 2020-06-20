package de.jeff_media.InvUnload;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CommandSeekbert implements CommandExecutor {

    private final Main main;

    CommandSeekbert(Main main) {
        this.main=main;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        if(!(sender instanceof Player)) {
            sender.sendMessage("You must be a player to run this command.");
            return true;
        }

        Player p = (Player) sender;

        if(args.length==0) {
            p.sendMessage("You must specify a material.");
            return true;
        }

        Material mat = Material.getMaterial(args[0]);

        if(mat==null) {
            p.sendMessage(String.format("%s is not a valid material.",args[0]));
            return true;
        }

        return false;
    }
}
