package br.com.backpacks;

import br.com.backpacks.backup.BackupHandler;
import br.com.backpacks.storage.StorageManager;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

public class ThreadBackpacks {
    public static void cancelAllTasks(){
        for(BukkitTask task : Bukkit.getScheduler().getPendingTasks()){
            if(task.getOwner().equals(Main.getMain())){
                task.cancel();
            }
        }
    }

    public static void saveAll() throws IOException{
        Bukkit.getScheduler().runTaskAsynchronously(Main.getMain(), () -> {
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
        });
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
}