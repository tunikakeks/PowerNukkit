package cn.nukkit.entity.item;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityInteractable;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.entity.EntityDamageEvent.DamageCause;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;

/**
 * @author good777LUCKY
 */
public class EntityCamera extends Entity implements EntityInteractable {

    public static final int NETWORK_ID = 62;
    
    public EntityCamera(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }
    
    @Override
    public void initEntity() {
        super.initEntity();
        
        this.setMaxHealth(4);
        this.setHealth(4);
    }
    
    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }
    
    @Override
    public String getName() {
        return "Camera";
    }
    
    @Override
    public float getWidth() {
        return 0.75f;
    }
    
    @Override
    public float getHeight() {
        return 1.8f;
    }
    
    @Override
    protected float getGravity() {
        return 0.08f;
    }
    
    @Override
    protected float getDrag() {
        return 0.02f;
    }
    
    @Override
    public boolean attack(EntityDamageEvent source) {
        if (source instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent entityDamageByEntityEvent = (EntityDamageByEntityEvent) source;
            Entity damager = entityDamageByEntityEvent.getDamager();
            if (damager instanceof Player) {
                Player damagerPlayer = (Player) damager;
                //this.level.addParticle(new );
                this.close();
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean canDoInteraction() {
        return true;
    }
    
    @Override
    public String getInteractButtonText() {
        return "action.interact.takepicture";
    }
    
    @Override
    public boolean onInteract(Player player, Item item, Vector3 clickedPos) {
        // TODO: Add Functionality
        return true;
    }
    
    @Override
    public boolean onUpdate(int currentTick) {
        if (this.closed) {
            return false;
        }
        
        int tickDiff = currentTick - lastUpdate;
        boolean hasUpdated = super.onUpdate(currentTick);
        
        if (closed || tickDiff <= 0 && !justCreated) {
            return hasUpdated;
        }
        
        this.timing.startTiming();
        lastUpdate = currentTick;
        boolean hasUpdate = entityBaseTick(tickDiff);
        
        if (isAlive() && !onGround) {
            motionY -= getGravity();
            move(motionX, motionY, motionZ);
            float friction = 1 - getDrag();
            motionX *= friction;
            motionY *= 1 - getDrag();
            motionZ *= friction;
            updateMovement();
            hasUpdate = true;
        }
        
        this.timing.stopTiming();
        
        return hasUpdate || !onGround || Math.abs(motionX) > 0.00001 || Math.abs(motionY) > 0.00001 || Math.abs(motionZ) > 0.00001;
        // TODO: Add Camera Functionality
    }
}
