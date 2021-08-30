package cn.nukkit.item;

import cn.nukkit.block.BlockCaveVines;

public class ItemGlowBerries extends StringItemEdible {

    public ItemGlowBerries() {
        super(MinecraftItemID.GLOW_BERRIES.getNamespacedId(), "Glow Berries");
        this.block = new BlockCaveVines();
    }
}
