package br.com.backpacks.backupHandler;

import br.com.backpacks.Main;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static br.com.backpacks.Main.lock;
import static br.com.backpacks.Main.saveComplete;

public class BackupHandler {
    private int keepBackups = 0;

    public String getPath() {
        return path;
    }

    private final String path;

    public BackupHandler(int keepBackups, String path) {
        this.keepBackups = keepBackups;
        this.path = path;
    }

    public boolean removeBackup(String name){
        File file = new File(path + "/" + name);
        return file.delete();
    }

    public void backup(String path) throws IOException, InvalidConfigurationException {
        Path source;
        try{
            source = Path.of(path);
        }   catch (Exception e){
            Main.getMain().getSLF4JLogger().error("Invalid path for backup, please use this syntax: /path/to/backup/folder");
            Main.getMain().getSLF4JLogger().error("Shutting down plugin...");

            Main.getMain().getThreadBackpacks().cancelAllTasks();

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
            Main.getMain().getThreadBackpacks().getExecutor().shutdownNow();
            Main.getMain().getServer().getPluginManager().disablePlugin(Main.getMain());
            return;
        }
        Instant start = Instant.now();
        source.toFile().mkdir();

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
        Instant finish = Instant.now();
        long time = Duration.between(start, finish).toMillis();
        Main.getMain().debugMessage("Backup completed in " + time + " ms!");
        if(getNumberOfFilesInPath(source) > keepBackups){
            List<String> names = new ArrayList<>();
            File[] files = source.toFile().listFiles();
            int index = 0;
            long oldestDate = files[0].lastModified();
            //remove the oldest file
            for(int i = 0; i < files.length; i++){
                names.add(files[i].getName());
                long currentLastModified = files[i].lastModified();
                if(currentLastModified < oldestDate){
                    oldestDate = currentLastModified;
                    index = i;
                    names.remove(i);
                }
            }
            files[index].delete();
        }
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
