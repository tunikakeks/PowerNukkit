package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.api.PowerNukkitDifference;
import cn.nukkit.api.PowerNukkitOnly;
import cn.nukkit.api.Since;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.blockentity.BlockEntityBed;
import cn.nukkit.blockproperty.BlockProperties;
import cn.nukkit.blockproperty.BooleanBlockProperty;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.block.BlockExplosionPrimeEvent;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBed;
import cn.nukkit.lang.TranslationContainer;
import cn.nukkit.level.Explosion;
import cn.nukkit.level.GameRule;
import cn.nukkit.level.Level;
import cn.nukkit.level.Location;
import cn.nukkit.math.AxisAlignedBB;
import cn.nukkit.math.BlockFace;
import cn.nukkit.math.SimpleAxisAlignedBB;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.potion.Effect;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.DyeColor;
import cn.nukkit.utils.Faceable;
import cn.nukkit.utils.TextFormat;
import lombok.extern.log4j.Log4j2;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static cn.nukkit.blockproperty.CommonBlockProperties.DIRECTION;

/**
 * @author MagicDroidX (Nukkit Project)
 */
@PowerNukkitDifference(since = "1.4.0.0-PN", info = "Implements BlockEntityHolder only in PowerNukkit")
@Log4j2
public class BlockBed extends BlockTransparentMeta implements Faceable, BlockEntityHolder<BlockEntityBed> {
    
    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    public static final BooleanBlockProperty HEAD_PIECE = new BooleanBlockProperty("head_piece_bit", false);

    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    public static final BooleanBlockProperty OCCUPIED = new BooleanBlockProperty("occupied_bit", false);
    
    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    public static final BlockProperties PROPERTIES = new BlockProperties(DIRECTION, OCCUPIED, HEAD_PIECE);

    public BlockBed() {
        this(0);
    }

    public BlockBed(int meta) {
        super(meta);
    }

