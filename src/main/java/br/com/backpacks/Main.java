package br.com.backpacks;

import br.com.backpacks.backpackUtils.BackPackManager;
import br.com.backpacks.recipes.RecipesNamespaces;
import br.com.backpacks.recipes.UpgradesRecipesNamespaces;
import org.bukkit.Bukkit;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.Recipe;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class Main extends JavaPlugin {

    private static Main back;

    private final List<FurnaceRecipe> furnaceRecipes = new ArrayList<>();

    public Main() throws IOException {
    }

    public List<FurnaceRecipe> getFurnaceRecipes() {
        return furnaceRecipes;
    }

    public static Boolean debugMode = false;

    public static String PREFIX = "§8[§6BackPacks§8] ";

    public static final BackPackManager backPackManager = new BackPackManager();

    public ThreadBackpacks getThreadBackpacks() {
        return threadBackpacks;
    }

    private ThreadBackpacks threadBackpacks;

    public static final Object lock = new Object();
    public static boolean saveComplete = false;

    public static Main getMain() {
        return back;
    }

    private static void setMain(Main back) {
        Main.back = back;
    }

    @Override
    public void onEnable() {
        setMain(this);
        try {
            threadBackpacks = new ThreadBackpacks();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        threadBackpacks.registerAll();
        registerRecipes();
        Bukkit.getConsoleSender().sendMessage(Main.PREFIX + "Hello from BackPacks");

        threadBackpacks.loadAll();
    }

    @Override
    public void onDisable() {
        Main.getMain().getLogger().info("Saving backpacks.");

        try {
            threadBackpacks.saveAll();
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

    private void registerRecipes(){
        //Backpacks
        Bukkit.addRecipe(new RecipesNamespaces().leather_backpack_recipe());
        Bukkit.addRecipe(new RecipesNamespaces().iron_backpack_recipe());
        Bukkit.addRecipe(new RecipesNamespaces().diamond_backpack_recipe());
        Bukkit.addRecipe(new RecipesNamespaces().netherite_backpack_recipe());
        Bukkit.addRecipe(new RecipesNamespaces().gold_backpack_recipe());
        Bukkit.addRecipe(new RecipesNamespaces().amethyst_backpack_recipe());
        Bukkit.addRecipe(new RecipesNamespaces().lapis_backpack_recipe());
        Bukkit.addRecipe(new RecipesNamespaces().driedBackpackRecipe());


        //Upgrades
        Bukkit.addRecipe(new UpgradesRecipesNamespaces().getRecipeTrashCan());
        Bukkit.addRecipe(new UpgradesRecipesNamespaces().getRecipeAutoFill());
        Bukkit.addRecipe(new UpgradesRecipesNamespaces().getRecipeAutoFood());
        Bukkit.addRecipe(new UpgradesRecipesNamespaces().getRecipeJukebox());
        Bukkit.addRecipe(new UpgradesRecipesNamespaces().getRecipeFurnaceGrid());
        Bukkit.addRecipe(new UpgradesRecipesNamespaces().getRecipeCraftingGrid());
        Bukkit.addRecipe(new UpgradesRecipesNamespaces().getRecipeEmeraldBlock());
        Bukkit.addRecipe(new UpgradesRecipesNamespaces().getRecipeLiquidTank());

        Iterator<Recipe> iterator = Bukkit.recipeIterator();

        while (iterator.hasNext()){
            Recipe recipe = iterator.next();
            if(!(recipe instanceof FurnaceRecipe)) continue;
            furnaceRecipes.add((FurnaceRecipe) recipe);
        }
    }

}