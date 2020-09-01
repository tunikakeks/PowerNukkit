package cn.nukkit.inventory;

import cn.nukkit.Player;
import cn.nukkit.level.Position;

/**
 * @author good777LUCKY
 */
public class SmithingInventory extends FakeBlockUIComponent {

    private static final int SLOT_INPUT = 51;
    private static final int SLOT_MATERIAL = 52;
    private static final int SLOT_RESULT = 50;
    
    public SmithingInventory(PlayerUIInventory playerUI, Position position) {
        super(playerUI, InventoryType.SMITHING, 1, position);
    }
    
    @Override
    public void close(Player who) {
        onClose(who);
    }
    
    @Override
    public void onClose(Player who) {
        super.onClose(who);
        who.craftingType = Player.CRAFTING_SMALL;
        who.resetCraftingGridType();
        for (int i = 0; i < 3; ++i) {
            this.getHolder().getLevel().dropItem(this.getHolder().add(0.5, 0.5, 0.5), this.getItem(i));
            this.clear(i);
        }
    }
    
    @Override
    public void onOpen(Player who) {
        super.onOpen(who);
        who.craftingType = Player.CRAFTING_SMITHING;
    }
    
    public Item getInput() {
        return getItem(SLOT_INPUT);
    }
    
    public Item getMaterial() {
        return getItem(SLOT_MATERIAL);
    }
    
    public Item getResult() {
        return getItem(SLOT_RESULT);
    }
    
    public boolean setInput(Item item, boolean send) {
        return setItem(SLOT_INPUT, item, send);
    }
    
    public boolean setInput(Item item) {
        return setFirstItem(item, true);
    }
    
    public boolean setMaterial(Item item, boolean send) {
        return setItem(SLOT_MATERIAL, item, send);
    }
    
    public boolean setMaterial(Item item) {
        return setSecondItem(item, true);
    }
    
    public boolean setResult(Item item, boolean send) {
        return setItem(SLOT_RESULT, item, send);
    }
    
    public boolean setResult(Item item) {
        return setResult(item, true);
    }
    
    @Override
    public void onSlotChange(int index, Item before, boolean send) {
        try {
            if (index > 1) {
                return;
            }
            Item inputItem = getInput();
            Item materialItem = getMaterial();
            if (!inputItem.isNull() && !materialItem.isNull() && (inputItem.getNetheriteResult().getId() == 0) && materialItem.getId() != ItemID.NETHERITE_INGOT) {
                setResult(Item.get(0));
                return;
            }
    
            if (inputItem.isNull()) {
                Item air = inputItem;
                inputItem = materialItem;
                materialItem = air;
            }
    
            if (inputItem.isNull()) {
                setResult(Item.get(0));
                return;
            }
            
            Item result = getNetheriteResult(inputItem);
            CompoundTag tag = inputItem.getNamedTag();
            if (tag == null) tag = new CompoundTag(); 
            
            result.setCompoundTag(tag);
            result.setDamage(inputItem.getDamage());
            setResult(result);
        } finally {
            super.onSlotChange(index, before, send);
        }
    }
    
    public Item getNetheriteResult(Item before) {
        if (before.getId() == ItemID.DIAMOND_SWORD) return Item.get(ItemID.NETHERITE_SWORD);
        if (before.getId() == ItemID.DIAMOND_PICKAXE) return Item.get(ItemID.NETHERITE_PICKAXE);
        if (before.getId() == ItemID.DIAMOND_AXE) return Item.get(ItemID.NETHERITE_AXE);
        if (before.getId() == ItemID.DIAMOND_SHOVEL) return Item.get(ItemID.NETHERITE_SHOVEL);
        if (before.getId() == ItemID.DIAMOND_HOE) return Item.get(ItemID.NETHERITE_HOE);
        
        if (before.getId() == ItemID.DIAMOND_HELMAT) return Item.get(ItemID.NETHERITE_HELMAT);
        if (before.getId() == ItemID.DIAMOND_CHESTPLATE) return Item.get(ItemID.NETHERITE_CHESTPLATE);
        if (before.getId() == ItemID.DIAMOND_LEGGINGS) return Item.get(ItemID.NETHERITE_LEGGINGS);
        if (before.getId() == ItemID.DIAMOND_BOOTS) return Item.get(ItemID.NETHERITE_BOOTS);
        
        return(Item.get(0));
    }
}
