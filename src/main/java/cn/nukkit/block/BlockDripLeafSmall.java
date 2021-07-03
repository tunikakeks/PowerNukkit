package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.blockproperty.BlockProperties;
import cn.nukkit.blockproperty.BooleanBlockProperty;
import cn.nukkit.blockproperty.CommonBlockProperties;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemTool;
import cn.nukkit.level.Level;
import cn.nukkit.level.particle.BoneMealParticle;
import cn.nukkit.math.BlockFace;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.concurrent.ThreadLocalRandom;

public class BlockDripLeafSmall extends BlockTransparent {

    public static final BooleanBlockProperty HEAD = new BooleanBlockProperty("small_dripleaf_head", false);

    public static final BlockProperties PROPERTIES = new BlockProperties(CommonBlockProperties.DIRECTION, HEAD);

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
    public boolean canBeActivated() {
        return true;
    }

    @Override
    public boolean place(@Nonnull Item item, @Nonnull Block block, @Nonnull Block target, @Nonnull BlockFace face, double fx, double fy, double fz, @Nullable Player player) {
        if(down().getId() != Block.MOSS_BLOCK || up().getId() != Block.AIR) {
            return false;
        }
        if (player != null) {
            this.setPropertyValue(CommonBlockProperties.DIRECTION, player.getDirection().getOpposite());
        }
        this.level.setBlock(this, this, true, true);
        this.setPropertyValue(HEAD, false);

        BlockDripLeafSmall up = (BlockDripLeafSmall) this.clone();
        up.setPropertyValue(HEAD, true);
        this.level.setBlock(up(), up, true, true);
        return this.getLevel().setBlock(this, this, true, true);
    }

    @Override
    public int onUpdate(int type) {
        if(type == Level.BLOCK_UPDATE_NORMAL) {
            if(this.down().getId() != Block.MOSS_BLOCK && !(this.down() instanceof BlockDripLeafSmall)) {
                this.getLevel().useBreakOn(this);
            }
        }
        return 0;
    }

    @Override
    public boolean onActivate(@Nonnull Item item, Player player) {
        if(!item.isFertilizer()) {
            return false;
        }
        this.getLevel().addParticle(new BoneMealParticle(this));
        BlockDripLeaf dripLeaf = new BlockDripLeaf();
        dripLeaf.setPropertyValue(BlockDripLeaf.BIG_HEAD, false);
        dripLeaf.setPropertyValue(CommonBlockProperties.DIRECTION, this.getPropertyValue(CommonBlockProperties.DIRECTION));
        this.getLevel().setBlock(this, dripLeaf, true, true);

        BlockDripLeaf dripLeafHead = (BlockDripLeaf) dripLeaf.clone();
        dripLeafHead.setPropertyValue(BlockDripLeaf.BIG_HEAD, true);

        if (down().getId() != Block.SMALL_DRIPLEAF_BLOCK) {
            this.getLevel().setBlock(up(), dripLeafHead, true, true);
        } else {
            this.getLevel().setBlock(down(), dripLeaf.clone(), true, true);
            int height = ThreadLocalRandom.current().nextInt(2,6);
            for (int hY = (int) (this.y + 1); hY <= (this.y + height); y++) {
                Block block = this.getLevel().getBlock((int) x, hY, (int) z);
                if (block.getId() != Block.AIR && !(block instanceof BlockDripLeafSmall)) {
                    this.getLevel().setBlock(block.down(), dripLeafHead, true, true);
                } else if (hY == (this.y + height)) {
                    this.getLevel().setBlock(block, dripLeafHead, true, true);
                } else {
                    this.getLevel().setBlock(block, dripLeaf.clone(), true, true);
                }
            }
        }
        return false;
    }

    @Override
    public boolean canHarvestWithHand() {
        return false;
    }
}
