package cn.nukkit.item;

import cn.nukkit.Server;
import cn.nukkit.api.PowerNukkitOnly;
import cn.nukkit.api.Since;
import cn.nukkit.item.RuntimeItems.MappingEntry;
import cn.nukkit.utils.BinaryStream;
import com.google.common.base.Preconditions;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import lombok.var;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.util.*;
import java.util.function.Supplier;

@Log4j2
public class RuntimeItemMapping {
    private final Int2ObjectMap<LegacyEntry> runtime2Legacy = new Int2ObjectOpenHashMap<>();
    private final Int2ObjectMap<RuntimeEntry> legacy2Runtime = new Int2ObjectOpenHashMap<>();//legacyFullID to Runtime
    private final Map<String, LegacyEntry> identifier2Legacy = new HashMap<>();
    @Since("1.19.70-r2")
    private final List<RuntimeEntry> itemPaletteEntries = new ArrayList<>();
    @Since("1.19.70-r2")
    private final Int2ObjectMap<String> runtimeId2Name = new Int2ObjectOpenHashMap<>();
    @Since("1.19.70-r2")
    private final Object2IntMap<String> name2RuntimeId = new Object2IntOpenHashMap<>();
    @Since("1.19.70-r2")
    private final Map<String, Supplier<Item>> namespacedIdItem = new HashMap<>();

    private static final Int2ObjectOpenHashMap<String> mappingEntries = new Int2ObjectOpenHashMap<>();
    private byte[] itemPalette;

