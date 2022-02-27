package cn.nukkit.item;

import cn.nukkit.api.Since;

/**
 * @author LT_Name
 */
@Since("FUTURE")
public class ItemSpyglass extends StringItem {

    public ItemSpyglass() {
        super(MinecraftItemID.SPYGLASS.getNamespacedId(), "Spy Glass");
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }
}
