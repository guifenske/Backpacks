package br.com.backpacks.yaml;

import br.com.backpacks.Main;
import br.com.backpacks.upgrades.*;
import br.com.backpacks.utils.BackPack;
import br.com.backpacks.utils.Upgrade;
import br.com.backpacks.utils.UpgradeType;
import br.com.backpacks.utils.inventory.InventoryBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public final class YamlUtils {

    public static void saveBackpacks(String path) throws IOException {
        File file = new File(path);
        YamlConfiguration config = new YamlConfiguration();

        for (BackPack backPack : Main.backPackManager.getBackpacks().values()) {
            config.set(backPack.getId() + ".loc", null);
            if(backPack.getLocation() != null){
                List<String> data = serializeLocation(backPack.getLocation());
                config.set(backPack.getId() + ".loc", data);
            }
            if(backPack.getOutputUpgrade() != -1){
                config.set(backPack.getId() + ".out", backPack.getOutputUpgrade());
            }
            if(backPack.getInputUpgrade() != -1){
                config.set(backPack.getId() + ".inp", backPack.getInputUpgrade());
            }
            config.set(backPack.getId() + ".i", backPack.serialize());
            saveStorageContents(backPack, config);
            if(backPack.getOwner() != null){
                config.set(backPack.getId() + ".owner", backPack.getOwner().toString());
            }
            if (backPack.getUpgrades() != null && !backPack.getUpgrades().isEmpty()) {
                serializeUpgrades(config, backPack);
            }
        }

        config.save(file);
    }

    public static void saveUpgrades(String path) throws IOException {
        File file = new File(path);
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
                    if(!jukeboxUpgrade.getDiscs().isEmpty()){
                        for(Map.Entry<Integer, ItemStack> item : jukeboxUpgrade.getDiscs().entrySet()){
                            config.set(upgrade.getId() + ".jukebox.discs." + item.getKey(), item.getValue().getType().name());
                        }
                    }
                    if(jukeboxUpgrade.getInventory().getItem(13) != null)    config.set(upgrade.getId() + ".jukebox.playing", jukeboxUpgrade.getInventory().getItem(13).getType().name());
                }
                case AUTOFEED -> {
                    AutoFeedUpgrade autoFeedUpgrade = (AutoFeedUpgrade) upgrade;
                    Main.getMain().debugMessage("Saving auto feed upgrade " + autoFeedUpgrade.getId());
                    config.set(upgrade.getId() + ".autofeed.enabled", autoFeedUpgrade.isEnabled());
                    if(!autoFeedUpgrade.getItems().isEmpty()){
                        for(Map.Entry<Integer, ItemStack> item : autoFeedUpgrade.getItems().entrySet()){
                            config.set(upgrade.getId() + ".autofeed.items." + item.getKey(), item.getValue());
                        }
                    }
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
                case LIQUIDTANK -> {
                    TanksUpgrade tanksUpgrade = (TanksUpgrade) upgrade;
                    Main.getMain().debugMessage("Saving tank upgrade " + tanksUpgrade.getId());
                    if(!tanksUpgrade.getItemsPerTank(1).isEmpty()){
                        for(Map.Entry<Integer, ItemStack> item : tanksUpgrade.getItemsPerTank(1).entrySet()){
                            config.set(upgrade.getId() + ".tank1." + item.getKey(), item.getValue());
                        }
                    }
                    if(!tanksUpgrade.getItemsPerTank(2).isEmpty()){
                        for(Map.Entry<Integer, ItemStack> item : tanksUpgrade.getItemsPerTank(2).entrySet()){
                            config.set(upgrade.getId() + ".tank2." + item.getKey(), item.getValue());
                        }
                    }
                    if(tanksUpgrade.getInventory().getItem(12) != null) config.set(upgrade.getId() + ".12", tanksUpgrade.getInventory().getItem(12));
                    if(tanksUpgrade.getInventory().getItem(14) != null) config.set(upgrade.getId() + ".14", tanksUpgrade.getInventory().getItem(14));
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
            int id = Integer.parseInt(i);
            if(Main.backPackManager.getUpgradesIds() == 0) Main.backPackManager.setUpgradesIds(id);
            if(Main.backPackManager.getUpgradesIds() < id){
                Main.backPackManager.setUpgradesIds(id);
            }
            switch (type){
                case FURNACE, BLAST_FURNACE, SMOKER -> {
                    FurnaceUpgrade upgrade = new FurnaceUpgrade(type, id);
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
                    Main.getMain().debugMessage("furnace loaded: " + i);
                    upgrade.updateInventory();
                    Main.backPackManager.getUpgradeHashMap().put(id, upgrade);
                }
                case JUKEBOX -> {
                    JukeboxUpgrade upgrade = new JukeboxUpgrade(id);
                    if(config.isSet(i + ".jukebox.discs")){
                        Set<String> keys = config.getConfigurationSection(i + ".jukebox.discs").getKeys(false);
                        for(String s : keys){
                            upgrade.getInventory().setItem(Integer.parseInt(s), new ItemStack(Material.getMaterial(config.getString(i + ".jukebox.discs." + s))));
                        }
                    }
                    if(config.isSet(i + ".jukebox.playing")){
                        upgrade.getInventory().setItem(13, upgrade.getSoundFromName(config.getString(i + ".jukebox.playing")));
                    }
                    Main.getMain().debugMessage("loading jukebox: " + i);
                    Main.backPackManager.getUpgradeHashMap().put(id, upgrade);
                }
                case COLLECTOR -> {
                    CollectorUpgrade upgrade = new CollectorUpgrade(id);
                    if(config.isSet(i + ".collector.enabled")){
                        upgrade.setEnabled(config.getBoolean(i + ".collector.enabled"));
                    }
                    if(config.isSet(i + ".collector.mode")){
                        upgrade.setMode(config.getInt(i + ".collector.mode"));
                    }
                    Main.getMain().debugMessage("loading collector: " + i);
                    upgrade.updateInventory();
                    Main.backPackManager.getUpgradeHashMap().put(id, upgrade);
                }
                case VILLAGERSFOLLOW -> {
                    VillagersFollowUpgrade upgrade = new VillagersFollowUpgrade(id);
                    if (config.isSet(i + ".villager.enabled")) {
                        upgrade.setEnabled(config.getBoolean(i + ".villager.enabled"));
                    }
                    Main.getMain().debugMessage("loading villagers follow: " + i);
                    upgrade.updateInventory();
                    Main.backPackManager.getUpgradeHashMap().put(id, upgrade);
                }
                case AUTOFEED -> {
                    AutoFeedUpgrade upgrade = new AutoFeedUpgrade(id);
                    if(config.isSet(i + ".autofeed.enabled")){
                        upgrade.setEnabled(config.getBoolean(i + ".autofeed.enabled"));
                    }
                    if(config.isSet(i + ".autofeed.items")){
                        Set<String> keys = config.getConfigurationSection(i + ".autofeed.items").getKeys(false);
                        for(String s : keys){
                            upgrade.getInventory().setItem(Integer.parseInt(s), config.getItemStack(i + ".autofeed.items." + s));
                        }
                    }
                    upgrade.updateInventory();

                    Main.getMain().debugMessage("loading auto feed: " + i);
                    Main.backPackManager.getUpgradeHashMap().put(id, upgrade);
                }
                case CRAFTING -> {
                    Upgrade upgrade = new Upgrade(UpgradeType.CRAFTING, id);
                    Main.backPackManager.getUpgradeHashMap().put(id, upgrade);
                }
                case ENCAPSULATE -> {
                    Upgrade upgrade = new Upgrade(UpgradeType.ENCAPSULATE, id);
                    Main.backPackManager.getUpgradeHashMap().put(id, upgrade);
                }
                case UNBREAKABLE -> {
                    Upgrade upgrade = new Upgrade(UpgradeType.UNBREAKABLE, id);
                    Main.backPackManager.getUpgradeHashMap().put(id, upgrade);
                }
                case LIQUIDTANK -> {
                    TanksUpgrade upgrade = new TanksUpgrade(id);
                    if(config.isSet(i + ".tank1")){
                        Set<String> keys = config.getConfigurationSection(i + ".tank1").getKeys(false);
                        for(String s : keys){
                            upgrade.getInventory().setItem(Integer.parseInt(s), config.getItemStack(i + ".tank1." + s));
                        }
                    }
                    if(config.isSet(i + ".tank2")){
                        Set<String> keys = config.getConfigurationSection(i + ".tank2").getKeys(false);
                        for(String s : keys){
                            upgrade.getInventory().setItem(Integer.parseInt(s), config.getItemStack(i + ".tank2." + s));
                        }
                    }
                    if(config.isSet(i + ".12")){
                        upgrade.getInventory().setItem(12, config.getItemStack(i + ".12"));
                    }
                    if(config.isSet(i + ".14")){
                        upgrade.getInventory().setItem(14, config.getItemStack(i + ".14"));
                    }

                    Main.getMain().debugMessage("loading tank upgrade: " + i);
                    Main.backPackManager.getUpgradeHashMap().put(id, upgrade);
                }
            }
        }
    }

    public static void loadBackpacks() {
        File file = new File(Main.getMain().getDataFolder().getAbsolutePath() + "/backpacks.yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        for (String i : config.getKeys(false)) {
            BackPack backPack = new BackPack().deserialize(config, i);
            if(config.isSet(i + ".loc")){
                backPack.setLocation(deserializeLocation(config.getStringList(i + ".loc")));
                backPack.setIsBlock(true);
                Main.backPackManager.getBackpacksPlacedLocations().put(backPack.getLocation(), backPack.getId());
            }
            Main.getMain().debugMessage("Loading backpack " + backPack.getName() + " id " + backPack.getId());
            Main.backPackManager.getBackpacks().put(backPack.getId(), backPack);

            new InventoryBuilder(InventoryBuilder.MenuType.CONFIG, backPack).build();
            new InventoryBuilder(InventoryBuilder.MenuType.UPGMENU, backPack).build();
            new InventoryBuilder(InventoryBuilder.MenuType.EDIT_IO_MENU, backPack).build();

            int id = backPack.getId();
            if(Main.backPackManager.getBackpackIds() == 0) Main.backPackManager.setBackpackIds(id);
            if(Main.backPackManager.getBackpackIds() < id){
                Main.backPackManager.setBackpackIds(id);
            }
        }
    }

    public static void loadBackpacks(ConcurrentHashMap<Integer, BackPack> hashMap){
        Main.backPackManager.setBackpackIds(0);
        Main.backPackManager.setBackpacks(hashMap);
        Main.backPackManager.getBackpacksPlacedLocations().clear();
        for(BackPack backPack : hashMap.values()){
            if(backPack.getLocation() != null){
                Main.backPackManager.getBackpacksPlacedLocations().put(backPack.getLocation(), backPack.getId());
            }

            InventoryBuilder.deleteAllMenusFromBackpack(backPack);
            new InventoryBuilder(InventoryBuilder.MenuType.CONFIG, backPack).build();
            new InventoryBuilder(InventoryBuilder.MenuType.UPGMENU, backPack).build();
            new InventoryBuilder(InventoryBuilder.MenuType.EDIT_IO_MENU, backPack).build();

            int id = backPack.getId();
            if(Main.backPackManager.getBackpackIds() == 0) Main.backPackManager.setBackpackIds(id);
            if(Main.backPackManager.getBackpackIds() < id){
                Main.backPackManager.setBackpackIds(id);
            }
        }
    }

    public static void loadUpgrades(ConcurrentHashMap<Integer, Upgrade> hashMap){
        Main.backPackManager.setUpgradesIds(0);
        Main.backPackManager.setUpgradeHashMap(hashMap);
        for(Integer id : hashMap.keySet()){
            if(Main.backPackManager.getUpgradesIds() == 0) Main.backPackManager.setUpgradesIds(id);
            if(Main.backPackManager.getUpgradesIds() < id){
                Main.backPackManager.setUpgradesIds(id);
            }
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