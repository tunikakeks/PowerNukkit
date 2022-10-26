package cn.nukkit.network.protocol.types;

public class PropertyData {
    private final int[] intProperties;
    private final float[] floatProperties;

    public PropertyData(int[] intProperties, float[] floatProperties) {
        this.intProperties = intProperties;
        this.floatProperties = floatProperties;
    }

    public int[] getIntProperties() {
        return intProperties;
    }

    public float[] getFloatProperties() {
        return floatProperties;
    }
}
