package br.com.backpacks.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class UpgradeManager {
    private List<Integer> backpackUpgrade = new ArrayList<>();
    private static ConcurrentHashMap<Integer, Upgrade> upgrades = new ConcurrentHashMap<>();
    public static int lastUpgradeID = 0;

    public static void setUpgrades(ConcurrentHashMap<Integer, Upgrade> upgradesList){
        upgrades = upgradesList;
    }

    public static ConcurrentHashMap<Integer, Upgrade> getUpgrades(){
        return upgrades;
    }

    public List<Integer> getUpgradesIds() {
        return backpackUpgrade;
    }

    public List<Upgrade> getBackpackUpgrade() {
        if(this.backpackUpgrade.isEmpty()) return new ArrayList<>();
        List<Upgrade> upgrades1 = new ArrayList<>();
        for (Integer upgrade : backpackUpgrade) {
            upgrades1.add(getUpgradeFromId(upgrade));
        }
        return upgrades1;
    }

    public void setUpgradesIds(List<Integer> list){
        this.backpackUpgrade.clear();
        this.backpackUpgrade.addAll(list);
    }

    public void setBackpackUpgrade(List<Upgrade> upgradeList){
        this.backpackUpgrade.clear();
        for(Upgrade upgrade : upgradeList) {
            this.backpackUpgrade.add(upgrade.getId());
        }
    }

    public Boolean containsUpgradeType(UpgradeType upgradeType) {
        for(Upgrade upgrade1 : getBackpackUpgrade()) {
            if(upgrade1.getType() == upgradeType) {
                return true;
            }
        }
        return false;
    }

    //get the upgrade from all upgrades, in the backpack or not
    public static Upgrade getUpgradeFromId(int id) {
        if(upgrades.containsKey(id)) return upgrades.get(id);
        return null;
    }

    public List<Upgrade> getUpgradesFromType(UpgradeType type){
        List<Upgrade> upgrades1 = new ArrayList<>();
        for(Upgrade upgrade : getBackpackUpgrade()) {
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

    public void stopTickingAllUpgrades(){
        for(Upgrade upgrade : getBackpackUpgrade()){
            upgrade.stopTickingUpgrade();
        }
    }

    public void stopTickingUpgrade(int upgradeID){
        UpgradeManager.getUpgradeFromId(upgradeID).stopTickingUpgrade();
    }

    private Integer inputUpgrade = -1;
    private Integer outputUpgrade = -1;

    public Integer getInputUpgrade() {
        return inputUpgrade;
    }

    public Integer getOutputUpgrade() {
        return outputUpgrade;
    }

    public void setInputUpgrade(int inputUpgrade){
        this.inputUpgrade = inputUpgrade;
    }

    public void setOutputUpgrade(int outputUpgrade){
        this.outputUpgrade = outputUpgrade;
    }
}
