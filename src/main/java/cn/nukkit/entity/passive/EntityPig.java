package cn.nukkit.entity.passive;

import cn.nukkit.api.PowerNukkitOnly;
import cn.nukkit.api.Since;
import cn.nukkit.entity.EntityBalloonable;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;

/**
 * @author BeYkeRYkt (Nukkit Project)
 */
public class EntityPig extends EntityAnimal implements EntityBalloonable {

    public static final int NETWORK_ID = 12;

    public EntityPig(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    public float getWidth() {
        if (this.isBaby()) {
            return 0.45f;
        }
        return 0.9f;
    }

    @Override
    public float getHeight() {
        if (this.isBaby()) {
            return 0.45f;
        }
        return 0.9f;
    }

    @Override
    public void initEntity() {
        super.initEntity();
        this.setMaxHealth(10);
    }

    @PowerNukkitOnly
    @Since("1.5.1.0-PN")
    @Override
    public String getOriginalName() {
        return "Pig";
    }

    @Override
    public Item[] getDrops() {
        return new Item[]{Item.get(((this.isOnFire()) ? Item.COOKED_PORKCHOP : Item.RAW_PORKCHOP))};
    }

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }

    @Override
    public boolean isBreedingItem(Item item) {
        int id = item.getId();

        return id == Item.CARROT || id == Item.POTATO || id == Item.BEETROOT;
    }

    @Override
    public float getBalloonMass() {
        return 0.75F;
    }
}
