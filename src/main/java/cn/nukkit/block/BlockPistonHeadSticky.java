package cn.nukkit.block;

import cn.nukkit.item.Item;

import cn.nukkit.api.PowerNukkitOnly;

@PowerNukkitOnly
public class BlockPistonHeadSticky extends BlockPistonHead {
    @PowerNukkitOnly
    public BlockPistonHeadSticky() {
        this(0);
    }

    @PowerNukkitOnly
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
