package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.api.PowerNukkitOnly;
import cn.nukkit.api.Since;
import cn.nukkit.blockproperty.BlockProperties;
import cn.nukkit.blockproperty.CommonBlockProperties;
import cn.nukkit.event.level.StructureGrowEvent;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemTool;
import cn.nukkit.level.Level;
import cn.nukkit.level.ListChunkManager;
import cn.nukkit.level.generator.object.tree.ObjectAzaleaTree;
import cn.nukkit.level.particle.BoneMealParticle;
import cn.nukkit.math.BlockFace;
import cn.nukkit.math.NukkitRandom;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author LoboMetalurgico
 * @since 13/06/2021
 */

@PowerNukkitOnly
@Since("FUTURE")
public class BlockAzalea extends BlockTransparent {
    @PowerNukkitOnly
    @Since("FUTURE")
    public static final BlockProperties PROPERTIES = CommonBlockProperties.EMPTY_PROPERTIES;

    @PowerNukkitOnly
    @Since("FUTURE")
    public BlockAzalea() {
    }

    @PowerNukkitOnly
    @Since("FUTURE")
    @Nonnull
    @Override
    public BlockProperties getProperties() {
        return PROPERTIES;
    }

    @Override
    public String getName() {
        return "Azalea";
    }

    @Override
    public int getId() {
        return AZALEA;
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
    public int getToolType() {
        return ItemTool.TYPE_NONE;
    }

    @Override
    public boolean canHarvestWithHand() {
        return true;
    }

    @Override
    public boolean canBeActivated() {
        return true;
    }

    @Override
    public boolean onActivate(@Nonnull Item item, Player player) {
        if (item.isFertilizer()) { // BoneMeal
            if (player != null && !player.isCreative()) {
                item.count--;
            }

            this.level.addParticle(new BoneMealParticle(this));
            if (ThreadLocalRandom.current().nextFloat() >= 0.45) {
                return true;
            }

            ListChunkManager chunkManager = new ListChunkManager(this.level);
            new ObjectAzaleaTree().placeObject(chunkManager, (int) this.x, (int) this.y, (int) this.z, new NukkitRandom());
            StructureGrowEvent structureGrowEvent = new StructureGrowEvent(this, chunkManager.getBlocks());
            this.level.getServer().getPluginManager().callEvent(structureGrowEvent);
            if (structureGrowEvent.isCancelled()) {
                return true;
            }
            for(Block block : structureGrowEvent.getBlockList()) {
                this.level.setBlock(block, block);
            }

            return true;
        }
        return false;
    }

    @Override
    public boolean place(@Nonnull Item item, @Nonnull Block block, @Nonnull Block target, @Nonnull BlockFace face, double fx, double fy, double fz, @Nullable Player player) {
        int id = this.down().getId();
        if (id != MOSS_BLOCK && id != GRASS && id != DIRT && id != DIRT_WITH_ROOTS) {
            return false;
        }
        this.getLevel().setBlock(this, this, true, true);
        return true;
    }

    @Override
    public int onUpdate(int type) {
        if (type == Level.BLOCK_UPDATE_NORMAL) {
            int id = this.down().getId();
            if (id != MOSS_BLOCK && id != GRASS && id != DIRT && id != DIRT_WITH_ROOTS) {
                this.getLevel().useBreakOn(this);
            }
            return type;
        }
        return 0;
    }

    @Override
    public boolean canPassThrough() {
        return true;
    }

    @Override
    public boolean canBeClimbed() {
        return true;
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
    public boolean isSolid(BlockFace side) {
        return false;
    }
}
