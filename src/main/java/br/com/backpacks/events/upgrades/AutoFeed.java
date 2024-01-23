package br.com.backpacks.events.upgrades;

import br.com.backpacks.Main;
import br.com.backpacks.backpackUtils.BackPack;
import br.com.backpacks.backpackUtils.BackpackAction;
import br.com.backpacks.backpackUtils.Upgrade;
import br.com.backpacks.backpackUtils.UpgradeType;
import br.com.backpacks.backpackUtils.inventory.ItemCreator;
import br.com.backpacks.recipes.RecipesNamespaces;
import br.com.backpacks.upgrades.AutoFeedUpgrade;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class AutoFeed implements Listener {

    public static final Set<Integer> fillSlots = Set.of(3,4,5,12,13,14,21,22,23);

    @EventHandler
    private static void tick(FoodLevelChangeEvent event){
        Player player = (Player) event.getEntity();
        if(!player.getPersistentDataContainer().has(new RecipesNamespaces().getHAS_BACKPACK())) return;
        BackPack backPack = Main.backPackManager.getBackpackFromId(player.getPersistentDataContainer().get(new RecipesNamespaces().getHAS_BACKPACK(), PersistentDataType.INTEGER));
        List<Upgrade> list = backPack.getUpgradesFromType(UpgradeType.AUTOFEED);
        if(list.isEmpty()) return;
        AutoFeedUpgrade upgrade = (AutoFeedUpgrade) list.get(0);

        if(upgrade.isEnabled()){
            if(upgrade.getItems() == null || upgrade.getItems().isEmpty()) return;
            int need = 20 - player.getFoodLevel();
            if(event.getFoodLevel() < 20){
                for(ItemStack itemStack : upgrade.getItems()){
                    if(itemStack == null) continue;
                    if(need < hungerPointsPerFood(itemStack) && player.getHealth() == player.getMaxHealth()) continue;

                    if(itemStack.getAmount() == 1) upgrade.getItems().remove(itemStack);
                    else itemStack.subtract();

                    event.setCancelled(true);

                    Main.getMain().debugMessage("Auto feed-ed " + player.getName() + ", backpack id " + backPack.getId());
                    player.setFoodLevel(player.getFoodLevel() + hungerPointsPerFood(itemStack));
                    player.setSaturation(player.getSaturation() + saturationPointsPerFood(itemStack));
                    applyEffectPerFood(player, itemStack);
                    player.getWorld().playSound(player.getLocation(), Sound.ENTITY_GENERIC_EAT, SoundCategory.MASTER ,1, 1);
                    return;
                }
            }
        }
    }

    @EventHandler
    private static void onClick(InventoryClickEvent event){
        if(event.getClickedInventory() == null) return;
        if(!BackpackAction.getAction((Player) event.getWhoClicked()).equals(BackpackAction.Action.UPGAUTOFEED)) return;
        boolean canUse = event.getWhoClicked().getPersistentDataContainer().has(new RecipesNamespaces().getHAS_BACKPACK());
        if(canUse)  canUse = Main.backPackManager.getCurrentBackpackId().get(event.getWhoClicked().getUniqueId()) == event.getWhoClicked().getPersistentDataContainer().get(new RecipesNamespaces().getHAS_BACKPACK(), PersistentDataType.INTEGER);

        if(!canUse){
            event.setCancelled(true);
            event.getWhoClicked().sendMessage("§cYou can't use this upgrade because this backpack is not in your back.");
            return;
        }
        if(event.getRawSlot() < 27 && !fillSlots.contains(event.getRawSlot()))  event.setCancelled(true);
        BackPack backPack = Main.backPackManager.getBackpackFromId(Main.backPackManager.getCurrentBackpackId().get(event.getWhoClicked().getUniqueId()));
        List<Upgrade> list = backPack.getUpgradesFromType(UpgradeType.AUTOFEED);
        AutoFeedUpgrade upgrade = (AutoFeedUpgrade) list.get(0);

        if(event.getRawSlot() == 10){
            upgrade.setEnabled(!upgrade.isEnabled());
            upgrade.updateInventory();
        }
    }

    @EventHandler
    private static void onClose(InventoryCloseEvent event){
        if(!BackpackAction.getAction((Player) event.getPlayer()).equals(BackpackAction.Action.UPGAUTOFEED)) return;
        BackPack backPack = Main.backPackManager.getBackpackFromId(Main.backPackManager.getCurrentBackpackId().get(event.getPlayer().getUniqueId()));
        List<Upgrade> list = backPack.getUpgradesFromType(UpgradeType.AUTOFEED);
        AutoFeedUpgrade upgrade = (AutoFeedUpgrade) list.get(0);
        List<ItemStack> foods = new ArrayList<>();

        for (int i : fillSlots) {
            ItemStack itemStack = event.getInventory().getItem(i);
            if (itemStack == null){
                foods.add(null);
                continue;
            }
            if (!checkFood(itemStack)) {
                event.getPlayer().getInventory().addItem(itemStack);
                event.getInventory().remove(itemStack);
                foods.add(null);
                continue;
            }
            foods.add(itemStack);
        }
        upgrade.setItems(foods);

        BackpackAction.removeAction((Player) event.getPlayer());
        BukkitTask task = new BukkitRunnable() {
            @Override
            public void run() {
                backPack.open((Player) event.getPlayer());
            }
        }.runTaskLater(Main.getMain(), 1L);
    }

    public static boolean checkFood(ItemStack itemStack){
        switch (itemStack.getType()){
            case BREAD, SUSPICIOUS_STEW, ROTTEN_FLESH, SWEET_BERRIES, SPIDER_EYE, POISONOUS_POTATO, GLOW_BERRIES, POTATO, PUFFERFISH, TROPICAL_FISH, APPLE, COOKIE, DRIED_KELP, ENCHANTED_GOLDEN_APPLE, GOLDEN_APPLE, GOLDEN_CARROT, HONEY_BOTTLE, MELON_SLICE, MUSHROOM_STEW, PUMPKIN_PIE, RABBIT_STEW, BAKED_POTATO, BEETROOT_SOUP, BEETROOT, CARROT, CHORUS_FRUIT, COOKED_BEEF, COOKED_CHICKEN, COOKED_COD, COOKED_MUTTON, COOKED_PORKCHOP, COOKED_RABBIT, COOKED_SALMON, BEEF, CHICKEN, COD, MUTTON, PORKCHOP, RABBIT, SALMON -> {
                return true;
            }
        }
        return false;
    }

    //according to minecraft wiki
    private static int hungerPointsPerFood(ItemStack itemStack){
        switch (itemStack.getType()){
            case BEETROOT, DRIED_KELP, POTATO, PUFFERFISH, TROPICAL_FISH -> {
                return 1;
            }

            case MELON_SLICE, COOKIE, MUTTON, POISONOUS_POTATO, CHICKEN, COD, SALMON, SPIDER_EYE, SWEET_BERRIES, GLOW_BERRIES -> {
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

    //according to minecraft wiki
    private static float saturationPointsPerFood(ItemStack itemStack){
        switch (itemStack.getType()){
            case PUFFERFISH, TROPICAL_FISH -> {
                return 0.2f;
            }

            case COOKIE, GLOW_BERRIES, COD, SALMON, SWEET_BERRIES -> {
                return 0.4f;
            }

            case DRIED_KELP, POTATO -> {
                return 0.6f;
            }

            case ROTTEN_FLESH -> {
                return 0.8f;
            }

            case BEETROOT, HONEY_BOTTLE, MELON_SLICE, POISONOUS_POTATO, CHICKEN, MUTTON -> {
                return 1.2f;
            }

            case BEEF, PORKCHOP, RABBIT -> {
                return 1.8f;
            }

            case APPLE, CHORUS_FRUIT -> {
                return 2.4f;
            }

            case SPIDER_EYE -> {
                return 3.2f;
            }

            case CARROT -> {
                return 3.6f;
            }

            case PUMPKIN_PIE -> {
                return 4.8f;
            }

            case BAKED_POTATO, BREAD, COOKED_COD, COOKED_RABBIT -> {
                return 6.0f;
            }

            case BEETROOT_SOUP, COOKED_CHICKEN, MUSHROOM_STEW, SUSPICIOUS_STEW -> {
                return 7.2f;
            }

            case COOKED_MUTTON, COOKED_SALMON, ENCHANTED_GOLDEN_APPLE, GOLDEN_APPLE -> {
                return 9.6f;
            }

            case RABBIT_STEW -> {
                return 12.0f;
            }

            case COOKED_PORKCHOP, COOKED_BEEF -> {
                return 12.8f;
            }

            case GOLDEN_CARROT -> {
                return 14.4f;
            }
        }

        return 0.0f;
    }

    private static void applyEffectPerFood(Player player, ItemStack itemStack){
        switch (itemStack.getType()){
            case ROTTEN_FLESH -> {
                if(ThreadLocalRandom.current().nextInt(1, 100) <= 80)   player.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 600, 3));
            }

            case PUFFERFISH -> {
                player.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 300, 3));
                player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 1200, 2));
                player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 300, 1));
            }

            case HONEY_BOTTLE -> player.removePotionEffect(PotionEffectType.POISON);

            case POISONOUS_POTATO -> {
                if(ThreadLocalRandom.current().nextInt(1, 100) <= 60)   player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 100, 1));
            }

            case CHICKEN -> {
                if(ThreadLocalRandom.current().nextInt(1, 100) <= 30)   player.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 600, 1));
            }

            case SPIDER_EYE -> player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 100, 1));

            case ENCHANTED_GOLDEN_APPLE -> {
                player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 2400, 4));
                player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 400, 2));
                player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 6000, 1));
                player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 6000, 1));
            }

            case GOLDEN_APPLE -> {
                player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 2400, 1));
                player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 100, 2));
            }

            case CHORUS_FRUIT -> {
                Location location = player.getLocation().add(ThreadLocalRandom.current().nextInt(-8, 8), 0, ThreadLocalRandom.current().nextInt(-8, 8));
                player.teleportAsync(player.getWorld().getHighestBlockAt(location).getLocation().add(0, 1, 0)); //add one just to make sure the player don't get suffocated by a block
            }
        }
    }
}
