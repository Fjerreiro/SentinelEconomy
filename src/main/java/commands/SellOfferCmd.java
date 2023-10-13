package commands;

import database.Database;
import me.fjerreiro.sentineleconomy.OfferHelper;
import me.fjerreiro.sentineleconomy.SentinelEconomy;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.milkbowl.vault.economy.Economy;
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

import static java.lang.Math.round;
import static me.fjerreiro.sentineleconomy.OfferHelper.checkMaxItemInInv;

public class SellOfferCmd implements CommandExecutor {

    private final SentinelEconomy sentinelEconomyPlugin;
    SellOffer sellOffer = new SellOffer();

    public SellOfferCmd(SentinelEconomy sentinelEconomyPlugin) {
        this.sentinelEconomyPlugin = sentinelEconomyPlugin;
    }



    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("[SentinelEconomy] Only players can use this command.");
            return true;
        }

        Player player = (Player) commandSender;
        Database database = this.sentinelEconomyPlugin.getDatabase();
        sellOffer.setPlayer(player.getName());

        if (strings.length != 3) {
            OfferHelper.sendValidationMessage(player, "Usage: /selloffer <hand/material> <quantity/all> <price per item>");
            return true;
        }

        ItemStack itemStack = null;
        Material material;
        Inventory inventory = player.getInventory();

        if (strings[0].equalsIgnoreCase("hand")) {
            itemStack = player.getInventory().getItemInMainHand();
            material = itemStack.getType();
            if (material == Material.AIR) {
                OfferHelper.sendValidationMessage(player, "Your main hand is empty.");
                return true;
            }
            sellOffer.setMaterial(String.valueOf(material));
        } else {
            material = Material.matchMaterial(strings[0]);
            if (material == null || material.isAir()) {
                final TextComponent message = Component.text("[SentinelEconomy] ", NamedTextColor.DARK_AQUA).append(Component.text("Can't match a material with the given input.", NamedTextColor.WHITE));
                player.sendMessage(message);
                return true;
            }            
            sellOffer.setMaterial(String.valueOf(material));            
            
            for (ItemStack itemStackToCheck : inventory.getContents()) {
                if (itemStackToCheck != null && itemStackToCheck.getType() == material && itemStackToCheck.hasItemMeta() == false) {
                        itemStack = itemStackToCheck;
                        break;
                    }
                }
            }
            if (itemStack == null) {
                final TextComponent message = Component.text("[SentinelEconomy] ", NamedTextColor.DARK_AQUA).append(Component.text("You don't have this material in your inventory", NamedTextColor.WHITE));
                player.sendMessage(message);
                return true;
            }
            

        if (material == Material.AIR) {
            final TextComponent message = Component.text("[SentinelEconomy] ", NamedTextColor.DARK_AQUA).append(Component.text("Your main hand is empty", NamedTextColor.WHITE));
            player.sendMessage(message);
            return true;
        }

        if (itemStack.hasItemMeta()) {
            final TextComponent message = Component.text("[SentinelEconomy] ", NamedTextColor.DARK_AQUA).append(Component.text("Listing items with NBT tags is not yet supported!", NamedTextColor.WHITE));
            player.sendMessage(message);
            return true;
        }

        String quantityOrAll = strings[1];
        int qty;

        if (quantityOrAll.equalsIgnoreCase("all")) {
            qty = checkMaxItemInInv(player, itemStack);

                if (qty == 0) {
                    final TextComponent message = Component.text("[SentinelEconomy] ", NamedTextColor.DARK_AQUA).append(Component.text("You don't have this material in your inventory", NamedTextColor.WHITE));
                    player.sendMessage(message);
                    return true;
                }
        } else {
            try {
                qty = Integer.parseInt(strings[1]);
                sellOffer.setTotalqty(qty);
            } catch (NumberFormatException e) {
                final TextComponent message = Component.text("[SentinelEconomy] ", NamedTextColor.DARK_AQUA).append(Component.text("Invalid quantity. Please enter a valid number or 'all'.", NamedTextColor.WHITE));
                player.sendMessage(message);
                return true;
            }
        }

        int price;

        try {
            price = Integer.parseInt(strings[2]);
            sellOffer.setPrice(price);
        } catch (NumberFormatException e) {
            final TextComponent message = Component.text("[SentinelEconomy] ", NamedTextColor.DARK_AQUA).append(Component.text("Invalid price. Please enter a valid number.", NamedTextColor.WHITE));
            player.sendMessage(message);
            return true;
        }

        ItemStack itemStackToList = itemStack.asQuantity(qty);

        if (inventory.containsAtLeast(itemStackToList, qty)) {
            int totalPrice = price * qty;
            Economy economy = SentinelEconomy.getEconomy();
            double listingTax = SentinelEconomy.getPlugin().getConfig().getDouble("ListingTax");
            double totalTax = listingTax * totalPrice;
            totalTax = (double) round(totalTax * 10000) /10000;
            String date = String.valueOf(LocalDateTime.now());

            if (OfferHelper.checkBalanceForTax(player, economy, totalTax)) {
                sellOffer.setDate(date);
                database.immediateSale(player, sellOffer);

                if (database.addSellOfferToDB(player, sellOffer)) {
                    player.getInventory().removeItemAnySlot(itemStackToList);
                    economy.withdrawPlayer(player, totalTax);
                } else {
                    return false;
                }

                final TextComponent message = Component.text("[SentinelEconomy] ", NamedTextColor.DARK_AQUA).append(Component.text("You created a sell listing for " + qty + " " + material + " for $" + totalPrice + ".", NamedTextColor.WHITE));
                player.sendMessage(message);
                final TextComponent message2 = Component.text("[SentinelEconomy] ", NamedTextColor.DARK_AQUA).append(Component.text("You paid $" + totalTax + " to make this listing.", NamedTextColor.WHITE));
                player.sendMessage(message2);

            } else {
                final TextComponent message = Component.text("[SentinelEconomy] ", NamedTextColor.DARK_AQUA).append(Component.text("You need at least $" + totalTax + " to make this listing.", NamedTextColor.WHITE));
                player.sendMessage(message);
                return true;
            }
        } else {
            final TextComponent message = Component.text("[SentinelEconomy] ", NamedTextColor.DARK_AQUA).append(Component.text("You don't have enough of that item to make this listing.", NamedTextColor.WHITE));
            player.sendMessage(message);
            return true;
        }
        return true;
    }  
}