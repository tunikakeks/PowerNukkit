package cn.nukkit.item.enchantment;

import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityLiving;

/**
 * @author MagicDroidX (Nukkit Project)
 */
public class EnchantmentKnockback extends Enchantment {
    protected EnchantmentKnockback() {
        super(ID_KNOCKBACK, "knockback", Rarity.UNCOMMON, EnchantmentType.SWORD);
    }

    @Override
    public int getMinEnchantAbility(int level) {
        return 5 + (level - 1) * 20;
    }

    @Override
    public int getMaxEnchantAbility(int level) {
        return super.getMinEnchantAbility(level) + 50;
    }

    @Override
    public int getMaxLevel() {
        return 2;
    }

    @Override
    public void doPostAttack(Entity attacker, Entity entity) {
        if(entity instanceof EntityLiving) {
            double deltaX = entity.x - attacker.x;
            double deltaZ = entity.z - attacker.z;
            ((EntityLiving) entity).knockBack(attacker, 0, deltaX, deltaZ, 0.23 * level);
        }
    }
}
