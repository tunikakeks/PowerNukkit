package cn.nukkit.event.player;

import cn.nukkit.Player;
import cn.nukkit.event.Event;
import cn.nukkit.item.Item;

public abstract class PlayerItemDamageEvent extends Event {

    protected Player player;
    protected Item before;
    protected Item after;

    public PlayerItemDamageEvent(Player player, Item before, Item after) {
        this.player = player;
        this.before = before;
        this.after = after;
    }

    public Player getPlayer() {
        return player;
    }

    public Item getBefore() {
        return before;
    }

    public Item getAfter() {
        return after;
    }
}
