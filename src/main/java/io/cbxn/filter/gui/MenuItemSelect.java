package io.cbxn.filter.gui;

import io.cbxn.filter.PickupFilter;
import io.cbxn.filter.player.PlayerProfile;
import io.cbxn.filter.profile.Profile;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CBXN on 20/01/2017.
 * https://cbxn.io/
 *
 * Project URL: https://cbxn.io/mc/project/4-PickupFilter
 */
public class MenuItemSelect extends MenuInventory {

    private List<String> itemList;
    private Profile profile;
    private PlayerProfile pp;

    public MenuItemSelect(PickupFilter pickupFilter, PlayerProfile pp, Profile profile, List<String> itemList) {
        super(pickupFilter, pickupFilter.getString("itemfilter.chat.menu.item.title"));

        this.profile = profile;
        this.pp = pp;
        this.itemList = itemList;
    }

    public Inventory createInventory(PlayerProfile pp) {
        Inventory inv = Bukkit.createInventory(null, 45, getName());

        for (int i = 0; i < itemList.size(); i++) {
            String matName = itemList.get(i);
            if (matName.equalsIgnoreCase("AIR") || i >= 36) continue;

            ItemStack itemStack = getItemButton(profile, matName);
            if (itemStack != null) {
                inv.setItem(i, itemStack);
            }
        }

        inv.setItem(36, getBackButton());

        return inv;
    }

    private ItemStack getItemButton(Profile profile, String matName) {
        Material mat = Material.getMaterial(matName);
        if (mat == null) return null;

        ItemStack item = new ItemStack(mat);

        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(pickupFilter.SECRET + ChatColor.GOLD + WordUtils.capitalize(mat.name().replaceAll("_", " ").toLowerCase()));
        ArrayList<String> lore = new ArrayList<String>();

        boolean contains = profile.getItems().contains(matName);
        lore.add(ChatColor.RESET + "" + ChatColor.GRAY + (contains ? ChatColor.GREEN + "" + ChatColor.BOLD + "Enabled" : ChatColor.RED + "" + ChatColor.BOLD + "Disabled"));
        lore.add("");
        lore.add(ChatColor.RESET + "" + ChatColor.GOLD + "Left click " + ChatColor.GRAY + "toggle status");
        lore.add(ChatColor.RESET + "" + ChatColor.GOLD + "+Shift click " + ChatColor.GRAY + "set profile icon");
        meta.setLore(lore);
        item.setItemMeta(meta);

        return item;
    }

    private ItemStack getBackButton() {
        ItemStack item = new ItemStack(Material.PAPER);

        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(pickupFilter.SECRET + ChatColor.GOLD + "Back");
        item.setItemMeta(meta);

        return item;
    }

    public void updateInventory(PlayerProfile pp) {
        for (int i = 0; i < itemList.size(); i++) {
            String matName = itemList.get(i);
            if (matName.equalsIgnoreCase("AIR")) continue;

            ItemStack itemStack = getItemButton(profile, matName);
            if (itemStack != null) {
                pp.getPlayer().getOpenInventory().setItem(i, itemStack);
            }
        }
        pp.getPlayer().getOpenInventory().setItem(36, getBackButton());
    }

    public void onInventoryClick(InventoryClickEvent event, PlayerProfile pp) {
        ItemStack item = event.getCurrentItem();
        if (pp == null)
            return;
        if (item == null || item.getItemMeta() == null) return;
        String nameD = item.getItemMeta().getDisplayName();
        if (nameD.equalsIgnoreCase(pickupFilter.SECRET + ChatColor.GOLD + "Back")) {
            pickupFilter.userManager.openInventory(pp, new MenuProfileEdit(pickupFilter, pp, profile));
        } else {
            Material findMaterial = Material.getMaterial(item.getType().name());
            if (findMaterial != null) {
                if (event.isLeftClick() && event.isShiftClick()) {
                    profile.setIcon(new ItemStack(findMaterial));
                    pp.getPlayer().sendMessage(pickupFilter.getString("itemfilter.chat.menu.item.update"));
                } else if (event.isLeftClick()) {
                    if (profile.getItems().contains(findMaterial.name())) {
                        profile.getItems().remove(profile.getItems().indexOf(findMaterial.name()));
                    } else {
                        profile.getItems().add(findMaterial.name());
                    }
                    updateInventory(pp);
                }
            }
        }
    }
}
