package br.com.backpacks.backup;

import br.com.backpacks.Config;
import br.com.backpacks.Main;
import br.com.backpacks.storage.StorageManager;
import br.com.backpacks.storage.YamlProvider;
import br.com.backpacks.utils.BackPack;
import br.com.backpacks.utils.Upgrade;
import br.com.backpacks.utils.UpgradeManager;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Barrel;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.ItemStack;

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
    private ConcurrentHashMap<Integer, BackPack> rollbackBackpack;
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
            files[index].delete();
        }
    }

    public long undoRollback() throws IOException {
        if(rollbackUpgrade == null) return -1;
        if(rollbackBackpack == null) return -1;
        Instant start = Instant.now();

        StorageManager.getProvider().loadUpgrades(rollbackUpgrade);
        StorageManager.getProvider().loadBackpacks(rollbackBackpack);

        Bukkit.getScheduler().runTask(Main.getMain(), ()->{
            for(Map.Entry<Location, Integer> entry : Main.backPackManager.getBackpacksPlacedLocations().entrySet()){
                BackPack backPack = Main.backPackManager.getBackpackFromId(entry.getValue());
                if(backPack.isShowingNameAbove()){
                    ArmorStand marker = (ArmorStand) entry.getKey().getWorld().spawnEntity(backPack.getLocation().clone().add(0, 1, 0).toCenterLocation(), EntityType.ARMOR_STAND, CreatureSpawnEvent.SpawnReason.CUSTOM);
                    marker.setVisible(false);
                    marker.setSmall(true);
                    marker.customName(Component.text(backPack.getName()));
                    marker.setCustomNameVisible(true);
                    marker.setCanTick(false);
                    marker.setCanMove(false);
                    marker.setCollidable(false);
                    marker.setInvulnerable(true);
                    marker.setBasePlate(false);
                    marker.setMarker(true);
                    backPack.setMarker(marker.getUniqueId());
                }
                entry.getKey().getBlock().setType(Material.BARREL);
                Barrel barrel = (Barrel) entry.getKey().getBlock().getState();
                barrel.update();
                barrel.getInventory().setItem(0, new ItemStack(Material.STICK));
            }
        });

        rollbackBackpack = null;
        rollbackUpgrade = null;

        Instant finish = Instant.now();
        Main.backPackManager.setCanBeOpen(true);
        return Duration.between(start, finish).toMillis();
    }

    public long restoreBackup(String path) throws IOException {
        try{
            Path.of(path);
        }   catch (Exception e){
            Main.backPackManager.setCanBeOpen(true);
            Main.getMain().getLogger().severe("Cannot restore backup, invalid path or file not found.");
            Main.getMain().getLogger().severe("Aborting restore task...");
            return -1L;
        }

        Instant start = Instant.now();
        rollbackBackpack = new ConcurrentHashMap<>(Main.backPackManager.getBackpacks());
        rollbackUpgrade = new ConcurrentHashMap<>(UpgradeManager.getUpgrades());
        YamlProvider yamlProvider = Config.getYamlProvider();

        File backpacksOriginal = new File(Main.getMain().getDataFolder().getAbsolutePath() + "/backpacks.yml");
        File upgradesOriginal = new File(Main.getMain().getDataFolder().getAbsolutePath() + "/upgrades.yml");
        backpacksOriginal.delete();
        upgradesOriginal.delete();
        ZipUtils.unzipAll(this.path + "/" + path);

        Main.backPackManager.getBackpacksPlacedLocations().clear();
        Main.backPackManager.getBackpacks().clear();
        UpgradeManager.getUpgrades().clear();

        yamlProvider.loadUpgrades();
        yamlProvider.loadBackpacks();

        Bukkit.getScheduler().runTask(Main.getMain(), ()->{
            for(Map.Entry<Location, Integer> entry : Main.backPackManager.getBackpacksPlacedLocations().entrySet()){
                BackPack backPack = Main.backPackManager.getBackpackFromId(entry.getValue());
                if(backPack.isShowingNameAbove()){
                    ArmorStand marker = (ArmorStand) entry.getKey().getWorld().spawnEntity(backPack.getLocation().clone().add(0, 1, 0).toCenterLocation(), EntityType.ARMOR_STAND, CreatureSpawnEvent.SpawnReason.CUSTOM);
                    marker.setVisible(false);
                    marker.setSmall(true);
                    marker.customName(Component.text(backPack.getName()));
                    marker.setCustomNameVisible(true);
                    marker.setCanTick(false);
                    marker.setCanMove(false);
                    marker.setCollidable(false);
                    marker.setInvulnerable(true);
                    marker.setBasePlate(false);
                    marker.setMarker(true);
                    backPack.setMarker(marker.getUniqueId());
                }
                entry.getKey().getBlock().setType(Material.BARREL);
                Barrel barrel = (Barrel) entry.getKey().getBlock().getState();
                barrel.update();
                barrel.getInventory().setItem(0, new ItemStack(Material.STICK));
            }
        });

        Main.backPackManager.setCanBeOpen(true);
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
