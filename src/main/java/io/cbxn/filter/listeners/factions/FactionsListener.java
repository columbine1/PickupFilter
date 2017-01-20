package io.cbxn.filter.listeners.factions;

import com.massivecraft.factions.Rel;
import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MFlag;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.ps.PS;
import com.massivecraft.massivecore.util.MUtil;
import io.cbxn.filter.PickupFilter;
import io.cbxn.filter.player.PlayerProfile;
import io.cbxn.filter.profile.Activation;
import io.cbxn.filter.profile.Profile;
import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 * Created by CBXN on 19/01/2017.
 * https://cbxn.io/
 *
 * Project URL: https://cbxn.io/mc/project/4-PickupFilter
 */
public class FactionsListener implements Listener {

    private PickupFilter pickupFilter;

    public FactionsListener(PickupFilter pickupFilter) {
        this.pickupFilter = pickupFilter;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (pickupFilter.allowUse(event.getPlayer())) {
            if (MUtil.isSameChunk(event)) return;
            Player player = event.getPlayer();
            if (MUtil.isntPlayer(player)) return;
            PlayerProfile found = pickupFilter.userManager.find(event.getPlayer().getUniqueId());
            if (found == null || !found.isEnabled()) return;

            MPlayer mplayer = MPlayer.get(player);
            PS chunkTo = PS.valueOf(event.getTo()).getChunk(true);
            Faction factionTo = BoardColl.get().getFactionAt(chunkTo);

            if (factionTo == mplayer.getFaction()) {
                checkAndActivateProfile(Activation.OWN, found);
            } else if (factionTo.getFlag(MFlag.getFlagPeaceful())) {
                checkAndActivateProfile(Activation.PEACEFUL, found);
            } else if (factionTo.isNone()) {
                checkAndActivateProfile(Activation.WILDERNESS, found);
            } else {
                if (factionTo.getRelationTo(mplayer.getFaction()) != Rel.NEUTRAL){
                    checkAndActivateProfile(Activation.NEUTRAL, found);
                } else if(factionTo.getRelationTo(mplayer.getFaction()) != Rel.ALLY){
                    checkAndActivateProfile(Activation.ALLY, found);
                } else if(factionTo.getRelationTo(mplayer.getFaction()) != Rel.TRUCE){
                    checkAndActivateProfile(Activation.TRUCE, found);
                } else if(factionTo.getRelationTo(mplayer.getFaction()) != Rel.ENEMY){
                    checkAndActivateProfile(Activation.ENEMY, found);
                }
            }
        }
    }

    private void checkAndActivateProfile(Activation activation, PlayerProfile pp) {
        Profile found = null;
        for (Profile p : pp.getProfiles()) {
            if (p.activation.contains(activation)) {
                found = p;
            }
        }

        if (found != null && pp.getActiveProfile() != null && pp.getActiveProfile() != found) {
            pp.setActiveProfile(found);
            if (pp.isEnableMsgs()){
                pp.getPlayer().sendMessage(pickupFilter.getString("itemfilter.chat.profileset").replace("%NAME%", found.getName()) + ChatColor.GRAY + " (" + WordUtils.capitalize(activation.name().toLowerCase()) + ")");
            }
        }
    }
}
