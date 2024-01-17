package br.com.backpacks.yaml;

import br.com.backpacks.Main;
import br.com.backpacks.backpackUtils.BackPack;
import br.com.backpacks.backpackUtils.Upgrade;
import br.com.backpacks.backpackUtils.UpgradeType;
import br.com.backpacks.upgrades.*;
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
            if (backPack.getUpgrades() != null && !backPack.getUpgrades().isEmpty()) {
                serializeUpgrades(config, backPack);
                if (backPack.containsUpgradeType(UpgradeType.FURNACE)) {
                    List<Upgrade> list = backPack.getUpgradesFromType(UpgradeType.FURNACE);
                    for(Upgrade upgrade : list){
                        Main.getMain().debugMessage("Saving furnace upgrade " + upgrade.getId(), "info");
                        FurnaceUpgrade furnaceUpgrade = (FurnaceUpgrade) upgrade;
                        if(furnaceUpgrade.getResult() != null)  config.set(backPack.getId() + ".furnace." + upgrade.getId() + ".result", furnaceUpgrade.getResult());
                        if(furnaceUpgrade.getFuel() != null)  config.set(backPack.getId() + ".furnace." + upgrade.getId() + ".fuel", furnaceUpgrade.getFuel());
                        if(furnaceUpgrade.getSmelting() != null)  config.set(backPack.getId() + ".furnace." + upgrade.getId() + ".smelting", furnaceUpgrade.getSmelting());
                    }
                }
                if(backPack.containsUpgradeType(UpgradeType.JUKEBOX)){
                    List<Upgrade> list = backPack.getUpgradesFromType(UpgradeType.JUKEBOX);
                    JukeboxUpgrade upgrade = (JukeboxUpgrade) list.get(0);
                    Main.getMain().debugMessage("Saving jukebox upgrade " + upgrade.getId(), "info");

                    if(upgrade.getDiscs() != null && !upgrade.getDiscs().isEmpty()) config.set(backPack.getId() + ".jukebox." + upgrade.getId() + ".discs", upgrade.serializeDiscs());
                    if(upgrade.getPlaying() != null)    config.set(backPack.getId() + ".jukebox." + upgrade.getId() + ".playing", upgrade.getPlaying().getType().name());
                    if(upgrade.getSound() != null)  config.set(backPack.getId() + ".jukebox." + upgrade.getId() + ".sound", upgrade.getSound().name());
                }
                if(backPack.containsUpgradeType(UpgradeType.AUTOFEED)){
                    List<Upgrade> list = backPack.getUpgradesFromType(UpgradeType.AUTOFEED);
                    AutoFeedUpgrade upgrade = (AutoFeedUpgrade) list.get(0);
                    Main.getMain().debugMessage("Saving auto feed upgrade " + upgrade.getId(), "info");
                    config.set(backPack.getId() + ".autofeed." + upgrade.getId() + ".enabled", upgrade.isEnabled());
                    if(upgrade.getItems() != null && !upgrade.getItems().isEmpty()) config.set(backPack.getId() + ".autofeed." + upgrade.getId() + ".items", upgrade.getItems());
                }
                if(backPack.containsUpgradeType(UpgradeType.VILLAGERSFOLLOW)){
                    List<Upgrade> list = backPack.getUpgradesFromType(UpgradeType.VILLAGERSFOLLOW);
                    VillagersFollowUpgrade upgrade = (VillagersFollowUpgrade) list.get(0);
                    Main.getMain().debugMessage("Saving villager upgrade " + upgrade.getId(), "info");
                    config.set(backPack.getId() + ".villager." + upgrade.getId() + ".enabled", upgrade.isEnabled());
                }
                if(backPack.containsUpgradeType(UpgradeType.COLLECTOR)){
                    List<Upgrade> list = backPack.getUpgradesFromType(UpgradeType.COLLECTOR);
                    CollectorUpgrade upgrade = (CollectorUpgrade) list.get(0);
                    Main.getMain().debugMessage("Saving collector upgrade " + upgrade.getId(), "info");
                    config.set(backPack.getId() + ".collector." + upgrade.getId() + ".enabled", upgrade.isEnabled());
                    config.set(backPack.getId() + ".collector." + upgrade.getId() + ".mode", upgrade.getMode());
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
    private static void serializeUpgrades(YamlConfiguration config, BackPack backPack){
        config.set(backPack.getId() + ".u", null);
        for(Upgrade upgrade : backPack.getUpgrades()){
            Main.getMain().debugMessage("Saving upgrade " + upgrade.getId() + " " + upgrade.getType(), "info");
            config.set(backPack.getId() + ".u." + upgrade.getId() + ".type", upgrade.getType().toString());
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