package cn.nukkit.inventory;

import cn.nukkit.api.PowerNukkitOnly;
import cn.nukkit.level.Position;

public class ElementConstructorInventory extends FakeBlockUIComponent {
    @PowerNukkitOnly
    public static final int OFFSET = 75;

    public ElementConstructorInventory(PlayerUIInventory playerUI, Position position) {
        super(playerUI, InventoryType.ELEMENT_CONSTRUCTOR, OFFSET, position);
    }
}
