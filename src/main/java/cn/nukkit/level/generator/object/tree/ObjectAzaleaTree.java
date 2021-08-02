package cn.nukkit.level.generator.object.tree;

import cn.nukkit.block.Block;
import cn.nukkit.block.BlockID;
import cn.nukkit.level.ChunkManager;
import cn.nukkit.math.BlockFace;
import cn.nukkit.math.NukkitRandom;

public class ObjectAzaleaTree extends ObjectTree {

    protected BlockFace face;

    protected int treeHeight;

    public ObjectAzaleaTree() {
        this(new NukkitRandom().nextBoundedInt(2) + 4);
    }

    public ObjectAzaleaTree(int height) {
        this.treeHeight = height;
        this.face = new BlockFace[] {BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST}[new NukkitRandom().nextBoundedInt(4)];
    }

    @Override
    public int getTrunkBlock() {
        return BlockID.LOG;
    }

    @Override
    public int getLeafBlock() {
        return BlockID.AZALEA_LEAVES;
    }

    @Override
    public int getTreeHeight() {
        return this.treeHeight;
    }

    @Override
    protected boolean overridable(int id) {
        switch (id) {
            case Block.AIR:
            case Block.SAPLING:
            case Block.LEAVES:
            case Block.SNOW_LAYER:
            case Block.LEAVES2:
                return true;
            default:
                return false;
        }
    }

    @Override
    public void placeObject(ChunkManager level, int x, int y, int z, NukkitRandom random) {
        this.placeTrunk(level, x, y, z, random, this.getTreeHeight());

        int minX = face.getXOffset() == 0 ? -1 : face.getXOffset() * -1;
        int minY = y - 1 + treeHeight;
        int minZ = face.getZOffset() == 0 ? -1 : face.getZOffset() * -1;

        int maxX = face.getXOffset() == 0 ? 1 : face.getXOffset() * 5;
        int maxY = y + 2 + treeHeight;
        int maxZ = face.getZOffset() == 0 ? 1 : face.getZOffset() * 5;
        for(int yy = minY; yy <= maxY; yy++) {
            for(int xx = x + Math.min(minX, maxX); xx <= x + Math.max(minX, maxX); xx++) {
                for(int zz = z + Math.min(minZ, maxZ); zz <= z + Math.max(minZ, maxZ) ; zz++) {
                    if(!overridable(level.getBlockIdAt(xx, yy, zz))) {
                        continue;
                    }

                    if(random.nextBoundedInt(10) < 7) {
                        level.setBlockAt(xx, yy, zz, random.nextBoundedInt(20) == 0 ? BlockID.AZALEA_LEAVES_FLOWERED : getLeafBlock());
                    }
                }
            }
        }
    }

    @Override
    protected void placeTrunk(ChunkManager level, int x, int y, int z, NukkitRandom random, int trunkHeight) {
        level.setBlockAt(x, y - 1, z, BlockID.DIRT_WITH_ROOTS);
        level.setBlockAt(x, y, z, getTrunkBlock());
        for (int yy = 0; yy <= trunkHeight; ++yy) {
            if(yy == trunkHeight) {
                int blockId = level.getBlockIdAt(x + face.getXOffset(), y + yy, z + face.getZOffset());
                if (this.overridable(blockId)) {
                    level.setBlockAt(x + face.getXOffset(), y + yy, z + face.getZOffset(), this.getTrunkBlock());
                }
            } else {
                int blockId = level.getBlockIdAt(x, y + yy, z);
                if (this.overridable(blockId)) {
                    level.setBlockAt(x, y + yy, z, this.getTrunkBlock());
                }
            }
        }
    }
}
