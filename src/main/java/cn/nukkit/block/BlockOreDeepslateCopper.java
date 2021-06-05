package cn.nukkit.block;

import cn.nukkit.item.Item;
import cn.nukkit.item.ItemTool;
import cn.nukkit.item.enchantment.Enchantment;

import java.util.concurrent.ThreadLocalRandom;

import static cn.nukkit.item.ItemID.RAW_COPPER;

/**
 * @copied_from MagicDroidX, BlockOreLapis (Nukkit Project)
 */
public class BlockOreDeepslateCopper extends BlockOre {

    public BlockOreDeepslateCopper() {
    }

    @Override
    public int getId() {
        return DEEPSLATE_COPPER_ORE;
    }

    @Override
    public double getHardness() {
        return 4.5;
    }

    @Override
    public double getResistance() {
        return 15;
    }

    @Override
    public int getToolType() {
        return ItemTool.TYPE_PICKAXE;
    }

    @Override
    public int getToolTier() {
        return ItemTool.TIER_STONE;
    }

    @Override
    public Item[] getDrops(Item item) {
        if (item.isPickaxe() && item.getTier() >= getToolTier()) {
            ThreadLocalRandom random = ThreadLocalRandom.current();
            int count = 2 + random.nextInt(2);
            Enchantment fortune = item.getEnchantment(Enchantment.ID_FORTUNE_DIGGING);
            if (fortune != null && fortune.getLevel() >= 1) {
                int i = random.nextInt(fortune.getLevel() + 2) - 1;

                if (i < 0) {
                    i = 0;
                }

                count *= (i + 1);
            }

            return new Item[]{
                    Item.get(RAW_COPPER, 0, count)
            };
        } else {
            return Item.EMPTY_ARRAY;
        }
    }

    @Override
    public String getName() {
        return "Deepslate Copper Ore";
    }

    @Override
    public boolean canHarvestWithHand() {
        return false;
    }
}
