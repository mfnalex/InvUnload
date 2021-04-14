package de.jeff_media.InvUnload.Hooks;

import de.jeff_media.InvUnload.Main;
import dev.lone.itemsadder.api.CustomStack;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

public final class ItemsAdderHandler extends ItemsAdderWrapper {

    private boolean itemsAdderInstalled;

    public ItemsAdderHandler(final Main main) {

        if (Bukkit.getPluginManager().getPlugin("ItemsAdder") == null) {
            itemsAdderInstalled = false;
            return;
        }

        try {
            Class.forName("dev.lone.itemsadder.api.ItemsAdder");
            itemsAdderInstalled = true;
        } catch (Throwable t) {
            main.getLogger().warning("Found ItemsAdder plugin but could not hook into it.");
            t.printStackTrace();
            itemsAdderInstalled = false;
        }
    }

    @Override
    public String getItemsAdderName(ItemStack item) {
        if(!itemsAdderInstalled) return null;
        return CustomStack.byItemStack(item).getDisplayName();
    }

    @Override
    public boolean isItemsAdderItem(ItemStack item) {
        return itemsAdderInstalled ? CustomStack.byItemStack(item) != null : false;
    }

}
