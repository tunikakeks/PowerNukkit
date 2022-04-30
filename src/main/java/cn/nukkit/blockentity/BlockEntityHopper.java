package cn.nukkit.blockentity;

import cn.nukkit.Player;
import cn.nukkit.api.PowerNukkitDifference;
import cn.nukkit.api.PowerNukkitOnly;
import cn.nukkit.api.Since;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockComposter;
import cn.nukkit.block.BlockHopper;
import cn.nukkit.block.BlockID;
import cn.nukkit.blockproperty.CommonBlockProperties;
import cn.nukkit.blockstate.BlockState;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.item.*;
import cn.nukkit.event.inventory.InventoryMoveItemEvent;
import cn.nukkit.inventory.*;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.item.ItemBucket;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.AxisAlignedBB;
import cn.nukkit.math.BlockFace;
import cn.nukkit.math.BlockVector3;
import cn.nukkit.math.SimpleAxisAlignedBB;
import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.ListTag;

import java.util.HashSet;

/**
 * @author CreeperFace
 * @since 8.5.2017
 */
public class BlockEntityHopper extends BlockEntitySpawnable implements InventoryHolder, BlockEntityContainer, BlockEntityNameable {

    protected HopperInventory inventory;

    public int transferCooldown;

    private AxisAlignedBB pickupArea;
    
    private boolean disabled;

    private int lastUpdate;

    private int lastSlotUpdated = -1;
    
    private final BlockVector3 temporalVector = new BlockVector3();

    public BlockEntityHopper(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    protected void initBlockEntity() {
        if (this.namedTag.contains("TransferCooldown")) {
            this.transferCooldown = this.namedTag.getInt("TransferCooldown");
        } else {
            this.transferCooldown = 8;
        }

        this.inventory = new HopperInventory(this);

        if (!this.namedTag.contains("Items") || !(this.namedTag.get("Items") instanceof ListTag)) {
            this.namedTag.putList(new ListTag<CompoundTag>("Items"));
        }

        for (int i = 0; i < this.getSize(); i++) {
            this.inventory.setItem(i, this.getItem(i));
        }

        this.pickupArea = new SimpleAxisAlignedBB(this.x, this.y, this.z - 0.0001, this.x + 1, this.y + 1.6, this.z + 1);
        
        this.scheduleUpdate();

        super.initBlockEntity();

        Block block = getBlock();
        if (block instanceof BlockHopper) {
            disabled = !((BlockHopper)block).isEnabled();
        }
    }

    @Override
    public boolean isBlockEntityValid() {
        return this.level.getBlockIdAt(this.getFloorX(), this.getFloorY(), this.getFloorZ()) == Block.HOPPER_BLOCK;
    }

    @Override
    public String getName() {
        return this.hasName() ? this.namedTag.getString("CustomName") : "Hopper";
    }

    @Override
    public boolean hasName() {
        return this.namedTag.contains("CustomName");
    }

    @Override
    public void setName(String name) {
        if (name == null || name.equals("")) {
            this.namedTag.remove("CustomName");
            return;
        }

        this.namedTag.putString("CustomName", name);
    }

    public boolean isOnTransferCooldown() {
        return this.transferCooldown > 0;
    }

    public void setTransferCooldown(int transferCooldown) {
        this.transferCooldown = transferCooldown;
    }

    @Override
    public int getSize() {
        return 5;
    }

    protected int getSlotIndex(int index) {
        ListTag<CompoundTag> list = this.namedTag.getList("Items", CompoundTag.class);
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getByte("Slot") == index) {
                return i;
            }
        }

