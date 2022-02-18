package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.item.Item;
import cn.nukkit.math.BlockFace;

import javax.annotation.Nonnull;

/**
 * @author good777LUCKY
 */
public class BlockGlowStick extends BlockTransparent {
    
    @Override
    public int getId() {
        return GLOW_STICK;
    }
    
    @Override
    public String getName() {
        return "Glow Stick";
    }
    
    @Override
    public boolean place(@Nonnull Item item, @Nonnull Block block, @Nonnull Block target, @Nonnull BlockFace face, double fx, double fy, double fz, Player player) {
        // Can't Place
        return false;
    }
}
