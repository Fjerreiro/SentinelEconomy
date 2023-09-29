package me.fjerreiro.sentineleconomy;

import commands.SellOfferCmd;
import database.Database;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;

public final class SentinelEconomy extends JavaPlugin {

    private static Plugin plugin;
    private static Economy econ = null;
    private Database database;

    @Override
    public void onEnable() {
        //Initialization:
        plugin = this;

        //Initialize the config for the first connection.
        getConfig().options().copyDefaults();
        saveDefaultConfig();
        getLogger().finest("Config file successfully loaded.");

        //Register the commands that are used in this plugin.
        getCommand("selloffer").setExecutor(new SellOfferCmd(this));

        //Set up the hook between Vault and SentinelEconomy.
        if (!setupEconomy() ) {
            getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        try {
            if (!getDataFolder().exists()) {
                getLogger().finest("Datafolder doesn't exist yet, making one now.");
                getDataFolder().mkdirs();
            }

            database = new Database(getDataFolder().getAbsolutePath() + "/database.db");
            getLogger().fine("SQLite successfully connected.");
        } catch (SQLException e) {
            e.printStackTrace();
            getLogger().severe("Failed to connect to the database!");
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        try {
            database.closeConnection();
        } catch (SQLException e) {
            getLogger().severe("Failed to close connection!");
        }
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

    public Database getDatabase() {
        return this.database;
    }
}