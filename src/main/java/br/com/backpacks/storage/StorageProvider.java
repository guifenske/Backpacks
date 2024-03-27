package br.com.backpacks.storage;

import br.com.backpacks.Main;
import br.com.backpacks.utils.Upgrade;
import br.com.backpacks.utils.UpgradeManager;
import br.com.backpacks.utils.backpacks.BackPack;
import br.com.backpacks.utils.inventory.InventoryBuilder;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

public class StorageProvider {
    private final StorageManager.StorageProviderType type;

    public StorageProvider(StorageManager.StorageProviderType type) {
        this.type = type;
    }

    public StorageManager.StorageProviderType getType() {
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

    public void loadBackpacks(ConcurrentHashMap<Integer, BackPack> hashMap){
        Main.backPackManager.setLastBackpackID(0);
        Main.backPackManager.setBackpacks(hashMap);
        Main.backPackManager.getBackpacksPlacedLocations().clear();
        if(hashMap.isEmpty()) return;
        for(BackPack backPack : hashMap.values()){
            if(backPack.getLocation() != null){
                Main.backPackManager.getBackpacksPlacedLocations().put(backPack.getLocation(), backPack.getId());
            }

            InventoryBuilder.deleteAllMenusFromBackpack(backPack);
            new InventoryBuilder(InventoryBuilder.MenuType.CONFIG, backPack).build();
            new InventoryBuilder(InventoryBuilder.MenuType.UPGMENU, backPack).build();
            new InventoryBuilder(InventoryBuilder.MenuType.EDIT_IO_MENU, backPack).build();

            int id = backPack.getId();
            if(Main.backPackManager.getLastBackpackID() == 0) Main.backPackManager.setLastBackpackID(id);
            if(Main.backPackManager.getLastBackpackID() < id){
                Main.backPackManager.setLastBackpackID(id);
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
