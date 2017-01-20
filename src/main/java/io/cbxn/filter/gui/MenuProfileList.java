package io.cbxn.filter.gui;

import io.cbxn.filter.PickupFilter;
import io.cbxn.filter.player.PlayerProfile;
import io.cbxn.filter.profile.Profile;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

/**
 * Created by CBXN on 18/01/2017.
 * https://cbxn.io/
 *
 * Project URL: https://cbxn.io/mc/project/4-PickupFilter
 */
public class MenuProfileList extends MenuInventory {


    public MenuProfileList(PickupFilter pickupFilter) {
        super(pickupFilter, pickupFilter.getString("itemfilter.chat.menu.profiles.title"));
    }

    public Inventory createInventory(PlayerProfile p) {
        Inventory menuProfileInventory = Bukkit.createInventory(null, 36, getName());

        for (int i = 0; i < p.getMaxProfiles(); i++) {
            Profile profile = null;
            if (p.getProfiles().size() - 1 >= i) {
                profile = p.getProfiles().get(i);
            }

            boolean enabled = false;
            if (profile == p.getActiveProfile()) {
                enabled = true;
            }

            menuProfileInventory.setItem(i, getEditProfileButton(profile, 1));
            menuProfileInventory.setItem(9 + i, getProfileSelectButton(profile, enabled, i + 1));
        }

        menuProfileInventory.setItem(27, getToggleFilterButton(p));
        menuProfileInventory.setItem(35, getShowMessageButton(p));

        return menuProfileInventory;
    }

    public void updateInventory(PlayerProfile p) {
        for (int i = 0; i < p.getMaxProfiles(); i++) {
            Profile profile = p.getProfiles().get(i);

            boolean enabled = false;
            if (p.getActiveProfile() == null) {
                p.setActiveProfile(profile);
                enabled = true;
            } else if (profile == p.getActiveProfile()) {
                enabled = true;
            }

            p.getPlayer().getOpenInventory().setItem(i, getEditProfileButton(profile, 1));
            p.getPlayer().getOpenInventory().setItem(9 + i, getProfileSelectButton(profile, enabled, i + 1));
        }
        p.getPlayer().getOpenInventory().setItem(35, getShowMessageButton(p));
        p.getPlayer().getOpenInventory().setItem(27, getToggleFilterButton(p));
    }

    public void onInventoryClick(InventoryClickEvent event, PlayerProfile found) {
        Player player = (Player) event.getWhoClicked();
        ItemStack item = event.getCurrentItem();

        if (found == null)
            return;
        if (item == null || item.getItemMeta() == null) return;
        if (item.getItemMeta().getDisplayName().equalsIgnoreCase(pickupFilter.SECRET + pickupFilter.getString("itemfilter.chat.menu.profiles.activate"))) {
            if (item.getItemMeta().getLore().size() == 1) {
                for (Profile p : found.getProfiles()) {
                    if ((ChatColor.GRAY + "" + ChatColor.RESET + p.getName()).equalsIgnoreCase(item.getItemMeta().getLore().get(0))) {
                        found.setActiveProfile(p);
                        pickupFilter.userManager.openInventory(found, new MenuProfileList(pickupFilter));
                        break;
                    }
                }
            }
        } else if (item.getItemMeta().getDisplayName().equalsIgnoreCase(pickupFilter.SECRET + pickupFilter.getString("itemfilter.chat.menu.profiles.enable"))) {
            found.setEnabled(true);
            pickupFilter.userManager.openInventory(found, new MenuProfileList(pickupFilter));
        } else if (item.getItemMeta().getDisplayName().equalsIgnoreCase(pickupFilter.SECRET + pickupFilter.getString("itemfilter.chat.menu.profiles.disable"))) {
            found.setEnabled(false);
            pickupFilter.userManager.openInventory(found, new MenuProfileList(pickupFilter));
        } else if (item.getItemMeta().getDisplayName().equalsIgnoreCase(pickupFilter.SECRET + ChatColor.WHITE + "Chat Messages")) {
            found.setEnableMsgs(!found.isEnableMsgs());
            pickupFilter.userManager.openInventory(found, new MenuProfileList(pickupFilter));
        } else {
            Profile edit = null;
            for (Profile p : found.getProfiles()) {
                if ((pickupFilter.SECRET + ChatColor.BLUE + p.getName()).equalsIgnoreCase(item.getItemMeta().getDisplayName())) {
                    edit = p;
                    break;
                }
            }

            if (edit == null) {
                return;
            }

            if (event.isLeftClick()) {
                pickupFilter.userManager.openInventory(found, new MenuProfileEdit(pickupFilter, found, edit));
            } else if (event.isRightClick()) {
                found.setWaitingOnChat(true);
                found.setEditingProfile(edit);
                player.closeInventory();
                player.sendMessage(" ");
                player.sendMessage(" ");
                player.sendMessage(pickupFilter.getString("itemfilter.chat.menu.profiles.chatmsg"));
                player.sendMessage(" ");
                player.sendMessage(" ");
            }
        }
    }

