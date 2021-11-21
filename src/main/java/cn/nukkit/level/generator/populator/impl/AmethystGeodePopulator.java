package cn.nukkit.level.generator.populator.impl;

import cn.nukkit.block.Block;
import cn.nukkit.block.BlockAmethystCluster;
import cn.nukkit.block.BlockID;
import cn.nukkit.blockproperty.CommonBlockProperties;
import cn.nukkit.level.ChunkManager;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.generator.populator.type.Populator;
import cn.nukkit.math.BlockFace;
import cn.nukkit.math.NukkitRandom;

public class AmethystGeodePopulator extends Populator {

    @Override
    public void populate(ChunkManager level, int chunkX, int chunkZ, NukkitRandom random, FullChunk chunk) {
        if(random.nextRange(1, 106) == 1) {
            int height = random.nextRange(0, 50);
            makeCircle(chunk, BlockID.SMOOTH_BASALT, height, 7, false);
            makeCircle(chunk, BlockID.CALCITE, height, 6, false);
            makeCircle(chunk, BlockID.AMETHYST_BLOCK, height, 5, false);
            makeCircle(chunk, BlockID.AIR, height, 4, true);
            generateCluster(chunk, random, height, 4);
        }
    }

    public void makeCircle(FullChunk chunk, int blockId, int height, int size, boolean filled) {
        final double invRadiusX = 1 / (double) size;
        final double invRadiusY = 1 / (double) size;
        final double invRadiusZ = 1 / (double) size;

        int px = 8;
        int pz = 8;

        final int ceilRadiusX = (int) Math.ceil(size);
        final int ceilRadiusY = (int) Math.ceil(size);
        final int ceilRadiusZ = (int) Math.ceil(size);

        int yy; double nextXn = 0;

        forX: for (int x = 0; x <= ceilRadiusX; ++x) {
            final double xn = nextXn;
            double dx = xn * xn; nextXn = (x + 1) * invRadiusX;
            double nextZn = 0;
            forZ: for (int z = 0; z <= ceilRadiusZ; ++z) {
                final double zn = nextZn; double dz = zn * zn;
                double dxz = dx + dz; nextZn = (z + 1) * invRadiusZ;
                double nextYn = 0;
                for (int y = 0; y <= ceilRadiusY; ++y) {
                    final double yn = nextYn;
                    double dy = yn * yn;
                    double dxyz = dxz + dy;
                    nextYn = (y + 1) * invRadiusY;
                    if (dxyz > 1) {
                        if (y == 0) {
                            if (z == 0) {
                                break forX;
                            }
                            break forZ;
                        }
                        break;
                    }
                    if (!filled) {
                        if (nextXn * nextXn + dy + dz <= 1 && nextYn * nextYn + dx + dz <= 1 && nextZn * nextZn + dx + dy <= 1) {
                            continue;
                        }
                    }
                    yy = height + y;
                    if (yy <= 256) {
                        setBlockId(chunk, px + x, height + y, pz + z, blockId);
                        if (x != 0) {
                            setBlockId(chunk, px - x, height + y, pz + z, blockId);
                        }
                        if (z != 0) {
                            setBlockId(chunk, px + x, height + y, pz - z, blockId);
                            if (x != 0) {
                                setBlockId(chunk, px - x, height + y, pz - z, blockId);
                            }
                        }
                    }
                    if (y != 0 && (yy = height - y) >= 0) {
                        setBlockId(chunk, px + x, yy, pz + z, blockId);
                        if (x != 0) {
                            setBlockId(chunk, px - x, yy, pz + z, blockId);
                        }
                        if (z != 0) {
                            setBlockId(chunk, px + x, yy, pz - z, blockId);
                            if (x != 0) {
                                setBlockId(chunk, px - x, yy, pz - z, blockId);
                            }
                        }
                    }
                }
            }
        }
    }

