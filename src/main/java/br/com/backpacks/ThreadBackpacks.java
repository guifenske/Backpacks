package br.com.backpacks;

import br.com.backpacks.advancements.BackpacksAdvancements;
import br.com.backpacks.advancements.NamespacesAdvacements;
import br.com.backpacks.commands.Bdebug;
import br.com.backpacks.events.ConfigItemsEvents;
import br.com.backpacks.events.HopperEvents;
import br.com.backpacks.events.backpacks.*;
import br.com.backpacks.events.inventory.*;
import br.com.backpacks.events.player.CraftBackpack;
import br.com.backpacks.events.player.FinishedSmelting;
import br.com.backpacks.events.player.Fishing;
import br.com.backpacks.events.player.InteractOtherPlayerBackpack;
import br.com.backpacks.events.upgrades.*;
import br.com.backpacks.yaml.YamlUtils;
import org.bukkit.Bukkit;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ThreadBackpacks {
    private ExecutorService executor;

    public ThreadBackpacks() throws IOException {
        File file = new File(Main.getMain().getDataFolder().getCanonicalFile().getAbsolutePath() + "/config.yml");
        executor = Executors.newSingleThreadExecutor();
        if(file.exists()){
            if(Main.getMain().getConfig().getInt("maxThreads") == 0){
                return;
            }
            executor = Executors.newFixedThreadPool(Main.getMain().getConfig().getInt("maxThreads"));
        }
    }

    public void registerAll() {
        executor.submit(() -> {

            Bukkit.getPluginManager().registerEvents(new BackpackInteract(), Main.getMain());
            Bukkit.getPluginManager().registerEvents(new BackpackBreak(), Main.getMain());
            Bukkit.getPluginManager().registerEvents(new BackpackPlace(), Main.getMain());
            Bukkit.getPluginManager().registerEvents(new CraftBackpack(), Main.getMain());
            Bukkit.getPluginManager().registerEvents(new OnClickBackpack(), Main.getMain());
            Bukkit.getPluginManager().registerEvents(new OnClickInConfigMenu(), Main.getMain());
            Bukkit.getPluginManager().registerEvents(new OnCloseBackpackConfigMenu(), Main.getMain());
            Bukkit.getPluginManager().registerEvents(new RenameBackpackChat(), Main.getMain());
            Bukkit.getPluginManager().registerEvents(new OpenBackpackOfTheBack(), Main.getMain());
            Bukkit.getPluginManager().registerEvents(new OnCloseBackpack(), Main.getMain());
            Bukkit.getPluginManager().registerEvents(new Fishing(), Main.getMain());
            Bukkit.getPluginManager().registerEvents(new FinishedSmelting(), Main.getMain());
            Bukkit.getPluginManager().registerEvents(new InteractOtherPlayerBackpack(), Main.getMain());
            Bukkit.getPluginManager().registerEvents(new OnCloseUpgradeMenu(), Main.getMain());
            Bukkit.getPluginManager().registerEvents(new OnClickUpgradesMenu(), Main.getMain());
            Bukkit.getPluginManager().registerEvents(new HopperEvents(), Main.getMain());
            Bukkit.getPluginManager().registerEvents(new ConfigItemsEvents(), Main.getMain());

            //Upgrades
            Bukkit.getPluginManager().registerEvents(new CraftingTable(), Main.getMain());
            Bukkit.getPluginManager().registerEvents(new Furnace(), Main.getMain());
            Bukkit.getPluginManager().registerEvents(new Jukebox(), Main.getMain());
            Bukkit.getPluginManager().registerEvents(new AutoFeed(), Main.getMain());
            Bukkit.getPluginManager().registerEvents(new VillagersFollow(), Main.getMain());
            Bukkit.getPluginManager().registerEvents(new Collector(), Main.getMain());

            BackpacksAdvancements.createAdvancement(NamespacesAdvacements.getCAUGHT_A_BACKPACK(), "chest", "Wow, thats a huge 'fish'", BackpacksAdvancements.Style.TASK);

            Main.getMain().getCommand("Bdebug").setExecutor(new Bdebug());
            return null;
        });
    }

    public void saveAll() throws IOException {

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
        shutdown();
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
        VillagersFollow.tick();
    }

    public void shutdown() {
        executor.shutdownNow();
    }
}