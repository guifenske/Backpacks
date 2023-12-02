package br.com.backpacks.yaml;


import br.com.backpacks.Main;
import br.com.backpacks.backpackUtils.BackPack;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Map;

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

        for(String i : config.getKeys(false)){
            BackPack backPack = new BackPack().deserialize(config, i);
            Main.backPackManager.getBackpacks_ids().add(backPack.getBackpack_id());
            Main.backPackManager.getBackpacks().put(backPack.getBackpack_id(), backPack);
        }

        if (!file.delete()) {
            Main.getMain().getLogger().warning("Failed to delete stored_backpacks.yml");
        }


    }

    public static void savePlacedBackpacks() throws IOException {
        File file = new File(Main.getMain().getDataFolder().getCanonicalFile().getAbsolutePath() + "/cached_backpacks_loc.yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);


        for(Location location : Main.backPackManager.getBackpacks_placed_locations().keySet()){
            BackPack backPack = Main.backPackManager.getBackpacks_placed_locations().get(location);
            config.set(location.serialize() + ".i", backPack.serialize());
            config.set(location.serialize() + ".1", backPack.getFirst_page().getStorageContents());

            if(backPack.getSecond_page() != null){
                config.set(location.serialize() + ".2", backPack.getSecond_page().getStorageContents());
            }

        }
        config.save(file);
    }

    public static void loadPlacedBackpacks() throws IOException {
        File file = new File(Main.getMain().getDataFolder().getCanonicalFile().getAbsolutePath() + "/cached_backpacks_loc.yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        if(file.length() == 0L) return;

        for(String i : config.getKeys(false)){
            Map<String, Object> args = (Map<String, Object>) config.get(i);
            if(args == null){
                Main.getMain().getLogger().warning("cached_backpacks_loc.yml has no data for " + i + ", skipping..");
                continue;
            }

            Location location = Location.deserialize(args);
            BackPack backPack = new BackPack().deserialize(config, i);
            Main.backPackManager.getBackpacks_placed_locations().put(location, backPack);
        }

    }

}