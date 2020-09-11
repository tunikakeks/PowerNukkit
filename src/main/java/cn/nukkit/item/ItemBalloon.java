package cn.nukkit.item;

import cn.nukkit.utils.DyeColor;

/**
 * @author good777LUCKY
 */
public class ItemBalloon extends Item {

    public ItemBalloon() {
        this(0, 1);
    }
    
    public ItemBalloon(Integer meta) {
        this(meta, 1);
    }
    
    public ItemBalloon(Integer meta, int count) {
        super(BALLOON, meta, count, DyeColor.getByWoolData(meta).getName() + " Balloon");
    }
    
    @Override
    public int getMaxStackSize() {
        return 16;
    }
    
    public DyeColor getDyeColor() {
        return DyeColor.getByDyeData(meta);
    }
    
    @Override
    public boolean canBeActivated() {
        return true;
    }
    
    
}
