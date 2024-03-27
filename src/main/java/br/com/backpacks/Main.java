package br.com.backpacks;

import br.com.backpacks.backup.BackupHandler;
import br.com.backpacks.commands.*;
import br.com.backpacks.events.ConfigItemsEvents;
import br.com.backpacks.events.HopperEvents;
import br.com.backpacks.events.OnDimensionSwitch;
import br.com.backpacks.events.ServerLoadEvent;
import br.com.backpacks.events.backpacks.*;
import br.com.backpacks.events.entity.*;
import br.com.backpacks.events.inventory.*;
import br.com.backpacks.events.upgrades.*;
import br.com.backpacks.recipes.BackpackRecipes;
import br.com.backpacks.recipes.UpgradesRecipes;
import br.com.backpacks.storage.MySQLProvider;
import br.com.backpacks.storage.StorageManager;
import br.com.backpacks.utils.Constants;
import br.com.backpacks.utils.backpacks.BackPackManager;
import br.com.backpacks.utils.backpacks.BackpackAction;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.BlastingRecipe;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.SmokingRecipe;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class Main extends JavaPlugin {
    private AutoSaveManager autoSaveManager;
    private BackupHandler backupHandler;
    private List<SmokingRecipe> smokingRecipes = new ArrayList<>();
    private List<FurnaceRecipe> furnaceRecipes = new ArrayList<>();
    private List<BlastingRecipe> blastingRecipes = new ArrayList<>();
    public static boolean saveComplete = false;
    private static Main main;
    public static Instant start;
    public static String PREFIX = "§8[§6BackPacks§8] ";
    public static final Object lock = new Object();
    public static final BackPackManager backPackManager = new BackPackManager();

    public void setSmokingRecipes(List<SmokingRecipe> smokingRecipes) {
        this.smokingRecipes = smokingRecipes;
    }

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

    public List<FurnaceRecipe> getFurnaceRecipes() {
        return furnaceRecipes;
    }

    public static Main getMain() {
        return main;
    }

    public BackupHandler getBackupHandler() {
        return backupHandler;
    }

    public void setBackupHandler(BackupHandler backupHandler) {
        this.backupHandler = backupHandler;
    }

    public AutoSaveManager getAutoSaveManager() {
        return autoSaveManager;
    }

    public void setAutoSaveManager(AutoSaveManager autoSaveManager) {
        this.autoSaveManager = autoSaveManager;
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();
        main = this;
        Constants.VERSION = Bukkit.getMinecraftVersion();
        start = Instant.now();

        if(!Constants.SUPPORTED_VERSIONS.contains(Constants.VERSION)){
            Bukkit.getConsoleSender().sendMessage(Main.PREFIX + "§cThis plugin at the moment is only compatible with 1.20.x, 1.19.x, 1.18.x versions.");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        Bukkit.getScheduler().runTaskAsynchronously(this, ()->{
            if(getConfig().getBoolean("debug")){
                Constants.DEBUG_MODE = true;
            }
            if(getConfig().getBoolean("fish_backpack")){
                Constants.CATCH_BACKPACK = true;
            }
            if(getConfig().getBoolean("kill_monster_backpack")){
                Constants.MONSTER_DROPS_BACKPACK = true;
            }

            if(getConfig().getBoolean("mysql.enabled")){
                StorageManager.setProvider(Config.getMySQLProvider());
                if(!((MySQLProvider) StorageManager.getProvider()).canConnect()){
                    StorageManager.setProvider(Config.getYamlProvider());
                }
            }   else{
                StorageManager.setProvider(Config.getYamlProvider());
            }

            ThreadBackpacks.loadAll();
            UpdateChecker.checkForUpdates();
        });

        //player
        Bukkit.getPluginManager().registerEvents(new CraftBackpack(), Main.getMain());
        Bukkit.getPluginManager().registerEvents(new Fishing(), Main.getMain());
        Bukkit.getPluginManager().registerEvents(new FinishedSmelting(), Main.getMain());
        Bukkit.getPluginManager().registerEvents(new InteractOtherPlayerBackpack(), Main.getMain());
        Bukkit.getPluginManager().registerEvents(new AnvilRenameBackpack(), Main.getMain());

        //backpack
        Bukkit.getPluginManager().registerEvents(new BackpackInteract(), Main.getMain());
        Bukkit.getPluginManager().registerEvents(new BackpackBreak(), Main.getMain());
        Bukkit.getPluginManager().registerEvents(new BackpackPlace(), Main.getMain());
        Bukkit.getPluginManager().registerEvents(new OnClickBackpack(), Main.getMain());
        Bukkit.getPluginManager().registerEvents(new OnClickInConfigMenu(), Main.getMain());
        Bukkit.getPluginManager().registerEvents(new OnCloseBackpackConfigMenu(), Main.getMain());
        Bukkit.getPluginManager().registerEvents(new RenameBackpack(), Main.getMain());
        Bukkit.getPluginManager().registerEvents(new OpenBackpackOfTheBack(), Main.getMain());
        Bukkit.getPluginManager().registerEvents(new OnCloseBackpack(), Main.getMain());
        Bukkit.getPluginManager().registerEvents(new OnCloseUpgradeMenu(), Main.getMain());
        Bukkit.getPluginManager().registerEvents(new OnClickUpgradesMenu(), Main.getMain());
        Bukkit.getPluginManager().registerEvents(new IOMenu(), Main.getMain());

        //others
        Bukkit.getPluginManager().registerEvents(new HopperEvents(), Main.getMain());
        Bukkit.getPluginManager().registerEvents(new ConfigItemsEvents(), Main.getMain());
        Bukkit.getPluginManager().registerEvents(new ServerLoadEvent(), Main.getMain());
        Bukkit.getPluginManager().registerEvents(new BpList(), Main.getMain());
        Bukkit.getPluginManager().registerEvents(new EntityDeathEvent(), Main.getMain());
        Bukkit.getPluginManager().registerEvents(new OnDimensionSwitch(), Main.getMain());

        //Upgrades
        Bukkit.getPluginManager().registerEvents(new CraftingTable(), Main.getMain());
        Bukkit.getPluginManager().registerEvents(new Furnace(), Main.getMain());
        Bukkit.getPluginManager().registerEvents(new Jukebox(), Main.getMain());
        Bukkit.getPluginManager().registerEvents(new AutoFeed(), Main.getMain());
        Bukkit.getPluginManager().registerEvents(new VillagersFollow(), Main.getMain());
        Bukkit.getPluginManager().registerEvents(new Collector(), Main.getMain());
        Bukkit.getPluginManager().registerEvents(new Tanks(), Main.getMain());

        Main.getMain().getCommand("bpgive").setExecutor(new BpGive());
        Main.getMain().getCommand("bplist").setExecutor(new BpList());
        Main.getMain().getCommand("bpbackup").setExecutor(new BpBackup());
        Main.getMain().getCommand("bpreload").setExecutor(new BpReload());
        Main.getMain().getCommand("bpupgbackpack").setExecutor(new BpUpgBackpack());
        Main.getMain().getCommand("bpupgive").setExecutor(new BpUpGive());
        registerRecipes();
    }

    @Override
    public void onDisable() {
        //reload logic
        for(UUID uuid : BackpackAction.getHashMap().keySet()){
            Player player = Bukkit.getPlayer(uuid);
            BackpackAction.getHashMap().remove(uuid);
            BackpackAction.getSpectators().remove(uuid);
            if(player == null) continue;
            player.closeInventory(InventoryCloseEvent.Reason.CANT_USE);
        }

        Bukkit.getConsoleSender().sendMessage("[Backpacks] Saving backpacks..");
        saveConfig();

        try {
            ThreadBackpacks.saveAll();
        } catch (IOException e) {
            Main.getMain().getLogger().severe("Something went wrong, please report to the developer!");
            throw new RuntimeException(e);
        }

        synchronized (lock) {
            while (!saveComplete) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    Main.getMain().getLogger().severe("Something went wrong, please report to the developer!");
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public static void debugMessage(String message){
        if(Constants.DEBUG_MODE){
            Main.getMain().getLogger().info(message);
        }
    }

    private void registerRecipes(){
        //Backpacks
        Bukkit.addRecipe(new BackpackRecipes().leatherBackpackRecipe());
        Bukkit.addRecipe(new BackpackRecipes().ironBackpackRecipe());
        Bukkit.addRecipe(new BackpackRecipes().diamondBackpackRecipe());
        Bukkit.addRecipe(new BackpackRecipes().netheriteBackpackRecipe());
        Bukkit.addRecipe(new BackpackRecipes().goldBackpackRecipe());
        Bukkit.addRecipe(new BackpackRecipes().amethystBackpackRecipe());
        Bukkit.addRecipe(new BackpackRecipes().lapisBackpackRecipe());
        Bukkit.addRecipe(new BackpackRecipes().driedBackpackRecipe());


        //Upgrades
        Bukkit.addRecipe(new UpgradesRecipes().getAutoFeedRecipe());
        Bukkit.addRecipe(new UpgradesRecipes().getJukeboxRecipe());
        Bukkit.addRecipe(new UpgradesRecipes().getFurnaceRecipe());
        Bukkit.addRecipe(new UpgradesRecipes().getSmokerRecipe());
        Bukkit.addRecipe(new UpgradesRecipes().getBlastFurnaceRecipe());
        Bukkit.addRecipe(new UpgradesRecipes().getCraftingTableRecipe());
        Bukkit.addRecipe(new UpgradesRecipes().getFollowingVillagersRecipe());
        Bukkit.addRecipe(new UpgradesRecipes().getEncapsulateRecipe());
        Bukkit.addRecipe(new UpgradesRecipes().getCollectorRecipe());
        Bukkit.addRecipe(new UpgradesRecipes().getUnbreakableUpgradeRecipe());
        Bukkit.addRecipe(new UpgradesRecipes().getLiquidTankRecipe());
    }

}