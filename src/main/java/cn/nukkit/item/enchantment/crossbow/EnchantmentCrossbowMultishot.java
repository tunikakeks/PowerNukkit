package cn.nukkit.item.enchantment.crossbow;

import cn.nukkit.item.enchantment.Enchantment;

/**
 * @author GoodLucky777
 */
public class EnchantmentCrossbowMultishot extends EnchantmentCrossbow {

    public EnchantmentCrossbowMultishot() {
        super(Enchantment.ID_CROSSBOW_MULTISHOT, "multishot", 1);
    }
    
    @Override
    public int getMinEnchantAbility(int level) {
        return 20;
    }
    
    @Override
    public int getMaxEnchantAbility(int level) {
        return 50;
    }
    
    @Override
    public int getMaxLevel() {
        return 1;
    }
    
    @Override
    public boolean isCompatibleWith(Enchantment enchantment) {
        return super.isCompatibleWith(enchantment) && enchantment.id != Enchantment.ID_CROSSBOW_PIERCING;
    }
}
