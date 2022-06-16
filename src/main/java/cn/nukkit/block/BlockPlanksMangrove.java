package cn.nukkit.block;

import cn.nukkit.item.Item;
import cn.nukkit.item.ItemTool;
import cn.nukkit.item.MinecraftItemID;
import cn.nukkit.utils.BlockColor;


public class BlockPlanksMangrove extends BlockSolid {

    @Override
    public int getId() {
        return MANGROVE_PLANKS;
    }

    @Override
    public double getHardness() {
        return 2;
    }

    @Override
    public double getResistance() {
        return 15;
    }

    @Override
    public int getBurnChance() {
        return 5;
    }

    @Override
    public int getBurnAbility() {
        return 20;
    }

    @Override
    public String getName() {
        return "Mangrove Planks";
    }

    @Override
    public int getToolType() {
        return ItemTool.TYPE_AXE;
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.PURPLE_TERRACOTA_BLOCK_COLOR;
    }

    @Override
    public Item toItem() {
        return MinecraftItemID.MANGROVE_PLANKS.get(1);
    }
}
