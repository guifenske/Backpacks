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
        back = this;

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

        //TO-DO save all backpacks in the corresponding file
    }
}
