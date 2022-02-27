package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.api.PowerNukkitDifference;
import cn.nukkit.api.PowerNukkitOnly;
import cn.nukkit.api.Since;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.blockentity.BlockEntityComparator;
import cn.nukkit.blockproperty.BlockProperties;
import cn.nukkit.blockproperty.BooleanBlockProperty;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemRedstoneComparator;
import cn.nukkit.level.Level;
import cn.nukkit.math.BlockFace;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.network.protocol.LevelEventPacket;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.RedstoneComponent;
import lombok.extern.log4j.Log4j2;

import javax.annotation.Nonnull;

import static cn.nukkit.blockproperty.CommonBlockProperties.DIRECTION;

/**
 * @author CreeperFace
 */
@PowerNukkitDifference(since = "1.4.0.0-PN", info = "Implements BlockEntityHolder only in PowerNukkit")
@PowerNukkitDifference(info = "Implements RedstoneComponent and uses methods from it.", since = "1.4.0.0-PN")
@Log4j2
public abstract class BlockRedstoneComparator extends BlockRedstoneDiode implements RedstoneComponent, BlockEntityHolder<BlockEntityComparator> {

    @PowerNukkitOnly
    @Since("1.5.0.0-PN")
    public static final BooleanBlockProperty OUTPUT_LIT = new BooleanBlockProperty("output_lit_bit", false);

    @PowerNukkitOnly
    @Since("1.5.0.0-PN")
    public static final BooleanBlockProperty OUTPUT_SUBTRACT = new BooleanBlockProperty("output_subtract_bit", false);

    @PowerNukkitOnly
    @Since("1.5.0.0-PN")
    public static final BlockProperties PROPERTIES = new BlockProperties(DIRECTION, OUTPUT_SUBTRACT, OUTPUT_LIT);

    public BlockRedstoneComparator() {
        this(0);
    }

    public BlockRedstoneComparator(int meta) {
        super(meta);
    }

    @Since("1.4.0.0-PN")
    @PowerNukkitOnly
    @Nonnull
    @Override
    public BlockProperties getProperties() {
        return PROPERTIES;
    }

    @Since("1.4.0.0-PN")
    @PowerNukkitOnly
    @Nonnull
    @Override
    public Class<? extends BlockEntityComparator> getBlockEntityClass() {
        return BlockEntityComparator.class;
    }

    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    @Nonnull
    @Override
    public String getBlockEntityType() {
        return BlockEntity.COMPARATOR;
    }

    @Override
    public boolean canBeFlowedInto() {
        return true;
    }

    @Override
    public int getWaterloggingLevel() {
        return 0;
    }

    @Override
    protected int getDelay() {
        return 2;
    }

    @Override
    public BlockFace getFacing() {
        return BlockFace.fromHorizontalIndex(this.getDamage());
    }

    public Mode getMode() {
        return (getDamage() & 4) > 0 ? Mode.SUBTRACT : Mode.COMPARE;
    }

    @Override
    protected BlockRedstoneComparator getUnpowered() {
        return (BlockRedstoneComparator) Block.get(BlockID.UNPOWERED_COMPARATOR, this.getDamage());
    }

    @Override
    protected BlockRedstoneComparator getPowered() {
        return (BlockRedstoneComparator) Block.get(BlockID.POWERED_COMPARATOR, this.getDamage());
    }

    @Override
    protected int getRedstoneSignal() {
        BlockEntityComparator comparator = getBlockEntity();
        return comparator == null? 0 : comparator.getOutputSignal();
    }

    @Override
    public void updateState() {
        if (!this.level.isBlockTickPending(this, this)) {
            int output = this.calculateOutput();
            int power = getRedstoneSignal();

            if (output != power || this.isPowered() != this.shouldBePowered()) {
                this.level.scheduleUpdate(this, this, 2);
            }
        }
    }

    @Override
    protected int calculateInputStrength() {
        int power = super.calculateInputStrength();
        BlockFace face = getFacing();
        Block block = this.getSide(face);

        if (block.hasComparatorInputOverride()) {
            power = block.getComparatorInputOverride();
        } else if (power < 15 && block.isNormalBlock()) {
            block = block.getSide(face);

            if (block.hasComparatorInputOverride()) {
                power = block.getComparatorInputOverride();
            }
        }

        return power;
    }

