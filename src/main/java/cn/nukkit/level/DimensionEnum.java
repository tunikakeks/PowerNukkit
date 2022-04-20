package cn.nukkit.level;

import cn.nukkit.api.Since;

@Since("FUTURE")
public enum DimensionEnum {
    @Since("FUTURE")
    OVERWORLD(new DimensionData(Level.DIMENSION_OVERWORLD, -64, 319)),

    @Since("FUTURE")
    NETHER(new DimensionData(Level.DIMENSION_NETHER, 0, 127)),

    @Since("FUTURE")
    END(new DimensionData(Level.DIMENSION_THE_END, 0, 255));

    private final DimensionData dimensionData;

    DimensionEnum(DimensionData dimensionData) {
        this.dimensionData = dimensionData;
    }

    @Since("FUTURE")
    public DimensionData getDimensionData() {
        return this.dimensionData;
    }

    @Since("FUTURE")
    public static DimensionData getDataFromId(int dimension) {
        for (DimensionEnum value : values()) {
            if (value.getDimensionData().getDimensionId() == dimension) {
                return value.getDimensionData();
            }
        }
        return null;
    }
}
