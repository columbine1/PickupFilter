package io.cbxn.filter.profile;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CBXN on 18/01/2017.
 * https://cbxn.io/
 *
 * Project URL: https://cbxn.io/mc/project/4-PickupFilter
 */
public class Profile {

    private String name;
    private ProfileState state = ProfileState.WHITELIST;
    private final List<ItemStack> items = new ArrayList<ItemStack>();
    public List<Activation> activation = new ArrayList<Activation>();

    public void setIcon(ItemStack icon) {
        this.icon = icon;
    }

    private ItemStack icon;

    public Profile(String name) {
        this.name = name;
        icon = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProfileState getState() {
        return state;
    }

    public void setState(ProfileState state) {
        this.state = state;
    }

    public List<ItemStack> getItems() {
        return items;
    }


    public ItemStack getIcon() {
        return icon;
    }
}
