package cn.nukkit.inventory;

import cn.nukkit.Player;
import cn.nukkit.item.Item;
import cn.nukkit.level.Position;

public class LoomInventory extends FakeBlockUIComponent {

    public static int OFFSET = 9;

    public LoomInventory(PlayerUIInventory playerUI, Position position) {
        super(playerUI, InventoryType.LOOM, OFFSET, position);
    }

    @Override
    public void onOpen(Player who) {
        super.onOpen(who);
        who.craftingType = Player.CRAFTING_LOOM;
    }

    @Override
    public void onClose(Player who) {
        super.onClose(who);
        who.resetCraftingGridType();
        who.craftingType = Player.CRAFTING_SMALL;

        Item[] drops = who.getInventory().addItem(getItem(0), getItem(1), getItem(2));
        for(Item drop : drops) {
            if (!who.dropItem(drop)) {
                this.getHolder().getLevel().dropItem(this.getHolder().add(0.5, 0.5, 0.5), drop);
            }
        }

        clear(0);
        clear(1);
        clear(2);

        who.resetCraftingGridType();
    }

    public Item getFirstItem() {
        return getItem(0);
    }

    public Item getSecondItem() {
        return getItem(1);
    }

    public void setFirstItem(Item item) {
        this.setItem(0, item);
    }

    public void setSecondItem(Item item) {
        this.setItem(1, item);
    }
}
