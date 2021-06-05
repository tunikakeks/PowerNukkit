package cn.nukkit.block;

import cn.nukkit.item.Item;

public class BlockPistonHeadSticky extends BlockPistonHead {
    public BlockPistonHeadSticky() {
        this(0);
    }
    
    public BlockPistonHeadSticky(int meta) {
        super(meta);
    }
    
    @Override
    public int getId() {
        return PISTON_HEAD_STICKY;
    }
    
    @Override
    public String getName() {
        return "Sticky Piston Head";
    }

    @Override
    public Item[] getDrops(Item item) {
        return new Item[] {Item.get(BlockID.STICKY_PISTON)};
    }
}
