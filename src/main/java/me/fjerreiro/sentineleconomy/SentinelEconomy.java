package me.fjerreiro.sentineleconomy;

import me.fjerreiro.sentineleconomy.commands.BuyOfferCmd;
import me.fjerreiro.sentineleconomy.commands.SellOfferCmd;
import database.dao.BuyOfferDB;
import database.dao.SellOfferDB;
import me.fjerreiro.sentineleconomy.database.DatabaseConnection;
import me.fjerreiro.sentineleconomy.hooks.HuskClaimsAPIHook;
import me.fjerreiro.sentineleconomy.hooks.VaultHook;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import java.sql.SQLException;

public final class SentinelEconomy extends JavaPlugin {

    private static Plugin plugin;

    private DatabaseConnection databaseConnection;
    private SellOfferDB sellOfferDB;
    private BuyOfferDB buyOfferDB;
    public HuskClaimsAPIHook huskClaimsAPIHook;

    @Override
    public void onEnable() {
        //Plugin Initialization:
        plugin = this;


        //Setting up Hooks:
        if (!VaultHook.setupEconomy()) {
            getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
        }


        if (getServer().getPluginManager().getPlugin("HuskClaims") != null) {
            this.huskClaimsAPIHook = new HuskClaimsAPIHook();
        }




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


    }


    @Override
    public void onDisable() {
        databaseConnection.closeConnection();
    }




    public static Plugin getPlugin() {
        return plugin;
    }
}