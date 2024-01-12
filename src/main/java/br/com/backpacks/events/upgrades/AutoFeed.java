package br.com.backpacks.events.upgrades;

import br.com.backpacks.Main;
import br.com.backpacks.backpackUtils.BackPack;
import br.com.backpacks.backpackUtils.BackpackAction;
import br.com.backpacks.backpackUtils.inventory.ItemCreator;
import br.com.backpacks.recipes.RecipesNamespaces;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class AutoFeed implements Listener {

    private static Set<Integer> fillSlots = Set.of(3,4,5,12,13,14,21,22,23);

    @EventHandler
    private static void tick(FoodLevelChangeEvent event){
        Player player = (Player) event.getEntity();
        if(!player.getPersistentDataContainer().has(new RecipesNamespaces().getHAS_BACKPACK())) return;
        BackPack backPack = Main.backPackManager.getBackpackFromId(player.getPersistentDataContainer().get(new RecipesNamespaces().getHAS_BACKPACK(), PersistentDataType.INTEGER));
        if(backPack.isAutoFeedEnabled() != null && backPack.isAutoFeedEnabled()){
            if(backPack.getAutoFeedItems() == null || backPack.getAutoFeedItems().isEmpty()) return;
            int need = 20 - player.getFoodLevel();
            if(event.getFoodLevel() < 20){
                for(ItemStack itemStack : backPack.getAutoFeedItems()){
                    if(itemStack == null) continue;
                    if(need < hungerPointsPerFood(itemStack)) continue;

                    if(itemStack.getAmount() == 1) backPack.getAutoFeedItems().remove(itemStack);
                    else itemStack.subtract();

                    event.setCancelled(true);

                    Main.getMain().debugMessage("Auto feed-ed " + player.getName() + ", backpack id " + backPack.getId(), "info");
                    player.setFoodLevel(player.getFoodLevel() + hungerPointsPerFood(itemStack));
                    player.setSaturation(20);
                    player.playSound(player, Sound.ENTITY_GENERIC_EAT, 1, 1);
                    return;
                }
            }
        }
    }

    public static Inventory inventory(Player player, BackPack backPack){
        Inventory inventory = Bukkit.createInventory(player, 27, "Auto Feed");
        ItemStack blank = new ItemCreator(Material.GRAY_STAINED_GLASS_PANE, "Put your food in the empty 9x9 space").get();
        ItemStack enable = new ItemCreator(Material.GREEN_STAINED_GLASS_PANE, "Enable").get();
        ItemStack disable = new ItemCreator(Material.RED_STAINED_GLASS_PANE, "Disable").get();

        for (int i = 0; i < 27; i++) {
            if(!fillSlots.contains(i)) inventory.setItem(i, blank);
        }

        if(backPack.isAutoFeedEnabled() != null && backPack.isAutoFeedEnabled())    inventory.setItem(10, disable);
        else{
            backPack.setAutoFeedEnabled(false);
            inventory.setItem(10, enable);
        }

        int i1 = 0;
        if(backPack.getAutoFeedItems() != null && !backPack.getAutoFeedItems().isEmpty()){
            for(int i : fillSlots){
                inventory.setItem(i, backPack.getAutoFeedItems().get(i1));
                i1++;
            }
        }

        return inventory;
    }

    @EventHandler
    private static void onClick(InventoryClickEvent event){
        if(event.getClickedInventory() == null) return;
        if(!BackpackAction.getAction((Player) event.getWhoClicked()).equals(BackpackAction.Action.UPGAUTOFEED)) return;
        if(event.getRawSlot() < 27 && !fillSlots.contains(event.getRawSlot()))  event.setCancelled(true);

        BackPack backPack = Main.backPackManager.getBackpackFromId(Main.backPackManager.getCurrentBackpackId().get(event.getWhoClicked().getUniqueId()));
        if(backPack == null) return;

        if(event.getRawSlot() == 10){
            if(backPack.isAutoFeedEnabled()){
                backPack.setAutoFeedEnabled(false);
                event.getClickedInventory().setItem(10, new ItemCreator(Material.GREEN_STAINED_GLASS_PANE, "Enable").get());
            }   else{
                backPack.setAutoFeedEnabled(true);
                event.getClickedInventory().setItem(10, new ItemCreator(Material.RED_STAINED_GLASS_PANE, "Disable").get());
            }
        }
    }

    @EventHandler
    private static void onClose(InventoryCloseEvent event){
        if(!BackpackAction.getAction((Player) event.getPlayer()).equals(BackpackAction.Action.UPGAUTOFEED)) return;
        BackPack backPack = Main.backPackManager.getBackpackFromId(Main.backPackManager.getCurrentBackpackId().get(event.getPlayer().getUniqueId()));

        List<ItemStack> foods = new ArrayList<>();

        for (int i : fillSlots) {
            ItemStack itemStack = event.getInventory().getItem(i);
            if (itemStack == null){
                foods.add(null);
                continue;
            }
            if (!checkFood(itemStack)) {
                event.getPlayer().getInventory().addItem(itemStack);
                foods.add(null);
                continue;
            }
            foods.add(itemStack);
        }
        backPack.setAutoFeedItems(foods);

        BackpackAction.setAction((Player) event.getPlayer(), BackpackAction.Action.NOTHING);
        BukkitTask task = new BukkitRunnable() {
            @Override
            public void run() {
                backPack.open((Player) event.getPlayer());
            }
        }.runTaskLater(Main.getMain(), 1L);
    }

    private static boolean checkFood(ItemStack itemStack){
        switch (itemStack.getType()){
            case BREAD, SUSPICIOUS_STEW, ROTTEN_FLESH, SWEET_BERRIES, SPIDER_EYE, POISONOUS_POTATO, GLOW_BERRIES, POTATO, PUFFERFISH, TROPICAL_FISH, APPLE, COOKIE, DRIED_KELP, ENCHANTED_GOLDEN_APPLE, GOLDEN_APPLE, GOLDEN_CARROT, HONEY_BOTTLE, MELON_SLICE, MUSHROOM_STEW, PUMPKIN_PIE, RABBIT_STEW, BAKED_POTATO, BEETROOT_SOUP, BEETROOT, CARROT, CHORUS_FRUIT, COOKED_BEEF, COOKED_CHICKEN, COOKED_COD, COOKED_MUTTON, COOKED_PORKCHOP, COOKED_RABBIT, COOKED_SALMON, BEEF, CHICKEN, COD, MUTTON, PORKCHOP, RABBIT, SALMON -> {
                return true;
            }
        }
        return false;
    }

    private static int hungerPointsPerFood(ItemStack itemStack){
        switch (itemStack.getType()){
            case BEETROOT, DRIED_KELP, POTATO, PUFFERFISH, TROPICAL_FISH -> {
                return 1;
            }

            case MELON_SLICE, COOKIE, MUTTON, POISONOUS_POTATO, CHICKEN, COD, SALMON, SPIDER_EYE, SWEET_BERRIES -> {
                return 2;
            }

            case CARROT, BEEF, PORKCHOP, RABBIT -> {
                return 3;
            }

            case APPLE, CHORUS_FRUIT, ENCHANTED_GOLDEN_APPLE, GOLDEN_APPLE, ROTTEN_FLESH -> {
                return 4;
            }

            case BREAD, BAKED_POTATO, COOKED_COD, COOKED_RABBIT -> {
                return 5;
            }

            case COOKED_MUTTON, COOKED_CHICKEN, BEETROOT_SOUP, COOKED_SALMON, GOLDEN_CARROT, HONEY_BOTTLE, MUSHROOM_STEW, SUSPICIOUS_STEW -> {
                return 6;
            }

            case COOKED_PORKCHOP, COOKED_BEEF, PUMPKIN_PIE -> {
                return 8;
            }

            case RABBIT_STEW -> {
                return 10;
            }
        }

        return 0;

    }

}
