package br.com.backpacks.upgrades;

import org.bukkit.inventory.ItemStack;

public class GetFurnaceUpgradeUtils{
    private ItemStack fuel;

    private ItemStack result;

    public ItemStack getResult() {
        return result;
    }

    public void setResult(ItemStack result) {
        this.result = result;
    }

    public ItemStack getFuel() {
        return fuel;
    }

    public void setFuel(ItemStack fuel) {
        this.fuel = fuel;
    }

    private ItemStack smelting;

    public ItemStack getSmelting() {
        return smelting;
    }

    public void setSmelting(ItemStack smelting) {
        this.smelting = smelting;
    }

}
