package br.com.backpacks;

import br.com.backpacks.backup.BackupHandler;
import br.com.backpacks.commands.*;
import br.com.backpacks.events.ConfigItemsEvents;
import br.com.backpacks.events.HopperEvents;
import br.com.backpacks.events.OnDimensionSwitch;
import br.com.backpacks.events.backpacks.*;
import br.com.backpacks.events.entity.*;
import br.com.backpacks.events.inventory.*;
import br.com.backpacks.events.upgrades.*;
import br.com.backpacks.recipes.BackpackRecipes;
import br.com.backpacks.recipes.UpgradesRecipes;
import br.com.backpacks.storage.MySQLProvider;
import br.com.backpacks.storage.StorageManager;
import br.com.backpacks.utils.Constants;
import br.com.backpacks.utils.backpacks.BackPack;
import br.com.backpacks.utils.backpacks.BackpackAction;
import br.com.backpacks.utils.backpacks.BackpackManager;
import org.bukkit.Bukkit;
import org.bukkit.block.Barrel;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.BlastingRecipe;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.SmokingRecipe;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public final class Main extends JavaPlugin {
    private AutoSaveManager autoSaveManager;
    private BackupHandler backupHandler;
    private final List<SmokingRecipe> smokingRecipes = new ArrayList<>();
    private final List<FurnaceRecipe> furnaceRecipes = new ArrayList<>();
    private final List<BlastingRecipe> blastingRecipes = new ArrayList<>();
    public static boolean saveComplete = false;
    private static Main main;
    public static Instant start;
    public static String PREFIX = "§8[§6BackPacks§8] ";
    public static final Object lock = new Object();

    public List<SmokingRecipe> getSmokingRecipes() {
        return smokingRecipes;
    }

    public List<BlastingRecipe> getBlastingRecipes() {
        return blastingRecipes;
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

        if(!Constants.SUPPORTED_VERSIONS.contains(Constants.VERSION)){
            Bukkit.getConsoleSender().sendMessage(Main.PREFIX + "§cThis plugin at the moment is only compatible with 1.20.x, 1.19.x, 1.18.x versions.");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        Bukkit.getScheduler().runTaskAsynchronously(Main.getMain(), ()->{
            start = Instant.now();
            if (getConfig().getBoolean("debug")) {
                Constants.DEBUG_MODE = true;
            }
            if (getConfig().getBoolean("fish_backpack")) {
                Constants.CATCH_BACKPACK = true;
            }
            if (getConfig().getBoolean("kill_monster_backpack")) {
                Constants.MONSTER_DROPS_BACKPACK = true;
            }

            if (getConfig().getBoolean("mysql.enabled")) {
                StorageManager.setProvider(Config.getMySQLProvider());
                if (!((MySQLProvider) StorageManager.getProvider()).canConnect()) {
                    StorageManager.setProvider(Config.getYamlProvider());
                }
            } else {
                StorageManager.setProvider(Config.getYamlProvider());
            }

            ThreadBackpacks.loadAll();
            UpdateChecker.checkForUpdates();
        });

        updateMarkersIds();

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
        Bukkit.getPluginManager().registerEvents(new BpList(), Main.getMain());
        Bukkit.getPluginManager().registerEvents(new EntityDeathEvent(), Main.getMain());
        Bukkit.getPluginManager().registerEvents(new OnDimensionSwitch(), Main.getMain());

        //Upgrades
        Bukkit.getPluginManager().registerEvents(new CraftingUpgradeEvents(), Main.getMain());
        Bukkit.getPluginManager().registerEvents(new FurnaceUpgradeEvents(), Main.getMain());
        Bukkit.getPluginManager().registerEvents(new JukeboxUpgradeEvents(), Main.getMain());
        Bukkit.getPluginManager().registerEvents(new AutoFeedUpgradeEvents(), Main.getMain());
        Bukkit.getPluginManager().registerEvents(new VillagersFollowUpgradeEvents(), Main.getMain());
        Bukkit.getPluginManager().registerEvents(new CollectorUpgradeEvents(), Main.getMain());
        Bukkit.getPluginManager().registerEvents(new TanksUpgradeEvents(), Main.getMain());
        Bukkit.getPluginManager().registerEvents(new FilterUpgradeEvents(), Main.getMain());

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
        if(StorageManager.getProvider() == null) return;

        //reload logic
        for(UUID uuid : BackpackAction.getHashMap().keySet()){
            Player player = Bukkit.getPlayer(uuid);
            if(player == null) continue;
            player.closeInventory(InventoryCloseEvent.Reason.CANT_USE);
        }

        for(BackPack backPack : BackpackManager.getBackpacks().values()){
            if(backPack.getLocation() == null) continue;
            Barrel barrel = (Barrel) backPack.getLocation().getBlock().getState();
            barrel.close();
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
        Bukkit.addRecipe(BackpackRecipes.leatherBackpackRecipe());
        Bukkit.addRecipe(BackpackRecipes.ironBackpackRecipe());
        Bukkit.addRecipe(BackpackRecipes.diamondBackpackRecipe());
        Bukkit.addRecipe(BackpackRecipes.netheriteBackpackRecipe());
        Bukkit.addRecipe(BackpackRecipes.goldBackpackRecipe());
        Bukkit.addRecipe(BackpackRecipes.amethystBackpackRecipe());
        Bukkit.addRecipe(BackpackRecipes.lapisBackpackRecipe());
        Bukkit.addRecipe(BackpackRecipes.driedBackpackRecipe());


        //Upgrades
        Bukkit.addRecipe(UpgradesRecipes.getAutoFeedRecipe());
        Bukkit.addRecipe(UpgradesRecipes.getJukeboxRecipe());
        Bukkit.addRecipe(UpgradesRecipes.getFurnaceRecipe());
        Bukkit.addRecipe(UpgradesRecipes.getCraftingRecipe());
        Bukkit.addRecipe(UpgradesRecipes.getFollowingVillagersRecipe());
        Bukkit.addRecipe(UpgradesRecipes.getEncapsulateRecipe());
        Bukkit.addRecipe(UpgradesRecipes.getCollectorRecipe());
        Bukkit.addRecipe(UpgradesRecipes.getUnbreakableUpgradeRecipe());
        Bukkit.addRecipe(UpgradesRecipes.getLiquidTankRecipe());
        Bukkit.addRecipe(UpgradesRecipes.getMagnetRecipe());
        Bukkit.addRecipe(UpgradesRecipes.getFilterRecipe());
        Bukkit.addRecipe(UpgradesRecipes.getAdvancedFilterUpgrade());

        Iterator<Recipe> iterator = Bukkit.recipeIterator();
        while (iterator.hasNext()){
            Recipe recipe = iterator.next();
            if(!(recipe instanceof FurnaceRecipe)){
                if(recipe instanceof SmokingRecipe){
                    smokingRecipes.add((SmokingRecipe) recipe);
                    continue;
                }   else if (recipe instanceof BlastingRecipe){
                    blastingRecipes.add((BlastingRecipe) recipe);
                    continue;
                }
                continue;
            }
            furnaceRecipes.add((FurnaceRecipe) recipe);
        }
    }

    private void updateMarkersIds(){
        for(BackPack backPack : BackpackManager.getBackpacks().values()){
            if(!backPack.isShowingNameAbove()) continue;
            Barrel barrel = (Barrel) backPack.getLocation().getBlock().getState();
            backPack.setMarkerId(UUID.fromString(barrel.getPersistentDataContainer().get(BackpackRecipes.getMARKER_ID(), PersistentDataType.STRING)));
        }
    }
}