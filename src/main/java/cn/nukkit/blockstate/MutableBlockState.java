package cn.nukkit.blockstate;

import cn.nukkit.api.API;
import cn.nukkit.api.DeprecationDetails;
import cn.nukkit.api.PowerNukkitOnly;
import cn.nukkit.api.Since;
import cn.nukkit.blockproperty.BlockProperties;
import cn.nukkit.blockstate.exception.InvalidBlockStateException;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static cn.nukkit.api.API.Definition.INTERNAL;
import static cn.nukkit.api.API.Usage.INCUBATING;

@PowerNukkitOnly
@Since("1.4.0.0-PN")
@ToString
@EqualsAndHashCode
@ParametersAreNonnullByDefault
public abstract class MutableBlockState implements IMutableBlockState {
    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    protected final int blockId;

    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    protected final BlockProperties properties;

    MutableBlockState(int blockId, BlockProperties properties) {
        this.blockId = blockId;
        this.properties = properties;
    }

    @API(definition = INTERNAL, usage = INCUBATING)
    void setDataStorageWithoutValidation(Number storage) {
        setDataStorage(storage);
    }

    @Since("1.4.0.0-PN")
    @PowerNukkitOnly
    @Override
    public void setState(IBlockState state) throws InvalidBlockStateException {
        if (state.getBlockId() == getBlockId()) {
            if (BlockState.class == state.getClass() && ((BlockState) state).isCachedValidationValid()) {
                setDataStorageWithoutValidation(state.getDataStorage());
            } else {
                setDataStorage(state.getDataStorage());
            }
        } else {
            IMutableBlockState.super.setState(state);
        }
    }

    @PowerNukkitOnly
    @Nonnull
    @Override
    public final BlockProperties getProperties() {
        return properties;
    }

    @PowerNukkitOnly
    @Nonnegative
    @Override
    public final int getBlockId() {
        return blockId;
    }

    @PowerNukkitOnly
    @Override
    @Deprecated
    @DeprecationDetails(reason = "Can't store all data, exists for backward compatibility reasons", since = "1.4.0.0-PN", replaceWith = "the BlockState itself")
    public final int getFullId() {
        return IMutableBlockState.super.getFullId();
    }

    @Override
    @Deprecated
    @DeprecationDetails(reason = "Can't store all data, exists for backward compatibility reasons", since = "1.4.0.0-PN", replaceWith = "the BlockState itself")
    @PowerNukkitOnly
    public final long getBigId() {
        return IMutableBlockState.super.getBigId();
    }

    @PowerNukkitOnly
    @Override
    public final int getBitSize() {
        return getProperties().getBitSize();
    }

    /**
     * @throws cn.nukkit.blockstate.exception.InvalidBlockStateException if the state is invalid
     */
    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    public abstract void validate();

    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    @Nonnull
    public abstract MutableBlockState copy();
}