    @Override
    public int getId() {
        return BED_BLOCK;
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
    public Class<? extends BlockEntityBed> getBlockEntityClass() {
        return BlockEntityBed.class;
    }

    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    @Nonnull
    @Override
    public String getBlockEntityType() {
        return BlockEntity.BED;
    }

    @Override
    public boolean canBeActivated() {
        return true;
    }

    @Override
    public double getResistance() {
        return 1;
    }

    @Override
    public double getHardness() {
        return 0.2;
    }

    @Override
    public String getName() {
        return this.getDyeColor().getName() + " Bed Block";
    }

    @Override
    public double getMaxY() {
        return this.y + 0.5625;
    }

    @PowerNukkitOnly
    @Override
    public int getWaterloggingLevel() {
        return 1;
    }

    @Override
    public boolean onActivate(@Nonnull Item item) {
        return this.onActivate(item, null);
    }

    @Override
    public boolean onActivate(@Nonnull Item item, Player player) {

        BlockFace dir = getBlockFace();

        boolean shouldExplode = this.level.getDimension() != Level.DIMENSION_OVERWORLD;
        boolean willExplode = shouldExplode && this.level.getGameRules().getBoolean(GameRule.RESPAWN_BLOCKS_EXPLODE);
        
        Block head;
        if (isHeadPiece()) {
            head = this;
        } else {
            head = getSide(dir);
            if (head.getId() != getId() || !((BlockBed) head).isHeadPiece() || !((BlockBed) head).getBlockFace().equals(dir)) {
                if (player != null && !willExplode) {
                    player.sendMessage(new TranslationContainer(TextFormat.GRAY + "%tile.bed.notValid"));
                }
                
                if (!shouldExplode) {
                    return true;
                }
            }
        }

        BlockFace footPart = dir.getOpposite();

        if (shouldExplode) {
            if (!willExplode) {
                return true;
            }

            BlockExplosionPrimeEvent event = new BlockExplosionPrimeEvent(this, 5);
            event.setIncendiary(true);
            if (event.isCancelled()) {
                return true;
            }
            
            level.setBlock(this, get(AIR), false, false);
            onBreak(Item.getBlock(BlockID.AIR));
            level.updateAround(this);

            Explosion explosion = new Explosion(this, event.getForce(), this);
            explosion.setFireChance(event.getFireChance());
            if (event.isBlockBreaking()) {
                explosion.explodeA();
            }
            explosion.explodeB();
            return true;
        }

        if (player == null || !player.hasEffect(Effect.CONDUIT_POWER) && getLevelBlockAtLayer(1) instanceof BlockWater) {
            return true;
        }

        AxisAlignedBB accessArea = new SimpleAxisAlignedBB(head.x - 2, head.y - 5.5, head.z - 2, head.x + 3, head.y + 2.5, head.z + 3)
                .addCoord(footPart.getXOffset(), 0, footPart.getZOffset());

        if (!accessArea.isVectorInside(player)) {
            player.sendMessage(new TranslationContainer(TextFormat.GRAY + "%tile.bed.tooFar"));
            return true;
        }

        Location spawn = Location.fromObject(head.add(0.5, 0.5, 0.5), player.getLevel(), player.getYaw(), player.getPitch());
        if (!player.getSpawn().equals(spawn)) {
            player.setSpawn(spawn);
        }
        player.sendMessage(new TranslationContainer(TextFormat.GRAY + "%tile.bed.respawnSet"));

        int time = this.getLevel().getTime() % Level.TIME_FULL;

        boolean isNight = (time >= Level.TIME_NIGHT && time < Level.TIME_SUNRISE);

        if (!isNight) {
            player.sendMessage(new TranslationContainer(TextFormat.GRAY + "%tile.bed.noSleep"));
            return true;
        }

        if (!player.isCreative()) {
            AxisAlignedBB checkMonsterArea = new SimpleAxisAlignedBB(head.x - 8, head.y - 6.5, head.z - 8, head.x + 9, head.y + 5.5, head.z + 9)
                    .addCoord(footPart.getXOffset(), 0, footPart.getZOffset());

            for (Entity entity : this.level.getCollidingEntities(checkMonsterArea)) {
                if (!entity.isClosed() && entity.isPreventingSleep(player)) {
                    player.sendTranslation(TextFormat.GRAY + "%tile.bed.notSafe");
                    return true;
                }
                // TODO: Check Chicken Jockey, Spider Jockey
            }
        }

        if (!player.sleepOn(head)) {
            player.sendMessage(new TranslationContainer(TextFormat.GRAY + "%tile.bed.occupied"));
        }

        return true;
    }

    @PowerNukkitDifference(since = "1.4.0.0-PN", info = "Fixed support logic")
    @Override
    public boolean place(@Nonnull Item item, @Nonnull Block block, @Nonnull Block target, @Nonnull BlockFace face, double fx, double fy, double fz, @Nullable Player player) {
        Block down = this.down();
        if (!(BlockLever.isSupportValid(down, BlockFace.UP) || down instanceof BlockCauldron)) {
            return false;
        }
        
        BlockFace direction = player == null? BlockFace.NORTH : player.getDirection();
        Block next = this.getSide(direction);
        Block downNext = next.down();

        if (!next.canBeReplaced() || !(BlockLever.isSupportValid(downNext, BlockFace.UP) || downNext instanceof BlockCauldron)) {
            return false;
        }
        
        Block thisLayer0 = level.getBlock(this, 0);
        Block thisLayer1 = level.getBlock(this, 1);
        Block nextLayer0 = level.getBlock(next, 0);
        Block nextLayer1 = level.getBlock(next, 1);
        
        setBlockFace(direction);

        level.setBlock(block, this, true, true);
        if (next instanceof BlockLiquid && ((BlockLiquid) next).usesWaterLogging()) {
            level.setBlock(next, 1, next, true, false);
        }
        
        BlockBed head = (BlockBed) clone();
        head.setHeadPiece(true);
        level.setBlock(next, head, true, true);
        
        BlockEntityBed thisBed = null;
        try {
            thisBed = createBlockEntity(new CompoundTag().putByte("color", item.getDamage()));
            BlockEntityHolder<?> nextBlock = (BlockEntityHolder<?>) next.getLevelBlock();
            nextBlock.createBlockEntity(new CompoundTag().putByte("color", item.getDamage()));
        } catch (Exception e) {
            log.warn("Failed to create the block entity {} at {} and {}", getBlockEntityType(), getLocation(), next.getLocation(), e);
            if (thisBed != null) {
                thisBed.close();
            }
            level.setBlock(thisLayer0, 0, thisLayer0, true);
            level.setBlock(thisLayer1, 1, thisLayer1, true);
            level.setBlock(nextLayer0, 0, nextLayer0, true);
            level.setBlock(nextLayer1, 1, nextLayer1, true);
            return false;
        }
        return true;
    }

    @Override
    public boolean onBreak(Item item) {
        BlockFace direction = getBlockFace();
        if (isHeadPiece()) { //This is the Top part of bed
            Block other = getSide(direction.getOpposite());
            if (other.getId() == getId() && !((BlockBed) other).isHeadPiece() && direction.equals(((BlockBed) other).getBlockFace())) {
                getLevel().setBlock(other, Block.get(BlockID.AIR), true, true);
            }
        } else { //Bottom Part of Bed
            Block other = getSide(direction);
            if (other.getId() == getId() && ((BlockBed) other).isHeadPiece() && direction.equals(((BlockBed) other).getBlockFace())) {
                getLevel().setBlock(other, Block.get(BlockID.AIR), true, true);
            }
        }

        getLevel().setBlock(this, Block.get(BlockID.AIR), true, false); // Do not update both parts to prevent duplication bug if there is two fallable blocks top of the bed

        return true;
    }

    @Override
    public Item toItem() {
        return new ItemBed(this.getDyeColor().getWoolData());
    }

    @Override
    public BlockColor getColor() {
        return this.getDyeColor().getColor();
    }

    public DyeColor getDyeColor() {
        if (this.level != null) {
            BlockEntityBed blockEntity = getBlockEntity();

            if (blockEntity != null) {
                return blockEntity.getDyeColor();
            }
        }

        return DyeColor.WHITE;
    }

    @Override
    public BlockFace getBlockFace() {
        return getPropertyValue(DIRECTION);
    }

    @Since("1.4.0.0-PN")
    @PowerNukkitOnly
    @Override
    public void setBlockFace(BlockFace face) {
        setPropertyValue(DIRECTION, face);
    }
    
    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    public boolean isHeadPiece() {
        return getBooleanValue(HEAD_PIECE);
    }

    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    public void setHeadPiece(boolean headPiece) {
        setBooleanValue(HEAD_PIECE, headPiece);
    }

    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    public boolean isOccupied() {
        return getBooleanValue(OCCUPIED);
    }

    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    public void setOccupied(boolean occupied) {
        setBooleanValue(OCCUPIED, occupied);
    }

    @Override
    @PowerNukkitOnly
    public boolean breaksWhenMoved() {
        return true;
    }

    @Override
    @PowerNukkitOnly
    public  boolean sticksToPiston() {
        return false;
    }

    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    public boolean isBedValid() {
        BlockFace dir = getBlockFace();
        Block head;
        Block foot;
        if (isHeadPiece()) {
            head = this;
            foot = getSide(dir.getOpposite());
        } else {
            head = getSide(dir);
            foot = this;
        }
        
        return head.getId() == foot.getId() 
                && ((BlockBed) head).isHeadPiece() && ((BlockBed) head).getBlockFace().equals(dir)
                && !((BlockBed) foot).isHeadPiece() && ((BlockBed) foot).getBlockFace().equals(dir);
    }

    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    @Nullable
    public BlockBed getHeadPart() {
        if (isHeadPiece()) {
            return this;
        }
        BlockFace dir = getBlockFace();
        Block head = getSide(dir);
        if (head.getId() == getId() && ((BlockBed) head).isHeadPiece() && ((BlockBed) head).getBlockFace().equals(dir)) {
            return (BlockBed) head;
        }
        return null;
    }

    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    @Nullable
    public BlockBed getFootPart() {
        if (!isHeadPiece()) {
            return this;
        }
        
        BlockFace dir = getBlockFace();
        Block foot = getSide(dir.getOpposite());
        if (foot.getId() == getId() && !((BlockBed) foot).isHeadPiece() && ((BlockBed) foot).getBlockFace().equals(dir)) {
            return (BlockBed) foot;
        }
        return null;
    }
}
