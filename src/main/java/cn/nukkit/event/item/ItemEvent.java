package cn.nukkit.event.item;

import cn.nukkit.event.Event;
import cn.nukkit.item.Item;

public abstract class ItemEvent extends Event {

    protected Item item;

    public Item getItem() {
        return item;
    }
}
