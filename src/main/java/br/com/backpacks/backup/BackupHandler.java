package br.com.backpacks.backup;

import br.com.backpacks.Main;
import br.com.backpacks.yaml.YamlUtils;
import org.bukkit.configuration.InvalidConfigurationException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class BackupHandler {
    public void setKeepBackups(int keepBackups) {
        this.keepBackups = keepBackups;
    }

    private int keepBackups;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    private String path;

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    private boolean enabled = true;

    public BackupHandler(int keepBackups, String path) {
        this.keepBackups = keepBackups;
        this.path = path;
    }

    public boolean removeBackup(String name){
        File file = new File(path + "/" + name);
        return file.delete();
    }

    public long backup(String path) throws IOException, InvalidConfigurationException {
        if(!enabled) return -1L;
        Path source;
        try{
            source = Path.of(path);
        }   catch (Exception e){
            Main.getMain().getSLF4JLogger().error("Invalid path for backup, please use this syntax: /path/to/backup/folder");
            Main.getMain().getLogger().severe("Aborting backup task...");
            return -1L;
        }
        Instant start = Instant.now();
        source.toFile().mkdir();

        File backpackFile = new File(path + "/backpacks.yml");
        File upgradeFile = new File(path + "/upgrades.yml");
        YamlUtils.saveBackpacks(path + "/backpacks.yml");
        YamlUtils.saveUpgrades(path + "/upgrades.yml");

        ZipUtils.zipAll(backpackFile.toPath(), upgradeFile.toPath(), path);
        while(getNumberOfFilesInPath(source) > keepBackups){
            File[] files = source.toFile().listFiles();
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
        Instant finish = Instant.now();
        long time = Duration.between(start, finish).toMillis();
        Main.getMain().debugMessage("Backup completed in " + time + " ms!");
        return time;
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
        File backpacksOriginal = new File(Main.getMain().getDataFolder().getAbsolutePath() + "/backpacks.yml");
        File upgradesOriginal = new File(Main.getMain().getDataFolder().getAbsolutePath() + "/upgrades.yml");
        backpacksOriginal.delete();
        upgradesOriginal.delete();

        ZipUtils.unzipAll(this.path + "/" + path);

        Main.backPackManager.getBackpacksPlacedLocations().clear();
        Main.backPackManager.getBackpacks().clear();
        Main.backPackManager.getUpgradeHashMap().clear();

        YamlUtils.loadUpgrades();
        YamlUtils.loadBackpacks();

        Main.backPackManager.setCanBeOpen(true);
        Instant finish = Instant.now();
        return Duration.between(start, finish).toMillis();
    }

    private int getNumberOfFilesInPath(Path path){
        return path.toFile().listFiles().length;
    }

    public List<String> getBackupsNames(){
        List<String> list = new ArrayList<>();
        if(Path.of(path).toFile().listFiles() == null || Path.of(path).toFile().listFiles().length == 0) return list;
        for(File file : Path.of(path).toFile().listFiles()){
            list.add(file.getName());
        }
        return list;
    }
}
