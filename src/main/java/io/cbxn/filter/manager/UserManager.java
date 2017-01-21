package io.cbxn.filter.manager;

import io.cbxn.filter.PickupFilter;
import io.cbxn.filter.gui.MenuInventory;
import io.cbxn.filter.player.PlayerProfile;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by CBXN on 18/01/2017.
 * https://cbxn.io/
 *
 * Project URL: https://cbxn.io/mc/project/4-PickupFilter
 */
public class UserManager {

    private Map<UUID, PlayerProfile> playerProfileMap;
    private PickupFilter pickupFilter;

    public UserManager(PickupFilter pickupFilter) {
        this.pickupFilter = pickupFilter;
        this.playerProfileMap = new HashMap<UUID, PlayerProfile>();
    }

    public Map<UUID, PlayerProfile> getPlayers() {
        return playerProfileMap;
    }

    public PlayerProfile find(UUID uuid) {
        return playerProfileMap.get(uuid);
    }

    public void openInventory(PlayerProfile pp, MenuInventory menuInventory) {
        boolean openMatchesMenu = pp.getPlayer().getOpenInventory().getTopInventory().getName().equalsIgnoreCase(menuInventory.getName());
        boolean currentMatchesMenu =  pp.getCurrentInventory() != null && pp.getCurrentInventory().getName().equalsIgnoreCase(menuInventory.getName());

        if (pp.getPlayer().getOpenInventory().getTopInventory() != null && openMatchesMenu) {
            if (pp.getCurrentInventory() != null && currentMatchesMenu) {
                menuInventory.updateInventory(pp);
                if (pp.getPlayer().getOpenInventory().getTopInventory() == null) {
                    pp.getPlayer().openInventory(menuInventory.getInventory());
                }
                return;
            }
        }

        pp.setCurrentInventory(menuInventory);
        menuInventory.setInventory(menuInventory.createInventory(pp));

        pp.getPlayer().openInventory(menuInventory.getInventory());
    }
}
