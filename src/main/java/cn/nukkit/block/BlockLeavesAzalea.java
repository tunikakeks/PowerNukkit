package cn.nukkit.block;

import cn.nukkit.blockproperty.BlockProperties;
import cn.nukkit.blockproperty.value.WoodType;
import cn.nukkit.item.Item;
import cn.nukkit.item.MinecraftItemID;

import javax.annotation.Nonnull;

public class BlockLeavesAzalea extends BlockLeaves {

    public static final BlockProperties PROPERTIES = new BlockProperties(PERSISTENT, UPDATE);

    @Override
    public String getName() {
        return "Azalea Leaves";
    }

    @Override
    public int getId() {
        return AZALEA_LEAVES;
    }

    @Override
    protected Item getSapling() {
        return toItem();
    }

    @Override
    protected boolean canDropApple() {
        return false;
    }

    @Override
    public void setType(WoodType type) {

    }

    @Override
    public WoodType getType() {
        return WoodType.AZALEA;
    }

    @Nonnull
    @Override
    public BlockProperties getProperties() {
        return PROPERTIES;
    }

    @Override
    public Item toItem() {
        return MinecraftItemID.AZALEA.get(1);
    }
}
