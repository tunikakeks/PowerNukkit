package cn.nukkit.event.entity;

import cn.nukkit.block.Block;
import cn.nukkit.entity.weather.EntityLightning;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import lombok.Getter;
import lombok.Setter;

public class LightningDistractEvent extends EntityEvent implements Cancellable {

    public static final HandlerList handlers = new HandlerList();

    public static HandlerList getHandlers() {
        return handlers;
    }

    @Getter
    private EntityLightning lightning;

    @Getter
    @Setter
    private Block distractionBlock;

    public LightningDistractEvent(EntityLightning lightning, Block block) {
        this.lightning = lightning;
        this.distractionBlock = block;
    }

    @Override
    public EntityLightning getEntity() {
        return (EntityLightning) super.getEntity();
    }
}
