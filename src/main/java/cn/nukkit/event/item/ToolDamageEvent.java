package cn.nukkit.event.item;

import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.item.Item;

public class ToolDamageEvent extends ItemDamageEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    public static HandlerList getHandlers() {
        return handlers;
    }

    private boolean cancelled;

    public ToolDamageEvent(Item before, Item after) {
        super(before, after);
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
