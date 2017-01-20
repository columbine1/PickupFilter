package io.cbxn.filter.player;

import io.cbxn.filter.gui.MenuInventory;
import io.cbxn.filter.profile.Profile;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CBXN on 18/01/2017.
 * https://cbxn.io/
 *
 * Project URL: https://cbxn.io/mc/project/4-PickupFilter
 */
public class PlayerProfile {

    private boolean isEnabled = false;
    private Player owner;
    private MenuInventory currentInventory;

    private List<Profile> profiles = new ArrayList<Profile>();

    private Profile activeProfile = null;
    private Profile editingProfile = null;

    private boolean waitingOnChat = false;

    private int maxProfiles = 9;

    public boolean enableMsgs = true;

    public PlayerProfile() {
        for (int i = 0; i < getMaxProfiles(); i++) {
            Profile profile = new Profile("Profile " + (i + 1));
            getProfiles().add(profile);

            if (getActiveProfile() == null) {
                setActiveProfile(profile);
            }
        }
    }

    public boolean isEnableMsgs() {
        return enableMsgs;
    }

    public void setEnableMsgs(boolean enableMsgs) {
        this.enableMsgs = enableMsgs;
    }

    public int getMaxProfiles() {
        return maxProfiles;
    }

    public void setMaxProfiles(int maxProfiles) {
        this.maxProfiles = maxProfiles;
    }

    public Profile getActiveProfile() {
        return activeProfile;
    }

    public List<Profile> getProfiles() {
        return profiles;
    }

    public void setActiveProfile(Profile activeProfile) {
        this.activeProfile = activeProfile;
    }

    public boolean isWaitingOnChat() {
        return waitingOnChat;
    }

    public void setWaitingOnChat(boolean waitingOnChat) {
        this.waitingOnChat = waitingOnChat;
    }

    public Profile getEditingProfile() {
        return editingProfile;
    }

    public void setEditingProfile(Profile editingProfile) {
        this.editingProfile = editingProfile;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public Player getPlayer() {
        return owner;
    }

    public void setPlayer(Player player) {
        this.owner = player;
    }

    public MenuInventory getCurrentInventory() {
        return currentInventory;
    }

    public void setCurrentInventory(MenuInventory currentInventory) {
        this.currentInventory = currentInventory;
    }
}
