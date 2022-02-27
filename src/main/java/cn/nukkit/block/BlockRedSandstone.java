package cn.nukkit.block;

import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.utils.BlockColor;

/**
 * @author CreeperFace
 * @since 26. 11. 2016
 */
public class BlockRedSandstone extends BlockSandstone {

    public BlockRedSandstone() {
        this(0);
    }

    public BlockRedSandstone(int meta) {
        super(meta);
    }

    @Override
    public int getId() {
        return RED_SANDSTONE;
    }

    @Override
    public String getName() {
        String[] names = new String[]{
                "Red Sandstone",
                "Chiseled Red Sandstone",
                "Smooth Red Sandstone",
                ""
        };

        return names[this.getDamage() & 0x03];
    }

    @Override
    public Item toItem() {
        return new ItemBlock(this, this.getDamage() & 0x03);
    }

    @Override
    public boolean canHarvestWithHand() {
        return false;
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.ORANGE_BLOCK_COLOR;
    }
}
