package cn.nukkit.entity.passive;

import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;

/**
 * @author Pub4Game
 * @since 21.06.2016
 *
 * @author good777LUCKY
 */
public interface EntityNPC extends EntityLiving {

    public static final int NETWORK_ID = 51;
    
    public EntityNPC(FullChunk chunk, CompoundTag nbt) {
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
