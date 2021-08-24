package de.jeff_media.InvUnload.utils;

import de.jeff_media.InvUnload.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.HashMap;

public class CoolDown {

    private static final HashMap<CommandSender, Long> map = new HashMap<>();

    public static boolean check(CommandSender sender) {
        if(map.containsKey(sender)) {
            long lastTime = map.get(sender);
            //System.out.println("lastTime: " + lastTime);
            long okayTime = lastTime + (long) (Main.getInstance().getConfig().getDouble("cooldown")*1000);
            //System.out.println("okayTime: " + okayTime);
            //System.out.println("now Time: " + System.currentTimeMillis());
            boolean isOkay =  System.currentTimeMillis() >= okayTime;
            if(!isOkay) {
                sender.sendMessage(Main.getInstance().messages.MSG_COOLDOWN);
                return false;
            }
            map.put(sender,System.currentTimeMillis());
            return true;
        } else {
            map.put(sender, System.currentTimeMillis());
            return true;
        }
    }
}
