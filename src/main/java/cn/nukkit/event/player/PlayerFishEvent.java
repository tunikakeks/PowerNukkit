package cn.nukkit.event.player;

import cn.nukkit.Player;
import cn.nukkit.event.Event;
import cn.nukkit.event.HandlerList;

public class PlayerFishEvent extends Event {
    private static HandlerList handlers = new HandlerList();
    private Player player;

    public static HandlerList getHandlers() {
        return handlers;
    }
    public PlayerFishEvent(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }
}
