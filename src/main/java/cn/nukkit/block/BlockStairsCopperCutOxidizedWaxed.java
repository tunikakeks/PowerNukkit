package cn.nukkit.block;

import cn.nukkit.item.ItemTool;

public class BlockStairsCopperCutOxidizedWaxed extends BlockStairs {
    public BlockStairsCopperCutOxidizedWaxed() {
        this(0);
    }

    public BlockStairsCopperCutOxidizedWaxed(int meta) {
        super(meta);
    }

    @Override
    public int getId() {
        return WAXED_OXIDIZED_CUT_COPPER_STAIRS;
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
        return "Waxed Oxidized Cut Copper Stairs";
    }

    @Override
    public boolean canHarvestWithHand() {
        return false;
    }
}
