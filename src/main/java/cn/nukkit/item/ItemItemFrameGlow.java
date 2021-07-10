package cn.nukkit.item;

import cn.nukkit.block.BlockItemFrameGlow;

/**
 * @author PleaseInsertNameHere
 * @since 10/07/2021
 */
public class ItemItemFrameGlow extends StringItem {
    public ItemItemFrameGlow() {
        this(1);
    }

    public ItemItemFrameGlow(int count) {
        super(MinecraftItemID.GLOW_FRAME.getNamespacedId(), "Glow Item Frame");
        this.block = new BlockItemFrameGlow();
        this.count = count;
    }
}
