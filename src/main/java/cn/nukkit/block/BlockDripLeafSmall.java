package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.blockproperty.BlockProperties;
import cn.nukkit.blockproperty.CommonBlockProperties;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemTool;
import cn.nukkit.level.Level;
import cn.nukkit.level.particle.BoneMealParticle;
import cn.nukkit.math.BlockFace;
import cn.nukkit.utils.BlockColor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.concurrent.ThreadLocalRandom;

public class BlockDripLeafSmall extends BlockTransparent {

    public static final BlockProperties PROPERTIES = new BlockProperties(CommonBlockProperties.DIRECTION, CommonBlockProperties.UPPER_BLOCK);

    @Override
    public String getName() {
        return "Small Dripleaf";
    }

    @Override
    public int getId() {
        return SMALL_DRIPLEAF_BLOCK;
    }

    @Nonnull
    @Override
    public BlockProperties getProperties() {
        return PROPERTIES;
    }

    @Override
    public boolean onBreak(Item item) {
        Block down = down();

        if (isTopHalf()) { // Top half
            this.getLevel().useBreakOn(down);
        } else {
            this.getLevel().setBlock(this, Block.get(BlockID.AIR), true, true);
        }

        return true;
    }

    public boolean isTopHalf() {
        return getBooleanValue(CommonBlockProperties.UPPER_BLOCK);
    }

    public void setTopHalf(boolean top) {
        this.setPropertyValue(CommonBlockProperties.UPPER_BLOCK, top);
    }

    private boolean isSupportValid(Block support) {
        switch (support.getId()) {
            case GRASS:
            case CLAY_BLOCK:
            case DIRT:
            case PODZOL:
            case FARMLAND:
            case MOSS_BLOCK:
                return true;
            default:
                return false;
        }
    }

    @Override
    public Item[] getDrops(Item item) {
        return item.isShears() ? new Item[] {this.toItem()} : Item.EMPTY_ARRAY;
    }

    @Override
    public int getToolType() {
        return ItemTool.TYPE_SHEARS;
    }

    @Override
    public double getHardness() {
        return 0;
    }

    @Override
    public double getResistance() {
        return 0;
    }

    @Override
    public int getWaterloggingLevel() {
        return 2;
    }

    @Override
    public boolean canHarvestWithHand() {
        return false;
    }

    @Override
    public boolean breaksWhenMoved() {
        return true;
    }

    public boolean sticksToPiston() {
        return false;
    }

    @Override
    public boolean place(@Nonnull Item item, @Nonnull Block block, @Nonnull Block target, @Nonnull BlockFace face, double fx, double fy, double fz, @Nullable Player player) {
        Block down = this.down();
        Block up = this.up();
        if(!isSupportValid(down) || !(block instanceof BlockWater) || up.getId() != AIR) {
            return false;
        }
        this.setPropertyValue(CommonBlockProperties.DIRECTION, player != null ? player.getDirection().getOpposite() : BlockFace.NORTH);
        this.setTopHalf(false);
        this.getLevel().setBlock(this, this, true, false);

        this.setTopHalf(true);
        this.getLevel().setBlock(up, this, true, true);
        this.getLevel().updateAround(this);
        return true;
    }

    @Override
    public int onUpdate(int type) {
        if (type == Level.BLOCK_UPDATE_NORMAL) {
            if (isTopHalf()) {
                // Top
                if (this.down().getId() != SMALL_DRIPLEAF_BLOCK) {
                    this.getLevel().setBlock(this, Block.get(BlockID.AIR), false, true);
                    return Level.BLOCK_UPDATE_NORMAL;
                }
            } else {
                // Bottom
                if (this.up().getId() != SMALL_DRIPLEAF_BLOCK || !isSupportValid(down())) {
                    this.getLevel().useBreakOn(this);
                    return Level.BLOCK_UPDATE_NORMAL;
                }
            }
        }
        return 0;
    }

    @Override
    public boolean canBeActivated() {
        return true;
    }

    @Override
    public boolean onActivate(@Nonnull Item item, Player player) {
        if(!item.isFertilizer()) {
            return false;
        }
        this.getLevel().addParticle(new BoneMealParticle(this));
        BlockDripLeaf dripLeaf = new BlockDripLeaf();

        if (down().getId() != Block.SMALL_DRIPLEAF_BLOCK) {
            this.getLevel().setBlock(up(), dripLeaf.clone(), true, true);
        } else {
            this.getLevel().setBlock(down(), dripLeaf.clone(), true, true);
            int height = ThreadLocalRandom.current().nextInt(2,6);
            for (int i = 1; i <= height; i++) {
                if ((this.y + i) < 255) {
                    this.getLevel().setBlock(up(i), dripLeaf.clone(), true, true);
                }
            }
        }
        this.getLevel().setBlock(this, dripLeaf, true, true);
        return false;
    }

    public BlockColor getColor() {
        return BlockColor.GREEN_BLOCK_COLOR;
    }
}
