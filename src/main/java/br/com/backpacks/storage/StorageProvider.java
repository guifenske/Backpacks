package br.com.backpacks.storage;

import br.com.backpacks.Main;
import br.com.backpacks.utils.Upgrade;
import br.com.backpacks.utils.UpgradeManager;
import br.com.backpacks.utils.backpacks.BackPack;
import br.com.backpacks.utils.backpacks.BackpackManager;
import br.com.backpacks.utils.inventory.InventoryBuilder;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.CreatureSpawnEvent;

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
        BackpackManager.lastBackpackID = 0;
        BackpackManager.setBackpacks(hashMap);
        BackpackManager.getBackpacksPlacedLocations().clear();
        if(hashMap.isEmpty()) return;
        for(BackPack backPack : hashMap.values()){
            if(backPack.getLocation() != null){
                BackpackManager.getBackpacksPlacedLocations().put(backPack.getLocation(), backPack.getId());
                Bukkit.getScheduler().runTask(Main.getMain(), ()->{
                    backPack.getLocation().getBlock().setType(Material.BARREL);
                    backPack.updateBarrelBlock();
                    if(backPack.isShowingNameAbove()){
                        ArmorStand marker = (ArmorStand) backPack.getLocation().getWorld().spawnEntity(backPack.getLocation().clone().add(0, 1, 0).toCenterLocation(), EntityType.ARMOR_STAND, CreatureSpawnEvent.SpawnReason.CUSTOM);
                        marker.setVisible(false);
                        marker.setSmall(true);
                        marker.customName(Component.text(backPack.getName()));
                        marker.setCustomNameVisible(true);
                        marker.setCanTick(false);
                        marker.setCanMove(false);
                        marker.setCollidable(false);
                        marker.setInvulnerable(true);
                        marker.setBasePlate(false);
                        marker.setMarker(true);
                        backPack.setMarkerId(marker.getUniqueId());
                    }
                });
            }

            InventoryBuilder.deleteAllMenusFromBackpack(backPack);
            new InventoryBuilder(InventoryBuilder.MenuType.CONFIG, backPack).build();
            new InventoryBuilder(InventoryBuilder.MenuType.UPGMENU, backPack).build();
            new InventoryBuilder(InventoryBuilder.MenuType.EDIT_IO_MENU, backPack).build();

            int id = backPack.getId();
            if(BackpackManager.lastBackpackID == 0) BackpackManager.lastBackpackID = id;
            if(BackpackManager.lastBackpackID < id){
                BackpackManager.lastBackpackID = id;
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
