package br.com.backpacks;

import br.com.backpacks.backpackUtils.BackPackManager;
import br.com.backpacks.recipes.RecipesNamespaces;
import br.com.backpacks.recipes.UpgradesRecipesNamespaces;
import org.bukkit.Bukkit;
import org.bukkit.inventory.BlastingRecipe;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.SmokingRecipe;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class Main extends JavaPlugin {
    private static Main back;
    private List<FurnaceRecipe> furnaceRecipes = new ArrayList<>();

    public void setSmokingRecipes(List<SmokingRecipe> smokingRecipes) {
        this.smokingRecipes = smokingRecipes;
    }

    private List<SmokingRecipe> smokingRecipes = new ArrayList<>();

    public List<SmokingRecipe> getSmokingRecipes() {
        return smokingRecipes;
    }

    public void setFurnaceRecipes(List<FurnaceRecipe> recipes){
        this.furnaceRecipes = recipes;
    }

    public List<BlastingRecipe> getBlastingRecipes() {
        return blastingRecipes;
    }

    public void setBlastingRecipes(List<BlastingRecipe> blastingRecipes) {
        this.blastingRecipes = blastingRecipes;
    }

    private List<BlastingRecipe> blastingRecipes = new ArrayList<>();
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
        String version = Bukkit.getBukkitVersion().split("-")[0];
        if(!version.contains("1.20")){
            Bukkit.getConsoleSender().sendMessage(Main.PREFIX + "§cThis plugin at the moment is only compatible with the 1.20.x versions.");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        setMain(this);
        saveDefaultConfig();

        if(getConfig().getBoolean("debug"))  debugMode = true;

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

    public void debugMessage(String message){
        if(debugMode){
            getLogger().info(message);
        }
    }

    private void registerRecipes(){
        //Backpacks
        Bukkit.addRecipe(new RecipesNamespaces().leatherBackpackRecipe());
        Bukkit.addRecipe(new RecipesNamespaces().ironBackpackRecipe());
        Bukkit.addRecipe(new RecipesNamespaces().diamondBackpackRecipe());
        Bukkit.addRecipe(new RecipesNamespaces().netheriteBackpackRecipe());
        Bukkit.addRecipe(new RecipesNamespaces().goldBackpackRecipe());
        Bukkit.addRecipe(new RecipesNamespaces().amethystBackpackRecipe());
        Bukkit.addRecipe(new RecipesNamespaces().lapisBackpackRecipe());
        Bukkit.addRecipe(new RecipesNamespaces().driedBackpackRecipe());


        //Upgrades
      //  Bukkit.addRecipe(new UpgradesRecipesNamespaces().getRecipeAutoFill());
        Bukkit.addRecipe(new UpgradesRecipesNamespaces().getAutoFeedRecipe());
        Bukkit.addRecipe(new UpgradesRecipesNamespaces().getJukeboxRecipe());
        Bukkit.addRecipe(new UpgradesRecipesNamespaces().getFurnaceRecipe());
        Bukkit.addRecipe(new UpgradesRecipesNamespaces().getSmokerRecipe());
        Bukkit.addRecipe(new UpgradesRecipesNamespaces().getBlastFurnaceRecipe());
        Bukkit.addRecipe(new UpgradesRecipesNamespaces().getCraftingTableRecipe());
        Bukkit.addRecipe(new UpgradesRecipesNamespaces().getFollowingVillagersRecipe());
        Bukkit.addRecipe(new UpgradesRecipesNamespaces().getEncapsulateRecipe());
        Bukkit.addRecipe(new UpgradesRecipesNamespaces().getCollectorRecipe());
        Bukkit.addRecipe(new UpgradesRecipesNamespaces().getUnbreakableUpgradeRecipe());
        //Bukkit.addRecipe(new UpgradesRecipesNamespaces().getRecipeLiquidTank());
    }

}