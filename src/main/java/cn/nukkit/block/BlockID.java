package cn.nukkit.block;

import cn.nukkit.api.DeprecationDetails;
import cn.nukkit.api.PowerNukkitOnly;
import cn.nukkit.api.Since;

public interface BlockID {
    int AIR = 0;
    int STONE = 1;
    int GRASS = 2;
    int DIRT = 3;
    int COBBLESTONE = 4;
    int COBBLE = 4;
    int PLANK = 5;
    int PLANKS = 5;
    int WOODEN_PLANK = 5;
    int WOODEN_PLANKS = 5;
    int SAPLING = 6;
    int SAPLINGS = 6;
    int BEDROCK = 7;
    int WATER = 8;
    int STILL_WATER = 9;
    int LAVA = 10;
    int STILL_LAVA = 11;
    int SAND = 12;
    int GRAVEL = 13;
    int GOLD_ORE = 14;
    int IRON_ORE = 15;
    int COAL_ORE = 16;
    int LOG = 17;
    int WOOD = 17;
    int TRUNK = 17;
    int LEAVES = 18;
    int LEAVE = 18;
    int SPONGE = 19;
    int GLASS = 20;
    int LAPIS_ORE = 21;
    int LAPIS_BLOCK = 22;
    int DISPENSER = 23;
    int SANDSTONE = 24;
    int NOTEBLOCK = 25;
    int BED_BLOCK = 26;
    int POWERED_RAIL = 27;
    int DETECTOR_RAIL = 28;
    int STICKY_PISTON = 29;
    int COBWEB = 30;
    int TALL_GRASS = 31;
    int BUSH = 32;
    int DEAD_BUSH = 32;
    int PISTON = 33;
    @PowerNukkitOnly @Since("1.6.0.0-PN") int PISTON_ARM_COLLISION = 34;
    @Deprecated
    @DeprecationDetails(since = "1.6.0.0-PN", by = "PowerNukkit", reason = "Incorrect name", replaceWith = "PISTON_ARM_COLLISION")
    int PISTON_HEAD = 34;
    int WOOL = 35;
    int DANDELION = 37;
    int POPPY = 38;
    int ROSE = 38;
    int FLOWER = 38;
    int RED_FLOWER = 38;
    int BROWN_MUSHROOM = 39;
    int RED_MUSHROOM = 40;
    int GOLD_BLOCK = 41;
    int IRON_BLOCK = 42;
    int DOUBLE_SLAB = 43;
    @PowerNukkitOnly @Since("FUTURE") int DOUBLE_STONE_BLOCK_SLAB = 43;
    @Deprecated
    @DeprecationDetails(since = "FUTURE", by = "PowerNukkit", reason = "Minecraft update", replaceWith = "DOUBLE_STONE_BLOCK_SLAB")
    int DOUBLE_STONE_SLAB = 43;
    @Deprecated
    @DeprecationDetails(since = "FUTURE", by = "PowerNukkit", reason = "Minecraft update", replaceWith = "DOUBLE_STONE_BLOCK_SLAB")
    int DOUBLE_SLABS = 43;
    int SLAB = 44;
    @PowerNukkitOnly @Since("FUTURE") int STONE_BLOCK_SLAB = 44;
    @Deprecated
    @DeprecationDetails(since = "FUTURE", by = "PowerNukkit", reason = "Minecraft update", replaceWith = "STONE_BLOCK_SLAB")
    int STONE_SLAB = 44;
    int SLABS = 44;
    int BRICKS = 45;
    int BRICKS_BLOCK = 45;
    int TNT = 46;
    int BOOKSHELF = 47;
    int MOSS_STONE = 48;
    int MOSSY_STONE = 48;
    int OBSIDIAN = 49;
    int TORCH = 50;
    int FIRE = 51;
    int MONSTER_SPAWNER = 52;
    @PowerNukkitOnly @Since("1.4.0.0-PN") int MOB_SPAWNER = MONSTER_SPAWNER;
    int WOOD_STAIRS = 53;
    int WOODEN_STAIRS = 53;
    int OAK_WOOD_STAIRS = 53;
    int OAK_WOODEN_STAIRS = 53;
    int CHEST = 54;
    int REDSTONE_WIRE = 55;
    int DIAMOND_ORE = 56;
    int DIAMOND_BLOCK = 57;
    int CRAFTING_TABLE = 58;
    int WORKBENCH = 58;
    int WHEAT_BLOCK = 59;
    int FARMLAND = 60;
    int FURNACE = 61;
    int BURNING_FURNACE = 62;
    int LIT_FURNACE = 62;
    int SIGN_POST = 63;
    int DOOR_BLOCK = 64;
    int WOODEN_DOOR_BLOCK = 64;
    int WOOD_DOOR_BLOCK = 64;
    int LADDER = 65;
    int RAIL = 66;
    int COBBLE_STAIRS = 67;
    int COBBLESTONE_STAIRS = 67;
    int WALL_SIGN = 68;
    int LEVER = 69;
    int STONE_PRESSURE_PLATE = 70;
    int IRON_DOOR_BLOCK = 71;
    int WOODEN_PRESSURE_PLATE = 72;

