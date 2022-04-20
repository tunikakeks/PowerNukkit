package cn.nukkit.level;

import cn.nukkit.api.Since;
import lombok.Data;
import lombok.Getter;

@Since("FUTURE")
@Data
public class DimensionData {
    @Getter(onMethod = @__(@Since("FUTURE")))
    private final int dimensionId;
    @Getter(onMethod = @__(@Since("FUTURE")))
    private final int minHeight;
    @Getter(onMethod = @__(@Since("FUTURE")))
    private final int maxHeight;
    @Getter(onMethod = @__(@Since("FUTURE")))
    private final int height;

    @Since("FUTURE")
    public DimensionData(int dimensionId, int minHeight, int maxHeight) {
        this.dimensionId = dimensionId;
        this.minHeight = minHeight;
        this.maxHeight = maxHeight;

        int height = maxHeight - minHeight;
        if (minHeight <= 0 && maxHeight > 0) {
            height += 1; // 0 y coordinate counts too
        }
        this.height = height;
    }
}
