package br.com.Backpacks;

import br.com.Backpacks.events.backpack_related.backpack_place;
import br.com.Backpacks.events.craft_backpack;
import br.com.Backpacks.events.player_leave_join;
import br.com.Backpacks.recipes.Recipes;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    public static Main back;

    public BackPackManager backPackManager = new BackPackManager();

    @Override
    public void onEnable() {
        // Plugin startup logic
        back = this;

        Bukkit.getPluginManager().registerEvents(new backpack_place(), this);
        Bukkit.getPluginManager().registerEvents(new craft_backpack(), this);
        Bukkit.getPluginManager().registerEvents(new player_leave_join(), this);
        Bukkit.addRecipe(new Recipes().leather_backpack_recipe());

        //ao player entrar, carregar todas as mochilas do arquivo gson
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage("Bye from BackPacks");
    }
}
