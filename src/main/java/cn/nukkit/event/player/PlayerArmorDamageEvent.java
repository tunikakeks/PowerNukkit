package cn.nukkit.event.player;

import cn.nukkit.Player;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.item.Item;

public class PlayerArmorDamageEvent extends PlayerItemDamageEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    public static HandlerList getHandlers() {
        return handlers;
    }

    private boolean cancelled;

    public PlayerArmorDamageEvent(Player player, Item before, Item after) {
        super(player, before, after);
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled() {
        this.setCancelled(true);
    }

    @Override
    public void setCancelled(boolean forceCancel) {
        this.cancelled = forceCancel;
    }
}
