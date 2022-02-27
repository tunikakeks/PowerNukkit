package cn.nukkit.blockstate;

import cn.nukkit.api.*;
import cn.nukkit.block.Block;
import cn.nukkit.blockproperty.BlockProperties;
import cn.nukkit.blockproperty.BlockProperty;
import cn.nukkit.blockproperty.exception.InvalidBlockPropertyException;
import cn.nukkit.blockstate.exception.InvalidBlockStateException;
import cn.nukkit.math.NukkitMath;
import cn.nukkit.utils.Validation;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

import static cn.nukkit.api.API.Definition.INTERNAL;
import static cn.nukkit.api.API.Usage.INCUBATING;
import static cn.nukkit.blockstate.IMutableBlockState.handleUnsupportedStorageType;

@PowerNukkitOnly
@Since("1.4.0.0-PN")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@ParametersAreNonnullByDefault
public class IntMutableBlockState extends MutableBlockState {
    private int storage;

    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    public IntMutableBlockState(int blockId, BlockProperties properties, int state) {
        super(blockId, properties);
        this.storage = state;
    }

    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    public IntMutableBlockState(int blockId, BlockProperties properties) {
        this(blockId, properties, 0);
    }

    @Nonnegative
    @Deprecated
    @DeprecationDetails(reason = "Can't store all data, exists for backward compatibility reasons", since = "1.4.0.0-PN", replaceWith = "getDataStorage()")
    @Override
    @PowerNukkitOnly
    public int getLegacyDamage() {
        return storage & Block.DATA_MASK;
    }

    @Unsigned
    @Deprecated
    @DeprecationDetails(reason = "Can't store all data, exists for backward compatibility reasons", since = "1.4.0.0-PN", replaceWith = "getDataStorage()")
    @Override
    @PowerNukkitOnly
    public int getBigDamage() {
        return storage;
    }

    @Nonnegative
    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    @Nonnull
    @Override
    public BigInteger getHugeDamage() {
        return BigInteger.valueOf(storage);
    }

    @Nonnegative
    @Nonnull
    @PowerNukkitOnly
    @Override
    public Integer getDataStorage() {
        return storage;
    }

    @Since("1.4.0.0-PN")
    @PowerNukkitOnly
    @Override
    public boolean isDefaultState() {
        return storage == 0;
    }

    @Since("1.4.0.0-PN")
    @PowerNukkitOnly
    @Override
    public void setDataStorage(@Nonnegative Number storage) {
        Class<? extends Number> c = storage.getClass();
        int state;
        if (c == Integer.class || c == Short.class || c == Byte.class) {
            state = storage.intValue();
        } else {
            try {
                state = new BigDecimal(storage.toString()).intValueExact();
            } catch (ArithmeticException | NumberFormatException e) {
                throw handleUnsupportedStorageType(getBlockId(), storage, e);
            }
        }
        setDataStorageFromInt(state);
    }

    @Since("1.4.0.0-PN")
    @PowerNukkitOnly
    @Override
    public void setDataStorageFromInt(@Nonnegative int storage) {
        validate(storage);
        this.storage = storage;
    }

    @Override
    @API(definition = INTERNAL, usage = INCUBATING)
    void setDataStorageWithoutValidation(Number storage) {
        this.storage = storage.intValue();
    }

    @PowerNukkitOnly
    @Override
    public void validate() {
        validate(storage);
    }
    
    private void validate(int state) {
        if (state == 0) {
            return;
        }

        Validation.checkPositive("state", state);
        
        int bitLength = NukkitMath.bitLength(state);
        if (bitLength > properties.getBitSize()) {
            throw new InvalidBlockStateException(
                    BlockState.of(getBlockId(), state),
                    "The state have more data bits than specified in the properties. Bits: " + bitLength + ", Max: " + properties.getBitSize()
            );
        }

        try {
            BlockProperties properties = this.properties;
            for (String name : properties.getNames()) {
                BlockProperty<?> property = properties.getBlockProperty(name);
                property.validateMeta(state, properties.getOffset(name));
            }
        } catch (InvalidBlockPropertyException e) {
            throw new InvalidBlockStateException(BlockState.of(getBlockId(), state), e);
        }
    }

    @Since("1.4.0.0-PN")
    @PowerNukkitOnly
    @Override
    public void setBooleanValue(String propertyName, boolean value) {
        storage = properties.setBooleanValue(storage, propertyName, value);
    }

    @Since("1.4.0.0-PN")
    @PowerNukkitOnly
    @Override
    public void setPropertyValue(String propertyName, @Nullable Serializable value) {
        storage = properties.setValue(storage, propertyName, value);
    }

    @Since("1.4.0.0-PN")
    @PowerNukkitOnly
    @Override
    public void setIntValue(String propertyName, int value) {
        storage = properties.setIntValue(storage, propertyName, value);
    }

    @PowerNukkitOnly
    @Nonnull
    @Override
    public Serializable getPropertyValue(String propertyName) {
        return properties.getValue(storage, propertyName);
    }

    @PowerNukkitOnly
    @Override
    public int getIntValue(String propertyName) {
        return properties.getIntValue(storage, propertyName);
    }

    @PowerNukkitOnly
    @Override
    public boolean getBooleanValue(String propertyName) {
        return properties.getBooleanValue(storage, propertyName);
    }

    @PowerNukkitOnly
    @Nonnull
    @Override
    public String getPersistenceValue(String propertyName) {
        return properties.getPersistenceValue(storage, propertyName);
    }

    @Nonnull
    @Override
    @PowerNukkitOnly
    public BlockState getCurrentState() {
        return BlockState.of(blockId, storage);
    }

    @Since("1.4.0.0-PN")
    @PowerNukkitOnly
    @Override
    public int getExactIntStorage() {
        return storage;
    }

    @PowerNukkitOnly
    @Nonnull
    @Override
    public IntMutableBlockState copy() {
        return new IntMutableBlockState(blockId, properties, storage);
    }
}
