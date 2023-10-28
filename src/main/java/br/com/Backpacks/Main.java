package br.com.Backpacks;

import br.com.Backpacks.crafting.recipesUtil;
import br.com.Backpacks.events.blockEvent;
import br.com.Backpacks.events.openBackpack;
import br.com.Backpacks.events.playerEvents;
import br.com.Backpacks.menus.backpackConfig;
import br.com.Backpacks.menus.backpackMenu;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public final class Main extends JavaPlugin {

    public static Main back;


    @Override
    public void onEnable() {
        // Plugin startup logic
        back = this;

        if(Bukkit.getPluginManager().getPlugin("NBTAPI") == null){
            Bukkit.getConsoleSender().sendMessage(this.getName() + " >> NBTAPI is not installed! Disabling plugin...");
            Bukkit.getPluginManager().disablePlugin(this);
        }   else    Bukkit.getConsoleSender().sendMessage(this.getName() + " >> NBTAPI found! Starting up...");


        CompletableFuture.runAsync(() -> {
            Bukkit.getPluginManager().registerEvents(new openBackpack(), this);
            Bukkit.getPluginManager().registerEvents(new backpackMenu(), this);
            Bukkit.getPluginManager().registerEvents(new playerEvents(), this);
            Bukkit.getPluginManager().registerEvents(new blockEvent(), this);
            Bukkit.getPluginManager().registerEvents(new backpackConfig(), this);
        });


        Bukkit.addRecipe(recipesUtil.LeatherBackpack());
        Bukkit.addRecipe(recipesUtil.Upgrader());
        Bukkit.addRecipe(recipesUtil.LeatherUpgrader());
        Bukkit.addRecipe(recipesUtil.GoldUpgrader());
        Bukkit.addRecipe(recipesUtil.IronUpgrader());
        Bukkit.addRecipe(recipesUtil.LapisUpgrader());
        Bukkit.addRecipe(recipesUtil.AmethystUpgrader());
        Bukkit.addRecipe(recipesUtil.GoldBackpack());
        Bukkit.addRecipe(recipesUtil.IronBackpack());
        Bukkit.addRecipe(recipesUtil.LapisBackpack());
        Bukkit.addRecipe(recipesUtil.AmethystBackpack());
        Bukkit.addRecipe(recipesUtil.DiamondBackpack());
        Bukkit.addRecipe(recipesUtil.leatherChestBack());
        Bukkit.addRecipe(recipesUtil.chainChestBack());
        Bukkit.addRecipe(recipesUtil.ironChestBack());
        Bukkit.addRecipe(recipesUtil.goldChestBack());
        Bukkit.addRecipe(recipesUtil.diamondChestBack());
        Bukkit.addRecipe(recipesUtil.Uncrafter());
        Bukkit.addRecipe(recipesUtil.NetheriteBackpack());
        Bukkit.addRecipe(recipesUtil.netheriteChestBack());
        Bukkit.addRecipe(recipesUtil.IronStackUpgrader());
        Bukkit.addRecipe(recipesUtil.GoldStackUpgrader());
        Bukkit.addRecipe(recipesUtil.DiamondStackUpgrader());
        Bukkit.addRecipe(recipesUtil.NetheriteStackUpgrader());
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage("Bye from " + this.getName() + "!");
    }

    public void saveConfigAsync(FileConfiguration config, File file) {
        CompletableFuture.runAsync(() -> {
            try {
                config.save(file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
