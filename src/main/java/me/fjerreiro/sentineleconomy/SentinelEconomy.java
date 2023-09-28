package me.fjerreiro.sentineleconomy;

import Commands.BuyOfferCmd;
import Commands.SellOfferCmd;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class SentinelEconomy extends JavaPlugin {

    private static Economy econ = null;

    @Override
    public void onEnable() {
        getCommand("buyoffer").setExecutor(new BuyOfferCmd());
        getCommand("selloffer").setExecutor(new SellOfferCmd());

        if (!setupEconomy() ) {
            getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
    }

    @Override
    public void onDisable() {
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    public static Economy getEconomy() {
        return econ;
    }

}