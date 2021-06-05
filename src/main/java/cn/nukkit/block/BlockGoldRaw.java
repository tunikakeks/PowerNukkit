package cn.nukkit.block;

import cn.nukkit.item.ItemTool;
import cn.nukkit.utils.BlockColor;

/**
 * @copied_from Angelic47, BlockIron (Nukkit Project)
 */
public class BlockGoldRaw extends BlockSolid {


    public BlockGoldRaw() {
    }

    @Override
    public int getId() {
        return RAW_GOLD_BLOCK;
    }

    @Override
    public String getName() {
        return "Raw Gold Block";
    }

    @Override
    public int getToolType() {
        return ItemTool.TYPE_PICKAXE;
    }

    @Override
    public double getHardness() {
        return 5;
    }

    @Override
    public double getResistance() {
        return 6;
    }

    @Override
    public int getToolTier() {
        return ItemTool.TIER_IRON;
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.GOLD_BLOCK_COLOR;
    }

    @Override
    public boolean canHarvestWithHand() {
        return false;
    }
}
