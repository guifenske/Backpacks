package br.com.backpacks.upgrades;

import org.bukkit.inventory.ItemStack;

public interface GetFurnace {

    ItemStack getResult();

    void setResult(ItemStack result);

    ItemStack getFuel();

    void setFuel(ItemStack fuel);

    ItemStack getSmelting();

    void setSmelting(ItemStack smelting);
}
