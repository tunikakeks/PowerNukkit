package cn.nukkit.level.generator.populator.impl.nether;

import cn.nukkit.level.ChunkManager;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.generator.populator.type.Populator;
import cn.nukkit.math.NukkitMath;
import cn.nukkit.math.NukkitRandom;

public class PopulatorRuinedPortal extends Populator {
    @Override
    public void populate(ChunkManager level, int chunkX, int chunkZ, NukkitRandom random, FullChunk chunk) {
        if (random.nextBoundedInt(501) == 0) {
            int x = NukkitMath.randomRange(random, chunkX << 4, (chunkX << 4) + 15);
            int z = NukkitMath.randomRange(random, chunkZ << 4, (chunkZ << 4) + 15);
            int y = this.getHighestWorkableBlock(level, x, z, chunk);
            if (y <= 32 || y > 100) {
                return;
            }
            y += 3;
            if (random.nextBoundedInt(20) == 0) {
                // TODO: Giant portal
                switch (random.nextBoundedInt(4)) {
                    case 0:
                        // Obsidian
                        this.placeBlock(x, y, z, OBSIDIAN, chunk);
                        this.placeBlock(x, y, z - 1, OBSIDIAN, chunk);
                        this.placeBlock(x, y, z - 2, OBSIDIAN, chunk);
                        this.placeBlock(x, y, z - 3, OBSIDIAN, chunk);
                        this.placeBlock(x, y, z - 4, OBSIDIAN, chunk);
                        this.placeBlock(x, y, z - 5, OBSIDIAN, chunk);
                        this.placeBlock(x, y, z - 6, OBSIDIAN, chunk);
                        this.placeBlock(x, y, z - 7, OBSIDIAN, chunk);
                        this.placeBlock(x, y + 1, z, OBSIDIAN, chunk);
                        this.placeBlock(x, y + 5, z, OBSIDIAN, chunk);
                        this.placeBlock(x, y + 6, z, OBSIDIAN, chunk);
                        this.placeBlock(x, y + 7, z, OBSIDIAN, chunk);
                        this.placeBlock(x, y + 8, z, OBSIDIAN, chunk);
                        this.placeBlock(x, y + 9, z, OBSIDIAN, chunk);
                        this.placeBlock(x, y + 1, z - 7, OBSIDIAN, chunk);
                        this.placeBlock(x, y + 2, z - 7, OBSIDIAN, chunk);
                        this.placeBlock(x, y + 3, z - 7, OBSIDIAN, chunk);
                        this.placeBlock(x, y + 4, z - 7, OBSIDIAN, chunk);
                        this.placeBlock(x, y + 5, z - 7, OBSIDIAN, chunk);
                        this.placeBlock(x, y + 6, z - 7, OBSIDIAN, chunk);
                        this.placeBlock(x, y + 9, z - 1, OBSIDIAN, chunk);
                        this.placeBlock(x, y + 9, z - 2, OBSIDIAN, chunk);
                        this.placeBlock(x, y + 9, z - 4, OBSIDIAN, chunk);
                        this.placeBlock(x - 3, y - 2, z - 9, OBSIDIAN, chunk);
                        this.placeBlock(x - 4, y - 1, z - 9, OBSIDIAN, chunk);
                        this.placeBlock(x - 4, y - 2, z - 9, OBSIDIAN, chunk);
                        this.placeBlock(x - 1, y, z + 1, OBSIDIAN, chunk);
                        this.placeBlock(x - 1, y - 1, z + 1, OBSIDIAN, chunk);

                        // Blackstone
                        this.placeBlock(x, y, z + 1, POLISHED_BLACKSTONE_BRICKS, chunk);
                        this.placeBlock(x, y - 1, z + 1, POLISHED_BLACKSTONE_BRICKS, chunk);
                        this.placeBlock(x, y - 1, z + 2, POLISHED_BLACKSTONE_BRICKS, chunk);
                        this.placeBlock(x, y, z + 2, POLISHED_BLACKSTONE_BRICKS, chunk);
                        this.placeBlock(x, y + 1, z + 2, POLISHED_BLACKSTONE_BRICKS, chunk);
                        this.placeBlock(x, y + 2, z + 2, POLISHED_BLACKSTONE_BRICKS, chunk);
                        this.placeBlock(x, y + 3, z + 2, POLISHED_BLACKSTONE_BRICKS, chunk);
                        this.placeBlock(x, y + 4, z + 2, POLISHED_BLACKSTONE_BRICKS, chunk);
                        this.placeBlock(x, y + 5, z + 2, POLISHED_BLACKSTONE_BRICKS, chunk);
                        this.placeBlock(x, y + 6, z + 2, POLISHED_BLACKSTONE_BRICKS, chunk);
                        this.placeBlock(x, y + 7, z + 2, POLISHED_BLACKSTONE_BRICKS, chunk);
                        this.placeBlock(x, y + 9, z + 2, POLISHED_BLACKSTONE_BRICKS, chunk);
                        this.placeBlock(x, y + 9, z + 1, POLISHED_BLACKSTONE_BRICKS, chunk);
                        this.placeBlock(x, y + 10, z + 2, POLISHED_BLACKSTONE_BRICKS, chunk);
                        this.placeBlock(x, y + 11, z + 1, POLISHED_BLACKSTONE_BRICKS, chunk);
                        this.placeBlock(x, y + 11, z - 1, POLISHED_BLACKSTONE_BRICKS, chunk);
                        this.placeBlock(x, y + 12, z - 1, POLISHED_BLACKSTONE_BRICKS, chunk);
                        this.placeBlock(x, y + 12, z - 2, POLISHED_BLACKSTONE_BRICKS, chunk);
                        this.placeBlock(x, y + 11, z - 3, POLISHED_BLACKSTONE_BRICKS, chunk);
                        this.placeBlock(x, y + 11, z - 4, POLISHED_BLACKSTONE_BRICKS, chunk);
                        this.placeBlock(x, y, z - 8, POLISHED_BLACKSTONE_BRICKS, chunk);
                        this.placeBlock(x, y - 1, z - 8, POLISHED_BLACKSTONE_BRICKS, chunk);
                        this.placeBlock(x, y - 1, z - 9, POLISHED_BLACKSTONE_BRICKS, chunk);
                        this.placeBlock(x, y, z - 9, POLISHED_BLACKSTONE_BRICKS, chunk);
                        this.placeBlock(x, y + 1, z - 9, POLISHED_BLACKSTONE_BRICKS, chunk);
                        this.placeBlock(x, y + 2, z - 9, POLISHED_BLACKSTONE_BRICKS, chunk);
                        this.placeBlock(x, y + 3, z - 9, POLISHED_BLACKSTONE_BRICKS, chunk);
                        this.placeBlock(x, y + 4, z - 9, POLISHED_BLACKSTONE_BRICKS, chunk);
                        this.placeBlock(x, y + 5, z - 9, POLISHED_BLACKSTONE_BRICKS, chunk);
                        this.placeBlock(x, y + 7, z - 9, POLISHED_BLACKSTONE_BRICKS, chunk);
                        this.placeBlock(x, y + 8, z - 9, POLISHED_BLACKSTONE_BRICKS, chunk);
                        this.placeBlock(x - 2, y - 2, z - 9, POLISHED_BLACKSTONE_BRICKS, chunk);
                        this.placeBlock(x + 1, y - 1, z + 1, POLISHED_BLACKSTONE_BRICKS, chunk);
                        this.placeBlock(x + 1, y - 1, z, POLISHED_BLACKSTONE_BRICKS, chunk);
                        this.placeBlock(x + 1, y - 1, z - 1, POLISHED_BLACKSTONE_BRICKS, chunk);
                        this.placeBlock(x + 1, y - 1, z - 2, POLISHED_BLACKSTONE_BRICKS, chunk);
                        this.placeBlock(x + 1, y - 1, z - 3, POLISHED_BLACKSTONE_BRICKS, chunk);
                        this.placeBlock(x + 1, y - 1, z - 4, POLISHED_BLACKSTONE_BRICKS, chunk);
                        this.placeBlock(x + 1, y - 1, z - 5, POLISHED_BLACKSTONE_BRICKS, chunk);
                        this.placeBlock(x + 1, y - 1, z - 6, POLISHED_BLACKSTONE_BRICKS, chunk);
                        this.placeBlock(x + 1, y - 1, z - 7, POLISHED_BLACKSTONE_BRICKS, chunk);
                        this.placeBlock(x + 1, y - 1, z - 8, POLISHED_BLACKSTONE_BRICKS, chunk);
                        this.placeBlock(x - 1, y - 1, z - 8, POLISHED_BLACKSTONE_BRICKS, chunk);
                        this.placeBlock(x - 1, y - 1, z + 1, POLISHED_BLACKSTONE_BRICKS, chunk);
                        this.placeBlock(x - 1, y - 1, z, POLISHED_BLACKSTONE_BRICKS, chunk);
                        this.placeBlock(x - 1, y - 1, z - 1, POLISHED_BLACKSTONE_BRICKS, chunk);
                        this.placeBlock(x - 1, y - 1, z - 2, POLISHED_BLACKSTONE_BRICKS, chunk);
                        this.placeBlock(x - 1, y - 1, z - 3, POLISHED_BLACKSTONE_BRICKS, chunk);
                        this.placeBlock(x - 1, y - 1, z - 4, POLISHED_BLACKSTONE_BRICKS, chunk);
                        this.placeBlock(x - 1, y - 1, z - 5, POLISHED_BLACKSTONE_BRICKS, chunk);
                        this.placeBlock(x - 1, y - 1, z - 6, POLISHED_BLACKSTONE_BRICKS, chunk);
                        this.placeBlock(x - 1, y - 1, z - 7, POLISHED_BLACKSTONE_BRICKS, chunk);
                        this.placeBlock(x - 1, y - 1, z - 8, POLISHED_BLACKSTONE_BRICKS, chunk);
                        this.placeBlock(x - 3, y - 1, z - 1, POLISHED_BLACKSTONE_BRICKS, chunk);
                        this.placeBlock(x - 3, y - 1, z - 2, POLISHED_BLACKSTONE_BRICKS, chunk);
                        this.placeBlock(x - 3, y - 1, z - 3, POLISHED_BLACKSTONE_BRICKS, chunk);
                        this.placeBlock(x - 3, y - 1, z - 4, POLISHED_BLACKSTONE_BRICKS, chunk);
                        this.placeBlock(x - 3, y - 1, z - 5, POLISHED_BLACKSTONE_BRICKS, chunk);
                        this.placeBlock(x - 3, y - 1, z - 6, POLISHED_BLACKSTONE_BRICKS, chunk);
                        this.placeBlock(x, y, z + 1, CHISELED_POLISHED_BLACKSTONE, chunk);
                        this.placeBlock(x, y + 6, z + 1, CHISELED_POLISHED_BLACKSTONE, chunk);
                        this.placeBlock(x, y + 3, z - 8, CHISELED_POLISHED_BLACKSTONE, chunk);
                        this.placeBlock(x, y + 6, z - 8, CHISELED_POLISHED_BLACKSTONE, chunk);
                        this.placeBlock(x, y + 9, z - 8, CHISELED_POLISHED_BLACKSTONE, chunk);
                        this.placeBlock(x + 1, y, z, POLISHED_BLACKSTONE_BRICK_SLAB, chunk);
                        this.placeBlock(x + 1, y, z - 1, POLISHED_BLACKSTONE_BRICK_SLAB, chunk);
                        this.placeBlock(x + 1, y, z - 2, POLISHED_BLACKSTONE_BRICK_SLAB, chunk);
                        this.placeBlock(x + 1, y, z - 3, POLISHED_BLACKSTONE_BRICK_SLAB, chunk);
                        this.placeBlock(x + 1, y, z - 4, POLISHED_BLACKSTONE_BRICK_SLAB, chunk);
                        this.placeBlock(x + 1, y, z - 5, POLISHED_BLACKSTONE_BRICK_SLAB, chunk);
                        this.placeBlock(x + 1, y, z - 6, POLISHED_BLACKSTONE_BRICK_SLAB, chunk);
                        this.placeBlock(x + 1, y, z - 7, POLISHED_BLACKSTONE_BRICK_SLAB, chunk);
                        this.placeBlock(x - 1, y, z + 1, POLISHED_BLACKSTONE_BRICK_SLAB, chunk);
                        this.placeBlock(x - 1, y, z, POLISHED_BLACKSTONE_BRICK_SLAB, chunk);
                        this.placeBlock(x - 1, y, z - 1, POLISHED_BLACKSTONE_BRICK_SLAB, chunk);
                        this.placeBlock(x - 1, y, z - 2, POLISHED_BLACKSTONE_BRICK_SLAB, chunk);
                        this.placeBlock(x - 1, y, z - 3, POLISHED_BLACKSTONE_BRICK_SLAB, chunk);
                        this.placeBlock(x - 1, y, z - 5, POLISHED_BLACKSTONE_BRICK_SLAB, chunk);
                        this.placeBlock(x - 1, y, z - 6, POLISHED_BLACKSTONE_BRICK_SLAB, chunk);
                        this.placeBlock(x - 1, y, z - 7, POLISHED_BLACKSTONE_BRICK_SLAB, chunk);
                        this.placeBlock(x - 1, y, z - 8, POLISHED_BLACKSTONE_BRICK_SLAB, chunk);
                        this.placeBlock(x + 1, y - 1, z + 2, POLISHED_BLACKSTONE_BRICK_SLAB, chunk);
                        this.placeBlock(x + 2, y - 1, z + 1, POLISHED_BLACKSTONE_BRICK_SLAB, chunk);
                        this.placeBlock(x + 2, y - 1, z, POLISHED_BLACKSTONE_BRICK_SLAB, chunk);
                        this.placeBlock(x + 3, y - 1, z - 1, POLISHED_BLACKSTONE_BRICK_SLAB, chunk);
                        this.placeBlock(x + 3, y - 1, z - 2, POLISHED_BLACKSTONE_BRICK_SLAB, chunk);
                        this.placeBlock(x + 3, y - 1, z - 4, POLISHED_BLACKSTONE_BRICK_SLAB, chunk);
                        this.placeBlock(x + 3, y - 1, z - 5, POLISHED_BLACKSTONE_BRICK_SLAB, chunk);
                        this.placeBlock(x + 3, y - 1, z - 8, POLISHED_BLACKSTONE_BRICK_SLAB, chunk);
                        this.placeBlock(x + 2, y - 1, z - 8, POLISHED_BLACKSTONE_BRICK_SLAB, chunk);
                        this.placeBlock(x + 2, y - 1, z - 9, POLISHED_BLACKSTONE_BRICK_SLAB, chunk);
                        this.placeBlock(x + 1, y - 1, z - 9, POLISHED_BLACKSTONE_BRICK_SLAB, chunk);
                        this.placeBlock(x - 1, y - 1, z - 9, POLISHED_BLACKSTONE_BRICK_SLAB, chunk);
                        this.placeBlock(x - 1, y + 1, z - 8, POLISHED_BLACKSTONE_BRICK_SLAB, chunk);

                        // Chains
                        this.placeBlock(x, y + 1, z + 1, CHAIN_BLOCK, chunk);
                        this.placeBlock(x, y + 4, z + 1, CHAIN_BLOCK, chunk);
                        this.placeBlock(x, y + 5, z + 1, CHAIN_BLOCK, chunk);
                        this.placeBlock(x, y + 7, z + 1, CHAIN_BLOCK, chunk);
                        this.placeBlock(x, y + 8, z + 1, CHAIN_BLOCK, chunk);
                        this.placeBlock(x, y + 10, z + 1, CHAIN_BLOCK, chunk);
                        this.placeBlock(x, y + 10, z, CHAIN_BLOCK, chunk);
                        this.placeBlock(x, y + 10, z - 2, CHAIN_BLOCK, chunk);
                        this.placeBlock(x, y + 11, z - 2, CHAIN_BLOCK, chunk);
                        this.placeBlock(x, y + 10, z - 3, CHAIN_BLOCK, chunk);
                        this.placeBlock(x, y + 10, z - 4, CHAIN_BLOCK, chunk);
                        this.placeBlock(x, y + 1, z - 8, CHAIN_BLOCK, chunk);
                        this.placeBlock(x, y + 2, z - 8, CHAIN_BLOCK, chunk);
                        this.placeBlock(x, y + 4, z - 8, CHAIN_BLOCK, chunk);
                        this.placeBlock(x, y + 5, z - 8, CHAIN_BLOCK, chunk);
                        this.placeBlock(x, y + 7, z - 8, CHAIN_BLOCK, chunk);
                        this.placeBlock(x, y + 8, z - 8, CHAIN_BLOCK, chunk);
                        this.placeBlock(x, y + 10, z - 8, CHAIN_BLOCK, chunk);

                        // Gold
                        this.placeBlock(x, y + 12, z - 3, GOLD_BLOCK, chunk);
                        this.placeBlock(x, y + 12, z - 4, GOLD_BLOCK, chunk);
                        break;
                    case 1:
                        break;
                    case 3:
                        break;
                }
            } else {
                switch (random.nextBoundedInt(10)) {
                    case 0:
                        break;
                    case 1:
                        break;
                    case 3:
                        break;
                    case 4:
                        break;
                    case 5:
                        break;
                    case 6:
                        break;
                    case 7:
                        break;
                    case 8:
                        break;
                    case 9:
                        break;
                }
            }
        }
    }

    @Override
    protected int getHighestWorkableBlock(ChunkManager level, int x, int z, FullChunk chunk) {
        int y;
        for (y = 0; y <= 127; ++y) {
            int b = chunk.getBlockId(x, y, z);
            if (b == AIR) {
                break;
            }
        }
        return y == 0 ? -1 : y;
    }

    private void placeBlock(int x, int y, int z, int id, FullChunk chunk) {
        this.placeBlock(x, y, z, id, 0, chunk);
    }

    private void placeBlock(int x, int y, int z, int id, int meta, FullChunk chunk) {
        chunk.setBlock(x, y, z, id, meta);
    }
}
