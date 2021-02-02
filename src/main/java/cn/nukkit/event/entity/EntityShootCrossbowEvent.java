package cn.nukkit.event.entity;

import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityLiving;
import cn.nukkit.entity.projectile.EntityProjectile;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.item.Item;

/**
 * @author GoodLucky777
 */
public class EntityShootCrossbowEvent extends EntityEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    
    public static HandlerList getHandlers() {
        return handlers;
    }
    
    private final Item crossbow;
    
    private EntityProjectile projectile;
    
    public EntityShootCrossbowEvent(EntityLiving shooter, Item crossbow, EntityProjectile projectile) {
        this.entity = shooter;
        this.crossbow = crossbow;
        this.projectile = projectile;
    }
    
    @Override
    public EntityLiving getEntity() {
        return (EntityLiving) this.entity;
    }
    
    public Item getCrossbow() {
        return this.crossbow;
    }
    
    public EntityProjectile getProjectile() {
        return this.projectile;
    }
    
    public void setProjectile(Entity projectile) {
        if (projectile != this.projectile) {
            if (this.projectile.getViewers().size() == 0) {
                this.projectile.kill();
                this.projectile.close();
            }
            this.projectile = (EntityProjectile) projectile;
        }
    }
}
