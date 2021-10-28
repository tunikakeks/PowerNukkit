package cn.nukkit.level.biome.impl.nether;

import cn.nukkit.level.generator.populator.impl.nether.PopulatorCrimsonFungiTree;
import cn.nukkit.level.generator.populator.impl.nether.PopulatorCrimsonGrasses;
import cn.nukkit.level.generator.populator.impl.nether.PopulatorCrimsonWeepingVines;

public class CrimsonForestBiome extends NetherBiome {

    public CrimsonForestBiome() {
        this.addPopulator(new PopulatorCrimsonFungiTree());
        this.addPopulator(new PopulatorCrimsonGrasses());
        this.addPopulator(new PopulatorCrimsonWeepingVines());
    }

    @Override
    public String getName() {
        return "Crimson Forest";
    }

    @Override
    public int getCoverBlock() {
        return CRIMSON_NYLIUM;
    }

    @Override
    public int getMiddleBlock() {
        return NETHERRACK;
    }
}