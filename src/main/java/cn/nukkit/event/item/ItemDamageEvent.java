package cn.nukkit.event.item;

import cn.nukkit.item.Item;

public abstract class ItemDamageEvent extends ItemEvent {

    protected Item after;

    public ItemDamageEvent(Item before, Item after) {
        this.item = before;
        this.after = after;
    }

    public Item getBefore() {
        return item;
    }

    public Item getAfter() {
        return after;
    }
}
