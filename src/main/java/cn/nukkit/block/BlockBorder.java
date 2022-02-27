package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.api.PowerNukkitOnly;
import cn.nukkit.api.Since;
import cn.nukkit.item.Item;
import cn.nukkit.math.AxisAlignedBB;
import cn.nukkit.math.BlockFace;
import cn.nukkit.math.Vector3;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@PowerNukkitOnly
@Since("1.4.0.0-PN")
@ParametersAreNonnullByDefault
public class BlockBorder extends BlockWallBase {
    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    public BlockBorder() {
        this(0);
    }

    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    public BlockBorder(int meta) {
        super(meta);
    }

    @Override
    public int getId() {
        return BORDER_BLOCK;
    }

    @Override
    public double getHardness() {
        return -1;
    }

    @Override
    public double getResistance() {
        return 18000000;
    }

    @Override
    public String getName() {
        return "Border Block";
    }

    @Override
    public boolean isBreakable(Item item) {
        return false;
    }

    @Override
    public boolean canBePushed() {
        return false;
    }

    @Override
    @PowerNukkitOnly
    public  boolean canBePulled() {
        return false;
    }

    @Override
    public boolean canHarvestWithHand() {
        return false;
    }

    @Override
    public boolean place(@Nonnull Item item, @Nonnull Block block, @Nonnull Block target, @Nonnull BlockFace face, double fx, double fy, double fz, @Nullable Player player) {
        if (player != null && (!player.isCreative() || !player.isOp())) {
            return false;
        }
        return super.place(item, block, target, face, fx, fy, fz, player);
    }

    @Since("1.4.0.0-PN")
    @PowerNukkitOnly
    @Override
    public boolean isBreakable(Vector3 vector, int layer, BlockFace face, Item item, @Nullable Player player, boolean setBlockDestroy) {
        if (player != null && (!player.isCreative() || !player.isOp())) {
            return false;
        }
        return super.isBreakable(vector, layer, face, item, player, setBlockDestroy);
    }

    @Override
    public Item[] getDrops(Item item) {
        return Item.EMPTY_ARRAY;
    }

    @Override
    protected AxisAlignedBB recalculateBoundingBox() {
        AxisAlignedBB aabb = super.recalculateBoundingBox();
        aabb.setMinY(Double.MIN_VALUE);
        aabb.setMaxY(Double.MAX_VALUE);
        return aabb;
    }
}
