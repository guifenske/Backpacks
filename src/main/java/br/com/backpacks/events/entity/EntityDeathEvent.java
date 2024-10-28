package br.com.backpacks.events.entity;

import br.com.backpacks.Main;
import br.com.backpacks.events.upgrades.Jukebox;
import br.com.backpacks.recipes.BackpackRecipes;
import br.com.backpacks.recipes.RecipesUtils;
import br.com.backpacks.upgrades.JukeboxUpgrade;
import br.com.backpacks.utils.Constants;
import br.com.backpacks.utils.UpgradeType;
import br.com.backpacks.utils.backpacks.BackPack;
import br.com.backpacks.utils.backpacks.RandomBackpackBuilder;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.persistence.PersistentDataType;

import java.util.concurrent.ThreadLocalRandom;

public class EntityDeathEvent implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    private void onPlayerDeath(PlayerDeathEvent event){
        if(!event.getEntity().getPersistentDataContainer().has(BackpackRecipes.HAS_BACKPACK)) return;
        BackPack backpack = Main.backPackManager.getBackpackFromId(event.getEntity().getPersistentDataContainer().get(BackpackRecipes.HAS_BACKPACK, PersistentDataType.INTEGER));

        Player player = event.getEntity();
        Location location = safeLocation(player.getLocation().getBlock().getLocation());

        location.setYaw(0.0f);
        location.setPitch(0.0f);
        location.getBlock().setType(Material.BARREL);

        if(backpack.getFirstUpgradeFromType(UpgradeType.JUKEBOX) != null){
            JukeboxUpgrade upgrade = (JukeboxUpgrade) backpack.getFirstUpgradeFromType(UpgradeType.JUKEBOX);
            if(upgrade.getSound() != null){
                upgrade.clearLoopingTask();
                Jukebox.stopSound(player, upgrade);
            }
        }

        backpack.setOwner(null);

        backpack.setIsBlock(true);
        backpack.setLocation(location);

        backpack.updateBarrelBlock();

        backpack.getConfigMenu().refreshMenu();
        Main.backPackManager.getBackpacksPlacedLocations().put(backpack.getLocation(), backpack.getId());
        player.getPersistentDataContainer().remove(BackpackRecipes.HAS_BACKPACK);

        player.sendMessage(Main.PREFIX + "§cYou died and your backpack was placed on: " + backpack.getLocation().getX() + ", " + backpack.getLocation().getY() + ", " + backpack.getLocation().getZ());
    }

    @EventHandler(ignoreCancelled = true)
    private void onEntityDeath(org.bukkit.event.entity.EntityDeathEvent event){
        if(!(event.getEntity() instanceof Monster)) return;
        if(event.getEntity().getKiller() == null) return;
        if(Constants.MONSTER_DROPS_BACKPACK && ThreadLocalRandom.current().nextInt(830) == 69) {
            RandomBackpackBuilder randomBackpackBuilder = new RandomBackpackBuilder("Unknown Backpack", Main.backPackManager.getLastBackpackID() + 1);
            BackPack backPack = randomBackpackBuilder.generateBackpack();
            Main.backPackManager.setLastBackpackID(Main.backPackManager.getLastBackpackID() + 1);

            event.getEntity().getWorld().dropItemNaturally(event.getEntity().getLocation(), RecipesUtils.getItemFromBackpack(backPack));
        }
    }

    private Location safeLocation(Location location){
        while(!location.getBlock().isEmpty()){
            location.add(0, 1, 0);
        }

        return location;
    }
}
