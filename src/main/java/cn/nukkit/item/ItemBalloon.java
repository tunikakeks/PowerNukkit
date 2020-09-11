package cn.nukkit.item;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockFence;
import cn.nukkit.event.entity.CreatureSpawnEvent;
import cn.nukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import cn.nukkit.level.Level;
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
        
        if (!(target instanceof BlockFence)) {
            return false;
        }
        
        CompoundTag nbt = new CompoundTag()
                .putList(new ListTag<DoubleTag>("Pos")
                        .add(new DoubleTag("", block.getX() + 0.5))
                        .add(new DoubleTag("", target.getBoundingBox() == null ? block.getY() + 1.385f : target.getBoundingBox().getMaxY() + 0.885f)) // TODO: Correct Value
                        .add(new DoubleTag("", block.getZ() + 0.5)))
                .putList(new ListTag<DoubleTag>("Motion")
                        .add(new DoubleTag("", 0))
                        .add(new DoubleTag("", 0))
                        .add(new DoubleTag("", 0)))
                .putList(new ListTag<FloatTag>("Rotation")
                        .add(new FloatTag("", 0))
                        .add(new FloatTag("", 0)));
                .putByte("Color", this.getDamage() & 0xf);
        
        CreatureSpawnEvent ev = new CreatureSpawnEvent(107, block, nbt, SpawnReason.BALLOON);
        level.getServer().getPluginManager().callEvent(ev);
        
        if (ev.isCancelled()) {
            return false;
        } 
        
        Entity entity = Entity.createEntity("Balloon", chunk, nbt);
        
        if (entity != null) {
            if (!player.isCreative()) {
                player.getInventory().decreaseCount(player.getInventory().getHeldItemIndex());
            }
            entity.spawnToAll();
            return true;
        }
        
        return false;
    }
}
