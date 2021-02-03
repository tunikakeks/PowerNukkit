package cn.nukkit.item;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.item.EntityFirework;
import cn.nukkit.entity.projectile.EntityArrow;
import cn.nukkit.entity.projectile.EntityProjectile;
import cn.nukkit.event.entity.EntityShootCrossbowEvent;
import cn.nukkit.event.entity.ProjectileLaunchEvent;
import cn.nukkit.inventory.Inventory;
import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.level.Sound;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.DoubleTag;
import cn.nukkit.nbt.tag.FloatTag;
import cn.nukkit.nbt.tag.ListTag;

import javax.annotation.Nonnull;

/**
 * @author GoodLucky777
 */
public class ItemCrossbow extends ItemTool {

    public ItemCrossbow() {
        this(0, 1);
    }
    
    public ItemCrossbow(Integer meta) {
        this(meta, 1);
    }
    
    public ItemCrossbow(Integer meta, int count) {
        super(CROSSBOW, meta, count, "Crossbow");
    }
    
    @Override
    public int getMaxDurability() {
        return ItemTool.DURABILITY_CROSSBOW;
    }
    
    @Override
    public boolean onUse(Player player, int ticksUsed) {
        if (ticksUsed < getChargeTick()) {
            return true;
        }
        
        if (isLoaded()) {
            return true;
        }
        
        Item shootableItem = findShootableItem(player);
        if (!player.isCreative()) {
            if (shootableItem == null) {
                player.getOffhandInventory().sendContents(player);
                player.getInventory().sendContents(player);
                return true;
            }
            
            shootableItem.count--;
            player.getOffhandInventory().sendContents(player);
            player.getInventory().sendContents(player);
        } else {
            if (shootableItem == null) {
                shootableItem = Item.get(ItemID.ARROW, 0, 1);
            }
        }
        
        loadProjectile(player, shootableItem);
        player.getLevel().addSound(player, Sound.CROSSBOW_LOADING_END);
        return true;
    }
    
    @Override
    public boolean onClickAir(Player player, Vector3 directionVector) {
        return !shoot(player);
    }
    
    @Override
    public boolean onRelease(Player player, int ticksUsed) {
        return true;
    }
    
    public int getChargeTick() {
        int quickChargeLevel = this.getEnchantmentLevel(Enchantment.ID_CROSSBOW_QUICK_CHARGE);
        return 25 - (quickChargeLevel == 0 ? 0 : quickChargeLevel * 5);
    }
    
    public Item findShootableItem(@Nonnull Player player) {
        int firstSlot = player.getOffhandInventory().first(Item.get(ItemID.ARROW, 0, 1), false);
        if (firstSlot != -1) {
            return player.getOffhandInventory().getItem(firstSlot);
        }
        
        firstSlot = player.getInventory().first(Item.get(ItemID.ARROW, 0, 1), false);
        if (firstSlot != -1) {
            return player.getInventory().getItem(firstSlot);
        }
        
        return null;
    }
    
    public boolean isLoaded() {
        if (!this.hasCompoundTag()) {
            return false;
        }
        
        if (!this.getNamedTag().contains("chargedItem")) {
            return false;
        }
        
        return true;
    }
    
    public void clearProjectile(@Nonnull Player player) {
        
        player.getInventory().setItemInHand(this);
    }
    
    public Item getProjectile() {
        if (!isLoaded()) {
            return null;
        }
        
        
    }
    
    public void loadProjectile(@Nonnull Player player, @Nonnull Item projectile) {
        String namedspaceId = projectile.getNamespaceId();
        if (namedspaceId == null) {
            return;
        }
        
        this.setCompoundTag(new CompoundTag("")
            .putCompound("chargedItem", new CompoundTag("chargedItem")
                .putString("Name", namedspaceId)
                .putShort("Damage", projectile.getDamage())
                .putByte("Count", projectile.getCount())));
        
        player.getInventory().setItemInHand(this);
    }
    
    public boolean shoot(@Nonnull Player player) {
        if (!isLoaded()) {
            return false;
        }
        
        Item projectileItem = getProjectile();
        if (projectileItem == null) {
            return false;
        }
        
        EntityProjectile[] projectiles;
        for (int i = 0; i < projectileItem.getCount(); i++) {
            if (projectileItem.getId() == ItemID.FIREWORKS) {
                
                projectiles[i] = (EntityProjectile) Entity.createEntity("Firework", player.chunk, nbt, player);
            } else {
                float yaw = 0;
                if (i == 0) {
                    yaw = 0;
                } else if (i == 1) {
                    yaw = -10f;
                } else if (i == 2) {
                    yaw = 10f;
                }
                
                CompoundTag nbt = new CompoundTag()
                    .putList(new ListTag<DoubleTag>("Pos")
                        .add(new DoubleTag("", player.x))
                        .add(new DoubleTag("", player.y + player.getEyeHeight()))
                        .add(new DoubleTag("", player.z)))
                    .putList(new ListTag<DoubleTag>("Motion")
                        .add(new DoubleTag("", -Math.sin(player.yaw / 180 * Math.PI) * Math.cos(player.pitch / 180 * Math.PI)))
                        .add(new DoubleTag("", -Math.sin(player.pitch / 180 * Math.PI)))
                        .add(new DoubleTag("", Math.cos(player.yaw / 180 * Math.PI) * Math.cos(player.pitch / 180 * Math.PI))))
                    .putList(new ListTag<FloatTag>("Rotation")
                        .add(new FloatTag("", (player.yaw > 180 ? 360 : 0) - (float) player.yaw + yaw))
                        .add(new FloatTag("", (float) -player.pitch)));
                
                if (player.isCreative() || yaw != 0) {
                    nbt.putByte("pickup", EntityArrow.PICKUP_CREATIVE);
                }
                
                projectiles[i] = (EntityProjectile) Entity.createEntity("Arrow", player.chunk, nbt, player);
            }
        }
        
        EntityShootCrossbowEvent entityShootCrossbowEvent = new EntityShootCrossbowEvent(player, this, projectiles);
        Server.getInstance().getPluginManager().callEvent(entityShootCrossbowEvent);
        if (entityShootCrossbowEvent.isCancelled()) {
            player.getInventory().sendContents(player);
            player.getOffhandInventory().sendContents(player);
            return false;
        }
        
        for (int i = 0; i < entityShootCrossbowEvent.getProjectilesCount(); i++) {
            launchProjectile(entityShootCrossbowEvent.getProjectile(i));
        }
        return true;
    }
    
    public boolean launchProjectile(@Nonnull EntityProjectile projectile) {
        ProjectileLaunchEvent projectileLaunchEvent = new ProjectileLaunchEvent(projectile);
        Server.getInstance().getPluginManager().callEvent(projectileLaunchEvent);
        if (projectileLaunchEvent.isCancelled()) {
            projectile.kill();
            return false;
        }
        
        projectile.spawnToAll();
        return true;
    }
}
