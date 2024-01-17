package br.com.backpacks.backpackUtils;

import br.com.backpacks.Main;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class UpgradeManager {
    private ConcurrentHashMap<Integer, Upgrade> upgrades = new ConcurrentHashMap<>();

    public List<Upgrade> getUpgrades() {
        if(this.upgrades.isEmpty()) return new ArrayList<>();
        return new ArrayList<>(upgrades.values());
    }

    public void setUpgrades(List<Upgrade> upgrades) {
        this.upgrades.clear();
        for(Upgrade upgrade : upgrades) {
            this.upgrades.put(upgrade.getId(), upgrade);
        }
    }

    public Boolean containsUpgradeType(UpgradeType upgradeType) {
        for(Upgrade upgrade1 : upgrades.values()) {
            if(upgrade1.getType() == upgradeType) {
                return true;
            }
        }
        return false;
    }

    //get the upgrade from all upgrades, in the backpack or not
    public Upgrade getUpgradeFromId(int id) {
        if(Main.backPackManager.getUpgradeHashMap().containsKey(id)) return Main.backPackManager.getUpgradeHashMap().get(id);
        return null;
    }

    public List<Upgrade> getUpgradesFromType(UpgradeType type){
        List<Upgrade> upgrades1 = new ArrayList<>();
        for(Upgrade upgrade : getUpgrades()) {
            if(upgrade.getType() == type) {
               upgrades1.add(upgrade);
            }
        }
        return upgrades1;
    }

    public static boolean canUpgradeStack(Upgrade upgrade){
        switch (upgrade.getType()){
            case FURNACE, CRAFTING -> {
                return true;
            }
        }

        return false;
    }
}
