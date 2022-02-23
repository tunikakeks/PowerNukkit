package cn.nukkit.dispenser;

import cn.nukkit.api.PowerNukkitOnly;
import cn.nukkit.block.*;
import cn.nukkit.item.Item;
import cn.nukkit.level.particle.SmokeParticle;
import cn.nukkit.math.BlockFace;

@PowerNukkitOnly
public class DyeDispenseBehavior extends DefaultDispenseBehavior {

    @PowerNukkitOnly
    public DyeDispenseBehavior() {
        super();
    }

    @PowerNukkitOnly
    @Override
    public Item dispense(BlockDispenser block, BlockFace face, Item item) {
        Block target = block.getSide(face);

        if (item.isFertilizer()) {
            if (target instanceof BlockCrops || target instanceof BlockSapling
                    || target instanceof BlockTallGrass
                    || target instanceof BlockDoublePlant || target instanceof BlockMushroom
                    || target instanceof BlockBambooSapling || target instanceof BlockBamboo
                    || target instanceof BlockCaveVines || target instanceof BlockNylium
                    || target instanceof BlockGrass) {
                if (!target.onActivate(item)) {
                    target.getLevel().addParticle(new SmokeParticle(block.getDispensePosition()));
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
