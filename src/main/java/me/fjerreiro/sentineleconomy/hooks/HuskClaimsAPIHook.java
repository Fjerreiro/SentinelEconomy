package me.fjerreiro.sentineleconomy.hooks;

import net.william278.huskclaims.api.HuskClaimsAPI;

public class HuskClaimsAPIHook {

    private final HuskClaimsAPI huskClaimsAPI;

    public HuskClaimsAPIHook() {
        this.huskClaimsAPI = HuskClaimsAPI.getInstance();
    }
}