package cn.nukkit.item;

import cn.nukkit.Player;
import cn.nukkit.block.BlockCaveVines;
import cn.nukkit.event.player.PlayerItemConsumeEvent;
import cn.nukkit.item.food.Food;
import cn.nukkit.math.Vector3;

/**
 * @author PleaseInsertNameHere
 * @since 01/07/2021
 */
public class ItemGlowBerries extends StringItem {
    public ItemGlowBerries() {
        this(1);
    }

    public ItemGlowBerries(int count) {
        super(MinecraftItemID.GLOW_BERRIES.getNamespacedId(), "Glow Berries");
        this.block = new BlockCaveVines();
        this.count = count;
    }

    @Override
    public boolean onClickAir(Player player, Vector3 directionVector) {
        if (player.getFoodData().getLevel() < player.getFoodData().getMaxLevel() || player.isCreative()) {
            return true;
        }
        player.getFoodData().sendFoodLevel();
        return false;
    }

    @Override
    public boolean onUse(Player player, int ticksUsed) {
        PlayerItemConsumeEvent consumeEvent = new PlayerItemConsumeEvent(player, this);

        player.getServer().getPluginManager().callEvent(consumeEvent);
        if (consumeEvent.isCancelled()) {
            player.getInventory().sendContents(player);
            return false;
        }

        Food food = Food.glow_berries;
        if ((player.isAdventure() || player.isSurvival()) && food.eatenBy(player)) {
            --this.count;
            player.getInventory().setItemInHand(this);
        }
        return true;
    }
}
