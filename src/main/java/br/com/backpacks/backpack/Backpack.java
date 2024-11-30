package br.com.backpacks.backpack;

import br.com.backpacks.Main;
import br.com.backpacks.recipes.BackpackRecipes;
import br.com.backpacks.storage.SerializationUtils;
import br.com.backpacks.upgrades.Upgrade;
import br.com.backpacks.upgrades.UpgradeManager;
import br.com.backpacks.upgrades.UpgradeType;
import br.com.backpacks.menu.*;
import br.com.backpacks.menu.backpacksMenus.BackpackConfigMenu;
import br.com.backpacks.menu.backpacksMenus.UpgradesInputOutputMenu;
import br.com.backpacks.menu.backpacksMenus.UpgradesMenu;
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

public final class Backpack {
    private int id;
    private Inventory firstPage;
    private Inventory secondPage;
    private int firstPageSize;
    private int secondPageSize;
    private BackpackType backpackType;
    private Location location;
    private boolean locked;
    private boolean isBlock = false;
    private UUID owner;
    private boolean showNameAbove = false;
    private UUID marker;
    private String name;
    private BackpackConfigMenu configMenu;
    private UpgradesMenu upgradesMenu;
    private UpgradesInputOutputMenu upgradesInputOutputMenu;
    private Integer inputUpgrade = -1;
    private Integer outputUpgrade = -1;
    private final Set<UUID> viewersIds = new HashSet<>();
    private final List<Integer> backpackUpgradesIds = new ArrayList<>();

    public Backpack(BackpackType type, int id) {
        this.backpackType = type;
        this.id = id;
        this.name = type.getName();

        updateSizeOfPages();

        firstPage = Bukkit.createInventory(null, firstPageSize, name);

        if(secondPageSize > 0){
            secondPage = Bukkit.createInventory(null, secondPageSize, name);
        }

        setConfigItems();

        configMenu = new BackpackConfigMenu(this);
        upgradesMenu = new UpgradesMenu(this);
        upgradesInputOutputMenu = new UpgradesInputOutputMenu(this);
    }

    public Backpack(String name, Inventory firstPage, int id, BackpackType type) {
        this.backpackType = type;
        this.firstPageSize = firstPage.getSize();
        this.name = name;
        this.firstPage = firstPage;
        this.id = id;

        configMenu = new BackpackConfigMenu(this);
        upgradesMenu = new UpgradesMenu(this);
        upgradesInputOutputMenu = new UpgradesInputOutputMenu(this);

        updateSizeOfPages();
        setConfigItems();
    }

    public Backpack() {
    }

    public Backpack(String name, Inventory firstPage, Inventory secondPage, int id, BackpackType type) {
        this.backpackType = type;
        this.firstPageSize = firstPage.getSize();
        this.secondPageSize = secondPage.getSize();
        this.name = name;
        this.secondPage = secondPage;
        this.firstPage = firstPage;
        this.id = id;

        configMenu = new BackpackConfigMenu(this);
        upgradesMenu = new UpgradesMenu(this);
        upgradesInputOutputMenu = new UpgradesInputOutputMenu(this);

        updateSizeOfPages();
        setConfigItems();
    }

    public List<String> serialize() {
        List<String> data = new ArrayList<>();
        data.add(name);
        data.add(backpackType.toString());
        return data;
    }

