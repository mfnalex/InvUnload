package de.jeff_media.InvUnload;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class CommandBlacklist implements CommandExecutor {

    Main main;

    CommandBlacklist(Main main) {
        this.main=main;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {

        if(!(commandSender instanceof Player)) {
            commandSender.sendMessage("This command is only available for players.");
            return true;
        }

        Player p = (Player) commandSender;
        BlackList b = main.getPlayerSetting(p).getBlacklist();
        ItemStack currentItem = p.getInventory().getItemInMainHand();

        ArrayList<Material> candidates = new ArrayList<>();
        ArrayList<String> errors = new ArrayList<>();
        ArrayList<Material> alreadyAdded = new ArrayList<>();
        ArrayList<Material> successes = new ArrayList<>();

        String option;

        if(args.length==0) {
            option="show";
        } else {
            option=args[0].toLowerCase();
            args[0]=null;
        }

        switch(option) {
            case "show":
                b.print(p);
                return true;
            case "add":
            case "remove":

                if(args.length==1) {
                    if(currentItem.getType()==Material.AIR) {
                        p.sendMessage(main.messages.BL_NOTHINGSPECIFIED);
                        return true;
                    }
                    candidates.add(currentItem.getType());
                } else {
                    for(String s : args) {
                        if(s==null) continue;
                        Material m = Material.getMaterial(s.toUpperCase());
                        if(m==Material.AIR) m = null;
                        if(m==null) {
                            errors.add(s);
                            continue;
                        }
                        candidates.add(m);
                    }
                }


                for(Material mat : candidates) {
                    if(b.contains(mat)) {
                        if (option.equals("add")) {
                            alreadyAdded.add(mat);
                        } else {
                            b.remove(mat);
                            successes.add(mat);
                        }
                        continue;
                    }
                    if(option.equals("add")) {
                        b.add(mat);
                        successes.add(mat);
                        continue;
                    }
                    alreadyAdded.add(mat);
                }

                if(errors.size()>1) {
                    p.sendMessage(String.format(main.messages.BL_INVALID2,errors.size()));
                } else if(errors.size()==1) {
                    p.sendMessage(String.format(main.messages.BL_INVALID1,errors.get(0)));
                }

                if(alreadyAdded.size()>1) {
                    if(option.equals("add")) {
                        p.sendMessage(String.format(main.messages.BL_ALREADYADDED2,alreadyAdded.size()));
                    } else {
                        p.sendMessage(String.format(main.messages.BL_NOTTHERE2,alreadyAdded.size()));
                    }
                } else if(alreadyAdded.size()==1) {
                    if(option.equals("add")) {
                        p.sendMessage(String.format(main.messages.BL_ALREADYADDED1,alreadyAdded.get(0).name()));
                    } else {
                        p.sendMessage(String.format(main.messages.BL_NOTTHERE1,alreadyAdded.get(0).name()));
                    }
                }

                if(successes.size()>1) {
                    if(option.equals("add")) {
                        p.sendMessage(String.format(main.messages.BL_ADDED2,successes.size()));
                    } else {
                        p.sendMessage(String.format(main.messages.BL_REMOVED2,successes.size()));
                    }

                } else if(successes.size()==1) {
                    if(option.equals("add")) {
                        p.sendMessage(String.format(main.messages.BL_ADDED1,successes.get(0).name()));
                    } else {
                        p.sendMessage(String.format(main.messages.BL_REMOVED1,successes.get(0).name()));
                    }

                }
                return true;
            default:
                return false;
        }
    }
}
