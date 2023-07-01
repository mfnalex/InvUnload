package de.jeff_media.InvUnload;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.block.DoubleChest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CommandSearchitem implements CommandExecutor {

    private final Main main;

    CommandSearchitem(Main main) {
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

        if (radius == null || radius > main.groupUtils.getMaxRadiusPerPlayer(p)) {
            p.sendMessage(String.format(main.messages.MSG_RADIUS_TOO_HIGH, main.groupUtils.getMaxRadiusPerPlayer(p)));
            return true;
        }

        if(mat==null) {
            p.sendMessage(String.format("%s is not a valid material.",args[0]));
            return true;
        }

        List<Block> chests = BlockUtils.findChestsInRadius(p.getLocation(), radius);
        BlockUtils.sortBlockListByDistance(chests, p.getLocation());

        ArrayList<Block> useableChests = new ArrayList<>();
        for(Block block : chests) {
            if(PlayerUtils.canPlayerUseChest(block, p, main)) {
                useableChests.add(block);
            }
        }

        if(useableChests.size()==0) {
            p.sendMessage(String.format(main.messages.MSG_NOTHING_FOUND,mat.name()));
            return true;
        }

        chests = null;

        ArrayList<Block> affectedChests = new ArrayList<>();
        ArrayList<InventoryHolder> doubleChests = new ArrayList<>();
        UnloadSummary summary = new UnloadSummary();
        for(Block block : useableChests) {

            Inventory inv = ((Container) block.getState()).getInventory();

            if(inv.getHolder() instanceof DoubleChest) {
                DoubleChest dc = (DoubleChest) inv.getHolder();
                if(doubleChests.contains(dc.getLeftSide())) continue;
                doubleChests.add(dc.getLeftSide());
            }

            if(InvUtils.searchItemInContainers(mat, inv, summary)) {
                affectedChests.add(block);
            }
        }

        if (main.canSeeCoordinates(p)) {
            summary.print(UnloadSummary.PrintRecipient.PLAYER, p);
        }

        if(affectedChests.size()==0) {
            p.sendMessage(String.format(main.messages.MSG_NOTHING_FOUND,mat.name()));
            return true;
        }
        
        for(Block block : affectedChests) {
            main.visualizer.chestAnimation(block,p);
        }
        main.visualizer.play(affectedChests, p);

        return true;
    }
}