    int REDSTONE_ORE = 73;
    int GLOWING_REDSTONE_ORE = 74;
    int LIT_REDSTONE_ORE = 74;
    int UNLIT_REDSTONE_TORCH = 75;
    int REDSTONE_TORCH = 76;
    int STONE_BUTTON = 77;
    int SNOW = 78;
    int SNOW_LAYER = 78;
    int ICE = 79;
    int SNOW_BLOCK = 80;
    int CACTUS = 81;
    int CLAY_BLOCK = 82;
    int REEDS = 83;
    int SUGARCANE_BLOCK = 83;
    int JUKEBOX = 84;
    int FENCE = 85;
    int PUMPKIN = 86;
    int NETHERRACK = 87;
    int SOUL_SAND = 88;
    int GLOWSTONE = 89;
    int GLOWSTONE_BLOCK = 89;
    int NETHER_PORTAL = 90;
    int LIT_PUMPKIN = 91;
    int JACK_O_LANTERN = 91;
    int CAKE_BLOCK = 92;
    int UNPOWERED_REPEATER = 93;
    int POWERED_REPEATER = 94;
    int INVISIBLE_BEDROCK = 95;
    int TRAPDOOR = 96;
    int MONSTER_EGG = 97;
    int STONE_BRICKS = 98;
    int STONE_BRICK = 98;
    int BROWN_MUSHROOM_BLOCK = 99;
    int RED_MUSHROOM_BLOCK = 100;
    int IRON_BAR = 101;
    int IRON_BARS = 101;
    int GLASS_PANE = 102;
    int GLASS_PANEL = 102;
    int MELON_BLOCK = 103;
    int PUMPKIN_STEM = 104;
    int MELON_STEM = 105;
    int VINE = 106;
    int VINES = 106;
    int FENCE_GATE = 107;
    int FENCE_GATE_OAK = 107;
    int BRICK_STAIRS = 108;
    int STONE_BRICK_STAIRS = 109;
    int MYCELIUM = 110;
    int WATER_LILY = 111;
    int LILY_PAD = 111;
    int NETHER_BRICKS = 112;
    int NETHER_BRICK_BLOCK = 112;
    int NETHER_BRICK_FENCE = 113;
    int NETHER_BRICKS_STAIRS = 114;
    int NETHER_WART_BLOCK = 115;
    int ENCHANTING_TABLE = 116;
    int ENCHANT_TABLE = 116;
    int ENCHANTMENT_TABLE = 116;
    int BREWING_STAND_BLOCK = 117;
    int BREWING_BLOCK = 117;
    int CAULDRON_BLOCK = 118;
    int END_PORTAL = 119;
    int END_PORTAL_FRAME = 120;
    int END_STONE = 121;
    int DRAGON_EGG = 122;
    int REDSTONE_LAMP = 123;
    int LIT_REDSTONE_LAMP = 124;
    //Note: dropper CAN NOT BE HARVESTED WITH HAND -- canHarvestWithHand method should be overridden FALSE.
    int DROPPER = 125;
    int ACTIVATOR_RAIL = 126;
    int COCOA = 127;
    int COCOA_BLOCK = 127;
    int SANDSTONE_STAIRS = 128;
    int EMERALD_ORE = 129;
    int ENDER_CHEST = 130;
    int TRIPWIRE_HOOK = 131;
    @PowerNukkitOnly @Since("1.6.0.0-PN") int TRIP_WIRE = 132;
    @Deprecated
    @DeprecationDetails(since = "1.6.0.0-PN", by = "Mojang", reason = "Renamed", replaceWith = "TRIP_WIRE")
    int TRIPWIRE = 132;
    int EMERALD_BLOCK = 133;
    int SPRUCE_WOOD_STAIRS = 134;
    int SPRUCE_WOODEN_STAIRS = 134;
    int BIRCH_WOOD_STAIRS = 135;
    int BIRCH_WOODEN_STAIRS = 135;
    int JUNGLE_WOOD_STAIRS = 136;
    int JUNGLE_WOODEN_STAIRS = 136;

