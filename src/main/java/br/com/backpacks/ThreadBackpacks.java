package br.com.backpacks;

import br.com.backpacks.backup.BackupHandler;
import br.com.backpacks.events.upgrades.Furnace;
import br.com.backpacks.events.upgrades.Magnet;
import br.com.backpacks.events.upgrades.VillagerBait;
import br.com.backpacks.storage.StorageManager;
import br.com.backpacks.upgrades.FurnaceUpgrade;
import br.com.backpacks.utils.Upgrade;
import br.com.backpacks.utils.UpgradeManager;
import br.com.backpacks.utils.UpgradeType;
import br.com.backpacks.utils.backpacks.BackPack;
import br.com.backpacks.utils.backpacks.BackPackManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.Recipe;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ThreadBackpacks {

    public static void saveAll() throws IOException{
        if(StorageManager.getProvider() == null || (Main.backPackManager.getBackpacks().isEmpty() && UpgradeManager.getUpgrades().isEmpty())){
            Main.saveComplete = true;
            synchronized (Main.lock){
                Main.lock.notifyAll();
            }

            return;
        }

        try {
            StorageManager.getProvider().saveBackpacks();
            StorageManager.getProvider().saveUpgrades();
            Main.getMain().saveConfig();
            Main.saveComplete = true;
            synchronized (Main.lock){
                Main.lock.notifyAll();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadAll() {
        StorageManager.getProvider().loadUpgrades();
        StorageManager.getProvider().loadBackpacks();

        BackupHandler backupHandler = Config.getBackupHandler();

        if(backupHandler != null){
            backupHandler.getScheduledBackupService().start();
        }

        Main.getMain().setBackupHandler(backupHandler);

        AutoSaveManager autoSaveManager = Config.getAutoSaveManager();
        if(autoSaveManager != null){
            autoSaveManager.start();
        }

        Main.getMain().setAutoSaveManager(autoSaveManager);

        for(BackPack backPack : Main.backPackManager.getBackpacks().values()){
            if(backPack.isShowingNameAbove()){
                for(Entity entity : backPack.getLocation().getChunk().getEntities()){
                    if(entity instanceof ArmorStand){
                        if(entity.getLocation().subtract(0, 1, 0).getBlock().getLocation().equals(backPack.getLocation())){
                            backPack.setMarker(entity.getUniqueId());
                            break;
                        }
                    }
                }
            }
        }

        Instant finish = Instant.now();

        Main.getMain().getLogger().info("Hello from Backpacks! " + Duration.between(Main.start, finish).toMillis() + "ms");
        ThreadBackpacks.startTicking();
    }

    public static void startTicking(){
        Bukkit.getScheduler().runTaskTimerAsynchronously(Main.getMain(), ()->{
            for(BackPack backPack : Main.backPackManager.getBackpacks().values()){
                if(backPack.getOwner() != null){
                    VillagerBait.tick(Bukkit.getPlayer(backPack.getOwner()));
                    Magnet.tick(Bukkit.getPlayer(backPack.getOwner()));
                }   else if(backPack.getLocation() != null){
                    Magnet.tick(backPack);
                }
            }
        }, 0L, 10L);
    }
}