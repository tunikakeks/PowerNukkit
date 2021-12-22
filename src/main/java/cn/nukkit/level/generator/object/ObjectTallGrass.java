package cn.nukkit.level.generator.object;

import cn.nukkit.block.Block;
import cn.nukkit.block.BlockID;
import cn.nukkit.level.ChunkManager;
import cn.nukkit.level.biome.EnumBiome;
import cn.nukkit.math.NukkitRandom;
import cn.nukkit.math.Vector3;

/**
 * @author ItsLucas (Nukkit Project)
 */
public class ObjectTallGrass {
    private static final Block[][] biomeBlocks = new Block[256][];
    private static final Block[] defaultBlocks = new Block[]{Block.get(BlockID.DANDELION), Block.get(BlockID.RED_FLOWER)};

    static {
        biomeBlocks[EnumBiome.PLAINS.id] = new Block[]{Block.get(BlockID.DANDELION), Block.get(BlockID.RED_FLOWER), Block.get(BlockID.RED_FLOWER, 3), Block.get(BlockID.RED_FLOWER, 4), Block.get(BlockID.RED_FLOWER, 8), Block.get(BlockID.RED_FLOWER, 9)};
        biomeBlocks[EnumBiome.SUNFLOWER_PLAINS.id] = new Block[]{Block.get(BlockID.DANDELION), Block.get(BlockID.RED_FLOWER), Block.get(BlockID.RED_FLOWER, 3), Block.get(BlockID.RED_FLOWER, 4), Block.get(BlockID.RED_FLOWER, 8), Block.get(BlockID.RED_FLOWER, 9)};
        biomeBlocks[EnumBiome.SWAMP.id] = new Block[]{Block.get(BlockID.DANDELION), Block.get(BlockID.RED_FLOWER), Block.get(BlockID.RED_FLOWER, 1)};
        biomeBlocks[EnumBiome.SWAMPLAND_M.id] = new Block[]{Block.get(BlockID.DANDELION), Block.get(BlockID.RED_FLOWER), Block.get(BlockID.RED_FLOWER, 1)};
        biomeBlocks[EnumBiome.FOREST.id] = new Block[]{Block.get(BlockID.DANDELION), Block.get(BlockID.RED_FLOWER)};
        biomeBlocks[EnumBiome.BIRCH_FOREST.id] = new Block[]{Block.get(BlockID.DANDELION), Block.get(BlockID.RED_FLOWER)};
        biomeBlocks[EnumBiome.FOREST_HILLS.id] = new Block[]{Block.get(BlockID.DANDELION), Block.get(BlockID.RED_FLOWER)};
        biomeBlocks[EnumBiome.FLOWER_FOREST.id] = new Block[]{Block.get(BlockID.DANDELION), Block.get(BlockID.RED_FLOWER), Block.get(BlockID.RED_FLOWER, 2), Block.get(BlockID.RED_FLOWER, 3), Block.get(BlockID.RED_FLOWER, 4), Block.get(BlockID.RED_FLOWER, 5), Block.get(BlockID.RED_FLOWER, 6), Block.get(BlockID.RED_FLOWER, 7)};
       // Todo: implement Meadow Biome
        // biomeBlocks[EnumBiome.MEADOW.id] = new Block[]{Block.get(BlockID.DANDELION), Block.get(BlockID.RED_FLOWER), Block.get(BlockID.RED_FLOWER, 2), Block.get(BlockID.RED_FLOWER, 3), Block.get(BlockID.RED_FLOWER, 8), Block.get(BlockID.RED_FLOWER, 9)};


    }

    public static void growGrass(ChunkManager level, Vector3 pos, NukkitRandom random) {
        for (int i = 0; i < 128; ++i) {
            int num = 0;

            int x = pos.getFloorX();
            int y = pos.getFloorY() + 1;
            int z = pos.getFloorZ();

            while (true) {
                if (num >= i / 16) {
                    if (level.getBlockIdAt(x, y, z) == Block.AIR) {
                        if (random.nextBoundedInt(8) == 0) {
                            Block[] blocks = biomeBlocks[level.getChunk(x >> 4, z >> 4).getBiomeId(x & 0xF, z & 0xF)];
                            if (blocks == null) {
                                blocks = defaultBlocks;
                            }
                            level.setBlockStateAt(x, y, z, blocks[random.nextBoundedInt(blocks.length)].getCurrentState());
                        } else {
                            level.setBlockAt(x, y, z, Block.TALL_GRASS, 1);
                        }
                    }

                    break;
                }

                x += random.nextRange(-1, 1);
                y += random.nextRange(-1, 1) * random.nextBoundedInt(3) / 2;
                z += random.nextRange(-1, 1);

                if (level.getBlockIdAt(x, y - 1, z) != Block.GRASS || y > 255 || y < 0) {
                    break;
                }

                ++num;
            }
        }
    }

    public static void growGrass(ChunkManager level, Vector3 pos, NukkitRandom random, int count, int radius) {
        int[][] arr = {
                {Block.DANDELION, 0},
                {Block.RED_FLOWER, 0},
                {Block.TALL_GRASS, 1},
                {Block.TALL_GRASS, 1},
                {Block.TALL_GRASS, 1},
                {Block.TALL_GRASS, 1}
        };
        int arrC = arr.length - 1;
        for (int c = 0; c < count; c++) {
            int x = random.nextRange((int) (pos.x - radius), (int) (pos.x + radius));
            int z = random.nextRange((int) (pos.z) - radius, (int) (pos.z + radius));

            if (level.getBlockIdAt(x, (int) (pos.y + 1), z) == Block.AIR && level.getBlockIdAt(x, (int) (pos.y), z) == Block.GRASS) {
                int[] t = arr[random.nextRange(0, arrC)];
                level.setBlockAt(x, (int) (pos.y + 1), z, t[0], t[1]);
            }
        }
    }
}
