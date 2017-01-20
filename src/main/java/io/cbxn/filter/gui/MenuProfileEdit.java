package io.cbxn.filter.gui;

import io.cbxn.filter.PickupFilter;
import io.cbxn.filter.player.PlayerProfile;
import io.cbxn.filter.profile.Activation;
import io.cbxn.filter.profile.Profile;
import io.cbxn.filter.profile.ProfileState;
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
 * Created by CBXN on 19/01/2017.
 * https://cbxn.io/
 *
 * Project URL: https://cbxn.io/mc/project/4-PickupFilter
 */
public class MenuProfileEdit extends MenuInventory {

    private Profile profile;

    private List<Integer> listArmor;
    private List<Integer> listTools;
    private List<Integer> listValuable;
    private List<Integer> listPotionSupplies;
    private List<Integer> listBlocks;
    private List<Integer> listFarming;
    private List<Integer> listFood;

    public MenuProfileEdit(PickupFilter pickupFilter, PlayerProfile pp, Profile profile) {
        super(pickupFilter, pickupFilter.getString("itemfilter.chat.menu.edit.title").replace("%NAME%", profile.getName()));
        this.profile = profile;
        pp.setEditingProfile(profile);

        listArmor = pickupFilter.getConfig().getIntegerList("itemfilter.items.armor");
        listTools = pickupFilter.getConfig().getIntegerList("itemfilter.items.tools");
        listValuable = pickupFilter.getConfig().getIntegerList("itemfilter.items.vals");
        listPotionSupplies = pickupFilter.getConfig().getIntegerList("itemfilter.items.pots");
        listBlocks = pickupFilter.getConfig().getIntegerList("itemfilter.items.blocks");
        listFarming = pickupFilter.getConfig().getIntegerList("itemfilter.items.farm");
        listFood = pickupFilter.getConfig().getIntegerList("itemfilter.items.food");
    }

    public Inventory createInventory(PlayerProfile pp) {
        Inventory inventory = Bukkit.createInventory(null, 36, getName());

        inventory.setItem(10, getArmorButton(pp));
        inventory.setItem(11, getToolsButton(pp));
        inventory.setItem(12, getValuableButton(pp));
        inventory.setItem(13, getPotionSuppButton(pp));
        inventory.setItem(14, getBlocksButton(pp));
        inventory.setItem(15, getFarmingButton(pp));
        inventory.setItem(16, getFoodButton(pp));
        inventory.setItem(16, getFoodButton(pp));

        inventory.setItem(27, getWL_BLButton(pp));
        if (pickupFilter.hasFactions || pickupFilter.hasFactionsUUID) {
            inventory.setItem(29, getOwnLandButton(pp));
            inventory.setItem(30, getNeutralButton(pp));
            inventory.setItem(31, getPeacefulButton(pp));
            inventory.setItem(32, getTruceButton(pp));
            inventory.setItem(33, getAllyButton(pp));
            inventory.setItem(34, getEnemyButton(pp));
            inventory.setItem(35, getWildernessButton(pp));
        }

        return inventory;
    }

    public void updateInventory(PlayerProfile pp) {
        pp.getPlayer().getOpenInventory().setItem(10, getArmorButton(pp));
        pp.getPlayer().getOpenInventory().setItem(11, getToolsButton(pp));
        pp.getPlayer().getOpenInventory().setItem(12, getValuableButton(pp));
        pp.getPlayer().getOpenInventory().setItem(13, getPotionSuppButton(pp));
        pp.getPlayer().getOpenInventory().setItem(14, getBlocksButton(pp));
        pp.getPlayer().getOpenInventory().setItem(15, getFarmingButton(pp));
        pp.getPlayer().getOpenInventory().setItem(16, getFoodButton(pp));

        pp.getPlayer().getOpenInventory().setItem(27, getWL_BLButton(pp));

        if (pickupFilter.hasFactions || pickupFilter.hasFactionsUUID) {
            pp.getPlayer().getOpenInventory().setItem(29, getOwnLandButton(pp));
            pp.getPlayer().getOpenInventory().setItem(30, getNeutralButton(pp));
            pp.getPlayer().getOpenInventory().setItem(31, getPeacefulButton(pp));
            pp.getPlayer().getOpenInventory().setItem(32, getTruceButton(pp));
            pp.getPlayer().getOpenInventory().setItem(33, getAllyButton(pp));
            pp.getPlayer().getOpenInventory().setItem(34, getEnemyButton(pp));
            pp.getPlayer().getOpenInventory().setItem(35, getWildernessButton(pp));
        }
    }

