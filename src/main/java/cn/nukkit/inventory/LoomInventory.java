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

        Item[] drops = new Item[] {getItem(0), getItem(1), getItem(2)};
        for(Item drop : drops) {
            if (!who.getInventory().canAddItem(drop)) {
                this.getHolder().getLevel().dropItem(this.getHolder().add(0.5, 0.5, 0.5), drop);
            } else {
                who.getInventory().addItem(drop);
            }
        }

        clear(0);
        clear(1);
        clear(2);

        who.resetCraftingGridType();
    }

    @Override
    public void onSlotChange(int index, Item before, boolean send) {
        if(index == 3) {
            Item input1 = getItem(0);
            Item input2 = getItem(1);
            input1.count--;
            input2.count--;
            setItem(0, input1);
            setItem(1, input2);
            playerUI.addItem(before);
        }
    }
}