        return -1;
    }

    @Override
    public Item getItem(int index) {
        int i = this.getSlotIndex(index);
        if (i < 0) {
            return new ItemBlock(Block.get(BlockID.AIR), 0, 0);
        } else {
            CompoundTag data = (CompoundTag) this.namedTag.getList("Items").get(i);
            return NBTIO.getItemHelper(data);
        }
    }

    @Override
    public void setItem(int index, Item item) {
        int i = this.getSlotIndex(index);

        CompoundTag d = NBTIO.putItemHelper(item, index);

        if (item.getId() == Item.AIR || item.getCount() <= 0) {
            if (i >= 0) {
                this.namedTag.getList("Items").getAll().remove(i);
            }
        } else if (i < 0) {
            (this.namedTag.getList("Items", CompoundTag.class)).add(d);
        } else {
            (this.namedTag.getList("Items", CompoundTag.class)).add(i, d);
        }
    }

    @Override
    public void saveNBT() {
        this.namedTag.putList(new ListTag<CompoundTag>("Items"));
        for (int index = 0; index < this.getSize(); index++) {
            this.setItem(index, this.inventory.getItem(index));
        }

        this.namedTag.putInt("TransferCooldown", this.transferCooldown);
    }

    @Override
    public HopperInventory getInventory() {
        return inventory;
    }

    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    public boolean isDisabled() {
        return disabled;
    }

    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    @Override
    public boolean onUpdate() {
        if (this.closed) {
            return false;
        }

        if (isOnTransferCooldown() && this.lastUpdate < this.server.getTick()) {
            this.transferCooldown--;
            return true;
        }
        
        if (disabled) {
        	return false;
        }

        Block blockSide = this.getBlock().getSide(BlockFace.UP);
        BlockEntity blockEntity = this.level.getBlockEntity(temporalVector.setComponentsAdding(this, BlockFace.UP));

        boolean changed = pushItems();

        if (blockEntity instanceof InventoryHolder || blockSide instanceof BlockComposter)  {
            changed = pullItems() || changed;
        } else {
            changed = pullMinecartItems() || changed;
            changed = pickupItems() || changed;
        }

        if (changed) {
            this.setTransferCooldown(8);
            setDirty();
        }


        return true;
    }

    @Since("1.4.0.0-PN")
    @PowerNukkitOnly
    @Override
    public boolean isObservable() {
        return false;
    }

    @PowerNukkitDifference(info = "Check if the hopper above is locked, then don't pull items.", since = "1.4.0.0-PN")
    public boolean pullItems() {
        if (this.inventory.isFull()) {
            return false;
        }

        Block blockSide = this.getBlock().getSide(BlockFace.UP);
        BlockEntity blockEntity = this.level.getBlockEntity(temporalVector.setComponentsAdding(this, BlockFace.UP));

        //Fix for furnace outputs
        if (blockEntity instanceof BlockEntityFurnace) {
            FurnaceInventory inv = ((BlockEntityFurnace) blockEntity).getInventory();
            Item item = inv.getFuel();

            if (item instanceof ItemBucket && ((ItemBucket) item).isEmpty()) {
                Item itemToAdd = item.clone();
                itemToAdd.count = 1;

                if (!this.inventory.canAddItem(itemToAdd)) {
                    return false;
                }

                InventoryMoveItemEvent ev = new InventoryMoveItemEvent(inv, this.inventory, this, itemToAdd, InventoryMoveItemEvent.Action.SLOT_CHANGE);
                this.server.getPluginManager().callEvent(ev);

                if (ev.isCancelled()) {
                    return false;
                }

                Item[] items = this.inventory.addItem(itemToAdd);

                if (items.length <= 0) {
                    item.count--;
                    inv.setFuel(item);
                    return true;
                }
            }
            item = inv.getResult();

            if (!item.isNull()) {
                Item itemToAdd = item.clone();
                itemToAdd.count = 1;

                if (!this.inventory.canAddItem(itemToAdd)) {
                    return false;
                }

                InventoryMoveItemEvent ev = new InventoryMoveItemEvent(inv, this.inventory, this, itemToAdd, InventoryMoveItemEvent.Action.SLOT_CHANGE);
                this.server.getPluginManager().callEvent(ev);

                if (ev.isCancelled()) {
                    return false;
                }

                Item[] items = this.inventory.addItem(itemToAdd);

                if (items.length <= 0) {
                    item.count--;
                    inv.setResult(item);
                    return true;
                }
            }
        } else if (blockEntity instanceof InventoryHolder) {
            Inventory inv = ((InventoryHolder) blockEntity).getInventory();

            int slot = -1;
            if (blockEntity instanceof BlockEntityHopper) {
                slot = ((BlockEntityHopper) blockEntity).lastUpdate >= this.server.getTick() && ((BlockEntityHopper) blockEntity).lastSlotUpdated >= 0 && ((BlockEntityHopper) blockEntity).lastSlotUpdated < ((BlockEntityHopper) blockEntity).getInventory().getSize() ? ((BlockEntityHopper) blockEntity).lastSlotUpdated : -1;
            }

            for (int i = 0; i < inv.getSize(); i++) {
                Item item = inv.getItem(i);

                if (!item.isNull()) {
                    if (slot == i && item.getCount() <= 1) {
                        continue;
                    }

                    Item itemToAdd = item.clone();
                    itemToAdd.count = 1;

                    if (!this.inventory.canAddItem(itemToAdd)) {
                        continue;
                    }

                    InventoryMoveItemEvent ev = new InventoryMoveItemEvent(inv, this.inventory, this, itemToAdd, InventoryMoveItemEvent.Action.SLOT_CHANGE);
                    this.server.getPluginManager().callEvent(ev);

                    if (ev.isCancelled()) {
                        continue;
                    }

                    int slotPulled = -1;
                    for (int j = 0; j < this.inventory.getSize(); j++) {
                        Item itemInInventory = this.inventory.getItem(j);
                        if (itemInInventory.getId() == 0 || (itemInInventory.getCount() < itemInInventory.getMaxStackSize() && itemInInventory.equals(itemToAdd))) {
                            itemToAdd.count += itemInInventory.count;
                            this.inventory.setItem(j, itemToAdd);
                            slotPulled = j;
                            break;
                        }
                    }

                    if (slotPulled == -1) {
                        continue;
                    }

                    item.count--;

                    inv.setItem(i, item);

                    this.lastUpdate = this.server.getTick();
                    this.lastSlotUpdated = slotPulled;

                    return true;
                }
            }
        } else if (blockSide instanceof BlockComposter) {
            BlockComposter blockComposter = (BlockComposter)blockSide;
            if (blockComposter.isFull()) {
                Item item = blockComposter.empty();

                if (item == null || item.isNull()) {
                    return false;
                }

                Item itemToAdd = item.clone();
                itemToAdd.count = 1;

                if (!this.inventory.canAddItem(itemToAdd)) {
                    return false;
                }

                Item[] items = this.inventory.addItem(itemToAdd);

                return items.length < 1;
            }
        }
        return false;
    }

    public boolean pullMinecartItems() {
        if (this.inventory.isFull()) {
            return false;
        }
        for(Entity entity : level.getCollidingEntities(new SimpleAxisAlignedBB(this.x + 0.125, this.y + 1, this.z + 0.125, this.x + 0.875, this.y + 1.875, this.z + 0.875))) {
            if(!(entity instanceof EntityMinecartAbstract) || entity instanceof EntityMinecartEmpty || entity instanceof EntityMinecartTNT) {
                continue;
            }

            InventoryHolder inventoryHolder = (InventoryHolder) entity;
            Inventory inventory = inventoryHolder.getInventory();

            for (int i = 0; i < inventory.getSize(); i++) {
                Item item = inventory.getItem(i);

                if (!item.isNull()) {
                    Item itemToAdd = item.clone();
                    itemToAdd.count = 1;

                    if (!this.inventory.canAddItem(itemToAdd)) {
                        continue;
                    }

                    InventoryMoveItemEvent ev = new InventoryMoveItemEvent(inventory, this.inventory, this, itemToAdd, InventoryMoveItemEvent.Action.SLOT_CHANGE);
                    this.server.getPluginManager().callEvent(ev);

                    if (ev.isCancelled()) {
                        continue;
                    }

                    int slotPulled = -1;
                    for (int j = 0; j < this.inventory.getSize(); j++) {
                        Item itemInInventory = this.inventory.getItem(j);
                        if (itemInInventory.getId() == 0 || (itemInInventory.getCount() < itemInInventory.getMaxStackSize() && itemInInventory.equals(itemToAdd))) {
                            itemToAdd.count += itemInInventory.count;
                            this.inventory.setItem(j, itemToAdd);
                            slotPulled = j;
                            break;
                        }
                    }

                    if (slotPulled == -1) {
                        continue;
                    }

                    item.count--;

                    inventory.setItem(i, item);
                    return true;
                }
            }
        }
        return false;
    }

    public boolean pickupItems() {
        if (this.inventory.isFull()) {
            return false;
        }

        boolean pickedUpItem = false;

        for (Entity entity : this.level.getCollidingEntities(this.pickupArea)) {
            if (!(entity instanceof EntityItem) || entity.isClosed()) {
                continue;
            }

            EntityItem itemEntity = (EntityItem) entity;
            Item item = itemEntity.getItem();

            if (item.isNull()) {
                continue;
            }

            int originalCount = item.getCount();

            if (!this.inventory.canAddItem(item)) {
                continue;
            }

            InventoryMoveItemEvent ev = new InventoryMoveItemEvent(null, this.inventory, this, item, InventoryMoveItemEvent.Action.PICKUP);
            this.server.getPluginManager().callEvent(ev);

            if (ev.isCancelled()) {
                continue;
            }

            Item[] items = this.inventory.addItem(item);

            if (items.length == 0) {
                entity.close();
                pickedUpItem = true;
                continue;
            }

            if (items[0].getCount() != originalCount) {
                pickedUpItem = true;
                item.setCount(items[0].getCount());
            }
        }

        //TODO: check for minecart
        return pickedUpItem;
    }

    @Override
    public void close() {
        if (!closed) {
            for (Player player : new HashSet<>(this.getInventory().getViewers())) {
                player.removeWindow(this.getInventory());
            }
            super.close();
        }
    }

    @Override
    public void onBreak() {
        for (Item content : inventory.getContents().values()) {
            level.dropItem(this, content);
        }
        this.inventory.clearAll();
    }

    public boolean pushItems() {
        if (this.inventory.isEmpty()) {
            return false;
        }

        BlockState levelBlockState = getLevelBlockState();
        if (levelBlockState.getBlockId() != BlockID.HOPPER_BLOCK) {
            return false;
        }
        
        BlockFace side = levelBlockState.getPropertyValue(CommonBlockProperties.FACING_DIRECTION);
        Block blockSide = this.getBlock().getSide(side);
        BlockEntity be = this.level.getBlockEntity(temporalVector.setComponentsAdding(this, side));

        if (!(be instanceof InventoryHolder) && !(blockSide instanceof BlockComposter)) {
            return fillMinecart(blockSide);
        }

        InventoryMoveItemEvent event;

        //Fix for furnace inputs
        if (be instanceof BlockEntityFurnace) {
            BlockEntityFurnace furnace = (BlockEntityFurnace) be;
            FurnaceInventory inventory = furnace.getInventory();
            if (inventory.isFull()) {
                return false;
            }

            boolean pushedItem = false;

            for (int i = 0; i < this.inventory.getSize(); i++) {
                Item item = this.inventory.getItem(i);
                if (!item.isNull()) {
                    Item itemToAdd = item.clone();
                    itemToAdd.setCount(1);

                    //Check direction of hopper
                    if (this.getBlock().getDamage() == 0) {
                        Item smelting = inventory.getSmelting();
                        if (smelting.isNull()) {
                            event = new InventoryMoveItemEvent(this.inventory, inventory, this, itemToAdd, InventoryMoveItemEvent.Action.SLOT_CHANGE);
                            this.server.getPluginManager().callEvent(event);

                            if (!event.isCancelled()) {
                                inventory.setSmelting(itemToAdd);
                                item.count--;
                                pushedItem = true;
                            }
                        } else if (inventory.getSmelting().equals(itemToAdd) && smelting.count < smelting.getMaxStackSize()) {
                            event = new InventoryMoveItemEvent(this.inventory, inventory, this, itemToAdd, InventoryMoveItemEvent.Action.SLOT_CHANGE);
                            this.server.getPluginManager().callEvent(event);

                            if (!event.isCancelled()) {
                                smelting.count++;
                                inventory.setSmelting(smelting);
                                item.count--;
                                pushedItem = true;
                            }
                        }
                    } else if (Fuel.duration.containsKey(itemToAdd.getId())) {
                        Item fuel = inventory.getFuel();
                        if (fuel.isNull()) {
                            event = new InventoryMoveItemEvent(this.inventory, inventory, this, itemToAdd, InventoryMoveItemEvent.Action.SLOT_CHANGE);
                            this.server.getPluginManager().callEvent(event);

                            if (!event.isCancelled()) {
                                inventory.setFuel(itemToAdd);
                                item.count--;
                                pushedItem = true;
                            }
                        } else if (fuel.getId() == itemToAdd.getId() && fuel.getDamage() == itemToAdd.getDamage() && fuel.count < fuel.getMaxStackSize()) {
                            event = new InventoryMoveItemEvent(this.inventory, inventory, this, itemToAdd, InventoryMoveItemEvent.Action.SLOT_CHANGE);
                            this.server.getPluginManager().callEvent(event);

                            if (!event.isCancelled()) {
                                fuel.count++;
                                inventory.setFuel(fuel);
                                item.count--;
                                pushedItem = true;
                            }
                        }
                    }

                    if (pushedItem) {
                        this.inventory.setItem(i, item);
                    }
                }
            }

            return pushedItem;
        } else if (blockSide instanceof BlockComposter) {
            BlockComposter composter = (BlockComposter)blockSide;
            if (composter.isFull()) {
                return false;
            }

            for (int i = 0; i < this.inventory.getSize(); i++) {
                Item item = this.inventory.getItem(i);

                if (item.isNull()) {
                    continue;
                }

                Item itemToAdd = item.clone();
                itemToAdd.setCount(1);

                if (!composter.onActivate(item)) {
                    return false;
                }
                item.count--;
                this.inventory.setItem(i, item);
                return true;
            }
        }
        else {
            Inventory inventory = ((InventoryHolder) be).getInventory();

            if (inventory.isFull()) {
                return false;
            }

            int slot = this.lastUpdate >= this.server.getTick() && lastSlotUpdated >= 0 && lastSlotUpdated < this.inventory.getSize() ? this.lastSlotUpdated : -1;

            for (int i = 0; i < this.inventory.getSize(); i++) {
                Item item = this.inventory.getItem(i);

                if (!item.isNull()) {
                    if (slot == i && item.getCount() <= 1) {
                        continue;
                    }

                    Item itemToAdd = item.clone();
                    itemToAdd.setCount(1);

                    if (!inventory.canAddItem(itemToAdd)) {
                        continue;
                    }

                    InventoryMoveItemEvent ev = new InventoryMoveItemEvent(this.inventory, inventory, this, itemToAdd, InventoryMoveItemEvent.Action.SLOT_CHANGE);
                    this.server.getPluginManager().callEvent(ev);

                    if (ev.isCancelled()) {
                        continue;
                    }

                    int slotPushed = -1;
                    for (int j = 0; j < inventory.getSize(); j++) {
                        Item itemInInventory = inventory.getItem(j);
                        if (itemInInventory.getId() == 0 || (itemInInventory.getCount() < itemInInventory.getMaxStackSize() && itemInInventory.equals(itemToAdd))) {
                            itemToAdd.count += itemInInventory.count;
                            inventory.setItem(j, itemToAdd);
                            slotPushed = j;
                            break;
                        }
                    }

                    if (slotPushed == -1) {
                        continue;
                    }

                    item.count--;
                    this.inventory.setItem(i, item);
                    if (be instanceof BlockEntityHopper) {
                        ((BlockEntityHopper) be).setTransferCooldown(8);
                        ((BlockEntityHopper) be).lastUpdate = this.server.getTick();
                        ((BlockEntityHopper) be).lastSlotUpdated = slotPushed;
                    }
                    return true;
                }
            }
        }

        //TODO: check for minecart
        return fillMinecart(blockSide);
    }

    private boolean fillMinecart(Block blockSide) {
        if (this.inventory.isEmpty()) {
            return false;
        }
        for(Entity entity : level.getCollidingEntities(new SimpleAxisAlignedBB(blockSide.x, blockSide.y, blockSide.z, blockSide.x + 1, blockSide.y + 1, blockSide.z + 1))) {
            if(entity.isClosed() || !(entity instanceof EntityMinecartAbstract) || entity instanceof EntityMinecartEmpty || entity instanceof EntityMinecartTNT) {
                continue;
            }

            InventoryHolder inventoryHolder = (InventoryHolder) entity;
            Inventory inventory = inventoryHolder.getInventory();

            for (int i = 0; i < this.inventory.getSize(); i++) {
                Item item = this.inventory.getItem(i);

                if (!item.isNull()) {
                    Item itemToAdd = item.clone();
                    itemToAdd.setCount(1);

                    if (!inventory.canAddItem(itemToAdd)) {
                        continue;
                    }

                    InventoryMoveItemEvent ev = new InventoryMoveItemEvent(this.inventory, inventory, this, itemToAdd, InventoryMoveItemEvent.Action.SLOT_CHANGE);
                    this.server.getPluginManager().callEvent(ev);

                    if (ev.isCancelled()) {
                        continue;
                    }

                    int slotPushed = -1;
                    for (int j = 0; j < inventory.getSize(); j++) {
                        Item itemInInventory = inventory.getItem(j);
                        if (itemInInventory.getId() == 0 || (itemInInventory.getCount() < itemInInventory.getMaxStackSize() && itemInInventory.equals(itemToAdd))) {
                            itemToAdd.count += itemInInventory.count;
                            inventory.setItem(j, itemToAdd);
                            slotPushed = j;
                            break;
                        }
                    }

                    if (slotPushed == -1) {
                        continue;
                    }

                    item.count--;
                    this.inventory.setItem(i, item);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public CompoundTag getSpawnCompound() {
        CompoundTag c = new CompoundTag()
                .putString("id", BlockEntity.HOPPER)
                .putInt("x", (int) this.x)
                .putInt("y", (int) this.y)
                .putInt("z", (int) this.z);

        if (this.hasName()) {
            c.put("CustomName", this.namedTag.get("CustomName"));
        }

        return c;
    }

    @Override
    public String toString() {
        return "BlockEntityHopper{" +
                ", transferCooldown=" + transferCooldown +
                ", disabled=" + disabled +
                ", lastUpdate=" + lastUpdate +
                ", lastSlotUpdated=" + lastSlotUpdated +
                '}';
    }
}