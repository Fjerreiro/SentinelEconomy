package me.fjerreiro.sentineleconomy;

import commands.BuyOfferCmd;
import commands.SellOfferCmd;
import database.dao.BuyOfferDB;
import database.dao.SellOfferDB;
import database.connection.DatabaseConnection;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;

public final class SentinelEconomy extends JavaPlugin {

    private static Plugin plugin;
    private static Economy econ = null;
    private DatabaseConnection databaseConnection;
    private SellOfferDB sellOfferDB;
    private BuyOfferDB buyOfferDB;

    @Override
    public void onEnable() {
        //Plugin Initialization:
        plugin = this;

        //Database initialization:
        try {
            if (!getDataFolder().exists()) {
                getLogger().finest("Datafolder doesn't exist yet, making one now.");
                getDataFolder().mkdirs();
            }
            databaseConnection = new DatabaseConnection(getDataFolder().getAbsolutePath() + "/database.db");
            sellOfferDB = new SellOfferDB(databaseConnection);
            buyOfferDB = new BuyOfferDB(databaseConnection);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //Command initialization:
        getCommand("selloffer").setExecutor(new SellOfferCmd(sellOfferDB));
        getCommand("buyoffer").setExecutor(new BuyOfferCmd(buyOfferDB));


        //Set up the hook between Vault and SentinelEconomy.
        if (!setupEconomy() ) {
            getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        databaseConnection.closeConnection();
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

    public SellOfferDB getSellOfferDB() {
        return this.sellOfferDB;
    }
}