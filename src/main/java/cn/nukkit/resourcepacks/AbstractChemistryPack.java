package cn.nukkit.resourcepacks;

public abstract class AbstractChemistryPack implements ResourcePack {

    private static final byte[] EMPTY_DATA = new byte[0];
    
    @Override
    public int getPackSize() {
        return 0;
    }
    
    @Override
    public byte[] getSha256() {
        return EMPTY_DATA;
    }
    
    @Override
    public byte[] getPackChunk(int off, int len) {
        return EMPTY_DATA;
    }
}
