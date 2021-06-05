package cn.nukkit.block;

import cn.nukkit.item.ItemTool;
import cn.nukkit.utils.BlockColor;

/**
 * @copied_from Angelic47, BlockIron (Nukkit Project)
 */
public class BlockIronRaw extends BlockSolid {


    public BlockIronRaw() {
    }

    @Override
    public int getId() {
        return RAW_IRON_BLOCK;
    }

    @Override
    public String getName() {
        return "Raw Iron Block";
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
        return ItemTool.TIER_STONE;
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.IRON_BLOCK_COLOR;
    }

    @Override
    public boolean canHarvestWithHand() {
        return false;
    }
}
