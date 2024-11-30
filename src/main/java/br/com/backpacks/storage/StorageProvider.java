package br.com.backpacks.storage;

import br.com.backpacks.Main;
import br.com.backpacks.upgrades.Upgrade;
import br.com.backpacks.upgrades.UpgradeManager;
import br.com.backpacks.backpack.Backpack;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

public class StorageProvider {
    private final StorageProviderType type;

    public StorageProvider(StorageProviderType type) {
        this.type = type;
    }

    public StorageProviderType getType() {
        return type;
    }

    public void loadBackpacks(){

    }

    public void loadUpgrades(){

    }

    public void saveBackpacks() throws IOException {

    }

    public void saveUpgrades() throws IOException {

    }

    public void loadBackpacks(ConcurrentHashMap<Integer, Backpack> hashMap){
        Main.backpackManager.setLastBackpackID(0);
        Main.backpackManager.setBackpacks(hashMap);
        Main.backpackManager.getBackpacksPlacedLocations().clear();

        if(hashMap.isEmpty()) return;

        for(Backpack backpack : hashMap.values()){

            if(backpack.getLocation() != null){
                Main.backpackManager.getBackpacksPlacedLocations().put(backpack.getLocation(), backpack.getId());
            }

            int id = backpack.getId();
            if(Main.backpackManager.getLastBackpackID() == 0) Main.backpackManager.setLastBackpackID(id);

            if(Main.backpackManager.getLastBackpackID() < id){
                Main.backpackManager.setLastBackpackID(id);
            }
        }
    }

    public void loadUpgrades(ConcurrentHashMap<Integer, Upgrade> hashMap){
        UpgradeManager.lastUpgradeID = 0;
        UpgradeManager.setUpgrades(hashMap);

        if(hashMap.isEmpty()) return;

        for(Integer id : hashMap.keySet()){
            if(UpgradeManager.lastUpgradeID == 0) UpgradeManager.lastUpgradeID = id;

            if(UpgradeManager.lastUpgradeID < id){
                UpgradeManager.lastUpgradeID = id;
            }
        }
    }
}