    int BEACON = 138;
    int COBBLE_WALL = 139;
    int STONE_WALL = 139;
    int COBBLESTONE_WALL = 139;
    int FLOWER_POT_BLOCK = 140;
    int CARROT_BLOCK = 141;
    int POTATO_BLOCK = 142;
    int WOODEN_BUTTON = 143;
    int SKULL_BLOCK = 144;
    int ANVIL = 145;
    int TRAPPED_CHEST = 146;
    int LIGHT_WEIGHTED_PRESSURE_PLATE = 147;
    int HEAVY_WEIGHTED_PRESSURE_PLATE = 148;
    int UNPOWERED_COMPARATOR = 149;
    int POWERED_COMPARATOR = 150;
    int DAYLIGHT_DETECTOR = 151;
    int REDSTONE_BLOCK = 152;
    int QUARTZ_ORE = 153;
    int HOPPER_BLOCK = 154;
    int QUARTZ_BLOCK = 155;
    int QUARTZ_STAIRS = 156;
    int DOUBLE_WOOD_SLAB = 157;
    int DOUBLE_WOODEN_SLAB = 157;
    int DOUBLE_WOOD_SLABS = 157;
    int DOUBLE_WOODEN_SLABS = 157;
    int WOOD_SLAB = 158;
    int WOODEN_SLAB = 158;
    int WOOD_SLABS = 158;
    int WOODEN_SLABS = 158;
    int STAINED_TERRACOTTA = 159;
    int STAINED_HARDENED_CLAY = STAINED_TERRACOTTA;
    int STAINED_GLASS_PANE = 160;
    int LEAVES2 = 161;
    int LEAVE2 = 161;
    int WOOD2 = 162;
    int TRUNK2 = 162;
    int LOG2 = 162;
    int ACACIA_WOOD_STAIRS = 163;
    int ACACIA_WOODEN_STAIRS = 163;
    int DARK_OAK_WOOD_STAIRS = 164;
    int DARK_OAK_WOODEN_STAIRS = 164;
    int SLIME_BLOCK = 165;

    int IRON_TRAPDOOR = 167;
    int PRISMARINE = 168;
    int SEA_LANTERN = 169;
    int HAY_BALE = 170;
    int CARPET = 171;
    int TERRACOTTA = 172;
    @Since("1.4.0.0-PN") @PowerNukkitOnly int HARDENED_CLAY = TERRACOTTA;
    int COAL_BLOCK = 173;
    int PACKED_ICE = 174;
    int DOUBLE_PLANT = 175;
    int STANDING_BANNER = 176;
    int WALL_BANNER = 177;
    int DAYLIGHT_DETECTOR_INVERTED = 178;
    int RED_SANDSTONE = 179;
    int RED_SANDSTONE_STAIRS = 180;
    @PowerNukkitOnly @Since("FUTURE") int DOUBLE_STONE_BLOCK_SLAB2 = 181;
    @Deprecated
    @DeprecationDetails(since = "FUTURE", by = "PowerNukkit", reason = "Minecraft update", replaceWith = "DOUBLE_STONE_BLOCK_SLAB2")
    int DOUBLE_RED_SANDSTONE_SLAB = 181;
    @PowerNukkitOnly @Since("FUTURE") int STONE_BLOCK_SLAB2 = 182;
    @Deprecated
    @DeprecationDetails(since = "FUTURE", by = "PowerNukkit", reason = "Minecraft update", replaceWith = "STONE_BLOCK_SLAB2")
    int RED_SANDSTONE_SLAB = 182;
    int FENCE_GATE_SPRUCE = 183;
    int FENCE_GATE_BIRCH = 184;
    int FENCE_GATE_JUNGLE = 185;
    int FENCE_GATE_DARK_OAK = 186;
    int FENCE_GATE_ACACIA = 187;

