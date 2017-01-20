package io.cbxn.filter.profile;

/**
 * Created by CBXN on 18/01/2017.
 * https://cbxn.io/
 *
 * Project URL: https://cbxn.io/mc/project/4-PickupFilter
 */
public enum ProfileState {

    WHITELIST("Whitelist"),
    BLACKLIST("Blacklist");

    private String name;

    ProfileState(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
