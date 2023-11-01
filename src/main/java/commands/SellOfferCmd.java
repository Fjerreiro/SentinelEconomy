package commands;

import database.dao.SellOfferDB;
import me.fjerreiro.sentineleconomy.OfferHelper;
import objects.SellOffer;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;

public class SellOfferCmd implements CommandExecutor {

    private SellOfferDB sellOfferDB;

    public SellOfferCmd(SellOfferDB sellOfferDB) {
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
            OfferHelper.sendValidationMessage(player, "Usage: /selloffer <hand/material> <quantity/all> <price per item>");
            return true;
        }

        // Create sellOffer object and prepare variables.
        SellOffer sellOffer = new SellOffer(null, player.getName(), null, null, false, 0, 0, 0, 0, null);
        ItemStack itemStackType;
        Material material;
        Inventory inventory = player.getInventory();


        // Check the third argument for a valid integer and set it for price
        try {
            sellOffer.setPrice(Double.parseDouble(strings[2]));
        } catch (NumberFormatException e) {
            OfferHelper.sendValidationMessage(player, "Invalid price. Please enter a valid number.");
            return true;
        }

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
            itemStackType = new ItemStack(material);
        }
        sellOffer.setMaterial(String.valueOf(material));

        // Determine the quantity of the offer.
        if (strings[1].equalsIgnoreCase("all")) {
            int qty = OfferHelper.checkMaxItemInInv(inventory, itemStackType);
            if (qty == 0) {
                OfferHelper.sendValidationMessage(player, "You don't have this material in your inventory");
                return true;
            }
            sellOffer.setTotalqty(qty);
        } else {
            try {
                sellOffer.setTotalqty(Integer.parseInt(strings[1]));
            } catch (NumberFormatException e) {
                OfferHelper.sendValidationMessage(player, "Invalid quantity. Please enter a valid number or 'all'.");
                return true;
            }
        }

        // Create a dummy itemStack with the same material and set it with the correct quantity, so it can be used to check if the player has enough.
        int totalQty = sellOffer.getTotalqty();
        ItemStack itemStackToList = itemStackType.asQuantity(sellOffer.getTotalqty());
        double totalPrice = totalQty * sellOffer.getPrice();

        if (inventory.containsAtLeast(itemStackToList, totalQty)) {


            // Insert the selloffer into the database
            sellOffer.setDate(String.valueOf(LocalDateTime.now()));

            if (sellOfferDB.insertSellOffer(player, sellOffer)) {
                player.getInventory().removeItemAnySlot(itemStackToList);
                OfferHelper.sendValidationMessage(player, "You created a sell listing for " + totalQty + " " + material + " for $" + totalPrice + ".");
            } else {
                return false;
            }
        } else {
            OfferHelper.sendValidationMessage(player, "You don't have enough of that item to make this listing.");
            return false;
        }
        return true;
    }
}
