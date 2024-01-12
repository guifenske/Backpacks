package br.com.backpacks.backpackUtils;

import br.com.backpacks.Main;
import br.com.backpacks.backpackUtils.inventory.ItemCreator;
import br.com.backpacks.recipes.RecipesNamespaces;
import br.com.backpacks.upgrades.GetAutoFeed;
import br.com.backpacks.upgrades.GetFurnace;
import br.com.backpacks.upgrades.GetJukebox;
import org.bukkit.*;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class BackPack implements GetFurnace, GetJukebox, GetAutoFeed {

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

    private List<Upgrade> upgrades;

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

    public boolean isBeingWeared() {
        return isBeingWeared;
    }

    public void setBeingWeared(boolean beingWeared) {
        isBeingWeared = beingWeared;
    }

    private boolean isBeingWeared = false;

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

    public List<String> serializeUpgrades(){
        List<String> list = new ArrayList<>();
        for(Upgrade upgrade : upgrades){
            list.add(upgrade.toString());
        }

        return list;
    }

    public List<String> serializeDiscs(){
        List<String> list = new ArrayList<>();
        for(ItemStack itemStack : getDiscs()){
            if(itemStack == null){
                list.add(null);
                continue;
            }
            list.add(itemStack.getType().name());
        }
        return list;
    }

    private void deserializeUpgrades(YamlConfiguration config, String s){
        if(containsUpgrade(Upgrade.FURNACE)){
            setFuel(config.getItemStack(s + ".furnace.f"));
            setSmelting(config.getItemStack(s + ".furnace.s"));
            setResult(config.getItemStack(s + ".furnace.r"));
        }
        if(containsUpgrade(Upgrade.JUKEBOX)){
            List<ItemStack> discs = new ArrayList<>();
            if(config.isSet(s + ".jukebox.playing"))    setPlaying(getSoundFromName(config.getString(s + ".jukebox.playing")));
            if(config.isSet(s + ".jukebox.discs")) {
                for (String item : config.getStringList(s + ".jukebox.discs")) {
                    discs.add(getSoundFromName(item));
                }
            }
            setDiscs(discs);
        }
        if(containsUpgrade(Upgrade.AUTOFEED)){
            if(config.isSet(s + ".afeed.enabled"))  setAutoFeedEnabled(config.getBoolean(s + ".afeed.enabled"));
            List<ItemStack> items = new ArrayList<>();
            if(config.isSet(s + ".afeed.items")) {
                for (Object item : config.getList(s + ".afeed.items")) {
                    items.add((ItemStack) item);
                }
            }
            setAutoFeedItems(items);
        }
    }

    public BackPack deserialize(YamlConfiguration config, String s) {
        if(!config.isSet(s + ".i")){
            Main.getMain().debugMessage("Backpack with id " + s + " not found!", "warning");
            return null;
        }

        List<String> components = config.getStringList(s + ".i");

        if(config.isSet(s + ".u")){
            List<String> upgradesStr = config.getStringList(s + ".u");

            List<Upgrade> upgrades = new ArrayList<>();
            for (String string : upgradesStr) {
                upgrades.add(Upgrade.valueOf(string));
            }

            setUpgrades(upgrades);

            deserializeUpgrades(config, s);
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
            Set<String> keysSecondPage = config.getConfigurationSection(s + ".2").getKeys(false);
            secondPage = Bukkit.createInventory(null, secondPageSize, name);

            if(config.isSet(s + ".2")){
                for(String index : keysSecondPage){
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
        BackpackAction.setAction(player, BackpackAction.Action.NOTHING);
        player.openInventory(firstPage);
        BackpackAction.setAction(player, BackpackAction.Action.OPENED);
    }

    public void openSecondPage(Player player){
        Main.backPackManager.getCurrentPage().put(player.getUniqueId(), 2);
        Main.backPackManager.getCurrentBackpackId().put(player.getUniqueId(), id);
        BackpackAction.setAction(player, BackpackAction.Action.NOTHING);
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

    public List<Upgrade> getUpgrades() {
        return upgrades;
    }

    public void setUpgrades(List<Upgrade> upgrades) {
        this.upgrades = upgrades;
    }

    public Boolean containsUpgrade(Upgrade upgrade) {
        return this.upgrades.contains(upgrade);
    }


    //Upgrades methods
    //Jukebox

    private ItemStack playing;

    private Boolean isPlaying = false;

    private List<ItemStack> discs;

    private Sound sound;

    @Override
    public ItemStack getPlaying() {
        return playing;
    }

    @Override
    public void setPlaying(ItemStack currentDisk) {
        this.playing = currentDisk;
    }

    @Override
    public Boolean isPlaying() {
        return isPlaying;
    }

    @Override
    public void setIsPlaying(Boolean playing) {
        this.isPlaying = playing;
    }

    @Override
    public List<ItemStack> getDiscs() {
        return discs;
    }

    @Override
    public void setDiscs(List<ItemStack> discs) {
        this.discs = discs;
    }

    @Override
    public Sound getSound() {
        return sound;
    }

    @Override
    public void setSound(Sound sound) {
        this.sound = sound;
    }

    @Override
    public ItemStack getSoundFromName(String name){
        return new ItemStack(Material.getMaterial(name));
    }

    //furnace

    private ItemStack fuel;
    private ItemStack smelting;
    private ItemStack result;

    @Override
    public ItemStack getFuel() {
        return fuel;
    }

    @Override
    public void setFuel(ItemStack fuel) {
        this.fuel = fuel;
    }

    @Override
    public ItemStack getSmelting() {
        return smelting;
    }

    public void setSmelting(ItemStack smelting) {
        this.smelting = smelting;
    }

    @Override
    public ItemStack getResult() {
        return result;
    }

    @Override
    public void setResult(ItemStack result) {
        this.result = result;
    }

    //auto-feed

    private List<ItemStack> autoFeedItems;
    private Boolean autoFeedEnabled;

    @Override
    public List<ItemStack> getAutoFeedItems() {
        return autoFeedItems;
    }

    @Override
    public Boolean isAutoFeedEnabled() {
        return autoFeedEnabled;
    }

    @Override
    public void setAutoFeedEnabled(Boolean bool) {
        this.autoFeedEnabled = bool;
    }

    @Override
    public void setAutoFeedItems(List<ItemStack> items) {
        this.autoFeedItems = items;
    }
}