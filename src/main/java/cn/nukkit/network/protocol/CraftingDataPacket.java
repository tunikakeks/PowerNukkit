package cn.nukkit.network.protocol;

import cn.nukkit.api.PowerNukkitOnly;
import cn.nukkit.api.Since;
import cn.nukkit.inventory.*;
import cn.nukkit.item.Item;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author Nukkit Project Team
 */
@ToString
public class CraftingDataPacket extends DataPacket {

    public static final byte NETWORK_ID = ProtocolInfo.CRAFTING_DATA_PACKET;

    public static final String CRAFTING_TAG_CRAFTING_TABLE = "crafting_table";
    public static final String CRAFTING_TAG_CARTOGRAPHY_TABLE = "cartography_table";
    public static final String CRAFTING_TAG_STONECUTTER = "stonecutter";
    public static final String CRAFTING_TAG_FURNACE = "furnace";
    public static final String CRAFTING_TAG_CAMPFIRE = "campfire";
    public static final String CRAFTING_TAG_BLAST_FURNACE = "blast_furnace";
    public static final String CRAFTING_TAG_SMOKER = "smoker";
    @PowerNukkitOnly @Since("1.6.0.0-PN") public static final String CRAFTING_TAG_SMITHING_TABLE = "smithing_table";

    private List<Recipe> entries = new ArrayList<>();
    private final List<BrewingRecipe> brewingEntries = new ArrayList<>();
    private final List<ContainerRecipe> containerEntries = new ArrayList<>();
    public boolean cleanRecipes;

    public void addShapelessRecipe(ShapelessRecipe... recipe) {
        Collections.addAll(entries, recipe);
    }

    @PowerNukkitOnly
    public void addStonecutterRecipe(StonecutterRecipe... recipes) {
        Collections.addAll(entries, recipes);
    }

    public void addShapedRecipe(ShapedRecipe... recipe) {
        Collections.addAll(entries, recipe);
    }

    @PowerNukkitOnly
    public void addCartographyRecipe(CartographyRecipe... recipe) {
        Stream.of(recipe).filter(r -> r.getRecipeId() != null).forEachOrdered(r -> entries.add(r));
    }

    public void addFurnaceRecipe(FurnaceRecipe... recipe) {
        Collections.addAll(entries, recipe);
    }

    @PowerNukkitOnly
    public void addSmokerRecipe(SmokerRecipe... recipe) {
        Collections.addAll(entries, recipe);
    }

    @PowerNukkitOnly
    public void addBlastFurnaceRecipe(BlastFurnaceRecipe... recipe) {
        Collections.addAll(entries, recipe);
    }

    @PowerNukkitOnly
    public void addCampfireRecipeRecipe(CampfireRecipe... recipe) {
        Collections.addAll(entries, recipe);
    }

    @Since("1.4.0.0-PN")
    public void addMultiRecipe(MultiRecipe... recipe) {
        Collections.addAll(entries, recipe);
    }

    public void addBrewingRecipe(BrewingRecipe... recipe) {
        Collections.addAll(brewingEntries, recipe);
    }

    public void addContainerRecipe(ContainerRecipe... recipe) {
        Collections.addAll(containerEntries, recipe);
    }

    @Override
    public DataPacket clean() {
        entries = new ArrayList<>();
        return super.clean();
    }

    @Override
    public void decode() {

    }

