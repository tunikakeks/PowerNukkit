package cn.nukkit.inventory;

import cn.nukkit.api.PowerNukkitOnly;
import cn.nukkit.level.Position;

public class LabTableInventory extends FakeBlockUIComponent {
    @PowerNukkitOnly
    public static final int OFFSET = 76;

    public LabTableInventory(PlayerUIInventory playerUI, Position position) {
        super(playerUI, InventoryType.LAB_TABLE, OFFSET, position);
    }
}
