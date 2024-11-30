package br.com.backpacks.upgrades;

import java.util.concurrent.ConcurrentHashMap;

public class UpgradeManager {
    private static ConcurrentHashMap<Integer, Upgrade> upgrades = new ConcurrentHashMap<>();
    public static int lastUpgradeID = 0;

    public static void setUpgrades(ConcurrentHashMap<Integer, Upgrade> upgradesList){
        upgrades = upgradesList;
    }

    public static ConcurrentHashMap<Integer, Upgrade> getUpgrades(){
        return upgrades;
    }

    public static Upgrade getUpgradeFromId(int id) {
        if(upgrades.containsKey(id)) return upgrades.get(id);
        return null;
    }

    public static boolean canUpgradeStack(Upgrade upgrade){
        switch (upgrade.getType()){
            case FURNACE, CRAFTING_GRID, ENCAPSULATE, UNBREAKABLE -> {
                return true;
            }
        }

        return false;
    }
}
