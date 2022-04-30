package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.blockproperty.BlockProperties;
import cn.nukkit.blockproperty.CommonBlockProperties;
import cn.nukkit.event.block.BlockGrowEvent;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemTool;
import cn.nukkit.item.MinecraftItemID;
import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.level.Level;
import cn.nukkit.math.AxisAlignedBB;
import cn.nukkit.math.BlockFace;
import cn.nukkit.math.SimpleAxisAlignedBB;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.concurrent.ThreadLocalRandom;

public class BlockAmethystBudMedium extends BlockTransparent {

    @Override
    public String getName() {
        return "Medium Amethyst Bud";
    }

    @Override
    public int getId() {
        return MEDIUM_AMETHYST_BUD;
    }

    @Nonnull
    @Override
    public BlockProperties getProperties() {
        return new BlockProperties(CommonBlockProperties.FACING_DIRECTION);
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
        return true;
    }

    @Override
    public int getWaterloggingLevel() {
        return 1;
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
    public AxisAlignedBB getBoundingBox() {
        BlockFace face = this.getPropertyValue(CommonBlockProperties.FACING_DIRECTION);
        return new SimpleAxisAlignedBB(face == BlockFace.EAST ? this.getFloorX() + 1 : this.getFloorX(), face == BlockFace.UP ? this.getFloorY() + 1 : this.getFloorY(), face == BlockFace.NORTH ? this.getFloorZ() + 1 : this.getFloorZ(), face == BlockFace.EAST ? this.getFloorX() + 0.9 : this.getFloorX() + 0.1, face == BlockFace.UP ? this.getFloorY() + 0.9 : this.getFloorY() + 0.1, face == BlockFace.NORTH ? this.getFloorZ() + 0.9 : this.getFloorZ() + 0.1);
    }

    @Override
    public boolean place(@Nonnull Item item, @Nonnull Block block, @Nonnull Block target, @Nonnull BlockFace face, double fx, double fy, double fz, @Nullable Player player) {
        if (!target.isSolid()) {
            return false;
        }
        setPropertyValue(CommonBlockProperties.FACING_DIRECTION, face);
        return this.getLevel().setBlock(this, this, true, true);
    }

    @Override
    public int onUpdate(int type) {
        if (type == Level.BLOCK_UPDATE_NORMAL) {
            BlockFace face = this.getPropertyValue(CommonBlockProperties.FACING_DIRECTION).getOpposite();
            if (!this.getSide(face).isSolid(face.getOpposite())) {
                this.getLevel().useBreakOn(this);
            }
            return type;
        } else if (type == Level.BLOCK_UPDATE_RANDOM) {
            BlockFace face = this.getPropertyValue(CommonBlockProperties.FACING_DIRECTION);
            if (this.getSide(face.getOpposite()) instanceof BlockBuddingAmethyst) {
                BlockAmethystBudLarge bud = new BlockAmethystBudLarge();
                bud.setPropertyValue(CommonBlockProperties.FACING_DIRECTION, face);
                BlockGrowEvent growEvent = new BlockGrowEvent(this, bud);
                Server.getInstance().getPluginManager().callEvent(growEvent);
                if (growEvent.isCancelled()) {
                    return type;
                }
                this.getLevel().setBlock(this, bud, true, true);
            }
            return type;
        }
        return 0;
    }
}
