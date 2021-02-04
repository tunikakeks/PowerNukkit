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
import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.DoubleTag;
import cn.nukkit.nbt.tag.FloatTag;
import cn.nukkit.nbt.tag.ListTag;

import javax.annotation.Nonnull;

/**
 * @author GoodLucky777
 */
public class ItemCrossbow extends ItemTool {

    private boolean hasStartLoaded = false;
    private boolean hasMiddleLoaded = false;
    
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
        if (ticksUsed >= getChargeTick()) {
            return false;
        }
        
        boolean quickCharge = this.hasEnchantment(Enchantment.ID_CROSSBOW_QUICK_CHARGE);
        if (!hasStartLoaded) {
            player.getLevel().addSound(player, quickCharge ? Sound.CROSSBOW_QUICK_CHARGE_START : Sound.CROSSBOW_LOADING_START);
        } else if (!hasMiddleLoaded) {
            player.getLevel().addSound(player, quickCharge ? Sound.CROSSBOW_QUICK_CHARGE_MIDDLE : Sound.CROSSBOW_LOADING_MIDDLE);
        }
        
        return true;
    }
    
    @Override
    public boolean onClickAir(Player player, Vector3 directionVector) {
        return !shoot(player);
    }
    
    @Override
    public boolean onRelease(Player player, int ticksUsed) {
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
        player.getLevel().addSound(player, this.hasEnchantment(Enchantment.ID_CROSSBOW_QUICK_CHARGE) ? Sound.CROSSBOW_QUICK_CHARGE_END : Sound.CROSSBOW_LOADING_END);
        
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
    
    public void clearProjectiles(@Nonnull Player player) {
        if (this.getNamedTag().contains("chargedItem")) {
            this.getNamedTag().remove("chargedItem");
            player.getInventory().setItemInHand(this);
        }
    }
    
    public Item getProjectile() {
        if (!isLoaded()) {
            return null;
        }
        
        CompoundTag tag = this.getNamedTag().getCompound("chargedItem");
        Item projectile = RuntimeItems.getRuntimeMapping().getItemByNamespaceId(tag.getString("Name"), tag.getByte("Count"));
        projectile.setDamage(tag.getShort("Damage"));
        if (tag.contains("tag")) {
            projectile.setCompoundTag(tag.getCompound("tag"));
        }
        
        return projectile;
    }
    
    public void loadProjectile(@Nonnull Player player, @Nonnull Item projectile) {
        String namedspaceId = projectile.getNamespaceId();
        if (namedspaceId == null) {
            return;
        }
        
        CompoundTag tag = new CompoundTag("")
            .putCompound("chargedItem", new CompoundTag("chargedItem")
                .putString("Name", namedspaceId)
                .putShort("Damage", projectile.getDamage())
                .putByte("Count", projectile.getCount()));
        
        if (projectile.hasCompoundTag()) {
            tag.get("chargedItem").put("tag", projectile.getNamedTag());
        }
        
        this.setCompoundTag(tag);
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
        EntityProjectile tempProjectile;
        for (int i = 0; i < projectileItem.getCount(); i++) {
            if (projectileItem.getId() == ItemID.FIREWORKS) {
                CompoundTag nbt = new CompoundTag()
                    .putList(new ListTag<DoubleTag>("Pos")
                        .add(new DoubleTag("", player.x))
                        .add(new DoubleTag("", player.getEyeHeight() - 0.15))
                        .add(new DoubleTag("", player.z)))
                    .putList(new ListTag<DoubleTag>("Motion")
                        .add(new DoubleTag("", -Math.sin(player.yaw / 180 * Math.PI) * Math.cos(player.pitch / 180 * Math.PI)))
                        .add(new DoubleTag("", -Math.sin(player.pitch / 180 * Math.PI)))
                        .add(new DoubleTag("", Math.cos(player.yaw / 180 * Math.PI) * Math.cos(player.pitch / 180 * Math.PI))))
                    .putList(new ListTag<FloatTag>("Rotation")
                        .add(new FloatTag("", (player.yaw > 180 ? 360 : 0) - (float) player.yaw))
                        .add(new FloatTag("", (float) -player.pitch)));
                
                if (projectileItem.hasCompoundTag()) {
                    nbt.putCompound("FireworkItem", NBTIO.putItemHelper(this));
                }
                
                tempProjectile = (EntityProjectile) Entity.createEntity("Firework", player.chunk, nbt, player);
                
                if (tempProjectile == null) {
                    continue;
                }
                
                projectiles[i] = tempProjectile;
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
                
                tempProjectile = (EntityProjectile) Entity.createEntity("Arrow", player.chunk, nbt, player);
                
                if (tempProjectile == null) {
                    continue;
                }
                
                ((EntityArrow) tempProjectile).setCritical(true);
                if (player.isCreative() || yaw != 0) {
                    ((EntityArrow) tempProjectile).setPickupMode(EntityArrow.PICKUP_CREATIVE);
                }
                
                projectiles[i] = tempProjectile;
            }
        }
        
        EntityShootCrossbowEvent entityShootCrossbowEvent = new EntityShootCrossbowEvent(player, this, projectiles);
        Server.getInstance().getPluginManager().callEvent(entityShootCrossbowEvent);
        if (entityShootCrossbowEvent.isCancelled()) {
            entityShootCrossbowEvent.killProjectiles();
            player.getInventory().sendContents(player);
            player.getOffhandInventory().sendContents(player);
            return false;
        }
        
        clearProjectiles(player);
        this.hasStartLoaded = false;
        this.hasMiddleLoaded = false;
        for (int i = 0; i < entityShootCrossbowEvent.getProjectilesCount(); i++) {
            launchProjectile(entityShootCrossbowEvent.getProjectile(i));
        }
        player.getLevel().addSound(player, Sound.CROSSBOW_SHOOT);
        
        return true;
    }
    
    public boolean launchProjectile(@Nonnull EntityProjectile projectile) {
        ProjectileLaunchEvent projectileLaunchEvent = new ProjectileLaunchEvent(projectile);
        Server.getInstance().getPluginManager().callEvent(projectileLaunchEvent);
        if (projectileLaunchEvent.isCancelled()) {
            projectileLaunchEvent.getEntity().kill();
            return false;
        }
        
        projectile.spawnToAll();
        return true;
    }
}