    int SPRUCE_DOOR_BLOCK = 193;
    int BIRCH_DOOR_BLOCK = 194;
    int JUNGLE_DOOR_BLOCK = 195;
    int ACACIA_DOOR_BLOCK = 196;
    int DARK_OAK_DOOR_BLOCK = 197;
    int GRASS_PATH = 198;
    int ITEM_FRAME_BLOCK = 199;
    int CHORUS_FLOWER = 200;
    int PURPUR_BLOCK = 201;
    //int COLORED_TORCH_RG = 202;
    int PURPUR_STAIRS = 203;
    //int COLORED_TORCH_BP = 204;
    int UNDYED_SHULKER_BOX = 205;
    int END_BRICKS = 206;
    //Note: frosted ice CAN NOT BE HARVESTED WITH HAND -- canHarvestWithHand method should be overridden FALSE.
    int ICE_FROSTED = 207;
    int END_ROD = 208;
    int END_GATEWAY = 209;
    @PowerNukkitOnly @Since("1.4.0.0-PN") int ALLOW = 210;
    @PowerNukkitOnly @Since("1.4.0.0-PN") int DENY = 211;
    @PowerNukkitOnly @Since("1.4.0.0-PN") int BORDER_BLOCK = 212;
    int MAGMA = 213;
    int BLOCK_NETHER_WART_BLOCK = 214;
    int RED_NETHER_BRICK = 215;
    int BONE_BLOCK = 216;
    @PowerNukkitOnly @Since("1.4.0.0-PN") int STRUCTURE_VOID = 217;
    int SHULKER_BOX = 218;
    int PURPLE_GLAZED_TERRACOTTA = 219;
    int WHITE_GLAZED_TERRACOTTA = 220;
    int ORANGE_GLAZED_TERRACOTTA = 221;
    int MAGENTA_GLAZED_TERRACOTTA = 222;
    int LIGHT_BLUE_GLAZED_TERRACOTTA = 223;
    int YELLOW_GLAZED_TERRACOTTA = 224;
    int LIME_GLAZED_TERRACOTTA = 225;
    int PINK_GLAZED_TERRACOTTA = 226;
    int GRAY_GLAZED_TERRACOTTA = 227;
    int SILVER_GLAZED_TERRACOTTA = 228;
    int CYAN_GLAZED_TERRACOTTA = 229;

    int BLUE_GLAZED_TERRACOTTA = 231;
    int BROWN_GLAZED_TERRACOTTA = 232;
    int GREEN_GLAZED_TERRACOTTA = 233;
    int RED_GLAZED_TERRACOTTA = 234;
    int BLACK_GLAZED_TERRACOTTA = 235;
    int CONCRETE = 236;
    int CONCRETE_POWDER = 237;
    @Since("1.4.0.0-PN")
    @PowerNukkitOnly
    @Deprecated @DeprecationDetails(since = "1.6.0.0-PN", by = "Mojang", replaceWith = "CONCRETE_POWDER", reason = "Renamed")
    int CONCRETEPOWDER = CONCRETE_POWDER;

    int CHORUS_PLANT = 240;
    int STAINED_GLASS = 241;

    int PODZOL = 243;
    int BEETROOT_BLOCK = 244;
    int STONECUTTER = 245;
    int GLOWING_OBSIDIAN = 246;
    @Since("1.4.0.0-PN") @PowerNukkitOnly int NETHERREACTOR = 247;
    int NETHER_REACTOR = NETHERREACTOR;

    @Since("1.5.0.0-PN") @Deprecated @DeprecationDetails(by = "PowerNukkit",
            reason = "This was added by Cloudburst Nukkit, but it is a tecnical block, avoid usinig it.",
            since = "1.5.0.0-PN")
    int INFO_UPDATE = 248;

    @Since("1.5.0.0-PN") @Deprecated @DeprecationDetails(by = "PowerNukkit",
            reason = "This was added by Cloudburst Nukkit, but it is a tecnical block, avoid usinig it.",
            since = "1.5.0.0-PN")
    int INFO_UPDATE2 = 249;

    int PISTON_EXTENSION = 250;
    @Since("1.4.0.0-PN") @PowerNukkitOnly int MOVING_BLOCK = PISTON_EXTENSION;

    int OBSERVER = 251;
    @Since("1.4.0.0-PN") @PowerNukkitOnly int STRUCTURE_BLOCK = 252;

    @PowerNukkitOnly int PRISMARINE_STAIRS = 257;
    @PowerNukkitOnly int DARK_PRISMARINE_STAIRS = 258;
    @PowerNukkitOnly int PRISMARINE_BRICKS_STAIRS = 259;
    @PowerNukkitOnly int STRIPPED_SPRUCE_LOG = 260;
    @PowerNukkitOnly int STRIPPED_BIRCH_LOG = 261;
    @PowerNukkitOnly int STRIPPED_JUNGLE_LOG = 262;
    @PowerNukkitOnly int STRIPPED_ACACIA_LOG = 263;
    @PowerNukkitOnly int STRIPPED_DARK_OAK_LOG = 264;
    @PowerNukkitOnly int STRIPPED_OAK_LOG = 265;
    @PowerNukkitOnly int BLUE_ICE = 266;

