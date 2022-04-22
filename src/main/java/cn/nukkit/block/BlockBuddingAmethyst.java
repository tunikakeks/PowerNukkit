package cn.nukkit.block;

import cn.nukkit.blockproperty.CommonBlockProperties;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemTool;
import cn.nukkit.level.Level;
import cn.nukkit.math.BlockFace;

import java.util.concurrent.ThreadLocalRandom;

public class BlockBuddingAmethyst extends BlockSolid {

    @Override
    public String getName() {
        return "Budding Amethyst";
    }

    @Override
    public int getId() {
        return BUDDING_AMETHYST;
    }

    @Override
    public double getHardness() {
        return 1.5;
    }

    @Override
    public double getResistance() {
        return 1.5;
    }

    @Override
    public int getToolType() {
        return ItemTool.TYPE_PICKAXE;
    }

    @Override
    public boolean canHarvestWithHand() {
        return false;
    }

    @Override
    public boolean breaksWhenMoved() {
        return true;
    }

    @Override
    public boolean sticksToPiston() {
        return false;
    }

    @Override
    public Item[] getDrops(Item item) {
        return Item.EMPTY_ARRAY;
    }

    @Override
    public int onUpdate(int type) {
        if (type == Level.BLOCK_UPDATE_RANDOM) {
            if (ThreadLocalRandom.current().nextInt(5) == 0) {
                for (BlockFace face : BlockFace.values()) {
                    Block side = this.getSide(face);
                    if (side instanceof BlockAir || side instanceof BlockWater) {
                        BlockAmethystBudSmall bud = new BlockAmethystBudSmall();
                        bud.setPropertyValue(CommonBlockProperties.FACING_DIRECTION, face);
                        this.getLevel().setBlock(side, bud, true, true);
                        if (side instanceof BlockWater && ((BlockWater) side).isSource()) {
                            this.getLevel().setBlock(side, 1, side, false, false);
                            this.getLevel().scheduleUpdate(side, 1);
                        }
                        break;
                    }
                }
            }
            return type;
        }
        return 0;
    }
}