    public void generateCluster(FullChunk chunk, NukkitRandom random, int height, int size) {
        final double invRadiusX = 1 / (double) size;
        final double invRadiusY = 1 / (double) size;
        final double invRadiusZ = 1 / (double) size;

        int px = 8;
        int pz = 8;

        final int ceilRadiusX = (int) Math.ceil(size);
        final int ceilRadiusY = (int) Math.ceil(size);
        final int ceilRadiusZ = (int) Math.ceil(size);

        int yy; double nextXn = 0;

        forX: for (int x = 0; x <= ceilRadiusX; ++x) {
            final double xn = nextXn;
            double dx = xn * xn; nextXn = (x + 1) * invRadiusX;
            double nextZn = 0;
            forZ: for (int z = 0; z <= ceilRadiusZ; ++z) {
                final double zn = nextZn; double dz = zn * zn;
                double dxz = dx + dz; nextZn = (z + 1) * invRadiusZ;
                double nextYn = 0;
                for (int y = 0; y <= ceilRadiusY; ++y) {
                    final double yn = nextYn;
                    double dy = yn * yn;
                    double dxyz = dxz + dy;
                    nextYn = (y + 1) * invRadiusY;
                    if (dxyz > 1) {
                        if (y == 0) {
                            if (z == 0) {
                                break forX;
                            }
                            break forZ;
                        }
                        break;
                    }
                    if (nextXn * nextXn + dy + dz <= 1 && nextYn * nextYn + dx + dz <= 1 && nextZn * nextZn + dx + dy <= 1) {
                        continue;
                    }
                    yy = height + y;
                    if (yy <= 256) {
                        placeCluster(chunk, px + x, height + y, pz + z, random);
                        if (x != 0) {
                            placeCluster(chunk, px - x, height + y, pz + z, random);
                        }
                        if (z != 0) {
                            placeCluster(chunk, +x, height + y, pz - z, random);
                            if (x != 0) {
                                placeCluster(chunk, px - x, height + y, pz - z, random);
                            }
                        }
                    }
                    if (y != 0 && (yy = height - y) >= 0) {
                        placeCluster(chunk, px + x, yy, pz + z, random);
                        if (x != 0) {
                            placeCluster(chunk, px - x, yy, pz + z, random);
                        }
                        if (z != 0) {
                            placeCluster(chunk, px + x, yy, pz - z, random);
                            if (x != 0) {
                                placeCluster(chunk, px - x, yy, pz - z, random);
                            }
                        }
                    }
                }
            }
        }
    }

    private void placeCluster(FullChunk chunk, int x, int y, int z, NukkitRandom random) {
        if(x == 0 || x == 15 || y == 0 || y == 255 || z == 0 || z == 15 || chunk.getBlockId(x, y, z) != 0) {
            return;
        }
        if(random.nextBoundedInt(20) == 0) {
            Block block = new BlockAmethystCluster();
            if(chunk.getBlockId(x + 1, y, z) != 0) {
                block.setPropertyValue(CommonBlockProperties.FACING_DIRECTION, BlockFace.WEST);
            } else if(chunk.getBlockId(x - 1, y, z) != 0) {
                block.setPropertyValue(CommonBlockProperties.FACING_DIRECTION, BlockFace.EAST);
            } else if(chunk.getBlockId(x, y, z + 1) != 0) {
                block.setPropertyValue(CommonBlockProperties.FACING_DIRECTION, BlockFace.NORTH);
            } else if(chunk.getBlockId(x, y, z - 1) != 0) {
                block.setPropertyValue(CommonBlockProperties.FACING_DIRECTION, BlockFace.SOUTH);
            } else if(chunk.getBlockId(x, y + 1, z) != 0) {
                block.setPropertyValue(CommonBlockProperties.FACING_DIRECTION, BlockFace.DOWN);
            } else if(chunk.getBlockId(x, y - 1, z) != 0) {
                block.setPropertyValue(CommonBlockProperties.FACING_DIRECTION, BlockFace.UP);
            }
            chunk.setBlockState(x, y, z, block.getCurrentState());
        }
    }

    private void setBlockId(FullChunk chunk, int x, int y, int z, int id) {
        if (chunk.getBlockId(x, y, z) != BEDROCK) {
            chunk.setBlockId(x, y, z, id);
        }
    }
}
