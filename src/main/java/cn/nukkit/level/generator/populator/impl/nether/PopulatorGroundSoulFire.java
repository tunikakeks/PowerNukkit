package cn.nukkit.level.generator.populator.impl.nether;

import cn.nukkit.block.Block;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.generator.populator.impl.PopulatorGroundFire;
import cn.nukkit.math.NukkitRandom;

public class PopulatorGroundSoulFire extends PopulatorGroundFire {
    @Override
    protected int getBlockId(int x, int z, NukkitRandom random, FullChunk chunk) {
        return SOUL_FIRE << Block.DATA_BITS;
    }

    @Override
    protected int belowBlock() {
        return SOUL_SAND;
    }
}
