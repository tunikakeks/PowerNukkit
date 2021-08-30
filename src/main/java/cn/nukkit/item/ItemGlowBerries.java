package cn.nukkit.item;

import cn.nukkit.block.BlockCaveVines;

/**
 * @author PleaseInsertNameHere
 * @since 01/07/2021
 */
public class ItemGlowBerries extends StringItemEdible {
    public ItemGlowBerries() {
        this(1);
    }

    public ItemGlowBerries(int count) {
        super(MinecraftItemID.GLOW_BERRIES.getNamespacedId(), "Glow Berries");
        this.block = new BlockCaveVines();
        this.count = count;
    }
}
