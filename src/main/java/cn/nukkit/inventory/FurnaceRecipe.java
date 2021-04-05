package cn.nukkit.inventory;

import cn.nukkit.item.Item;

/**
 * @author MagicDroidX (Nukkit Project)
 */
public class FurnaceRecipe implements SmeltingRecipe {

    private final Item output;

    private Item ingredient;

    private double experience;

    public FurnaceRecipe(Item result, Item ingredient) {
        this(result, ingredient, 0);
    }

    public FurnaceRecipe(Item result, Item ingredient, double experience) {
        this.output = result.clone();
        this.ingredient = ingredient.clone();
        this.experience = experience;
    }

    public void setInput(Item item) {
        this.ingredient = item.clone();
    }

    @Override
    public Item getInput() {
        return this.ingredient.clone();
    }

    @Override
    public Item getResult() {
        return this.output.clone();
    }

    public double getExperience() {
        return experience;
    }

    @Override
    public void registerToCraftingManager(CraftingManager manager) {
        manager.registerFurnaceRecipe(this);
    }

    @Override
    public RecipeType getType() {
        return this.ingredient.hasMeta() ? RecipeType.FURNACE_DATA : RecipeType.FURNACE;
    }
}
