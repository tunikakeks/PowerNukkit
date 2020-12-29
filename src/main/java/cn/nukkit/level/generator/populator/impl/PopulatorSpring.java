package cn.nukkit.level.generator.populator.impl;

import cn.nukkit.block.Block;
import cn.nukkit.blockstate.BlockState;
import cn.nukkit.level.ChunkManager;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.generator.populator.type.Populator;
import cn.nukkit.math.NukkitMath;
import cn.nukkit.math.NukkitRandom;

import static cn.nukkit.block.BlockID.AIR;
import static cn.nukkit.block.BlockID.STONE;

/**
 * @author GoodLucky777
 */
public class PopulatorSpring extends Populator {

    private final BlockState blockState;
    private final int tries;
    private final int minHeight;
    private final int maxHeight;
    
    public PopulatorSpring(Block block, int tries, int minHeight, int maxHeight) {
        this.blockState = block.getCurrentState();
        this.tries = tries;
        this.minHeight = minHeight;
        this.maxHeight = maxHeight;
    }
    
    @Override
    public void populate(ChunkManager level, int chunkX, int chunkZ, NukkitRandom random, FullChunk chunk) {
        int sourceX = chunkX << 4;
        int sourceZ = chunkZ << 4;
        int endX = sourceX + 15;
        int endZ = sourceZ + 15;
        
        for (int i = 0; i < tries; i++) {
            int x = NukkitMath.randomRange(random, sourceX, endX);
            int z = NukkitMath.randomRange(random, sourceZ, endZ);
            int y = NukkitMath.randomRange(random, minHeight, maxHeight);
            
            int replace = level.getBlockIdAt(x, y, z);
            if (!(replace == AIR || replace == STONE)) {
                continue;
            }
            
            if (level.getBlockIdAt(x, y - 1, z) != STONE || level.getBlockIdAt(x, y + 1, z) != STONE) {
                continue;
            }
            
            int stoneCount = 0;
            if (level.getBlockIdAt(x + 1, y, z) == STONE) stoneCount++;
            if (level.getBlockIdAt(x - 1, y, z) == STONE) stoneCount++;
            if (level.getBlockIdAt(x, y, z + 1) == STONE) stoneCount++;
            if (level.getBlockIdAt(x, y, z - 1) == STONE) stoneCount++;
            
            if (stoneCount != 3) {
                continue;
            }
            
            int airCount = 0;
            if (level.getBlockIdAt(x + 1, y, z) == AIR) airCount++;
            if (level.getBlockIdAt(x - 1, y, z) == AIR) airCount++;
            if (level.getBlockIdAt(x, y, z + 1) == AIR) airCount++;
            if (level.getBlockIdAt(x, y, z - 1) == AIR) airCount++;
            
            if (airCount != 1) {
                continue;
            }
            
            level.setBlockStateAt(x, y, z, blockState);
            // TODO: Tick the block immediately
        }
    }
}
