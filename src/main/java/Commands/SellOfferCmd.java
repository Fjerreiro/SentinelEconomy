package Commands;

import me.fjerreiro.sentineleconomy.OfferHelper;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class SellOfferCmd implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("[SentinelEconomy] Only players can use this command.");
            return true;
        }

        Player player = (Player) commandSender;

        if (strings.length != 3) {
            player.sendMessage("[SentinelEconomy] Usage: /selloffer <material/hand> <quantity/all> <price per item>");
            return true;
        }

        String materialOrHand = strings[0];
        String quantityOrAll = strings[1];
        Material material;
        int qty;
        int price;

        if (materialOrHand.equalsIgnoreCase("hand")) {
            ItemStack mainHandItemStack = player.getInventory().getItemInMainHand();
            material = mainHandItemStack.getType();

            if (material == Material.AIR) {
                player.sendMessage("[SentinelEconomy] Your main hand is empty");
                return true;
            }
        } else {
            material = Material.matchMaterial(materialOrHand);

            if (material == null) {
                player.sendMessage("[SentinelEconomy] Invalid material name");
                return true;
            }
        }

        if (quantityOrAll.equalsIgnoreCase("all")) {
            OfferHelper offerHelper = new OfferHelper();
            qty = offerHelper.checkMaxItemInInv(player, material);

                if (qty == 0) {
                    player.sendMessage("[SentinelEconomy] You don't have this material in your inventory");
                    return true;
                }
        } else {
            try {
                qty = Integer.parseInt(strings[1]);
            } catch (NumberFormatException e) {
                player.sendMessage("[SentinelEconomy] Invalid quantity. Please enter a valid number or 'all'.");
                return true;
            }
        }

        try {
            price = Integer.parseInt(strings[2]);
        } catch (NumberFormatException e) {
            player.sendMessage("[SentinelEconomy] Invalid price. Please enter a valid number.");
            return true;
        }

        ItemStack itemStackToList = new ItemStack(material, qty);

        if (player.getInventory().containsAtLeast(itemStackToList, qty)) {
            player.getInventory().removeItem(itemStackToList);
            player.sendMessage("[SentinelEconomy] You listed " + qty + " " + material + " for $" + (price*qty));
        } else {
            player.sendMessage("[SentinelEconomy] You don't have enough of that item to make this listing.");
            return true;
        }
        return true;
    }
}