    public void onInventoryClick(InventoryClickEvent event, PlayerProfile pp) {
        ItemStack item = event.getCurrentItem();
        if (pp == null)
            return;
        if (item == null || item.getItemMeta() == null) return;
        String nameD = item.getItemMeta().getDisplayName();
        if (nameD.startsWith(pickupFilter.SECRET + pickupFilter.getString("itemfilter.chat.menu.edit.neut_name"))) {
            checkState(Activation.NEUTRAL, pp);
        } else if (nameD.startsWith(pickupFilter.SECRET + pickupFilter.getString("itemfilter.chat.menu.edit.all_name"))) {
            checkState(Activation.ALLY, pp);
        } else if (nameD.startsWith(pickupFilter.SECRET + pickupFilter.getString("itemfilter.chat.menu.edit.enem_name"))) {
            checkState(Activation.ENEMY, pp);
        } else if (nameD.startsWith(pickupFilter.SECRET + pickupFilter.getString("itemfilter.chat.menu.edit.own_name"))) {
            checkState(Activation.OWN, pp);
        } else if (nameD.startsWith(pickupFilter.SECRET + pickupFilter.getString("itemfilter.chat.menu.edit.peac_name"))) {
            checkState(Activation.PEACEFUL, pp);
        } else if (nameD.startsWith(pickupFilter.SECRET + pickupFilter.getString("itemfilter.chat.menu.edit.truc_name"))) {
            checkState(Activation.TRUCE, pp);
        } else if (nameD.startsWith(pickupFilter.SECRET + pickupFilter.getString("itemfilter.chat.menu.edit.wild_name"))) {
            checkState(Activation.WILDERNESS, pp);
        } else if (nameD.startsWith(pickupFilter.SECRET + pickupFilter.getString("itemfilter.chat.menu.edit.all_name"))) {
            checkState(Activation.ALLY, pp);
        } else {
            String wh = "Mode: " + (profile.getState() == ProfileState.WHITELIST ?
                    ChatColor.WHITE + "" + ChatColor.BOLD + "Whitelist" :
                    ChatColor.GOLD + "" + ChatColor.BOLD + "Blacklist");
            if (nameD.equalsIgnoreCase(pickupFilter.SECRET + wh)) {
                if (profile.getState() == ProfileState.WHITELIST) {
                    profile.setState(ProfileState.BLACKLIST);
                } else {
                    profile.setState(ProfileState.WHITELIST);
                }
                updateInventory(pp);
            }
        }
    }

    private ItemStack getValuableButton(PlayerProfile pp) {
        ItemStack item = new ItemStack(Material.GOLD_BLOCK);

        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(pickupFilter.SECRET + ChatColor.AQUA + "Valuables");
        ArrayList<String> lore = new ArrayList<String>();

        item.setItemMeta(meta);
        return item;
    }

    private ItemStack getPotionSuppButton(PlayerProfile pp) {
        ItemStack item = new ItemStack(Material.POTION);

        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(pickupFilter.SECRET + ChatColor.AQUA + "Potion Supplies");

        item.setItemMeta(meta);
        return item;
    }

    private ItemStack getBlocksButton(PlayerProfile pp) {
        ItemStack item = new ItemStack(Material.DIRT);

        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(pickupFilter.SECRET + ChatColor.AQUA + "Blocks");

        item.setItemMeta(meta);
        return item;
    }

    private ItemStack getFarmingButton(PlayerProfile pp) {
        ItemStack item = new ItemStack(Material.DIAMOND_HOE);

        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(pickupFilter.SECRET + ChatColor.AQUA + "Farming");

        item.setItemMeta(meta);
        return item;
    }

    private ItemStack getFoodButton(PlayerProfile pp) {
        ItemStack item = new ItemStack(Material.TNT);

        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(pickupFilter.SECRET + ChatColor.AQUA + "Raiding Supplies");

        item.setItemMeta(meta);
        return item;
    }

    private ItemStack getArmorButton(PlayerProfile pp) {
        ItemStack item = new ItemStack(Material.DIAMOND_HELMET);

        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(pickupFilter.SECRET + ChatColor.AQUA + "Armor and Weapons");

        item.setItemMeta(meta);
        return item;
    }

    private ItemStack getToolsButton(PlayerProfile pp) {
        ItemStack item = new ItemStack(Material.DIAMOND_PICKAXE);

        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(pickupFilter.SECRET + ChatColor.AQUA + "Tools");

        item.setItemMeta(meta);
        return item;
    }

