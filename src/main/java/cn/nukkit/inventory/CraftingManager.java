package cn.nukkit.inventory;

import cn.nukkit.Server;
import cn.nukkit.api.PowerNukkitOnly;
import cn.nukkit.api.Since;
import cn.nukkit.block.BlockID;
import cn.nukkit.block.BlockUnknown;
import cn.nukkit.blockproperty.UnknownRuntimeIdException;
import cn.nukkit.blockproperty.exception.BlockPropertyNotFoundException;
import cn.nukkit.blockstate.BlockState;
import cn.nukkit.blockstate.BlockStateRegistry;
import cn.nukkit.item.*;
import cn.nukkit.network.protocol.CraftingDataPacket;
import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.utils.BinaryStream;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.Utils;
import io.netty.util.collection.CharObjectHashMap;
import io.netty.util.internal.EmptyArrays;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import lombok.extern.log4j.Log4j2;
import lombok.var;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.*;
import java.util.stream.Stream;
import java.util.zip.Deflater;

/**
 * @author MagicDroidX (Nukkit Project)
 */
@Log4j2
public class CraftingManager {

    public final Collection<Recipe> recipes = new ArrayDeque<>();

    @Since("1.5.0.0-PN")
    public static DataPacket packet = null;

    protected final Map<Integer, Map<UUID, ShapedRecipe>> shapedRecipes = new Int2ObjectOpenHashMap<>();

    public final Map<Integer, FurnaceRecipe> furnaceRecipes = new Int2ObjectOpenHashMap<>();
    @PowerNukkitOnly
    public final Map<Integer, BlastFurnaceRecipe> blastFurnaceRecipes = new Int2ObjectOpenHashMap<>();
    @PowerNukkitOnly
    public final Map<Integer, SmokerRecipe> smokerRecipes = new Int2ObjectOpenHashMap<>();
    @PowerNukkitOnly
    public final Map<Integer, CampfireRecipe> campfireRecipes = new Int2ObjectOpenHashMap<>();

    @Since("1.4.0.0-PN")
    public final Map<UUID, MultiRecipe> multiRecipes = new HashMap<>();

    public final Map<Integer, BrewingRecipe> brewingRecipes = new Int2ObjectOpenHashMap<>();
    public final Map<Integer, ContainerRecipe> containerRecipes = new Int2ObjectOpenHashMap<>();
    @PowerNukkitOnly
    public final Map<Integer, StonecutterRecipe> stonecutterRecipes = new Int2ObjectOpenHashMap<>();

    private static int RECIPE_COUNT = 0;
    protected final Map<Integer, Map<UUID, ShapelessRecipe>> shapelessRecipes = new Int2ObjectOpenHashMap<>();

    @PowerNukkitOnly
    protected final Map<Integer, Map<UUID, CartographyRecipe>> cartographyRecipes = new Int2ObjectOpenHashMap<>();

    private final Int2ObjectOpenHashMap<Map<UUID, SmithingRecipe>> smithingRecipes = new Int2ObjectOpenHashMap<>();

    public static final Comparator<Item> recipeComparator = (i1, i2) -> {
        if (i1.getId() > i2.getId()) {
            return 1;
        } else if (i1.getId() < i2.getId()) {
            return -1;
        } else if (i1.getDamage() > i2.getDamage()) {
            return 1;
        } else if (i1.getDamage() < i2.getDamage()) {
            return -1;
        } else return Integer.compare(i1.getCount(), i2.getCount());
    };

    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    public static DataPacket getCraftingPacket() {
        return packet;
    }

