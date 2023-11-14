package commands;

import database.dao.BuyOfferDB;
import database.dao.SellOfferDB;
import me.fjerreiro.sentineleconomy.OfferHelper;
import net.milkbowl.vault.economy.Economy;
import objects.BuyOffer;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;

import static me.fjerreiro.sentineleconomy.SentinelEconomy.getEconomy;

public class BuyOfferCmd implements CommandExecutor {

    private final BuyOfferDB buyOfferDB;
    private final SellOfferDB sellOfferDB;

    public BuyOfferCmd(BuyOfferDB buyOfferDB, SellOfferDB sellOfferDB) {
        this.buyOfferDB = buyOfferDB;
        this.sellOfferDB = sellOfferDB;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        // Check if command sender is a player.
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("[SentinelEconomy] Only players can use this command.");
            return true;
        }

        Player player = (Player) commandSender;

        // Check if player uses 3 arguments in their command.
        if (strings.length != 3) {
            OfferHelper.sendValidationMessage(player, "Usage: /buyoffer <hand/material> <quantity> <price per item>");
            return true;
        }

        // Create buyOffer object and prepare variables.
        BuyOffer buyOffer = new BuyOffer(null, player.getName(), null, null, false, 0, 0, 0, 0, null);
        ItemStack itemStackType;
        Material material;

        // Check the third argument for a valid integer and set it for price.
        double price;
        try {
            price = Double.parseDouble(strings[2]);
            buyOffer.setPrice(price);
        } catch (NumberFormatException e) {
            OfferHelper.sendValidationMessage(player, "Invalid price. Please enter a valid number.");
            return true;
        }

        // Determine the quantity of the offer.
        int qty;
        try {
            qty = Integer.parseInt(strings[1]);
            buyOffer.setTotalqty(qty);
        } catch (NumberFormatException e) {
            OfferHelper.sendValidationMessage(player, "Invalid quantity. Please enter a valid number.");
            return true;
        }

        // Check if the player has enough money in their balance to make this listing
        Economy economy = getEconomy();
        double totalMoney = (qty * price);
        if (economy.getBalance(player) < totalMoney) {
            OfferHelper.sendValidationMessage(player, "You don't have enough money to make this listing.");
            return true;
        }
        buyOffer.setMoney(totalMoney);

        // Set the material of the offer based on "hand" or material name.
            if (strings[0].equalsIgnoreCase("hand")) {
                itemStackType = player.getInventory().getItemInMainHand();
                material = itemStackType.getType();
                if (material == Material.AIR) {
                    OfferHelper.sendValidationMessage(player, "Your main hand is empty.");
                    return true;
                }
                // Check for NBT tags. To be supported in the future with custom methods.
                if (itemStackType.hasItemMeta()) {
                    OfferHelper.sendValidationMessage(player, "Listing items with NBT tags is not yet supported!");
                    return true;
                }
            } else {
                // Try to match a material by given input.
                material = Material.matchMaterial(strings[0]);
                if (material == null || material.isAir()) {
                    OfferHelper.sendValidationMessage(player, "Can't match a material with the given input.");
                    return true;
                }
            }
        buyOffer.setMaterial(String.valueOf(material));

        // Check if a SellOffer can immediately be filled with this buy offer
        String sqlQuery = "";
        if (sellOfferDB.retrieveSellOfferList(player, sqlQuery) != null) {

        }






        // Insert the buyoffer into the database
            buyOffer.setDate(String.valueOf(LocalDateTime.now()));

            if (buyOfferDB.insertBuyOffer(player, buyOffer)) {
                economy.withdrawPlayer(player, totalMoney);
                OfferHelper.sendValidationMessage(player, "You created a buy listing for " + qty + " " + material + " for $" + totalMoney + ".");
            } else {
                return false;
            }
        return true;
    }
}
