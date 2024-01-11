package br.com.backpacks.events.player;

import br.com.backpacks.backpackUtils.BackPack;
import br.com.backpacks.backpackUtils.RandomBackpack;
import br.com.backpacks.recipes.RecipesNamespaces;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockCookEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class FinishedSmelting implements Listener {

    @EventHandler
    private void onSmelt(BlockCookEvent event){
        if(!event.getSource().getItemMeta().getPersistentDataContainer().has(new RecipesNamespaces().getIS_BACKPACK())) return;

        ItemStack driedBackpack = new ItemStack(Material.CHEST);
        ItemMeta meta = driedBackpack.getItemMeta();
        meta.setDisplayName("Unknown Backpack");
        meta.getPersistentDataContainer().set(new RecipesNamespaces().getIS_BACKPACK(), PersistentDataType.INTEGER, 1);
        meta.getPersistentDataContainer().set(new RecipesNamespaces().getNAMESPACE_BACKPACK_ID(), PersistentDataType.INTEGER, new CraftBackpack().generateId());
        RandomBackpack randomBackpack = new RandomBackpack("Unknown Backpack", meta.getPersistentDataContainer().get(new RecipesNamespaces().getNAMESPACE_BACKPACK_ID(), PersistentDataType.INTEGER));
        BackPack backPack = randomBackpack.generateBackpack();
        meta.getPersistentDataContainer().set(backPack.getNamespaceOfBackpackType(), PersistentDataType.INTEGER, 1);
        driedBackpack.setItemMeta(meta);
        event.setResult(driedBackpack);
    }
}
