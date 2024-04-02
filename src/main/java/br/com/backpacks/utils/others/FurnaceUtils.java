package br.com.backpacks.utils.others;

import java.util.EnumMap;

public class FurnaceUtils {
    
    public static EnumMap<Material, Fuel> fuelMap = new EnumMap<>(Material.class);

    public enum Fuel {
        COAL(8),
        COAL_BLOCK(80),
        BLAZE_ROD(12),
        PLANKS(1),
        STICK(1),
        BOAT(6),
        CHARCOAL(8),
        LAVA_BUCKET(100),
        DRIED_KELP_BLOCK(20),
        WOODEN_EQUIPMENT(1),
        BOWL(1),
        LOGS(2),
        WOOD(2),
        STRIPPED_WOOD(2),
        STRIPPED_LOGS(2),
        SIGN(1),
        HANGING_SIGN(1),
        SAPLING(1),
        CARPET(1),
        NOTHING(0);

        Fuel(int maxOperation) {
            this.maxOperation = maxOperation;
        }

        public int getMaxOperation() {
            return maxOperation;
        }

        private final int maxOperation;
    }
    
    static {
        fuelMap.put(Material.BLACK_CARPET, Fuel.CARPET);
        fuelMap.put(Material.BLUE_CARPET, Fuel.CARPET);
        fuelMap.put(Material.BROWN_CARPET, Fuel.CARPET);
        fuelMap.put(Material.CYAN_CARPET, Fuel.CARPET);
        fuelMap.put(Material.GRAY_CARPET, Fuel.CARPET);
        fuelMap.put(Material.GREEN_CARPET, Fuel.CARPET);
        fuelMap.put(Material.LIGHT_BLUE_CARPET, Fuel.CARPET);
        fuelMap.put(Material.LIGHT_GRAY_CARPET, Fuel.CARPET);
        fuelMap.put(Material.LIME_CARPET, Fuel.CARPET);
        fuelMap.put(Material.MAGENTA_CARPET, Fuel.CARPET);
        fuelMap.put(Material.ORANGE_CARPET, Fuel.CARPET);
        fuelMap.put(Material.PINK_CARPET, Fuel.CARPET);
        fuelMap.put(Material.PURPLE_CARPET, Fuel.CARPET);
        fuelMap.put(Material.RED_CARPET, Fuel.CARPET);
        fuelMap.put(Material.WHITE_CARPET, Fuel.CARPET);
        fuelMap.put(Material.YELLOW_CARPET, Fuel.CARPET);
        fuelMap.put(Material.DRIED_KELP_BLOCK, Fuel.DRIED_KELP_BLOCK);
        fuelMap.put(Material.LAVA_BUCKET, Fuel.LAVA_BUCKET);
        fuelMap.put(Material.COAL, Fuel.COAL);
        fuelMap.put(Material.CHARCOAL, Fuel.CHARCOAL);
        fuelMap.put(Material.COAL_BLOCK, Fuel.COAL_BLOCK);
        fuelMap.put(Material.BLAZE_ROD, Fuel.BLAZE_ROD);
        fuelMap.put(Material.ACACIA_PLANKS, Fuel.PLANKS);
        fuelMap.put(Material.BAMBOO_PLANKS, Fuel.PLANKS);
        fuelMap.put(Material.BIRCH_PLANKS, Fuel.PLANKS);
        fuelMap.put(Material.CHERRY_PLANKS, Fuel.PLANKS);
        fuelMap.put(Material.CRIMSON_PLANKS, Fuel.PLANKS);
        fuelMap.put(Material.OAK_PLANKS, Fuel.PLANKS);
        fuelMap.put(Material.DARK_OAK_PLANKS, Fuel.PLANKS);
        fuelMap.put(Material.JUNGLE_PLANKS, Fuel.PLANKS);
        fuelMap.put(Material.MANGROVE_PLANKS, Fuel.PLANKS);
        fuelMap.put(Material.SPRUCE_PLANKS, Fuel.PLANKS);
        fuelMap.put(Material.WARPED_PLANKS, Fuel.PLANKS);
        fuelMap.put(Material.STICK, Fuel.STICK);
        fuelMap.put(Material.ACACIA_BOAT, Fuel.BOAT);
        fuelMap.put(Material.ACACIA_CHEST_BOAT, Fuel.BOAT);
        fuelMap.put(Material.BIRCH_BOAT, Fuel.BOAT);
        fuelMap.put(Material.BIRCH_CHEST_BOAT, Fuel.BOAT);
        fuelMap.put(Material.CHERRY_BOAT, Fuel.BOAT);
        fuelMap.put(Material.CHERRY_CHEST_BOAT, Fuel.BOAT);
        fuelMap.put(Material.DARK_OAK_BOAT, Fuel.BOAT);
        fuelMap.put(Material.DARK_OAK_CHEST_BOAT, Fuel.BOAT);
        fuelMap.put(Material.JUNGLE_BOAT, Fuel.BOAT);
        fuelMap.put(Material.JUNGLE_CHEST_BOAT, Fuel.BOAT);
        fuelMap.put(Material.MANGROVE_BOAT, Fuel.BOAT);
        fuelMap.put(Material.MANGROVE_CHEST_BOAT, Fuel.BOAT);
        fuelMap.put(Material.OAK_BOAT, Fuel.BOAT);
        fuelMap.put(Material.OAK_CHEST_BOAT, Fuel.BOAT);
        fuelMap.put(Material.SPRUCE_BOAT, Fuel.BOAT);
        fuelMap.put(Material.SPRUCE_CHEST_BOAT, Fuel.BOAT);
        fuelMap.put(Material.WOODEN_AXE, Fuel.WOODEN_EQUIPMENT);
        fuelMap.put(Material.WOODEN_SHOVEL, Fuel.WOODEN_EQUIPMENT);
        fuelMap.put(Material.WOODEN_SWORD, Fuel.WOODEN_EQUIPMENT);
        fuelMap.put(Material.WOODEN_HOE, Fuel.WOODEN_EQUIPMENT);
        fuelMap.put(Material.WOODEN_PICKAXE, Fuel.WOODEN_EQUIPMENT);
        fuelMap.put(Material.BOWL, Fuel.BOWL);
        fuelMap.put(Material.ACACIA_LOG, Fuel.LOGS);
        fuelMap.put(Material.BIRCH_LOG, Fuel.LOGS);
        fuelMap.put(Material.CHERRY_LOG, Fuel.LOGS);
        fuelMap.put(Material.DARK_OAK_LOG, Fuel.LOGS);
        fuelMap.put(Material.JUNGLE_LOG, Fuel.LOGS);
        fuelMap.put(Material.MANGROVE_LOG, Fuel.LOGS);
        fuelMap.put(Material.OAK_LOG, Fuel.LOGS);
        fuelMap.put(Material.SPRUCE_LOG, Fuel.LOGS);
        fuelMap.put(Material.STRIPPED_ACACIA_LOG, Fuel.STRIPPED_LOGS);
        fuelMap.put(Material.STRIPPED_BIRCH_LOG, Fuel.STRIPPED_LOGS);
        fuelMap.put(Material.STRIPPED_CHERRY_LOG, Fuel.STRIPPED_LOGS);
        fuelMap.put(Material.STRIPPED_DARK_OAK_LOG, Fuel.STRIPPED_LOGS);
        fuelMap.put(Material.STRIPPED_JUNGLE_LOG, Fuel.STRIPPED_LOGS);
        fuelMap.put(Material.STRIPPED_MANGROVE_LOG, Fuel.STRIPPED_LOGS);
        fuelMap.put(Material.STRIPPED_OAK_LOG, Fuel.STRIPPED_LOGS);
        fuelMap.put(Material.STRIPPED_SPRUCE_LOG, Fuel.STRIPPED_LOGS);
        fuelMap.put(Material.STRIPPED_ACACIA_WOOD, Fuel.STRIPPED_WOOD);
        fuelMap.put(Material.STRIPPED_BIRCH_WOOD, Fuel.STRIPPED_WOOD);
        fuelMap.put(Material.STRIPPED_CHERRY_WOOD, Fuel.STRIPPED_WOOD);
        fuelMap.put(Material.STRIPPED_DARK_OAK_WOOD, Fuel.STRIPPED_WOOD);
        fuelMap.put(Material.STRIPPED_JUNGLE_WOOD, Fuel.STRIPPED_WOOD);
        fuelMap.put(Material.STRIPPED_MANGROVE_WOOD, Fuel.STRIPPED_WOOD);
        fuelMap.put(Material.STRIPPED_OAK_WOOD, Fuel.STRIPPED_WOOD);
        fuelMap.put(Material.STRIPPED_SPRUCE_WOOD, Fuel.STRIPPED_WOOD);
        fuelMap.put(Material.ACACIA_WOOD, Fuel.WOOD);
        fuelMap.put(Material.BIRCH_WOOD, Fuel.WOOD);
        fuelMap.put(Material.CHERRY_WOOD, Fuel.WOOD);
        fuelMap.put(Material.DARK_OAK_WOOD, Fuel.WOOD);
        fuelMap.put(Material.JUNGLE_WOOD, Fuel.WOOD);
        fuelMap.put(Material.MANGROVE_WOOD, Fuel.WOOD);
        fuelMap.put(Material.OAK_WOOD, Fuel.WOOD);
        fuelMap.put(Material.SPRUCE_WOOD, Fuel.WOOD);
        fuelMap.put(Material.ACACIA_SIGN, Fuel.SIGN);
        fuelMap.put(Material.BIRCH_SIGN, Fuel.SIGN);
        fuelMap.put(Material.CHERRY_SIGN, Fuel.SIGN);
        fuelMap.put(Material.DARK_OAK_SIGN, Fuel.SIGN);
        fuelMap.put(Material.JUNGLE_SIGN, Fuel.SIGN);
        fuelMap.put(Material.MANGROVE_SIGN, Fuel.SIGN);
        fuelMap.put(Material.OAK_SIGN, Fuel.SIGN);
        fuelMap.put(Material.SPRUCE_SIGN, Fuel.SIGN);
        fuelMap.put(Material.ACACIA_WALL_SIGN, Fuel.HANGING_SIGN);
        fuelMap.put(Material.BIRCH_WALL_SIGN, Fuel.HANGING_SIGN);
        fuelMap.put(Material.CHERRY_WALL_SIGN, Fuel.HANGING_SIGN);
        fuelMap.put(Material.DARK_OAK_WALL_SIGN, Fuel.HANGING_SIGN);
        fuelMap.put(Material.JUNGLE_WALL_SIGN, Fuel.HANGING_SIGN);
        fuelMap.put(Material.MANGROVE_WALL_SIGN, Fuel.HANGING_SIGN);
        fuelMap.put(Material.OAK_WALL_SIGN, Fuel.HANGING_SIGN);
        fuelMap.put(Material.SPRUCE_WALL_SIGN, Fuel.HANGING_SIGN);
        fuelMap.put(Material.ACACIA_SAPLING, Fuel.SAPLING);
        fuelMap.put(Material.BIRCH_SAPLING, Fuel.SAPLING);
        fuelMap.put(Material.CHERRY_SAPLING, Fuel.SAPLING);
        fuelMap.put(Material.DARK_OAK_SAPLING, Fuel.SAPLING);
        fuelMap.put(Material.JUNGLE_SAPLING, Fuel.SAPLING);
        fuelMap.put(Material.OAK_SAPLING, Fuel.SAPLING);
        fuelMap.put(Material.SPRUCE_SAPLING, Fuel.SAPLING);
    }

