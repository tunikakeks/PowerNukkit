package cn.nukkit.entity.item;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityInteractable;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.entity.EntityDamageEvent.DamageCause;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;

/**
 * @author good777LUCKY
 */
public class EntityBalloon extends Entity implements EntityInteractable {

    public static final int NETWORK_ID = 107;
    
    public EntityBalloon(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }
    
    @Override
    public void initEntity() {
        super.initEntity();
        
        this.setMaxHealth(1);
        this.setHealth(1);
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
        return -1.6f; // Lift Force
    }
    
    @Override
    protected float getDrag() {
        return 0.02f;
    }
    
    @Override
    public boolean attack(EntityDamageEvent source) {
        DamageCause cause = source.getCause();
        // TODO: Add Functionality
        this.close();
        return true;
    }
    
    @Override
    public boolean canDoInteraction() {
        return true;
    }
    
    @Override
    public String getInteractButtonText() {
        return "action.interact.balloon";
    }
    
    @Override
    public boolean onInteract(Player player, Item item, Vector3 clickedPos) {
        // TODO: Add Functionality
        return true;
    }
    
    // TODO: Add Balloon Functionality
}
