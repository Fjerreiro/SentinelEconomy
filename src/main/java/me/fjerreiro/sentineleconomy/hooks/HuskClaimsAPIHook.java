package me.fjerreiro.sentineleconomy.hooks;

import net.william278.huskclaims.api.BukkitHuskClaimsAPI;

public class HuskClaimsAPIHook {

    private final BukkitHuskClaimsAPI huskClaimsAPI;

    public HuskClaimsAPIHook() {
        this.huskClaimsAPI = BukkitHuskClaimsAPI.getInstance();
    }
}