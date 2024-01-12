package br.com.backpacks.yaml;

import br.com.backpacks.Main;
import br.com.backpacks.backpackUtils.BackPack;
import br.com.backpacks.backpackUtils.Upgrade;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class YamlUtils {

    public static void save_backpacks_yaml() throws IOException {
        File file = new File(Main.getMain().getDataFolder().getAbsolutePath() + "/backpacks.yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        for (BackPack backPack : Main.backPackManager.getBackpacks().values()) {
            if(backPack.getLocation() != null){
                List<String> data = serializeLocation(backPack.getLocation());
                config.set(backPack.getId() + ".loc", data);
            }
            config.set(backPack.getId() + ".i", backPack.serialize());
            saveStorageContents(backPack, config);
            if (backPack.getUpgrades() != null) {
                config.set(backPack.getId() + ".u", backPack.serializeUpgrades());
                if (backPack.containsUpgrade(Upgrade.FURNACE)) {
                    config.set(backPack.getId() + ".furnace.f", backPack.getFuel());
                    config.set(backPack.getId() + ".furnace.s", backPack.getSmelting());
                    config.set(backPack.getId() + ".furnace.r", backPack.getResult());
                }
                if(backPack.containsUpgrade(Upgrade.JUKEBOX)){
                    config.set(backPack.getId() + ".jukebox.discs", backPack.serializeDiscs());
                    config.set(backPack.getId() + ".jukebox.playing", backPack.getPlaying().getType().name());
                }
                if(backPack.containsUpgrade(Upgrade.AUTOFEED)){
                    config.set(backPack.getId() + ".afeed.enabled", backPack.isAutoFeedEnabled());
                    config.set(backPack.getId() + ".afeed.items", backPack.getAutoFeedItems());
                }
            }
        }

        config.save(file);
    }

    public static void loadBackpacksYaml() {
        File file = new File(Main.getMain().getDataFolder().getAbsolutePath() + "/backpacks.yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        for (String i : config.getKeys(false)) {
            BackPack backPack = new BackPack().deserialize(config, i);
            backPack.setIsBlock(false);
            if(config.isSet(i + ".loc")){
                backPack.setLocation(deserializeLocation(config.getStringList(i + ".loc")));
                Main.backPackManager.getBackpacksPlacedLocations().put(backPack.getLocation(), backPack.getId());
            }
            Main.getMain().debugMessage("Loading backpack " + backPack.getName() + " with id " + backPack.getId(), "info");
            Main.backPackManager.getBackpacks().put(backPack.getId(), backPack);
        }
    }

    private static List<String> serializeLocation(Location location) {
        List<String> data = new ArrayList<>();
        data.add(location.getWorld().getName());
        data.add(String.valueOf(location.getX()));
        data.add(String.valueOf(location.getY()));
        data.add(String.valueOf(location.getZ()));
        return data;
    }

    private static Location deserializeLocation(List<String> oldData) {
        String world = oldData.get(0);
        double x = Double.parseDouble(oldData.get(1));
        double y = Double.parseDouble(oldData.get(2));
        double z = Double.parseDouble(oldData.get(3));

        return new Location(Bukkit.getServer().getWorld(world), x, y, z);
    }

    private static void saveStorageContents(BackPack backPack, YamlConfiguration config){
        int i = 0;
        int i1 = 0;
        config.set(backPack.getId() + ".1", null);
        for(ItemStack itemStack : backPack.getStorageContentsFirstPage()){
            if(itemStack == null){
                i++;
                continue;
            }

            config.set(backPack.getId() + ".1." + i, itemStack);
            i++;
        }

        if(backPack.getSecondPage() != null){
            config.set(backPack.getId() + ".2", null);
            for(ItemStack itemStack : backPack.getStorageContentsSecondPage()){
                if(itemStack == null){
                    i1++;
                    continue;
                }

                config.set(backPack.getId() + ".2." + i1, itemStack);
                i1++;
            }
        }
    }

}