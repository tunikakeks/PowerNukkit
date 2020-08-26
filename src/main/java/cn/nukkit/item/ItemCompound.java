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
                return "Salt";
            case 1:
                return "Sodium Oxide";
            case 2:
                return "Lye";
            case 3:
                return "Magnesium Nitrate";
            case 4:
                return "Iron Sulfide";
            case 5:
                return "Lithium Hydride";
            case 6:
                return "Sodium Hydride";
            case 7:
                return "Calcium Bromide";
            case 8:
                return "Magnesium Oxide";
            case 9:
                return "Sodium Acetate";
            case 10:
                return "Luminol";
            case 11:
                return "Charcoal"; // Invalid Natural Compound
            case 12:
                return "Suger"; // Invalid Natural Compound
            case 13:
                return "Aluminium Oxide";
            case 14:
                return "Boron Trioxide";
            case 15:
                return "Soap";
            case 16:
                return "Polyethylene";
            case 17:
                return "Garbage";
            case 18:
                return "Magnesium Salts"; // ?
            case 19:
                return "Sulfate";
            case 20:
                return "Barium Sulfate";
            case 21:
                return "Potassium Chloride";
            case 22:
                return "Mercuric Chloride";
            case 23:
                return "Cerium Chloride";
            case 24:
                return "Tungsten Chloride";
            case 25:
                return "Calcium Chloride";
            case 26:
                return "Water";
            case 27:
                return "Glue";
            case 28:
                return "Hypochlorite"; // ?
            case 29:
                return "Crude Oil";
            case 30:
                return "Latex";
            case 31:
                return "Potassium Iodide";
            case 32:
                return "Sodium Fluoride";
            case 33:
                return "Benzene";
            case 34:
                return "Ink"; // Invalid Natural Compound
            case 35:
                return "Hydrogen Peroxide";
            case 36:
                return "Ammonia";
            case 37:
                return "Sodium Hypochlorite";
            default:
                return "Compound";
        }
    }
}
