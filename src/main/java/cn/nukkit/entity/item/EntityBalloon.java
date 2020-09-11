package cn.nukkit.entity.item;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityInteractable;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.entity.EntityDamageEvent.DamageCause;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;

/**
 * @author good777LUCKY
 */
public class EntityBalloon extends Entity {

    public static final int NETWORK_ID = 107;
    
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
    }
    
    // TODO: Add Balloon Functionality
}
