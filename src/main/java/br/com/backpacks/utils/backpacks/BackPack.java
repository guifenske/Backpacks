package br.com.backpacks.utils.backpacks;

import br.com.backpacks.Main;
import br.com.backpacks.recipes.BackpackRecipes;
import br.com.backpacks.utils.UpgradeManager;
import br.com.backpacks.utils.inventory.ItemCreator;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Barrel;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

public final class BackPack extends UpgradeManager {
    private int id;
    private Inventory firstPage;
    private int firstPageSize;
    private int secondPageSize;
    private BackpackType backpackType;
    private Location location;
    private boolean locked;
    private Inventory secondPage;
    private Boolean isBlock = false;
    private final Set<UUID> viewersIds = new HashSet<>();
    private UUID owner;
    private boolean showNameAbove = false;
    private UUID marker;
    private String name;

    public BackPack(BackpackType type, int id) {
        this.backpackType = type;
        this.id = id;
        this.name = type.getName();
        updateSizeOfPages();
        firstPage = Bukkit.createInventory(null, firstPageSize, name);
        if(secondPageSize > 0){
            secondPage = Bukkit.createInventory(null, secondPageSize, name);
        }
        setConfigOptionItems();
    }

    public BackPack(String name, Inventory firstPage, int id, BackpackType type) {
        this.backpackType = type;
        this.firstPageSize = firstPage.getSize();
        this.name = name;
        this.firstPage = firstPage;
        this.id = id;

        updateSizeOfPages();
        setConfigOptionItems();
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
        setConfigOptionItems();
    }
    public List<String> serialize() {
        List<String> data = new ArrayList<>();
        data.add(name);
        data.add(backpackType.toString());
        return data;
    }

    public BackPack deserialize(YamlConfiguration config, String id) {
        if(!config.isSet(id + ".i")){
            Main.debugMessage("Backpack with id " + id + " not found!");
            return null;
        }

        List<String> components = config.getStringList(id + ".i");

        if(config.isSet(id + ".u")){
            List<Integer> upgradesIds = config.getIntegerList(id + ".u");
            setUpgradesIds(upgradesIds);
        }

        if(config.isSet(id + ".out")){
            setOutputUpgrade(config.getInt(id + ".out"));
        }

        if(config.isSet(id + ".inp")){
            setInputUpgrade(config.getInt(id + ".inp"));
        }

        if(config.isSet(id + ".owner")){
            setOwner(UUID.fromString(config.getString(id + ".owner")));
        }

        name = components.get(0);
        backpackType = BackpackType.valueOf(components.get(1));
        this.id = Integer.parseInt(id);

        updateSizeOfPages();

        firstPage = Bukkit.createInventory(null, firstPageSize, name);

        if(config.isSet(id + ".1")){
            Set<String> keysFirstPage = config.getConfigurationSection(id + ".1").getKeys(false);
            for(String index : keysFirstPage){
                getFirstPage().setItem(Integer.parseInt(index), config.getItemStack(id + ".1." + index));
            }
        }

        if(secondPageSize > 0) {
            secondPage = Bukkit.createInventory(null, secondPageSize, name);
            if(config.isSet(id + ".2")) {
                Set<String> keysSecondPage = config.getConfigurationSection(id + ".2").getKeys(false);

                for (String index : keysSecondPage) {
                    getSecondPage().setItem(Integer.parseInt(index), config.getItemStack(id + ".2." + index));
                }

            }
        }

        setConfigOptionItems();
        return this;
    }

    public Inventory getSecondPage() {
        return secondPage;
    }