    @Override
    public void encode() {
        this.reset();
        this.putUnsignedVarInt(entries.size());

        int recipeNetworkId = 1;

        for (Recipe recipe : entries) {
            this.putVarInt(recipe.getType().networkType);
            switch (recipe.getType()) {
                case STONECUTTER:
                    StonecutterRecipe stonecutter = (StonecutterRecipe) recipe;
                    this.putString(stonecutter.getRecipeId());
                    this.putUnsignedVarInt(1);
                    this.putRecipeIngredient(stonecutter.getIngredient());
                    this.putUnsignedVarInt(1);
                    this.putSlot(stonecutter.getResult(), true);
                    this.putUUID(stonecutter.getId());
                    this.putString(CRAFTING_TAG_STONECUTTER);
                    this.putVarInt(stonecutter.getPriority());
                    this.putUnsignedVarInt(recipeNetworkId++);
                    break;
                case SHAPELESS:
                case CARTOGRAPHY:
                case SHULKER_BOX:
                case SMITHING:
                    ShapelessRecipe shapeless = (ShapelessRecipe) recipe;
                    this.putString(shapeless.getRecipeId());
                    List<Item> ingredients = shapeless.getIngredientList();
                    this.putUnsignedVarInt(ingredients.size());
                    for (Item ingredient : ingredients) {
                        this.putRecipeIngredient(ingredient);
                    }
                    this.putUnsignedVarInt(1);
                    this.putSlot(shapeless.getResult(), true);
                    this.putUUID(shapeless.getId());
                    switch (recipe.getType()) {
                        case CARTOGRAPHY:
                            this.putString(CRAFTING_TAG_CARTOGRAPHY_TABLE);
                            break;
                        case SHAPELESS:
                        case SHULKER_BOX:
                            this.putString(CRAFTING_TAG_CRAFTING_TABLE);
                            break;
                        case SMITHING:
                            this.putString(CRAFTING_TAG_SMITHING_TABLE);
                            break;
                    }
                    this.putVarInt(shapeless.getPriority());
                    this.putUnsignedVarInt(recipeNetworkId++);
                    break;
                case SHAPED:
                    ShapedRecipe shaped = (ShapedRecipe) recipe;
                    this.putString(shaped.getRecipeId());
                    this.putVarInt(shaped.getWidth());
                    this.putVarInt(shaped.getHeight());

                    for (int z = 0; z < shaped.getHeight(); ++z) {
                        for (int x = 0; x < shaped.getWidth(); ++x) {
                            this.putRecipeIngredient(shaped.getIngredient(x, z));
                        }
                    }
                    List<Item> outputs = new ArrayList<>();
                    outputs.add(shaped.getResult());
                    outputs.addAll(shaped.getExtraResults());
                    this.putUnsignedVarInt(outputs.size());
                    for (Item output : outputs) {
                        this.putSlot(output, true);
                    }
                    this.putUUID(shaped.getId());
                    this.putString(CRAFTING_TAG_CRAFTING_TABLE);
                    this.putVarInt(shaped.getPriority());
                    this.putUnsignedVarInt(recipeNetworkId++);
                    break;
                case FURNACE:
                case FURNACE_DATA:
                case SMOKER:
                case SMOKER_DATA:
                case BLAST_FURNACE:
                case BLAST_FURNACE_DATA:
                case CAMPFIRE:
                case CAMPFIRE_DATA:
                    SmeltingRecipe smelting = (SmeltingRecipe) recipe;
                    Item input = smelting.getInput();
                    this.putVarInt(input.getId());
                    if (recipe.getType().name().endsWith("_DATA")) {
                        this.putVarInt(input.getDamage());
                    }
                    this.putSlot(smelting.getResult(), true);
                    switch (recipe.getType()) {
                        case FURNACE:
                        case FURNACE_DATA:
                            this.putString(CRAFTING_TAG_FURNACE);
                            break;
                        case SMOKER:
                        case SMOKER_DATA:
                            this.putString(CRAFTING_TAG_SMOKER);
                            break;
                        case BLAST_FURNACE:
                        case BLAST_FURNACE_DATA:
                            this.putString(CRAFTING_TAG_BLAST_FURNACE);
                            break;
                        case CAMPFIRE:
                        case CAMPFIRE_DATA:
                            this.putString(CRAFTING_TAG_CAMPFIRE);
                            break;
                    }
                    break;
                case MULTI:
                    this.putUUID(((MultiRecipe) recipe).getId());
                    this.putUnsignedVarInt(recipeNetworkId++);
                    break;
            }
        }

        this.putUnsignedVarInt(this.brewingEntries.size());
        for (BrewingRecipe recipe : brewingEntries) {
            this.putVarInt(recipe.getInput().getNetworkId());
            this.putVarInt(recipe.getInput().getDamage());
            this.putVarInt(recipe.getIngredient().getNetworkId());
            this.putVarInt(recipe.getIngredient().getDamage());
            this.putVarInt(recipe.getResult().getNetworkId());
            this.putVarInt(recipe.getResult().getDamage());
        }

        this.putUnsignedVarInt(this.containerEntries.size());
        for (ContainerRecipe recipe : containerEntries) {
            this.putVarInt(recipe.getInput().getNetworkId());
            this.putVarInt(recipe.getIngredient().getNetworkId());
            this.putVarInt(recipe.getResult().getNetworkId());
        }

        this.putUnsignedVarInt(0); // Material reducers size

        this.putBoolean(cleanRecipes);
    }

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

}
