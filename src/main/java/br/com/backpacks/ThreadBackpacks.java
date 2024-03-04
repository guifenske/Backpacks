package br.com.backpacks;

import br.com.backpacks.backup.BackupHandler;
import br.com.backpacks.backup.ScheduledBackup;
import br.com.backpacks.storage.StorageManager;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;

public class ThreadBackpacks {
    public ScheduledExecutorService getExecutor() {
        return executor;
    }
    private ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    public ThreadBackpacks() throws IOException {
        String filePath = Main.getMain().getDataFolder().getCanonicalFile().getAbsolutePath() + "/config.yml";
        if (Files.exists(Paths.get(filePath))) {
            if (Main.getMain().getConfig().getInt("maxThreads") == 0) {
                return;
            }
            if(Runtime.getRuntime().availableProcessors() < Main.getMain().getConfig().getInt("maxThreads")){
                return;
            }
            executor = Executors.newScheduledThreadPool(Main.getMain().getConfig().getInt("maxThreads"));
        }
    }

    public void cancelAllTasks(){
        for(BukkitTask task : Bukkit.getScheduler().getPendingTasks()){
            if(task.getOwner().equals(Main.getMain())){
                task.cancel();
            }
        }
    }

    public void saveAll() throws IOException{
        cancelAllTasks();

        Future<Void> future = executor.submit(() -> {
            StorageManager.getProvider().saveBackpacks();
            StorageManager.getProvider().saveUpgrades();
            Main.getMain().saveConfig();
            return null;
        });

        try {
            future.get();
        } catch (Exception ignored) {

        }
        executor.shutdownNow();
        Main.saveComplete = true;
        synchronized (Main.lock){
            Main.lock.notifyAll();
        }
    }

    public void loadAll() {
        Future<Void> future = executor.submit(() -> {
            StorageManager.getProvider().loadUpgrades();
            StorageManager.getProvider().loadBackpacks();
            return null;
        });

        try {
            future.get();
        } catch (Exception ignored) {

        }

        Instant finish = Instant.now();
        Main.getMain().getLogger().info("Hello from Backpacks! " + Duration.between(Main.start, finish).toMillis() + "ms");

        if(!Main.getMain().getConfig().getBoolean("autobackup.enabled")) return;
        ScheduledBackup scheduledBackup = new ScheduledBackup();
        if(Main.getMain().getConfig().getInt("autobackup.interval") > 0){
            scheduledBackup.setInterval(Main.getMain().getConfig().getInt("autobackup.interval"));
        }   else{
            Main.getMain().getLogger().warning("Invalid interval for autobackup, please use a number greater than 0.");
            return;
        }


        if(Main.getMain().getConfig().getString("autobackup.type") != null){
            try{
                ScheduledBackup.IntervalType.valueOf(Main.getMain().getConfig().getString("autobackup.type"));
            }   catch (IllegalArgumentException e){
                Main.getMain().getLogger().warning("Invalid type for autobackup, please use MINUTES | HOURS | SECONDS.");
                return;
            }
            scheduledBackup.setType(ScheduledBackup.IntervalType.valueOf(Main.getMain().getConfig().getString("autobackup.type")));
        }   else{
            Main.getMain().getLogger().warning("Invalid type for autobackup, please use MINUTES | HOURS | SECONDS.");
        }
        int keep = 0;
        if(Main.getMain().getConfig().getInt("autobackup.keep") > 0){
            keep = Main.getMain().getConfig().getInt("autobackup.keep");
        }   else{
            Main.getMain().getLogger().warning("Invalid keep for autobackup, please use a number greater than 0.");
        }
        BackupHandler backupHandler = new BackupHandler(keep);
        Main.getMain().setBackupHandler(backupHandler);
        scheduledBackup.startWithDelay();
    }
}