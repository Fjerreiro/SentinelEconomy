package me.fjerreiro.sentineleconomy;

import net.md_5.bungee.api.chat.hover.content.Item;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class OfferHelper {
    public static int checkMaxItemInInv(@NotNull Player player, ItemStack itemStackToCheck) {
        int totalAmount = 0;

        for (ItemStack itemStack : player.getInventory().getContents()) {
            if (itemStack != null && itemStack.isSimilar(itemStackToCheck)) {
                totalAmount += itemStack.getAmount();
            }
        }
        return totalAmount;
    }

    public static int calculateEmptySlots(@NotNull Inventory inventory) {
        int emptySlots = 0;

        for (ItemStack itemStack : inventory.getContents()) {
            if (itemStack.getType().isAir()) {
                emptySlots += 1;
            }
        }
        return emptySlots;
    }

    public static boolean checkBalanceForTax(Player player, Economy economy, double listingTax) {
        return economy.getBalance(player) > listingTax;
    }
}