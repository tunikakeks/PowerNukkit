package cn.nukkit.level.generator.object.tree;

import cn.nukkit.block.Block;
import cn.nukkit.block.BlockWood;
import cn.nukkit.level.ChunkManager;
import cn.nukkit.math.NukkitRandom;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @copied_from MagicDroidX - ObjectOakTree (Nukkit Project)
 */
public class ObjectAzaleaTree extends ObjectTree {
    private int treeHeight = 7;
    private final boolean isFlowered;

    public ObjectAzaleaTree(boolean isFlowered) {
        super();

        this.isFlowered = isFlowered;
    }

    @Override
    public int getTrunkBlock() {
        return Block.LOG;
    }

    @Override
    public int getLeafBlock() {
        return isFlowered ? ThreadLocalRandom.current().nextInt(1,4) == 1 ? Block.FLOWERED_AZALEA_LEAVES : Block.AZALEA_LEAVES : Block.AZALEA_LEAVES;
    }

    @Override
    public int getType() {
        return BlockWood.OAK;
    }

    @Override
    public int getTreeHeight() {
        return this.treeHeight;
    }

    @Override
    public void placeObject(ChunkManager level, int x, int y, int z, NukkitRandom random) {
        this.treeHeight = random.nextBoundedInt(3) + 4;
        super.placeObject(level, x, y, z, random);
    }
}
