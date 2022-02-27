package cn.nukkit.inventory;

import cn.nukkit.api.PowerNukkitOnly;
import cn.nukkit.api.Since;

/**
 * @author MagicDroidX (Nukkit Project)
 */
public enum InventoryType {
    CHEST(27, "Chest", 0),
    ENDER_CHEST(27, "Ender Chest", 0),
    DOUBLE_CHEST(27 + 27, "Double Chest", 0),
    PLAYER(40, "Player", -1), //36 CONTAINER, 4 ARMOR
    FURNACE(3, "Furnace", 2),
    CRAFTING(5, "Crafting", 1), //4 CRAFTING slots, 1 RESULT
    WORKBENCH(10, "Crafting", 1), //9 CRAFTING slots, 1 RESULT
    BREWING_STAND(5, "Brewing", 4), //1 INPUT, 3 POTION, 1 fuel
    ANVIL(3, "Anvil", 5), //2 INPUT, 1 OUTPUT
    ENCHANT_TABLE(2, "Enchant", 3), //1 INPUT/OUTPUT, 1 LAPIS
    DISPENSER(9, "Dispenser", 6), //9 CONTAINER
    DROPPER(9, "Dropper", 7), //9 CONTAINER
    HOPPER(5, "Hopper", 8), //5 CONTAINER
    //CAULDRON typeId:9
    //MINECART_COMMAND_BLOCK typeId:16
    //HORSE typeId:12
    //JUKEBOX typeId:17
    UI(1, "UI", -1),
    @PowerNukkitOnly CURSOR(1, "Cursor", -1),
    SHULKER_BOX(27, "Shulker Box", 0),
    BEACON(1, "Beacon", 13),
    // 14 STRUCTURE_EDITOR
    // 15 TRADE
    // 18 ARMOR
    COMPOUND_CREATOR(10, "Compound Creator", 20),
    ELEMENT_CONSTRUCTOR(1, "Element Constructor", 21),
    MATERIAL_REDUCER(10, "Material Reducer", 22),
    LAB_TABLE(9, "Lab Table", 23),
    // 25 LECTERN
    // 31 HUD
    // 32 JIGSAW_EDITOR
    @PowerNukkitOnly GRINDSTONE(3, "Grindstone", 26),
    @PowerNukkitOnly BLAST_FURNACE(3, "Blast Furnace", 27),
    @PowerNukkitOnly SMOKER(3, "Smoker", 28),
    @PowerNukkitOnly STONECUTTER(2, "Stonecutter", 29), // Should be 29 but it's totally driven by the client, so setting to -1 = UI
    @PowerNukkitOnly CARTOGRAPHY(3, "Cartography Table", 30),
    @PowerNukkitOnly LOOM(4, "Loom", 24),
    @PowerNukkitOnly BARREL(27, "Barrel", 0),
    @PowerNukkitOnly CAMPFIRE(4, "Campfire", -9), // -9 = NONE
    @PowerNukkitOnly @Since("1.4.0.0-PN") ENTITY_EQUIPMENT(36, "Entity Equipment", -1), //36 CONTAINER
    @PowerNukkitOnly @Since("1.4.0.0-PN") ENTITY_ARMOR(4, "Entity Armor", -1), //4 ARMOR
    MINECART_CHEST(27, "Minecart with Chest", 0), // Should be 10
    MINECART_HOPPER(5, "Minecart with Hopper", 8), // Should be 11
    OFFHAND(1, "Offhand", -1),
    @PowerNukkitOnly @Since("1.4.0.0-PN") TRADING(3, "Villager Trade", 15),
    @PowerNukkitOnly @Since("1.4.0.0-PN") SMITHING_TABLE(2, "Smithing Table", 33);

    private final int size;
    private final String title;
    private final int typeId;

    InventoryType(int defaultSize, String defaultBlockEntity, int typeId) {
        this.size = defaultSize;
        this.title = defaultBlockEntity;
        this.typeId = typeId;
    }

    public int getDefaultSize() {
        return size;
    }

    public String getDefaultTitle() {
        return title;
    }

    public int getNetworkType() {
        return typeId;
    }
}
