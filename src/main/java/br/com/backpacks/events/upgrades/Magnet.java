package br.com.backpacks.events.upgrades;

import br.com.backpacks.Main;
import br.com.backpacks.recipes.BackpackRecipes;
import br.com.backpacks.utils.UpgradeType;
import br.com.backpacks.utils.backpacks.BackPack;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.event.Listener;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;

public class Magnet implements Listener {

    public static void tick(Player player){
        if(player == null) return;
        if(!player.getPersistentDataContainer().has(new BackpackRecipes().getHAS_BACKPACK(), PersistentDataType.INTEGER)) return;
        BackPack backPack = Main.backPackManager.getBackpackFromId(player.getPersistentDataContainer().get(new BackpackRecipes().getHAS_BACKPACK(), PersistentDataType.INTEGER));
        if(backPack.getFirstUpgradeFromType(UpgradeType.MAGNET) == null) return;

        pullItemsNearby(player, player.getLocation());
    }

    public static void tick(BackPack backPack){
        if(backPack.getFirstUpgradeFromType(UpgradeType.MAGNET) == null) return;
        pullItemsNearby(null, backPack.getLocation().clone());
    }

    private static void pullItemsNearby(Player player, Location location) {
        int chunkRadius = 1;
        int maxDistanceSquared = 25;
        int x = (int) location.getX(), z = (int) location.getZ();
        World world = location.getWorld();

        //iterates over chunks around location
        for (int chX = -chunkRadius; chX <= chunkRadius; chX++) {
            for (int chZ = -chunkRadius; chZ <= chunkRadius; chZ++) {
                for (Entity e : new Location(world, x + (chX * 16), 0, z + (chZ * 16)).getChunk().getEntities()) {
                    double distanceSquared = location.distanceSquared(e.getLocation());
                    if ((e.getType().equals(EntityType.DROPPED_ITEM) || e.getType().equals(EntityType.EXPERIENCE_ORB)) && distanceSquared <= maxDistanceSquared){

                        if(player != null){
                            if(e instanceof Item){
                                //only pull items that are not of the player using the upgrade
                                if(((Item) e).getThrower() != null && ((Item) e).getThrower().equals(player.getUniqueId())) continue;
                            }

                            if(distanceSquared <= 4) continue;
                            pullItem(e, location, Math.sqrt(distanceSquared), true);
                        }   else{
                            if(e instanceof ExperienceOrb) continue;
                            double distance = Math.sqrt(distanceSquared);
                            pullItem(e, location, distance, false);
                        }

                    }
                }
            }
        }
    }

    private static void pullItem(Entity entity, Location pullTo, double distance, boolean isPlayer){
        if(isPlayer && entity instanceof Item){
            ((Item) entity).setPickupDelay(0);
        }
        Vector direction = pullTo.subtract(entity.getLocation()).toVector();

        //distribute the velocity of the item evenly in 5 ticks
        entity.setVelocity(direction.normalize().multiply(distance / 5));
    }

}
