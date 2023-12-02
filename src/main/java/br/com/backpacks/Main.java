package br.com.backpacks;

import br.com.backpacks.backpackUtils.BackPackManager;
import br.com.backpacks.events.CraftBackpack;
import br.com.backpacks.events.backpack_related.backpack_break;
import br.com.backpacks.events.backpack_related.backpack_interact;
import br.com.backpacks.events.backpack_related.backpack_place;
import br.com.backpacks.recipes.RecipesNamespaces;
import br.com.backpacks.yaml.YamlUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public final class Main extends JavaPlugin {

    private static Main back;

    public static final BackPackManager backPackManager = new BackPackManager();

    private static final Object lock = new Object();
    private static boolean saveComplete = false;

    public static Main getMain() {
        return back;
    }

    private static void setBack(Main back) {
        Main.back = back;
    }

    @Override
    public void onEnable() {
        setBack(this);
        registerEvents();
        registerRecipes();

        Bukkit.getConsoleSender().sendMessage("Hello from BackPacks");

        try {
            YamlUtils.loadBackpacksYaml();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            YamlUtils.loadPlacedBackpacks();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage("Bye from BackPacks");
        try {
            saveAll();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        synchronized (lock) {
            while (!saveComplete) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private void saveAll() throws IOException {
        if(Main.backPackManager.getBackpacks_ids().isEmpty()){
            synchronized (lock) {
                saveComplete = true;
                lock.notifyAll();
            }
            return;
        }

        YamlUtils.save_backpacks_yaml();
        YamlUtils.savePlacedBackpacks();

        synchronized (lock) {
            saveComplete = true;
            lock.notifyAll();
        }
    }

    private void registerEvents(){
        Bukkit.getPluginManager().registerEvents(new backpack_interact(), this);
        Bukkit.getPluginManager().registerEvents(new backpack_break(), this);
        Bukkit.getPluginManager().registerEvents(new backpack_place(), this);
        Bukkit.getPluginManager().registerEvents(new CraftBackpack(), this);
    }

    private void registerRecipes(){
        Bukkit.addRecipe(new RecipesNamespaces().leather_backpack_recipe());
        Bukkit.addRecipe(new RecipesNamespaces().iron_backpack_recipe());
        Bukkit.addRecipe(new RecipesNamespaces().diamond_backpack_recipe());
        Bukkit.addRecipe(new RecipesNamespaces().netherite_backpack_recipe());
        Bukkit.addRecipe(new RecipesNamespaces().gold_backpack_recipe());
        Bukkit.addRecipe(new RecipesNamespaces().amethyst_backpack_recipe());
        Bukkit.addRecipe(new RecipesNamespaces().lapis_backpack_recipe());
    }

}