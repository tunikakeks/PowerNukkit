package cn.nukkit.block;

import cn.nukkit.api.PowerNukkitOnly;
import cn.nukkit.api.Since;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemWarpedSign;

@PowerNukkitOnly
@Since("1.4.0.0-PN")
public class BlockWarpedWallSign extends BlockWallSign {
    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    public BlockWarpedWallSign() {
        this(0);
    }

    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    public BlockWarpedWallSign(int meta) {
        super(meta);
    }

    @Override
    public int getId() {
        return WARPED_WALL_SIGN;
    }

    @PowerNukkitOnly
    @Override
    protected int getPostId() {
        return WARPED_STANDING_SIGN;
    }

    @Override
    public String getName() {
        return "Warped Wall Sign";
    }

    @Override
    public Item toItem() {
        return new ItemWarpedSign();
    }
    
    @Override
    public int getBurnChance() {
        return 0;
    }
    
    @Override
    public int getBurnAbility() {
        return 0;
    }
}
