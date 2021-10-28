package cn.nukkit.level.biome.impl.nether;

import cn.nukkit.level.generator.populator.impl.nether.PopulatorWarpedGrasses;
import cn.nukkit.level.generator.populator.impl.nether.PopulatorWarpedFungiTree;
import cn.nukkit.level.generator.populator.impl.nether.PopulatorWarpedTwistingVines;

public class WarpedForestBiome extends NetherBiome {

    public WarpedForestBiome() {
        this.addPopulator(new PopulatorWarpedFungiTree());
        this.addPopulator(new PopulatorWarpedGrasses());
        this.addPopulator(new PopulatorWarpedTwistingVines());
    }

    @Override
    public String getName() {
        return "Warped Forest";
    }

    @Override
    public int getCoverBlock() {
        return WARPED_NYLIUM;
    }

    @Override
    public int getMiddleBlock() {
        return NETHERRACK;
    }
}