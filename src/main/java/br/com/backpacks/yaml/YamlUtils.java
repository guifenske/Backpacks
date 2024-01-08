package br.com.backpacks.yaml;

import br.com.backpacks.Main;
import br.com.backpacks.backpackUtils.BackPack;
import br.com.backpacks.backpackUtils.Upgrade;
import de.leonhard.storage.Json;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public final class YamlUtils {

    public static void save_backpacks_yaml() {
        Json config = new Json("stored_backpacks.json", Main.getMain().getDataFolder().getAbsolutePath());

        for (BackPack backPack : Main.backPackManager.getBackpacks().values()) {
            config.set(backPack.getId() + ".i", backPack.serialize());
            config.set(backPack.getId() + ".1", backPack.serializeStorageContents(1));
            if (backPack.getUpgrades() != null) {
                config.set(backPack.getId() + ".u", backPack.serializeUpgrades());
                if (backPack.containsUpgrade(Upgrade.FURNACE)) {
                    config.set(backPack.getId() + ".furnace.f", backPack.getFuel());
                    config.set(backPack.getId() + ".furnace.s", backPack.getSmelting());
                    config.set(backPack.getId() + ".furnace.r", backPack.getResult());
                }
            }
            if (backPack.getSecondPageSize() > 0) {
                config.set(backPack.getId() + ".2", backPack.serializeStorageContents(2));
            }
        }
    }

    public static void loadBackpacksYaml() {
        Json config = new Json("stored_backpacks.json", Main.getMain().getDataFolder().getAbsolutePath());

        for (String i : config.keySet()) {
            BackPack backPack = new BackPack().deserialize(config, i);
            backPack.setIsBlock(false);
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

    public static void savePlacedBackpacks() {
        if (Main.backPackManager.getBackpacksPlacedLocations().isEmpty()) {
            return;
        }

        Path filePath = Paths.get(Main.getMain().getDataFolder().getAbsolutePath(), "cached_backpacks_loc.yml");
        YamlConfiguration config = new YamlConfiguration();

        for (Location location : Main.backPackManager.getBackpacksPlacedLocations().keySet()) {
            BackPack backPack = Main.backPackManager.getBackpacksPlacedLocations().get(location);
            List<String> data = serializeLocation(location);
            config.set(backPack.getId() + ".loc", data);
            config.set(backPack.getId() + ".i", backPack.serialize());
            config.set(backPack.getId() + ".1", backPack.serializeStorageContents(1));
            if (backPack.getUpgrades() != null) {
                if (backPack.containsUpgrade(Upgrade.FURNACE)) {
                    config.set(backPack.getId() + ".furnace.f", backPack.getFuel());
                    config.set(backPack.getId() + ".furnace.s", backPack.getSmelting());
                    config.set(backPack.getId() + ".furnace.r", backPack.getResult());
                }
                config.set(backPack.getId() + ".u", backPack.serializeUpgrades());
            }
            if (backPack.getSecondPage() != null) {
                config.set(backPack.getId() + ".2", backPack.serializeStorageContents(2));
            }
        }
        try {
            config.save(filePath.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void loadPlacedBackpacks(){
        Json config = new Json("stored_backpacks.json", Main.getMain().getDataFolder().getAbsolutePath());

        for (String i : config.keySet()) {
            BackPack backPack = new BackPack().deserialize(config, i);
            backPack.setIsBlock(true);
            Location location = deserializeLocation((List<String>) config.getList(i + ".loc"));
            Main.backPackManager.getBackpacksPlacedLocations().put(location, backPack);
        }
    }
}