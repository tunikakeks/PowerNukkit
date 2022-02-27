package cn.nukkit.inventory;

import cn.nukkit.api.PowerNukkitOnly;
import cn.nukkit.level.Position;

public class MaterialReducerInventory extends FakeBlockUIComponent {
    @PowerNukkitOnly
    public static final int OFFSET = 64;

    public MaterialReducerInventory(PlayerUIInventory playerUI, Position position) {
        super(playerUI, InventoryType.MATERIAL_REDUCER, OFFSET, position);
    }
}
