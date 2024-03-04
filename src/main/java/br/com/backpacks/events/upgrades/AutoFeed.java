package br.com.backpacks.events.upgrades;

import br.com.backpacks.Main;
import br.com.backpacks.recipes.BackpackRecipes;
import br.com.backpacks.upgrades.AutoFeedUpgrade;
import br.com.backpacks.utils.BackPack;
import br.com.backpacks.utils.BackpackAction;
import br.com.backpacks.utils.UpgradeType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.concurrent.ThreadLocalRandom;

public class AutoFeed implements Listener {
    @EventHandler
    private static void tick(FoodLevelChangeEvent event){
        Player player = (Player) event.getEntity();
        if(!player.getPersistentDataContainer().has(new BackpackRecipes().getHAS_BACKPACK(), PersistentDataType.INTEGER)) return;
        BackPack backPack = Main.backPackManager.getBackpackFromId(player.getPersistentDataContainer().get(new BackpackRecipes().getHAS_BACKPACK(), PersistentDataType.INTEGER));
        if(backPack.getUpgradeFromType(UpgradeType.AUTOFEED) == null) return;
        AutoFeedUpgrade upgrade = (AutoFeedUpgrade) backPack.getUpgradeFromType(UpgradeType.AUTOFEED);
        if(!upgrade.isEnabled() || backPack.getBackpackItems().isEmpty()) return;
        int need = 20 - player.getFoodLevel();

        ItemStack lesserHungerPointsItem = null;
        ItemStack optionalFood = null;
        for(ItemStack itemStack : backPack.getBackpackItems()){
            if(!checkFood(itemStack)) continue;
            int hungerPoints = hungerPointsPerFood(itemStack);
            if(hungerPoints == need){
                if(itemStack.getAmount() == 1) itemStack.setType(Material.AIR);
                else itemStack.subtract();
                event.setCancelled(true);
                player.setFoodLevel(player.getFoodLevel() + hungerPoints);
                player.setSaturation(player.getSaturation() + saturationPointsPerFood(itemStack));
                player.getWorld().playSound(player.getLocation(), Sound.ENTITY_GENERIC_EAT, 1, 1);
                applyEffectPerFood(player, itemStack);
                return;
            }   else if(hungerPoints < need){
                if(lesserHungerPointsItem == null) lesserHungerPointsItem = itemStack;
                else if(hungerPoints > hungerPointsPerFood(lesserHungerPointsItem)){
                    lesserHungerPointsItem = itemStack;
                }
            }   else{
                optionalFood = itemStack;
            }
        }
        if(lesserHungerPointsItem == null){
            if(player.getHealth() < player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() - 1 && optionalFood != null){
                if(optionalFood.getAmount() == 1) optionalFood.setType(Material.AIR);
                else optionalFood.subtract();
                event.setCancelled(true);
                player.setFoodLevel(player.getFoodLevel() + hungerPointsPerFood(optionalFood));
                player.setSaturation(player.getSaturation() + saturationPointsPerFood(optionalFood));
                player.getWorld().playSound(player.getLocation(), Sound.ENTITY_GENERIC_EAT, 1, 1);
                applyEffectPerFood(player, optionalFood);
                return;
            }
            return;
        }
        ItemStack itemStack = lesserHungerPointsItem;
        if(itemStack.getAmount() == 1) itemStack.setType(Material.AIR);
        else itemStack.subtract();
        event.setCancelled(true);
        player.setFoodLevel(player.getFoodLevel() + hungerPointsPerFood(itemStack));
        player.setSaturation(player.getSaturation() + saturationPointsPerFood(itemStack));
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_GENERIC_EAT, 1, 1);
        applyEffectPerFood(player, itemStack);
    }

    @EventHandler
    private static void onClick(InventoryClickEvent event){
        if(!BackpackAction.getActions(event.getWhoClicked()).contains(BackpackAction.Action.UPGAUTOFEED)) return;
        event.setCancelled(true);
        if(event.getRawSlot() == 13){
            BackPack backPack = Main.backPackManager.getPlayerCurrentBackpack(event.getWhoClicked());
            AutoFeedUpgrade upgrade = (AutoFeedUpgrade) backPack.getUpgradeFromType(UpgradeType.AUTOFEED);
            upgrade.setEnabled(!upgrade.isEnabled());
            upgrade.updateInventory();
        }
    }

    @EventHandler
    private static void onClose(InventoryCloseEvent event){
        if(!BackpackAction.getActions(event.getPlayer()).contains(BackpackAction.Action.UPGAUTOFEED)) return;
        BackPack backPack = Main.backPackManager.getPlayerCurrentBackpack(event.getPlayer());
        BackpackAction.clearPlayerActions(event.getPlayer());
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
                player.teleportAsync(player.getWorld().getHighestBlockAt(location).getLocation());
                player.getWorld().playSound(player.getLocation(), Sound.ITEM_CHORUS_FRUIT_TELEPORT, 1,1);
            }
        }
    }
}
