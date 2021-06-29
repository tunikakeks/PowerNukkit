package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.api.PowerNukkitOnly;
import cn.nukkit.api.Since;
import cn.nukkit.blockproperty.BlockProperties;
import cn.nukkit.blockproperty.CommonBlockProperties;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemTool;
import cn.nukkit.math.BlockFace;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockLightningRod extends BlockTransparent {

    @Override
    public String getName() {
        return "Lightning Rod";
    }

    @Override
    public int getId() {
        return BlockID.LIGHTNING_ROD;
    }

    @Nonnull
    @Override
    public BlockProperties getProperties() {
        return new BlockProperties(CommonBlockProperties.FACING_DIRECTION);
    }

    @Override
    public double getHardness() {
        return 3;
    }

    @Override
    public double getResistance() {
        return 6;
    }

    @Override
    public int getToolType() {
        return ItemTool.TYPE_PICKAXE;
    }

    @Override
    public int getToolTier() {
        return ItemTool.TIER_STONE;
    }

    @Override
    public boolean place(@Nonnull Item item, @Nonnull Block block, @Nonnull Block target, @Nonnull BlockFace face, double fx, double fy, double fz, @Nullable Player player) {
        if(target.canBeReplaced()) {
            target = target.down();
            face = BlockFace.UP;
        }
        this.setPropertyValue(CommonBlockProperties.FACING_DIRECTION, face);
        return this.getLevel().setBlock(block, this, true, true);
    }
}
