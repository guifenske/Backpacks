package br.com.backpacks.backpackUtils;

import br.com.backpacks.recipes.RecipesNamespaces;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BackPack implements Serializable {

    public Inventory getSecondPage() {
        return secondPage;
    }

    public BackpackType getBackpackType() {
        return backpackType;
    }

    private BackpackType backpackType;

    private Inventory secondPage;

    public Inventory getFirstPage() {
        return firstPage;
    }

    public int getId() {
        return id;
    }

    private int id;

    private Inventory firstPage;

    public int getSecondPageSize() {
        return secondPageSize;
    }

    private int firstPageSize;
    private int secondPageSize;

    public String getName() {
        return name;
    }
    private String name;

    public BackPack(String name, Inventory firstPage, int id, BackpackType type) {
        this.backpackType = type;
        this.firstPageSize = firstPage.getSize();
        this.name = name;
        this.firstPage = firstPage;
        this.id = id;
    }

    public BackPack() {

    }

    public BackPack(String name, Inventory firstPage, Inventory secondPage, int id, BackpackType type) {
        this.backpackType = type;
        this.firstPageSize = firstPage.getSize();
        this.secondPageSize = secondPage.getSize();
        this.name = name;
        this.secondPage = secondPage;
        this.firstPage = firstPage;
        this.id = id;
    }
    public List<String> serialize() {
        List<String> data = new ArrayList<>();
        data.add(name);
        data.add(backpackType.toString());
        return data;
    }

    public BackPack deserialize(YamlConfiguration config, String s) {
        List<String> components = (List<String>) config.getList(s + ".i");

        if(!config.isSet(s + ".i")){
            Bukkit.getConsoleSender().sendMessage(s + ".i" + " not found in the config, please report to the devs");
            return null;
        }

        List<ItemStack> list = new ArrayList<>();
        List<ItemStack> list2 = new ArrayList<>();

        name = components.get(0);
        backpackType = BackpackType.valueOf(components.get(1));
        id = Integer.parseInt(s);

        updateSizeOfPages(backpackType);

        firstPage = Bukkit.createInventory(null, firstPageSize, name);

        for(Object item : config.getList(s + ".1")){
            list.add((ItemStack) item);
        }

        firstPage.setStorageContents(list.toArray(new ItemStack[0]));

        if(secondPageSize > 0) {
            secondPage = Bukkit.createInventory(null, secondPageSize, name);
            for (Object item : config.getList(s + ".2")) {
                list2.add((ItemStack) item);
            }

            secondPage.setStorageContents(list2.toArray(new ItemStack[0]));
        }

        return this;
    }

    public NamespacedKey getNamespaceOfBackpackType() {

        switch (getBackpackType()) {
            case LEATHER -> {
                return new RecipesNamespaces().getNAMESPACE_LEATHER_BACKPACK();
            }
            case IRON -> {
                return new RecipesNamespaces().getNAMESPACE_IRON_BACKPACK();
            }
            case GOLD -> {
                return new RecipesNamespaces().getNAMESPACE_GOLD_BACKPACK();
            }
            case LAPIS -> {
                return new RecipesNamespaces().getNAMESPACE_LAPIS_BACKPACK();
            }
            case AMETHYST -> {
                return new RecipesNamespaces().getNAMESPACE_AMETHYST_BACKPACK();
            }
            case DIAMOND -> {
                return new RecipesNamespaces().getNAMESPACE_DIAMOND_BACKPACK();
            }
            case NETHERITE -> {
                return new RecipesNamespaces().getNAMESPACE_NETHERITE_BACKPACK();
            }
        }

        return null;
    }

    private void updateSizeOfPages(BackpackType type){
        switch (type){
            case LEATHER -> firstPageSize = 18;
            case IRON -> firstPageSize = 27;
            case GOLD -> firstPageSize = 36;
            case LAPIS -> firstPageSize = 45;
            case AMETHYST -> firstPageSize = 54;
            case DIAMOND ->{
                firstPageSize = 54;
                secondPageSize = 27;
            }
            case NETHERITE ->{
                firstPageSize = 54;
                secondPageSize = 54;
            }
        }
    }

    public void open(Player player){
        player.openInventory(firstPage);
    }

}
