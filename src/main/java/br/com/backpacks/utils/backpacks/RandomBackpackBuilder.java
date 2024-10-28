package br.com.backpacks.utils.backpacks;

import br.com.backpacks.Main;
import br.com.backpacks.upgrades.*;
import br.com.backpacks.utils.Upgrade;
import br.com.backpacks.utils.UpgradeManager;
import br.com.backpacks.utils.UpgradeType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.loot.LootContext;
import org.bukkit.loot.LootTable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class RandomBackpackBuilder {

    private final int id;
    private final String name;
    private BackpackType type;

    public RandomBackpackBuilder(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public BackPack generateBackpack(){
        generateBackpackType();

        if(type.getSecondPageSize() == 0){
            BackPack backPack = new BackPack(name, generateFistPage(), id, type);
            backPack.setIsBlock(false);

            if(shouldGenerateUpgrades()){
                backPack.setBackpackUpgrades(generateUpgrades());
            }

            Main.backPackManager.getBackpacks().put(id, backPack);
            return backPack;
        }

        BackPack backPack = new BackPack(name, generateFistPage(), generateSecondPage(), id, type);
        backPack.setIsBlock(false);

        if(shouldGenerateUpgrades()){
            backPack.setBackpackUpgrades(generateUpgrades());
        }

        Main.backPackManager.getBackpacks().put(id, backPack);
        return backPack;
    }

    private void generateBackpackType(){
        List<BackpackType> types = new ArrayList<>(List.of(BackpackType.values()));
        types.remove(BackpackType.NETHERITE);
        int randomIndex = ThreadLocalRandom.current().nextInt(types.size() - 1);
        type = types.get(randomIndex);
    }

    private Inventory generateFistPage() {
        //generate first page with random loot
        Inventory firstPage = Bukkit.createInventory(null, type.getFirstPageSize(), name);

        // Get the default loot table
        LootTable lootTable = Bukkit.getLootTable(NamespacedKey.minecraft("chests/simple_dungeon"));

        // Generate random loot
        LootContext lootContext = new LootContext.Builder(new Location(Bukkit.getWorld("world"), 0, 0, 0)).build();
        ItemStack[] loot = lootTable.populateLoot(new Random(), lootContext).toArray(new ItemStack[0]);

        // Add the loot to the first page inventory
        firstPage.setStorageContents(applyRandomOrderToLoot(Arrays.asList(loot), firstPage));

        return firstPage;
    }

    private Inventory generateSecondPage() {
        return Bukkit.createInventory(null, type.getSecondPageSize(), name);
    }


    private ItemStack[] applyRandomOrderToLoot(List<ItemStack> loot, Inventory inventory){
        List<ItemStack> newLoot = new ArrayList<>();

        for(int i = 0; i < inventory.getSize() - 3; i++){
            newLoot.add(null);
        }

        for(ItemStack item : loot){
            int randomIndex = ThreadLocalRandom.current().nextInt(inventory.getSize() - 3);

            while(newLoot.get(randomIndex) != null){
                randomIndex = ThreadLocalRandom.current().nextInt(inventory.getSize() - 3);
            }

            newLoot.set(randomIndex, item);
        }

        return newLoot.toArray(new ItemStack[0]);
    }

    private boolean shouldGenerateUpgrades(){
        List<Boolean> booleans = List.of(false, false, false, false, true);
        int index = ThreadLocalRandom.current().nextInt(booleans.size() - 1);
        return booleans.get(index);
    }

    private List<Upgrade> generateUpgrades(){
        List<UpgradeType> types = List.of(UpgradeType.values());
        int bound = type.getMaxUpgrades();
        List<Upgrade> upgrades = new ArrayList<>();

        for(int i = 1; i <= bound; i++){

            if(ThreadLocalRandom.current().nextBoolean()){
                int indexUpgradeType = ThreadLocalRandom.current().nextInt(types.size() - 1);
                UpgradeType upgradeType = types.get(indexUpgradeType);

                switch (upgradeType){
                    case JUKEBOX ->{
                        JukeboxUpgrade upgrade = new JukeboxUpgrade(UpgradeManager.lastUpgradeID + 1);
                        UpgradeManager.getUpgrades().put(upgrade.getId(), upgrade);
                        upgrades.add(upgrade);
                    }

                    case FURNACE ->  {
                        FurnaceUpgrade upgrade = new FurnaceUpgrade(UpgradeManager.lastUpgradeID + 1);
                        UpgradeManager.getUpgrades().put(upgrade.getId(), upgrade);
                        upgrades.add(upgrade);
                    }

                    case AUTOFEED ->  {
                        AutoFeedUpgrade upgrade = new AutoFeedUpgrade(UpgradeManager.lastUpgradeID + 1);
                        UpgradeManager.getUpgrades().put(upgrade.getId(), upgrade);
                        upgrades.add(upgrade);
                    }

                    case COLLECTOR ->  {
                        CollectorUpgrade upgrade = new CollectorUpgrade(UpgradeManager.lastUpgradeID + 1);
                        UpgradeManager.getUpgrades().put(upgrade.getId(), upgrade);
                        upgrades.add(upgrade);
                    }

                    case LIQUID_TANK ->  {
                        TanksUpgrade upgrade = new TanksUpgrade(UpgradeManager.lastUpgradeID + 1);
                        UpgradeManager.getUpgrades().put(upgrade.getId(), upgrade);
                        upgrades.add(upgrade);
                    }

                    case ENCAPSULATE ->  {
                        Upgrade upgrade = new Upgrade(UpgradeType.ENCAPSULATE, UpgradeManager.lastUpgradeID + 1);
                        UpgradeManager.getUpgrades().put(upgrade.getId(), upgrade);
                        upgrades.add(upgrade);
                    }

                    case VILLAGER_BAIT ->  {
                        VillagerBaitUpgrade upgrade = new VillagerBaitUpgrade(UpgradeManager.lastUpgradeID + 1);
                        UpgradeManager.getUpgrades().put(upgrade.getId(), upgrade);
                        upgrades.add(upgrade);
                    }

                    case CRAFTING_GRID ->  {
                        Upgrade upgrade = new Upgrade(UpgradeType.CRAFTING_GRID, UpgradeManager.lastUpgradeID + 1);
                        UpgradeManager.getUpgrades().put(upgrade.getId(), upgrade);
                        upgrades.add(upgrade);
                    }

                    case UNBREAKABLE ->  {
                        Upgrade upgrade = new Upgrade(UpgradeType.UNBREAKABLE, UpgradeManager.lastUpgradeID + 1);
                        UpgradeManager.getUpgrades().put(upgrade.getId(), upgrade);
                        upgrades.add(upgrade);
                    }

                }

                UpgradeManager.lastUpgradeID++;
            }
        }
        return upgrades;
    }
}
