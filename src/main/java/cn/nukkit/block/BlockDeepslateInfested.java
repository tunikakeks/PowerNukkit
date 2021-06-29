package cn.nukkit.block;

import cn.nukkit.item.Item;

/**
 * @author PleaseInsertNameHere
 * @since 28/06/2021
 */
public class BlockDeepslateInfested extends BlockSolid {
    public BlockDeepslateInfested() {

    }

    @Override
    public String getName() {
        return "Infested Deepslate";
    }

    @Override
    public int getId() {
        return Block.INFESTED_DEEPSLATE;
    }

    @Override
    public double getHardness() {
        return 0.75;
    }

    @Override
    public double getResistance() {
        return 3.75;
    }

    @Override
    public Item[] getDrops(Item item) {
        return Item.EMPTY_ARRAY;
    }
}
