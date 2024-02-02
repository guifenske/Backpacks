package br.com.backpacks.backpackUtils;

import br.com.backpacks.Main;

import java.util.ArrayList;
import java.util.List;

public class UpgradeManager {
    private List<Integer> upgrades = new ArrayList<>();

    public List<Integer> getUpgradesIds() {
        if(upgrades.isEmpty())  return new ArrayList<>();
        return new ArrayList<>(upgrades);
    }

    public List<Upgrade> getUpgrades() {
        if(this.upgrades.isEmpty()) return new ArrayList<>();
        List<Upgrade> upgrades1 = new ArrayList<>();
        for (Integer upgrade : upgrades) {
            upgrades1.add(getUpgradeFromId(upgrade));
        }
        return upgrades1;
    }

    public void setUpgradesIds(List<Integer> list){
        this.upgrades.clear();
        this.upgrades.addAll(list);
    }

    public void setUpgrades(List<Upgrade> upgradeList){
        this.upgrades.clear();
        for(Upgrade upgrade : upgradeList) {
            this.upgrades.add(upgrade.getId());
        }
    }

    public Boolean containsUpgradeType(UpgradeType upgradeType) {
        for(Upgrade upgrade1 : getUpgrades()) {
            if(upgrade1.getType() == upgradeType) {
                return true;
            }
        }
        return false;
    }

    //get the upgrade from all upgrades, in the backpack or not
    public static Upgrade getUpgradeFromId(int id) {
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
            case FURNACE, CRAFTING, ENCAPSULATE, SMOKER, BLAST_FURNACE, UNBREAKABLE -> {
                return true;
            }
        }

        return false;
    }
}
