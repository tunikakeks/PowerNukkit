package cn.nukkit.dispenser;

import cn.nukkit.block.Block;
import cn.nukkit.block.BlockDispenser;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.passive.EntitySheep;
import cn.nukkit.item.Item;
import cn.nukkit.math.BlockFace;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static cn.nukkit.entity.Entity.DATA_FLAGS;
import static cn.nukkit.entity.Entity.DATA_FLAG_SHEARED;

public class ShearsDispenseBehaviour extends DefaultDispenseBehavior {

    @Override
    public Item dispense(BlockDispenser block, BlockFace face, Item item) {
        Block target = block.getSide(face);
        item = item.clone();

        for (Entity entity : target.getLevel().getNearbyEntities(target)) {
            if (entity != null && entity.getNetworkId() == EntitySheep.NETWORK_ID) {
                if (!entity.getDataFlag(DATA_FLAGS, DATA_FLAG_SHEARED)) {
                    try {
                        Method method = entity.getClass().getMethod("shear", boolean.class);
                        try {
                            method.invoke(entity, true);
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    } catch (NoSuchMethodException e) {
                    }
                    item.useOn(entity);
                    return item.getDamage() >= item.getMaxDurability() ? null : item;
                }
            }
        }
        return item;
    }
}
