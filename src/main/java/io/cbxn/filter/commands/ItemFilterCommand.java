package io.cbxn.filter.commands;

import io.cbxn.filter.PickupFilter;
import io.cbxn.filter.gui.MenuProfileList;
import io.cbxn.filter.player.PlayerProfile;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by CBXN on 18/01/2017.
 * https://cbxn.io/
 *
 * Project URL: https://cbxn.io/mc/project/4-PickupFilter
 */
public class ItemFilterCommand implements CommandExecutor {

    private PickupFilter pickupFilter;

    public ItemFilterCommand(PickupFilter pickupFilter) {
        this.pickupFilter = pickupFilter;
    }

    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        Player player = pickupFilter.getServer().getPlayer(commandSender.getName());
        if (pickupFilter.allowUse(player)) {
            PlayerProfile found = pickupFilter.userManager.find(player.getUniqueId());

            if (found != null) {
                if (args.length == 0) {
                    pickupFilter.userManager.openInventory(found, new MenuProfileList(pickupFilter));
                    return true;
                } else if (args[0].equalsIgnoreCase("enable") || (args[0].equalsIgnoreCase("toggle") && !found.isEnabled())) {
                    found.setEnabled(true);
                    commandSender.sendMessage(pickupFilter.getString("itemfilter.chat.status.enabled"));
                    return true;
                } else if (args[0].equalsIgnoreCase("disable") || (args[0].equalsIgnoreCase("toggle") && found.isEnabled())) {
                    found.setEnabled(false);
                    commandSender.sendMessage(pickupFilter.getString("itemfilter.status.chat.disabled"));
                    return true;
                }
            } else {
                commandSender.sendMessage(pickupFilter.getString("itemfilter.chat.error"));
                return false;
            }
        } else {
            commandSender.sendMessage(pickupFilter.getString("itemfilter.chat.error"));
            return false;
        }

        return false;
    }
}