    private ItemStack getWL_BLButton(PlayerProfile pp) {
        ItemStack item = new ItemStack(Material.WOOL, 1, (short) (profile.getState() == ProfileState.WHITELIST ? 0 : 15));

        ItemMeta meta = item.getItemMeta();
        String wh = "Mode: " + (profile.getState() == ProfileState.WHITELIST ?
                ChatColor.WHITE + "" + ChatColor.BOLD + "Whitelist" :
                ChatColor.GOLD + "" + ChatColor.BOLD + "Blacklist");
        meta.setDisplayName(pickupFilter.SECRET + wh);
        ArrayList<String> lore = new ArrayList<String>();

        if (profile.getState() == ProfileState.WHITELIST) {
            lore.add(ChatColor.RESET + "Selected items will be");
            lore.add(ChatColor.RESET + "picked up by the filter.");
            lore.add("");
            lore.add(ChatColor.GRAY + "Click to switch");
        } else {
            lore.add(ChatColor.RESET + "Selected items will " + ChatColor.BOLD + "NOT" + ChatColor.RESET + " be");
            lore.add(ChatColor.RESET + "picked up by the filter.");
            lore.add("");
            lore.add(ChatColor.GRAY + "Click to switch");
        }

        meta.setLore(lore);
        item.setItemMeta(meta);

        return item;
    }

    private ItemStack getNeutralButton(PlayerProfile pp) {
        ItemStack item = new ItemStack(Material.WOOL, 1, (short) (profile.activation.contains(Activation.NEUTRAL) ? 4 : 7));

        ItemMeta meta = item.getItemMeta();
        String enableName = profile.activation.contains(Activation.NEUTRAL) ? ChatColor.GREEN + "" + ChatColor.BOLD + "(Enabled)" : ChatColor.RED + "" + ChatColor.BOLD + "(Disabled)";
        meta.setDisplayName(pickupFilter.SECRET + pickupFilter.getString("itemfilter.chat.menu.edit.neut_name") + ChatColor.RESET + " " + enableName);
        ArrayList<String> lore = new ArrayList<String>();

        Profile checked = checkSetState(Activation.NEUTRAL, pp);
        boolean allow = checked == null;

        if (allow) {
            lore.add(pickupFilter.getString("itemfilter.chat.menu.edit.neut_lore1"));
            lore.add(pickupFilter.getString("itemfilter.chat.menu.edit.neut_lore2"));
            lore.add(pickupFilter.getString("itemfilter.chat.menu.edit.neut_lore3"));
        } else if (checked != profile) {
            lore.add(pickupFilter.getString("itemfilter.chat.menu.edit.err_lore1").replace("%NAME%", checked.getName()));
            lore.add(pickupFilter.getString("itemfilter.chat.menu.edit.err_lore2").replace("%NAME%", checked.getName()));
            lore.add(pickupFilter.getString("itemfilter.chat.menu.edit.err_lore3").replace("%NAME%", checked.getName()));
        }

        meta.setLore(lore);
        item.setItemMeta(meta);

        return item;
    }

    private ItemStack getAllyButton(PlayerProfile pp) {
        ItemStack item = new ItemStack(Material.WOOL, 1, (short) (profile.activation.contains(Activation.ALLY) ? 2 : 7));

        ItemMeta meta = item.getItemMeta();
        String enableName = profile.activation.contains(Activation.ALLY) ? ChatColor.GREEN + "" + ChatColor.BOLD + "(Enabled)" : ChatColor.RED + "" + ChatColor.BOLD + "(Disabled)";
        meta.setDisplayName(pickupFilter.SECRET + pickupFilter.getString("itemfilter.chat.menu.edit.all_name") + ChatColor.RESET + " " + enableName);
        ArrayList<String> lore = new ArrayList<String>();

        Profile checked = checkSetState(Activation.ALLY, pp);
        boolean allow = checked == null;

        if (allow) {
            lore.add(pickupFilter.getString("itemfilter.chat.menu.edit.all_lore1"));
            lore.add(pickupFilter.getString("itemfilter.chat.menu.edit.all_lore2"));
            lore.add(pickupFilter.getString("itemfilter.chat.menu.edit.all_lore3"));
        } else if (checked != profile) {
            lore.add(pickupFilter.getString("itemfilter.chat.menu.edit.err_lore1").replace("%NAME%", checked.getName()));
            lore.add(pickupFilter.getString("itemfilter.chat.menu.edit.err_lore2").replace("%NAME%", checked.getName()));
            lore.add(pickupFilter.getString("itemfilter.chat.menu.edit.err_lore3").replace("%NAME%", checked.getName()));
        }

        meta.setLore(lore);
        item.setItemMeta(meta);

        return item;
    }

