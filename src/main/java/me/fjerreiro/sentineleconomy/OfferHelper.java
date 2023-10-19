package me.fjerreiro.sentineleconomy;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class OfferHelper {
    public static int checkMaxItemInInv(@NotNull Inventory inventory, ItemStack itemStackToCheck) {
        int totalAmount = 0;

        for (ItemStack itemStack : inventory.getContents()) {
            if (itemStack != null && itemStack.isSimilar(itemStackToCheck)) {
                totalAmount += itemStack.getAmount();
            }
        }
        return totalAmount;
    }

    public static void sendValidationMessage(@NotNull Player player, String string) {
        final TextComponent message = Component.text("[SentinelEconomy] ", NamedTextColor.DARK_AQUA).append(Component.text(string, NamedTextColor.WHITE));
        player.sendMessage(message);
    }
}