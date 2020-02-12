package cn.nukkit.block;

import cn.nukkit.inventory.CartographyRecipe;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemTool;
import cn.nukkit.player.Player;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.Identifier;

public class BlockCartographyTable extends BlockSolid {
    public BlockCartographyTable(Identifier id) {
        super(id);
    }

    @Override
    public int getToolType() {
        return ItemTool.TYPE_AXE;
    }

    @Override
    public double getResistance() {
        return 12.5;
    }

    @Override
    public double getHardness() {
        return 2.5;
    }

    @Override
    public int getBurnChance() {
        return 5;
    }

    @Override
    public boolean canHarvestWithHand() {
        return true;
    }

    @Override
    public boolean canBeActivated() {
        return true;
    }

    /* Require Server-Side GUI function */

    @Override
    public boolean onActivate(Item item, Player player) {
        if(player != null) {
            player.addWindow(new CartographyRecipe(player.getUIInventory(), this), Player.CRAFTING_CARTOGRAPHY);
        }
        return true;
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.WOOD_BLOCK_COLOR;
    }
}
