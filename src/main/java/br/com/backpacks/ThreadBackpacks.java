package br.com.backpacks;

import br.com.backpacks.backup.BackupHandler;
import br.com.backpacks.events.upgrades.Magnet;
import br.com.backpacks.events.upgrades.VillagersFollow;
import br.com.backpacks.storage.StorageManager;
import br.com.backpacks.utils.backpacks.BackPack;
import org.bukkit.Bukkit;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

public class ThreadBackpacks {

    public static void saveAll() throws IOException{
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

        Instant finish = Instant.now();
        Main.getMain().getLogger().info("Hello from Backpacks! " + Duration.between(Main.start, finish).toMillis() + "ms");
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
    }

    public static void startTicking(){
        Bukkit.getScheduler().runTaskTimerAsynchronously(Main.getMain(), ()->{
            for(BackPack backPack : Main.backPackManager.getBackpacks().values()){
                if(backPack.getOwner() != null){
                    VillagersFollow.tick(Bukkit.getPlayer(backPack.getOwner()));
                    Magnet.tick(Bukkit.getPlayer(backPack.getOwner()));
                }   else if(backPack.getLocation() != null){
                    Magnet.tick(backPack);
                }
            }
        }, 0L, 10L);
    }
}