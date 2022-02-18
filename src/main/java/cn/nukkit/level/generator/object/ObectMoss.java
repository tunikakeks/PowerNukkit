package cn.nukkit.level.generator.object;

import cn.nukkit.block.Block;
import cn.nukkit.level.ChunkManager;
import cn.nukkit.math.NukkitRandom;
import cn.nukkit.math.Vector3;

public class ObectMoss {

    public static void growMoss(ChunkManager level, Vector3 pos, NukkitRandom random) {
        for (int i = 0; i < 128; ++i) {
            int num = 0;

            int x = pos.getFloorX();
            int y = pos.getFloorY() + 1;
            int z = pos.getFloorZ();

            while (true) {
                if (num >= i / 16) {
                    int blockId = level.getBlockIdAt(x, y, z);
                    if (blockId == Block.AIR || blockId == Block.TALL_GRASS) {
                        if (random.nextBoundedInt(5) == 0) {
                            level.setBlockAt(x, y, z, random.nextBoolean() ? Block.AZALEA : Block.FLOWERING_AZALEA);
                            level.setBlockAt(x, y - 1, z, Block.MOSS_BLOCK);
                        } else {
                            if(random.nextBoundedInt(3) == 0) {
                                level.setBlockAt(x, y, z, Block.MOSS_CARPET);
                            }
                        }
                    } else {
                        level.setBlockAt(x, y - 1, z, Block.MOSS_BLOCK);
                    }

                    break;
                }

                x += random.nextRange(-1, 1);
                y += random.nextRange(-1, 1) * random.nextBoundedInt(3) / 2;
                z += random.nextRange(-1, 1);

                int blockId = level.getBlockIdAt(x, y - 1, z);
                if ((blockId != Block.GRASS && blockId != Block.STONE) || y > 255 || y < 0) {
                    break;
                }

                ++num;
            }
        }
    }
}
