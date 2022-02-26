package cn.nukkit.inventory;

import cn.nukkit.api.PowerNukkitOnly;
import cn.nukkit.level.Position;

public class CompoundCreatorInventory extends FakeBlockUIComponent {
    @PowerNukkitOnly
    public static final int OFFSET = 53;

    public CompoundCreatorInventory(PlayerUIInventory playerUI, Position position) {
        super(playerUI, InventoryType.COMPOUND_CREATOR, OFFSET, position);
    }
}
