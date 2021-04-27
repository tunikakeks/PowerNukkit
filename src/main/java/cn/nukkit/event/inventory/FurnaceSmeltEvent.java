package cn.nukkit.event.inventory;

import cn.nukkit.blockentity.BlockEntityFurnace;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.block.BlockEvent;
import cn.nukkit.item.Item;

/**
 * @author MagicDroidX (Nukkit Project)
 */
public class FurnaceSmeltEvent extends BlockEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    public static HandlerList getHandlers() {
        return handlers;
    }

    private final BlockEntityFurnace furnace;
    private final Item source;
    private Item result;
    private double experience;

    public FurnaceSmeltEvent(BlockEntityFurnace furnace, Item source, Item result) {
        super(furnace.getBlock());
        this.source = source.clone();
        this.source.setCount(1);
        this.result = result;
        this.furnace = furnace;
        this.experience = 0;
    }

    public FurnaceSmeltEvent(BlockEntityFurnace furnace, Item source, Item result, double experience) {
        super(furnace.getBlock());
        this.source = source.clone();
        this.source.setCount(1);
        this.result = result;
        this.furnace = furnace;
        this.experience = experience;
    }

    public BlockEntityFurnace getFurnace() {
        return furnace;
    }

    public Item getSource() {
        return source;
    }

    public Item getResult() {
        return result;
    }

    public void setResult(Item result) {
        this.result = result;
    }

    public double getExperience() {
        return experience;
    }

    public void setExperience(double experience) {
        this.experience = experience;
    }
}