    public RuntimeItemMapping(Map<String, MappingEntry> mappings) {
        try (InputStream stream = Server.class.getClassLoader().getResourceAsStream("runtime_item_states.json")) {
            if (stream == null) {
                throw new AssertionError("Unable to load runtime_item_states.json");
            }
            JsonArray runtimeItems = JsonParser.parseReader(new InputStreamReader(stream)).getAsJsonArray();

            for (JsonElement element : runtimeItems) {
                if (!element.isJsonObject()) {
                    throw new IllegalStateException("Invalid entry");
                }
                JsonObject entry = element.getAsJsonObject();
                String identifier = entry.get("name").getAsString();
                int runtimeId = entry.get("id").getAsInt();
                this.runtimeId2Name.put(runtimeId, identifier);
                this.name2RuntimeId.put(identifier, runtimeId);
                boolean hasDamage = false;
                int damage = 0;
                int legacyId;
                if (mappings.containsKey(identifier)) {
                    MappingEntry mapping = mappings.get(identifier);
                    legacyId = RuntimeItems.getLegacyIdFromLegacyString(mapping.legacyName());
                    if (legacyId == -1) {
                        throw new IllegalStateException("Unable to match  " + mapping + " with legacyId");
                    }
                    damage = mapping.damage();
                    hasDamage = true;
                } else {
                    legacyId = RuntimeItems.getLegacyIdFromLegacyString(identifier);
                    if (legacyId == -1) {
                        log.trace("Unable to find legacyId for " + identifier);
                        continue;
                    }
                }

                int fullId = RuntimeItems.getFullId(legacyId, damage);
                LegacyEntry legacyEntry = new LegacyEntry(legacyId, hasDamage, damage);
                RuntimeEntry runtimeEntry = new RuntimeEntry(identifier, runtimeId, hasDamage, false);
                this.runtime2Legacy.put(runtimeId, legacyEntry);
                this.identifier2Legacy.put(identifier, legacyEntry);
                this.legacy2Runtime.put(fullId, runtimeEntry);
                this.itemPaletteEntries.add(runtimeEntry);
            }

            this.generatePalette();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        try (InputStream stream = Server.class.getClassLoader().getResourceAsStream("block_mappings.json")) {
            if (stream == null) {
                throw new AssertionError("Unable to load item_mappings.json");
            }
            JsonObject itemMapping = JsonParser.parseReader(new InputStreamReader(stream)).getAsJsonObject();
            for (String legacyID : itemMapping.keySet()) {
                JsonObject convertData = itemMapping.getAsJsonObject(legacyID);
                int id = Integer.parseInt(legacyID);
                for (String damageStr : convertData.keySet()) {
                    String identifier = convertData.get(damageStr).getAsString();
                    int damage = Integer.parseInt(damageStr);
                    mappingEntries.put(RuntimeItems.getFullId(id, damage), identifier);
                }
            }
            mappingEntries.trim();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        mappingEntries.forEach((id, damage) -> {
            String identifier = damage.split(";")[0];
            int runtime = this.name2RuntimeId.getOrDefault(identifier, -1);
            if (runtime != -1) {
                this.legacy2Runtime.put(id.intValue(), new RuntimeEntry(identifier, runtime, true, false));
            }
        });
    }

    private void generatePalette() {
        if (this.itemPaletteEntries.isEmpty()) {
            return;
        }
        BinaryStream paletteBuffer = new BinaryStream();
        paletteBuffer.putUnsignedVarInt(this.itemPaletteEntries.size());
        for (RuntimeEntry entry : this.itemPaletteEntries) {
            paletteBuffer.putString(entry.identifier());
            paletteBuffer.putLShort(entry.runtimeId());
            paletteBuffer.putBoolean(entry.isComponent()); // Component item
        }
        this.itemPalette = paletteBuffer.getBuffer();
    }

    /**
     * From runtimeId to get legacy item entry.
     *
     * @param runtimeId the runtime id
     * @return the legacy entry
     */
    public LegacyEntry fromRuntime(int runtimeId) {
        LegacyEntry legacyEntry = this.runtime2Legacy.get(runtimeId);
        if (legacyEntry == null) {
            throw new IllegalArgumentException("Unknown runtime2Legacy mapping: " + runtimeId);
        }
        return legacyEntry;
    }

    /**
     * from legacy item id and meta value to get runtimeId
     *
     * @param id   the id
     * @param meta the meta
     * @return the runtime entry
     */
    public RuntimeEntry toRuntime(int id, int meta) {
        RuntimeEntry runtimeEntry = this.legacy2Runtime.get(RuntimeItems.getFullId(id, meta));
        if (runtimeEntry == null) {
            runtimeEntry = this.legacy2Runtime.get(RuntimeItems.getFullId(id, 0));
        }

        if (runtimeEntry == null) {
            throw new IllegalArgumentException("Unknown legacy2Runtime mapping: id=" + id + " meta=" + meta);
        }
        return runtimeEntry;
    }

    /**
     * From identifier to get legacy entry.According to item_mappings.json
     *
     * @param identifier the identifier
     * @return the legacy entry
     */
    public LegacyEntry fromIdentifier(String identifier) {
        return this.identifier2Legacy.get(identifier);
    }

    /**
     * Get item palette byte [ ].
     *
     * @return the byte [ ]
     */
    public byte[] getItemPalette() {
        return this.itemPalette;
    }

    
    /**
     * Returns the <b>network id</b> based on the <b>full id</b> of the given item.
     *
     * @param item Given item
     * @return The <b>network id</b>
     * @throws IllegalArgumentException If the mapping of the <b>full id</b> to the <b>network id</b> is unknown
     */
    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    public int getNetworkFullId(Item item) {
        if (item instanceof StringItem) {
            return name2RuntimeId.getInt(item.getNamespaceId());
        }
        int fullId = RuntimeItems.getFullId(item.getId(), item.getDamage());
        if (!item.hasMeta() && item.getDamage() != 0) { // Fuzzy crafting recipe of a remapped item, like charcoal
            fullId = RuntimeItems.getFullId(item.getId(), item.getDamage());
        }
        RuntimeEntry runtimeEntry = legacy2Runtime.get(fullId);
        /*if (runtimeEntry == null) {
            String id = BlockStateRegistry.getBlockMapping(fullId);
            if (id != null) {
                return getNetworkIdByNamespaceId(id.split(";")[0]).orElse(0);
            }
        }*/
        if (runtimeEntry == null) {
            runtimeEntry = legacy2Runtime.get(RuntimeItems.getFullId(item.getId(), 0));
        }
        if (runtimeEntry == null) {
            throw new IllegalArgumentException("Unknown item mapping " + item);
        }
        return runtimeEntry.runtimeId();
    }


    /**
     * Returns the <b>full id</b> of a given <b>network id</b>.
     *
     * @param networkId The given <b>network id</b>
     * @return The <b>full id</b>
     * @throws IllegalArgumentException If the mapping of the <b>full id</b> to the <b>network id</b> is unknown
     */
    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    public int getLegacyFullId(int networkId) {
        LegacyEntry legacyEntry = runtime2Legacy.get(networkId);
        if (legacyEntry == null) {
            throw new IllegalArgumentException("Unknown network mapping " + networkId);
        }
        return legacyEntry.fullID();
    }

    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    public byte[] getItemDataPalette() {
        return this.itemPalette;
    }

    /**
     * Returns the <b>namespaced id</b> of a given <b>network id</b>.
     *
     * @param networkId The given <b>network id</b>
     * @return The <b>namespace id</b> or {@code null} if it is unknown
     */
    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    @Nullable
    public String getNamespacedIdByNetworkId(int networkId) {
        return runtimeId2Name.get(networkId);
    }

    /**
     * Returns the <b>network id</b> of a given <b>namespaced id</b>.
     *
     * @param namespaceId The given <b>namespaced id</b>
     * @return A <b>network id</b> wrapped in {@link OptionalInt} or an empty {@link OptionalInt} if it is unknown
     */
    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    
    public OptionalInt getNetworkIdByNamespaceId( String namespaceId) {
        int id = name2RuntimeId.getOrDefault(namespaceId, -1);
        if (id == -1) return OptionalInt.empty();
        return OptionalInt.of(id);
    }

    /**
     * Creates a new instance of the respective {@link Item} by the <b>namespaced id</b>.
     *
     * @param namespaceId The namespaced id
     * @param amount      How many items will be in the stack.
     * @return The correct {@link Item} instance with the write <b>item id</b> and <b>item damage</b> values.
     * @throws IllegalArgumentException If there are unknown mappings in the process.
     */
    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    
    public Item getItemByNamespaceId(String namespaceId, int amount, int runtimeBlockId) {
        int legacyFullId = getLegacyFullId(
                getNetworkIdByNamespaceId(namespaceId)
                        .orElseThrow(()-> new IllegalArgumentException("The network id of \""+namespaceId+"\" is unknown"))
        );
        if (RuntimeItems.hasData(legacyFullId)) {
            return Item.get(RuntimeItems.getId(legacyFullId), RuntimeItems.getData(legacyFullId), amount, EmptyArrays.EMPTY_BYTES, runtimeBlockId);
        } else {
            Item item = Item.get(RuntimeItems.getId(legacyFullId), 0, amount, EmptyArrays.EMPTY_BYTES, runtimeBlockId);
            return item;
        }
    }


    @SneakyThrows
    @Since("1.19.70-r2")
    public void registerNamespacedIdItem( Class<? extends StringItem> item) {
        Constructor<? extends StringItem> declaredConstructor = item.getDeclaredConstructor();
        var Item = declaredConstructor.newInstance();
        registerNamespacedIdItem(Item.getNamespaceId(), stringItemSupplier(declaredConstructor));
    }

    @PowerNukkitOnly
    public void registerNamespacedIdItem( String namespacedId,  Constructor<? extends Item> constructor) {
        Preconditions.checkNotNull(namespacedId, "namespacedId is null");
        Preconditions.checkNotNull(constructor, "constructor is null");
        this.namespacedIdItem.put(namespacedId.toLowerCase(Locale.ENGLISH), itemSupplier(constructor));
    }

    @SneakyThrows
    @PowerNukkitOnly
    public void registerNamespacedIdItem( String namespacedId,  Supplier<Item> constructor) {
        Preconditions.checkNotNull(namespacedId, "namespacedId is null");
        Preconditions.checkNotNull(constructor, "constructor is null");
        this.namespacedIdItem.put(namespacedId.toLowerCase(Locale.ENGLISH), constructor);
    }

    
    public static Int2ObjectOpenHashMap<String> getBlockMapping() {
        return mappingEntries;
    }

    
    private static Supplier<Item> itemSupplier( Constructor<? extends Item> constructor) {
        return () -> {
            try {
                return constructor.newInstance();
            } catch (ReflectiveOperationException e) {
                throw new UnsupportedOperationException(e);
            }
        };
    }

    @Since("1.19.60-r1")
    
    private static Supplier<Item> stringItemSupplier( Constructor<? extends StringItem> constructor) {
        return () -> {
            try {
                return (Item) constructor.newInstance();
            } catch (ReflectiveOperationException e) {
                throw new UnsupportedOperationException(e);
            }
        };
    }

    public class LegacyEntry {
        public final int legacyId;
        public final boolean hasDamage;
        public final int damage;

        public LegacyEntry(int legacyId, boolean hasDamage, int damage) {
            this.legacyId = legacyId;
            this.hasDamage = hasDamage;
            this.damage = damage;
        }

        public int damage() {
            return this.hasDamage ? this.damage : 0;
        }

        public int fullID() {
            return RuntimeItems.getFullId(legacyId, damage);
        }

        public int legacyId() {
            return legacyId;
        }

        public boolean hasDamage() {
            return hasDamage;
        }
    }

    public class RuntimeEntry {
        public final String identifier;
        public final int runtimeId;
        public final boolean hasDamage;
        public final boolean isComponent;

        public RuntimeEntry(String identifier, int runtimeId, boolean hasDamage, boolean isComponent) {
            this.identifier = identifier;
            this.runtimeId = runtimeId;
            this.hasDamage = hasDamage;
            this.isComponent = isComponent;
        }

        public String identifier() {
            return identifier;
        }

        public int runtimeId() {
            return runtimeId;
        }

        public boolean hasDamage() {
            return hasDamage;
        }

        public boolean isComponent() {
            return isComponent;
        }
    }
}