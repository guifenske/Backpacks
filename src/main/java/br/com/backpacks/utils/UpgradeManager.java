package br.com.backpacks.utils;

import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class UpgradeManager {
    private static ConcurrentHashMap<Integer, Upgrade> upgrades = new ConcurrentHashMap<>();
    public static AtomicInteger lastUpgradeID = new AtomicInteger(0);
    public static void setUpgrades(ConcurrentHashMap<Integer, Upgrade> upgradesList){
        upgrades = upgradesList;
    }
    public static ConcurrentHashMap<Integer, Upgrade> getUpgrades(){
        return upgrades;
    }
    private static ConcurrentHashMap<UUID, Integer> playerUpgrade = new ConcurrentHashMap<>();
    private final List<Integer> backpackUpgrades = new ArrayList<>();
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

    //get the upgrade from all upgrades, in the backpack or not
    public static Upgrade getUpgradeFromId(int id) {
        if(upgrades.containsKey(id)) return upgrades.get(id);
        return null;
    }

    public Upgrade getUpgradeFromType(UpgradeType type){
        for(Upgrade upgrade : getBackpackUpgrades()) {
            if(upgrade.getType() == type) {
               return upgrade;
            }
        }
        return null;
    }

    public static boolean canUpgradeStack(Upgrade upgrade){
        switch (upgrade.getType()){
            case FURNACE, CRAFTING, ENCAPSULATE, UNBREAKABLE -> {
                return true;
            }
        }

        return false;
    }

    public void stopTickingAllUpgrades(){
        for(Upgrade upgrade : getBackpackUpgrades()){
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

    public static void setPlayerCurrentUpgrade(Player player, int upgradeId) {
        playerUpgrade.put(player.getUniqueId(), upgradeId);
    }

    public static Upgrade getPlayerCurrentUpgrade(HumanEntity player){
        if(!playerUpgrade.containsKey(player.getUniqueId())) return null;
        int upgradeId = playerUpgrade.get(player.getUniqueId());
        return getUpgradeFromId(upgradeId);
    }

    public static void removePlayerCurrentUpgrade(HumanEntity player){
        playerUpgrade.remove(player.getUniqueId());
    }
}
