package io.cbxn.filter.gui;

import io.cbxn.filter.PickupFilter;
import io.cbxn.filter.player.PlayerProfile;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

/**
 * Created by CBXN on 19/01/2017.
 * https://cbxn.io/
 *
 * Project URL: https://cbxn.io/mc/project/4-PickupFilter
 */
public abstract class MenuInventory {

    protected Inventory inventory;
    private final String name;
    protected PickupFilter pickupFilter;

    public MenuInventory(PickupFilter pickupFilter, String name) {
        this.name = name;
        this.pickupFilter = pickupFilter;
    }

    public abstract Inventory createInventory(PlayerProfile pp);
    public abstract void updateInventory(PlayerProfile pp);
    public abstract void onInventoryClick(InventoryClickEvent event, PlayerProfile pp);

    public String getName() {
        return name;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }
}
