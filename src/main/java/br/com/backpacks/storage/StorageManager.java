package br.com.backpacks.storage;

import br.com.backpacks.AutoSaveManager;
import br.com.backpacks.utils.Config;
import br.com.backpacks.Main;
import br.com.backpacks.recipes.BackpackRecipes;
import br.com.backpacks.upgrades.UpgradeManager;
import br.com.backpacks.backpack.Backpack;
import br.com.backpacks.scheduler.TickComponent;
import org.bukkit.block.Barrel;
import org.bukkit.persistence.PersistentDataType;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

public class StorageManager {
    private static StorageProvider provider;

    public static StorageProvider getProvider() {
        return provider;
    }

    public static void setProvider(StorageProvider provider) {
        StorageManager.provider = provider;
    }

    public static void saveAll(boolean async) {
        if(provider == null || (Main.backpackManager.getBackpacks().isEmpty() && UpgradeManager.getUpgrades().isEmpty())){

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

        Main.getMain().getTickManager().runComponentAsync(new TickComponent(()->{
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
        }));
    }

    public static void loadAll() {
        provider.loadUpgrades();
        provider.loadBackpacks();

        AutoSaveManager autoSaveManager = Config.getAutoSaveManager();
        if(autoSaveManager != null){
            autoSaveManager.start();
        }

        Main.getMain().setAutoSaveManager(autoSaveManager);

        for(Backpack backpack : Main.backpackManager.getBackpacks().values()){
            if(backpack.isShowingNameAbove()){
                Barrel backpackBlock = (Barrel) backpack.getLocation().getBlock().getState();
                UUID markerId = UUID.fromString(backpackBlock.getPersistentDataContainer().get(BackpackRecipes.NAMESPACE_BACKPACK_MARKER_ID, PersistentDataType.STRING));

                backpack.setMarker(markerId);
            }
        }

        Instant finish = Instant.now();

        Main.getMain().getLogger().info("Hello from Backpacks! " + Duration.between(Main.getMain().start, finish).toMillis() + "ms");
    }
}
