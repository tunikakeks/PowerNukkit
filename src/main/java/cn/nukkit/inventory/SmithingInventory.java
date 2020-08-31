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
}
