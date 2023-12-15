package br.com.backpacks;

import br.com.backpacks.advancements.BackpacksAdvancements;
import br.com.backpacks.advancements.NamespacesAdvacements;
import br.com.backpacks.commands.Bdebug;
import br.com.backpacks.events.CraftBackpack;
import br.com.backpacks.events.FinishedSmelting;
import br.com.backpacks.events.Fishing;
import br.com.backpacks.events.InteractOtherPlayerBackpack;
import br.com.backpacks.events.backpack_related.*;
import br.com.backpacks.events.inventory.*;
import br.com.backpacks.events.upgrades_related.CraftingGrid;
import br.com.backpacks.events.upgrades_related.FurnaceGrid;
import br.com.backpacks.events.upgrades_related.TrashCan;
import br.com.backpacks.yaml.YamlUtils;
import org.bukkit.Bukkit;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ThreadBackpacks {
    private ExecutorService executor;

    public ThreadBackpacks() {
        executor = Executors.newCachedThreadPool();
    }

    //example for now
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
            Bukkit.getPluginManager().registerEvents(new TrashCan(), Main.getMain());
            Bukkit.getPluginManager().registerEvents(new InteractOtherPlayerBackpack(), Main.getMain());
            Bukkit.getPluginManager().registerEvents(new OnCloseUpgradeMenu(), Main.getMain());
            Bukkit.getPluginManager().registerEvents(new OnClickUpgradesMenu(), Main.getMain());

            //Upgrades
            Bukkit.getPluginManager().registerEvents(new CraftingGrid(), Main.getMain());
            Bukkit.getPluginManager().registerEvents(new FurnaceGrid(), Main.getMain());

            BackpacksAdvancements.createAdvancement(NamespacesAdvacements.getCAUGHT_A_BACKPACK(), "chest", "Wow, thats a huge 'fish'", BackpacksAdvancements.Style.TASK);

            Main.getMain().getCommand("Bdebug").setExecutor(new Bdebug());
            return null;
        });
    }

    public void saveAll() {

        Future<Void> future = executor.submit(() -> {

            YamlUtils.save_backpacks_yaml();
            YamlUtils.savePlacedBackpacks();

            return null;
        });

        try {
            future.get();
        } catch (Exception ignored) {
        }

        Main.saveComplete = true;
        synchronized (Main.lock){
            Main.lock.notifyAll();
        }

        shutdown();

    }

    public void loadAll(){
        executor.submit(() -> {

            YamlUtils.loadBackpacksYaml();
            YamlUtils.loadPlacedBackpacks();

            return null;
        });
    }


    public void shutdown() {
        executor.shutdown();
    }
}