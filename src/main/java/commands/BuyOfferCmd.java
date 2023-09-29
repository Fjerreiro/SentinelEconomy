package commands;

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
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;

public class BuyOfferCmd implements CommandExecutor {

    private final SentinelEconomy sentinelEconomyPlugin;

    public BuyOfferCmd(SentinelEconomy sentinelEconomyPlugin) {
        this.sentinelEconomyPlugin = sentinelEconomyPlugin;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("[SentinelEconomy] Only players can use this command.");
            return true;
        }

        Player player = (Player) commandSender;

        if (strings.length != 3) {
            final TextComponent message = Component.text("[SentinelEconomy] ", NamedTextColor.DARK_AQUA).append(Component.text("Usage: /buyoffer <material/hand> <quantity> <price per item>", NamedTextColor.WHITE));
            player.sendMessage(message);
            return true;
        }

        String materialOrHand = strings[0];
        Material material;

        if (materialOrHand.equalsIgnoreCase("hand")) {
            ItemStack mainHandItemStack = player.getInventory().getItemInMainHand();
            material = mainHandItemStack.getType();

            if (material == Material.AIR) {
                final TextComponent message = Component.text("[SentinelEconomy] ", NamedTextColor.DARK_AQUA).append(Component.text("Your main hand is empty", NamedTextColor.WHITE));
                player.sendMessage(message);
                return true;
            }
        } else {
            material = Material.matchMaterial(materialOrHand);

            if (material == null) {
                final TextComponent message = Component.text("[SentinelEconomy] ", NamedTextColor.DARK_AQUA).append(Component.text("Invalid material name", NamedTextColor.WHITE));
                player.sendMessage(message);
                return true;
            }
        }

        int qty;

        try {
            qty = Integer.parseInt(strings[1]);
            } catch (NumberFormatException e) {
                final TextComponent message = Component.text("[SentinelEconomy] ", NamedTextColor.DARK_AQUA).append(Component.text("Invalid quantity. Please enter a valid number.", NamedTextColor.WHITE));
                player.sendMessage(message);
                return true;
        }

        int price;

        try {
            price = Integer.parseInt(strings[2]);
        } catch (NumberFormatException e) {
            final TextComponent message = Component.text("[SentinelEconomy] ", NamedTextColor.DARK_AQUA).append(Component.text("Invalid price. Please enter a valid number.", NamedTextColor.WHITE));
            player.sendMessage(message);
            return true;
        }

        int totalPrice = price * qty;
        Economy economy = SentinelEconomy.getEconomy();
        double listingTax = SentinelEconomy.getPlugin().getConfig().getDouble("ListingTax");
        double totalTax = listingTax * totalPrice;
        double totalMoney = totalPrice + totalTax;
        String date = String.valueOf(LocalDateTime.now());

            if (economy.getBalance(player) >= totalMoney) {
                economy.withdrawPlayer(player, totalMoney);
                this.sentinelEconomyPlugin.getDatabase().addBuyOfferToDB(player, material, price, date, qty);

                final TextComponent message = Component.text("[SentinelEconomy] ", NamedTextColor.DARK_AQUA).append(Component.text("You created a buy listing for " + qty + " " + material + " for $" + totalPrice + ".", NamedTextColor.WHITE));
                player.sendMessage(message);
                final TextComponent message2 = Component.text("[SentinelEconomy] ", NamedTextColor.DARK_AQUA).append(Component.text("You paid $" + totalMoney + " to make this listing.", NamedTextColor.WHITE));
                player.sendMessage(message2);

            } else {
                final TextComponent message = Component.text("[SentinelEconomy] ", NamedTextColor.DARK_AQUA).append(Component.text("You need at least $" + (totalMoney) + " to make this listing.", NamedTextColor.WHITE));
                player.sendMessage(message);
                return true;
            }
        return true;
    }
}