package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.blockentity.BlockEntityCauldron;
import cn.nukkit.blockproperty.ArrayBlockProperty;
import cn.nukkit.blockproperty.BlockProperties;
import cn.nukkit.blockproperty.BooleanBlockProperty;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemTool;
import cn.nukkit.level.Level;
import cn.nukkit.math.BlockFace;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.concurrent.ThreadLocalRandom;

public class BlockPointedDripstone extends BlockSolid {

    public static final ArrayBlockProperty<DripstoneThickness> THICKNESS = new ArrayBlockProperty<>("dripstone_thickness", false, DripstoneThickness.class);

    public static final BooleanBlockProperty HANGING = new BooleanBlockProperty("hanging", false);

    public static final BlockProperties PROPERTIES = new BlockProperties(THICKNESS, HANGING);

    @Override
    public String getName() {
        return "Pointed Dripstone";
    }

    @Override
    public int getId() {
        return POINTED_DRIPSTONE;
    }

    @Nonnull
    @Override
    public BlockProperties getProperties() {
        return PROPERTIES;
    }

    @Override
    public double getHardness() {
        return 1.5;
    }

    @Override
    public double getResistance() {
        return 3;
    }

    @Override
    public int getToolType() {
        return ItemTool.TYPE_PICKAXE;
    }

    @Override
    public boolean canHarvestWithHand() {
        return true;
    }

    public boolean isHanging() {
        return this.getPropertyValue(HANGING);
    }

    public void setHanging(boolean hanging) {
        this.setPropertyValue(HANGING, hanging);
    }

    public DripstoneThickness getThickness() {
        return this.getPropertyValue(THICKNESS);
    }

    public void setThickness(DripstoneThickness thickness) {
        this.setPropertyValue(THICKNESS, thickness);
    }

    @Override
    public boolean place(@Nonnull Item item, @Nonnull Block block, @Nonnull Block target, @Nonnull BlockFace face, double fx, double fy, double fz, @Nullable Player player) {
        if(face != BlockFace.UP && face != BlockFace.DOWN || !target.isSolid()) {
            return false;
        }
        this.setHanging(face == BlockFace.DOWN);
        return this.getLevel().setBlock(this, this, true, true);
    }

    @Override
    public int onUpdate(int type) {
        if(type == Level.BLOCK_UPDATE_NORMAL) {
            BlockFace side = this.isHanging() ? BlockFace.UP : BlockFace.DOWN;
            Block against = this.getSide(side);
            if(!against.isSolid()) {
                this.getLevel().useBreakOn(this);
                return type;
            }
            Block opposite = this.getSide(side.getOpposite());
            if(opposite instanceof BlockPointedDripstone) {
                if(this.isHanging() != ((BlockPointedDripstone) opposite).isHanging()) {
                    if(this.getThickness() != DripstoneThickness.MERGE) {
                        this.setThickness(DripstoneThickness.MERGE);
                        this.getLevel().setBlock(this, this, true, true);
                        opposite.onUpdate(type);
                    }
                } else {
                    this.setThickness(((BlockPointedDripstone) opposite).getThickness().next());
                    this.getLevel().setBlock(this, this, true, true);
                }
            } else {
                this.setThickness(DripstoneThickness.TIP);
                this.getLevel().setBlock(this, this, true, true);
            }
            if(against instanceof BlockPointedDripstone) {
                against.onUpdate(type);
            }
            return type;
        }
        if(type == Level.BLOCK_UPDATE_RANDOM) {
            if(!this.isHanging()) {
                return 0;
            }
            Block up = this.up(2);
            if(up instanceof BlockLiquid) {
                if(ThreadLocalRandom.current().nextInt(256) < (up instanceof BlockWater ? 45 : 15)) {
                    boolean maxLengthReached = true;
                    Block dripstoneTip = null;
                    for(int i = 0; i <= 11; i++) {
                        dripstoneTip = this.down(i);
                        if(dripstoneTip.getId() != POINTED_DRIPSTONE) {
                            maxLengthReached = false;
                            break;
                        }
                    }
                    if(!maxLengthReached) {
                        for(int i = 1; i <= 10; i++) {
                            Block down = dripstoneTip.down(i);
                            if(down.getId() == AIR) {
                                continue;
                            }
                            if(down instanceof BlockCauldron) {
                                BlockCauldron cauldron = (BlockCauldron) down;
                                if(up instanceof BlockLava && cauldron.isEmpty()) {
                                    BlockEntityCauldron blockEntityCauldron = cauldron.getOrCreateBlockEntity();
                                    BlockCauldronLava cauldronLava = new BlockCauldronLava(0xE);
                                    cauldronLava.setFillLevel(BlockCauldron.FILL_LEVEL.getMaxValue());
                                    this.level.setBlock(cauldron, cauldronLava, true, true);
                                    blockEntityCauldron.clearCustomColor();
                                    blockEntityCauldron.setType(BlockEntityCauldron.PotionType.LAVA);
                                } else if(!cauldron.isFull()) {
                                    cauldron.setFillLevel(cauldron.getFillLevel() + 2);
                                    this.getLevel().setBlock(cauldron, cauldron, true);
                                }
                            }
                            break;
                        }
                    }
                }
            }
            Block down = this.down();
            if(down instanceof BlockPointedDripstone) {
                down.onUpdate(type);
                return 0;
            }
            if(down.getId() != AIR) {
                return 0;
            }
            if(ThreadLocalRandom.current().nextInt(90000) >= 1024) {
                return 0;
            }
            int upCount = 1;
            while(true) {
                up = this.up(upCount++);
                if(up instanceof BlockPointedDripstone) {
                    continue;
                }
                if(up instanceof BlockDripstone && up.up() instanceof BlockWater) {
                    break;
                }
                return 0;
            }
            boolean air = false;
            for(int i = 1; i <= 11; i++) {
                down = this.down(i);
                if(down.getId() == AIR) {
                    air = true;
                } else {
                    if(!air) {
                        return 0;
                    }
                    if(down.isSolid()) {
                        this.getLevel().setBlock(down.up(), Block.get(Block.POINTED_DRIPSTONE), true, true);
                    }
                    break;
                }
            }
            boolean maxLengthReached = true;
            for(int i = 1; i < 7; i++) {
                if(!(this.up(i) instanceof BlockPointedDripstone)) {
                    maxLengthReached = false;
                    break;
                }
            }
            if(maxLengthReached) {
                return 0;
            }
            down = this.down();
            if(down.getId() == AIR) {
                BlockPointedDripstone dripstone = new BlockPointedDripstone();
                dripstone.setHanging(true);
                this.getLevel().setBlock(down, dripstone, true, true);
            }
        }
        return 0;
    }

    public enum DripstoneThickness {

        TIP,
        FRUSTUM,
        MIDDLE,
        BASE,
        MERGE;

        public DripstoneThickness next() {
            switch(this) {
                case FRUSTUM:
                    return MIDDLE;
                case MIDDLE:
                case BASE:
                    return BASE;
            }
            return FRUSTUM;
        }

        public DripstoneThickness previous() {
            switch(this) {
                case MIDDLE:
                    return FRUSTUM;
                case BASE:
                    return MIDDLE;
            }
            return TIP;
        }
    }
}
