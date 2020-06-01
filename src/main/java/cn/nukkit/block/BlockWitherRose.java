package cn.nukkit.block;

import cn.nukkit.entity.Entity;
import cn.nukkit.entity.impl.EntityLiving;
import cn.nukkit.item.Item;
import cn.nukkit.math.AxisAlignedBB;
import cn.nukkit.player.Player;
import cn.nukkit.potion.Effect;
import cn.nukkit.utils.Identifier;

public class BlockWitherRose extends BlockFlower {

    public BlockWitherRose(Identifier id) {
        super(id);
    }

    @Override
    public boolean canPlantOn(Identifier id) {
        return super.canPlantOn(id) || id == BlockIds.NETHERRACK || id == BlockIds.SOUL_SAND;
    }

    @Override
    public boolean onActivate(Item item) {
        return false;
    }

    @Override
    public void onEntityCollide(Entity entity) {
        if (this.getLevel().getServer().getDifficulty() != 0 && entity instanceof EntityLiving) {
            EntityLiving living = (EntityLiving) entity;
            if (!living.invulnerable && !living.hasEffect(Effect.WITHER)
                    && (!(living instanceof Player) || !((Player) living).isCreative() && !((Player) living).isSpectator())) {
                Effect effect = Effect.getEffect(Effect.WITHER);
                effect.setDuration(40);
                effect.setAmplifier(1);
                living.addEffect(effect);
            }
        }
    }

    @Override
    protected AxisAlignedBB recalculateCollisionBoundingBox() {
        return this;
    }

    @Override
    public boolean hasEntityCollision() {
        return true;
    }
}
