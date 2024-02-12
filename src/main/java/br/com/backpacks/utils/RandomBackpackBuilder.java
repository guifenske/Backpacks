package br.com.backpacks.utils;

import br.com.backpacks.Main;
import br.com.backpacks.upgrades.*;
import br.com.backpacks.utils.inventory.InventoryBuilder;
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

    private int id;
    private String name;
    private BackpackType type;
    public RandomBackpackBuilder(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public BackPack generateBackpack(){
        generateBackpackType();
        if(Main.backPackManager.getSizeSecondPageFromBackpackType(type) == 0){
            BackPack backPack = new BackPack(name, generateFistPage(), id, type);
            backPack.setIsBlock(false);
            if(shouldGenerateUpgrades()){
                backPack.setUpgrades(generateUpgrades());
            }
            Main.backPackManager.getBackpacks().put(id, backPack);
            return backPack;
        }

        BackPack backPack = new BackPack(name, generateFistPage(), generateSecondPage(), id, type);
        backPack.setIsBlock(false);
        if(shouldGenerateUpgrades()){
            backPack.setUpgrades(generateUpgrades());
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
        Inventory firstPage = Bukkit.createInventory(null, Main.backPackManager.getSizeFirstPageFromBackpackType(type), name);

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
        return Bukkit.createInventory(null, Main.backPackManager.getSizeSecondPageFromBackpackType(type), name);
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
        int bound = InventoryBuilder.getFreeUpgradesSlots(type);
        List<Upgrade> upgrades = new ArrayList<>();
        for(int i = 1; i <= bound; i++){
            if(ThreadLocalRandom.current().nextBoolean()){
                int indexUpgradeType = ThreadLocalRandom.current().nextInt(types.size() - 1);
                UpgradeType upgradeType = types.get(indexUpgradeType);
                switch (upgradeType){
                    case JUKEBOX ->{
                        JukeboxUpgrade upgrade = new JukeboxUpgrade(Main.backPackManager.getUpgradesIds() + 1);
                        Main.backPackManager.getUpgradeHashMap().put(upgrade.getId(), upgrade);
                        upgrades.add(upgrade);
                    }
                    case SMOKER -> {
                        FurnaceUpgrade upgrade = new FurnaceUpgrade(UpgradeType.SMOKER, Main.backPackManager.getUpgradesIds() + 1);
                        Main.backPackManager.getUpgradeHashMap().put(upgrade.getId(), upgrade);
                        upgrades.add(upgrade);
                    }
                    case BLAST_FURNACE ->  {
                        FurnaceUpgrade upgrade = new FurnaceUpgrade(UpgradeType.BLAST_FURNACE, Main.backPackManager.getUpgradesIds() + 1);
                        Main.backPackManager.getUpgradeHashMap().put(upgrade.getId(), upgrade);
                        upgrades.add(upgrade);
                    }
                    case FURNACE ->  {
                        FurnaceUpgrade upgrade = new FurnaceUpgrade(UpgradeType.FURNACE, Main.backPackManager.getUpgradesIds() + 1);
                        Main.backPackManager.getUpgradeHashMap().put(upgrade.getId(), upgrade);
                        upgrades.add(upgrade);
                    }
                    case AUTOFEED ->  {
                        AutoFeedUpgrade upgrade = new AutoFeedUpgrade(Main.backPackManager.getUpgradesIds() + 1);
                        Main.backPackManager.getUpgradeHashMap().put(upgrade.getId(), upgrade);
                        upgrades.add(upgrade);
                    }
                    case COLLECTOR ->  {
                        CollectorUpgrade upgrade = new CollectorUpgrade(Main.backPackManager.getUpgradesIds() + 1);
                        Main.backPackManager.getUpgradeHashMap().put(upgrade.getId(), upgrade);
                        upgrades.add(upgrade);
                    }
                    case LIQUIDTANK ->  {
                        TanksUpgrade upgrade = new TanksUpgrade(Main.backPackManager.getUpgradesIds() + 1);
                        Main.backPackManager.getUpgradeHashMap().put(upgrade.getId(), upgrade);
                        upgrades.add(upgrade);
                    }
                    case ENCAPSULATE ->  {
                        Upgrade upgrade = new Upgrade(UpgradeType.ENCAPSULATE, Main.backPackManager.getUpgradesIds() + 1);
                        Main.backPackManager.getUpgradeHashMap().put(upgrade.getId(), upgrade);
                        upgrades.add(upgrade);
                    }
                    case VILLAGERSFOLLOW ->  {
                        VillagersFollowUpgrade upgrade = new VillagersFollowUpgrade(Main.backPackManager.getUpgradesIds() + 1);
                        Main.backPackManager.getUpgradeHashMap().put(upgrade.getId(), upgrade);
                        upgrades.add(upgrade);
                    }
                    case CRAFTING ->  {
                        Upgrade upgrade = new Upgrade(UpgradeType.CRAFTING, Main.backPackManager.getUpgradesIds() + 1);
                        Main.backPackManager.getUpgradeHashMap().put(upgrade.getId(), upgrade);
                        upgrades.add(upgrade);
                    }
                    case UNBREAKABLE ->  {
                        Upgrade upgrade = new Upgrade(UpgradeType.UNBREAKABLE, Main.backPackManager.getUpgradesIds() + 1);
                        Main.backPackManager.getUpgradeHashMap().put(upgrade.getId(), upgrade);
                        upgrades.add(upgrade);
                    }
                }
                Main.backPackManager.setUpgradesIds(Main.backPackManager.getUpgradesIds() + 1);
            }
        }
        return upgrades;
    }


}
