package br.com.backpacks;

import br.com.backpacks.backup.BackupHandler;
import br.com.backpacks.backup.ScheduledBackup;
import br.com.backpacks.commands.*;
import br.com.backpacks.events.ConfigItemsEvents;
import br.com.backpacks.events.HopperEvents;
import br.com.backpacks.events.ServerLoadEvent;
import br.com.backpacks.events.backpacks.*;
import br.com.backpacks.events.inventory.*;
import br.com.backpacks.events.player.*;
import br.com.backpacks.events.upgrades.*;
import br.com.backpacks.yaml.YamlUtils;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;

public class ThreadBackpacks {
    public ScheduledExecutorService getExecutor() {
        return executor;
    }
    private ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    public ThreadBackpacks() throws IOException {
        File file = new File(Main.getMain().getDataFolder().getCanonicalFile().getAbsolutePath() + "/config.yml");
        if(file.exists()){
            if(Main.getMain().getConfig().getInt("maxThreads") == 0){
                return;
            }
            executor = Executors.newScheduledThreadPool(Main.getMain().getConfig().getInt("maxThreads"));
        }
    }

    public void registerAll() {
        executor.submit(() -> {
            //player
            Bukkit.getPluginManager().registerEvents(new PlayerDeathEvent(), Main.getMain());
            Bukkit.getPluginManager().registerEvents(new CraftBackpack(), Main.getMain());
            Bukkit.getPluginManager().registerEvents(new Fishing(), Main.getMain());
            Bukkit.getPluginManager().registerEvents(new FinishedSmelting(), Main.getMain());
            Bukkit.getPluginManager().registerEvents(new InteractOtherPlayerBackpack(), Main.getMain());
            Bukkit.getPluginManager().registerEvents(new AnvilRenameBackpack(), Main.getMain());

            //backpack
            Bukkit.getPluginManager().registerEvents(new BackpackInteract(), Main.getMain());
            Bukkit.getPluginManager().registerEvents(new BackpackBreak(), Main.getMain());
            Bukkit.getPluginManager().registerEvents(new BackpackPlace(), Main.getMain());
            Bukkit.getPluginManager().registerEvents(new OnClickBackpack(), Main.getMain());
            Bukkit.getPluginManager().registerEvents(new OnClickInConfigMenu(), Main.getMain());
            Bukkit.getPluginManager().registerEvents(new OnCloseBackpackConfigMenu(), Main.getMain());
            Bukkit.getPluginManager().registerEvents(new RenameBackpackChat(), Main.getMain());
            Bukkit.getPluginManager().registerEvents(new OpenBackpackOfTheBack(), Main.getMain());
            Bukkit.getPluginManager().registerEvents(new OnCloseBackpack(), Main.getMain());
            Bukkit.getPluginManager().registerEvents(new OnCloseUpgradeMenu(), Main.getMain());
            Bukkit.getPluginManager().registerEvents(new OnClickUpgradesMenu(), Main.getMain());
            Bukkit.getPluginManager().registerEvents(new IOMenu(), Main.getMain());

            //others
            Bukkit.getPluginManager().registerEvents(new HopperEvents(), Main.getMain());
            Bukkit.getPluginManager().registerEvents(new ConfigItemsEvents(), Main.getMain());
            Bukkit.getPluginManager().registerEvents(new ServerLoadEvent(), Main.getMain());
            Bukkit.getPluginManager().registerEvents(new BpList(), Main.getMain());

            //Upgrades
            Bukkit.getPluginManager().registerEvents(new CraftingTable(), Main.getMain());
            Bukkit.getPluginManager().registerEvents(new Furnace(), Main.getMain());
            Bukkit.getPluginManager().registerEvents(new Jukebox(), Main.getMain());
            Bukkit.getPluginManager().registerEvents(new AutoFeed(), Main.getMain());
            Bukkit.getPluginManager().registerEvents(new VillagersFollow(), Main.getMain());
            Bukkit.getPluginManager().registerEvents(new Collector(), Main.getMain());
            Bukkit.getPluginManager().registerEvents(new Tanks(), Main.getMain());

            Main.getMain().getCommand("bbug").setExecutor(new BpDebug());
            Main.getMain().getCommand("bpgive").setExecutor(new BpGive());
            Main.getMain().getCommand("bpgiveid").setExecutor(new BpGiveID());
            Main.getMain().getCommand("bplist").setExecutor(new BpList());
            Main.getMain().getCommand("bpbackup").setExecutor(new BpBackup());
            Main.getMain().getCommand("bpreload").setExecutor(new BpReload());
            return null;
        });
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
            YamlUtils.saveBackpacks(Main.getMain().getDataFolder().getAbsolutePath() + "/backpacks.yml");
            YamlUtils.saveUpgrades(Main.getMain().getDataFolder().getAbsolutePath() + "/upgrades.yml");
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
            YamlUtils.loadUpgrades();
            YamlUtils.loadBackpacks();
            return null;
        });

        try {
            future.get();
        } catch (Exception ignored) {

        }

        if(!Main.getMain().getConfig().getBoolean("autobackup.enabled")) return;
        ScheduledBackup scheduledBackup = new ScheduledBackup();
        if(Main.getMain().getConfig().getInt("autobackup.interval") > 0){
            scheduledBackup.setInterval(Main.getMain().getConfig().getInt("autobackup.interval"));
        }   else{
            Main.getMain().getLogger().warning("Invalid interval for autobackup, please use a number greater than 0.");
            return;
        }

        if(Main.getMain().getConfig().isSet("autobackup.path")){
            if(Main.getMain().getConfig().getString("autobackup.path") != null){
                String path = Main.getMain().getConfig().getString("autobackup.path");
                if(path.equalsIgnoreCase("DEFAULT")){
                    path = Main.getMain().getDataFolder().getAbsolutePath() + "/Backups";
                }
                try{
                    Path.of(path);
                }   catch (Exception e){
                    path = Main.getMain().getDataFolder().getAbsolutePath() + "/Backups";
                    Main.getMain().getLogger().warning("Invalid path for autobackup, please use this syntax: /path/to/backup/folder");
                    Main.getMain().getLogger().info("Using default backups folder.");
                }
                scheduledBackup.setPath(path);
            }   else{
                Main.getMain().getLogger().warning("Invalid path for autobackup, please use this syntax: /path/to/backup/folder");
                return;
            }
        }   else{
            scheduledBackup.setPath(Main.getMain().getDataFolder().getAbsolutePath() + "/Backups");
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
        BackupHandler backupHandler = new BackupHandler(keep, scheduledBackup.getPath());
        Main.getMain().setBackupHandler(backupHandler);
        scheduledBackup.startWithDelay();
    }
}