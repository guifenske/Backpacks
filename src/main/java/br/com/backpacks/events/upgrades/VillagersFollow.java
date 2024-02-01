package br.com.backpacks.events.upgrades;

import br.com.backpacks.Main;
import br.com.backpacks.backpackUtils.BackPack;
import br.com.backpacks.backpackUtils.BackpackAction;
import br.com.backpacks.backpackUtils.Upgrade;
import br.com.backpacks.backpackUtils.UpgradeType;
import br.com.backpacks.recipes.RecipesNamespaces;
import br.com.backpacks.upgrades.VillagersFollowUpgrade;
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

import java.util.HashSet;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class VillagersFollow implements Listener {
    public static void tick() {
        Main.getMain().getThreadBackpacks().getExecutor().scheduleAtFixedRate(() ->{
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (!player.getPersistentDataContainer().has(new RecipesNamespaces().getHAS_BACKPACK())) continue;
                if (!player.getInventory().getItemInMainHand().getType().equals(Material.EMERALD_BLOCK) && !player.getInventory().getItemInOffHand().getType().equals(Material.EMERALD_BLOCK)) {
                    continue;
                }
                BackPack backpack = Main.backPackManager.getBackpackFromId(player.getPersistentDataContainer().get(new RecipesNamespaces().getHAS_BACKPACK(), PersistentDataType.INTEGER));
                List<Upgrade> list = backpack.getUpgradesFromType(UpgradeType.VILLAGERSFOLLOW);
                if (list.isEmpty()) continue;
                VillagersFollowUpgrade upgrade = (VillagersFollowUpgrade) list.get(0);

                if (!upgrade.isEnabled()) {
                    continue;
                }
                for (Entity entity : getNearbyEntities(player.getLocation().toBlockLocation())) {
                    if (entity.getType().equals(EntityType.VILLAGER)) moveToPlayer((Mob) entity, player);
                }
            }
        }, 0, 500, TimeUnit.MILLISECONDS);
    }

    @EventHandler
    private static void onClick(InventoryClickEvent event) {
        if (BackpackAction.getAction((Player) event.getWhoClicked()) != BackpackAction.Action.UPGVILLAGERSFOLLOW)
            return;
        event.setCancelled(true);
        BackPack backPack = Main.backPackManager.getBackpackFromId(Main.backPackManager.getCurrentBackpackId().get(event.getWhoClicked().getUniqueId()));
        List<Upgrade> list = backPack.getUpgradesFromType(UpgradeType.VILLAGERSFOLLOW);
        VillagersFollowUpgrade upgrade = (VillagersFollowUpgrade) list.get(0);

        if (event.getRawSlot() == 13) {
            upgrade.setEnabled(!upgrade.isEnabled());
            upgrade.updateInventory();
        }
    }

    @EventHandler
    private static void onClose(InventoryCloseEvent event) {
        if (BackpackAction.getAction((Player) event.getPlayer()) != BackpackAction.Action.UPGVILLAGERSFOLLOW) return;
        BackPack backPack = Main.backPackManager.getBackpackFromId(Main.backPackManager.getCurrentBackpackId().get(event.getPlayer().getUniqueId()));
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

    private static Entity[] getNearbyEntities(Location l) {
        int chunkRadius = 1;
        HashSet<Entity> radiusEntities = new HashSet<>();
        int x = (int) l.getX(), z = (int) l.getZ();
        World world = l.getWorld();
        for (int chX = -chunkRadius; chX <= chunkRadius; chX++) {
            for (int chZ = -chunkRadius; chZ <= chunkRadius; chZ++) {
                for (Entity e : new Location(world, x + (chX * 16), 0, z + (chZ * 16)).getChunk().getEntities()) {
                    if (e.getLocation().distanceSquared(l) <= 100){
                        radiusEntities.add(e);
                    }
                }
            }
        }
        return radiusEntities.toArray(new Entity[0]);
    }
}
