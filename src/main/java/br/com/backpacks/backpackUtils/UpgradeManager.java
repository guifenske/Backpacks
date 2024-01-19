package br.com.backpacks.backpackUtils;

import br.com.backpacks.Main;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UpgradeManager {
    private Set<Integer> upgrades = new HashSet<>();

    public List<Integer> getUpgradesIds() {
        if(upgrades.isEmpty())  return new ArrayList<>();
        return new ArrayList<>(upgrades);
    }

    public Set<Upgrade> getUpgrades() {
        if(this.upgrades.isEmpty()) return new HashSet<>();
        Set<Upgrade> upgradeHashSet = new HashSet<>();
        for(int i : upgrades){
            upgradeHashSet.add(getUpgradeFromId(i));
        }
        return upgradeHashSet;
    }

    public void setUpgrades(List<Integer> list){
        this.upgrades.clear();
        this.upgrades.addAll(list);
    }

    public void setUpgrades(Set<Upgrade> upgrades) {
        this.upgrades.clear();
        for(Upgrade upgrade : upgrades) {
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
