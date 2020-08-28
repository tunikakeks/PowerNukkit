package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.blockproperty.BlockProperties;
import cn.nukkit.blockproperty.BooleanBlockProperty;
import cn.nukkit.entity.Entity;
import cn.nukkit.item.Item;
import cn.nukkit.level.Level;
import cn.nukkit.level.Sound;
import cn.nukkit.math.NukkitRandom;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.DoubleTag;
import cn.nukkit.nbt.tag.FloatTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.utils.BlockColor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author xtypr
 * @since 2015/12/8
 */
public class BlockTNT extends BlockSolidMeta {
    protected static final BooleanBlockProperty EXPLODE_BIT = new BooleanBlockProperty("explode_bit", false);
    protected static final BooleanBlockProperty ALLOW_UNDERWATER_BIT = new BooleanBlockProperty("allow_underwater_bit", false);
    public static final BlockProperties PROPERTIES = new BlockProperties(
        ALLOW_UNDERWATER_BIT,
        EXPLODE_BIT
    );
    
    public BlockTNT() {
        this(0);
    }
    
    @Override
    public int getId() {
        return TNT;
    }
    
    @Nonnull
    @Override
    public BlockProperties getProperties() {
        return PROPERTIES;
    }
    
    @Override
    public String getName() {
        return (isUnderwaterAllowed()) ? "Underwater TNT" : "TNT";
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
    public boolean canBeActivated() {
        return true;
    }
    
    @Override
    public int getBurnChance() {
        return 15;
    }
    
    @Override
    public int getBurnAbility() {
        return 100;
    }
    
    public void prime() {
        this.prime(80);
    }
    
    public void prime(int fuse) {
        prime(fuse, null);
    }
    
    public void prime(int fuse, Entity source) {
        this.getLevel().setBlock(this, Block.get(BlockID.AIR), true);
        double mot = (new NukkitRandom()).nextSignedFloat() * Math.PI * 2;
        CompoundTag nbt = new CompoundTag()
                .putList(new ListTag<DoubleTag>("Pos")
                        .add(new DoubleTag("", this.x + 0.5))
                        .add(new DoubleTag("", this.y))
                        .add(new DoubleTag("", this.z + 0.5)))
                .putList(new ListTag<DoubleTag>("Motion")
                        .add(new DoubleTag("", -Math.sin(mot) * 0.02))
                        .add(new DoubleTag("", 0.2))
                        .add(new DoubleTag("", -Math.cos(mot) * 0.02)))
                .putList(new ListTag<FloatTag>("Rotation")
                        .add(new FloatTag("", 0))
                        .add(new FloatTag("", 0)))
                .putShort("Fuse", fuse);
        Entity tnt = Entity.createEntity("PrimedTnt",
                this.getLevel().getChunk(this.getFloorX() >> 4, this.getFloorZ() >> 4),
                nbt, source
        );
        if(tnt == null) {
            return;
        }
        tnt.spawnToAll();
        this.level.addSound(this, Sound.RANDOM_FUSE);
    }
    
    @Override
    public int onUpdate(int type) {
        if (!this.level.getServer().isRedstoneEnabled()) {
            return 0;
        }

        if ((type == Level.BLOCK_UPDATE_NORMAL || type == Level.BLOCK_UPDATE_REDSTONE) && this.level.isBlockPowered(this.getLocation())) {
            this.prime();
        }

        return 0;
    }
    
    @Override
    public boolean onActivate(@Nonnull Item item, @Nullable Player player) {
        if (item.getId() == Item.FLINT_STEEL) {
            item.useOn(this);
            this.prime(80, player);
            return true;
        }
        if (item.getId() == Item.FIRE_CHARGE) {
            if (!player.isCreative()) player.getInventory().removeItem(Item.get(Item.FIRE_CHARGE, 0, 1));
            this.level.addSound(player, Sound.MOB_GHAST_FIREBALL);
            this.prime(80, player);
            return true;
        }
        return false;
    }
    
    @Override
    public boolean onBreak(Item item) {
        if (isUnderwaterAllowed) {
            this.prime();
        }
        return super.onBreak(item);
    }
    
    public boolean isUnderwaterAllowed() {
        return getBooleanValue(ALLOW_UNDERWATER_BIT);
    }
    
    public void setUnderwaterAllowed(boolean underwater) {
        setBooleanValue(ALLOW_UNDERWATER_BIT, underwater);
    }
    
    public boolean isExplode() {
        return getBooleanValue(EXPLODE_BIT);
    }
    
    public void setExplode(boolean explode) {
        setBooleanValue(EXPLODE_BIT, explode);
    }
    
    @Override
    public BlockColor getColor() {
        return BlockColor.TNT_BLOCK_COLOR;
    }
}
