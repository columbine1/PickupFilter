package io.cbxn.filter.manager;

import io.cbxn.filter.PickupFilter;
import io.cbxn.filter.gui.MenuInventory;
import io.cbxn.filter.player.PlayerProfile;
import io.cbxn.filter.profile.Activation;
import io.cbxn.filter.profile.Profile;
import io.cbxn.filter.profile.ProfileState;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.util.*;

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

    public void load(PlayerProfile player, UUID uuid) {
        File folder = new File(pickupFilter.getDataFolder() + File.separator + "userdata");
        if (!folder.exists()) folder.mkdirs();

        File userfile = new File(pickupFilter.getDataFolder() + File.separator + "userdata", uuid + ".yml");

        try {
            if (userfile.exists()) {
                YamlConfiguration user = new YamlConfiguration();
                user.load(userfile);

                player.setEnabled(user.getBoolean("if.enable"));
                player.setEnableMsgs(user.getBoolean("if.messages"));

                for (int i = 0; i < 9; i++) {
                    Profile PROFILE = player.getProfiles().get(i);

                    if (PROFILE != null) {
                        PROFILE.setName(user.getString("if.profile_" + i + ".name"));

                        ProfileState state = null;
                        for (ProfileState PS : ProfileState.values()) {
                            if (PS.name().equalsIgnoreCase(user.getString("if.profile_" + i + ".state"))) {
                                state = PS;
                            }
                        }
                        if (state != null) {
                            PROFILE.setState(state);
                        }

                        List<String> actNames = (List<String>) user.getList("if.profile_" + i + ".activation");
                        for (String s : actNames) {
                            for (Activation a : Activation.values()) {
                                if (a.name().equalsIgnoreCase(s)) {
                                    PROFILE.activation.add(a);
                                }
                            }
                        }

                        PROFILE.items = (List<String>) user.getList("if.profile_" + i + ".items");
                        Material mat = Material.getMaterial(user.getString("if.profile_" + i + ".icon"));
                        if (mat != null) {
                            PROFILE.setIcon(new ItemStack(mat));
                        }
                    }
                }

                if (!user.getString("if.activeProfile").equalsIgnoreCase("no")) {
                    for (Profile PROFILE : player.getProfiles()) {
                        if (PROFILE.getName().equalsIgnoreCase(user.getString("if.activeProfile"))) {
                            player.setActiveProfile(PROFILE);
                            break;
                        }
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void save(PlayerProfile player, UUID uuid) {
        File folder = new File(pickupFilter.getDataFolder() + File.separator + "userdata");
        if (!folder.exists()) folder.mkdirs();

        File userfile = new File(pickupFilter.getDataFolder() + File.separator + "userdata", uuid + ".yml");

        try {
            boolean flag = userfile.exists() || userfile.createNewFile();

            if (flag) {
                YamlConfiguration user = new YamlConfiguration();

                user.set("if.enable", player.isEnabled());
                if (player.getActiveProfile() != null) {
                    user.set("if.activeProfile", player.getActiveProfile().getName());
                } else {
                    user.set("if.activeProfile", "no");
                }
                user.set("if.messages", player.isEnableMsgs());

                for (int i = 0; i < 9; i++) {
                    Profile PROFILE = player.getProfiles().get(i);
                    if (PROFILE != null) {
                        List<String> tmpList = new ArrayList<String>();
                        for (Activation a : PROFILE.activation) {
                            tmpList.add(a.name());
                        }

                        user.set("if.profile_" + i + ".name", PROFILE.getName());
                        user.set("if.profile_" + i + ".state", PROFILE.getState().name());
                        user.set("if.profile_" + i + ".items", PROFILE.getItems());
                        user.set("if.profile_" + i + ".activation", tmpList);
                        if (!PROFILE.getIcon().getType().name().equalsIgnoreCase("STAINED_GLASS_PANE")) {
                            user.set("if.profile_" + i + ".icon", PROFILE.getIcon().getType().name());
                        }
                    }
                }

                user.save(userfile);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
