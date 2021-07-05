package cn.nukkit.item.randomitem;

import cn.nukkit.item.Item;
import cn.nukkit.math.NukkitRandom;

/**
 * @author Snake1999
 * @since 2016/1/15
 */
public class ConstantItemSelector extends Selector {

    protected final Item item;

    protected final int minCount;

    protected final int maxCount;

    private final NukkitRandom random;

    public ConstantItemSelector(int id, Selector parent) {
        this(id, 0, parent);
    }

    public ConstantItemSelector(int id, Integer meta, Selector parent) {
        this(id, meta, 1, parent);
    }

    public ConstantItemSelector(int id, Integer meta, int count, Selector parent) {
        this(Item.get(id, meta, count), parent);
    }

    public ConstantItemSelector(Item item, Selector parent) {
        this(item, parent, 1, 1);
    }

    public ConstantItemSelector(Item item, Selector parent, int minCount, int maxCount) {
        super(parent);
        this.item = item;
        this.minCount = minCount;
        this.maxCount = maxCount;
        this.random = new NukkitRandom();
    }

    public Item getItem() {
        return item;
    }

    public Object select() {
        if(minCount != maxCount) {
            Item item = getItem().clone();
            item.setCount(random.nextRange(minCount, maxCount));
            return item;
        }
        return item;
    }
}
