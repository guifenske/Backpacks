package br.com.backpacks.upgrades;

import br.com.backpacks.utils.Upgrade;
import br.com.backpacks.utils.UpgradeType;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

public final class ExpStorageUpgrade extends Upgrade {

    private float totalExp = 0.0F;
    private final Inventory inventory;

    public ExpStorageUpgrade(int id) {
        super(UpgradeType.EXP_STORAGE, id);
        inventory = Bukkit.createInventory(null, 54, Component.text("Exp Storage Upgrade"));
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    public float getTotalExp() {
        return totalExp;
    }

    public void setTotalExp(float totalExp) {
        this.totalExp = totalExp;
    }
}
