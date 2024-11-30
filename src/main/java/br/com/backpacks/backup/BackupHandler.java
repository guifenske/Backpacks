package br.com.backpacks.backup;

import br.com.backpacks.utils.Config;
import br.com.backpacks.Main;
import br.com.backpacks.recipes.BackpackRecipes;
import br.com.backpacks.storage.StorageManager;
import br.com.backpacks.storage.YamlProvider;
import br.com.backpacks.upgrades.Upgrade;
import br.com.backpacks.upgrades.UpgradeManager;
import br.com.backpacks.utils.ZipUtils;
import br.com.backpacks.backpack.Backpack;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Barrel;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.persistence.PersistentDataType;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BackupHandler {
    private ScheduledBackupService scheduledBackupService;
    private int keepBackups;
    private final Path path = Path.of(Main.getMain().getDataFolder().getAbsolutePath() + "/Backups");
    private ConcurrentHashMap<Integer, Backpack> rollbackBackpack;
    private ConcurrentHashMap<Integer, Upgrade> rollbackUpgrade;
    public BackupHandler(int keepBackups) {
        this.keepBackups = keepBackups;
    }

    public void setKeepBackups(int keepBackups) {
        this.keepBackups = keepBackups;
    }

    public void setScheduledBackupService(ScheduledBackupService scheduledBackupService) {
        this.scheduledBackupService = scheduledBackupService;
    }

    public ScheduledBackupService getScheduledBackupService() {
        return scheduledBackupService;
    }

    public boolean removeBackup(String name){
        File file = new File(path + "/" + name);
        return file.delete();
    }

    public long backup() throws IOException, InvalidConfigurationException {
        Instant start = Instant.now();
        path.toFile().mkdir();

        File backpackFile = new File(path + "/backpacks.yml");
        File upgradeFile = new File(path + "/upgrades.yml");
        YamlProvider yamlProvider = Config.getYamlProvider();
        yamlProvider.saveBackpacks();
        yamlProvider.saveUpgrades();

        ZipUtils.zipAll(backpackFile.toPath(), upgradeFile.toPath(), path);
        removeLeftoverFiles();

        Instant finish = Instant.now();
        long time = Duration.between(start, finish).toMillis();
        Main.debugMessage("Backup completed in " + time + " ms!");
        return time;
    }

    public void removeLeftoverFiles(){
        while(getNumberOfFilesInPath(path) > keepBackups){
            File[] files = path.toFile().listFiles();

            if(files == null){
                return;
            }

            int index = 0;
            long oldestDate = files[0].lastModified();

            //remove the oldest file
            for(int i = 0; i < files.length; i++){
                long currentLastModified = files[i].lastModified();
                if(currentLastModified < oldestDate){
                    oldestDate = currentLastModified;
                    index = i;
                }
            }

            try{
                files[index].delete();
            }   catch (SecurityException exception){
                Main.getMain().getLogger().severe(exception.getMessage());
            }
        }
    }

    public long undoRollback() throws IOException {
        if(rollbackUpgrade == null) return -1;
        if(rollbackBackpack == null) return -1;
        Instant start = Instant.now();

        StorageManager.getProvider().loadUpgrades(rollbackUpgrade);
        StorageManager.getProvider().loadBackpacks(rollbackBackpack);

        Bukkit.getScheduler().runTask(Main.getMain(), ()->{
            for(Map.Entry<Location, Integer> entry : Main.backpackManager.getBackpacksPlacedLocations().entrySet()){
                Backpack backpack = Main.backpackManager.getBackpackFromId(entry.getValue());
                if(backpack.isShowingNameAbove()){
                    ArmorStand marker = (ArmorStand) entry.getKey().getWorld().spawnEntity(backpack.getLocation().clone().add(0, 1, 0), EntityType.ARMOR_STAND);
                    marker.setVisible(false);
                    marker.setSmall(true);
                    marker.setCustomName(backpack.getName());
                    marker.setCustomNameVisible(true);
                    marker.setCollidable(false);
                    marker.setInvulnerable(true);
                    marker.setBasePlate(false);
                    marker.setMarker(true);
                    backpack.setMarker(marker.getUniqueId());
                    marker.setRemoveWhenFarAway(false);

                    backpack.setMarker(marker.getUniqueId());

                    Barrel backpackBlock = (Barrel) backpack.getLocation().getBlock().getState();
                    backpackBlock.getPersistentDataContainer().set(BackpackRecipes.NAMESPACE_BACKPACK_MARKER_ID, PersistentDataType.STRING, marker.getUniqueId().toString());
                }

                entry.getKey().getBlock().setType(Material.BARREL);
                backpack.updateBarrelBlock();
            }
        });

        rollbackBackpack = null;
        rollbackUpgrade = null;

        Instant finish = Instant.now();
        Main.backpackManager.setCanBeOpen(true);
        return Duration.between(start, finish).toMillis();
    }

    public long rollback(String path) throws IOException {
        try{
            Path.of(path);
        }   catch (Exception e){
            Main.backpackManager.setCanBeOpen(true);
            Main.getMain().getLogger().severe("Cannot restore backup, invalid path or file not found.");
            Main.getMain().getLogger().severe("Aborting restore task...");
            return -1L;
        }

        Instant start = Instant.now();
        rollbackBackpack = new ConcurrentHashMap<>(Main.backpackManager.getBackpacks());
        rollbackUpgrade = new ConcurrentHashMap<>(UpgradeManager.getUpgrades());
        YamlProvider yamlProvider = Config.getYamlProvider();

        File backpacksOriginal = new File(Main.getMain().getDataFolder().getAbsolutePath() + "/backpacks.yml");
        File upgradesOriginal = new File(Main.getMain().getDataFolder().getAbsolutePath() + "/upgrades.yml");
        backpacksOriginal.delete();
        upgradesOriginal.delete();
        ZipUtils.unzipAll(this.path + "/" + path);

        Main.backpackManager.getBackpacksPlacedLocations().clear();
        Main.backpackManager.getBackpacks().clear();
        UpgradeManager.getUpgrades().clear();

        yamlProvider.loadUpgrades();
        yamlProvider.loadBackpacks();

        Bukkit.getScheduler().runTask(Main.getMain(), ()->{
            for(Map.Entry<Location, Integer> entry : Main.backpackManager.getBackpacksPlacedLocations().entrySet()){
                Backpack backpack = Main.backpackManager.getBackpackFromId(entry.getValue());
                if(backpack.isShowingNameAbove()){
                    ArmorStand marker = (ArmorStand) entry.getKey().getWorld().spawnEntity(backpack.getLocation().clone().add(0, 1, 0), EntityType.ARMOR_STAND);
                    marker.setVisible(false);
                    marker.setSmall(true);
                    marker.setCustomName(backpack.getName());
                    marker.setCustomNameVisible(true);
                    marker.setCollidable(false);
                    marker.setInvulnerable(true);
                    marker.setBasePlate(false);
                    marker.setMarker(true);
                    marker.setRemoveWhenFarAway(false);

                    backpack.setMarker(marker.getUniqueId());

                    Barrel backpackBlock = (Barrel) backpack.getLocation().getBlock().getState();
                    backpackBlock.getPersistentDataContainer().set(BackpackRecipes.NAMESPACE_BACKPACK_MARKER_ID, PersistentDataType.STRING, marker.getUniqueId().toString());
                }

                entry.getKey().getBlock().setType(Material.BARREL);
                backpack.updateBarrelBlock();
            }
        });

        Main.backpackManager.setCanBeOpen(true);
        Instant finish = Instant.now();
        return Duration.between(start, finish).toMillis();
    }

    private int getNumberOfFilesInPath(Path path){
        return path.toFile().listFiles().length;
    }

    public List<String> getBackupsNames(){
        List<String> list = new ArrayList<>();
        if(path.toFile().listFiles() == null || path.toFile().listFiles().length == 0) return list;
        for(File file : path.toFile().listFiles()){
            list.add(file.getName());
        }
        return list;
    }
}
