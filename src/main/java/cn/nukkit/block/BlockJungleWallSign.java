package cn.nukkit.block;

import cn.nukkit.api.PowerNukkitOnly;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemJungleSign;

@PowerNukkitOnly
public class BlockJungleWallSign extends BlockWallSign {
    @PowerNukkitOnly
    public BlockJungleWallSign() {
        this(0);
    }

    @PowerNukkitOnly
    public BlockJungleWallSign(int meta) {
        super(meta);
    }

    @Override
    public int getId() {
        return JUNGLE_WALL_SIGN;
    }

    @PowerNukkitOnly
    @Override
    protected int getPostId() {
        return JUNGLE_STANDING_SIGN;
    }

    @Override
    public String getName() {
        return "Jungle Wall Sign";
    }

    @Override
    public Item toItem() {
        return new ItemJungleSign();
    }
}
