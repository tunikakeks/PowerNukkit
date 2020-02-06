package cn.nukkit.block;

import cn.nukkit.AdventureSettings;
import cn.nukkit.entity.Entity;
import cn.nukkit.level.Sound;
import cn.nukkit.math.AxisAlignedBB;
import cn.nukkit.math.SimpleAxisAlignedBB;
import cn.nukkit.math.Vector3f;
import cn.nukkit.player.Player;
import cn.nukkit.utils.Identifier;

import java.util.Random;

public class BlockHoney extends BlockSolid {
    private static final Random RANDOM = new Random();

    public BlockHoney(Identifier id) {
        super(id);
    }

    @Override
    public double getHardness() {
        return 0;
    }

    @Override
    public double getResistance() {
        return 0;
    }

    @Override
    public boolean hasEntityCollision() {
        return true;
    }

    @Override
    public void onEntityCollide(Entity entity) {
        if (!entity.isOnGround() && entity.getMotion().y <= 0.08 &&
                (!(entity instanceof Player)
                        || !((Player) entity).getAdventureSettings().get(AdventureSettings.Type.FLYING))) {
            double ex = Math.abs(x + 0.5D - entity.getX());
            double ez = Math.abs(z + 0.5D - entity.getZ());
            double width = 0.4375D + (double)(entity.getWidth() / 2.0F);
            if (ex + 1.0E-3D > width || ez + 1.0E-3D > width) {
                Vector3f motion = entity.getMotion();
                motion.y = -0.05;
                if (entity.getMotion().y < -0.13) {
                    double m = -0.05 / entity.getMotion().y;
                    motion.x *= m;
                    motion.z *= m;
                }

                if (!entity.getMotion().equals(motion)) {
                    entity.setMotion(motion);
                }
                entity.resetFallDistance();

                if (RANDOM.nextInt(10) == 0) {
                    level.addSound((Vector3f) entity, Sound.LAND_SLIME);
                }
            }
        }
    }

    @Override
    protected AxisAlignedBB recalculateCollisionBoundingBox() {
        return new SimpleAxisAlignedBB(x, y, z, x+1, y+1, z+1);
    }

    @Override
    public double getMinX() {
        return x + 0.1;
    }

    @Override
    public double getMaxX() {
        return x + 0.9;
    }

    @Override
    public double getMinZ() {
        return z + 0.1;
    }

    @Override
    public double getMaxZ() {
        return z + 0.9;
    }
}
