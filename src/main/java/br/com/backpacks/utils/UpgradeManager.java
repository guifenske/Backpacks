package br.com.backpacks.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class UpgradeManager {
    private final List<Integer> backpackUpgrades = new ArrayList<>();
    private static ConcurrentHashMap<Integer, Upgrade> upgrades = new ConcurrentHashMap<>();
    public static int lastUpgradeID = 0;

    public static void setUpgrades(ConcurrentHashMap<Integer, Upgrade> upgradesList){
        upgrades = upgradesList;
    }

    public static ConcurrentHashMap<Integer, Upgrade> getUpgrades(){
        return upgrades;
    }

    public List<Integer> getUpgradesIds() {
        return backpackUpgrades;
    }

    public List<Upgrade> getBackpackUpgrades() {
        if(this.backpackUpgrades.isEmpty()) return new ArrayList<>();
        List<Upgrade> upgrades1 = new ArrayList<>();
        for (Integer upgrade : backpackUpgrades) {
            upgrades1.add(getUpgradeFromId(upgrade));
        }
        return upgrades1;
    }

    public void setUpgradesIds(List<Integer> list){
        this.backpackUpgrades.clear();
        this.backpackUpgrades.addAll(list);
    }

    public void setBackpackUpgrades(List<Upgrade> upgradeList){
        this.backpackUpgrades.clear();
        for(Upgrade upgrade : upgradeList) {
            this.backpackUpgrades.add(upgrade.getId());
        }
    }

    public Boolean containsUpgradeType(UpgradeType upgradeType) {
        for(Upgrade upgrade1 : getBackpackUpgrades()) {
            if(upgrade1.getType() == upgradeType) {
                return true;
            }
        }
        return false;
    }

    public static Upgrade getUpgradeFromId(int id) {
        if(upgrades.containsKey(id)) return upgrades.get(id);
        return null;
    }

    public Upgrade getFirstUpgradeFromType(UpgradeType type){
        for(Upgrade upgrade : getBackpackUpgrades()) {
            if(upgrade.getType() == type) {
               return upgrade;
            }
        }
        return null;
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
        for(Upgrade upgrade : getBackpackUpgrades()){
            upgrade.stopTicking();
        }
    }

    public void stopTickingUpgrade(int upgradeID){
        UpgradeManager.getUpgradeFromId(upgradeID).stopTicking();
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
