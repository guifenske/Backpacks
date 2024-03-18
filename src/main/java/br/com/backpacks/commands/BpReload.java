package br.com.backpacks.commands;

import br.com.backpacks.AutoSaveManager;
import br.com.backpacks.Config;
import br.com.backpacks.Main;
import br.com.backpacks.backup.BackupHandler;
import br.com.backpacks.utils.Constants;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class BpReload implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length > 0) return false;
        if(sender instanceof Player player){
            if(!player.isOp()){
                player.sendMessage(Main.PREFIX + "§cYou don't have permission to use this command");
                return true;
            }
        }

        Main.getMain().saveDefaultConfig();
        Main.getMain().reloadConfig();
        Constants.DEBUG_MODE = Config.getBoolean("debug");
        Constants.CATCH_BACKPACK = Config.getBoolean("fish_backpack");
        Constants.MONSTER_DROPS_BACKPACK = Config.getBoolean("kill_monster_backpack");

        BackupHandler backupHandler = Config.getBackupHandler();
        if(backupHandler == null && Main.getMain().getBackupHandler() != null){
            Main.getMain().getBackupHandler().getScheduledBackupService().cancel();
            Main.getMain().setBackupHandler(null);
        }   else if(backupHandler != null && Main.getMain().getBackupHandler() == null){
            backupHandler.getScheduledBackupService().start();
            Main.getMain().setBackupHandler(backupHandler);
        }
        if(backupHandler != null && Main.getMain().getBackupHandler() != null){
            Main.getMain().getBackupHandler().getScheduledBackupService().cancel();
            backupHandler.getScheduledBackupService().start();
            Main.getMain().setBackupHandler(backupHandler);
        }

        AutoSaveManager autoSaveManager = Config.getAutoSaveManager();
        if(autoSaveManager == null && Main.getMain().getAutoSaveManager() != null){
            Main.getMain().getAutoSaveManager().cancel();
            Main.getMain().setAutoSaveManager(null);
        }   else if(autoSaveManager != null && Main.getMain().getAutoSaveManager() == null){
            autoSaveManager.start();
            Main.getMain().setAutoSaveManager(autoSaveManager);
        }
        if(autoSaveManager != null && Main.getMain().getAutoSaveManager() != null){
            Main.getMain().getAutoSaveManager().cancel();
            autoSaveManager.start();
            Main.getMain().setAutoSaveManager(autoSaveManager);
        }

        sender.sendMessage(Main.PREFIX + "§aConfig reloaded successfully.");

        return true;
    }
}
