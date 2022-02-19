package cn.nukkit.block;

import cn.nukkit.utils.BlockColor;

/**
 * @author good777LUCKY
 */
public class BlockCamera extends BlockSolid {

    @Override
    public int getId() {
        return CAMERA;
    }
    
    @Override
    public double getHardness() {
        return 0;
    }
    
    @Override
    public double getResistance() {
        return 0;
    }
    
    @Override
    public String getName() {
        return "Camera";
    }
    
    @Override
    public BlockColor getColor() {
        return BlockColor.WOOD_BLOCK_COLOR;
    }
}