    @Override
    protected boolean shouldBePowered() {
        int input = this.calculateInputStrength();

        if (input >= 15) {
            return true;
        } else if (input == 0) {
            return false;
        } else {
            int sidePower = this.getPowerOnSides();
            return sidePower == 0 || input >= sidePower;
        }
    }

    private int calculateOutput() {
        return getMode() == Mode.SUBTRACT ? Math.max(this.calculateInputStrength() - this.getPowerOnSides(), 0) : this.calculateInputStrength();
    }

    @PowerNukkitDifference(info = "Trigger observer.", since = "1.4.0.0-PN")
    @Override
    public boolean onActivate(@Nonnull Item item, Player player) {
        if (getMode() == Mode.SUBTRACT) {
            this.setDamage(this.getDamage() - 4);
        } else {
            this.setDamage(this.getDamage() + 4);
        }

        this.level.addLevelEvent(this.add(0.5, 0.5, 0.5), LevelEventPacket.EVENT_SOUND_BUTTON_CLICK, this.getMode() == Mode.SUBTRACT ? 500 : 550);
        this.level.setBlock(this, this, true, false);
        this.level.updateComparatorOutputLevelSelective(this, true);
        //bug?

        this.onChange();
        return true;
    }

    @Override
    public int onUpdate(int type) {
        if (type == Level.BLOCK_UPDATE_SCHEDULED) {
            this.onChange();
            return type;
        }

        return super.onUpdate(type);
    }

    @PowerNukkitDifference(info = "Trigger observer.", since = "1.4.0.0-PN")
    private void onChange() {
        if (!this.level.getServer().isRedstoneEnabled()) {
            return;
        }

        int output = this.calculateOutput();
        // We can't use getOrCreateBlockEntity(), because the update method is called on block place,
        // before the "real" BlockEntity is set. That means, if we'd use the other method here,
        // it would create two BlockEntities.
        BlockEntityComparator blockEntityComparator = getBlockEntity();
        if (blockEntityComparator == null)
            return;

        int currentOutput = blockEntityComparator.getOutputSignal();
        blockEntityComparator.setOutputSignal(output);

        if (currentOutput != output || getMode() == Mode.COMPARE) {
            boolean shouldBePowered = this.shouldBePowered();
            boolean isPowered = this.isPowered();

            if (isPowered && !shouldBePowered) {
                this.level.setBlock(this, getUnpowered(), true, false);
                this.level.updateComparatorOutputLevelSelective(this, true);
            } else if (!isPowered && shouldBePowered) {
                this.level.setBlock(this, getPowered(), true, false);
                this.level.updateComparatorOutputLevelSelective(this, true);
            }

            Block side = this.getSide(getFacing().getOpposite());
            side.onUpdate(Level.BLOCK_UPDATE_REDSTONE);
            RedstoneComponent.updateAroundRedstone(side);
        }
    }

    @Override
    public boolean place(@Nonnull Item item, @Nonnull Block block, @Nonnull Block target, @Nonnull BlockFace face, double fx, double fy, double fz, Player player) {
        Block layer0 = level.getBlock(this, 0);
        Block layer1 = level.getBlock(this, 1);
        if (!super.place(item, block, target, face, fx, fy, fz, player)) {
            return false;
        }
        
        try {
            createBlockEntity(new CompoundTag().putList(new ListTag<>("Items")));
        } catch (Exception e) {
            log.warn("Failed to create the block entity {} at {}", getBlockEntityType(), getLocation(), e);
            level.setBlock(layer0, 0, layer0, true);
            level.setBlock(layer1, 1, layer1, true);
            return false;
        }
        
        onUpdate(Level.BLOCK_UPDATE_REDSTONE);
        return true;
    }

    @Override
    public boolean isPowered() {
        return this.isPowered || (this.getDamage() & 8) > 0;
    }

    @Override
    public Item toItem() {
        return new ItemRedstoneComparator();
    }

    public enum Mode {
        COMPARE,
        SUBTRACT
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.AIR_BLOCK_COLOR;
    }
}
