package br.com.backpacks.events.upgrades;

import br.com.backpacks.recipes.BackpackRecipes;
import br.com.backpacks.utils.UpgradeType;
import br.com.backpacks.utils.backpacks.BackPack;
import br.com.backpacks.utils.backpacks.BackpackManager;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public class MagnetUpgradeEvents implements Listener {

    public static void tick(Player player){
        if(player == null) return;
        if(!player.getPersistentDataContainer().has(BackpackRecipes.getHAS_BACKPACK(), PersistentDataType.INTEGER)) return;
        BackPack backPack = BackpackManager.getBackpackFromId(player.getPersistentDataContainer().get(BackpackRecipes.getHAS_BACKPACK(), PersistentDataType.INTEGER));
        if(backPack.getUpgradeFromType(UpgradeType.MAGNET) == null) return;

        pullItemsNearby(player, player.getLocation());
    }

    public static void tick(@NotNull BackPack backPack){
        if(backPack.getUpgradeFromType(UpgradeType.MAGNET) == null) return;
        pullItemsNearby(null, backPack.getLocation().clone());
    }

    private static void pullItemsNearby(Player player, @NotNull Location location) {
        int chunkRadius = 1;
        int maxDistanceSquared = 25;
        int x = (int) location.getX(), z = (int) location.getZ();
        World world = location.getWorld();

        //iterates over chunks around location
        for (int chX = -chunkRadius; chX <= chunkRadius; chX++) {
            for (int chZ = -chunkRadius; chZ <= chunkRadius; chZ++) {
                if(!new Location(world, x + (chX * 16), 0, z + (chZ * 16)).isChunkLoaded()) continue;
                for (Entity e : new Location(world, x + (chX * 16), 0, z + (chZ * 16)).getChunk().getEntities()) {
                    double distanceSquared = location.distanceSquared(e.getLocation());
                    if (e.getType().equals(EntityType.DROPPED_ITEM) && distanceSquared <= maxDistanceSquared){

                        if(player != null){
                            if(e instanceof Item){
                                if(((Item) e).getThrower() != null && ((Item) e).getThrower().equals(player.getUniqueId())) continue;
                            }

                            if(distanceSquared <= 4) continue;
                            pullItem(e, location, true);
                        }   else{
                            pullItem(e, location, false);
                        }

                    }
                }
            }
        }
    }

    private static void pullItem(Entity entity, Location pullTo, boolean isPlayer){
        if(isPlayer){
            ((Item) entity).setPickupDelay(0);
        }
        Vector direction = pullTo.subtract(entity.getLocation()).toVector();
        entity.setVelocity(direction.normalize());
    }

}
