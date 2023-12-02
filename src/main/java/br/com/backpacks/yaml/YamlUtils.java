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
            config.set(backPack.getBackpack_id() + ".i", backPack.serialize());
            config.set(backPack.getBackpack_id() + ".1", backPack.getFirst_page().getStorageContents());

            if(backPack.get_Second_page_size() > 0){
                config.set(backPack.getBackpack_id() + ".2", backPack.getSecond_page().getStorageContents());
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
            Main.backPackManager.getBackpacks_ids().add(backPack.getBackpack_id());
            Main.backPackManager.getBackpacks().put(backPack.getBackpack_id(), backPack);
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
        if(Main.backPackManager.getBackpacks_placed_locations().isEmpty()) return;

        File file = new File(Main.getMain().getDataFolder().getCanonicalFile().getAbsolutePath() + "/cached_backpacks_loc.yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        for(Location location : Main.backPackManager.getBackpacks_placed_locations().keySet()){
            BackPack backPack = Main.backPackManager.getBackpacks_placed_locations().get(location);
            List<String> data = serializeLocation(location);
            config.set(backPack.getBackpack_id() + ".loc", data);
            config.set(backPack.getBackpack_id() + ".i", backPack.serialize());
            config.set(backPack.getBackpack_id() + ".1", backPack.getFirst_page().getStorageContents());

            if(backPack.getSecond_page() != null){
                config.set(backPack.getBackpack_id() + ".2", backPack.getSecond_page().getStorageContents());
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
            Main.backPackManager.getBackpacks_ids().add(backPack.getBackpack_id());
            Location location = deserializeLocation((List<String>) config.getList(i + ".loc"));
            Main.backPackManager.getBackpacks_placed_locations().put(location, backPack);
        }

        if (!file.delete()) {
            Main.getMain().getLogger().warning("Failed to delete cached_locs.yml");
        }

    }

}