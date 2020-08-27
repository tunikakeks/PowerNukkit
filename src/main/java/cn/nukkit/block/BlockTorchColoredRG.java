package cn.nukkit.block;

import cn.nukkit.blockproperty.BlockProperties;
import cn.nukkit.blockproperty.BooleanBlockProperty;

/**
 * @author good777LUCKY
 */
public class BlockTorchColoredRG extends BlockTorch {
    protected static final BooleanBlockProperty COLOR_BIT = new BooleanBlockProperty("color_bit", false);
    public static final BlockProperties PROPERTIES = new BlockProperties(
        COLOR_BIT,
        TORCH_FACING_DIRECTION
    );
    
    public BlockTorchColoredRG() {
        // Does Nothing
    }
    
    public BlockTorchColoredRG(int meta) {
        super(meta);
    }
    
    @Override
    public String getName() {
        return "Colored Torch";
    }
    
    @Override
    public int getId() {
        return COLORED_TORCH_RG;
    }
    
    @Nonnull
    @Override
    public BlockProperties getProperties() {
        return PROPERTIES;
    }
}
