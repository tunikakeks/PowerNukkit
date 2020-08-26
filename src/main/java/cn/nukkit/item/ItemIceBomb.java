package cn.nukkit.item;

/**
 * @author good777LUCKY
 */
public class ItemIceBomb extends ProjectileItem {

    public ItemIceBomb() {
        this(0, 1);
    }
    
    public ItemIceBomb(Integer meta) {
        this(meta, 1);
    }
    
    public ItemIceBomb(Integer meta, int count) {
        super(ICE_BOMB, 0, count, "Ice Bomb");
    }
    
    @Override
    public int getMaxStackSize() {
        return 16;
    }
    
    @Override
    public String getProjectileEntityType() {
        return "IceBomb";
    }
    
    @Override
    public float getThrowForce() {
        return 1.5f;
    }
    
    // TODO: Add CoolDown
}
