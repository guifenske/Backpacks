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
import br.com.backpacks.utils.backpacks.BackPackManager;
import br.com.backpacks.utils.backpacks.BackpackAction;
import br.com.backpacks.utils.scheduler.TickComponent;
import br.com.backpacks.utils.scheduler.TickManager;
import org.bukkit.Bukkit;
import org.bukkit.block.Barrel;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.time.Instant;
import java.util.UUID;

public final class Main extends JavaPlugin {
    private AutoSaveManager autoSaveManager;
    private BackupHandler backupHandler;
    public boolean saveComplete = false;
    private static Main main;
    public Instant start;
    public final String PREFIX = "§8[§6BackPacks§8] ";
    public final Object lock = new Object();
    public static final BackPackManager backPackManager = new BackPackManager();
    private final TickManager tickManager = new TickManager();

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

    public TickManager getTickManager() {
        return tickManager;
    }

    @Override
    public void onEnable() {
        main = this;
        saveDefaultConfig();

        Constants.VERSION = Bukkit.getServer().getMinecraftVersion();

        if(!Constants.SUPPORTED_VERSIONS.contains(Constants.VERSION)){
            Bukkit.getConsoleSender().sendMessage(Main.getMain().PREFIX + "§cThis plugin at the moment is only compatible with 1.21.x versions. Current server version: " + Constants.VERSION);
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        main.start = Instant.now();

        Constants.DEBUG_MODE = getConfig().getBoolean("debug");
        Constants.CATCH_BACKPACK = getConfig().getBoolean("fish_backpack");
        Constants.MONSTER_DROPS_BACKPACK = getConfig().getBoolean("kill_monster_backpack");

        if(getConfig().getBoolean("mysql.enabled")){
            StorageManager.setProvider(Config.getMySQLProvider());

            if(!((MySQLProvider) StorageManager.getProvider()).canConnect()){
                StorageManager.setProvider(Config.getYamlProvider());
            }
        }

        else{
            StorageManager.setProvider(Config.getYamlProvider());
        }

        tickManager.startAsyncTicking();

        tickManager.runComponentAsync(new TickComponent() {
            @Override
            public void tick() {
                StorageManager.loadAll();
            }
        });

        tickManager.addAsyncComponent(new TickComponent(5) {
            @Override
            public void tick() {
                for(BackPack backPack : Main.backPackManager.getBackpacks().values()){
                    if(backPack.getOwner() != null){
                        VillagerBait.tick(Bukkit.getPlayer(backPack.getOwner()));
                        Magnet.tick(Bukkit.getPlayer(backPack.getOwner()));
                    }   else if(backPack.getLocation() != null){
                        Magnet.tick(backPack);
                    }
                }
            }
        }, 0);

        UpdateChecker.checkForUpdates();

        //player
        Bukkit.getPluginManager().registerEvents(new CraftBackpack(), Main.getMain());
        Bukkit.getPluginManager().registerEvents(new Fishing(), Main.getMain());
        Bukkit.getPluginManager().registerEvents(new FurnaceEvents(), Main.getMain());
        Bukkit.getPluginManager().registerEvents(new InteractOtherPlayerBackpack(), Main.getMain());
        Bukkit.getPluginManager().registerEvents(new AnvilRenameBackpack(), Main.getMain());

        //backpack
        Bukkit.getPluginManager().registerEvents(new BackpackInteract(), Main.getMain());
        Bukkit.getPluginManager().registerEvents(new BackpackBreak(), Main.getMain());
        Bukkit.getPluginManager().registerEvents(new BackpackPlace(), Main.getMain());
        Bukkit.getPluginManager().registerEvents(new OnClickBackpack(), Main.getMain());
        Bukkit.getPluginManager().registerEvents(new RenameBackpack(), Main.getMain());
        Bukkit.getPluginManager().registerEvents(new OpenEquippedBackpack(), Main.getMain());
        Bukkit.getPluginManager().registerEvents(new OnCloseBackpack(), Main.getMain());
        Bukkit.getPluginManager().registerEvents(new MenuListener(), Main.getMain());

        //others
        Bukkit.getPluginManager().registerEvents(new HopperEvents(), Main.getMain());
        Bukkit.getPluginManager().registerEvents(new ConfigItemsEvents(), Main.getMain());
        Bukkit.getPluginManager().registerEvents(new BpList(), Main.getMain());
        Bukkit.getPluginManager().registerEvents(new EntityDeathEvent(), Main.getMain());
        Bukkit.getPluginManager().registerEvents(new OnDimensionSwitch(), Main.getMain());

        //Upgrades
        Bukkit.getPluginManager().registerEvents(new CraftingTable(), Main.getMain());
        Bukkit.getPluginManager().registerEvents(new Furnace(), Main.getMain());
        Bukkit.getPluginManager().registerEvents(new Jukebox(), Main.getMain());
        Bukkit.getPluginManager().registerEvents(new AutoFeed(), Main.getMain());
        Bukkit.getPluginManager().registerEvents(new VillagerBait(), Main.getMain());
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
        if(!BackpackAction.getHashMap().isEmpty()){
            for(UUID uuid : BackpackAction.getHashMap().keySet()){
                Player player = Bukkit.getPlayer(uuid);

                if(player == null) continue;

                BackPack backPack = Main.backPackManager.getPlayerCurrentBackpack(player);

                if(backPack.getLocation() != null){
                    Barrel barrel = (Barrel) backPack.getLocation().getBlock().getState();
                    barrel.close();
                }

                BackpackAction.getHashMap().remove(uuid);
                BackpackAction.getSpectators().remove(uuid);

                player.closeInventory();
            }
        }

        Main.getMain().getLogger().info("[Backpacks] Saving backpacks..");
        saveConfig();

        StorageManager.saveAll(false);

        synchronized (main.lock) {
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
        Bukkit.addRecipe(UpgradesRecipes.getCraftingTableRecipe());
        Bukkit.addRecipe(UpgradesRecipes.getFollowingVillagersRecipe());
        Bukkit.addRecipe(UpgradesRecipes.getEncapsulateRecipe());
        Bukkit.addRecipe(UpgradesRecipes.getCollectorRecipe());
        Bukkit.addRecipe(UpgradesRecipes.getUnbreakableUpgradeRecipe());
        Bukkit.addRecipe(UpgradesRecipes.getLiquidTankRecipe());
    }

}