package de.jeff_media.InvUnload.Hooks;

import de.jeff_media.InvUnload.Main;
import org.bukkit.inventory.ItemStack;

public class ItemsAdderWrapper {

    public static ItemsAdderWrapper init(Main main) {

        if (!main.getConfig().getBoolean("use-itemsadder")) return new ItemsAdderWrapper();

        ItemsAdderWrapper handler;
        try {
            handler = new ItemsAdderHandler(main);
        } catch (final Throwable t) {

            handler = new ItemsAdderWrapper();
        }
        return handler;
    }

    public String getItemsAdderName(ItemStack item) {
        return null;
    }

    public boolean isItemsAdderItem(ItemStack item) {
        return false;
    }
}
