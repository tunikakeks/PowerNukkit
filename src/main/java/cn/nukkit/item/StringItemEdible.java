package cn.nukkit.item;

import cn.nukkit.Player;
import cn.nukkit.event.player.PlayerItemConsumeEvent;
import cn.nukkit.item.food.Food;
import cn.nukkit.level.Sound;
import cn.nukkit.math.Vector3;
import cn.nukkit.network.protocol.CompletedUsingItemPacket;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class StringItemEdible extends StringItem {

    public StringItemEdible(@Nonnull String id, @Nullable String name) {
        super(id, name);
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
        if (player.isSpectator()) {
            player.getInventory().sendContents(player);
            return false;
        }

        Food food = Food.getByRelative(this);

        if (food == null || ticksUsed < food.getEatingTick()) {
            return false;
        }

        PlayerItemConsumeEvent consumeEvent = new PlayerItemConsumeEvent(player, this);

        player.getServer().getPluginManager().callEvent(consumeEvent);
        if (consumeEvent.isCancelled()) {
            player.getInventory().sendContents(player);
            return false;
        }

        if (food.eatenBy(player)) {
            player.completeUsingItem(this.getNetworkId(), CompletedUsingItemPacket.ACTION_EAT);

            if (player.isAdventure() || player.isSurvival()) {
                --this.count;
                player.getInventory().setItemInHand(this);

                player.getLevel().addSound(player, Sound.RANDOM_BURP);
            }
        }

        return true;
    }
}
