package cn.nukkit.entity.mob;

import cn.nukkit.api.PowerNukkitOnly;
import cn.nukkit.api.Since;
import cn.nukkit.entity.EntityBalloonable;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;

@Since("1.4.0.0-PN")
public class EntitySnowGolem extends EntityMob implements EntityBalloonable {
    @Since("1.4.0.0-PN")
    public EntitySnowGolem(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Since("1.4.0.0-PN")
    public static final int NETWORK_ID = 21;

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }

    @PowerNukkitOnly
    @Since("1.5.1.0-PN")
    @Override
    public String getOriginalName() {
        return "Snow Golem";
    }

    @Override
    public float getWidth() {
        return 0.4f;
    }

    @Override
    public float getHeight() {
        return 1.8f;
    }

    @Override
    protected void initEntity() {
        super.initEntity();
        this.setMaxHealth(4);
    }

    @Override
    public float getBalloonMass() {
        return 1.0F;
    }
}
