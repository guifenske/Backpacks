package br.com.backpacks;

import br.com.backpacks.backup.BackupHandler;
import br.com.backpacks.backup.ScheduledBackupService;
import br.com.backpacks.storage.MySQLProvider;
import br.com.backpacks.storage.YamlProvider;

import java.util.concurrent.TimeUnit;

public class Config {
    public static boolean getBoolean(String path){
        return Main.getMain().getConfig().getBoolean(path);
    }

    public static int getInt(String path){
        return Main.getMain().getConfig().getInt(path);
    }

    public static String getString(String path){
        return Main.getMain().getConfig().getString(path);
    }

    public static BackupHandler getBackupHandler(){
        if(!Config.getBoolean("autobackup.enabled")) return null;
        ScheduledBackupService scheduledBackupService = new ScheduledBackupService();
        if(Config.getInt("autobackup.interval") > 0){
            scheduledBackupService.setInterval(Config.getInt("autobackup.interval"));
        }   else{
            scheduledBackupService.setInterval(-1);
            Main.getMain().getLogger().warning("Invalid interval for autobackup, please use a number greater than 0.");
        }

        if(Config.getString("autobackup.type") != null){
            try{
                scheduledBackupService.setType(TimeUnit.valueOf(Config.getString("autobackup.type")));
            }   catch (IllegalArgumentException e){
                Main.getMain().getLogger().warning("Invalid type for autobackup, please use MINUTES | HOURS | SECONDS.");
            }
        }   else{
            Main.getMain().getLogger().warning("Invalid type for autobackup, please use MINUTES | HOURS | SECONDS.");
        }
        int keep = 0;
        if(Config.getInt("autobackup.keep") > 0){
            keep = Config.getInt("autobackup.keep");
        }   else{
            Main.getMain().getLogger().warning("Invalid keep for autobackup, please use a number greater than 0.");
        }
        BackupHandler backupHandler = new BackupHandler(keep);
        backupHandler.setScheduledBackupService(scheduledBackupService);
        return backupHandler;
    }

    public static AutoSaveManager getAutoSaveManager(){
        if(!Config.getBoolean("autosave.enabled")) return null;
        AutoSaveManager autoSaveManager = new AutoSaveManager();
        if(Config.getInt("autosave.interval") > 0){
            autoSaveManager.setInterval(Config.getInt("autosave.interval"));
        }   else{
            Main.getMain().getLogger().warning("Invalid interval for autosave, please use a number greater than 0.");
            autoSaveManager.setInterval(-1);
        }

        if(Config.getString("autosave.type") != null){
            try{
                TimeUnit.valueOf(Config.getString("autosave.type"));
            }   catch (IllegalArgumentException e){
                Main.getMain().getLogger().warning("Invalid type for autosave, please use MINUTES | HOURS | SECONDS.");
            }
            autoSaveManager.setType(TimeUnit.valueOf(Config.getString("autosave.type")));
        }   else{
            Main.getMain().getLogger().warning("Invalid type for autosave, please use MINUTES | HOURS | SECONDS.");
        }
        return autoSaveManager;
    }

    public static MySQLProvider getMySQLProvider(){
        if(!Config.getBoolean("mysql.enabled")) return null;
        return new MySQLProvider(Config.getString("mysql.url"), Config.getString("mysql.username"), Config.getString("mysql.password"));
    }

    public static YamlProvider getYamlProvider(){
        return new YamlProvider(Main.getMain().getDataFolder().getAbsolutePath() + "/backpacks.yml", Main.getMain().getDataFolder().getAbsolutePath() + "/upgrades.yml");
    }
}