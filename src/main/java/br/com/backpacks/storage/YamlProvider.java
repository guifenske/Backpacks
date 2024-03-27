package br.com.backpacks.storage;

import br.com.backpacks.Main;
import br.com.backpacks.upgrades.*;
import br.com.backpacks.utils.Upgrade;
import br.com.backpacks.utils.UpgradeManager;
import br.com.backpacks.utils.UpgradeType;
import br.com.backpacks.utils.backpacks.BackPack;
import br.com.backpacks.utils.inventory.InventoryBuilder;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class YamlProvider extends StorageProvider {
    private final String backpacksPath;
    private final String upgradesPath;

    public YamlProvider(String backpacksPath, String upgradesPath) {
        super(StorageManager.StorageProviderType.YAML);
        this.backpacksPath = backpacksPath;
        this.upgradesPath = upgradesPath;
    }

    @Override
    public void saveBackpacks() throws IOException {
        File file = new File(backpacksPath);
        YamlConfiguration config = new YamlConfiguration();

        for (BackPack backPack : Main.backPackManager.getBackpacks().values()) {
            if(backPack.getLocation() != null){
                List<String> data = SerializationUtils.serializeLocationToList(backPack.getLocation());
                config.set(backPack.getId() + ".loc", data);
            }
            if(backPack.getOutputUpgrade() != -1){
                config.set(backPack.getId() + ".out", backPack.getOutputUpgrade());
            }
            if(backPack.getInputUpgrade() != -1){
                config.set(backPack.getId() + ".inp", backPack.getInputUpgrade());
            }
            if(backPack.isShowingNameAbove()){
                config.set(backPack.getId() + ".shownameabove", true);
            }
            config.set(backPack.getId() + ".i", backPack.serialize());
            saveStorageContents(backPack, config);
            if(backPack.getOwner() != null){
                config.set(backPack.getId() + ".owner", backPack.getOwner().toString());
            }
            if (backPack.getBackpackUpgrades() != null && !backPack.getBackpackUpgrades().isEmpty()) {
                serializeUpgrades(config, backPack);
            }
            Main.debugMessage("Saving backpack " + backPack.getId());
        }

        config.save(file);
    }

    @Override
    public void saveUpgrades() throws IOException {
        File file = new File(upgradesPath);
        YamlConfiguration config = new YamlConfiguration();

        for(Upgrade upgrade : UpgradeManager.getUpgrades().values()){
            config.set(upgrade.getId() + ".type", upgrade.getType().toString());
            UpgradeType type = upgrade.getType();
            switch (type){
                case FURNACE, BLAST_FURNACE, SMOKER -> {
                    Main.debugMessage("Saving furnace upgrade " + upgrade.getId());
                    FurnaceUpgrade furnaceUpgrade = (FurnaceUpgrade) upgrade;
                    if(furnaceUpgrade.getResult() != null)  config.set(upgrade.getId() + ".furnace.result", furnaceUpgrade.getResult());
                    if(furnaceUpgrade.getFuel() != null)  config.set(upgrade.getId() + ".furnace.fuel", furnaceUpgrade.getFuel());
                    if(furnaceUpgrade.getSmelting() != null)  config.set(upgrade.getId() + ".furnace.smelting", furnaceUpgrade.getSmelting());
                    if(furnaceUpgrade.getOperation() > 0)   config.set(upgrade.getId() + ".furnace.operation", furnaceUpgrade.getOperation());
                    if(furnaceUpgrade.getLastMaxOperation() > 0)   config.set(upgrade.getId() + ".furnace.maxoperation", furnaceUpgrade.getLastMaxOperation());
                }
                case JUKEBOX -> {
                    JukeboxUpgrade jukeboxUpgrade = (JukeboxUpgrade) upgrade;
                    Main.debugMessage("Saving jukebox upgrade " + jukeboxUpgrade.getId());
                    if(!jukeboxUpgrade.getDiscs().isEmpty()){
                        for(Map.Entry<Integer, ItemStack> item : jukeboxUpgrade.getDiscs().entrySet()){
                            config.set(upgrade.getId() + ".jukebox.discs." + item.getKey(), item.getValue().getType().name());
                        }
                    }
                    if(jukeboxUpgrade.getInventory().getItem(13) != null)    config.set(upgrade.getId() + ".jukebox.playing", jukeboxUpgrade.getInventory().getItem(13).getType().name());
                }
                case AUTOFEED -> {
                    AutoFeedUpgrade autoFeedUpgrade = (AutoFeedUpgrade) upgrade;
                    Main.debugMessage("Saving auto feed upgrade " + autoFeedUpgrade.getId());
                    config.set(upgrade.getId() + ".autofeed.enabled", autoFeedUpgrade.isEnabled());
                }
                case VILLAGERSFOLLOW -> {
                    VillagersFollowUpgrade followUpgrade = (VillagersFollowUpgrade) upgrade;
                    Main.debugMessage("Saving villager upgrade " + followUpgrade.getId());
                    config.set(upgrade.getId() + ".villager.enabled", followUpgrade.isEnabled());
                }
                case COLLECTOR -> {
                    CollectorUpgrade collectorUpgrade = (CollectorUpgrade) upgrade;
                    Main.debugMessage("Saving collector upgrade " + collectorUpgrade.getId());
                    config.set(upgrade.getId() + ".collector.enabled", collectorUpgrade.isEnabled());
                    config.set(upgrade.getId() + ".collector.mode", collectorUpgrade.getMode());
                }
                case LIQUIDTANK -> {
                    TanksUpgrade tanksUpgrade = (TanksUpgrade) upgrade;
                    Main.debugMessage("Saving tank upgrade " + tanksUpgrade.getId());
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

    @Override
    public void loadUpgrades(){
        File file = new File(Main.getMain().getDataFolder().getAbsolutePath() + "/upgrades.yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        UpgradeManager.lastUpgradeID = 0;

        for(String i : config.getKeys(false)){
            UpgradeType type = UpgradeType.valueOf(config.getString(i + ".type"));
            int id = Integer.parseInt(i);
            if(UpgradeManager.lastUpgradeID == 0) UpgradeManager.lastUpgradeID = id;
            if(UpgradeManager.lastUpgradeID < id){
                UpgradeManager.lastUpgradeID = id;
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
                    Main.debugMessage("furnace loaded: " + i);
                    upgrade.updateInventory();
                    UpgradeManager.getUpgrades().put(id, upgrade);
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
                    Main.debugMessage("loading jukebox: " + i);
                    UpgradeManager.getUpgrades().put(id, upgrade);
                }
                case COLLECTOR -> {
                    CollectorUpgrade upgrade = new CollectorUpgrade(id);
                    if(config.isSet(i + ".collector.enabled")){
                        upgrade.setEnabled(config.getBoolean(i + ".collector.enabled"));
                    }
                    if(config.isSet(i + ".collector.mode")){
                        upgrade.setMode(config.getInt(i + ".collector.mode"));
                    }
                    Main.debugMessage("loading collector: " + i);
                    upgrade.updateInventory();
                    UpgradeManager.getUpgrades().put(id, upgrade);
                }
                case VILLAGERSFOLLOW -> {
                    VillagersFollowUpgrade upgrade = new VillagersFollowUpgrade(id);
                    if (config.isSet(i + ".villager.enabled")) {
                        upgrade.setEnabled(config.getBoolean(i + ".villager.enabled"));
                    }
                    Main.debugMessage("loading villagers follow: " + i);
                    upgrade.updateInventory();
                    UpgradeManager.getUpgrades().put(id, upgrade);
                }
                case AUTOFEED -> {
                    AutoFeedUpgrade upgrade = new AutoFeedUpgrade(id);
                    if(config.isSet(i + ".autofeed.enabled")){
                        upgrade.setEnabled(config.getBoolean(i + ".autofeed.enabled"));
                    }
                    upgrade.updateInventory();
                    Main.debugMessage("loading auto feed: " + i);
                    UpgradeManager.getUpgrades().put(id, upgrade);
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

                    Main.debugMessage("loading tank upgrade: " + i);
                    UpgradeManager.getUpgrades().put(id, upgrade);
                }
                default -> {
                    Upgrade upgrade = new Upgrade(type, id);
                    UpgradeManager.getUpgrades().put(id, upgrade);
                    Main.debugMessage("loading " + type.toString().toLowerCase() + " upgrade: " + i);
                }
            }
        }
    }

    @Override
    public void loadBackpacks() {
        File file = new File(Main.getMain().getDataFolder().getAbsolutePath() + "/backpacks.yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        Main.backPackManager.setLastBackpackID(0);

        for (String i : config.getKeys(false)) {
            BackPack backPack = new BackPack().deserialize(config, i);
            if(config.isSet(i + ".loc")){
                backPack.setLocation(SerializationUtils.deserializeLocationAsList(config.getStringList(i + ".loc")));
                backPack.setIsBlock(true);
                if(config.isSet(i + ".shownameabove")){
                    backPack.setShowNameAbove(true);
                }
                Main.backPackManager.getBackpacksPlacedLocations().put(backPack.getLocation(), backPack.getId());
            }
            Main.debugMessage("Loading backpack " + backPack.getName() + " id " + backPack.getId());
            Main.backPackManager.getBackpacks().put(backPack.getId(), backPack);

            new InventoryBuilder(InventoryBuilder.MenuType.CONFIG, backPack).build();
            new InventoryBuilder(InventoryBuilder.MenuType.UPGMENU, backPack).build();
            new InventoryBuilder(InventoryBuilder.MenuType.EDIT_IO_MENU, backPack).build();

            int id = backPack.getId();
            if(Main.backPackManager.getLastBackpackID() == 0) Main.backPackManager.setLastBackpackID(id);
            if(Main.backPackManager.getLastBackpackID() < id){
                Main.backPackManager.setLastBackpackID(id);
            }
        }
    }

    private void serializeUpgrades(YamlConfiguration config, BackPack backPack){
        config.set(backPack.getId() + ".u", null);
        config.set(backPack.getId() + ".u", backPack.getUpgradesIds());
    }

    private void saveStorageContents(BackPack backPack, YamlConfiguration config){
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