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

    //every material that the plugin interact with
    public enum Material{
        COOKED_PORKCHOP(0.35f),
        BAKED_POTATO(0.35f),
        COOKED_BEEF(0.35f),
        COOKED_PORKCH(0.35f),
        COOKED_CHICKEN(0.35f),
        COOKED_MUTTON(0.35f),
        COOKED_COD(0.35f),
        COOKED_RABBIT(0.35f),
        COOKED_SALMON(0.35f),
        IRON_INGOT(0.7f),
        COPPER_INGOT(0.7f),
        GOLD_INGOT(1),
        EMERALD(1),
        NETHERITE_SCRAP(2),
        DIAMOND(1),
        DRIED_KELP(0.1f),
        IRON_NUGGET(0.1f),
        GOLD_NUGGET(0.1f),
        GREEN_DYE(0.1f),
        POPPED_CHORUS_FRUIT(0.1f),
        SPONGE(0.15f),
        LAPIS_LAZULI(0.2f),
        GLASS(0.1f),
        REDSTONE(0.3f),
        BRICK(0.35f),
        STONE(0.1f),
        TERRACOTTA(0.35f),
        LIME_DYE(0.1f),
        NETHER_BRICK(0.1f),
        DEEPSLATE(0.1f),
        CRACKED_STONE_BRICKS(0.1f),
        CRACKED_DEEPSLATE(0.1f),
        CRACKED_DEEPSLATE_TILES(0.1f),
        CRACKED_DEEPSLATE_BRICKS(0.1f),
        SMOOTH_SANDSTONE(0.1f),
        SMOOTH_RED_SANDSTONE(0.1f),
        CRACKED_NETHER_BRICKS(0.1f),
        SMOOTH_BASALT(0.1f),
        CRACKED_POLISHED_BLACKSTONE_BRICKS(0.1f),
        SMOOTH_QUARTZ(0.1f),
        GRAY_GLAZED_TERRACOTTA(0.1f),
        GREEN_GLAZED_TERRACOTTA(0.1f),
        BLACK_GLAZED_TERRACOTTA(0.1f),
        BLUE_GLAZED_TERRACOTTA(0.1f),
        BROWN_GLAZED_TERRACOTTA(0.1f),
        CYAN_GLAZED_TERRACOTTA(0.1f),
        LIGHT_BLUE_GLAZED_TERRACOTTA(0.1f),
        LIGHT_GRAY_GLAZED_TERRACOTTA(0.1f),
        LIME_GLAZED_TERRACOTTA(0.1f),
        YELLOW_GLAZED_TERRACOTTA(0.1f),
        WHITE_GLAZED_TERRACOTTA(0.1f),
        ORANGE_GLAZED_TERRACOTTA(0.1f),
        MAGENTA_GLAZED_TERRACOTTA(0.1f),
        PINK_GLAZED_TERRACOTTA(0.1f),
        PURPLE_GLAZED_TERRACOTTA(0.1f),
        RED_GLAZED_TERRACOTTA(0.1f),
        SMOOTH_STONE(0.1f),
        BLACK_CARPET(0),
        BLUE_CARPET(0),
        BROWN_CARPET(0),
        CYAN_CARPET(0),
        GRAY_CARPET(0),
        GREEN_CARPET(0),
        LIGHT_BLUE_CARPET(0),
        LIGHT_GRAY_CARPET(0),
        LIME_CARPET(0),
        MAGENTA_CARPET(0),
        ORANGE_CARPET(0),
        PINK_CARPET(0),
        PURPLE_CARPET(0),
        RED_CARPET(0),
        WHITE_CARPET(0),
        YELLOW_CARPET(0),
        DRIED_KELP_BLOCK(0),
        COAL(0.1f),
        CHARCOAL(0.15f),
        COAL_BLOCK(0),
        BLAZE_ROD(0),
        ACACIA_PLANKS(0),
        BAMBOO_PLANKS(0),
        BIRCH_PLANKS(0),
        CHERRY_PLANKS(0),
        CRIMSON_PLANKS(0),
        OAK_PLANKS(0),
        DARK_OAK_PLANKS(0),
        JUNGLE_PLANKS(0),
        MANGROVE_PLANKS(0),
        SPRUCE_PLANKS(0),
        WARPED_PLANKS(0),
        STICK(0),
        ACACIA_BOAT(0),
        ACACIA_CHEST_BOAT(0),
        BIRCH_BOAT(0),
        BIRCH_CHEST_BOAT(0),
        CHERRY_BOAT(0),
        CHERRY_CHEST_BOAT(0),
        DARK_OAK_BOAT(0),
        DARK_OAK_CHEST_BOAT(0),
        JUNGLE_BOAT(0),
        JUNGLE_CHEST_BOAT(0),
        MANGROVE_BOAT(0),
        MANGROVE_CHEST_BOAT(0),
        OAK_BOAT(0),
        OAK_CHEST_BOAT(0),
        SPRUCE_BOAT(0),
        SPRUCE_CHEST_BOAT(0),
        WOODEN_AXE(0),
        WOODEN_SHOVEL(0),
        WOODEN_SWORD(0),
        WOODEN_HOE(0),
        WOODEN_PICKAXE(0),
        BOWL(0),
        ACACIA_LOG(0),
        BIRCH_LOG(0),
        CHERRY_LOG(0),
        DARK_OAK_LOG(0),
        JUNGLE_LOG(0),
        MANGROVE_LOG(0),
        OAK_LOG(0),
        SPRUCE_LOG(0),
        STRIPPED_ACACIA_LOG(0),
        STRIPPED_BIRCH_LOG(0),
        STRIPPED_CHERRY_LOG(0),
        STRIPPED_DARK_OAK_LOG(0),
        STRIPPED_JUNGLE_LOG(0),
        STRIPPED_MANGROVE_LOG(0),
        STRIPPED_OAK_LOG(0),
        STRIPPED_SPRUCE_LOG(0),
        STRIPPED_ACACIA_WOOD(0),
        STRIPPED_BIRCH_WOOD(0),
        STRIPPED_CHERRY_WOOD(0),
        STRIPPED_DARK_OAK_WOOD(0),
        STRIPPED_JUNGLE_WOOD(0),
        STRIPPED_MANGROVE_WOOD(0),
        STRIPPED_SPRUCE_WOOD(0),
        STRIPPED_OAK_WOOD(0),
        ACACIA_WOOD(0),
        BIRCH_WOOD(0),
        CHERRY_WOOD(0),
        DARK_OAK_WOOD(0),
        JUNGLE_WOOD(0),
        MANGROVE_WOOD(0),
        OAK_WOOD(0),
        SPRUCE_WOOD(0),
        ACACIA_SIGN(0),
        BIRCH_SIGN(0),
        CHERRY_SIGN(0),
        DARK_OAK_SIGN(0),
        JUNGLE_SIGN(0),
        MANGROVE_SIGN(0),
        OAK_SIGN(0),
        SPRUCE_SIGN(0),
        ACACIA_WALL_SIGN(0),
        BIRCH_WALL_SIGN(0),
        CHERRY_WALL_SIGN(0),
        DARK_OAK_WALL_SIGN(0),
        JUNGLE_WALL_SIGN(0),
        MANGROVE_WALL_SIGN(0),
        OAK_WALL_SIGN(0),
        SPRUCE_WALL_SIGN(0),
        ACACIA_SAPLING(0),
        BIRCH_SAPLING(0),
        CHERRY_SAPLING(0),
        DARK_OAK_SAPLING(0),
        JUNGLE_SAPLING(0),
        OAK_SAPLING(0),
        SPRUCE_SAPLING(0),
        LAVA_BUCKET(0);

        public float getExpFromCooking() {
            return exp;
        }

        private final float exp;

        Material(float exp){
            this.exp = exp;
        }
    }

}
