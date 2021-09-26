package cn.nukkit.block;

import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;

public class BlockLeavesAzaleaFlowered extends BlockLeavesAzalea {

    @Override
    public String getName() {
        return "Flowered Azalea Leaves";
    }

    @Override
    public int getId() {
        return AZALEA_LEAVES_FLOWERED;
    }

    @Override
    public Item toItem() {
        return new ItemBlock(Block.get(Block.AZALEA_LEAVES_FLOWERED));
    }
}
