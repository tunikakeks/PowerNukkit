package cn.nukkit.blockstate;

import cn.nukkit.Server;
import cn.nukkit.api.DeprecationDetails;
import cn.nukkit.api.PowerNukkitOnly;
import cn.nukkit.api.Since;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockID;
import cn.nukkit.block.BlockUnknown;
import cn.nukkit.blockproperty.BlockProperties;
import cn.nukkit.blockproperty.exception.BlockPropertyNotFoundException;
import cn.nukkit.blockstate.exception.InvalidBlockStateException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.Tag;
import cn.nukkit.utils.BinaryStream;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.HumanStringComparator;
import com.google.common.base.Preconditions;
import io.netty.util.internal.EmptyArrays;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.*;
import java.nio.ByteOrder;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@PowerNukkitOnly
@Since("1.4.0.0-PN")
@UtilityClass
@ParametersAreNonnullByDefault
@Log4j2
public class BlockStateRegistry {
    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    public final int BIG_META_MASK = 0xFFFFFFFF;
    private final ExecutorService asyncStateRemover = Executors.newSingleThreadExecutor();
    private final Pattern BLOCK_ID_NAME_PATTERN = Pattern.compile("^blockid:(\\d+)$"); 

    private final Registration updateBlockRegistration;

    private final Map<BlockState, Registration> blockStateRegistration = new ConcurrentHashMap<>();
    private final Map<String, Registration> stateIdRegistration = new ConcurrentHashMap<>();
    private final Int2ObjectMap<Registration> runtimeIdRegistration = new Int2ObjectOpenHashMap<>();

    private final Map<String, BlockState> mappedBlocks = new ConcurrentHashMap<>();

    private final Int2ObjectMap<String> blockIdToPersistenceName = new Int2ObjectOpenHashMap<>();
    private final Map<String, Integer> persistenceNameToBlockId = new LinkedHashMap<>();
    
    private final byte[] blockPaletteBytes;

    private final List<String> knownStateIds;

