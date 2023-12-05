package br.com.backpacks.backpackUtils;

import br.com.backpacks.Main;
import br.com.backpacks.recipes.RecipesNamespaces;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class BackPack{

    public Inventory getSecondPage() {
        return secondPage;
    }

    public BackpackType getBackpackType() {
        return backpackType;
    }

    private BackpackType backpackType;

    private Inventory secondPage;

    public Boolean getIsBlock() {
        return isBlock;
    }

    public void setIsBlock(Boolean block) {
        isBlock = block;
    }

    private Boolean isBlock = false;

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

    public void setName(String name) {
        this.name = name;
        Inventory newFirstPage = Bukkit.createInventory(null, firstPageSize, name);
        newFirstPage.setStorageContents(firstPage.getStorageContents());


        firstPage = newFirstPage;

        if(secondPageSize > 0){
            Inventory newSecondPage = Bukkit.createInventory(null, secondPageSize, name);
            newSecondPage.setStorageContents(secondPage.getStorageContents());
            secondPage = newSecondPage;
        }
    }

    private String name;

    public BackPack(String name, Inventory firstPage, int id, BackpackType type) {
        this.backpackType = type;
        this.firstPageSize = firstPage.getSize();
        this.name = name;
        this.firstPage = firstPage;
        this.id = id;

        setArrowsAndConfigOptionItems();
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

        setArrowsAndConfigOptionItems();
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

        setArrowsAndConfigOptionItems();

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
        Main.backPackManager.isInBackpack.put(player.getUniqueId(), id);
        player.openInventory(firstPage);
    }

    public void openSecondPage(Player player){
        Main.backPackManager.isInBackpack.put(player.getUniqueId(), id);
        player.openInventory(secondPage);
    }

    public ItemStack[] getStorageContentsFirstPage() {
        ItemStack[] array = firstPage.getStorageContents();
        int length = array.length;

        if (secondPageSize > 0) {
            ItemStack[] list = new ItemStack[length - 2];
            System.arraycopy(array, 0, list, 0, length - 2);
            return list;
        }

        ItemStack[] list = new ItemStack[length - 1];
        System.arraycopy(array, 0, list, 0, length - 1);
        return list;
    }

    public ItemStack[] getStorageContentsSecondPage() {
        ItemStack[] array = firstPage.getStorageContents();
        int length = array.length;
        ItemStack[] list = new ItemStack[length - 2];

        System.arraycopy(array, 0, list, 0, length - 2);
        return list;
    }


    public void setArrowsAndConfigOptionItems(){
        ItemStack arrowLeft = new ItemStack(Material.ARROW);
        ItemMeta arrowLeftMeta = arrowLeft.getItemMeta();
        arrowLeftMeta.setDisplayName("§cBack");
        arrowLeftMeta.getPersistentDataContainer().set(new RecipesNamespaces().getIS_CONFIG_ITEM(), PersistentDataType.INTEGER, 1);
        arrowLeft.setItemMeta(arrowLeftMeta);

        ItemStack arrowRight = new ItemStack(Material.ARROW);
        ItemMeta arrowRightMeta = arrowRight.getItemMeta();
        arrowRightMeta.setDisplayName("§aNext");
        arrowRightMeta.getPersistentDataContainer().set(new RecipesNamespaces().getIS_CONFIG_ITEM(), PersistentDataType.INTEGER, 1);
        arrowRight.setItemMeta(arrowRightMeta);

        ItemStack config = new ItemStack(Material.NETHER_STAR);
        ItemMeta configMeta = config.getItemMeta();
        configMeta.setDisplayName("§6Config");
        configMeta.getPersistentDataContainer().set(new RecipesNamespaces().getIS_CONFIG_ITEM(), PersistentDataType.INTEGER, 1);
        config.setItemMeta(configMeta);

        firstPage.setItem(firstPageSize - 1, config);

        if(secondPageSize > 0){
            firstPage.setItem(firstPageSize - 2, arrowRight);
            secondPage.setItem(secondPageSize - 2, arrowLeft);
            secondPage.setItem(firstPageSize - 1, config);
        }
    }
}
