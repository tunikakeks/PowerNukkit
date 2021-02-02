package cn.nukkit.item.enchantment.crossbow;

import cn.nukkit.item.enchantment.Enchantment;

/**
 * @author GoodLucky777
 */
public class EnchantmentCrossbowQuickCharge extends EnchantmentCrossbow {

    public EnchantmentCrossbowQuickCharge() {
        super(Enchantment.ID_CROSSBOW_QUICK_CHARGE, "quickCharge", 5);
    }
    
    @Override
    public int getMinEnchantAbility(int level) {
        return 12 + (level - 1) * 20;
    }
    
    @Override
    public int getMaxEnchantAbility(int level) {
        return 50;
    }
    
    @Override
    public int getMaxLevel() {
        return 3;
    }
}
