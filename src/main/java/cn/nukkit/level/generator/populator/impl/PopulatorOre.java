package cn.nukkit.level.generator.populator.impl;

import cn.nukkit.api.PowerNukkitOnly;
import cn.nukkit.api.Since;
import cn.nukkit.block.Block;
import cn.nukkit.level.ChunkManager;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.generator.object.ore.OreType;
import cn.nukkit.level.generator.populator.type.Populator;
import cn.nukkit.math.NukkitMath;
import cn.nukkit.math.NukkitRandom;

/**
 * @author DaPorkchop_
 */
public class PopulatorOre extends Populator {
    private final int replaceId;
    private OreType[] oreTypes = OreType.EMPTY_ARRAY;

    /**
     * @implNote Removed from the new-raknet branch
     */
    @Since("1.4.0.0-PN")
    @PowerNukkitOnly
    public PopulatorOre() {
        this(Block.STONE);
    }

    /**
     * @implNote Removed from the new-raknet branch
     */
    @Since("1.4.0.0-PN")
    @PowerNukkitOnly
    public PopulatorOre(int id) {
        this.replaceId = id;
    }

    public PopulatorOre(int replaceId, OreType[] oreTypes) {
        this.replaceId = replaceId;
        this.oreTypes = oreTypes;
    }

    @Override
    public void populate(ChunkManager level, int chunkX, int chunkZ, NukkitRandom random, FullChunk chunk) {
        int sx = chunkX << 4;
        int ex = sx + 15;
        int sz = chunkZ << 4;
        int ez = sz + 15;
        for (OreType type : this.oreTypes) {
            for (int i = 0; i < type.clusterCount; i++) {
                int x = NukkitMath.randomRange(random, sx, ex);
                int z = NukkitMath.randomRange(random, sz, ez);
                int y = NukkitMath.randomRange(random, type.minHeight, type.maxHeight);
                if (level.getBlockIdAt(x, y, z) != replaceId) {
                    continue;
                }
                type.spawn(level, random, replaceId, x, y, z);
            }
        }
    }

    /**
     * @implNote Removed from the new-raknet branch
     */
    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    public void setOreTypes(OreType[] oreTypes) {
        this.oreTypes = oreTypes;
    }
}