    public Backpack deserialize(YamlConfiguration config, String id) {
        if(!config.isSet(id + ".i")){
            Main.debugMessage("Backpack with id " + id + " not found!");
            return null;
        }

        List<String> components = config.getStringList(id + ".i");

        if(config.isSet(id + ".u")){
            List<Integer> upgradesIds = config.getIntegerList(id + ".u");

            upgradesIds.removeIf(upgradeId -> !UpgradeManager.getUpgrades().containsKey(upgradeId));
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

        if(config.isSet(id + ".loc")){
            setLocation(SerializationUtils.deserializeLocationAsList(config.getStringList(id + ".loc")));
            setIsBlock(true);

            if(config.isSet(id + ".shownameabove")){
                setShowNameAbove(true);
            }

            Main.backpackManager.getBackpacksPlacedLocations().put(getLocation(), getId());
        }

        configMenu = new BackpackConfigMenu(this);
        upgradesMenu = new UpgradesMenu(this);
        upgradesInputOutputMenu = new UpgradesInputOutputMenu(this);

        setConfigItems();
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
        firstPageSize = getType().getFirstPageSize();
        secondPageSize = getType().getSecondPageSize();
    }

    public void open(Player player){
        getViewersIds().add(player.getUniqueId());
        Main.backpackManager.setPlayerCurrentPage(player, 1);
        Main.backpackManager.setPlayerCurrentBackpack(player, this);
        player.openInventory(firstPage);
        BackpackAction.setAction(player, BackpackAction.Action.OPENED);
    }

    public void openSecondPage(Player player){
        getViewersIds().add(player.getUniqueId());
        Main.backpackManager.setPlayerCurrentPage(player, 2);
        Main.backpackManager.setPlayerCurrentBackpack(player, this);
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


    public void setConfigItems(){
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

        barrel.getInventory().clear();

        if(bpItems.isEmpty()){
            return;
        }

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

    public BackpackConfigMenu getConfigMenu(){
        return configMenu;
    }

    public UpgradesMenu getUpgradesMenu(){
        return upgradesMenu;
    }

    public UpgradesInputOutputMenu getUpgradesInputOutputMenu() {
        return upgradesInputOutputMenu;
    }

    /**
     * Use only when the Menus have no viewers on it.
     */
    public void updateMenuTitles(){
        configMenu = new BackpackConfigMenu(this);
        upgradesMenu = new UpgradesMenu(this);
        upgradesInputOutputMenu = new UpgradesInputOutputMenu(this);
    }

    public Menu getPlayerCurrentMenu(Player player){
        switch (BackpackAction.getAction(player)){
            case UPGMENU -> {
                return upgradesMenu;
            }

            case CONFIGMENU -> {
                return configMenu;
            }

            case IOMENU -> {
                return upgradesInputOutputMenu;
            }

            case EDITINPUT, EDITOUTPUT -> {
                return upgradesInputOutputMenu.getEditInputOutputMenu();
            }
        }

        return null;
    }

    public List<Integer> getUpgradesIds() {
        return backpackUpgradesIds;
    }

    public List<Upgrade> getBackpackUpgrades() {
        if(this.backpackUpgradesIds.isEmpty()) return new ArrayList<>();
        List<Upgrade> upgrades1 = new ArrayList<>();

        for (Integer upgradeId : backpackUpgradesIds) {
            upgrades1.add(UpgradeManager.getUpgradeFromId(upgradeId));
        }

        return upgrades1;
    }

    public void setUpgradesIds(List<Integer> list){
        this.backpackUpgradesIds.clear();
        this.backpackUpgradesIds.addAll(list);
    }

    public Boolean containsUpgradeType(UpgradeType upgradeType) {
        for(Upgrade upgrade1 : getBackpackUpgrades()) {
            if(upgrade1.getType() == upgradeType) {
                return true;
            }
        }
        return false;
    }

    public Upgrade getFirstUpgradeFromType(UpgradeType type){
        for(Upgrade upgrade : getBackpackUpgrades()) {

            if(upgrade.getType() == type) {
                return upgrade;
            }

        }

        return null;
    }

    public void stopTickingAllUpgrades(){
        for(Upgrade upgrade : getBackpackUpgrades()){
            upgrade.stopTicking();
        }
    }

    public Integer getInputUpgrade() {
        return inputUpgrade;
    }

    public Integer getOutputUpgrade() {
        return outputUpgrade;
    }

    public void setInputUpgrade(int inputUpgrade){
        this.inputUpgrade = inputUpgrade;
    }

    public void setOutputUpgrade(int outputUpgrade){
        this.outputUpgrade = outputUpgrade;
    }

}