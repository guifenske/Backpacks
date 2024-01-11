package br.com.backpacks.backpackUtils;

import br.com.backpacks.Main;
import br.com.backpacks.recipes.RecipesNamespaces;
import br.com.backpacks.upgrades.GetFurnace;
import br.com.backpacks.upgrades.GetJukebox;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class BackPack implements GetFurnace, GetJukebox{

    public Inventory getSecondPage() {
        return secondPage;
    }

    public BackpackType getType() {
        return backpackType;
    }

    private BackpackType backpackType;

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
            for(String item : config.getStringList(s + ".jukebox.discs")){
                discs.add(getSoundFromName(item));
            }
            setDiscs(discs);
            setPlaying(getSoundFromName(config.getString(s + ".jukebox.playing")));
        }
    }

    public BackPack deserialize(YamlConfiguration config, String s) {
        if(!config.isSet(s + ".i")){
            Main.getMain().getLogger().warning("Backpack with id " + s + " not found!");
            return null;
        }

        List<String> components = (List<String>) config.getList(s + ".i");

        if(config.isSet(s + ".u")){
            List<String> upgradesStr = (List<String>) config.getList(s + ".u");

            List<Upgrade> upgrades = new ArrayList<>();
            for (String string : upgradesStr) {
                upgrades.add(Upgrade.valueOf(string));
            }

            setUpgrades(upgrades);

            deserializeUpgrades(config, s);
        }

        List<ItemStack> list = new ArrayList<>();
        List<ItemStack> list2 = new ArrayList<>();

        name = components.get(0);
        backpackType = BackpackType.valueOf(components.get(1));
        id = Integer.parseInt(s);

        updateSizeOfPages();

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
            secondPage.setItem(secondPageSize - 1, config);
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

}