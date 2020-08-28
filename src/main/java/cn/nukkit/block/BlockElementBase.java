package cn.nukkit.block;

/**
 * @author good777LUCKY
 */
public class BlockElementBase extends BlockSolid {

    public BlockElementBase() {
        // Does Nothing
    }
    
    @Override
    public double getHardness() {
        return 0;
    }
    
    @Override
    public double getResistance() {
        return 0;
    }
}
