package cn.nukkit.item;

public class ItemSpyGlass extends StringItem {

    public ItemSpyGlass() {
        super(MinecraftItemID.SPYGLASS.getNamespacedId(), "Spy Glass");
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }
}
