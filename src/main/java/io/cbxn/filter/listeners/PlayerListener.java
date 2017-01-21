package io.cbxn.filter.listeners;

import io.cbxn.filter.PickupFilter;
import io.cbxn.filter.gui.MenuProfileList;
import io.cbxn.filter.player.PlayerProfile;
import io.cbxn.filter.profile.Profile;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;

import java.util.regex.Pattern;

/**
 * Created by CBXN on 18/01/2017.
 * https://cbxn.io/
 *
 * Project URL: https://cbxn.io/mc/project/4-PickupFilter
 */
public class PlayerListener implements Listener {

    private PickupFilter pickupFilter;

    public PlayerListener(PickupFilter pickupFilter) {
        this.pickupFilter = pickupFilter;
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        if (pickupFilter.allowUse(event.getPlayer())) {
            PlayerProfile found = pickupFilter.userManager.find(event.getPlayer().getUniqueId());

            if (found != null && found.isWaitingOnChat() && found.getEditingProfile() != null) {
                found.setWaitingOnChat(false);
                found.setEditingProfile(null);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChatMessage(AsyncPlayerChatEvent event) {
        if (pickupFilter.allowUse(event.getPlayer())) {
            PlayerProfile found = pickupFilter.userManager.find(event.getPlayer().getUniqueId());

            if (found != null && found.isWaitingOnChat() && found.getEditingProfile() != null) {
                event.setCancelled(true);
                String newName = event.getMessage();

                if (newName.length() > 12 || newName.length() < 3) {
                    event.getPlayer().sendMessage(pickupFilter.getString("itemfilter.chat.profile.error"));
                    return;
                }

                Pattern p = Pattern.compile("[a-zA-z0-9 ]*");
                if (!p.matcher(newName).matches()) {
                    event.getPlayer().sendMessage(pickupFilter.getString("itemfilter.chat.profile.invalid"));
                    return;
                }

                for (Profile profile : found.getProfiles()) {
                    if (profile.getName().equalsIgnoreCase(newName)) {
                        event.getPlayer().sendMessage(pickupFilter.getString("itemfilter.chat.profile.exists"));
                        return;
                    }
                }

                found.getEditingProfile().setName(newName);
                found.setWaitingOnChat(false);
                event.getPlayer().sendMessage(pickupFilter.getString("itemfilter.chat.profile.success").replace("%NAME%", newName.toUpperCase()));
                pickupFilter.userManager.openInventory(found, new MenuProfileList(pickupFilter));
            }
        }
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
                        allowPickup = check && active.getItems().contains(event.getItem().getItemStack().getType().name());
                        break;
                    case BLACKLIST:
                        allowPickup = check && !active.getItems().contains(event.getItem().getItemStack().getType().name());
                        break;
                }

                if (!allowPickup) {
                    event.setCancelled(true);
                    // todo: Optionally delete non picked up items perhaps?
                }
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        this.handleJoin(event.getPlayer());
    }

    @EventHandler
    public void onPlayerKick(PlayerKickEvent event) {
        this.handleQuit(event.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        this.handleQuit(event.getPlayer());
    }

    private void handleJoin(Player p) {
        PlayerProfile playerProfile = new PlayerProfile();
        playerProfile.setPlayer(p);

        // todo: Load settings

        for (int i = 9; i >= 1; i--) {
            if (p.hasPermission("itemfilter.profiles.max." + i)) {
                playerProfile.setMaxProfiles(i);
                break;
            }
        }

        pickupFilter.userManager.getPlayers().put(p.getUniqueId(), playerProfile);
    }

    private void handleQuit(Player p) {
        // todo: Save settings
        pickupFilter.userManager.getPlayers().remove(p.getUniqueId());
    }
}
