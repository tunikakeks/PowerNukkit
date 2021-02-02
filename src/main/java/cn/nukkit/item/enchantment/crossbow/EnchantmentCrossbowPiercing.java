package cn.nukkit.item.enchantment.crossbow;

import cn.nukkit.item.enchantment.Enchantment;

/**
 * @author GoodLucky777
 */
public class EnchantmentCrossbowPiercing extends EnchantmentCrossbow {

    public EnchantmentCrossbowPiercing() {
        super(Enchantment.ID_CROSSBOW_PIERCING, "piercing", 4);
    }
    
    @Override
    public int getMinEnchantAbility(int level) {
        return 1 + (level - 1) * 10;
    }
    
    @Override
    public int getMaxEnchantAbility(int level) {
        return 50;
    }
    
    @Override
    public int getMaxLevel() {
        return 4;
    }
    
    @Override
    public boolean isCompatibleWith(Enchantment enchantment) {
        return super.isCompatibleWith(enchantment) && enchantment.id != Enchantment.ID_CROSSBOW_MULTISHOT;
    }
}
