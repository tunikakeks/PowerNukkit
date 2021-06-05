package cn.nukkit.block;

import cn.nukkit.item.ItemTool;

/**
 * @copied_from MagicDroidX, BlockOreGold (Nukkit Project)
 */
public class BlockOreDeepslateGold extends BlockOre {

    public BlockOreDeepslateGold() {
    }

    @Override
    public int getId() {
        return DEEPSLATE_GOLD_ORE;
    }

    @Override
    public double getHardness() {
        return 4.5;
    }

    @Override
    public double getResistance() {
        return 15;
    }

    @Override
    public int getToolType() {
        return ItemTool.TYPE_PICKAXE;
    }

    @Override
    public String getName() {
        return "Deepslate Gold Ore";
    }

    @Override
    public int getToolTier() {
        return ItemTool.TIER_IRON;
    }

    @Override
    public boolean canHarvestWithHand() {
        return false;
    }
}
