package cn.nukkit.entity;

import cn.nukkit.Player;
import cn.nukkit.api.PowerNukkitDifference;
import cn.nukkit.api.PowerNukkitOnly;
import cn.nukkit.api.Since;
import cn.nukkit.entity.item.EntityBalloon;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBalloon;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;

import javax.annotation.Nonnull;

/**
 * @author MagicDroidX (Nukkit Project)
 */
@PowerNukkitDifference(since = "1.4.0.0-PN", info = "Implements EntityNameable only in PowerNukkit")
public abstract class EntityCreature extends EntityLiving implements EntityNameable {
    private EntityBalloon balloon = null;

    public EntityCreature(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    // Armor stands, when implemented, should also check this.
    @Override
    public boolean onInteract(Player player, Item item, Vector3 clickedPos) {
        switch (item.getId()) {
            case Item.NAME_TAG:
                if (!player.isAdventure()) {
                    return applyNameTag(player, item);
                }
                break;
            case Item.BALLOON:
                if (this instanceof EntityBalloonable && item instanceof ItemBalloon) {
                    this.setBalloon(EntityBalloon.create(this.add(0.0D, 1.75D, 0.0D), ((ItemBalloon) item).getDyeColor(), 256.0F, false, this));
                    this.balloon.spawnToAll();
                    return true;
                }
                break;
        }
        return false;
    }

    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    @Override
    public final boolean playerApplyNameTag(@Nonnull Player player, @Nonnull Item item) {
        return applyNameTag(player, item);
    }

    // Structured like this so I can override nametags in player and dragon classes
    // without overriding onInteract.
    @Since("1.4.0.0-PN")
    protected boolean applyNameTag(@Nonnull Player player, @Nonnull Item item){
        // The code was moved to the default block of that interface
        return EntityNameable.super.playerApplyNameTag(player, item);
    }

    public EntityBalloon getBalloon() {
        return this instanceof EntityBalloonable ? balloon : null;
    }

    public void setBalloon(EntityBalloon balloon) {
        if (this instanceof EntityBalloonable) {
            this.balloon = balloon;
        }
    }
}
