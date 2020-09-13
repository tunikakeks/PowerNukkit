package cn.nukkit.entity.item;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.entity.EntityDamageEvent.DamageCause;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;

/**
 * @author good777LUCKY
 */
public class EntityBalloon extends Entity {

    public static final int NETWORK_ID = 107;
    
    protected long balloonAttached;
    protected float balloonMaxHeight = 256.0f;
    protected boolean balloonShouldDrop = false;
    
    public EntityBalloon(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }
    
    @Override
    public void initEntity() {
        super.initEntity();
        
        this.setMaxHealth(1);
        this.setHealth(1);
        
        this.setDataFlag(DATA_FLAGS, DATA_FLAG_GRAVITY, false);
        if (this.namedTag.contains("Color")) {
            this.dataProperties.putByte(DATA_COLOR, this.namedTag.getByte("Color"));
        } else {
            this.dataProperties.putByte(DATA_COLOR, 0);
        }
    }
    
    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }
    
    @Override
    public String getName() {
        return "Balloon";
    }
    
    @Override
    public float getWidth() {
        return 0.4f;
    }
    
    @Override
    public float getHeight() {
        return 0.4f;
    }
    
    @Override
    protected float getGravity() {
        return -1.6f; // Should be Lift Force, balloon doesn't have gravity
    }
    
    @Override
    protected float getDrag() {
        return 0.02f; // TODO: Correct value
    }
    
    @Override
    public boolean attack(EntityDamageEvent source) {
        DamageCause cause = source.getCause();
        // TODO: Add Functionality
        this.close();
        return true;
    }
    
    @Override
    public void saveNBT() {
        super.saveNBT();
        
        this.namedTag.putByte("Color", this.dataProperties.getByte(DATA_COLOR));
        this.namedTag.putLong("balloon_attached", balloonAttached);
        this.namedTag.putFloat("balloon_max_height", balloonMaxHeight);
        this.namedTag.putBoolean("balloon_should_drop", balloonShouldDrop);
    }
    
    @Override
    public boolean onUpdate(int currentTick) {
        if (this.closed) {
            return false;
        }
        
        int tickDiff = currentTick - this.lastUpdate;
        if (tickDiff <= 0 && !this.justCreated) {
            return true;
        }
        
        this.lastUpdate = currentTick;
        
        this.timing.startTiming();
        
        boolean hasUpdate = this.entityBaseTick(tickDiff);
        
        this.timing.stopTiming();
        
        return hasUpdate || !this.onGround || Math.abs(this.motionX) > 0.00001 || Math.abs(this.motionY) > 0.00001 || Math.abs(this.motionZ) > 0.00001;
    }
}
