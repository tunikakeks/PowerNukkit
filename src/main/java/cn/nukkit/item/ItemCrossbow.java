package cn.nukkit.item;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
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
        return !shoot();
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
    
    public boolean shoot() {
        if (!isLoaded()) {
            return false;
        }
        
        
        return true;
    }
    
    public boolean launchProjectile(@Nonnull EntityProjectile projectile) {
        return true;
    }
}
