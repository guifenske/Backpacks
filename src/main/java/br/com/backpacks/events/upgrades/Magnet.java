package br.com.backpacks.events.upgrades;

import br.com.backpacks.Main;
import br.com.backpacks.recipes.BackpackRecipes;
import br.com.backpacks.utils.BackPack;
import br.com.backpacks.utils.UpgradeType;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.persistence.PersistentDataType;

public class Magnet implements Listener {

    public static void tick(Player player){
        if(!player.getPersistentDataContainer().has(new BackpackRecipes().getHAS_BACKPACK(), PersistentDataType.INTEGER)) return;
        BackPack backPack = Main.backPackManager.getBackpackFromId(player.getPersistentDataContainer().get(new BackpackRecipes().getHAS_BACKPACK(), PersistentDataType.INTEGER));
        if(backPack.getUpgradeFromType(UpgradeType.MAGNET) == null) return;

        pullItemsNearby(player.getLocation());
    }

    private static void pullItemsNearby(Location location){
        int chunkRadius = 1;
        int distanceSquared = 25;
        int x = (int) location.getX(), z = (int) location.getZ();
        World world = location.getWorld();

        //iterates over chunks around location
        for (int chX = -chunkRadius; chX <= chunkRadius; chX++) {
            for (int chZ = -chunkRadius; chZ <= chunkRadius; chZ++) {
                if(!new Location(world, x + (chX * 16), 0, z + (chZ * 16)).isChunkLoaded()) continue;
                for (Entity e : new Location(world, x + (chX * 16), 0, z + (chZ * 16)).getChunk().getEntities()) {
                    if (e.getType().equals(EntityType.DROPPED_ITEM) && e.getLocation().distanceSquared(location) <= distanceSquared){
                        //just to not making the item doing a bit of flickering near the player
                        if(e.getLocation().distanceSquared(location) <= 4) continue;
                        pullItem((Item) e, location);
                    }
                }
            }
        }
    }

    private static void pullItem(Item item, Location pullTo){
        item.setPickupDelay(0);
        item.teleportAsync(pullTo);
    }

}
