package io.cbxn.filter.listeners;

import io.cbxn.filter.PickupFilter;
import io.cbxn.filter.player.PlayerProfile;
import io.cbxn.filter.profile.Profile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;

/**
 * Created by CBXN on 18/01/2017.
 * https://cbxn.io/
 *
 * Project URL: https://cbxn.io/mc/project/4-PickupFilter
 */
public class PickupListener implements Listener {

    private PickupFilter pickupFilter;

    public PickupListener(PickupFilter pickupFilter) {
        this.pickupFilter = pickupFilter;
    }

    @EventHandler
    public void onItemPickup(PlayerPickupItemEvent event) {
        if (pickupFilter.allowUse(event.getPlayer())) {
            PlayerProfile found = pickupFilter.userManager.find(event.getPlayer().getUniqueId());

            if (found != null && found.isEnabled() && found.getActiveProfile() != null) {
                Profile active = found.getActiveProfile();
                boolean allowPickup = true;
                boolean check = !(event.getItem() == null || event.getItem().getItemStack() == null);

                switch (active.getState()) {
                    case WHITELIST:
                        allowPickup = check && active.getItems().contains(event.getItem().getItemStack());
                        break;
                    case BLACKLIST:
                        allowPickup = check && !active.getItems().contains(event.getItem().getItemStack());
                        break;
                }

                if (!allowPickup) {
                    event.setCancelled(true);
                    // todo: Optionally delete non picked up items perhaps?
                }
            }
        }
    }
}