    private ItemStack getTruceButton(PlayerProfile pp) {
        ItemStack item = new ItemStack(Material.WOOL, 1, (short) (profile.activation.contains(Activation.TRUCE) ? 6 : 7));

        ItemMeta meta = item.getItemMeta();
        String enableName = profile.activation.contains(Activation.TRUCE) ? ChatColor.GREEN + "" + ChatColor.BOLD + "(Enabled)" : ChatColor.RED + "" + ChatColor.BOLD + "(Disabled)";
        meta.setDisplayName(pickupFilter.SECRET + pickupFilter.getString("itemfilter.chat.menu.edit.truc_name") + ChatColor.RESET + " " + enableName);
        ArrayList<String> lore = new ArrayList<String>();

        Profile checked = checkSetState(Activation.TRUCE, pp);
        boolean allow = checked == null;

        if (allow) {
            lore.add(pickupFilter.getString("itemfilter.chat.menu.edit.truc_lore1"));
            lore.add(pickupFilter.getString("itemfilter.chat.menu.edit.truc_lore2"));
            lore.add(pickupFilter.getString("itemfilter.chat.menu.edit.truc_lore3"));
        } else if (checked != profile) {
            lore.add(pickupFilter.getString("itemfilter.chat.menu.edit.err_lore1").replace("%NAME%", checked.getName()));
            lore.add(pickupFilter.getString("itemfilter.chat.menu.edit.err_lore2").replace("%NAME%", checked.getName()));
            lore.add(pickupFilter.getString("itemfilter.chat.menu.edit.err_lore3").replace("%NAME%", checked.getName()));
        }

        meta.setLore(lore);
        item.setItemMeta(meta);

        return item;
    }

    private ItemStack getPeacefulButton(PlayerProfile pp) {
        ItemStack item = new ItemStack(Material.WOOL, 1, (short) (profile.activation.contains(Activation.PEACEFUL) ? 6 : 7));

        ItemMeta meta = item.getItemMeta();
        String enableName = profile.activation.contains(Activation.PEACEFUL) ? ChatColor.GREEN + "" + ChatColor.BOLD + "(Enabled)" : ChatColor.RED + "" + ChatColor.BOLD + "(Disabled)";
        meta.setDisplayName(pickupFilter.SECRET + pickupFilter.getString("itemfilter.chat.menu.edit.peac_name") + ChatColor.RESET + " " + enableName);
        ArrayList<String> lore = new ArrayList<String>();

        Profile checked = checkSetState(Activation.PEACEFUL, pp);
        boolean allow = checked == null;

        if (allow) {
            lore.add(pickupFilter.getString("itemfilter.chat.menu.edit.peac_lore1"));
            lore.add(pickupFilter.getString("itemfilter.chat.menu.edit.peac_lore2"));
            lore.add(pickupFilter.getString("itemfilter.chat.menu.edit.peac_lore3"));
        } else if (checked != profile) {
            lore.add(pickupFilter.getString("itemfilter.chat.menu.edit.err_lore1").replace("%NAME%", checked.getName()));
            lore.add(pickupFilter.getString("itemfilter.chat.menu.edit.err_lore2").replace("%NAME%", checked.getName()));
            lore.add(pickupFilter.getString("itemfilter.chat.menu.edit.err_lore3").replace("%NAME%", checked.getName()));
        }

        meta.setLore(lore);
        item.setItemMeta(meta);

        return item;
    }

    private ItemStack getOwnLandButton(PlayerProfile pp) {
        ItemStack item = new ItemStack(Material.WOOL, 1, (short) (profile.activation.contains(Activation.OWN) ? 5 : 7));

        ItemMeta meta = item.getItemMeta();
        String enableName = profile.activation.contains(Activation.OWN) ? ChatColor.GREEN + "" + ChatColor.BOLD + "(Enabled)" : ChatColor.RED + "" + ChatColor.BOLD + "(Disabled)";
        meta.setDisplayName(pickupFilter.SECRET + pickupFilter.getString("itemfilter.chat.menu.edit.own_name") + ChatColor.RESET + " " + enableName);
        ArrayList<String> lore = new ArrayList<String>();

        Profile checked = checkSetState(Activation.OWN, pp);
        boolean allow = checked == null;

        if (allow) {
            lore.add(pickupFilter.getString("itemfilter.chat.menu.edit.own_lore1"));
            lore.add(pickupFilter.getString("itemfilter.chat.menu.edit.own_lore2"));
            lore.add(pickupFilter.getString("itemfilter.chat.menu.edit.own_lore3"));
        } else if (checked != profile) {
            lore.add(pickupFilter.getString("itemfilter.chat.menu.edit.err_lore1").replace("%NAME%", checked.getName()));
            lore.add(pickupFilter.getString("itemfilter.chat.menu.edit.err_lore2").replace("%NAME%", checked.getName()));
            lore.add(pickupFilter.getString("itemfilter.chat.menu.edit.err_lore3").replace("%NAME%", checked.getName()));
        }

        meta.setLore(lore);
        item.setItemMeta(meta);

        return item;
    }