    //<editor-fold desc="static initialization" defaultstate="collapsed">
    static {

        //<editor-fold desc="Loading block_ids.csv" defaultstate="collapsed">
        try (InputStream stream = Server.class.getClassLoader().getResourceAsStream("block_ids.csv")) { 
            if (stream == null) {
                throw new AssertionError("Unable to locate block_ids.csv");
            }

            int count = 0;
            try(BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    count++;
                    line = line.trim();
                    if (line.isEmpty()) {
                        continue;
                    }
                    String[] parts = line.split(",");
                    Preconditions.checkArgument(parts.length == 2 || parts[0].matches("^[0-9]+$"));
                    if (parts.length > 1 && parts[1].startsWith("minecraft:")) {
                        int id = Integer.parseInt(parts[0]);
                        blockIdToPersistenceName.put(id, parts[1]);
                        persistenceNameToBlockId.put(parts[1], id);
                    }
                }
            } catch (Exception e) {
                throw new IOException("Error reading the line "+count+" of the block_ids.csv", e);
            }
            
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        //</editor-fold>

        //<editor-fold desc="Loading canonical_block_states.nbt" defaultstate="collapsed">
        List<CompoundTag> tags = new ArrayList<>();
        List<String> loadingKnownStateIds = new ArrayList<>();
        try (InputStream stream = Server.class.getClassLoader().getResourceAsStream("canonical_block_states.nbt")) {
            if (stream == null) {
                throw new AssertionError("Unable to locate block state nbt");
            }

            try (BufferedInputStream bis = new BufferedInputStream(stream)) {
                int runtimeId = 0;
                while (bis.available() > 0) {
                    CompoundTag tag = NBTIO.read(bis, ByteOrder.BIG_ENDIAN, true);
                    tag.putInt("runtimeId", runtimeId++);
                    tag.putInt("blockId", persistenceNameToBlockId.getOrDefault(tag.getString("name").toLowerCase(), -1));
                    tags.add(tag);
                    loadingKnownStateIds.add(getStateId(tag));
                }
            }
            knownStateIds = Arrays.asList(loadingKnownStateIds.toArray(EmptyArrays.EMPTY_STRINGS));
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        //</editor-fold>
        Integer infoUpdateRuntimeId = null;

        Set<String> warned = new HashSet<>();

        for (CompoundTag state : tags) {
            int blockId = state.getInt("blockId");
            int runtimeId = state.getInt("runtimeId");
            String name = state.getString("name").toLowerCase();

            if (name.equals("minecraft:unknown")) {
                infoUpdateRuntimeId = runtimeId;
            }
            
            // Special condition: minecraft:wood maps 3 blocks, minecraft:wood, minecraft:log and minecraft:log2
            // All other cases, register the name normally

            // map the following json:
            /*
            {
                "85": {
                    "0": "minecraft:oak_fence",
                    "1": "minecraft:spruce_fence",
                    "2": "minecraft:birch_fence",
                    "3": "minecraft:jungle_fence",
                    "4": "minecraft:acacia_fence",
                    "5": "minecraft:dark_oak_fence"
                },
                "17": {
                    "0": "minecraft:oak_log;pillar_axis=y",
                    "1": "minecraft:spruce_log;pillar_axis=y",
                    "2": "minecraft:birch_log;pillar_axis=y",
                    "3": "minecraft:jungle_log;pillar_axis=y",
                    "4": "minecraft:oak_log;pillar_axis=x",
                    "5": "minecraft:spruce_log;pillar_axis=x",
                    "6": "minecraft:birch_log;pillar_axis=x",
                    "7": "minecraft:jungle_log;pillar_axis=x",
                    "8": "minecraft:oak_log;pillar_axis=z",
                    "9": "minecraft:spruce_log;pillar_axis=z",
                    "10": "minecraft:birch_log;pillar_axis=z",
                    "11": "minecraft:jungle_log;pillar_axis=z"
                },
                "162": {
                    "0": "minecraft:acacia_log;pillar_axis=y",
                    "1": "minecraft:dark_oak_log;pillar_axis=y",
                    "4": "minecraft:acacia_log;pillar_axis=x",
                    "5": "minecraft:dark_oak_log;pillar_axis=x",
                    "8": "minecraft:acacia_log;pillar_axis=z",
                    "9": "minecraft:dark_oak_log;pillar_axis=z"
                }
            }
            */
            // that means, if the name is for example minecraft:oak_log, we need to register the runtime id for minecraft:log with id 17:0
            
            Config cfg = new Config(Config.JSON);
            try (InputStream recipesStream = Server.class.getClassLoader().getResourceAsStream("block_map.json")) {
                cfg.load(Objects.requireNonNull(recipesStream, "Unable to find block_map.json"));
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }

            /*if (cfg.has(name)) {
                var subMap = jsonMap.get(name);
                if (subMap.isJsonObject()) {
                    // we have a submap, so we need to register all the submap entries
                    //if name is minecraft:oak_log, we need to register a redirect for 17:0
                    for (var entry : subMap.entrySet()) {
                        var subName = entry.getValue().getAsString();
                        var subRuntimeId = entry.getKey().getAsInt();
                        
                        //fullBlockId is for example 17:0 or 162:0
                        var fullBlockId = subRuntimeId + ":" + subName.split(";")[0];
                        state.put("blockId", fullBlockId);
                        registerStateId(state, runtimeId);
                    }
                }
            } else */
            
            for(var entry : cfg.entrySet()) {
                var subMap = entry.getValue().getAsJsonObject();
                if (subMap.has(name)) {
                    // we have a submap, so we need to register all the submap entries
                    //if name is minecraft:oak_log, we need to register a redirect for 17:0
                    for (var subEntry : subMap.entrySet()) {
                        String subName = subEntry.getValue().getAsString();
                        int subRuntimeId = subEntry.getKey().getAsInt();
                        
                        //fullBlockId is for example 17:0 or 162:0
                        String fullBlockId = subRuntimeId + ":" + subName.split(";")[0];
                        state.put("blockId", fullBlockId);
                        registerStateId(state, runtimeId);
                    }
                }
            }

            if (isNameOwnerOfId(state.getString("name"), blockId)) {
                registerPersistenceName(blockId, state.getString("name"));
                registerStateId(state, runtimeId);
            } else if (blockId == -1) {
                if (warned.add(name)) {
                    log.warn("Unknown block id for the block named {}", state.getString("name"));
                }
                log.info("Block {} - {}", state, runtimeId);
                registerStateId(state, runtimeId);
            }
        }

        if (infoUpdateRuntimeId == null) {
            throw new IllegalStateException("Could not find the minecraft:info_update runtime id!");
        }
        
        updateBlockRegistration = findRegistrationByRuntimeId(infoUpdateRuntimeId);
        
        try {
            blockPaletteBytes = NBTIO.write(tags, ByteOrder.LITTLE_ENDIAN, true);
        } catch (IOException e) {
            throw new ExceptionInInitializerError(e);
        }
        
    }
    //</editor-fold>

    private boolean isNameOwnerOfId(String name, int blockId) {
        return blockId != -1 && !name.equals("minecraft:wood") || blockId == BlockID.WOOD_BARK;
    }
    
    @Nonnull
    private String getStateId(CompoundTag block) {
        Map<String, String> propertyMap = new TreeMap<>(HumanStringComparator.getInstance());
        for (Tag tag : block.getCompound("states").getAllTags()) {
            propertyMap.put(tag.getName(), tag.parseValue().toString());
        }

        String blockName = block.getString("name").toLowerCase(Locale.ENGLISH);
        Preconditions.checkArgument(!blockName.isEmpty(), "Couldn't find the block name!");
        StringBuilder stateId = new StringBuilder(blockName);
        propertyMap.forEach((name, value) -> stateId.append(';').append(name).append('=').append(value));
        return stateId.toString();
    }

    @Nullable
    private static Registration findRegistrationByRuntimeId(int runtimeId) {
        return runtimeIdRegistration.get(runtimeId);
    }

    @PowerNukkitOnly
    @Since("1.5.2.0-PN")
    @Nullable
    public String getKnownBlockStateIdByRuntimeId(int runtimeId) {
        if (runtimeId >= 0 && runtimeId < knownStateIds.size()) {
            return knownStateIds.get(runtimeId);
        }
        return null;
    }

    @PowerNukkitOnly
    @Since("1.5.2.0-PN")
    public int getKnownRuntimeIdByBlockStateId(String stateId) {
        int result = knownStateIds.indexOf(stateId);
        if (result != -1) {
            return result;
        }
        BlockState state;
        try {
            state = BlockState.of(stateId);
        } catch (NoSuchElementException|IllegalStateException|IllegalArgumentException ignored) {
            return -1;
        }
        String fullStateId = state.getStateId();
        return knownStateIds.indexOf(fullStateId);
    }

    /**
     * @return {@code null} if the runtime id does not matches any known block state.
     */
    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    @Nullable
    public static BlockState getBlockStateByRuntimeId(int runtimeId) {
        Registration registration = findRegistrationByRuntimeId(runtimeId);
        if (registration == null) {
            return null;
        }
        BlockState state = registration.state;
        if (state != null) {
            return state;
        }
        CompoundTag originalBlock = registration.originalBlock;
        if (originalBlock != null) {
            state = buildStateFromCompound(originalBlock);
            if (state != null) {
                registration.state = state;
                registration.originalBlock = null;
            }
        }
        return state;
    }
    
    @Nullable
    private BlockState buildStateFromCompound(CompoundTag block) {
        String name = block.getString("name").toLowerCase(Locale.ENGLISH);
        Integer id = getBlockId(name);
        if (id == null) {
            return null;
        }

        BlockState state = BlockState.of(id);

        CompoundTag properties = block.getCompound("states");
        for (Tag tag : properties.getAllTags()) {
            state = state.withProperty(tag.getName(), tag.parseValue().toString());
        }
        
        return state;
    }

    private static NoSuchElementException runtimeIdNotRegistered(int runtimeId) {
        return new NoSuchElementException("The block id for the runtime id "+runtimeId+" is not registered");
    }

    @PowerNukkitOnly
    @Since("1.5.2.0-PN")
    public int getBlockIdByRuntimeId(int runtimeId) {
        Registration registration = findRegistrationByRuntimeId(runtimeId);
        if (registration == null) {
            throw runtimeIdNotRegistered(runtimeId);
        }
        BlockState state = registration.state;
        if (state != null) {
            return state.getBlockId();
        }
        CompoundTag originalBlock = registration.originalBlock;
        if (originalBlock == null) {
            throw runtimeIdNotRegistered(runtimeId);
        }
        try {
            state = buildStateFromCompound(originalBlock);
        } catch (BlockPropertyNotFoundException e) {
            String name = originalBlock.getString("name").toLowerCase(Locale.ENGLISH);
            Integer id = getBlockId(name);
            if (id == null) {
                throw runtimeIdNotRegistered(runtimeId);
            }
            return id;
        }
        if (state != null) {
            registration.state = state;
            registration.originalBlock = null;
        } else {
            throw runtimeIdNotRegistered(runtimeId);
        }
        return state.getBlockId();
    }

    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    public int getRuntimeId(BlockState state) {
        return getRegistration(convertToNewState(state)).runtimeId;
    }

    private BlockState convertToNewState(BlockState oldState) {
        // Check OldWoodBarkUpdater.java and https://minecraft.fandom.com/wiki/Log#Metadata
        // The Only bark variant is replaced in the client side to minecraft:wood with the same wood type
        if (oldState.getBitSize() == 4 && (oldState.getBlockId() == BlockID.LOG || oldState.getBlockId() == BlockID.LOG2)) {
            int exactInt = oldState.getExactIntStorage();
            if ((exactInt & 0b1100) == 0b1100) {
                int increment = oldState.getBlockId() == BlockID.LOG ? 0b000 : 0b100;
                return BlockState.of(BlockID.WOOD_BARK, (exactInt & 0b11) + increment);
            }
        }
        return oldState;
    }

    private Registration getRegistration(BlockState state) {
        return blockStateRegistration.computeIfAbsent(state, BlockStateRegistry::findRegistration);
    }

    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    public int getRuntimeId(int blockId) {
        return getRuntimeId(BlockState.of(blockId));
    }

    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    @Deprecated
    @DeprecationDetails(reason = "The meta is limited to 32 bits", replaceWith = "getRuntimeId(BlockState state)", since = "1.3.0.0-PN")
    public int getRuntimeId(int blockId, int meta) {
        return getRuntimeId(BlockState.of(blockId, meta));
    }

    private Registration findRegistration(final BlockState state) {
        // Special case for PN-96 PowerNukkit#210 where the world contains blocks like 0:13, 0:7, etc
        if (state.getBlockId() == BlockID.AIR) {
            Registration airRegistration = blockStateRegistration.get(BlockState.AIR);
            if (airRegistration != null) {
                return new Registration(state, airRegistration.runtimeId, null);
            }
        }

        Registration registration = findRegistrationByStateId(state);
        removeStateIdsAsync(registration);
        return registration;
    }

    private Registration findRegistrationByStateId(BlockState state) {
        Registration registration;
        try {
            registration = stateIdRegistration.remove(state.getStateId());
            if (registration != null) {
                registration.state = state;
                registration.originalBlock = null;
                return registration;
            }
        } catch (Exception e) {
            try {
                log.fatal("An error has occurred while trying to get the stateId of state: "
                        + "{}:{}"
                        + " - {}"
                        + " - {}",
                        state.getBlockId(),
                        state.getDataStorage(),
                        state.getProperties(),
                        blockIdToPersistenceName.get(state.getBlockId()),
                        e);
            } catch (Exception e2) {
                e.addSuppressed(e2);
                log.fatal("An error has occurred while trying to get the stateId of state: {}:{}",
                        state.getBlockId(), state.getDataStorage(), e);
            }
        }
        
        try {
            registration = stateIdRegistration.remove(state.getLegacyStateId());
            if (registration != null) {
                registration.state = state;
                registration.originalBlock = null;
                return registration;
            }
        } catch (Exception e) {
            log.fatal("An error has occurred while trying to parse the legacyStateId of {}:{}", state.getBlockId(), state.getDataStorage(), e);
        }

        return logDiscoveryError(state);
    }
    
    private void removeStateIdsAsync(@Nullable Registration registration) {
        if (registration != null && registration != updateBlockRegistration) {
            asyncStateRemover.submit(() -> stateIdRegistration.values().removeIf(r -> r.runtimeId == registration.runtimeId));
        }
    }

    private Registration logDiscoveryError(BlockState state) {
        log.error("Found an unknown BlockId:Meta combination: {}:{}"
                + " - {}"
                + " - {}"
                + " - {}"
                + ", trying to repair or replacing with an \"UPDATE!\" block.",
                state.getBlockId(), state.getDataStorage(), state.getStateId(), state.getProperties(),
                blockIdToPersistenceName.get(state.getBlockId())
                );
        return updateBlockRegistration;
    }
    
    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    public List<String> getPersistenceNames() {
        return new ArrayList<>(persistenceNameToBlockId.keySet());
    }

    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    @Nonnull
    public String getPersistenceName(int blockId) {
        String persistenceName = blockIdToPersistenceName.get(blockId);
        if (persistenceName == null) {
            String fallback = "blockid:"+ blockId;
            log.warn("The persistence name of the block id {} is unknown! Using {} as an alternative!", blockId, fallback);
            registerPersistenceName(blockId, fallback);
            return fallback;
        }
        return persistenceName;
    }

    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    public void registerPersistenceName(int blockId, String persistenceName) {
        synchronized (blockIdToPersistenceName) {
            String newName = persistenceName.toLowerCase();
            String oldName = blockIdToPersistenceName.putIfAbsent(blockId, newName);
            if (oldName != null && !persistenceName.equalsIgnoreCase(oldName)) {
                throw new UnsupportedOperationException("The persistence name registration tried to replaced a name. Name:" + persistenceName + ", Old:" + oldName + ", Id:" + blockId);
            }
            Integer oldId = persistenceNameToBlockId.putIfAbsent(newName, blockId);
            if (oldId != null && blockId != oldId) {
                blockIdToPersistenceName.remove(blockId);
                throw new UnsupportedOperationException("The persistence name registration tried to replaced an id. Name:" + persistenceName + ", OldId:" + oldId + ", Id:" + blockId);
            }
        }
    }

    private void registerStateId(CompoundTag block, int runtimeId) {
        String stateId = getStateId(block);
        Registration registration = new Registration(null, runtimeId, block);
        
        Registration old = stateIdRegistration.putIfAbsent(stateId, registration);
        if (old != null && !old.equals(registration)) {
            throw new UnsupportedOperationException("The persistence NBT registration tried to replaced a runtime id. Old:"+old+", New:"+runtimeId+", State:"+stateId);
        }
        
        runtimeIdRegistration.put(runtimeId, registration);
    }
    
    private void registerState(int blockId, int meta, CompoundTag originalState, int runtimeId) {
        BlockState state = BlockState.of(blockId, meta);
        Registration registration = new Registration(state, runtimeId, null);

        Registration old = blockStateRegistration.putIfAbsent(state, registration);
        if (old != null && !registration.equals(old)) {
            throw new UnsupportedOperationException("The persistence NBT registration tried to replaced a runtime id. Old:"+old+", New:"+runtimeId+", State:"+state);
        }
        runtimeIdRegistration.put(runtimeId, registration);
        
        stateIdRegistration.remove(getStateId(originalState));
        stateIdRegistration.remove(state.getLegacyStateId());
    }

    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    public int getBlockPaletteDataVersion() {
        @SuppressWarnings("UnnecessaryLocalVariable")
        Object obj = blockPaletteBytes;
        return obj.hashCode();
    }

    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    @Nonnull
    public byte[] getBlockPaletteBytes() {
        return blockPaletteBytes.clone();
    }

    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    public void putBlockPaletteBytes(BinaryStream stream) {
        stream.put(blockPaletteBytes);
    }

    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    public int getBlockPaletteLength() {
        return blockPaletteBytes.length;
    }

    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    public void copyBlockPaletteBytes(byte[] target, int targetIndex) {
        System.arraycopy(blockPaletteBytes, 0, target, targetIndex, blockPaletteBytes.length);
    }

    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    @SuppressWarnings({"deprecation", "squid:CallToDepreca"})
    @Nonnull
    public BlockProperties getProperties(int blockId) {
        int fullId = blockId << Block.DATA_BITS;
        Block block;
        if (fullId >= Block.fullList.length || fullId < 0 || (block = Block.fullList[fullId]) == null) {
            return BlockUnknown.PROPERTIES;
        }
        return block.getProperties();
    }

    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    @Nonnull
    public MutableBlockState createMutableState(int blockId) {
        return getProperties(blockId).createMutableState(blockId);
    }

    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    @Nonnull
    public MutableBlockState createMutableState(int blockId, int bigMeta) {
        MutableBlockState blockState = createMutableState(blockId);
        blockState.setDataStorageFromInt(bigMeta);
        return blockState;
    }

    /**
     * @throws InvalidBlockStateException
     */
    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    @Nonnull
    public MutableBlockState createMutableState(int blockId, Number storage) {
        MutableBlockState blockState = createMutableState(blockId);
        blockState.setDataStorage(storage);
        return blockState;
    }

    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    public int getUpdateBlockRegistration() {
        return updateBlockRegistration.runtimeId;
    }

    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    @Nullable
    public Integer getBlockId(String persistenceName) {
        Integer blockId = persistenceNameToBlockId.get(persistenceName);
        if (blockId != null) {
            return blockId;
        }
        
        Matcher matcher = BLOCK_ID_NAME_PATTERN.matcher(persistenceName);
        if (matcher.matches()) {
            try {
                return Integer.parseInt(matcher.group(1));
            } catch (NumberFormatException ignored) {
            }
        }
        return null;
    }

    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    public int getFallbackRuntimeId() {
        return updateBlockRegistration.runtimeId;
    }

    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    public BlockState getFallbackBlockState() {
        return updateBlockRegistration.state;
    }
    
    @AllArgsConstructor
    @ToString
    @EqualsAndHashCode
    private static class Registration {
        @Nullable
        private BlockState state;
        
        private final int runtimeId;
        
        @Nullable
        private CompoundTag originalBlock;
    }
}
