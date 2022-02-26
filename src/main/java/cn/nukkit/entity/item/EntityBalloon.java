package cn.nukkit.entity.item;

import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityBalloonable;
import cn.nukkit.entity.EntityCreature;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.entity.EntityDamageEvent.DamageCause;
import cn.nukkit.level.Location;
import cn.nukkit.level.ParticleEffect;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.DoubleTag;
import cn.nukkit.nbt.tag.FloatTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.utils.DyeColor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

/**
 * @author good777LUCKY
 */
public class EntityBalloon extends Entity {

    public static final int NETWORK_ID = 107;
    
    protected Entity balloonAttached;
    protected float balloonMaxHeight;
    protected boolean balloonShouldDrop;

    protected int attachedNetworkId = -1;

    public EntityBalloon(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }
    
    @Override
    public void initEntity() {
        super.initEntity();
        
        this.setMaxHealth(1);
        this.setHealth(1);
        
        this.setDataFlag(DATA_FLAGS, DATA_FLAG_GRAVITY, false);
        if (this.namedTag.contains("Color")) {
            this.dataProperties.putByte(DATA_COLOR, this.namedTag.getByte("Color"));
        } else {
            this.dataProperties.putByte(DATA_COLOR, 0);
        }
        if (this.namedTag.contains("balloon_attached")) {
            this.balloonAttached = this.level.getEntity(this.namedTag.getLong("balloon_attached"));
            if (this.balloonAttached != null) {
                this.dataProperties.putLong(DATA_BALLOON_ATTACHED_ENTITY, this.balloonAttached.getId()); // TODO: Or DATA_LEAD_HOLDER_EID?
                this.setDataFlag(DATA_FLAGS, DATA_FLAG_LEASHED, true);
            }
        } else {
            this.balloonAttached = null; // Not Attached
        }
        if (this.namedTag.contains("balloon_max_height")) {
            this.balloonMaxHeight = this.namedTag.getFloat("balloon_max_height");
        } else {
            this.balloonMaxHeight = 256f;
        }
        if (this.namedTag.contains("balloon_should_drop")) {
            this.balloonShouldDrop = this.namedTag.getBoolean("balloon_should_drop");
        } else {
            balloonShouldDrop = false;
        }
    }
    
    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }
    
    @Override
    public String getName() {
        return "Balloon";
    }
    
    @Override
    public float getWidth() {
        return 0.4f;
    }
    
    @Override
    public float getHeight() {
        return 0.4f;
    }
    
    @Override
    protected float getGravity() {
        return -1.6f; // Should be Lift Force, balloon doesn't have gravity
    }
    
    @Override
    protected float getDrag() {
        return 0.02f; // TODO: Correct value
    }
    
    @Override
    public boolean attack(EntityDamageEvent source) {
        DamageCause cause = source.getCause();
        // TODO: Add Functionality
        if (cause == DamageCause.PROJECTILE) {
            this.close();
            this.level.addParticleEffect(this, ParticleEffect.ENDROD);
            return true;
        }

        if (source instanceof EntityDamageByEntityEvent) {
            Entity damager = ((EntityDamageByEntityEvent) source).getDamager();
            
            double deltaX = this.x - damager.x;
            double deltaZ = this.z - damager.z;
            this.knockBack(damager, source.getDamage(), deltaX, deltaZ, 0.1);
        }
        return true;
    }
    
    @Override
    public void saveNBT() {
        super.saveNBT();
        
        this.namedTag.putByte("Color", this.dataProperties.getByte(DATA_COLOR));
        this.namedTag.putLong("balloon_attached", balloonAttached.getId());
        this.namedTag.putFloat("balloon_max_height", balloonMaxHeight);
        this.namedTag.putBoolean("balloon_should_drop", balloonShouldDrop);
    }
    
    @Override
    public boolean onUpdate(int currentTick) {
        if (this.closed) {
            return false;
        }
        
        int tickDiff = currentTick - this.lastUpdate;
        if (tickDiff <= 0 && !this.justCreated) {
            return true;
        }
        
        this.lastUpdate = currentTick;
        
        this.timing.startTiming();
        
        boolean hasUpdate = this.entityBaseTick(tickDiff);

        if (!this.getLevelBlock().canPassThrough()) {
            this.close();
            this.level.addParticleEffect(this, ParticleEffect.ENDROD);
            return false;
        }

        if (attachedNetworkId != -1 && !isAttached()) {
            if (attachedNetworkId == EntityLeashKnot.NETWORK_ID) {
                this.setDataFlag(DATA_FLAGS, DATA_FLAG_LEASHED, false);
                balloonMaxHeight = 256.0F;
            }

            attachedNetworkId = -1;
            balloonAttached = null;
        }

        attachedNetworkId = isAttached() ? getAttachedEntity().getNetworkId() : -1;
        
        if (this.isAlive()) {
            if (this.y >= this.balloonMaxHeight) {
                if (isLeashed() && this.y < 256.0F) {
                    return true;
                }

                this.close(this.getAttachedEntity() instanceof EntityBalloonable);
                return false;
            }
            
            motionY -= getGravity() * 0.1F / (this.getAttachedEntity() instanceof EntityBalloonable ? ((EntityBalloonable) this.getAttachedEntity()).getBalloonMass() : 1.0F);
            
            move(motionX, motionY, motionZ);

            if (this.getAttachedEntity() instanceof EntityBalloonable) {
                this.getAttachedEntity().move(motionX, motionY, motionZ);
            }
            
            float friction = 1 - getDrag();
            
            // No fiction for X and Z
            motionY *= friction;
            
            updateMovement();
            if (this.getLevelBlock().up().isSolid() && !this.getLevelBlock().up().canPassThrough()) {
                this.close();
                this.level.addParticleEffect(this, ParticleEffect.ENDROD);
            }
            // TODO: Add Functionality
        }
        
        this.timing.stopTiming();
        
        return hasUpdate || !this.onGround || Math.abs(this.motionX) > 0.00001 || Math.abs(this.motionY) > 0.00001 || Math.abs(this.motionZ) > 0.00001;
    }
    
    public void knockBack(Entity attacker, double damage, double x, double z, double base) {
        double f = Math.sqrt(x * x + z * z);
        if (f <= 0) {
            return;
        }
        
        f = 1 / f;
        
        Vector3 motion = new Vector3(this.motionX, this.motionY, this.motionZ);
        
        motion.x /= 2d;
        motion.z /= 2d;
        motion.x += x * f * base;
        motion.z += z * f * base;
        
        this.setMotion(motion);
    }

    public void close(boolean killAttached) {
        super.close();

        if (isAttached()) {
            if (killAttached) {
                this.getAttachedEntity().close();
                return;
            }

            ((EntityCreature) this.getAttachedEntity()).setBalloon(null);
        }
    }

    @Override
    public void close() {
        this.close(isLeashed());
    }

    public Entity getAttachedEntity() {
        return this.balloonAttached;
    }

    public boolean isAttached() {
        return this.balloonAttached != null && !this.balloonAttached.isClosed();
    }

    public boolean isLeashed() {
        return this.balloonAttached instanceof EntityLeashKnot;
    }

    public static EntityBalloon create(@Nonnull Location location, @Nonnull DyeColor color, double maxHeight, boolean dropWhenClosed, @Nullable Entity attachedEntity) {
        CompoundTag nbt = new CompoundTag()
                .putList(new ListTag<DoubleTag>("Pos")
                        .add(new DoubleTag("", location.getX()))
                        .add(new DoubleTag("", location.getY()))
                        .add(new DoubleTag("", location.getZ())))
                .putList(new ListTag<DoubleTag>("Motion")
                        .add(new DoubleTag("", 0))
                        .add(new DoubleTag("", 0))
                        .add(new DoubleTag("", 0)))
                .putList(new ListTag<FloatTag>("Rotation")
                        .add(new FloatTag("", 0))
                        .add(new FloatTag("", 0)))
                .putByte("Color", color.getDyeData() & 0xf)
                .putFloat("balloon_max_height", (float) maxHeight);

        if (attachedEntity != null) {
            nbt.putLong("balloon_attached", attachedEntity.getId());
        }

        return (EntityBalloon) Entity.createEntity("Balloon", Objects.requireNonNull(location.getChunk()), nbt);
    }
}
