package br.com.backpacks.backupHandler;

import br.com.backpacks.Main;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import static br.com.backpacks.Main.lock;
import static br.com.backpacks.Main.saveComplete;

public class BackupHandler {
    private int keepBackups = 0;

    public BackupHandler(int keepBackups) {
        this.keepBackups = keepBackups;
    }


    public void backup(String path) throws IOException, InvalidConfigurationException {
        Path source;
        try{
            source = Path.of(path);
        }   catch (Exception e){
            Main.getMain().getSLF4JLogger().error("Invalid path for backup, please use this syntax: /path/to/backup/folder");
            Main.getMain().getSLF4JLogger().error("Shutting down plugin...");
            try {
                Main.getMain().getThreadBackpacks().saveAll();
            } catch (IOException exception) {
                throw new RuntimeException(exception);
            }
            synchronized (lock) {
                while (!saveComplete) {
                    try {
                        lock.wait();
                    } catch (InterruptedException exception) {
                        throw new RuntimeException(exception);
                    }
                }
            }
            Main.getMain().getServer().getPluginManager().disablePlugin(Main.getMain());
            return;
        }

        source.toFile().mkdir();

        Main.getMain().debugMessage("Starting backup...");
        File backpackFile = new File(path + "/backpacks.yml");
        File upgradeFile = new File(path + "/upgrades.yml");
        YamlConfiguration yaml = new YamlConfiguration();
        YamlConfiguration yaml2 = new YamlConfiguration();
        File backpacks = new File(Main.getMain().getDataFolder().getAbsolutePath() + "/backpacks.yml");
        File upgrades = new File(Main.getMain().getDataFolder().getAbsolutePath() + "/upgrades.yml");
        if(!backpacks.exists()) backpacks.createNewFile();
        if(!upgrades.exists()) upgrades.createNewFile();
        yaml.load(backpacks);
        yaml.save(backpackFile);
        yaml2.load(upgrades);
        yaml2.save(upgradeFile);

        ZipUtils.zipAll(backpackFile.toPath(), upgradeFile.toPath(), path);
        Main.getMain().debugMessage("Backup complete!");
        if(getNumberOfFilesInPath(source) > keepBackups){
            File[] files = source.toFile().listFiles();
            int index = 0;
            long oldestDate = files[0].lastModified();
            //remove the oldest file
            for(int i = 0; i < files.length; i++){
                long creationDate = Long.parseLong(files[i].getName().split("-")[1].split(".zip")[0]);
                if(creationDate < oldestDate){
                    oldestDate = creationDate;
                    index = i;
                }
            }
            files[index].delete();
        }
    }

    private int getNumberOfFilesInPath(Path path){
        return path.toFile().listFiles().length;
    }

}