    //every material that the furnace upgrade interact with
    public enum Material{
        BLACK_CARPET,
        BLUE_CARPET,
        BROWN_CARPET,
        CYAN_CARPET,
        GRAY_CARPET,
        GREEN_CARPET,
        LIGHT_BLUE_CARPET,
        LIGHT_GRAY_CARPET,
        LIME_CARPET,
        MAGENTA_CARPET,
        ORANGE_CARPET,
        PINK_CARPET,
        PURPLE_CARPET,
        RED_CARPET,
        WHITE_CARPET,
        YELLOW_CARPET,
        DRIED_KELP_BLOCK,
        COAL,
        CHARCOAL,
        COAL_BLOCK,
        BLAZE_ROD,
        ACACIA_PLANKS,
        BAMBOO_PLANKS,
        BIRCH_PLANKS,
        CHERRY_PLANKS,
        CRIMSON_PLANKS,
        OAK_PLANKS,
        DARK_OAK_PLANKS,
        JUNGLE_PLANKS,
        MANGROVE_PLANKS,
        SPRUCE_PLANKS,
        WARPED_PLANKS,
        STICK,
        ACACIA_BOAT,
        ACACIA_CHEST_BOAT,
        BIRCH_BOAT,
        BIRCH_CHEST_BOAT,
        CHERRY_BOAT,
        CHERRY_CHEST_BOAT,
        DARK_OAK_BOAT,
        DARK_OAK_CHEST_BOAT,
        JUNGLE_BOAT,
        JUNGLE_CHEST_BOAT,
        MANGROVE_BOAT,
        MANGROVE_CHEST_BOAT,
        OAK_BOAT,
        OAK_CHEST_BOAT,
        SPRUCE_BOAT,
        SPRUCE_CHEST_BOAT,
        WOODEN_AXE,
        WOODEN_SHOVEL,
        WOODEN_SWORD,
        WOODEN_HOE,
        WOODEN_PICKAXE,
        BOWL,
        ACACIA_LOG,
        BIRCH_LOG,
        CHERRY_LOG,
        DARK_OAK_LOG,
        JUNGLE_LOG,
        MANGROVE_LOG,
        OAK_LOG,
        SPRUCE_LOG,
        STRIPPED_ACACIA_LOG,
        STRIPPED_BIRCH_LOG,
        STRIPPED_CHERRY_LOG,
        STRIPPED_DARK_OAK_LOG,
        STRIPPED_JUNGLE_LOG,
        STRIPPED_MANGROVE_LOG,
        STRIPPED_OAK_LOG,
        STRIPPED_SPRUCE_LOG,
        STRIPPED_ACACIA_WOOD,
        STRIPPED_BIRCH_WOOD,
        STRIPPED_CHERRY_WOOD,
        STRIPPED_DARK_OAK_WOOD,
        STRIPPED_JUNGLE_WOOD,
        STRIPPED_MANGROVE_WOOD,
        STRIPPED_SPRUCE_WOOD,
        STRIPPED_OAK_WOOD,
        ACACIA_WOOD,
        BIRCH_WOOD,
        CHERRY_WOOD,
        DARK_OAK_WOOD,
        JUNGLE_WOOD,
        MANGROVE_WOOD,
        OAK_WOOD,
        SPRUCE_WOOD,
        ACACIA_SIGN,
        BIRCH_SIGN,
        CHERRY_SIGN,
        DARK_OAK_SIGN,
        JUNGLE_SIGN,
        MANGROVE_SIGN,
        OAK_SIGN,
        SPRUCE_SIGN,
        ACACIA_WALL_SIGN,
        BIRCH_WALL_SIGN,
        CHERRY_WALL_SIGN,
        DARK_OAK_WALL_SIGN,
        JUNGLE_WALL_SIGN,
        MANGROVE_WALL_SIGN,
        OAK_WALL_SIGN,
        SPRUCE_WALL_SIGN,
        ACACIA_SAPLING,
        BIRCH_SAPLING,
        CHERRY_SAPLING,
        DARK_OAK_SAPLING,
        JUNGLE_SAPLING,
        OAK_SAPLING,
        SPRUCE_SAPLING,
        LAVA_BUCKET;
    }

}
