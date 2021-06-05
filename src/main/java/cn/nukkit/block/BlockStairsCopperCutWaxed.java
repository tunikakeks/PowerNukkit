package cn.nukkit.block;

import cn.nukkit.item.ItemTool;

public class BlockStairsCopperCutWaxed extends BlockStairs {
    public BlockStairsCopperCutWaxed() {
        this(0);
    }

    public BlockStairsCopperCutWaxed(int meta) {
        super(meta);
    }

    @Override
    public int getId() {
        return WAXED_CUT_COPPER_STAIRS;
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
    public String getName() {
        return "Waxed Cut Copper Stairs";
    }

    @Override
    public boolean canHarvestWithHand() {
        return false;
    }
}
