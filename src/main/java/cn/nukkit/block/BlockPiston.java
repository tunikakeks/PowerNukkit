package cn.nukkit.block;

import cn.nukkit.api.PowerNukkitDifference;
import cn.nukkit.api.PowerNukkitOnly;

/**
 * @author CreeperFace
 */
@PowerNukkitDifference(since = "1.4.0.0-PN", info = "Implements BlockEntityHolder only in PowerNukkit")
public class BlockPiston extends BlockPistonBase {

    public BlockPiston() {
        this(0);
    }

    public BlockPiston(int meta) {
        super(meta);
    }

    @Override
    public int getId() {
        return PISTON;
    }

    @Override
    public String getName() {
        return "Piston";
    }

    @PowerNukkitOnly
    @Override
    public int getPistonHeadBlockId() {
        return PISTON_ARM_COLLISION;
    }
}