    //<editor-fold desc="constructors and setup" defaultstate="collapsed">
    @SuppressWarnings("rawtypes")
    public CraftingManager() {
        Config recipesConfig = new Config(Config.JSON);
        try (InputStream recipesStream = Server.class.getClassLoader().getResourceAsStream("recipes.json")) {
            recipesConfig.load(Objects.requireNonNull(recipesStream, "Unable to find recipes.json"));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        if (Server.getInstance().isEducationEditionEnabled()) {
            Config edu = new Config(Config.JSON);
            edu.load(Server.class.getClassLoader().getResourceAsStream("recipes_edu.json"));

            List<Map> recipes = recipesConfig.getMapList("recipes");
            recipes.addAll(edu.getMapList("recipes"));
            List<Map> potionMixes = recipesConfig.getMapList("potionMixes");
            potionMixes.addAll(edu.getMapList("potionMixes"));
            List<Map> containerMixes = recipesConfig.getMapList("containerMixes");
            containerMixes.addAll(edu.getMapList("containerMixes"));

            recipesConfig.set("recipes", recipes);
            recipesConfig.set("potionMixes", potionMixes);
            recipesConfig.set("containerMixes", containerMixes);
        }

        this.loadRecipes(recipesConfig);

        String path = Server.getInstance().getDataPath() + "custom_recipes.json";
        File filePath = new File(path);

        if (filePath.exists()) {
            Config customRecipes = new Config(filePath, Config.JSON);
            this.loadRecipes(customRecipes);
        }
        this.rebuildPacket();

        log.info("Loaded {} recipes.", this.recipes.size());
    }

    @SuppressWarnings("unchecked")
    private void loadRecipes(Config config) {
        List<Map> recipes = config.getMapList("recipes");
        log.info("Loading recipes...");
        toNextRecipe:
        for (Map<String, Object> recipe : recipes) {
            try {
                int type = Utils.toInt(recipe.get("type"));
                switch (type) {
                    case 0:
                    case 5:
                        String craftingBlock = (String) recipe.get("block");
                        if (type == 5) {
                            craftingBlock = "shulker_box";
                        }
                        if (!"crafting_table".equals(craftingBlock) && !"stonecutter".equals(craftingBlock)
                                && !"cartography_table".equalsIgnoreCase(craftingBlock) && !"shulker_box".equalsIgnoreCase(craftingBlock)
                                && !"smithing_table".equalsIgnoreCase(craftingBlock)) {
                            // Ignore other recipes than crafting table, stonecutter, smithing table and cartography table
                            continue;
                        }
                        // TODO: handle multiple result items
                        List<Map> outputs = ((List<Map>) recipe.get("output"));
                        if (outputs.size() > 1) {
                            continue;
                        }
                        Map<String, Object> first = outputs.get(0);
                        List<Item> sorted = new ArrayList<>();
                        for (Map<String, Object> ingredient : ((List<Map>) recipe.get("input"))) {
                            Item recipeItem = parseRecipeItem(ingredient);
                            if (recipeItem.isNull()) {
                                continue toNextRecipe;
                            }
                            sorted.add(recipeItem);
                        }
                        // Bake sorted list
                        sorted.sort(recipeComparator);

                        String recipeId = (String) recipe.get("id");
                        int priority = Utils.toInt(recipe.get("priority"));

                        Item result = parseRecipeItem(first);
                        if (result.isNull()) {
                            continue toNextRecipe;
                        }
                        switch (craftingBlock) {
                            case "crafting_table":
                                this.registerRecipe(new ShapelessRecipe(recipeId, priority, result, sorted));
                                break;
                            case "shulker_box":
                                this.registerRecipe(new ShulkerBoxRecipe(recipeId, priority, result, sorted));
                                break;
                            case "stonecutter":
                                this.registerRecipe(new StonecutterRecipe(recipeId, priority, result, sorted.get(0)));
                                break;
                            case "cartography_table":
                                this.registerRecipe(new CartographyRecipe(recipeId, priority, result, sorted));
                                break;
                            case "smithing_table":
                                this.registerRecipe(new SmithingRecipe(recipeId, priority, sorted, result));
                                break;
                        }
                        break;
                    case 1:
                        craftingBlock = (String) recipe.get("block");
                        if (!"crafting_table".equals(craftingBlock)) {
                            // Ignore other recipes than crafting table ones
                            continue;
                        }
                        outputs = (List<Map>) recipe.get("output");

                        first = outputs.remove(0);
                        String[] shape = ((List<String>) recipe.get("shape")).toArray(EmptyArrays.EMPTY_STRINGS);
                        Map<Character, Item> ingredients = new CharObjectHashMap<>();
                        List<Item> extraResults = new ArrayList<>();

                        Map<String, Map<String, Object>> input = (Map) recipe.get("input");
                        for (Map.Entry<String, Map<String, Object>> ingredientEntry : input.entrySet()) {
                            char ingredientChar = ingredientEntry.getKey().charAt(0);
                            Item ingredient = parseRecipeItem(ingredientEntry.getValue());
                            if (ingredient.isNull()) {
                                continue toNextRecipe;
                            }
                            ingredients.put(ingredientChar, ingredient);
                        }

                        for (Map<String, Object> data : outputs) {
                            Item output = parseRecipeItem(data);
                            if (output.isNull()) {
                                continue toNextRecipe;
                            }
                            extraResults.add(output);
                        }

                        recipeId = (String) recipe.get("id");
                        priority = Utils.toInt(recipe.get("priority"));

                        Item primaryResult = parseRecipeItem(first);
                        if (primaryResult.isNull()) {
                            continue toNextRecipe;
                        }
                        this.registerRecipe(new ShapedRecipe(recipeId, priority, primaryResult, shape, ingredients, extraResults));
                        break;
                    case 2:
                    case 3:
                        craftingBlock = (String) recipe.get("block");
                        if (!"furnace".equals(craftingBlock) && !"blast_furnace".equals(craftingBlock)
                                && !"smoker".equals(craftingBlock) && !"campfire".equals(craftingBlock)) {
                            // Ignore other recipes than furnaces, blast furnaces, smokers and campfire
                            continue;
                        }
                        Map<String, Object> resultMap = (Map) recipe.get("output");
                        Item resultItem = parseRecipeItem(resultMap);
                        if (resultItem.isNull()) {
                            continue toNextRecipe;
                        }
                        Item inputItem;
                        try {
                            Map<String, Object> inputMap = (Map) recipe.get("input");
                            inputItem = parseRecipeItem(inputMap);
                        } catch (Exception old) {
                            inputItem = Item.get(Utils.toInt(recipe.get("inputId")), recipe.containsKey("inputDamage") ? Utils.toInt(recipe.get("inputDamage")) : -1, 1);
                        }
                        double experience = 0;
                        if(resultMap.containsKey("experience")) {
                            experience = (double) resultMap.get("experience");
                        }
                        if (inputItem.isNull()) {
                            continue toNextRecipe;
                        }
                        switch (craftingBlock) {
                            case "furnace":
                                this.registerRecipe(new FurnaceRecipe(resultItem, inputItem, experience));
                                break;
                            case "blast_furnace":
                                this.registerRecipe(new BlastFurnaceRecipe(resultItem, inputItem, experience));
                                break;
                            case "smoker":
                                this.registerRecipe(new SmokerRecipe(resultItem, inputItem, experience));
                                break;
                            case "campfire":
                                this.registerRecipe(new CampfireRecipe(resultItem, inputItem));
                                break;
                        }
                        break;
                    case 4:
                        this.registerRecipe(new MultiRecipe(UUID.fromString((String) recipe.get("uuid"))));
                        break;
                    default:
                        break;
                }
            } catch (Exception e) {
                log.error("Exception during registering recipe", e);
            }
        }

        // Load brewing recipes
        List<Map> potionMixes = config.getMapList("potionMixes");

        RuntimeItemMapping runtimeMapping = RuntimeItems.getRuntimeMapping();
        for (Map potionMix : potionMixes) {
            String fromPotionId = potionMix.get("inputId").toString();
            int fromPotionMeta = ((Number) potionMix.get("inputMeta")).intValue();
            String ingredient = potionMix.get("reagentId").toString();
            int ingredientMeta = ((Number) potionMix.get("reagentMeta")).intValue();
            String toPotionId = potionMix.get("outputId").toString();
            int toPotionMeta = ((Number) potionMix.get("outputMeta")).intValue();

            registerBrewingRecipe(new BrewingRecipe(
                    Item.fromString(fromPotionId + ":" + fromPotionMeta),
                    Item.fromString(ingredient + ":" + ingredientMeta),
                    Item.fromString(toPotionId + ":" + toPotionMeta)
            ));
        }

        List<Map> containerMixes = config.getMapList("containerMixes");

        for (Map containerMix : containerMixes) {
            String fromItemId = containerMix.get("inputId").toString();
            String ingredient = containerMix.get("reagentId").toString();
            String toItemId = containerMix.get("outputId").toString();

            registerContainerRecipe(new ContainerRecipe(Item.fromString(fromItemId), Item.fromString(ingredient), Item.fromString(toItemId)));
        }

        // Allow to rename without crafting 
        registerCartographyRecipe(new CartographyRecipe(Item.get(ItemID.EMPTY_MAP), Collections.singletonList(Item.get(ItemID.EMPTY_MAP))));
        registerCartographyRecipe(new CartographyRecipe(Item.get(ItemID.EMPTY_MAP, 2), Collections.singletonList(Item.get(ItemID.EMPTY_MAP, 2))));
        registerCartographyRecipe(new CartographyRecipe(Item.get(ItemID.MAP), Collections.singletonList(Item.get(ItemID.MAP))));
        registerCartographyRecipe(new CartographyRecipe(Item.get(ItemID.MAP, 3), Collections.singletonList(Item.get(ItemID.MAP, 3))));
        registerCartographyRecipe(new CartographyRecipe(Item.get(ItemID.MAP, 4), Collections.singletonList(Item.get(ItemID.MAP, 4))));
        registerCartographyRecipe(new CartographyRecipe(Item.get(ItemID.MAP, 5), Collections.singletonList(Item.get(ItemID.MAP, 5))));
    }

    private Item parseRecipeItem(Map<String, Object> data) {
        String nbt = (String) data.get("nbt_b64");
        boolean fuzzy = data.containsKey("fuzzy") && Boolean.parseBoolean(data.get("fuzzy").toString());
        byte[] nbtBytes = nbt != null ? Base64.getDecoder().decode(nbt) : EmptyArrays.EMPTY_BYTES;

        int count = data.containsKey("count") ? ((Number) data.get("count")).intValue() : 1;

        Item item;
        if (data.containsKey("blockState")) {
            String blockStateId = data.get("blockState").toString();
            try {
                BlockState state = BlockState.of(blockStateId);
                item = state.asItemBlock(count);
                item.setCompoundTag(nbtBytes);
                if (fuzzy) {
                    item = item.createFuzzyCraftingRecipe();
                }
                return item;
            } catch (BlockPropertyNotFoundException | UnknownRuntimeIdException e) {
                int runtimeId = BlockStateRegistry.getKnownRuntimeIdByBlockStateId(blockStateId);
                if (runtimeId == -1) {
                    log.warn("Unsupported block found in recipes.json: {}", blockStateId);
                    return Item.get(BlockID.AIR);
                }
                int blockId = BlockStateRegistry.getBlockIdByRuntimeId(runtimeId);
                BlockState defaultBlockState = BlockState.of(blockId);
                if (defaultBlockState.getProperties().equals(BlockUnknown.PROPERTIES)) {
                    log.warn("Unsupported block found in recipes.json: {}", blockStateId);
                    return Item.get(BlockID.AIR);
                }
                log.error("Failed to load a recipe with {}", blockStateId, e);
                return Item.get(BlockID.AIR);
            } catch (Exception e) {
                log.error("Failed to load the block state {}", blockStateId, e);
                return Item.getBlock(BlockID.AIR);
            }
        }

        if (data.containsKey("blockRuntimeId")) {
            int blockRuntimeId = Utils.toInt(data.get("blockRuntimeId"));
            try {
                BlockState state = BlockStateRegistry.getBlockStateByRuntimeId(blockRuntimeId);
                if (state == null || state.equals(BlockState.AIR)) {
                    return Item.getBlock(BlockID.AIR);
                }
                if (state.getProperties().equals(BlockUnknown.PROPERTIES)) {
                    return Item.getBlock(BlockID.AIR);
                }
                item = state.asItemBlock(count);
                item.setCompoundTag(nbtBytes);
                if (fuzzy) {
                    item = item.createFuzzyCraftingRecipe();
                }
                return item;
            } catch (BlockPropertyNotFoundException e) {
                log.debug("Failed to load the block runtime id {}", blockRuntimeId, e);
            }
        }

        Integer legacyId = null;
        if (data.containsKey("legacyId")) {
            legacyId = Utils.toInt(data.get("legacyId"));
        }
        if (legacyId != null && legacyId > 255) {
            try {
                int fullId = RuntimeItems.getRuntimeMapping().getLegacyFullId(legacyId);
                int itemId = RuntimeItems.getId(fullId);
                Integer meta = null;
                if (RuntimeItems.hasData(fullId)) {
                    meta = RuntimeItems.getData(fullId);
                }

                if (data.containsKey("damage")) {
                    int damage = Utils.toInt(data.get("damage"));
                    if (damage == Short.MAX_VALUE) {
                        fuzzy = true;
                    } else if (meta == null) {
                        meta = damage;
                    }
                }

                item = Item.get(itemId, meta == null ? 0 : meta, count);
                if (fuzzy) {
                    item = item.createFuzzyCraftingRecipe();
                }

                item.setCompoundTag(nbtBytes);
                return item;
            } catch (IllegalArgumentException e) {
                log.debug("Failed to load a crafting recipe item, attempting to load by string id", e);
            }
        }

        String id = data.get("id").toString();
        if (data.containsKey("damage")) {
            int meta = Utils.toInt(data.get("damage"));
            if (meta == Short.MAX_VALUE) {
                item = Item.fromString(id);
                fuzzy = true;
            } else {
                item = Item.fromString(id + ":" + meta);
            }
        } else {
            item = Item.fromString(id);
        }
        item.setCount(count);
        item.setCompoundTag(nbtBytes);
        if (fuzzy) {
            item = item.createFuzzyCraftingRecipe();
        }

        return item;
    }

    public void rebuildPacket() {
        CraftingDataPacket pk = new CraftingDataPacket();
        pk.cleanRecipes = true;

        for (Recipe recipe : this.recipes) {
            if (recipe instanceof ShapedRecipe) {
                pk.addShapedRecipe((ShapedRecipe) recipe);
            } else if (recipe instanceof ShapelessRecipe) {
                pk.addShapelessRecipe((ShapelessRecipe) recipe);
            }
        }

        for (Map<UUID, CartographyRecipe> map : cartographyRecipes.values()) {
            for (CartographyRecipe recipe : map.values()) {
                pk.addCartographyRecipe(recipe);
            }
        }

        for (FurnaceRecipe recipe : this.furnaceRecipes.values()) {
            pk.addFurnaceRecipe(recipe);
        }

        for (MultiRecipe recipe : this.multiRecipes.values()) {
            pk.addMultiRecipe(recipe);
        }

        for (SmokerRecipe recipe : this.smokerRecipes.values()) {
            pk.addSmokerRecipe(recipe);
        }

        for (BlastFurnaceRecipe recipe : this.blastFurnaceRecipes.values()) {
            pk.addBlastFurnaceRecipe(recipe);
        }

        for (CampfireRecipe recipe : this.campfireRecipes.values()) {
            pk.addCampfireRecipeRecipe(recipe);
        }

        for (BrewingRecipe recipe : this.brewingRecipes.values()) {
            pk.addBrewingRecipe(recipe);
        }

        for (ContainerRecipe recipe : this.containerRecipes.values()) {
            pk.addContainerRecipe(recipe);
        }

        for (StonecutterRecipe recipe : this.stonecutterRecipes.values()) {
            pk.addStonecutterRecipe(recipe);
        }

        pk.tryEncode();
        // TODO: find out whats wrong with compression
        packet = pk.compress(Deflater.BEST_COMPRESSION);
    }
    //</editor-fold>

    public FurnaceRecipe matchFurnaceRecipe(Item input) {
        if (input.isNull()) {
            return null;
        }
        FurnaceRecipe recipe = this.furnaceRecipes.get(getItemHash(input));
        if (recipe == null) recipe = this.furnaceRecipes.get(getItemHash(input, 0));
        return recipe;
    }

    @PowerNukkitOnly
    public CampfireRecipe matchCampfireRecipe(Item input) {
        if (input.isNull()) {
            return null;
        }
        CampfireRecipe recipe = this.campfireRecipes.get(getItemHash(input));
        if (recipe == null) recipe = this.campfireRecipes.get(getItemHash(input, 0));
        return recipe;
    }

    @PowerNukkitOnly
    public BlastFurnaceRecipe matchBlastFurnaceRecipe(Item input) {
        if (input.isNull()) {
            return null;
        }
        BlastFurnaceRecipe recipe = this.blastFurnaceRecipes.get(getItemHash(input));
        if (recipe == null) recipe = this.blastFurnaceRecipes.get(getItemHash(input, 0));
        return recipe;
    }

    @PowerNukkitOnly
    public SmokerRecipe matchSmokerRecipe(Item input) {
        if (input.isNull()) {
            return null;
        }
        SmokerRecipe recipe = this.smokerRecipes.get(getItemHash(input));
        if (recipe == null) recipe = this.smokerRecipes.get(getItemHash(input, 0));
        return recipe;
    }

    @PowerNukkitOnly("Public only in PowerNukkit")
    @Since("FUTURE")
    public static UUID getMultiItemHash(Collection<Item> items) {
        BinaryStream stream = new BinaryStream();
        for (Item item : items) {
            stream.putVarInt(getFullItemHash(item));
        }
        return UUID.nameUUIDFromBytes(stream.getBuffer());
    }

    @PowerNukkitOnly("Public only in PowerNukkit")
    @Since("FUTURE")
    public static int getFullItemHash(Item item) {
        return 31 * getItemHash(item) + item.getCount();
    }

    @PowerNukkitOnly
    public void registerStonecutterRecipe(StonecutterRecipe recipe) {
        this.stonecutterRecipes.put(getItemHash(recipe.getResult()), recipe);
    }

    public void registerFurnaceRecipe(FurnaceRecipe recipe) {
        Item input = recipe.getInput();
        this.furnaceRecipes.put(getItemHash(input), recipe);
    }

    @PowerNukkitOnly
    public void registerBlastFurnaceRecipe(BlastFurnaceRecipe recipe) {
        Item input = recipe.getInput();
        this.blastFurnaceRecipes.put(getItemHash(input), recipe);
    }

    @PowerNukkitOnly
    public void registerSmokerRecipe(SmokerRecipe recipe) {
        Item input = recipe.getInput();
        this.smokerRecipes.put(getItemHash(input), recipe);
    }

    @PowerNukkitOnly
    public void registerCampfireRecipe(CampfireRecipe recipe) {
        Item input = recipe.getInput();
        this.campfireRecipes.put(getItemHash(input), recipe);
    }

    @PowerNukkitOnly("Public only in PowerNukkit")
    @Since("FUTURE")
    public static int getItemHash(Item item) {
        return getItemHash(item, item.getDamage());
    }

    @PowerNukkitOnly
    @Since("FUTURE")
    public static int getItemHash(Item item, int meta) {
        int id = item.getId();
        int hash = 31 + (id << 8 | meta & 0xFF);
        hash *= 31 + (id == ItemID.STRING_IDENTIFIED_ITEM && item instanceof StringItem? item.getNamespaceId().hashCode() : 0);
        return hash;
    }

    public void registerShapedRecipe(ShapedRecipe recipe) {
        int resultHash = getItemHash(recipe.getResult());
        Map<UUID, ShapedRecipe> map = this.shapedRecipes.computeIfAbsent(resultHash, k -> new HashMap<>());
        List<Item> inputList = new LinkedList<>(recipe.getIngredientsAggregate());
        map.put(getMultiItemHash(inputList), recipe);
    }


    public void registerRecipe(Recipe recipe) {
        UUID id = null;
        if (recipe instanceof CraftingRecipe || recipe instanceof StonecutterRecipe) {
            id = Utils.dataToUUID(String.valueOf(++RECIPE_COUNT), String.valueOf(recipe.getResult().getId()), String.valueOf(recipe.getResult().getDamage()), String.valueOf(recipe.getResult().getCount()), Arrays.toString(recipe.getResult().getCompoundTag()));
        }
        if (recipe instanceof CraftingRecipe) {
            ((CraftingRecipe) recipe).setId(id);
            this.recipes.add(recipe);
        } else if (recipe instanceof StonecutterRecipe) {
            ((StonecutterRecipe) recipe).setId(id);
        }

        recipe.registerToCraftingManager(this);
    }

    @PowerNukkitOnly
    public void registerCartographyRecipe(CartographyRecipe recipe) {
        List<Item> list = recipe.getIngredientList();
        list.sort(recipeComparator);

        UUID hash = getMultiItemHash(list);

        int resultHash = getItemHash(recipe.getResult());
        Map<UUID, CartographyRecipe> map = cartographyRecipes.computeIfAbsent(resultHash, k -> new HashMap<>());

        map.put(hash, recipe);
    }

    public void registerShapelessRecipe(ShapelessRecipe recipe) {
        List<Item> list = recipe.getIngredientsAggregate();

        UUID hash = getMultiItemHash(list);

        int resultHash = getItemHash(recipe.getResult());
        Map<UUID, ShapelessRecipe> map = this.shapelessRecipes.computeIfAbsent(resultHash, k -> new HashMap<>());

        map.put(hash, recipe);
    }

    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    public void registerSmithingRecipe(@Nonnull SmithingRecipe recipe) {
        List<Item> list = recipe.getIngredientsAggregate();

        UUID hash = getMultiItemHash(list);

        int resultHash = getItemHash(recipe.getResult());
        Map<UUID, SmithingRecipe> map = this.smithingRecipes.computeIfAbsent(resultHash, k -> new HashMap<>());

        map.put(hash, recipe);
    }

    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    @Nullable
    public SmithingRecipe matchSmithingRecipe(Item equipment, Item ingredient) {
        List<Item> inputList = new ArrayList<>(2);
        inputList.add(equipment.decrement(equipment.count - 1));
        inputList.add(ingredient.decrement(ingredient.count - 1));
        return matchSmithingRecipe(inputList);
    }

    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    @Nullable
    public SmithingRecipe matchSmithingRecipe(@Nonnull List<Item> inputList) {
        inputList.sort(recipeComparator);
        UUID inputHash = getMultiItemHash(inputList);

        return smithingRecipes.values().stream().flatMap(map -> map.entrySet().stream())
                .filter(entry -> entry.getKey().equals(inputHash))
                .map(Map.Entry::getValue)
                .findFirst().orElseGet(() ->
                        smithingRecipes.values().stream().flatMap(map -> map.values().stream())
                                .filter(recipe -> recipe.matchItems(inputList))
                                .findFirst().orElse(null)
                );
    }

    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    @Nullable
    public SmithingRecipe matchSmithingRecipe(@Nonnull Item equipment, @Nonnull Item ingredient, @Nonnull Item primaryOutput) {
        List<Item> inputList = Arrays.asList(equipment, ingredient);
        return matchSmithingRecipe(inputList, primaryOutput);
    }

    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    public SmithingRecipe matchSmithingRecipe(@Nonnull List<Item> inputList, @Nonnull Item primaryOutput) {
        int outputHash = getItemHash(primaryOutput);
        if (!this.smithingRecipes.containsKey(outputHash)) {
            return null;
        }

        inputList.sort(recipeComparator);

        UUID inputHash = getMultiItemHash(inputList);

        Map<UUID, SmithingRecipe> recipeMap = this.smithingRecipes.get(outputHash);

        if (recipeMap != null) {
            SmithingRecipe recipe = recipeMap.get(inputHash);

            if (recipe != null && (recipe.matchItems(inputList) || matchItemsAccumulation(recipe, inputList, primaryOutput))) {
                return recipe;
            }

            for (SmithingRecipe smithingRecipe : recipeMap.values()) {
                if (smithingRecipe.matchItems(inputList) || matchItemsAccumulation(smithingRecipe, inputList, primaryOutput)) {
                    return smithingRecipe;
                }
            }
        }

        return null;
    }

    @PowerNukkitOnly("Public only in PowerNukkit")
    @Since("FUTURE")
    public static int getPotionHash(Item ingredient, Item potion) {
        int ingredientId = ingredient.getId();
        int potionId = potion.getId();
        int hash = 17;
        hash *= 31 + ingredientId;
        hash *= 31 + (ingredientId == ItemID.STRING_IDENTIFIED_ITEM? ingredient.getNamespaceId().hashCode() : 0);
        hash *= 31 + potion.getDamage();
        hash *= 31 + potionId;
        hash *= 31 + (potionId == ItemID.STRING_IDENTIFIED_ITEM? potion.getNamespaceId().hashCode() : 0);
        hash *= 31 + potion.getDamage();
        return hash;
    }
    
    @PowerNukkitOnly
    @Since("FUTURE")
    public static int getContainerHash(@Nonnull Item ingredient, @Nonnull Item container) {
        int ingredientId = ingredient.getId();
        int containerId = container.getId();
        int hash = 17;
        hash *= 31 + ingredientId;
        hash *= 31 + (ingredientId == ItemID.STRING_IDENTIFIED_ITEM? ingredient.getNamespaceId().hashCode() : 0);
        hash *= 31 + containerId;
        hash *= 31 + (containerId == ItemID.STRING_IDENTIFIED_ITEM? container.getNamespaceId().hashCode() : 0);
        return hash;
    }

    public void registerBrewingRecipe(BrewingRecipe recipe) {
        Item input = recipe.getIngredient();
        Item potion = recipe.getInput();

        int potionHash = getPotionHash(input, potion);
        var brewingRecipes = this.brewingRecipes;
        if (brewingRecipes.containsKey(potionHash)) {
            log.warn("The brewing recipe {} is being replaced by {}", brewingRecipes.get(potionHash), recipe);
        }
        brewingRecipes.put(potionHash, recipe);
    }

    public void registerContainerRecipe(ContainerRecipe recipe) {
        Item input = recipe.getIngredient();
        Item potion = recipe.getInput();

        this.containerRecipes.put(getContainerHash(input, potion), recipe);
    }

    public BrewingRecipe matchBrewingRecipe(Item input, Item potion) {
        return this.brewingRecipes.get(getPotionHash(input, potion));
    }

    public ContainerRecipe matchContainerRecipe(Item input, Item potion) {
        return this.containerRecipes.get(getContainerHash(input, potion));
    }

    @PowerNukkitOnly
    public StonecutterRecipe matchStonecutterRecipe(Item output) {
        return this.stonecutterRecipes.get(getItemHash(output));
    }

    @PowerNukkitOnly
    public CartographyRecipe matchCartographyRecipe(List<Item> inputList, Item primaryOutput, List<Item> extraOutputList) {
        int outputHash = getItemHash(primaryOutput);

        if (cartographyRecipes.containsKey(outputHash)) {
            inputList.sort(recipeComparator);

            UUID inputHash = getMultiItemHash(inputList);

            Map<UUID, CartographyRecipe> recipes = cartographyRecipes.get(outputHash);

            if (recipes == null) {
                return null;
            }

            CartographyRecipe recipe = recipes.get(inputHash);

            if (recipe != null && recipe.matchItems(inputList, extraOutputList) || matchItemsAccumulation(recipe, inputList, primaryOutput, extraOutputList)) {
                return recipe;
            }

            for (CartographyRecipe cartographyRecipe : recipes.values()) {
                if (cartographyRecipe.matchItems(inputList, extraOutputList) || matchItemsAccumulation(cartographyRecipe, inputList, primaryOutput, extraOutputList)) {
                    return cartographyRecipe;
                }
            }
        }

        return null;
    }

    public CraftingRecipe matchRecipe(List<Item> inputList, Item primaryOutput, List<Item> extraOutputList) {
        //TODO: try to match special recipes before anything else (first they need to be implemented!)

        int outputHash = getItemHash(primaryOutput);
        if (this.shapedRecipes.containsKey(outputHash)) {
            inputList.sort(recipeComparator);

            UUID inputHash = getMultiItemHash(inputList);

            Map<UUID, ShapedRecipe> recipeMap = this.shapedRecipes.get(outputHash);

            if (recipeMap != null) {
                ShapedRecipe recipe = recipeMap.get(inputHash);

                if (recipe != null && (recipe.matchItems(inputList, extraOutputList) || matchItemsAccumulation(recipe, inputList, primaryOutput, extraOutputList))) {
                    return recipe;
                }

                for (ShapedRecipe shapedRecipe : recipeMap.values()) {
                    if (shapedRecipe.matchItems(inputList, extraOutputList) || matchItemsAccumulation(shapedRecipe, inputList, primaryOutput, extraOutputList)) {
                        return shapedRecipe;
                    }
                }
            }
        }

        if (this.shapelessRecipes.containsKey(outputHash)) {
            inputList.sort(recipeComparator);

            UUID inputHash = getMultiItemHash(inputList);

            Map<UUID, ShapelessRecipe> recipes = this.shapelessRecipes.get(outputHash);

            if (recipes == null) {
                return null;
            }

            ShapelessRecipe recipe = recipes.get(inputHash);

            if (recipe != null && (recipe.matchItems(inputList, extraOutputList) || matchItemsAccumulation(recipe, inputList, primaryOutput, extraOutputList))) {
                return recipe;
            }

            for (ShapelessRecipe shapelessRecipe : recipes.values()) {
                if (shapelessRecipe.matchItems(inputList, extraOutputList) || matchItemsAccumulation(shapelessRecipe, inputList, primaryOutput, extraOutputList)) {
                    return shapelessRecipe;
                }
            }
        }

        return null;
    }

    private boolean matchItemsAccumulation(SmithingRecipe recipe, List<Item> inputList, Item primaryOutput) {
        Item recipeResult = recipe.getResult();
        if (primaryOutput.equals(recipeResult, recipeResult.hasMeta(), recipeResult.hasCompoundTag()) && primaryOutput.getCount() % recipeResult.getCount() == 0) {
            int multiplier = primaryOutput.getCount() / recipeResult.getCount();
            return recipe.matchItems(inputList, multiplier);
        }
        return false;
    }

    private boolean matchItemsAccumulation(CraftingRecipe recipe, List<Item> inputList, Item primaryOutput, List<Item> extraOutputList) {
        Item recipeResult = recipe.getResult();
        if (primaryOutput.equals(recipeResult, recipeResult.hasMeta(), recipeResult.hasCompoundTag()) && primaryOutput.getCount() % recipeResult.getCount() == 0) {
            int multiplier = primaryOutput.getCount() / recipeResult.getCount();
            return recipe.matchItems(inputList, extraOutputList, multiplier);
        }
        return false;
    }

    @Since("1.4.0.0-PN")
    public void registerMultiRecipe(MultiRecipe recipe) {
        this.multiRecipes.put(recipe.getId(), recipe);
    }

    public static class Entry {
        final int resultItemId;
        final int resultMeta;
        final int ingredientItemId;
        final int ingredientMeta;
        final String recipeShape;
        final int resultAmount;

        public Entry(int resultItemId, int resultMeta, int ingredientItemId, int ingredientMeta, String recipeShape, int resultAmount) {
            this.resultItemId = resultItemId;
            this.resultMeta = resultMeta;
            this.ingredientItemId = ingredientItemId;
            this.ingredientMeta = ingredientMeta;
            this.recipeShape = recipeShape;
            this.resultAmount = resultAmount;
        }
    }
}
