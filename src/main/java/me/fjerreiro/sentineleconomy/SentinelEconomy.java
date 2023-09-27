package me.fjerreiro.sentineleconomy;

import Commands.BuyOfferCmd;
import Commands.SellOfferCmd;
import org.bukkit.plugin.java.JavaPlugin;

public final class SentinelEconomy extends JavaPlugin {

    @Override
    public void onEnable() {
        getCommand("buyoffer").setExecutor(new BuyOfferCmd());
        getCommand("selloffer").setExecutor(new SellOfferCmd());
    }

    @Override
    public void onDisable() {
    }
}
