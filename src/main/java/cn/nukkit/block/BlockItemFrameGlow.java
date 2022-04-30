package cn.nukkit.block;

import cn.nukkit.api.PowerNukkitOnly;
import cn.nukkit.api.Since;
import cn.nukkit.blockentity.BlockEntityItemFrame;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemItemFrame;
import cn.nukkit.item.ItemItemFrameGlow;

/**
 * @author LoboMetalurgico
 * @since 13/06/2021
 */

// TODO: This is a basic implementation just to allow for use, fixes must be made
@PowerNukkitOnly
@Since("FUTURE")
public class BlockItemFrameGlow extends BlockItemFrame {
    @PowerNukkitOnly
    @Since("FUTURE")
    public BlockItemFrameGlow() {
        this(0);
    }

    @PowerNukkitOnly
    @Since("FUTURE")
    public BlockItemFrameGlow(int meta) {
        super(meta);
    }

    @Override
    public String getName() {
        return "Glow Item Frame";
    }

    @Override
    public int getId() {
        return GLOW_FRAME;
    }

    @Override
    public Item toItem() {
        BlockEntityItemFrame itemFrame = this.getBlockEntity();
        if (itemFrame != null) {
            Item itemInFrame = itemFrame.getItem();
            return itemInFrame.isNull() ? new ItemItemFrameGlow() : itemInFrame;
        }
        return new ItemItemFrameGlow();
    }
}
