package de.jeff_media.InvUnload;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class CommandSearchItem implements CommandExecutor {

    private final Main main;

    CommandSearchItem (Main main) {
        this.main=main;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        if(!(sender instanceof Player)) {
            sender.sendMessage("You must be a player to run this command.");
            return true;
        }

        Player p = (Player) sender;
        Integer radius = null;
        Material mat = null;

        if(args.length>=2) {
            if(StringUtils.isNumeric(args[0])) {
                radius = Integer.parseInt(args[0]);
                if(Material.getMaterial(args[1].toUpperCase()) != null) {
                    mat = Material.getMaterial(args[1].toUpperCase());
                }
            } else {
                if(Material.getMaterial(args[0].toUpperCase()) != null) {
                    mat = Material.getMaterial(args[0].toUpperCase());
                    if(StringUtils.isNumeric(args[1])) {
                        radius = Integer.parseInt(args[1]);
                    } else {
                        p.sendMessage("Invalid radius.");
                        return true;
                    }
                }
            }
        }

        if(args.length==1) {
            if(StringUtils.isNumeric(args[0])) {
                radius = Integer.parseInt(args[0]);
                if(p.getInventory().getItemInMainHand()!=null && p.getInventory().getItemInMainHand().getType()!=null) {
                    mat = p.getInventory().getItemInMainHand().getType();
                }
            } else {
                mat = Material.getMaterial(args[0].toUpperCase());
                radius = main.groupUtils.getDefaultRadiusPerPlayer(p);
            }
        }

        if(args.length==0 && p.getInventory().getItemInMainHand()!=null) {
            mat = p.getInventory().getItemInMainHand().getType();
            radius = main.groupUtils.getDefaultRadiusPerPlayer(p);
        }

        if(mat == null ) {
            p.sendMessage("You must specify a valid material or hold something in your hand.");
            return true;
        }

        if(radius == null) {
            p.sendMessage("Invalid radius.");
            return true;
        }

        if(mat==null) {
            p.sendMessage(String.format("%s is not a valid material.",args[0]));
            return true;
        }

        ArrayList<Block> chests = BlockUtils.findChestsInRadius(p.getLocation(), radius);
        if(chests.size()==0) {
            p.sendMessage(main.messages.MSG_NO_CHESTS_NEARBY);
            return true;
        }
        BlockUtils.sortBlockListByDistance(chests, p.getLocation());

        ArrayList<Block> useableChests = new ArrayList<>();
        for(Block block : chests) {
            if(PlayerUtils.canPlayerUseChest(block, p, main)) {
                useableChests.add(block);
            }
        }

        if(useableChests.size()==0) {
            p.sendMessage(main.messages.MSG_NO_CHESTS_NEARBY);
            return true;
        }

        chests = null;

        ArrayList<Block> affectedChests = new ArrayList<>();

        UnloadSummary summary = new UnloadSummary();
        for(Block block : useableChests) {
            Inventory inv = ((Container) block.getState()).getInventory();
            if(InvUtils.searchItemInContainers(mat, inv, summary)) {
                affectedChests.add(block);
            }
        }

        if(main.getConfig().getBoolean("always-show-summary")) {
            summary.print(UnloadSummary.PrintRecipient.PLAYER, p);
        }

        if(affectedChests.size()==0) {
            p.sendMessage(String.format(main.messages.MSG_NOTHING_FOUND,mat.name()));
            return true;
        }

        //if (main.debug) p.sendMessage(String.format("Unload: %s tried, %s affected | Dump: %s tried, %s affected", triedUnloadChests, affectedUnloadChests, triedDumpChests, affectedDumpChests));

        main.visualizer.save(p, affectedChests,summary);

        for(Block block : affectedChests) {
            main.visualizer.chestAnimation(block,p);
            if(main.getConfig().getBoolean("laser-animation")) {
                main.visualizer.playLaser(affectedChests, p, main.getConfig().getInt("laser-default-duration"));
            }
            if(main.chestSortHook.shouldSort(p)) {
                main.chestSortHook.sort(block);
                //System.out.println("Sorting "+block.getLocation());
            }
        }

        return true;
    }
}
