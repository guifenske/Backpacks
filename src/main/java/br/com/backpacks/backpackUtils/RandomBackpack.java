package br.com.backpacks.backpackUtils;

import br.com.backpacks.Main;
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

public class RandomBackpack {

    private int id;
    private String name;
    private BackpackType type;
    public RandomBackpack(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public BackPack generateBackpack(){
        generateBackpackType();
        if(Main.backPackManager.getSizeSecondPageFromBackpackType(type) == 0){
            BackPack backPack = new BackPack(name, generateFistPage(), id, type);
            backPack.setIsBlock(false);
            Main.backPackManager.getBackpacks().put(id, backPack);
            return backPack;
        }

        BackPack backPack = new BackPack(name, generateFistPage(), generateSecondPage(), id, type);
        backPack.setIsBlock(false);
        Main.backPackManager.getBackpacks().put(id, backPack);
        return backPack;
    }

    private void generateBackpackType(){
        BackpackType[] types = BackpackType.values();
        int randomIndex = ThreadLocalRandom.current().nextInt(types.length - 1);
        type = types[randomIndex];
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


}
