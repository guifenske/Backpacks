package br.com.backpacks.events.upgrades;

import br.com.backpacks.Main;
import br.com.backpacks.recipes.BackpackRecipes;
import br.com.backpacks.upgrades.VillagerBaitUpgrade;
import br.com.backpacks.upgrades.UpgradeType;
import br.com.backpacks.backpack.Backpack;
import br.com.backpacks.backpack.BackpackAction;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class VillagerBait implements Listener {
    public static void tick(Player player) {
        if(player == null) return;
        if (!player.getPersistentDataContainer().has(BackpackRecipes.HAS_BACKPACK)) return;

        if (!player.getInventory().getItemInMainHand().getType().equals(Material.EMERALD_BLOCK) && !player.getInventory().getItemInOffHand().getType().equals(Material.EMERALD_BLOCK)) {
            return;
        }

        Backpack backpack = Main.backpackManager.getBackpackFromId(player.getPersistentDataContainer().get(BackpackRecipes.HAS_BACKPACK, PersistentDataType.INTEGER));
        VillagerBaitUpgrade upgrade = (VillagerBaitUpgrade) backpack.getFirstUpgradeFromType(UpgradeType.VILLAGER_BAIT);
        if(upgrade == null) return;

        if (!upgrade.isEnabled()) {
            return;
        }

        //move nearby villagers in a 10 blocks radius
        moveNearbyVillagers(player, 100);
    }

    @EventHandler
    private static void onClick(InventoryClickEvent event) {
        if (!BackpackAction.getAction(event.getWhoClicked()).equals(BackpackAction.Action.UPGVILLAGERBAIT)) {
            return;
        }

        Player player = (Player) event.getWhoClicked();

        event.setCancelled(true);
        Backpack backpack = Main.backpackManager.getPlayerCurrentBackpack(player);
        VillagerBaitUpgrade upgrade = (VillagerBaitUpgrade) backpack.getFirstUpgradeFromType(UpgradeType.VILLAGER_BAIT);

        if (event.getRawSlot() == 13) {
            upgrade.setEnabled(!upgrade.isEnabled());
            upgrade.updateInventory();
        }
    }

    @EventHandler
    private static void onClose(InventoryCloseEvent event) {
        if (!BackpackAction.getAction(event.getPlayer()).equals(BackpackAction.Action.UPGVILLAGERBAIT)) {
            return;
        }

        Player player = (Player) event.getPlayer();

        Backpack backpack = Main.backpackManager.getPlayerCurrentBackpack(player);

        BackpackAction.clearPlayerAction(event.getPlayer());
        BukkitTask task = new BukkitRunnable() {
            @Override
            public void run() {
                backpack.open((Player) event.getPlayer());
            }
        }.runTaskLater(Main.getMain(), 1L);
    }

    private static void moveToPlayer(Mob mob, Player player) {
        Bukkit.getScheduler().runTask(Main.getMain(), () ->{
            mob.setTarget(player);
            mob.getPathfinder().moveTo(player, 0.5);
        });
    }

    private static void moveNearbyVillagers(Player player, int distanceSquared) {
        int chunkRadius = 1;
        int x = (int) player.getLocation().getX(), z = (int) player.getLocation().getZ();
        World world = player.getLocation().getWorld();

        //iterates over chunks around location
        for (int chX = -chunkRadius; chX <= chunkRadius; chX++) {
            for (int chZ = -chunkRadius; chZ <= chunkRadius; chZ++) {
                for (Entity e : new Location(world, x + (chX * 16), 0, z + (chZ * 16)).getChunk().getEntities()) {
                    if (e.getLocation().distanceSquared(player.getLocation()) <= distanceSquared && e.getType().equals(EntityType.VILLAGER)){
                        moveToPlayer((Mob) e, player);
                    }
                }
            }
        }
    }
}
