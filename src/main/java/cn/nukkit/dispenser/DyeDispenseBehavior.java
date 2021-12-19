package cn.nukkit.dispenser;

import cn.nukkit.block.*;
import cn.nukkit.item.Item;
import cn.nukkit.math.BlockFace;

public class DyeDispenseBehavior extends DefaultDispenseBehavior {

    @Override
    public Item dispense(BlockDispenser block, BlockFace face, Item item) {
        Block target = block.getSide(face);

        if (item.isFertilizer()) {
            if (target instanceof BlockCrops || target instanceof BlockSapling || target instanceof BlockTallGrass
                    || target instanceof BlockDoublePlant || target instanceof BlockMushroom || target instanceof BlockBambooSapling
                    || target instanceof BlockBamboo || target instanceof BlockCaveVines) {
                if (!target.onActivate(item)) {
                    item.count++;
                }
                return null;
            } else {
                this.success = false;
            }
        }

        return super.dispense(block, face, item);
    }
}
