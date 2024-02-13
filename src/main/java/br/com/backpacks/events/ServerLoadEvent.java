package br.com.backpacks.events;

import br.com.backpacks.Main;
import br.com.backpacks.events.upgrades.Furnace;
import br.com.backpacks.events.upgrades.VillagersFollow;
import br.com.backpacks.upgrades.FurnaceUpgrade;
import br.com.backpacks.utils.BackPack;
import br.com.backpacks.utils.Upgrade;
import br.com.backpacks.utils.UpgradeType;
import org.bukkit.Bukkit;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.BlastingRecipe;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.SmokingRecipe;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ServerLoadEvent implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onServerLoad(org.bukkit.event.server.ServerLoadEvent event){

        for(BackPack backPack : Main.backPackManager.getBackpacks().values()){
            if(backPack.isShowingNameAbove()){
                for(Entity entity : backPack.getLocation().getChunk().getEntities()){
                    if(entity instanceof ArmorStand){
                        if(entity.getLocation().toBlockLocation().equals(backPack.getLocation().toBlockLocation().clone().add(0, 1, 0))){
                            backPack.setMarker(entity.getUniqueId());
                            break;
                        }
                    }
                }
            }
        }

        //moved this to here to get the recipes after the server loads, resulting in possible more recipes from others plugins
        Iterator<Recipe> iterator = Bukkit.recipeIterator();
        List<FurnaceRecipe> furnaceRecipes = new ArrayList<>();
        List<SmokingRecipe> smokingRecipes = new ArrayList<>();
        List<BlastingRecipe> blastingRecipes = new ArrayList<>();

        while (iterator.hasNext()){
            Recipe recipe = iterator.next();
            if(!(recipe instanceof FurnaceRecipe)){
                if(recipe instanceof SmokingRecipe){
                    smokingRecipes.add((SmokingRecipe) recipe);
                    continue;
                }   else if (recipe instanceof BlastingRecipe){
                    blastingRecipes.add((BlastingRecipe) recipe);
                    continue;
                }
                continue;
            }
            furnaceRecipes.add((FurnaceRecipe) recipe);
        }

        Main.getMain().setBlastingRecipes(blastingRecipes);
        Main.getMain().setFurnaceRecipes(furnaceRecipes);
        Main.getMain().setSmokingRecipes(smokingRecipes);

        VillagersFollow.tick();
        for(Upgrade upgrade : Main.backPackManager.getUpgradeHashMap().values()){
            if(upgrade.getType().equals(UpgradeType.FURNACE) || upgrade.getType().equals(UpgradeType.BLAST_FURNACE) || upgrade.getType().equals(UpgradeType.SMOKER)){
                if(!((FurnaceUpgrade) upgrade).canTick()) continue;
                Furnace.shouldTick.add(upgrade.getId());
                Furnace.tick((FurnaceUpgrade) upgrade);
            }
        }
    }
}
