package br.com.Backpacks;

import br.com.Backpacks.backpackUtils.BackPackManager;
import br.com.Backpacks.events.backpack_related.backpack_break;
import br.com.Backpacks.events.backpack_related.backpack_interact;
import br.com.Backpacks.events.backpack_related.backpack_place;
import br.com.Backpacks.events.craft_backpack;
import br.com.Backpacks.events.player_leave_join;
import br.com.Backpacks.recipes.Recipes;
import br.com.Backpacks.yaml.YamlUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public final class Main extends JavaPlugin {

    public static Main back;

    public BackPackManager backPackManager = new BackPackManager();

    private static final Object lock = new Object();
    private static boolean save_complete = false;

    @Override
    public void onEnable() {
        back = this;

        Bukkit.getPluginManager().registerEvents(new backpack_interact(), this);
        Bukkit.getPluginManager().registerEvents(new backpack_break(), this);
        Bukkit.getPluginManager().registerEvents(new backpack_place(), this);
        Bukkit.getPluginManager().registerEvents(new craft_backpack(), this);
        Bukkit.getPluginManager().registerEvents(new player_leave_join(), this);
        Bukkit.addRecipe(new Recipes().leather_backpack_recipe());
        Bukkit.addRecipe(new Recipes().iron_backpack_recipe());
        Bukkit.addRecipe(new Recipes().diamond_backpack_recipe());
        Bukkit.addRecipe(new Recipes().netherite_backpack_recipe());
        Bukkit.addRecipe(new Recipes().gold_backpack_recipe());
        Bukkit.addRecipe(new Recipes().amethyst_backpack_recipe());
        Bukkit.addRecipe(new Recipes().lapis_backpack_recipe());

    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage("Bye from BackPacks");
        try {
            save_all();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        synchronized (lock) {
            while (!save_complete) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public static void save_all() throws IOException {
        if(Main.back.backPackManager.getBackpacks_ids().isEmpty()){
            synchronized (lock) {
                save_complete = true;
                lock.notifyAll();
            }
            return;
        }

        for(Player player : Bukkit.getOnlinePlayers()){
            try {
                YamlUtils.save_backpacks_yaml(player);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        synchronized (lock) {
            save_complete = true;
            lock.notifyAll();
        }
    }
}
