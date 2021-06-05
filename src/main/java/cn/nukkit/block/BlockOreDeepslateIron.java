package cn.nukkit.block;

import cn.nukkit.item.ItemTool;
import cn.nukkit.math.NukkitRandom;

/**
 * @copied_from MagicDroidX, BlockOreIron (Nukkit Project)
 */
public class BlockOreDeepslateIron extends BlockOre {

    public BlockOreDeepslateIron() {
    }

    @Override
    public int getId() {
        return DEEPSLATE_IRON_ORE;
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
        return "Deepslate Iron Ore";
    }

    @Override
    public int getToolTier() {
        return ItemTool.TIER_STONE;
    }

    @Override
    public int getDropExp() {
        return new NukkitRandom().nextRange(0, 2);
    }

    @Override
    public boolean canHarvestWithHand() {
        return false;
    }

    @Override
    public boolean canSilkTouch() {
        return true;
    }
}
