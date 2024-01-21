package br.com.backpacks.backpackUtils;

import br.com.backpacks.Main;
import br.com.backpacks.backpackUtils.inventory.ItemCreator;
import br.com.backpacks.recipes.RecipesNamespaces;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class BackPack extends UpgradeManager {

    public Inventory getSecondPage() {
        return secondPage;
    }

    public BackpackType getType() {
        return backpackType;
    }

    private BackpackType backpackType;

    public int getConfigItemsSpace() {
        return configItemsSpace;
    }

    private int configItemsSpace = 0;

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    private Location location;

    private boolean locked;

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public boolean isLocked() {
        return locked;
    }

    private Inventory secondPage;

    public Boolean isBlock() {
        return block;
    }

    public void setIsBlock(Boolean block) {
        this.block = block;
    }

    private Boolean block = false;

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

    public boolean isBeingWorn() {
        return isWorn;
    }

    public void setBeingWorn(boolean worn) {
        isWorn = worn;
    }

    private boolean isWorn = false;

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

    private String getDefaultName(){
        switch (getType()){
            case LEATHER -> {
                return "Leather Backpack";
            }
            case IRON -> {
                return "Iron Backpack";
            }
            case GOLD -> {
                return "Gold Backpack";
            }
            case LAPIS -> {
                return "Lapis Backpack";
            }
            case AMETHYST -> {
                return "Amethyst Backpack";
            }
            case DIAMOND -> {
                return "Diamond Backpack";
            }
            case NETHERITE -> {
                return "Netherite Backpack";
            }
        }

        return "Backpack";
    }

    private String name;
    public BackPack(BackpackType type, int id) {
        this.backpackType = type;
        this.id = id;
        this.name = getDefaultName();
        updateSizeOfPages();
        firstPage = Bukkit.createInventory(null, firstPageSize, name);
        if(secondPageSize > 0){
            secondPage = Bukkit.createInventory(null, secondPageSize, name);
        }
        setArrowsAndConfigOptionItems();
    }

    public BackPack(String name, Inventory firstPage, int id, BackpackType type) {
        this.backpackType = type;
        this.firstPageSize = firstPage.getSize();
        this.name = name;
        this.firstPage = firstPage;
        this.id = id;

        updateSizeOfPages();
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

        updateSizeOfPages();
        setArrowsAndConfigOptionItems();
    }
    public List<String> serialize() {
        List<String> data = new ArrayList<>();
        data.add(name);
        data.add(backpackType.toString());
        return data;
    }

    public BackPack deserialize(YamlConfiguration config, String s) {
        if(!config.isSet(s + ".i")){
            Main.getMain().debugMessage("Backpack with id " + s + " not found!");
            return null;
        }

        List<String> components = config.getStringList(s + ".i");

        if(config.isSet(s + ".u")){
            List<Integer> upgradesIds = config.getIntegerList(s + ".u");
            setUpgrades(upgradesIds);
        }

        name = components.get(0);
        backpackType = BackpackType.valueOf(components.get(1));
        id = Integer.parseInt(s);

        updateSizeOfPages();

        firstPage = Bukkit.createInventory(null, firstPageSize, name);

        if(config.isSet(s + ".1")){
            Set<String> keysFirstPage = config.getConfigurationSection(s + ".1").getKeys(false);
            for(String index : keysFirstPage){
                getFirstPage().setItem(Integer.parseInt(index), config.getItemStack(s + ".1." + index));
            }
        }

        if(secondPageSize > 0) {
            secondPage = Bukkit.createInventory(null, secondPageSize, name);
            if(config.isSet(s + ".2")) {
                Set<String> keysSecondPage = config.getConfigurationSection(s + ".2").getKeys(false);

                for (String index : keysSecondPage) {
                    getSecondPage().setItem(Integer.parseInt(index), config.getItemStack(s + ".2." + index));
                }

            }
        }

        setArrowsAndConfigOptionItems();
        return this;
    }

    public NamespacedKey getNamespaceOfBackpackType() {

        switch (getType()) {
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

    private void updateSizeOfPages(){
        switch (getType()){
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
        Main.backPackManager.getCurrentPage().put(player.getUniqueId(), 1);
        Main.backPackManager.getCurrentBackpackId().put(player.getUniqueId(), id);
        BackpackAction.removeAction(player);
        player.openInventory(firstPage);
        BackpackAction.setAction(player, BackpackAction.Action.OPENED);
    }

    public void openSecondPage(Player player){
        Main.backPackManager.getCurrentPage().put(player.getUniqueId(), 2);
        Main.backPackManager.getCurrentBackpackId().put(player.getUniqueId(), id);
        BackpackAction.removeAction(player);
        player.openInventory(secondPage);
        BackpackAction.setAction(player, BackpackAction.Action.OPENED);
    }

    public List<ItemStack> getStorageContentsFirstPageWithoutNulls() {
        List<ItemStack> list = new ArrayList<>();
        for(ItemStack itemStack : firstPage.getStorageContents()){
            if(itemStack == null) continue;
            list.add(itemStack);
        }
        return list;
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
        ItemStack[] array = secondPage.getStorageContents();
        int length = array.length;
        ItemStack[] list = new ItemStack[length - 2];

        System.arraycopy(array, 0, list, 0, length - 2);
        return list;
    }


    public void setArrowsAndConfigOptionItems(){
        ItemStack arrowLeft = new ItemCreator(Material.ARROW, "§aPrevious").get();
        ItemStack arrowRight = new ItemCreator(Material.ARROW, "§aNext").get();
        ItemStack config = new ItemCreator(Material.NETHER_STAR, "§6Config").get();

        firstPage.setItem(firstPageSize - 1, config);
        configItemsSpace = 1;

        if(secondPageSize > 0){
            firstPage.setItem(firstPageSize - 2, arrowRight);
            secondPage.setItem(secondPageSize - 2, arrowLeft);
            secondPage.setItem(secondPageSize - 1, config);
            configItemsSpace = 2;
        }
    }

    public boolean containsItem(ItemStack itemStack){
        for(ItemStack item : firstPage.getStorageContents()){
            if(item == null)    continue;
            if(item.isSimilar(itemStack)) return true;
        }
        if(secondPageSize > 0){
            for(ItemStack item : secondPage.getStorageContents()){
                if(item == null)    continue;
                if(item.isSimilar(itemStack)) return true;
            }
        }
        return false;
    }

    //used in the PickupItemEvent
    public List<ItemStack> getRemainingItems() {
        return remainingItems;
    }

    public void setRemainingItems(List<ItemStack> remainingItems) {
        this.remainingItems = remainingItems;
    }

    private List<ItemStack> remainingItems = new ArrayList<>();

    public List<ItemStack> tryAddItem(ItemStack itemStack){
        List<ItemStack> list = new ArrayList<>();
        remainingItems = list;
        if(itemStack == null) return list;

        if(!firstPage.addItem(itemStack).isEmpty()){
            list.addAll(firstPage.addItem(itemStack).values());
            if(secondPageSize > 0){
                List<ItemStack> list2 = new ArrayList<>();
                for(ItemStack item : list){
                    if(!secondPage.addItem(item).isEmpty()){
                        list2.addAll(secondPage.addItem(item).values());
                    }
                }
                remainingItems = list2;
                return list2;
            }
        }

        remainingItems = list;
        return list;
    }
}