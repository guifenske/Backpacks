package br.com.backpacks.events.upgrades;

import br.com.backpacks.Main;
import br.com.backpacks.recipes.BackpackRecipes;
import br.com.backpacks.upgrades.VillagersFollowUpgrade;
import br.com.backpacks.utils.BackPack;
import br.com.backpacks.utils.BackpackAction;
import br.com.backpacks.utils.UpgradeType;
import com.destroystokyo.paper.entity.Pathfinder;
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

public class VillagersFollow implements Listener {
    public static void tick(Player player) {
        if (!player.getPersistentDataContainer().has(new BackpackRecipes().getHAS_BACKPACK(), PersistentDataType.INTEGER)) return;
        if (!player.getInventory().getItemInMainHand().getType().equals(Material.EMERALD_BLOCK) && !player.getInventory().getItemInOffHand().getType().equals(Material.EMERALD_BLOCK)) {
            return;
        }
        BackPack backpack = Main.backPackManager.getBackpackFromId(player.getPersistentDataContainer().get(new BackpackRecipes().getHAS_BACKPACK(), PersistentDataType.INTEGER));
        VillagersFollowUpgrade upgrade = (VillagersFollowUpgrade) backpack.getUpgradeFromType(UpgradeType.VILLAGERSFOLLOW);
        if(upgrade == null) return;

        if (!upgrade.isEnabled()) {
            return;
        }

        //move nearby villagers in a 10 blocks radius
        moveNearbyVillagers(player.getLocation().toBlockLocation(), player, 100);
    }

    @EventHandler
    private static void onClick(InventoryClickEvent event) {
        if (!BackpackAction.getAction(event.getWhoClicked()).equals(BackpackAction.Action.UPGVILLAGERSFOLLOW)) {
            return;
        }
        event.setCancelled(true);
        BackPack backPack = Main.backPackManager.getBackpackFromId(Main.backPackManager.getCurrentBackpackId().get(event.getWhoClicked().getUniqueId()));
        VillagersFollowUpgrade upgrade = (VillagersFollowUpgrade) backPack.getUpgradeFromType(UpgradeType.VILLAGERSFOLLOW);

        if (event.getRawSlot() == 13) {
            upgrade.setEnabled(!upgrade.isEnabled());
            upgrade.updateInventory();
        }
    }

    @EventHandler
    private static void onClose(InventoryCloseEvent event) {
        if (!BackpackAction.getAction(event.getPlayer()).equals(BackpackAction.Action.UPGVILLAGERSFOLLOW)) {
            return;
        }
        BackPack backPack = Main.backPackManager.getBackpackFromId(Main.backPackManager.getCurrentBackpackId().get(event.getPlayer().getUniqueId()));
        BackpackAction.clearPlayerAction(event.getPlayer());
        BukkitTask task = new BukkitRunnable() {
            @Override
            public void run() {
                backPack.open((Player) event.getPlayer());
            }
        }.runTaskLater(Main.getMain(), 1L);
    }

    private static void moveToPlayer(Mob entity, Player player) {
        Bukkit.getScheduler().runTask(Main.getMain(), () ->{
            Pathfinder pathfinder = entity.getPathfinder();
            pathfinder.moveTo(player, 0.55D);
        });
    }

    private static void moveNearbyVillagers(Location l, Player player, int distanceSquared) {
        int chunkRadius = 1;
        int x = (int) l.getX(), z = (int) l.getZ();
        World world = l.getWorld();

        //iterates over chunks around location
        for (int chX = -chunkRadius; chX <= chunkRadius; chX++) {
            for (int chZ = -chunkRadius; chZ <= chunkRadius; chZ++) {
                if(!new Location(world, x + (chX * 16), 0, z + (chZ * 16)).isChunkLoaded()) continue;
                for (Entity e : new Location(world, x + (chX * 16), 0, z + (chZ * 16)).getChunk().getEntities()) {
                    if (e.getLocation().distanceSquared(l) <= distanceSquared && e.getType().equals(EntityType.VILLAGER)){
                        moveToPlayer((Mob) e, player);
                    }
                }
            }
        }
    }
}
