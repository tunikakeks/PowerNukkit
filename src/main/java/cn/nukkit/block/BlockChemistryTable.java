package cn.nukkit.block;

import cn.nukkit.item.Item;
import cn.nukkit.item.ItemTool;
import cn.nukkit.utils.BlockColor;

/**
 * @author good777LUCKY
 */
public class BlockChemistryTable extends BlockSolidMeta {

    public BlockChemistryTable() {
        this(0);
    }
    
    public BlockChemistryTable(int meta) {
        super(meta);
    }
    
    @Override
    public int getId() {
        return CHEMISTRY_TABLE;
    }
    
    @Override
    public double getHardness() {
        return 2.5;
    }
    
    @Override
    public double getResistance() {
        return 2;
    }
    
    @Override
    public int getToolType() {
        return ItemTool.TYPE_PICKAXE;
    }
    
    @Override
    public String getName() {
        return "Chemistry Table";
    }
    
    @Override
    public BlockColor getColor() {
        return BlockColor.WHITE_BLOCK_COLOR;
    }
}
