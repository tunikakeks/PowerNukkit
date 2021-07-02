package cn.nukkit.entity.passive;

import cn.nukkit.Player;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;

import javax.annotation.Nonnull;

public class EntityAxolotl extends EntityAnimal {

    public static final int NETWORK_ID = 130;

    public EntityAxolotl(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Nonnull
    @Override
    public String getName() {
        return "Axolotl";
    }

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }

    @Override
    public boolean playerApplyNameTag(@Nonnull Player player, @Nonnull Item item, boolean consume) {
        return super.playerApplyNameTag(player, item, consume);
    }

    @Override
    public boolean applyNameTag(Item item) {
        return super.applyNameTag(item);
    }

    @Override
    public float getWidth() {
        return .65f;
    }

    @Override
    public float getHeight() {
        return .55f;
    }
}
