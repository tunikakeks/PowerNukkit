package cn.nukkit.dispenser;

import cn.nukkit.api.PowerNukkitDifference;
import cn.nukkit.api.PowerNukkitOnly;
import cn.nukkit.block.BlockDispenser;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.projectile.EntityProjectile;
import cn.nukkit.item.Item;
import cn.nukkit.level.Sound;
import cn.nukkit.math.BlockFace;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;

/**
 * @author CreeperFace
 */
public class ProjectileDispenseBehavior extends DefaultDispenseBehavior {

    private final String entityType;

    public ProjectileDispenseBehavior(String entity) {
        this.entityType = entity;
    }

    @Override
    @PowerNukkitDifference(info = "Implement sound.", since = "1.4.0.0-PN")
    public @PowerNukkitOnly Item dispense(BlockDispenser source, BlockFace face, Item item) {
        Vector3 dispensePos = source.getDispensePosition();

        CompoundTag nbt = Entity.getDefaultNBT(dispensePos);
        this.correctNBT(nbt);

        Entity projectile = Entity.createEntity(getEntityType(), source.level.getChunk(dispensePos.getChunkX(), dispensePos.getChunkZ()), nbt);

        if (!(projectile instanceof EntityProjectile)) {
            return super.dispense(source, face, item);
        }

        Vector3 motion = new Vector3(face.getXOffset(), face.getYOffset() + 0.1f, face.getZOffset())
                .normalize();

        projectile.setMotion(motion);
        ((EntityProjectile) projectile).inaccurate(getAccuracy());
        projectile.setMotion(projectile.getMotion().multiply(getMotion()));

        ((EntityProjectile) projectile).updateRotation();

        projectile.spawnToAll();

        source.level.addSound(source, Sound.RANDOM_BOW);
        return null;
    }

    @PowerNukkitOnly
    protected double getMotion() {
        return 1.1;
    }

    @PowerNukkitOnly
    protected float getAccuracy() {
        return 6;
    }

    protected String getEntityType() {
        return this.entityType;
    }

    /**
     * you can add extra data of projectile here
     *
     * @param nbt tag
     */
    protected void correctNBT(CompoundTag nbt) {

    }
}
