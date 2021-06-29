package cn.nukkit.entity.mob;

import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;

import javax.annotation.Nonnull;

public class EntityGoat extends EntityMob {

    public static final int NETWORK_ID = 128;

    public EntityGoat(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Nonnull
    @Override
    public String getName() {
        return "Goat";
    }

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }
}
