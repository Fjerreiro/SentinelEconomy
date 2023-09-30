package commands;

import me.fjerreiro.sentineleconomy.OfferHelper;
import me.fjerreiro.sentineleconomy.SentinelEconomy;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;

import static me.fjerreiro.sentineleconomy.OfferHelper.checkMaxItemInInv;

public class SellOfferCmd implements CommandExecutor {

    private final SentinelEconomy sentinelEconomyPlugin;

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

        if (strings.length != 2) {
            final TextComponent message = Component.text("[SentinelEconomy] ", NamedTextColor.DARK_AQUA).append(Component.text("Usage: /selloffer <quantity/all> <price per item>", NamedTextColor.WHITE));
            player.sendMessage(message);
            return true;
        }



        ItemStack itemStackInHand = player.getInventory().getItemInMainHand();
        Material materialInHand = itemStackInHand.getType();

        if (materialInHand == Material.AIR) {
            final TextComponent message = Component.text("[SentinelEconomy] ", NamedTextColor.DARK_AQUA).append(Component.text("Your main hand is empty", NamedTextColor.WHITE));
            player.sendMessage(message);
            return true;
        }

        if (itemStackInHand.hasItemMeta()) {
            final TextComponent message = Component.text("[SentinelEconomy] ", NamedTextColor.DARK_AQUA).append(Component.text("Listing items with NBT tags is not yet supported!", NamedTextColor.WHITE));
            player.sendMessage(message);
            return true;
        }

        String quantityOrAll = strings[0];
        int qty;

        if (quantityOrAll.equalsIgnoreCase("all")) {
            qty = checkMaxItemInInv(player, itemStackInHand);

                if (qty == 0) {
                    final TextComponent message = Component.text("[SentinelEconomy] ", NamedTextColor.DARK_AQUA).append(Component.text("You don't have this material in your inventory", NamedTextColor.WHITE));
                    player.sendMessage(message);
                    return true;
                }
        } else {
            try {
                qty = Integer.parseInt(strings[0]);
            } catch (NumberFormatException e) {
                final TextComponent message = Component.text("[SentinelEconomy] ", NamedTextColor.DARK_AQUA).append(Component.text("Invalid quantity. Please enter a valid number or 'all'.", NamedTextColor.WHITE));
                player.sendMessage(message);
                return true;
            }
        }

        int price;

        try {
            price = Integer.parseInt(strings[1]);
        } catch (NumberFormatException e) {
            final TextComponent message = Component.text("[SentinelEconomy] ", NamedTextColor.DARK_AQUA).append(Component.text("Invalid price. Please enter a valid number.", NamedTextColor.WHITE));
            player.sendMessage(message);
            return true;
        }

        ItemStack itemStackToList = itemStackInHand.asQuantity(qty);

        if (player.getInventory().containsAtLeast(itemStackToList, qty)) {
            int totalPrice = price * qty;
            Economy economy = SentinelEconomy.getEconomy();
            double listingTax = SentinelEconomy.getPlugin().getConfig().getDouble("ListingTax");
            double totalTax = listingTax * totalPrice;
            String date = String.valueOf(LocalDateTime.now());

            if (OfferHelper.checkBalanceForTax(player, economy, totalTax)) {
                player.getInventory().removeItem(itemStackToList);
                economy.withdrawPlayer(player, totalTax);
                this.sentinelEconomyPlugin.getDatabase().addSellOfferToDB(player, materialInHand, price, date, qty);

                final TextComponent message = Component.text("[SentinelEconomy] ", NamedTextColor.DARK_AQUA).append(Component.text("You created a sell listing for " + qty + " " + materialInHand + " for $" + totalPrice + ".", NamedTextColor.WHITE));
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