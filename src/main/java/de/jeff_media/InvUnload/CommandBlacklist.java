package de.jeff_media.InvUnload;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CommandBlacklist implements CommandExecutor, TabCompleter {

    final Main main;

    CommandBlacklist(Main main) {
        this.main = main;
    }

    private ArrayList<String> inv2stringlist(Inventory inv, int startSlot, int endSlot) {
        ArrayList<String> list = new ArrayList<>();
        for (int i = startSlot; i <= endSlot; i++) {
            if (inv.getItem(i) == null) continue;
            if (!list.contains(inv.getItem(i).getType().name())) {
                list.add(inv.getItem(i).getType().name());
            }
        }
        return list;
    }

    private String matlist2string(List<Material> list) {
        return list.stream()
                .map(Material::name)
                .collect(Collectors.joining(", "));
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {

        if (!(commandSender instanceof Player)) {
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

        if (args.length == 0) {
            option = "show";
        } else {
            option = args[0].toLowerCase();
            args[0] = null;
        }

        switch (option) {
            case "show":
                b.print(p, main);
                return true;
            case "add":
            case "remove":

                if (args.length == 1) {
                    if (currentItem.getType() == Material.AIR) {
                        p.sendMessage(main.messages.BL_NOTHINGSPECIFIED);
                        return true;
                    }
                    candidates.add(currentItem.getType());
                } else if (args[1].equalsIgnoreCase("inv")
                        || args[1].equalsIgnoreCase("inventory")
                        || args[1].equalsIgnoreCase("hotbar")) {

                    ArrayList<String> list = inv2stringlist(
                            p.getInventory(),
                            args[1].equalsIgnoreCase("hotbar") ? 0 : 9,
                            args[1].equalsIgnoreCase("hotbar") ? 8 : 35);

                    String[] newArgs = new String[1 + list.size()];
                    newArgs[0] = args[0];
                    for (int i = 1; i < list.size() + 1; i++) {
                        newArgs[i] = list.get(i-1);
                    }
                    args = newArgs;
                }


                for (String s : args) {
                    if (s == null) continue;
                    Material m = Material.getMaterial(s.toUpperCase());
                    if (m == Material.AIR) m = null;
                    if (m == null) {
                        errors.add(s);
                        continue;
                    }
                    candidates.add(m);
                }


                for (Material mat : candidates) {
                    successes.add(mat);
                    if (option.equals("add")) {
                        if(!b.contains(mat)) {
                            b.add(mat);
                        }
                    } else {
                        b.remove(mat);
                    }
                }

                if (errors.size() > 0) {
                    p.sendMessage(String.format(main.messages.BL_INVALID, stringlist2string(errors)));
                }
                if (successes.size() > 0) {
                    String message;
                    if (option.equals("add")) {
                        message = main.messages.BL_ADDED;
                    } else {
                        message = main.messages.BL_REMOVED;
                    }
                    p.sendMessage(String.format(message, matlist2string(successes)));
                }

                return true;
            case "reset":
                p.sendMessage(String.format(main.messages.BL_REMOVED, matlist2string(b.mats)));
                b.mats.clear();
                return true;
            default:
                return false;
        }
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        if (!(commandSender instanceof Player)) return null;

        String[] commands = {"show", "add", "remove", "reset"};
        if (args.length == 0) return Arrays.asList(commands);
        ArrayList<String> list = new ArrayList<>();

        if (args.length == 1) {
            for (String string : commands) {
                if (string.toLowerCase().startsWith(args[0])) list.add(string);
            }
            return list;
        }

        if (args.length >= 2 && args[0].equalsIgnoreCase("remove")) {
            for (Material mat : main.getPlayerSetting((Player) commandSender).getBlacklist().mats) {
                list.add(mat.name());
            }
            return list;
        }

        if (args.length >= 2 && args[0].equals("add")) {
            return main.materialTabCompleter.onTabComplete(commandSender, command, s, args);
        }

        return null;
    }

    private String stringlist2string(List<String> list) {
        return String.join(", ", list);
    }
}