    @PowerNukkitOnly int SEAGRASS = 385;
    @PowerNukkitOnly int CORAL = 386;
    @PowerNukkitOnly int CORAL_BLOCK = 387;
    @PowerNukkitOnly int CORAL_FAN = 388;
    @PowerNukkitOnly int CORAL_FAN_DEAD = 389;
    @PowerNukkitOnly int CORAL_FAN_HANG = 390;
    @PowerNukkitOnly int CORAL_FAN_HANG2 = 391;
    @PowerNukkitOnly int CORAL_FAN_HANG3 = 392;
    @PowerNukkitOnly int BLOCK_KELP = 393;
    @PowerNukkitOnly int DRIED_KELP_BLOCK = 394;
    @PowerNukkitOnly int ACACIA_BUTTON = 395;
    @PowerNukkitOnly int BIRCH_BUTTON = 396;
    @PowerNukkitOnly int DARK_OAK_BUTTON = 397;
    @PowerNukkitOnly int JUNGLE_BUTTON = 398;
    @PowerNukkitOnly int SPRUCE_BUTTON = 399;
    @PowerNukkitOnly int ACACIA_TRAPDOOR = 400;
    @PowerNukkitOnly int BIRCH_TRAPDOOR = 401;
    @PowerNukkitOnly int DARK_OAK_TRAPDOOR = 402;
    @PowerNukkitOnly int JUNGLE_TRAPDOOR = 403;
    @PowerNukkitOnly int SPRUCE_TRAPDOOR = 404;
    @PowerNukkitOnly int ACACIA_PRESSURE_PLATE = 405;
    @PowerNukkitOnly int BIRCH_PRESSURE_PLATE = 406;
    @PowerNukkitOnly int DARK_OAK_PRESSURE_PLATE = 407;
    @PowerNukkitOnly int JUNGLE_PRESSURE_PLATE = 408;
    @PowerNukkitOnly int SPRUCE_PRESSURE_PLATE = 409;
    @PowerNukkitOnly int CARVED_PUMPKIN = 410;
    @PowerNukkitOnly int SEA_PICKLE = 411;
    @PowerNukkitOnly int CONDUIT = 412;

    @PowerNukkitOnly int TURTLE_EGG = 414;
    @PowerNukkitOnly int BUBBLE_COLUMN = 415;
    @PowerNukkitOnly int BARRIER = 416;
    @PowerNukkitOnly @Since("FUTURE") int STONE_BLOCK_SLAB3 = 417;
    @Deprecated
    @DeprecationDetails(since = "FUTURE", by = "PowerNukkit", reason = "Minecraft update", replaceWith = "STONE_BLOCK_SLAB3")
    @PowerNukkitOnly
    int STONE_SLAB3 = 417;
    @PowerNukkitOnly int BAMBOO = 418;
    @PowerNukkitOnly int BAMBOO_SAPLING = 419;
    @PowerNukkitOnly int SCAFFOLDING = 420;
    @PowerNukkitOnly @Since("FUTURE") int STONE_BLOCK_SLAB4 = 421;
    @Deprecated
    @DeprecationDetails(since = "FUTURE", by = "PowerNukkit", reason = "Minecraft update", replaceWith = "STONE_BLOCK_SLAB4")
    @PowerNukkitOnly
    int STONE_SLAB4 = 421;
    @PowerNukkitOnly @Since("FUTURE") int DOUBLE_STONE_BLOCK_SLAB3 = 422;
    @Deprecated
    @DeprecationDetails(since = "FUTURE", by = "PowerNukkit", reason = "Minecraft update", replaceWith = "DOUBLE_STONE_BLOCK_SLAB3")
    @PowerNukkitOnly
    int DOUBLE_STONE_SLAB3 = 422;
    @PowerNukkitOnly @Since("FUTURE") int DOUBLE_STONE_BLOCK_SLAB4 = 423;
    @Deprecated
    @DeprecationDetails(since = "FUTURE", by = "PowerNukkit", reason = "Minecraft update", replaceWith = "DOUBLE_STONE_BLOCK_SLAB4")
    @PowerNukkitOnly
    int DOUBLE_STONE_SLAB4 = 423;
    @PowerNukkitOnly int GRANITE_STAIRS = 424;
    @PowerNukkitOnly int DIORITE_STAIRS = 425;
    @PowerNukkitOnly int ANDESITE_STAIRS = 426;
    @PowerNukkitOnly int POLISHED_GRANITE_STAIRS = 427;
    @PowerNukkitOnly int POLISHED_DIORITE_STAIRS = 428;
    @PowerNukkitOnly int POLISHED_ANDESITE_STAIRS = 429;
    @PowerNukkitOnly int MOSSY_STONE_BRICK_STAIRS = 430;
    @PowerNukkitOnly int SMOOTH_RED_SANDSTONE_STAIRS = 431;
    @PowerNukkitOnly int SMOOTH_SANDSTONE_STAIRS = 432;
    @PowerNukkitOnly int END_BRICK_STAIRS = 433;
    @PowerNukkitOnly int MOSSY_COBBLESTONE_STAIRS = 434;
    @PowerNukkitOnly int NORMAL_STONE_STAIRS = 435;
    @PowerNukkitOnly int SPRUCE_STANDING_SIGN = 436;
    @PowerNukkitOnly int SPRUCE_WALL_SIGN = 437;
    @PowerNukkitOnly int SMOOTH_STONE = 438;
    @PowerNukkitOnly int RED_NETHER_BRICK_STAIRS = 439;
    @PowerNukkitOnly int SMOOTH_QUARTZ_STAIRS = 440;
    @PowerNukkitOnly int BIRCH_STANDING_SIGN = 441;
    @PowerNukkitOnly int BIRCH_WALL_SIGN = 442;
    @PowerNukkitOnly int JUNGLE_STANDING_SIGN = 443;
    @PowerNukkitOnly int JUNGLE_WALL_SIGN = 444;
    @PowerNukkitOnly int ACACIA_STANDING_SIGN = 445;
    @PowerNukkitOnly int ACACIA_WALL_SIGN = 446;
    @PowerNukkitOnly int DARKOAK_STANDING_SIGN = 447;
    @PowerNukkitOnly int DARK_OAK_STANDING_SIGN = 447;
    @PowerNukkitOnly int DARKOAK_WALL_SIGN = 448;
    @PowerNukkitOnly int DARK_OAK_WALL_SIGN = 448;
    @PowerNukkitOnly int LECTERN = 449;
    @PowerNukkitOnly int GRINDSTONE = 450;
    @PowerNukkitOnly int BLAST_FURNACE = 451;
    @PowerNukkitOnly int STONECUTTER_BLOCK = 452;
    @PowerNukkitOnly int SMOKER = 453;
    @PowerNukkitOnly int LIT_SMOKER = 454;
    @PowerNukkitOnly int CARTOGRAPHY_TABLE = 455;
    @PowerNukkitOnly int FLETCHING_TABLE = 456;
    @PowerNukkitOnly int SMITHING_TABLE = 457;
    @PowerNukkitOnly int BARREL = 458;
    @PowerNukkitOnly int LOOM = 459;

