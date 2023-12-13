package br.com.backpacks;

import br.com.backpacks.backpackUtils.BackPackManager;
import br.com.backpacks.recipes.RecipesNamespaces;
import br.com.backpacks.recipes.UpgradesRecipesNamespaces;
import br.com.backpacks.yaml.YamlUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public final class Main extends JavaPlugin {

    private static Main back;

    public static Boolean debugMode = false;

    public static String PREFIX = "§8[§6BackPacks§8] §7";

    public static final BackPackManager backPackManager = new BackPackManager();

    public ThreadBackpacks getThreadBackpacks() {
        return threadBackpacks;
    }

    private final ThreadBackpacks threadBackpacks = new ThreadBackpacks();

    public static final Object lock = new Object();
    public static boolean saveComplete = false;

    public static Main getMain() {
        return back;
    }

    private static void setBack(Main back) {
        Main.back = back;
    }

    @Override
    public void onEnable() {
        setBack(this);
        threadBackpacks.registerAll();
        registerRecipes();
        Bukkit.getConsoleSender().sendMessage(Main.PREFIX + "Hello from BackPacks");

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
        if(Main.backPackManager.getBackpacks().isEmpty()){
            synchronized (lock) {
                saveComplete = true;
                lock.notifyAll();
            }
            return;
        }

        YamlUtils.save_backpacks_yaml();
        YamlUtils.savePlacedBackpacks();

        threadBackpacks.shutdown();
        Main.getMain().getLogger().info("All backpacks saved!");
        synchronized (lock) {
            saveComplete = true;
            lock.notifyAll();
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
    }

}