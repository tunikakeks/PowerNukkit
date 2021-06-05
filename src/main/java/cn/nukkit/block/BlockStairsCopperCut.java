package cn.nukkit.block;

import cn.nukkit.item.ItemTool;

public class BlockStairsCopperCut extends BlockStairs {
    public BlockStairsCopperCut() {
        this(0);
    }

    public BlockStairsCopperCut(int meta) {
        super(meta);
    }

    @Override
    public int getId() {
        return CUT_COPPER_STAIRS;
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
        return "Cut Copper Stairs";
    }

    @Override
    public boolean canHarvestWithHand() {
        return false;
    }
}
