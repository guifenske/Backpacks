package br.com.backpacks.backpackUtils;

import br.com.backpacks.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.loot.LootContext;
import org.bukkit.loot.LootTable;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class RandomBackpack {

    private int id;
    private String name;
    private BackpackType type;

    public BackpackType getType() {
        return type;
    }
    public RandomBackpack(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public BackPack generateBackpack(){
        generateBackpackType();
        if(Main.backPackManager.getSizeSecondPageFromBackpackType(type) == 0){
            BackPack backPack = new BackPack(name, generateFistPage(), id, type);
            Main.backPackManager.getBackpacks().put(id, backPack);
            return backPack;
        }

        BackPack backPack = new BackPack(name, generateFistPage(), generateSecondPage(), id, type);
        Main.backPackManager.getBackpacks().put(id, backPack);
        return backPack;
    }

    private void generateBackpackType(){
        BackpackType[] types = BackpackType.values();
        int randomIndex = ThreadLocalRandom.current().nextInt(types.length);
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
        firstPage.setStorageContents(loot);

        return firstPage;
    }

    private Inventory generateSecondPage() {
        return Bukkit.createInventory(null, Main.backPackManager.getSizeSecondPageFromBackpackType(type), name);
    }




}