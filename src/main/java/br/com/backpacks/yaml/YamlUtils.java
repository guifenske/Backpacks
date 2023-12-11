package br.com.backpacks.yaml;


import br.com.backpacks.Main;
import br.com.backpacks.backpackUtils.BackPack;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class YamlUtils {

    public static void save_backpacks_yaml() throws IOException {
        File file = new File(Main.getMain().getDataFolder().getCanonicalFile().getAbsolutePath() + "/stored_backpacks.yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        for(BackPack backPack : Main.backPackManager.getBackpacks().values()){
            config.set(backPack.getId() + ".i", backPack.serialize());
            config.set(backPack.getId() + ".1", backPack.getStorageContentsFirstPage());
            if(backPack.getUpgrades() != null)  config.set(backPack.getId() + ".u", backPack.serializeUpgrades());

            if(backPack.getSecondPageSize() > 0){
                config.set(backPack.getId() + ".2", backPack.getStorageContentsSecondPage());
            }

        }
        config.save(file);
    }

    public static void loadBackpacksYaml() throws IOException {
        File file = new File(Main.getMain().getDataFolder().getCanonicalFile().getAbsolutePath() + "/stored_backpacks.yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        if(!file.exists()) return;
        if(file.length() == 0L) return;

        for(String i : config.getKeys(false)){
            BackPack backPack = new BackPack().deserialize(config, i);
            backPack.setIsBlock(false);
            Main.backPackManager.getBackpacks().put(backPack.getId(), backPack);
        }

        if (!file.delete()) {
            Main.getMain().getLogger().warning("Failed to delete stored_backpacks.yml");
        }
    }

    private static List<String> serializeLocation(Location location){
        List<String> data = new ArrayList<>();
        data.add(location.getWorld().getName());
        data.add(String.valueOf(location.getX()));
        data.add(String.valueOf(location.getY()));
        data.add(String.valueOf(location.getZ()));
        return data;
    }

    private static Location deserializeLocation(List<String> oldData){
        String world = oldData.get(0);
        double x = Double.parseDouble(oldData.get(1));
        double y = Double.parseDouble(oldData.get(2));
        double z = Double.parseDouble(oldData.get(3));

        return new Location(Bukkit.getWorld(world), x, y, z);
    }

    public static void savePlacedBackpacks() throws IOException {
        if(Main.backPackManager.getBackpacksPlacedLocations().isEmpty()) return;

        File file = new File(Main.getMain().getDataFolder().getCanonicalFile().getAbsolutePath() + "/cached_backpacks_loc.yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        for(Location location : Main.backPackManager.getBackpacksPlacedLocations().keySet()){
            BackPack backPack = Main.backPackManager.getBackpacksPlacedLocations().get(location);
            List<String> data = serializeLocation(location);
            config.set(backPack.getId() + ".loc", data);
            config.set(backPack.getId() + ".i", backPack.serialize());
            config.set(backPack.getId() + ".1", backPack.getStorageContentsFirstPage());
            if(backPack.getUpgrades() != null)  config.set(backPack.getId() + ".u", backPack.serializeUpgrades());

            if(backPack.getSecondPage() != null){
                config.set(backPack.getId() + ".2", backPack.getStorageContentsSecondPage());
            }

        }
        config.save(file);
    }

    public static void loadPlacedBackpacks() throws IOException {
        File file = new File(Main.getMain().getDataFolder().getCanonicalFile().getAbsolutePath() + "/cached_backpacks_loc.yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        if(!file.exists()) return;
        if(file.length() == 0L) return;

        for(String i : config.getKeys(false)){
            BackPack backPack = new BackPack().deserialize(config, i);
            backPack.setIsBlock(true);
            Location location = deserializeLocation((List<String>) config.getList(i + ".loc"));
            Main.backPackManager.getBackpacksPlacedLocations().put(location, backPack);
        }

        if (!file.delete()) {
            Main.getMain().getLogger().warning("Failed to delete cached_locs.yml");
        }
    }

}