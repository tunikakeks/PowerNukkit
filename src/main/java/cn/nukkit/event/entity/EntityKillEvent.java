package cn.nukkit.event.entity;

import cn.nukkit.entity.Entity;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.Event;
import cn.nukkit.event.HandlerList;

public class EntityKillEvent extends Event implements Cancellable {

    private static HandlerList handlers = new HandlerList();
    private Entity entity;

    public EntityKillEvent(Entity entity) {
        this.entity = entity;
    }

    public static HandlerList getHandlers() {
        return handlers;
    }

    public Entity getEntity() {
        return entity;
    }
}
