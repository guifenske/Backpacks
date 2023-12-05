package br.com.backpacks.recipes;

import br.com.backpacks.backpackUtils.BackPack;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class Utils {

    public static ItemStack getItemFromBackpack(BackPack backPack) {
        switch (backPack.getType()) {
            case LEATHER -> {
                ItemStack leatherBackpack = new ItemStack(Material.CHEST);
                ItemMeta leatherMeta = leatherBackpack.getItemMeta();
                leatherMeta.setDisplayName(backPack.getName());
                leatherMeta.getPersistentDataContainer().set(new RecipesNamespaces().getIS_BACKPACK(), PersistentDataType.INTEGER, 1);
                leatherMeta.getPersistentDataContainer().set(new RecipesNamespaces().getNAMESPACE_BACKPACK_ID(), PersistentDataType.INTEGER, backPack.getId());
                leatherMeta.getPersistentDataContainer().set(new RecipesNamespaces().getNAMESPACE_LEATHER_BACKPACK(), PersistentDataType.INTEGER, 1);
                leatherBackpack.setItemMeta(leatherMeta);
                return leatherBackpack;
            }

            case IRON -> {
                ItemStack ironBackpack = new ItemStack(Material.CHEST);
                ItemMeta ironMeta = ironBackpack.getItemMeta();
                ironMeta.setDisplayName(backPack.getName());
                ironMeta.getPersistentDataContainer().set(new RecipesNamespaces().getIS_BACKPACK(), PersistentDataType.INTEGER, 1);
                ironMeta.getPersistentDataContainer().set(new RecipesNamespaces().getNAMESPACE_BACKPACK_ID(), PersistentDataType.INTEGER, backPack.getId());
                ironMeta.getPersistentDataContainer().set(new RecipesNamespaces().getNAMESPACE_IRON_BACKPACK(), PersistentDataType.INTEGER, 1);
                ironBackpack.setItemMeta(ironMeta);
                return ironBackpack;
            }

            case GOLD -> {
                ItemStack goldBackpack = new ItemStack(Material.CHEST);
                ItemMeta goldMeta = goldBackpack.getItemMeta();
                goldMeta.setDisplayName(backPack.getName());
                goldMeta.getPersistentDataContainer().set(new RecipesNamespaces().getIS_BACKPACK(), PersistentDataType.INTEGER, 1);
                goldMeta.getPersistentDataContainer().set(new RecipesNamespaces().getNAMESPACE_BACKPACK_ID(), PersistentDataType.INTEGER, backPack.getId());
                goldMeta.getPersistentDataContainer().set(new RecipesNamespaces().getNAMESPACE_GOLD_BACKPACK(), PersistentDataType.INTEGER, 1);
                goldBackpack.setItemMeta(goldMeta);
                return goldBackpack;
            }

            case LAPIS -> {
                ItemStack lapisBackpack = new ItemStack(Material.CHEST);
                ItemMeta lapisMeta = lapisBackpack.getItemMeta();
                lapisMeta.setDisplayName(backPack.getName());
                lapisMeta.getPersistentDataContainer().set(new RecipesNamespaces().getIS_BACKPACK(), PersistentDataType.INTEGER, 1);
                lapisMeta.getPersistentDataContainer().set(new RecipesNamespaces().getNAMESPACE_BACKPACK_ID(), PersistentDataType.INTEGER, backPack.getId());
                lapisMeta.getPersistentDataContainer().set(new RecipesNamespaces().getNAMESPACE_LAPIS_BACKPACK(), PersistentDataType.INTEGER, 1);
                lapisBackpack.setItemMeta(lapisMeta);
                return lapisBackpack;
            }

            case AMETHYST -> {
                ItemStack amethystBackpack = new ItemStack(Material.CHEST);
                ItemMeta amethystMeta = amethystBackpack.getItemMeta();
                amethystMeta.setDisplayName(backPack.getName());
                amethystMeta.getPersistentDataContainer().set(new RecipesNamespaces().getIS_BACKPACK(), PersistentDataType.INTEGER, 1);
                amethystMeta.getPersistentDataContainer().set(new RecipesNamespaces().getNAMESPACE_BACKPACK_ID(), PersistentDataType.INTEGER, backPack.getId());
                amethystMeta.getPersistentDataContainer().set(new RecipesNamespaces().getNAMESPACE_AMETHYST_BACKPACK(), PersistentDataType.INTEGER, 1);
                amethystBackpack.setItemMeta(amethystMeta);
                return amethystBackpack;
            }

            case DIAMOND -> {
                ItemStack diamondBackpack = new ItemStack(Material.CHEST);
                ItemMeta diamondMeta = diamondBackpack.getItemMeta();
                diamondMeta.setDisplayName(backPack.getName());
                diamondMeta.getPersistentDataContainer().set(new RecipesNamespaces().getIS_BACKPACK(), PersistentDataType.INTEGER, 1);
                diamondMeta.getPersistentDataContainer().set(new RecipesNamespaces().getNAMESPACE_BACKPACK_ID(), PersistentDataType.INTEGER, backPack.getId());
                diamondMeta.getPersistentDataContainer().set(new RecipesNamespaces().getNAMESPACE_DIAMOND_BACKPACK(), PersistentDataType.INTEGER, 1);
                diamondBackpack.setItemMeta(diamondMeta);
                return diamondBackpack;
            }

            case NETHERITE -> {
                ItemStack netheriteBackpack = new ItemStack(Material.CHEST);
                ItemMeta netheriteMeta = netheriteBackpack.getItemMeta();
                netheriteMeta.setDisplayName(backPack.getName());
                netheriteMeta.getPersistentDataContainer().set(new RecipesNamespaces().getIS_BACKPACK(), PersistentDataType.INTEGER, 1);
                netheriteMeta.getPersistentDataContainer().set(new RecipesNamespaces().getNAMESPACE_BACKPACK_ID(), PersistentDataType.INTEGER, backPack.getId());
                netheriteMeta.getPersistentDataContainer().set(new RecipesNamespaces().getNAMESPACE_NETHERITE_BACKPACK(), PersistentDataType.INTEGER, 1);
                netheriteBackpack.setItemMeta(netheriteMeta);
                return netheriteBackpack;
            }

        }

        return null;
    }

}
