package br.com.backpacks.events;

import br.com.backpacks.recipes.RecipesNamespaces;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Monster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.persistence.PersistentDataType;

import java.util.concurrent.ThreadLocalRandom;

public class EntitySpawnEvent implements Listener {

    @EventHandler
    private void onSpawn(org.bukkit.event.entity.EntitySpawnEvent event){
        if(!(event.getEntity() instanceof Monster)) return;
        if(event.getEntity().getEntitySpawnReason().equals(CreatureSpawnEvent.SpawnReason.CUSTOM)) return;
        if(event.getEntity().getType().equals(EntityType.SPIDER) || event.getEntity().getType().equals(EntityType.CAVE_SPIDER)) return;

        if(ThreadLocalRandom.current().nextInt(0, 530) == 69){
            event.getEntity().getPersistentDataContainer().set(new RecipesNamespaces().getHAS_BACKPACK(), PersistentDataType.INTEGER ,-1);
        }
    }
}
