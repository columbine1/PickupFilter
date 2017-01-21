package io.cbxn.filter;

import io.cbxn.filter.commands.ItemFilterCommand;
import io.cbxn.filter.listeners.InventoryListener;
import io.cbxn.filter.listeners.PlayerListener;
import io.cbxn.filter.listeners.factions.FactionsListener;
import io.cbxn.filter.manager.UserManager;

import org.apache.logging.log4j.LogManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by CBXN on 08/01/2017.
 * https://cbxn.io/
 *
 * Project URL: https://cbxn.io/mc/project/4-PickupFilter
 */
public class PickupFilter extends JavaPlugin {

    public UserManager userManager;

    public boolean hasFactions = false;
    public boolean hasFactionsUUID = false;

    @Override
    public void onEnable() {
        /* Listeners */
        this.getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        this.getServer().getPluginManager().registerEvents(new InventoryListener(this), this);

        /* CommandHandler */
        this.getServer().getPluginCommand("itemfilter").setExecutor(new ItemFilterCommand(this));

        /* Other */
        userManager = new UserManager(this);
        hasFactions = checkFactionsSupport();
        hasFactionsUUID = checkFactionsUUIDSupport();

        if (hasFactions)  {
            this.getServer().getPluginManager().registerEvents(new FactionsListener(this), this);
            log("Factions support enabled!");
        } else if (hasFactionsUUID) {
            log("FactionsUUID support enabled!");
        } else {
            log("Factions plugin could not be found, not supporting!");
        }
    }

    @Override
    public void onDisable() {
        userManager = null;
    }

    public boolean allowUse(Player p) {
        return p.hasPermission("itemfilter");
    }

    public String getString(String configItem) {
        return getConfig().getString(configItem).replace("&", "\247");
    }

    public String SECRET = ChatColor.MAGIC + "" + ChatColor.DARK_AQUA + "" + ChatColor.GRAY + ChatColor.RESET;

    public void log(String s) {
        LogManager.getLogger("Minecraft").info("[ItemFilter] " + s);
    }

    private boolean checkFactionsSupport() {
        try {
            return com.massivecraft.factions.Factions.class != null;
        } catch (NoClassDefFoundError e) {
            return false;
        }
    }

    // todo add factionsUUID support (and other?)
    private boolean checkFactionsUUIDSupport() {
        try {
            return false;
        } catch (NoClassDefFoundError e) {
            return false;
        }
    }
}
