package Commands;

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

import static me.fjerreiro.sentineleconomy.OfferHelper.checkMaxItemInInv;

public class SellOfferCmd implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("[SentinelEconomy] Only players can use this command.");
            return true;
        }

        Player player = (Player) commandSender;

        if (strings.length != 3) {
            final TextComponent message = Component.text("[SentinelEconomy] ", NamedTextColor.DARK_AQUA).append(Component.text("Usage: /selloffer <material/hand> <quantity/all> <price per item>", NamedTextColor.WHITE));
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

        String quantityOrAll = strings[1];
        int qty;

        if (quantityOrAll.equalsIgnoreCase("all")) {
            qty = checkMaxItemInInv(player, material);

                if (qty == 0) {
                    final TextComponent message = Component.text("[SentinelEconomy] ", NamedTextColor.DARK_AQUA).append(Component.text("You don't have this material in your inventory", NamedTextColor.WHITE));
                    player.sendMessage(message);
                    return true;
                }
        } else {
            try {
                qty = Integer.parseInt(strings[1]);
            } catch (NumberFormatException e) {
                final TextComponent message = Component.text("[SentinelEconomy] ", NamedTextColor.DARK_AQUA).append(Component.text("Invalid quantity. Please enter a valid number or 'all'.", NamedTextColor.WHITE));
                player.sendMessage(message);
                return true;
            }
        }

        int price;

        try {
            price = Integer.parseInt(strings[2]);
        } catch (NumberFormatException e) {
            final TextComponent message = Component.text("[SentinelEconomy] ", NamedTextColor.DARK_AQUA).append(Component.text("Invalid price. Please enter a valid number.", NamedTextColor.WHITE));
            player.sendMessage(message);
            return true;
        }

        ItemStack itemStackToList = new ItemStack(material, qty);
        Economy economy = SentinelEconomy.getEconomy();

        if (player.getInventory().containsAtLeast(itemStackToList, qty)) {
            player.getInventory().removeItem(itemStackToList);
            final TextComponent message = Component.text("[SentinelEconomy] ", NamedTextColor.DARK_AQUA).append(Component.text("You listed " + qty + " " + material + " for $" + (price*qty), NamedTextColor.WHITE));
            player.sendMessage(message);
            player.sendMessage(String.valueOf(listingTax));
        } else {
            final TextComponent message = Component.text("[SentinelEconomy] ", NamedTextColor.DARK_AQUA).append(Component.text("You don't have enough of that item to make this listing.", NamedTextColor.WHITE));
            player.sendMessage(message);
            return true;
        }
        return true;
    }
}