    public BackpackType getType() {
        return backpackType;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public boolean isLocked() {
        return locked;
    }

    public Boolean isBlock() {
        return isBlock;
    }

    public void setIsBlock(Boolean block) {
        this.isBlock = block;
    }

    public Inventory getFirstPage() {
        return firstPage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSecondPageSize() {
        return secondPageSize;
    }

    public Set<UUID> getViewersIds() {
        return viewersIds;
    }

    public String getName() {
        return name;
    }

    public void setOwner(UUID owner) {
        this.owner = owner;
    }

    public UUID getOwner() {
        return owner;
    }

    public boolean isShowingNameAbove() {
        return showNameAbove;
    }

    public void setShowNameAbove(boolean showNameAbove) {
        this.showNameAbove = showNameAbove;
    }

    public UUID getMarker() {
        return marker;
    }

    public ArmorStand getMarkerEntity(){
        return (ArmorStand) Bukkit.getEntity(marker);
    }

    public void setMarker(UUID uuid) {
        this.marker = uuid;
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
        getViewersIds().add(player.getUniqueId());
        Main.backPackManager.getCurrentPage().put(player.getUniqueId(), 1);
        Main.backPackManager.getCurrentBackpackId().put(player.getUniqueId(), id);
        player.openInventory(firstPage);
        BackpackAction.setAction(player, BackpackAction.Action.OPENED);
    }

    public void openSecondPage(Player player){
        getViewersIds().add(player.getUniqueId());
        Main.backPackManager.getCurrentPage().put(player.getUniqueId(), 2);
        Main.backPackManager.getCurrentBackpackId().put(player.getUniqueId(), id);
        player.openInventory(secondPage);
        BackpackAction.setAction(player, BackpackAction.Action.OPENED);
    }

    public ItemStack getFirstItem(){
        for(ItemStack itemStack : firstPage){
            if(itemStack == null) continue;
            if(itemStack.hasItemMeta() && itemStack.getItemMeta().getPersistentDataContainer().has(BackpackRecipes.IS_CONFIG_ITEM, PersistentDataType.INTEGER)) continue;
            return itemStack;
        }

        if(getSecondPage() != null){
            for(ItemStack itemStack : secondPage){
                if(itemStack == null) continue;
                if(itemStack.hasItemMeta() && itemStack.getItemMeta().getPersistentDataContainer().has(BackpackRecipes.IS_CONFIG_ITEM, PersistentDataType.INTEGER)) continue;
                return itemStack;
            }
        }

        return null;
    }

    public ItemStack[] getStorageContentsFirstPage() {
        ItemStack[] array = firstPage.getContents();
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
        ItemStack[] array = secondPage.getContents();
        int length = array.length;
        ItemStack[] list = new ItemStack[length - 2];

        System.arraycopy(array, 0, list, 0, length - 2);
        return list;
    }


    public void setConfigOptionItems(){
        ItemStack arrowLeft = new ItemCreator(Material.ARROW, "§aPrevious Page").build();
        ItemStack arrowRight = new ItemCreator(Material.ARROW, "§aNext Page").build();
        ItemStack config = new ItemCreator(Material.NETHER_STAR, "§6Config").build();

        firstPage.setItem(firstPageSize - 1, config);

        if(secondPageSize > 0){
            firstPage.setItem(firstPageSize - 2, arrowRight);
            secondPage.setItem(secondPageSize - 2, arrowLeft);
            secondPage.setItem(secondPageSize - 1, config);
        }
    }

    public boolean containsItem(ItemStack itemStack){
        for(ItemStack item : firstPage){
            if(item == null)    continue;
            if(item.isSimilar(itemStack)) return true;
        }
        if(secondPageSize > 0){
            for(ItemStack item : secondPage){
                if(item == null)    continue;
                if(item.isSimilar(itemStack)) return true;
            }
        }
        return false;
    }
    public List<ItemStack> tryAddItem(ItemStack itemStack){
        List<ItemStack> list = new ArrayList<>();
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
                return list2;
            }
        }

        return list;
    }

    public List<ItemStack> getBackpackItems(){
        List<ItemStack> list = new ArrayList<>();

        for(ItemStack itemStack : firstPage){
            if(itemStack == null) continue;
            if(itemStack.hasItemMeta() && itemStack.getItemMeta().getPersistentDataContainer().has(BackpackRecipes.IS_CONFIG_ITEM, PersistentDataType.INTEGER)) continue;
            list.add(itemStack);
        }

        if(secondPage != null){
            for(ItemStack itemStack : secondPage){
                if(itemStack == null) continue;
                if(itemStack.hasItemMeta() && itemStack.getItemMeta().getPersistentDataContainer().has(BackpackRecipes.IS_CONFIG_ITEM, PersistentDataType.INTEGER)) continue;
                list.add(itemStack);
            }
        }

        return list;
    }

    public void updateBarrelBlock(){
        if(!isBlock()) return;
        Barrel barrel = (Barrel) location.getBlock().getState();
        List<ItemStack> bpItems = getBackpackItems();

        if(bpItems.isEmpty()){
            barrel.getInventory().clear();
            return;
        }

        barrel.getInventory().clear();
        for(int i = 0 ; i < bpItems.size() ; i++){
            barrel.getInventory().setItem(i, bpItems.get(i));
        }
    }

    public void removeBackpackItem(Player player){
        for(ItemStack stack : player.getInventory().getContents()){
            if(stack == null) continue;
            if(!stack.getType().equals(Material.BARREL)) continue;
            if(!stack.hasItemMeta()) continue;
            if(!stack.getItemMeta().getPersistentDataContainer().has(BackpackRecipes.BACKPACK_ID)) continue;
            if(stack.getItemMeta().getPersistentDataContainer().get(BackpackRecipes.BACKPACK_ID, PersistentDataType.INTEGER).equals(id)){
                player.getInventory().removeItem(stack);
            }
        }
    }
}