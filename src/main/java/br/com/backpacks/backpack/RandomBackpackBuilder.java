package br.com.backpacks.backpack;

import br.com.backpacks.Main;
import br.com.backpacks.upgrades.*;
import br.com.backpacks.upgrades.Upgrade;
import br.com.backpacks.upgrades.UpgradeManager;
import br.com.backpacks.upgrades.UpgradeType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.Inventory;
import org.bukkit.loot.LootContext;
import org.bukkit.loot.LootTable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class RandomBackpackBuilder {

    private final int id;
    private final String name;
    private BackpackType type;

    public RandomBackpackBuilder(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public Backpack generateBackpack(){
        generateBackpackType();

        if(type.getSecondPageSize() == 0){
            Backpack backpack = new Backpack(name, generateFistPage(), id, type);
            backpack.setIsBlock(false);

            if(shouldGenerateUpgrades()){
                backpack.setUpgradesIds(generateUpgrades());
            }

            Main.backpackManager.getBackpacks().put(id, backpack);
            return backpack;
        }

        Backpack backpack = new Backpack(name, generateFistPage(), generateSecondPage(), id, type);
        backpack.setIsBlock(false);

        if(shouldGenerateUpgrades()){
            backpack.setUpgradesIds(generateUpgrades());
        }

        Main.backpackManager.getBackpacks().put(id, backpack);
        return backpack;
    }

    private void generateBackpackType(){
        List<BackpackType> types = new ArrayList<>(List.of(BackpackType.values()));
        types.remove(BackpackType.NETHERITE);
        types.remove(BackpackType.DIAMOND);
        types.remove(BackpackType.AMETHYST);
        int randomIndex = ThreadLocalRandom.current().nextInt(types.size() - 1);
        type = types.get(randomIndex);
    }

    private Inventory generateFistPage() {
        Inventory firstPage = Bukkit.createInventory(null, type.getFirstPageSize(), name);

        // Get the default loot table
        LootTable lootTable = Bukkit.getLootTable(NamespacedKey.minecraft("chests/simple_dungeon"));

        // Generate random loot
        LootContext lootContext = new LootContext.Builder(new Location(Bukkit.getWorld("world"), 0, 0, 0)).build();

        lootTable.fillInventory(firstPage, ThreadLocalRandom.current(), lootContext);

        return firstPage;
    }

    private Inventory generateSecondPage() {
        return Bukkit.createInventory(null, type.getSecondPageSize(), name);
    }

    private boolean shouldGenerateUpgrades(){
        List<Boolean> booleans = List.of(false, false, false, false, true);
        int index = ThreadLocalRandom.current().nextInt(booleans.size() - 1);
        return booleans.get(index);
    }

    private List<Integer> generateUpgrades(){
        List<UpgradeType> types = new ArrayList<>(List.of(UpgradeType.values()));

        types.remove(UpgradeType.AUTOFILL);

        int bound = type.getMaxUpgrades();

        List<Integer> upgradesIds = new ArrayList<>();

        for(int i = 1; i <= bound; i++){

            if(ThreadLocalRandom.current().nextBoolean()){
                int indexUpgradeType = ThreadLocalRandom.current().nextInt(types.size() - 1);
                UpgradeType upgradeType = types.get(indexUpgradeType);

                switch (upgradeType){
                    case JUKEBOX ->{
                        JukeboxUpgrade upgrade = new JukeboxUpgrade(UpgradeManager.lastUpgradeID + 1);
                        UpgradeManager.getUpgrades().put(upgrade.getId(), upgrade);
                        upgradesIds.add(upgrade.getId());
                    }

                    case FURNACE ->  {
                        FurnaceUpgrade upgrade = new FurnaceUpgrade(UpgradeManager.lastUpgradeID + 1);
                        UpgradeManager.getUpgrades().put(upgrade.getId(), upgrade);
                        upgradesIds.add(upgrade.getId());
                    }

                    case AUTOFEED ->  {
                        AutoFeedUpgrade upgrade = new AutoFeedUpgrade(UpgradeManager.lastUpgradeID + 1);
                        UpgradeManager.getUpgrades().put(upgrade.getId(), upgrade);
                        upgradesIds.add(upgrade.getId());
                    }

                    case COLLECTOR ->  {
                        CollectorUpgrade upgrade = new CollectorUpgrade(UpgradeManager.lastUpgradeID + 1);
                        UpgradeManager.getUpgrades().put(upgrade.getId(), upgrade);
                        upgradesIds.add(upgrade.getId());
                    }

                    case LIQUID_TANK ->  {
                        TanksUpgrade upgrade = new TanksUpgrade(UpgradeManager.lastUpgradeID + 1);
                        UpgradeManager.getUpgrades().put(upgrade.getId(), upgrade);
                        upgradesIds.add(upgrade.getId());
                    }

                    case VILLAGER_BAIT ->  {
                        VillagerBaitUpgrade upgrade = new VillagerBaitUpgrade(UpgradeManager.lastUpgradeID + 1);
                        UpgradeManager.getUpgrades().put(upgrade.getId(), upgrade);
                        upgradesIds.add(upgrade.getId());
                    }

                    default -> {
                        Upgrade upgrade = new Upgrade(upgradeType, UpgradeManager.lastUpgradeID + 1);
                        UpgradeManager.getUpgrades().put(upgrade.getId(), upgrade);
                        upgradesIds.add(upgrade.getId());
                    }

                }

                UpgradeManager.lastUpgradeID++;
            }
        }

        return upgradesIds;
    }
}