    private ItemStack getEnemyButton(PlayerProfile pp) {
        ItemStack item = new ItemStack(Material.WOOL, 1, (short) (profile.activation.contains(Activation.ENEMY) ? 14 : 7));

        ItemMeta meta = item.getItemMeta();
        String enableName = profile.activation.contains(Activation.ENEMY) ? ChatColor.GREEN + "" + ChatColor.BOLD + "(Enabled)" : ChatColor.RED + "" + ChatColor.BOLD + "(Disabled)";
        meta.setDisplayName(pickupFilter.SECRET + pickupFilter.getString("itemfilter.chat.menu.edit.enem_name") + ChatColor.RESET + " " + enableName);
        ArrayList<String> lore = new ArrayList<String>();

        Profile checked = checkSetState(Activation.ENEMY, pp);
        boolean allow = checked == null;

        if (allow) {
            lore.add(pickupFilter.getString("itemfilter.chat.menu.edit.enem_lore1"));
            lore.add(pickupFilter.getString("itemfilter.chat.menu.edit.enem_lore2"));
            lore.add(pickupFilter.getString("itemfilter.chat.menu.edit.enem_lore3"));
        } else if (checked != profile) {
            lore.add(pickupFilter.getString("itemfilter.chat.menu.edit.err_lore1").replace("%NAME%", checked.getName()));
            lore.add(pickupFilter.getString("itemfilter.chat.menu.edit.err_lore2").replace("%NAME%", checked.getName()));
            lore.add(pickupFilter.getString("itemfilter.chat.menu.edit.err_lore3").replace("%NAME%", checked.getName()));
        }

        meta.setLore(lore);
        item.setItemMeta(meta);

        return item;
    }

    private ItemStack getWildernessButton(PlayerProfile pp) {
        ItemStack item = new ItemStack(Material.WOOL, 1, (short) (profile.activation.contains(Activation.WILDERNESS) ? 13 : 7));

        ItemMeta meta = item.getItemMeta();
        String enableName = profile.activation.contains(Activation.WILDERNESS) ? ChatColor.GREEN + "" + ChatColor.BOLD + "(Enabled)" : ChatColor.RED + "" + ChatColor.BOLD + "(Disabled)";
        meta.setDisplayName(pickupFilter.SECRET + pickupFilter.getString("itemfilter.chat.menu.edit.wild_name") + ChatColor.RESET + " " + enableName);
        ArrayList<String> lore = new ArrayList<String>();

        Profile checked = checkSetState(Activation.WILDERNESS, pp);
        boolean allow = checked == null;

        if (allow) {
            lore.add(pickupFilter.getString("itemfilter.chat.menu.edit.wild_lore1"));
            lore.add(pickupFilter.getString("itemfilter.chat.menu.edit.wild_lore2"));
            lore.add(pickupFilter.getString("itemfilter.chat.menu.edit.wild_lore3"));
        } else if (checked != profile) {
            lore.add(pickupFilter.getString("itemfilter.chat.menu.edit.err_lore1").replace("%NAME%", checked.getName()));
            lore.add(pickupFilter.getString("itemfilter.chat.menu.edit.err_lore2").replace("%NAME%", checked.getName()));
            lore.add(pickupFilter.getString("itemfilter.chat.menu.edit.err_lore3").replace("%NAME%", checked.getName()));
        }

        meta.setLore(lore);
        item.setItemMeta(meta);

        return item;
    }

    private void checkState(Activation activation, PlayerProfile pp) {
        Profile found = null;
        for (Profile p : pp.getProfiles()) {
            if (p == profile) continue;
            if (p.activation.contains(activation))
                found = p;
        }

        if (found == null) {
            if (!profile.activation.contains(activation)) {
                profile.activation.add(activation);
            } else {
                profile.activation.remove(activation);
            }
            updateInventory(pp);
        } else {
            pp.getPlayer().sendMessage(pickupFilter.getString("itemfilter.chat.menu.edit.error").replace("%NAME%", found.getName()));
        }
    }

    private Profile checkSetState(Activation activation, PlayerProfile pp) {
        Profile found = null;
        for (Profile p : pp.getProfiles()) {
            if (p.activation.contains(activation))
                found = p;
        }

        return found;
    }
}
