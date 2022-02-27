package cn.nukkit.inventory;

import cn.nukkit.api.PowerNukkitOnly;
import cn.nukkit.item.Item;

@PowerNukkitOnly
public class SmokerRecipe implements SmeltingRecipe {

    private final Item output;

    private Item ingredient;

    private double experience;

    @PowerNukkitOnly
    public SmokerRecipe(Item result, Item ingredient) {
        this(result, ingredient, 0);
    }

    public SmokerRecipe(Item result, Item ingredient, double experience) {
        this.output = result.clone();
        this.ingredient = ingredient.clone();
        this.experience = experience;
    }

    @PowerNukkitOnly
    public void setInput(Item item) {
        this.ingredient = item.clone();
    }

    @PowerNukkitOnly
    @Override
    public Item getInput() {
        return this.ingredient.clone();
    }

    @Override
    public double getExperience() {
        return experience;
    }

    @Override
    public Item getResult() {
        return this.output.clone();
    }

    @Override
    public void registerToCraftingManager(CraftingManager manager) {
        manager.registerSmokerRecipe(this);
    }

    @Override
    public RecipeType getType() {
        return this.ingredient.hasMeta() ? RecipeType.SMOKER_DATA : RecipeType.SMOKER;
    }
}
