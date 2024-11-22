package br.com.backpacks.storage;

import br.com.backpacks.AutoSaveManager;
import br.com.backpacks.Config;
import br.com.backpacks.Main;
import br.com.backpacks.backup.BackupHandler;
import br.com.backpacks.utils.UpgradeManager;
import br.com.backpacks.utils.backpacks.BackPack;
import br.com.backpacks.utils.scheduler.TickComponent;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

public class StorageManager {
    private static StorageProvider provider;

    public static StorageProvider getProvider() {
        return provider;
    }

    public static void setProvider(StorageProvider provider) {
        StorageManager.provider = provider;
    }

    public static void saveAll(boolean async) {
        if(provider == null || (Main.backPackManager.getBackpacks().isEmpty() && UpgradeManager.getUpgrades().isEmpty())){

            Main.getMain().saveComplete = true;
            synchronized (Main.getMain().lock){
                Main.getMain().lock.notifyAll();
            }

            return;
        }

        if(!async){
            try {
                provider.saveBackpacks();
                provider.saveUpgrades();

                Main.getMain().saveConfig();

                Main.getMain().saveComplete = true;
                synchronized (Main.getMain().lock){
                    Main.getMain().lock.notifyAll();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }

        Main.getMain().getTickManager().runComponentAsync(new TickComponent(0) {
            @Override
            public void tick() {
                try {
                    provider.saveBackpacks();
                    provider.saveUpgrades();

                    Main.getMain().saveConfig();

                    Main.getMain().saveComplete = true;
                    synchronized (Main.getMain().lock){
                        Main.getMain().lock.notifyAll();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void loadAll() {
        provider.loadUpgrades();
        provider.loadBackpacks();

        BackupHandler backupHandler = Config.getBackupHandler();

        if(backupHandler != null){
            backupHandler.getScheduledBackupService().start();
        }

        Main.getMain().setBackupHandler(backupHandler);

        AutoSaveManager autoSaveManager = Config.getAutoSaveManager();
        if(autoSaveManager != null){
            autoSaveManager.start();
        }

        Main.getMain().setAutoSaveManager(autoSaveManager);

        for(BackPack backPack : Main.backPackManager.getBackpacks().values()){
            if(backPack.isShowingNameAbove()){
                for(Entity entity : backPack.getLocation().getChunk().getEntities()){
                    if(entity instanceof ArmorStand){
                        if(entity.getLocation().subtract(0, 1, 0).getBlock().getLocation().equals(backPack.getLocation())){
                            backPack.setMarker(entity.getUniqueId());
                            break;
                        }
                    }
                }
            }
        }

        Instant finish = Instant.now();

        Main.getMain().getLogger().info("Hello from Backpacks! " + Duration.between(Main.getMain().start, finish).toMillis() + "ms");
    }
}
