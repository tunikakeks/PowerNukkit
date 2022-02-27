package cn.nukkit.item;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockFence;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.item.EntityBalloon;
import cn.nukkit.event.entity.CreatureSpawnEvent;
import cn.nukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import cn.nukkit.level.Level;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.BlockFace;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.DoubleTag;
import cn.nukkit.nbt.tag.FloatTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.utils.DyeColor;

/**
 * @author good777LUCKY
 */
public class ItemBalloon extends Item {

    public ItemBalloon() {
        this(0, 1);
    }
    
    public ItemBalloon(Integer meta) {
        this(meta, 1);
    }
    
    public ItemBalloon(Integer meta, int count) {
        super(BALLOON, meta, count, DyeColor.getByWoolData(meta).getName() + " Balloon");
    }
    
    @Override
    public int getMaxStackSize() {
        return 16;
    }
    
    public DyeColor getDyeColor() {
        return DyeColor.getByDyeData(meta);
    }
    
    @Override
    public boolean canBeActivated() {
        return true;
    }
    
    @Override
    public boolean onActivate(Level level, Player player, Block block, Block target, BlockFace face, double fx, double fy, double fz) {
        FullChunk chunk = level.getChunk((int) block.getX() >> 4, (int) block.getZ() >> 4);

        if (chunk == null) {
            return false;
        }

        if (target instanceof BlockFence) {
            if (target.up().getId() != AIR || target.up(2).getId() != AIR) {
                return false;
            }

            CompoundTag nbtLeashKnot = new CompoundTag()
                    .putList(new ListTag<DoubleTag>("Pos")
                            .add(new DoubleTag("", target.getX() + 0.5))
                            .add(new DoubleTag("", target.getY() + 0.25))
                            .add(new DoubleTag("", target.getZ() + 0.5)))
                    .putList(new ListTag<DoubleTag>("Motion")
                            .add(new DoubleTag("", 0))
                            .add(new DoubleTag("", 0))
                            .add(new DoubleTag("", 0)))
                    .putList(new ListTag<FloatTag>("Rotation")
                            .add(new FloatTag("", 0))
                            .add(new FloatTag("", 0)));

            Entity entityLeashKnot = Entity.createEntity("LeashKnot", chunk, nbtLeashKnot);

            if (entityLeashKnot == null) {
                return false;
            }

            EntityBalloon entityBalloon = EntityBalloon.create(target.getLocation().add(1.0D, 1.75D, 0.5D), this.getDyeColor(), block.y + 2.0D, false, entityLeashKnot);

            CreatureSpawnEvent ev = new CreatureSpawnEvent(EntityBalloon.NETWORK_ID, block, entityBalloon.namedTag, SpawnReason.BALLOON);
            level.getServer().getPluginManager().callEvent(ev);

            if (ev.isCancelled()) {
                return false;
            }

            if (!player.isCreative()) {
                player.getInventory().decreaseCount(player.getInventory().getHeldItemIndex());
            }

            entityLeashKnot.spawnToAll();
            entityBalloon.spawnToAll();
            return true;
        }
        return true;
    }
}