     @PowerNukkitOnly int BELL = 461;
     @PowerNukkitOnly int SWEET_BERRY_BUSH = 462;
     @PowerNukkitOnly int LANTERN = 463;
     @PowerNukkitOnly int CAMPFIRE_BLOCK = 464;
     @PowerNukkitOnly int LAVA_CAULDRON = 465;
     @PowerNukkitOnly int JIGSAW = 466;
     @PowerNukkitOnly int WOOD_BARK = 467;
     @PowerNukkitOnly int COMPOSTER = 468;
     @PowerNukkitOnly int LIT_BLAST_FURNACE = 469;
     @PowerNukkitOnly int LIGHT_BLOCK = 470;
     @PowerNukkitOnly int WITHER_ROSE = 471;
     @PowerNukkitOnly @Since("1.6.0.0-PN") int STICKY_PISTON_ARM_COLLISION = 472;
     @PowerNukkitOnly
     @Deprecated
     @DeprecationDetails(since = "1.6.0.0-PN", by = "Mojang", replaceWith = "STICKY_PISTON_ARM_COLLISION", reason = "Renamed")
     int STICKYPISTONARMCOLLISION = STICKY_PISTON_ARM_COLLISION;
     @PowerNukkitOnly
     @Deprecated
     @DeprecationDetails(since = "1.6.0.0-PN", by = "PowerNukkit", replaceWith = "STICKY_PISTON_ARM_COLLISION", reason = "Renamed")
     int PISTON_HEAD_STICKY = STICKY_PISTON_ARM_COLLISION;
     @PowerNukkitOnly int BEE_NEST = 473;
     @PowerNukkitOnly int BEEHIVE = 474;
     @PowerNukkitOnly int HONEY_BLOCK = 475;
     @PowerNukkitOnly int HONEYCOMB_BLOCK = 476;
    @Since("1.4.0.0-PN") @PowerNukkitOnly int LODESTONE = 477;
    @Since("1.4.0.0-PN") @PowerNukkitOnly int CRIMSON_ROOTS = 478;
    @Since("1.4.0.0-PN") @PowerNukkitOnly int WARPED_ROOTS = 479;
    @Since("1.4.0.0-PN") @PowerNukkitOnly int CRIMSON_STEM = 480;
    @Since("1.4.0.0-PN") @PowerNukkitOnly int WARPED_STEM = 481;
    @Since("1.4.0.0-PN") @PowerNukkitOnly int WARPED_WART_BLOCK = 482;
    @Since("1.4.0.0-PN") @PowerNukkitOnly int CRIMSON_FUNGUS = 483;
    @Since("1.4.0.0-PN") @PowerNukkitOnly int WARPED_FUNGUS = 484;
    @Since("1.4.0.0-PN") @PowerNukkitOnly int SHROOMLIGHT = 485;
    @Since("1.4.0.0-PN") @PowerNukkitOnly int WEEPING_VINES = 486;
    @Since("1.4.0.0-PN") @PowerNukkitOnly int CRIMSON_NYLIUM = 487;
    @Since("1.4.0.0-PN") @PowerNukkitOnly int WARPED_NYLIUM = 488;
    @Since("1.4.0.0-PN") @PowerNukkitOnly int BASALT = 489;
    @Since("1.4.0.0-PN") @PowerNukkitOnly int POLISHED_BASALT = 490;
    @Since("1.4.0.0-PN") @PowerNukkitOnly int SOUL_SOIL = 491;
    @Since("1.4.0.0-PN") @PowerNukkitOnly int SOUL_FIRE = 492;
    @Since("1.4.0.0-PN") @PowerNukkitOnly int NETHER_SPROUTS_BLOCK = 493;
    @Since("1.4.0.0-PN") @PowerNukkitOnly int TARGET = 494;
    @Since("1.4.0.0-PN") @PowerNukkitOnly int STRIPPED_CRIMSON_STEM = 495;
    @Since("1.4.0.0-PN") @PowerNukkitOnly int STRIPPED_WARPED_STEM = 496;
    @Since("1.4.0.0-PN") @PowerNukkitOnly int CRIMSON_PLANKS = 497;
    @Since("1.4.0.0-PN") @PowerNukkitOnly int WARPED_PLANKS = 498;
    @Since("1.4.0.0-PN") @PowerNukkitOnly int CRIMSON_DOOR_BLOCK = 499;
    @Since("1.4.0.0-PN") @PowerNukkitOnly int WARPED_DOOR_BLOCK = 500;
    @Since("1.4.0.0-PN") @PowerNukkitOnly int CRIMSON_TRAPDOOR = 501;
    @Since("1.4.0.0-PN") @PowerNukkitOnly int WARPED_TRAPDOOR = 502;
    // 503
    // 504
    @Since("1.4.0.0-PN") @PowerNukkitOnly int CRIMSON_STANDING_SIGN = 505;
    @Since("1.4.0.0-PN") @PowerNukkitOnly int WARPED_STANDING_SIGN = 506;
    @Since("1.4.0.0-PN") @PowerNukkitOnly int CRIMSON_WALL_SIGN = 507;
    @Since("1.4.0.0-PN") @PowerNukkitOnly int WARPED_WALL_SIGN = 508;
    @Since("1.4.0.0-PN") @PowerNukkitOnly int CRIMSON_STAIRS = 509;
    @Since("1.4.0.0-PN") @PowerNukkitOnly int WARPED_STAIRS = 510;
    @Since("1.4.0.0-PN") @PowerNukkitOnly int CRIMSON_FENCE = 511;
    @Since("1.4.0.0-PN") @PowerNukkitOnly int WARPED_FENCE = 512;
    @Since("1.4.0.0-PN") @PowerNukkitOnly int CRIMSON_FENCE_GATE = 513;
    @Since("1.4.0.0-PN") @PowerNukkitOnly int WARPED_FENCE_GATE = 514;
    @Since("1.4.0.0-PN") @PowerNukkitOnly int CRIMSON_BUTTON = 515;
    @Since("1.4.0.0-PN") @PowerNukkitOnly int WARPED_BUTTON = 516;
    @Since("1.4.0.0-PN") @PowerNukkitOnly int CRIMSON_PRESSURE_PLATE = 517;
    @Since("1.4.0.0-PN") @PowerNukkitOnly int WARPED_PRESSURE_PLATE = 518;
    @Since("1.4.0.0-PN") @PowerNukkitOnly int CRIMSON_SLAB = 519;
    @Since("1.4.0.0-PN") @PowerNukkitOnly int WARPED_SLAB = 520;
    @Since("1.4.0.0-PN") @PowerNukkitOnly int CRIMSON_DOUBLE_SLAB = 521;
    @Since("1.4.0.0-PN") @PowerNukkitOnly int WARPED_DOUBLE_SLAB = 522;
    @Since("1.4.0.0-PN") @PowerNukkitOnly int SOUL_TORCH = 523;
    @Since("1.4.0.0-PN") @PowerNukkitOnly int SOUL_LANTERN = 524;
    @Since("1.4.0.0-PN") @PowerNukkitOnly int NETHERITE_BLOCK = 525;
    @Since("1.4.0.0-PN") @PowerNukkitOnly int ANCIENT_DERBRIS = 526;
    @Since("1.4.0.0-PN") @PowerNukkitOnly int RESPAWN_ANCHOR = 527;
    @Since("1.4.0.0-PN") @PowerNukkitOnly int BLACKSTONE = 528;
    @Since("1.4.0.0-PN") @PowerNukkitOnly int POLISHED_BLACKSTONE_BRICKS = 529;
    @Since("1.4.0.0-PN") @PowerNukkitOnly int POLISHED_BLACKSTONE_BRICK_STAIRS = 530;
    @Since("1.4.0.0-PN") @PowerNukkitOnly int BLACKSTONE_STAIRS = 531;
    @Since("1.4.0.0-PN") @PowerNukkitOnly int BLACKSTONE_WALL = 532;
    @Since("1.4.0.0-PN") @PowerNukkitOnly int POLISHED_BLACKSTONE_BRICK_WALL = 533;
    @Since("1.4.0.0-PN") @PowerNukkitOnly int CHISELED_POLISHED_BLACKSTONE = 534;
    @Since("1.4.0.0-PN") @PowerNukkitOnly int CRACKED_POLISHED_BLACKSTONE_BRICKS = 535;
    @Since("1.4.0.0-PN") @PowerNukkitOnly int GILDED_BLACKSTONE = 536;
    @Since("1.4.0.0-PN") @PowerNukkitOnly int BLACKSTONE_SLAB = 537;
    @Since("1.4.0.0-PN") @PowerNukkitOnly int BLACKSTONE_DOUBLE_SLAB = 538;
    @Since("1.4.0.0-PN") @PowerNukkitOnly int POLISHED_BLACKSTONE_BRICK_SLAB = 539;
    @Since("1.4.0.0-PN") @PowerNukkitOnly int POLISHED_BLACKSTONE_BRICK_DOUBLE_SLAB = 540;
    @Since("1.4.0.0-PN") @PowerNukkitOnly int CHAIN_BLOCK = 541;
    @Since("1.4.0.0-PN") @PowerNukkitOnly int TWISTING_VINES = 542;
    @Since("1.4.0.0-PN") @PowerNukkitOnly int NETHER_GOLD_ORE = 543;
    @Since("1.4.0.0-PN") @PowerNukkitOnly int CRYING_OBSIDIAN = 544;
    @Since("1.4.0.0-PN") @PowerNukkitOnly int SOUL_CAMPFIRE_BLOCK = 545;
    @Since("1.4.0.0-PN") @PowerNukkitOnly int POLISHED_BLACKSTONE = 546;
    @Since("1.4.0.0-PN") @PowerNukkitOnly int POLISHED_BLACKSTONE_STAIRS = 547;
    @Since("1.4.0.0-PN") @PowerNukkitOnly int POLISHED_BLACKSTONE_SLAB = 548;
    @Since("1.4.0.0-PN") @PowerNukkitOnly int POLISHED_BLACKSTONE_DOUBLE_SLAB = 549;
    @Since("1.4.0.0-PN") @PowerNukkitOnly int POLISHED_BLACKSTONE_PRESSURE_PLATE = 550;
    @Since("1.4.0.0-PN") @PowerNukkitOnly int POLISHED_BLACKSTONE_BUTTON = 551;
    @Since("1.4.0.0-PN") @PowerNukkitOnly int POLISHED_BLACKSTONE_WALL = 552;
    @Since("1.4.0.0-PN") @PowerNukkitOnly int WARPED_HYPHAE = 553;
    @Since("1.4.0.0-PN") @PowerNukkitOnly int CRIMSON_HYPHAE = 554;
    @Since("1.4.0.0-PN") @PowerNukkitOnly int STRIPPED_CRIMSON_HYPHAE = 555;
    @Since("1.4.0.0-PN") @PowerNukkitOnly int STRIPPED_WARPED_HYPHAE = 556;
    @Since("1.4.0.0-PN") @PowerNukkitOnly int CHISELED_NETHER_BRICKS = 557;
    @Since("1.4.0.0-PN") @PowerNukkitOnly int CRACKED_NETHER_BRICKS = 558;
    @Since("1.4.0.0-PN") @PowerNukkitOnly int QUARTZ_BRICKS = 559;
}