    private ItemStack getToggleFilterButton(PlayerProfile pp) {
        ItemStack item = new ItemStack(Material.INK_SACK, 1, (short) (pp.isEnabled() ? 10 : 8));

        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(pickupFilter.SECRET + pickupFilter.getString(pp.isEnabled() ? "itemfilter.chat.menu.profiles.disable" : "itemfilter.chat.menu.profiles.enable"));
        ArrayList<String> lore = new ArrayList<String>();
        meta.setLore(lore);
        item.setItemMeta(meta);

        return item;
    }

    private ItemStack getEditProfileButton(Profile profile, int amount) {
        ItemStack item = profile.getIcon();

        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(pickupFilter.SECRET + ChatColor.BLUE + profile.getName());
        ArrayList<String> lore = new ArrayList<String>();

        lore.add(pickupFilter.getString("itemfilter.chat.menu.profiles.left"));
        lore.add(pickupFilter.getString("itemfilter.chat.menu.profiles.right"));
        lore.add(" ");
        lore.add(ChatColor.GRAY + profile.getState().getName());

        meta.setLore(lore);
        item.setItemMeta(meta);

        return item;
    }

    private ItemStack getProfileSelectButton(Profile p, boolean enabled, int amount) {
        ItemStack item = new ItemStack(Material.INK_SACK, amount, (short) (enabled ? 10 : 8));

        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(pickupFilter.SECRET + (enabled ? pickupFilter.getString("itemfilter.chat.menu.profiles.current") : pickupFilter.getString("itemfilter.chat.menu.profiles.activate")));
        ArrayList<String> lore = new ArrayList<String>();
        lore.add(ChatColor.GRAY + "" + ChatColor.RESET + p.getName());
        meta.setLore(lore);
        item.setItemMeta(meta);

        return item;
    }

    private ItemStack getShowMessageButton(PlayerProfile pp) {
        ItemStack item = new ItemStack(Material.INK_SACK, 1, (short) (pp.isEnableMsgs() ? 13 : 8));

        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(pickupFilter.SECRET + ChatColor.WHITE + "Chat Messages");
        ArrayList<String> lore = new ArrayList<String>();
        if (pp.isEnableMsgs()) {
            lore.add(pickupFilter.getString("itemfilter.chat.menu.profiles.enablmsg"));
        } else {
            lore.add(pickupFilter.getString("itemfilter.chat.menu.profiles.disablmsg"));
        }
        lore.add(pickupFilter.getString("itemfilter.chat.menu.profiles.msglore1"));
        lore.add(pickupFilter.getString("itemfilter.chat.menu.profiles.msglore2"));
        meta.setLore(lore);
        item.setItemMeta(meta);

        return item;
    }
}
