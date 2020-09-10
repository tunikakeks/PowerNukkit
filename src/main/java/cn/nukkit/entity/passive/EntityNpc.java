package cn.nukkit.entity.passive;

import cn.nukkit.entity.EntityInteractable;
import cn.nukkit.entity.EntityLiving;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;

/**
 * @author good777LUCKY
 */
public class EntityNpc extends EntityLiving implements EntityInteractable {

    public static final int NETWORK_ID = 51;
    
    public EntityNpc(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }
    
    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }
    
    @Override
    public float getWidth() {
        return 0.6f;
    }
    
    @Override
    public float getHeight() {
        return 2.1f;
    }
    
    @Override
    public boolean canDoInteraction() {
        return true;
    }
    
    @Override
    public String getInteractButtonText() {
        return "action.interact.edit";
    }
    
    @Override
    public String getName() {
        return "NPC";
    }
    
    @Override
    public void initEntity() {
        super.initEntity();
        this.setMaxHealth(Float.MAX_VALUE);
        this.setHealth(20);
        this.setNameTagVisible(true);
        this.setNameTagAlwaysVisible(true);
    }
}
