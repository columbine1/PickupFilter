package io.cbxn.filter.listeners;

import io.cbxn.filter.PickupFilter;
import io.cbxn.filter.gui.MenuProfileList;
import io.cbxn.filter.player.PlayerProfile;
import io.cbxn.filter.profile.Profile;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitScheduler;

/**
 * Created by CBXN on 19/01/2017.
 * https://cbxn.io/
 *
 * Project URL: https://cbxn.io/mc/project/4-PickupFilter
 */
public class InventoryListener implements Listener {

    private PickupFilter pickupFilter;

    public InventoryListener(PickupFilter pickupFilter) {
        this.pickupFilter = pickupFilter;
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        final PlayerProfile found = pickupFilter.userManager.find(player.getUniqueId());

        if (found != null && found.getCurrentInventory() != null) {
            if (event.getInventory().getName().equalsIgnoreCase(found.getCurrentInventory().getName())) {

                if (found.getEditingProfile() != null && found.getCurrentInventory().getName().equalsIgnoreCase(pickupFilter.getString("itemfilter.chat.menu.edit.title").replace("%NAME%", found.getEditingProfile().getName()))) {
                    BukkitScheduler scheduler = pickupFilter.getServer().getScheduler();
                    scheduler.scheduleSyncDelayedTask(pickupFilter, new Runnable() {
                        public void run() {
                            found.setCurrentInventory(null);
                            found.getPlayer().closeInventory();
                            pickupFilter.userManager.openInventory(found, new MenuProfileList(pickupFilter));
                        }
                    }, 1L);
                } else {
                    found.setCurrentInventory(null);
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        ItemStack item = event.getCurrentItem();
        Inventory inventory = event.getInventory();

        if (item == null || inventory == null || player == null || item.getData().getItemType() == Material.AIR || item.getItemMeta() == null) return;

        PlayerProfile found = pickupFilter.userManager.find(player.getUniqueId());
        if (found != null && found.getCurrentInventory() != null && found.getCurrentInventory().getName().equalsIgnoreCase(inventory.getName())) {
            if(event.getClickedInventory().getType() == InventoryType.PLAYER) return;
            event.setCancelled(true);
            found.getCurrentInventory().onInventoryClick(event, found);
        }
    }
}
