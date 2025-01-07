package br.com.backpacks.storage;

import br.com.backpacks.Main;
import br.com.backpacks.upgrades.*;
import br.com.backpacks.upgrades.Upgrade;
import br.com.backpacks.upgrades.UpgradeManager;
import br.com.backpacks.upgrades.UpgradeType;
import br.com.backpacks.backpack.Backpack;
import br.com.backpacks.scheduler.TickComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Furnace;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
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
        super(StorageProviderType.YAML);
        this.backpacksPath = backpacksPath;
        this.upgradesPath = upgradesPath;
    }

    @Override
    public void saveBackpacks() throws IOException {

        File file = new File(backpacksPath);
        YamlConfiguration config = new YamlConfiguration();

        for (Backpack backpack : Main.backpackManager.getBackpacks().values()) {

            if(backpack.getLocation() != null){
                List<String> data = SerializationUtils.serializeLocationToList(backpack.getLocation());
                config.set(backpack.getId() + ".loc", data);
            }

            if(backpack.getOutputUpgrade() != -1){
                config.set(backpack.getId() + ".out", backpack.getOutputUpgrade());
            }

            if(backpack.getInputUpgrade() != -1){
                config.set(backpack.getId() + ".inp", backpack.getInputUpgrade());
            }

            if(backpack.isShowingNameAbove()){
                config.set(backpack.getId() + ".shownameabove", true);
            }

            config.set(backpack.getId() + ".i", backpack.serialize());
            saveStorageContents(backpack, config);

            if(backpack.getOwner() != null){
                config.set(backpack.getId() + ".owner", backpack.getOwner().toString());
            }

            if (!backpack.getBackpackUpgrades().isEmpty()) {
                serializeUpgrades(config, backpack);
            }

            Main.debugMessage("Saving backpack " + backpack.getId());
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
                case FURNACE -> {
                    FurnaceUpgrade furnaceUpgrade = (FurnaceUpgrade) upgrade;

                    if(furnaceUpgrade.getFurnace() != null){
                        List<String> data = SerializationUtils.serializeLocationToList(furnaceUpgrade.getFurnace().getLocation());
                        config.set(upgrade.getId() + ".furnace.loc", data);
                    }
                }

                case JUKEBOX -> {
                    JukeboxUpgrade jukeboxUpgrade = (JukeboxUpgrade) upgrade;

                    if(!jukeboxUpgrade.getDiscs().isEmpty()){
                        for(Map.Entry<Integer, ItemStack> item : jukeboxUpgrade.getDiscs().entrySet()){
                            config.set(upgrade.getId() + ".jukebox.discs." + item.getKey(), item.getValue().getType().name());
                        }
                    }

                    if(jukeboxUpgrade.getInventory().getItem(13) != null)    config.set(upgrade.getId() + ".jukebox.playing", jukeboxUpgrade.getInventory().getItem(13).getType().name());
                }

                case AUTOFEED -> {
                    AutoFeedUpgrade autoFeedUpgrade = (AutoFeedUpgrade) upgrade;
                    config.set(upgrade.getId() + ".autofeed.enabled", autoFeedUpgrade.isEnabled());
                }

                case VILLAGER_BAIT -> {
                    VillagerBaitUpgrade followUpgrade = (VillagerBaitUpgrade) upgrade;
                    config.set(upgrade.getId() + ".villager.enabled", followUpgrade.isEnabled());
                }

                case COLLECTOR -> {
                    CollectorUpgrade collectorUpgrade = (CollectorUpgrade) upgrade;
                    config.set(upgrade.getId() + ".collector.enabled", collectorUpgrade.isEnabled());
                    config.set(upgrade.getId() + ".collector.mode", collectorUpgrade.getMode());
                }

                case LIQUID_TANK -> {
                    TanksUpgrade tanksUpgrade = (TanksUpgrade) upgrade;

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

            Main.debugMessage("Saving " + upgrade.getType().getName() + " Upgrade");

        }
        config.save(file);
    }

    @Override
    public void loadUpgrades(){
        File file = new File(Main.getMain().getDataFolder().getAbsolutePath() + "/upgrades.yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        UpgradeManager.lastUpgradeID = 0;

        for(String i : config.getKeys(false)){
            UpgradeType type;

            try{
                type = UpgradeType.valueOf(config.getString(i + ".type"));
            }   catch (IllegalArgumentException e){
                continue;
            }

            int id = Integer.parseInt(i);
            if(UpgradeManager.lastUpgradeID == 0) UpgradeManager.lastUpgradeID = id;

            if(UpgradeManager.lastUpgradeID < id){
                UpgradeManager.lastUpgradeID = id;
            }

            try{
                switch (type){
                    case FURNACE -> {
                        FurnaceUpgrade upgrade = new FurnaceUpgrade(id);

                        if(config.isSet(i + ".furnace.loc")){
                            Block block = SerializationUtils.deserializeLocationAsList(config.getStringList(i + ".furnace.loc")).getBlock();

                            Bukkit.getScheduler().runTask(Main.getMain(), ()->{
                                Furnace furnace = (Furnace) block.getState();
                                upgrade.setFurnace(furnace);
                            });

                            upgrade.setTickComponentId(Main.getMain().getTickManager().addAsyncComponent(new TickComponent(10, ()->{
                                for(Player player : Bukkit.getOnlinePlayers()){
                                    player.sendBlockChange(block.getLocation(), Material.AIR.createBlockData());
                                }
                            }), 10).getId());
                        }

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

                        upgrade.updateInventory();
                        UpgradeManager.getUpgrades().put(id, upgrade);
                    }

                    case VILLAGER_BAIT -> {
                        VillagerBaitUpgrade upgrade = new VillagerBaitUpgrade(id);

                        if (config.isSet(i + ".villager.enabled")) {
                            upgrade.setEnabled(config.getBoolean(i + ".villager.enabled"));
                        }

                        upgrade.updateInventory();
                        UpgradeManager.getUpgrades().put(id, upgrade);
                    }

                    case AUTOFEED -> {
                        AutoFeedUpgrade upgrade = new AutoFeedUpgrade(id);

                        if(config.isSet(i + ".autofeed.enabled")){
                            upgrade.setEnabled(config.getBoolean(i + ".autofeed.enabled"));
                        }

                        upgrade.updateInventory();
                        UpgradeManager.getUpgrades().put(id, upgrade);
                    }

                    case LIQUID_TANK -> {
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

                        UpgradeManager.getUpgrades().put(id, upgrade);
                    }

                    default -> {
                        Upgrade upgrade = new Upgrade(type, id);
                        UpgradeManager.getUpgrades().put(id, upgrade);
                    }
                }

                Main.debugMessage("Loading " + type.getName() + " upgrade: " + i);
            }   catch (Exception exception){
                Main.getMain().getLogger().severe(exception.getMessage());
            }

        }
    }

    @Override
    public void loadBackpacks() {
        File file = new File(Main.getMain().getDataFolder().getAbsolutePath() + "/backpacks.yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        Main.backpackManager.setLastBackpackID(0);

        for (String i : config.getKeys(false)) {

            Backpack backpack = new Backpack().deserialize(config, i);

            Main.debugMessage("Loading backpack " + backpack.getName() + " id " + backpack.getId());
            Main.backpackManager.getBackpacks().put(backpack.getId(), backpack);

            int id = backpack.getId();
            if(Main.backpackManager.getLastBackpackID() == 0) Main.backpackManager.setLastBackpackID(id);
            if(Main.backpackManager.getLastBackpackID() < id){
                Main.backpackManager.setLastBackpackID(id);
            }
        }
    }

    private void serializeUpgrades(YamlConfiguration config, Backpack backpack){
        config.set(backpack.getId() + ".u", null);
        config.set(backpack.getId() + ".u", backpack.getUpgradesIds());
    }

    private void saveStorageContents(Backpack backpack, YamlConfiguration config){
        int i = 0;
        int i1 = 0;
        config.set(backpack.getId() + ".1", null);

        for(ItemStack itemStack : backpack.getStorageContentsFirstPage()){
            if(itemStack == null){
                i++;
                continue;
            }

            config.set(backpack.getId() + ".1." + i, itemStack);
            i++;
        }

        if(backpack.getSecondPage() != null){
            config.set(backpack.getId() + ".2", null);

            for(ItemStack itemStack : backpack.getStorageContentsSecondPage()){
                if(itemStack == null){
                    i1++;
                    continue;
                }

                config.set(backpack.getId() + ".2." + i1, itemStack);
                i1++;
            }
        }
    }

}