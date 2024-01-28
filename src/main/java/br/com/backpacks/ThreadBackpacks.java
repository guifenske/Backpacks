package br.com.backpacks;

import br.com.backpacks.advancements.BackpacksAdvancements;
import br.com.backpacks.advancements.NamespacesAdvacements;
import br.com.backpacks.backupHandler.BackupHandler;
import br.com.backpacks.backupHandler.ScheduledBackup;
import br.com.backpacks.commands.Bdebug;
import br.com.backpacks.commands.Bpgive;
import br.com.backpacks.commands.BpgiveID;
import br.com.backpacks.commands.Bplist;
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
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;

public class ThreadBackpacks {
    public ScheduledExecutorService getExecutor() {
        return executor;
    }
    private int maxThreads = 1;
    private ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    public ThreadBackpacks() throws IOException {
        File file = new File(Main.getMain().getDataFolder().getCanonicalFile().getAbsolutePath() + "/config.yml");
        if(file.exists()){
            if(Main.getMain().getConfig().getInt("maxThreads") == 0){
                return;
            }
            executor = Executors.newScheduledThreadPool(Main.getMain().getConfig().getInt("maxThreads"));
            maxThreads = Main.getMain().getConfig().getInt("maxThreads");
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

            //others
            Bukkit.getPluginManager().registerEvents(new HopperEvents(), Main.getMain());
            Bukkit.getPluginManager().registerEvents(new ConfigItemsEvents(), Main.getMain());
            Bukkit.getPluginManager().registerEvents(new ServerLoadEvent(), Main.getMain());
            Bukkit.getPluginManager().registerEvents(new Bplist(), Main.getMain());

            //Upgrades
            Bukkit.getPluginManager().registerEvents(new CraftingTable(), Main.getMain());
            Bukkit.getPluginManager().registerEvents(new Furnace(), Main.getMain());
            Bukkit.getPluginManager().registerEvents(new Jukebox(), Main.getMain());
            Bukkit.getPluginManager().registerEvents(new AutoFeed(), Main.getMain());
            Bukkit.getPluginManager().registerEvents(new VillagersFollow(), Main.getMain());
            Bukkit.getPluginManager().registerEvents(new Collector(), Main.getMain());

            //advancements
            BackpacksAdvancements.createAdvancement(NamespacesAdvacements.getCAUGHT_A_BACKPACK(), "tropical_fish", "Wow, thats a strange 'fish'", "Caught a backpack instead of a fish" ,BackpacksAdvancements.Style.TASK);
            BackpacksAdvancements.createAdvancement(NamespacesAdvacements.getTHEFIRSTOFUS(), "barrel", "The first of us.", "Craft your first backpack" ,BackpacksAdvancements.Style.TASK);

            Main.getMain().getCommand("bdebug").setExecutor(new Bdebug());
            Main.getMain().getCommand("bpgive").setExecutor(new Bpgive());
            Main.getMain().getCommand("bpgiveid").setExecutor(new BpgiveID());
            Main.getMain().getCommand("bplist").setExecutor(new Bplist());
            return null;
        });
    }

    private void cancelAllTasks(){
        for(BukkitTask task : Bukkit.getScheduler().getPendingTasks()){
            if(task.getOwner().equals(Main.getMain())){
                task.cancel();
            }
        }
    }

    public void saveAll() throws IOException {
        cancelAllTasks();

        Future<Void> future = executor.submit(() -> {
            YamlUtils.saveBackpacks();
            YamlUtils.saveUpgrades();
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

        Main.getMain().setBackupHandler(new BackupHandler(Main.getMain().getConfig().getInt("autobackup.keep")));

        if(Main.getMain().getConfig().getBoolean("autobackup.enabled")){
            if(Main.getMain().getConfig().isSet("autobackup.path")){
                new ScheduledBackup(ScheduledBackup.IntervalType.valueOf(Main.getMain().getConfig().getString("autobackup.type")), Main.getMain().getConfig().getInt("autobackup.interval"), Main.getMain().getConfig().getString("autobackup.path")).startWithDelay();
                return;
            }
            new ScheduledBackup(ScheduledBackup.IntervalType.valueOf(Main.getMain().getConfig().getString("autobackup.type")), Main.getMain().getConfig().getInt("autobackup.interval")).startWithDelay();
        }

    }
}