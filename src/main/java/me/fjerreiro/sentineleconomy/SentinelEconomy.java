package me.fjerreiro.sentineleconomy;

import Commands.BuyOfferCmd;
import Commands.SellOfferCmd;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class SentinelEconomy extends JavaPlugin {

    private static Plugin plugin;
    private static Economy econ = null;

    @Override
    public void onEnable() {
        //Initialization:
        plugin = this;

        //Initialize the config for the first connection.
        getConfig().options().copyDefaults();
        saveDefaultConfig();

        //Register the commands that are used in this plugin.
        getCommand("buyoffer").setExecutor(new BuyOfferCmd());
        getCommand("selloffer").setExecutor(new SellOfferCmd());

        //Set up the hook between Vault and SentinelEconomy.
        if (!setupEconomy() ) {
            getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
    }

    @Override
    public void onDisable() {
    }

    //Methods to set up the Economy hook for Vault.
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

    public static Plugin getPlugin() {
        return plugin;
    }
}