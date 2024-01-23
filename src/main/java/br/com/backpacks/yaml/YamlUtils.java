package br.com.backpacks.yaml;

import br.com.backpacks.Main;
import br.com.backpacks.backpackUtils.BackPack;
import br.com.backpacks.backpackUtils.Upgrade;
import br.com.backpacks.backpackUtils.UpgradeType;
import br.com.backpacks.events.upgrades.Furnace;
import br.com.backpacks.upgrades.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class YamlUtils {

    public static void saveBackpacks() throws IOException {
        File file = new File(Main.getMain().getDataFolder().getAbsolutePath() + "/backpacks.yml");
        YamlConfiguration config = new YamlConfiguration();

        for (BackPack backPack : Main.backPackManager.getBackpacks().values()) {
            config.set(backPack.getId() + ".loc", null);
            if(backPack.getUpgradeInputId() > -1) config.set(backPack.getId() + ".input", backPack.getUpgradeInputId());
            if(backPack.getUpgradeOutputId() > -1) config.set(backPack.getId() + ".output", backPack.getUpgradeOutputId());
            if(backPack.getLocation() != null){
                List<String> data = serializeLocation(backPack.getLocation());
                config.set(backPack.getId() + ".loc", data);
            }
            config.set(backPack.getId() + ".i", backPack.serialize());
            saveStorageContents(backPack, config);
            if (backPack.getUpgrades() != null && !backPack.getUpgrades().isEmpty()) {
                serializeUpgrades(config, backPack);
            }
        }

        config.save(file);
    }

    public static void saveUpgrades() throws IOException {
        File file = new File(Main.getMain().getDataFolder().getAbsolutePath() + "/upgrades.yml");
        YamlConfiguration config = new YamlConfiguration();

        for(Upgrade upgrade : Main.backPackManager.getUpgradeHashMap().values()){
            config.set(upgrade.getId() + ".type", upgrade.getType().toString());
            UpgradeType type = upgrade.getType();
            switch (type){
                case FURNACE, BLAST_FURNACE, SMOKER -> {
                    Main.getMain().debugMessage("Saving furnace upgrade " + upgrade.getId());
                    FurnaceUpgrade furnaceUpgrade = (FurnaceUpgrade) upgrade;
                    if(furnaceUpgrade.getResult() != null)  config.set(upgrade.getId() + ".furnace.result", furnaceUpgrade.getResult());
                    if(furnaceUpgrade.getFuel() != null)  config.set(upgrade.getId() + ".furnace.fuel", furnaceUpgrade.getFuel());
                    if(furnaceUpgrade.getSmelting() != null)  config.set(upgrade.getId() + ".furnace.smelting", furnaceUpgrade.getSmelting());
                    if(furnaceUpgrade.getOperation() > 0)   config.set(upgrade.getId() + ".furnace.operation", furnaceUpgrade.getOperation());
                    if(furnaceUpgrade.getLastMaxOperation() > 0)   config.set(upgrade.getId() + ".furnace.maxoperation", furnaceUpgrade.getLastMaxOperation());
                }
                case JUKEBOX -> {
                    JukeboxUpgrade jukeboxUpgrade = (JukeboxUpgrade) upgrade;
                    Main.getMain().debugMessage("Saving jukebox upgrade " + jukeboxUpgrade.getId());
                    if(jukeboxUpgrade.getDiscs() != null && !jukeboxUpgrade.getDiscs().isEmpty()) config.set(upgrade.getId() + ".jukebox.discs", jukeboxUpgrade.serializeDiscs());
                    if(jukeboxUpgrade.getPlaying() != null)    config.set(upgrade.getId() + ".jukebox.playing", jukeboxUpgrade.getPlaying().getType().name());
                    if(jukeboxUpgrade.getSound() != null)  config.set(upgrade.getId() + ".jukebox.sound", jukeboxUpgrade.getSound().name());
                }
                case AUTOFEED -> {
                    AutoFeedUpgrade autoFeedUpgrade = (AutoFeedUpgrade) upgrade;
                    Main.getMain().debugMessage("Saving auto feed upgrade " + autoFeedUpgrade.getId());
                    config.set(upgrade.getId() + ".autofeed.enabled", autoFeedUpgrade.isEnabled());
                    if(autoFeedUpgrade.getItems() != null && !autoFeedUpgrade.getItems().isEmpty()) config.set(upgrade.getId() + ".autofeed.items", autoFeedUpgrade.getItems());
                }
                case VILLAGERSFOLLOW -> {
                    VillagersFollowUpgrade followUpgrade = (VillagersFollowUpgrade) upgrade;
                    Main.getMain().debugMessage("Saving villager upgrade " + followUpgrade.getId());
                    config.set(upgrade.getId() + ".villager.enabled", followUpgrade.isEnabled());
                }
                case COLLECTOR -> {
                    CollectorUpgrade collectorUpgrade = (CollectorUpgrade) upgrade;
                    Main.getMain().debugMessage("Saving collector upgrade " + collectorUpgrade.getId());
                    config.set(upgrade.getId() + ".collector.enabled", collectorUpgrade.isEnabled());
                    config.set(upgrade.getId() + ".collector.mode", collectorUpgrade.getMode());
                }
            }
        }
        config.save(file);
    }

    public static void loadUpgrades(){
        File file = new File(Main.getMain().getDataFolder().getAbsolutePath() + "/upgrades.yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        for(String i : config.getKeys(false)){
            UpgradeType type = UpgradeType.valueOf(config.getString(i + ".type"));
            Main.backPackManager.setUpgradesIds(Integer.parseInt(i));
            switch (type){
                case FURNACE, BLAST_FURNACE, SMOKER -> {
                    Main.getMain().debugMessage("loading furnace: " + i);
                    FurnaceUpgrade upgrade = new FurnaceUpgrade(Integer.parseInt(i), type);
                    if(config.isSet(i + ".furnace.result")){
                        upgrade.setResult(config.getItemStack(i + ".furnace.result"));
                    }
                    if(config.isSet(i + ".furnace.fuel")){
                        upgrade.setFuel(config.getItemStack(i + ".furnace.fuel"));
                    }
                    if(config.isSet(i + ".furnace.smelting")){
                        upgrade.setSmelting(config.getItemStack(i + ".furnace.smelting"));
                    }
                    if(config.isSet(i + ".furnace.operation")){
                        upgrade.setOperation(config.getInt(i + ".furnace.operation"));
                    }
                    if(config.isSet(i + ".furnace.maxoperation")){
                        upgrade.setLastMaxOperation(config.getInt(i + ".furnace.maxoperation"));
                    }
                    upgrade.updateInventory();
                    Main.backPackManager.getUpgradeHashMap().put(Integer.parseInt(i), upgrade);

                    if(upgrade.canTick()){
                        Furnace.shouldTick.add(upgrade.getId());
                        Furnace.tick(upgrade);
                    }
                }
                case JUKEBOX -> {
                    Main.getMain().debugMessage("loading jukebox: " + i);
                    JukeboxUpgrade upgrade = new JukeboxUpgrade(Integer.parseInt(i));
                    if(config.isSet(i + ".jukebox.discs")){
                        List<ItemStack> discs = new ArrayList<>();
                        for(String disc : config.getStringList(i + ".jukebox.discs")){
                            discs.add(upgrade.getSoundFromName(disc));
                        }
                        upgrade.setDiscs(discs);
                    }
                    if(config.isSet(i + ".jukebox.playing")){
                        upgrade.setPlaying(upgrade.getSoundFromName(config.getString(i + ".jukebox.playing")));
                    }
                    if(config.isSet(i + ".jukebox.sound")){
                        upgrade.setSound(Sound.valueOf(config.getString(i + ".jukebox.sound")));
                    }
                    upgrade.updateInventory();
                    Main.backPackManager.getUpgradeHashMap().put(Integer.parseInt(i), upgrade);
                }
                case COLLECTOR -> {
                    Main.getMain().debugMessage("loading collector: " + i);
                    CollectorUpgrade upgrade = new CollectorUpgrade(Integer.parseInt(i));
                    if(config.isSet(i + ".collector.enabled")){
                        upgrade.setEnabled(config.getBoolean(i + ".collector.enabled"));
                    }
                    if(config.isSet(i + ".collector.mode")){
                        upgrade.setMode(config.getInt(i + ".collector.mode"));
                    }
                    upgrade.updateInventory();
                    Main.backPackManager.getUpgradeHashMap().put(Integer.parseInt(i), upgrade);
                }
                case VILLAGERSFOLLOW -> {
                    Main.getMain().debugMessage("loading villagers follow: " + i);
                    VillagersFollowUpgrade upgrade = new VillagersFollowUpgrade(Integer.parseInt(i));
                    if (config.isSet(i + ".villager.enabled")) {
                        upgrade.setEnabled(config.getBoolean(i + ".villager.enabled"));
                    }
                    upgrade.updateInventory();
                    Main.backPackManager.getUpgradeHashMap().put(Integer.parseInt(i), upgrade);
                }
                case AUTOFEED -> {
                    Main.getMain().debugMessage("loading auto feed: " + i);
                    AutoFeedUpgrade upgrade = new AutoFeedUpgrade(Integer.parseInt(i));
                    if(config.isSet(i + ".autofeed.enabled")){
                        upgrade.setEnabled(config.getBoolean(i + ".autofeed.enabled"));
                    }
                    if(config.isSet(i + ".autofeed.items")){
                        upgrade.setItems((List<ItemStack>) config.getList(i + ".autofeed.items"));
                    }
                    upgrade.updateInventory();
                    Main.backPackManager.getUpgradeHashMap().put(Integer.parseInt(i), upgrade);
                }
                case CRAFTING -> {
                    Upgrade upgrade = new Upgrade(UpgradeType.CRAFTING, Integer.parseInt(i));
                    Main.backPackManager.getUpgradeHashMap().put(Integer.parseInt(i), upgrade);
                }
                case ENCAPSULATE -> {
                    Upgrade upgrade = new Upgrade(UpgradeType.ENCAPSULATE, Integer.parseInt(i));
                    Main.backPackManager.getUpgradeHashMap().put(Integer.parseInt(i), upgrade);
                }
            }
        }
    }

    public static void loadBackpacks() {
        File file = new File(Main.getMain().getDataFolder().getAbsolutePath() + "/backpacks.yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        for (String i : config.getKeys(false)) {
            BackPack backPack = new BackPack().deserialize(config, i);
            backPack.setIsBlock(false);
            if(config.isSet(i + ".loc")){
                backPack.setLocation(deserializeLocation(config.getStringList(i + ".loc")));
                Main.backPackManager.getBackpacksPlacedLocations().put(backPack.getLocation(), backPack.getId());
            }
            Main.getMain().debugMessage("Loading backpack " + backPack.getName() + " with id " + backPack.getId());
            Main.backPackManager.getBackpacks().put(backPack.getId(), backPack);
            Main.backPackManager.setBackpackIds(backPack.getId());
        }
    }
    private static void serializeUpgrades(YamlConfiguration config, BackPack backPack){
        config.set(backPack.getId() + ".u", null);
        config.set(backPack.getId() + ".u", backPack.getUpgradesIds());
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