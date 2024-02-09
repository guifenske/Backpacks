package br.com.backpacks.events.player;

import br.com.backpacks.Main;
import br.com.backpacks.backpackUtils.BackPack;
import br.com.backpacks.backpackUtils.UpgradeType;
import br.com.backpacks.backpackUtils.inventory.InventoryBuilder;
import br.com.backpacks.events.upgrades.Jukebox;
import br.com.backpacks.recipes.RecipesNamespaces;
import br.com.backpacks.upgrades.JukeboxUpgrade;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Barrel;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public class PlayerDeathEvent implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    private void onPlayerDeath(org.bukkit.event.entity.PlayerDeathEvent event){
        if(!event.getPlayer().getPersistentDataContainer().has(new RecipesNamespaces().getHAS_BACKPACK())) return;
        BackPack backpack = Main.backPackManager.getBackpackFromId(event.getPlayer().getPersistentDataContainer().get(new RecipesNamespaces().getHAS_BACKPACK(), PersistentDataType.INTEGER));
        Player player = event.getPlayer();
        Location location = safeLocation(player.getLocation().toBlockLocation());

        location.setYaw(0.0f);
        location.setPitch(0.0f);
        location.getBlock().setType(Material.BARREL);
        location.getBlock().tick();
        //we need to do this to trigger the hopper event
        Barrel barrel = (Barrel) location.getBlock().getState();
        barrel.getInventory().addItem(new ItemStack(Material.STICK));
        if(!backpack.getUpgradesFromType(UpgradeType.JUKEBOX).isEmpty()){
            JukeboxUpgrade upgrade = (JukeboxUpgrade) backpack.getUpgradesFromType(UpgradeType.JUKEBOX).get(0);
            if(upgrade.getSound() != null){
                Jukebox.stopSound(player, upgrade);
            }
        }
        backpack.setOwner(null);

        backpack.setIsBlock(true);
        backpack.setLocation(location);
        InventoryBuilder.updateConfigInv(backpack);
        Main.backPackManager.getBackpacksPlacedLocations().put(backpack.getLocation(), backpack.getId());
        player.getPersistentDataContainer().remove(new RecipesNamespaces().getHAS_BACKPACK());
        player.sendMessage(Main.PREFIX + "§cYou died and your backpack was placed on: " + backpack.getLocation().getX() + ", " + backpack.getLocation().getY() + ", " + backpack.getLocation().getZ() + "!");
    }

    private Location safeLocation(Location location){
        while(location.getBlock().isSolid()){
            location.add(0, 1, 0);
        }
        return location;
    }
}
