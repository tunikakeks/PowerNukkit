package cn.nukkit.block;

import cn.nukkit.api.PowerNukkitOnly;
import cn.nukkit.api.Since;
import cn.nukkit.blockproperty.BlockProperties;
import cn.nukkit.blockproperty.value.StoneSlab2Type;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.item.ItemTool;
import cn.nukkit.utils.BlockColor;

import javax.annotation.Nonnull;

/**
 * @author CreeperFace
 * @since 26. 11. 2016
 */
public class BlockSlabRedSandstone extends BlockSlab {
    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    public static final BlockProperties PROPERTIES = new BlockProperties(
            StoneSlab2Type.PROPERTY,
            TOP_SLOT_PROPERTY
    );

    public static final int RED_SANDSTONE = 0;
    public static final int PURPUR = 1;
    @PowerNukkitOnly public static final int PRISMARINE = 2;
    @PowerNukkitOnly public static final int PRISMARINE_BRICKS = 3;
    @PowerNukkitOnly public static final int DARK_PRISMARINE = 4;
    @PowerNukkitOnly public static final int MOSSY_COBBLESTONE = 5;
    @PowerNukkitOnly public static final int SMOOTH_SANDSTONE = 6;
    @PowerNukkitOnly public static final int RED_NETHER_BRICK = 7;

    public BlockSlabRedSandstone() {
        this(0);
    }

    @Since("1.4.0.0-PN")
    @PowerNukkitOnly
    @Nonnull
    @Override
    public ItemBlock asItemBlock(int count) {
        return new ItemBlock(this, this.getDamage() & 0x07, count);

    }

    public BlockSlabRedSandstone(int meta) {
        super(meta, DOUBLE_RED_SANDSTONE_SLAB);
    }

    @Override
    public int getId() {
        return RED_SANDSTONE_SLAB;
    }

    @Since("1.4.0.0-PN")
    @PowerNukkitOnly
    @Nonnull
    @Override
    public BlockProperties getProperties() {
        return PROPERTIES;
    }

    @PowerNukkitOnly
    @Override
    public String getSlabName() {
        return getSlabType().getEnglishName();
    }

    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    public StoneSlab2Type getSlabType() {
        return getPropertyValue(StoneSlab2Type.PROPERTY);
    }

    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    public void setSlabType(StoneSlab2Type type) {
        setPropertyValue(StoneSlab2Type.PROPERTY, type);
    }

    @PowerNukkitOnly
    @Override
    public boolean isSameType(BlockSlab slab) {
        return slab.getId() == getId() && getSlabType().equals(slab.getPropertyValue(StoneSlab2Type.PROPERTY));
    }

    @Override
    public BlockColor getColor() {
        return getSlabType().getColor();
    }

    @Override
    public int getToolType() {
        return ItemTool.TYPE_PICKAXE;
    }

    @Override
    @PowerNukkitOnly
    public int getToolTier() {
        return ItemTool.TIER_WOODEN;
    }

    @Override
    public boolean canHarvestWithHand() {
        return false;
    }
}
