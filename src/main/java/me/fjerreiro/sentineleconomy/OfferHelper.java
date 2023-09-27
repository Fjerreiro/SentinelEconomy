package me.fjerreiro.sentineleconomy;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class OfferHelper {
    public int checkMaxItemInInv(@NotNull Player player, Material materialToCheck) {
        int totalAmount = 0;

        for (ItemStack itemStack : player.getInventory().getContents()) {
            if (itemStack != null && itemStack.getType() == materialToCheck) {
                totalAmount += itemStack.getAmount();
            }
        }
        return totalAmount;
    }
}