package cn.nukkit.entity.mob;

import cn.nukkit.Player;
import cn.nukkit.api.PowerNukkitDifference;
import cn.nukkit.api.PowerNukkitOnly;
import cn.nukkit.api.Since;
import cn.nukkit.entity.EntityAgeable;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;

/**
 * @author Erik Miller | EinBexiii
 */
@Since("1.3.1.0-PN")
@PowerNukkitDifference(since = "1.4.0.0-PN", info = "Implements EntityAgeable only in PowerNukkit!")
public class EntityZoglin extends EntityMob implements EntityAgeable {

    public final static int NETWORK_ID = 126;

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }

    public EntityZoglin(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    protected void initEntity() {
        super.initEntity();
        this.setMaxHealth(40);
    }

    @Override
    public float getWidth() {
        if (this.isBaby()) {
            return 0.85f;
        }
        return 0.9f;
    }

    @Override
    public float getHeight() {
        if (this.isBaby()) {
            return 0.85f;
        }
        return 0.9f;
    }

    @PowerNukkitOnly
    @Since("1.5.1.0-PN")
    @Override
    public String getOriginalName() {
        return "Zoglin";
    }

    @PowerNukkitOnly
    @Override
    public boolean isUndead() {
        return true;
    }

    @PowerNukkitOnly
    @Override
    public boolean isPreventingSleep(Player player) {
        return true;
    }

    @PowerNukkitOnly
    @Override
    public boolean isBaby() {
        return this.getDataFlag(DATA_FLAGS, DATA_FLAG_BABY);
    }

    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    public void setBaby(boolean baby) {
        this.setDataFlag(DATA_FLAGS, DATA_FLAG_BABY, baby);
        this.setScale(baby ? 0.5f : 1.0f);
    }
}
