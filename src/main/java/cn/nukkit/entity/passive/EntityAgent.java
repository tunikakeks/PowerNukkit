package cn.nukkit.entity.passive;

import cn.nukkit.api.PowerNukkitOnly;
import cn.nukkit.api.Since;
import cn.nukkit.entity.EntityLiving;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;

public class EntityAgent extends EntityLiving {

    public static final int NETWORK_ID = 56;

    public EntityAgent(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);

        this.setMaxHealth(Integer.MAX_VALUE);
        this.setHealth(Integer.MAX_VALUE);

        this.invulnerable = true;
        this.fireProof = true;
    }

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }

    @Override
    public float getWidth() {
        return 0.5F;
    }

    @Override
    public float getHeight() {
        return 0.5F;
    }

    @Override
    public boolean attack(EntityDamageEvent source) {
        return false;
    }

    @PowerNukkitOnly
    @Since("1.5.1.0-PN")
    @Override
    public String getOriginalName() {
        return "Agent";
    }
}
