package cn.nukkit.item.randomitem;

import cn.nukkit.api.Since;
import cn.nukkit.block.BlockID;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemID;
import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.math.NukkitMath;
import cn.nukkit.potion.Potion;

import static cn.nukkit.item.randomitem.RandomItem.*;

/**
 * @author Snake1999
 * @since 2016/1/15
 */
public final class Fishing {

    public static final Selector ROOT_FISHING = putSelector(new Selector(ROOT));

    public static final Selector FISHES = putSelector(new Selector(ROOT_FISHING), 0.85F);
    public static final Selector TREASURES = putSelector(new Selector(ROOT_FISHING), 0.05F);
    public static final Selector JUNKS = putSelector(new Selector(ROOT_FISHING), 0.1F);
    public static final Selector FISH = putSelector(new ConstantItemSelector(Item.RAW_FISH, FISHES), 0.6F);
    public static final Selector SALMON = putSelector(new ConstantItemSelector(Item.RAW_SALMON, FISHES), 0.25F);
    public static final Selector CLOWNFISH = putSelector(new ConstantItemSelector(Item.CLOWNFISH, FISHES), 0.02F);
    public static final Selector PUFFERFISH = putSelector(new ConstantItemSelector(Item.PUFFERFISH, FISHES), 0.13F);
    public static final Selector TREASURE_BOW = putSelector(new ConstantItemWithRandomEnchantmentSelector(Item.BOW, new Enchantment[] {Enchantment.getEnchantment(Enchantment.ID_BOW_POWER), Enchantment.getEnchantment(Enchantment.ID_BOW_KNOCKBACK), Enchantment.getEnchantment(Enchantment.ID_BOW_INFINITY), Enchantment.getEnchantment(Enchantment.ID_BOW_FLAME), Enchantment.getEnchantment(Enchantment.ID_DURABILITY)}, 3, false, true, TREASURES), 0.1667F);
    public static final Selector TREASURE_ENCHANTED_BOOK;
    public static final Selector TREASURE_FISHING_ROD = putSelector(new ConstantItemWithRandomEnchantmentSelector(Item.FISHING_ROD, new Enchantment[] {Enchantment.getEnchantment(Enchantment.ID_FORTUNE_FISHING), Enchantment.getEnchantment(Enchantment.ID_LURE), Enchantment.getEnchantment(Enchantment.ID_DURABILITY), Enchantment.getEnchantment(Enchantment.ID_MENDING)}, 3, false, true, TREASURES), 0.1667F);
    public static final Selector TREASURE_NAME_TAG = putSelector(new ConstantItemSelector(Item.NAME_TAG, TREASURES), 0.1667F);
    public static final Selector TREASURE_SADDLE = putSelector(new ConstantItemSelector(Item.SADDLE, TREASURES), 0.1667F);
    public static final Selector TREASURE_NAUTILUS_SHELL = putSelector(new ConstantItemSelector(Item.NAUTILUS_SHELL, TREASURES), 0.1667F);
    public static final Selector JUNK_BOWL = putSelector(new ConstantItemSelector(Item.BOWL, JUNKS), 0.12F);
    public static final Selector JUNK_FISHING_ROD = putSelector(new ConstantItemSelector(Item.FISHING_ROD, JUNKS), 0.024F);
    public static final Selector JUNK_LEATHER = putSelector(new ConstantItemSelector(Item.LEATHER, JUNKS), 0.12F);
    public static final Selector JUNK_LEATHER_BOOTS = putSelector(new ConstantItemSelector(Item.LEATHER_BOOTS, JUNKS), 0.12F);
    public static final Selector JUNK_ROTTEN_FLESH = putSelector(new ConstantItemSelector(Item.ROTTEN_FLESH, JUNKS), 0.12F);
    public static final Selector JUNK_STICK = putSelector(new ConstantItemSelector(Item.STICK, JUNKS), 0.06F);
    public static final Selector JUNK_STRING_ITEM = putSelector(new ConstantItemSelector(Item.STRING, JUNKS), 0.06F);
    public static final Selector JUNK_WATTER_BOTTLE = putSelector(new ConstantItemSelector(Item.POTION, Potion.NO_EFFECTS, JUNKS), 0.12F);
    public static final Selector JUNK_BONE = putSelector(new ConstantItemSelector(Item.BONE, JUNKS), 0.12F);
    public static final Selector JUNK_TRIPWIRE_HOOK = putSelector(new ConstantItemSelector(Item.TRIPWIRE_HOOK, JUNKS), 0.12F);

    public static Item getFishingResult(Item rod) {
        int fortuneLevel = 0;
        int lureLevel = 0;
        if (rod != null) {
            fortuneLevel = rod.getEnchantmentLevel(Enchantment.ID_FORTUNE_FISHING);
            lureLevel = rod.getEnchantmentLevel(Enchantment.ID_LURE);
        }
        return getFishingResult(fortuneLevel, lureLevel);
    }

    public static Item getFishingResult(int fortuneLevel, int lureLevel) {
        float treasureChance = NukkitMath.clamp(0.05f + 0.01f * fortuneLevel - 0.01f * lureLevel, 0, 1);
        float junkChance = NukkitMath.clamp(0.05f - 0.025f * fortuneLevel - 0.01f * lureLevel, 0, 1);
        float fishChance = NukkitMath.clamp(1 - treasureChance - junkChance, 0, 1);
        putSelector(FISHES, fishChance);
        putSelector(TREASURES, treasureChance);
        putSelector(JUNKS, junkChance);
        Object result = selectFrom(ROOT_FISHING);
        return (Item) result;
    }

    static {
        Enchantment[] enchantments = new Enchantment[34];
        for(int i = 0; i < enchantments.length; i++) {
            if(i == 33) enchantments[i] = Enchantment.getEnchantment(36);
            enchantments[i] = Enchantment.getEnchantment(i);
        }
        TREASURE_ENCHANTED_BOOK = putSelector(new ConstantItemWithRandomEnchantmentSelector(Item.ENCHANTED_BOOK, enchantments, 1, true, TREASURES),  0.1667F);
    }
}
