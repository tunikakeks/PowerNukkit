package cn.nukkit.item;

/**
 * @author good777LUCKY
 */
public class ItemCompound extends Item {

    public static final int SALT = 0;
    public static final int SODIUM_OXIDE = 1;
    public static final int LYE = 2;
    public static final int MAGNESIUM_NITRATE = 3;
    public static final int IRON_SULFIDE = 4;
    
    public ItemCompound() {
        this(0, 1);
    }
    
    public ItemCompound(Integer meta) {
        this(meta, 1);
    }
    
    public ItemCompound(Integer meta, int count) {
        super(COMPOUND, meta, count, "Compound");
    }
    
    protected static String getName(int meta) {
        switch (meta) {
            case 0:
                return "Salt"
            case 1:
                return "Sodium Oxide";
            case 2:
                return "Lye";
            case 3:
                return "Magnesium Nitrate";
            case 4:
                return "Iron Sulfide";
            default:
                return "Compound";
        }
    }
}
