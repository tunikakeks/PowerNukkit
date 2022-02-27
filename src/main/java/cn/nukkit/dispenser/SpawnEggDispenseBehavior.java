package cn.nukkit.dispenser;

import cn.nukkit.api.PowerNukkitOnly;
import cn.nukkit.block.BlockDispenser;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityLiving;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemSpawnEgg;
import cn.nukkit.math.BlockFace;
import cn.nukkit.math.Vector3;

@PowerNukkitOnly
public class SpawnEggDispenseBehavior extends DefaultDispenseBehavior {

    @PowerNukkitOnly
    public SpawnEggDispenseBehavior() {
        super();
    }

    @PowerNukkitOnly
    @Override
    public Item dispense(BlockDispenser block, BlockFace face, Item item) {
        Vector3 pos = block.getSide(face).add(0.5, 0.7, 0.5);

        Entity entity = Entity.createEntity(((ItemSpawnEgg)item).getEntityNetworkId(), block.level.getChunk(pos.getChunkX(), pos.getChunkZ()),
                Entity.getDefaultNBT(pos));

        this.success = entity != null;

        if (this.success) {
            if (item.hasCustomName() && entity instanceof EntityLiving) {
                entity.setNameTag(item.getCustomName());
            }

            entity.spawnToAll();
            return null;
        }

        return super.dispense(block, face, item);
    }
}
