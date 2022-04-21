package cn.nukkit.event.block;

import cn.nukkit.Player;
import cn.nukkit.api.DeprecationDetails;
import cn.nukkit.api.PowerNukkitOnly;
import cn.nukkit.api.Since;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockAnvil;
import cn.nukkit.blockproperty.value.AnvilDamage;
import cn.nukkit.blockstate.BlockState;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.inventory.transaction.CraftingTransaction;
import com.google.common.base.Preconditions;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Since("1.1.1.0-PN")
public class AnvilDamageEvent extends BlockEvent implements Cancellable {
    
    private static final HandlerList handlers = new HandlerList();

    @Since("1.1.1.0-PN")
    public static HandlerList getHandlers() {
        return handlers;
    }

    @Nullable
    private final Player player;

    @Nullable
    private final CraftingTransaction transaction;

    @Nonnull
    private final DamageCause cause;

    @Nonnull
    private final BlockState oldState;

    @Nonnull
    private BlockState newState;

    @Since("1.4.0.0-PN")
    public AnvilDamageEvent(@Nonnull Block block, int oldDamage, int newDamage, @Nonnull DamageCause cause, @Nullable Player player) {
        this(adjustBlock(block, oldDamage), block.getCurrentState().withData(newDamage), player, null, cause);
    }

    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    public AnvilDamageEvent(@Nonnull Block block, @Nonnull Block newState, @Nullable Player player, @Nullable CraftingTransaction transaction, @Nonnull DamageCause cause) {
        this(block, newState.getCurrentState(), player, transaction, cause);
    }

    @PowerNukkitOnly
    @Since("1.6.0.0-PN")
    public AnvilDamageEvent(@Nonnull Block block, @Nonnull BlockState newState, @Nullable Player player, @Nullable CraftingTransaction transaction, @Nonnull DamageCause cause) {
        super(Preconditions.checkNotNull(block, "block").clone());
        this.oldState = block.getCurrentState();
        this.player = player;
        this.transaction = transaction;
        this.cause = Preconditions.checkNotNull(cause, "cause");
        this.newState = Preconditions.checkNotNull(newState, "newState");
    }
    
    @PowerNukkitOnly
    @Since("1.1.1.0-PN")
    @Nullable
    public CraftingTransaction getTransaction() {
        return transaction;
    }
    
    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    @Nonnull
    public DamageCause getDamageCause() {
        return cause;
    }

    @Deprecated
    @DeprecationDetails(since = "1.6.0.0-PN", by = "PowerNukkit", reason = "Unstable use of raw block state data", replaceWith = "getOldAnvilDamage or getOldBlockState")
    @Since("1.4.0.0-PN")
    public int getOldDamage() {
        if (!block.getProperties().contains(BlockAnvil.DAMAGE)) {
            return 0;
        }
        return block.getIntValue(BlockAnvil.DAMAGE);
    }

    @PowerNukkitOnly
    @Since("1.6.0.0-PN")
    @Nullable
    public AnvilDamage getOldAnvilDamage() {
        if (oldState.getProperties().contains(BlockAnvil.DAMAGE)) {
            return oldState.getPropertyValue(BlockAnvil.DAMAGE);
        }
        return null;
    }

    @PowerNukkitOnly
    @Since("1.6.0.0-PN")
    @Nonnull
    public BlockState getOldBlockState() {
        return oldState;
    }

    @PowerNukkitOnly
    @Since("1.6.0.0-PN")
    @Nonnull
    public BlockState getNewBlockState() {
        return newState;
    }

    @PowerNukkitOnly("Used to be inherited from BlockFadeEvent")
    @Since("1.1.1.0-PN")
    @Nonnull
    public Block getNewState() {
        return newState.getBlockRepairing(block);
    }

    @Deprecated
    @DeprecationDetails(since = "1.6.0.0-PN", by = "PowerNukkit", reason = "Unstable use of raw block state data", replaceWith = "getNewAnvilDamage or getNewBlockState")
    @Since("1.4.0.0-PN")
    public int getNewDamage() {
        BlockState newBlockState = getNewBlockState();
        return newBlockState.getProperties().contains(BlockAnvil.DAMAGE)? newBlockState.getIntValue(BlockAnvil.DAMAGE) : 0;
    }

    @PowerNukkitOnly
    @Since("1.6.0.0-PN")
    public void setNewBlockState(@Nonnull BlockState state) {
        this.newState = Preconditions.checkNotNull(state);
    }

    @Deprecated
    @DeprecationDetails(since = "1.6.0.0-PN", by = "PowerNukkit", reason = "Unstable use of raw block state data",
            replaceWith = "setNewBlockState example: setNewBlockState(BlockState.of(BlockID.ANVIL).withProperty(BlockAnvil.DAMAGE, AnvilDamage.VERY_DAMAGED))")
    @Since("1.4.0.0-PN")
    public void setNewDamage(int newDamage) {
        BlockState newBlockState = getNewBlockState();
        if (newBlockState.getProperties().contains(BlockAnvil.DAMAGE)) {
            this.setNewBlockState(newBlockState.withProperty(BlockAnvil.DAMAGE, BlockAnvil.DAMAGE.getValueForMeta(newDamage)));
        }
    }

    @PowerNukkitOnly
    @Since("1.6.0.0-PN")
    public void setNewState(@Nonnull Block block) {
        this.newState = block.getCurrentState();
    }

    @Since("1.6.0.0-PN")
    @Nonnull
    public DamageCause getCause() {
        return this.cause;
    }

    @Since("1.1.1.0-PN")
    @Nullable
    public Player getPlayer() {
        return this.player;
    }

    @Since("1.4.0.0-PN")
    public enum DamageCause {
        @Since("1.4.0.0-PN") USE,
        @Since("1.4.0.0-PN") FALL
    }

    private static Block adjustBlock(Block block, int damage) {
        Block adjusted = Preconditions.checkNotNull(block, "block").clone();
        adjusted.setDataStorage(damage);
        return adjusted;
    }
}
