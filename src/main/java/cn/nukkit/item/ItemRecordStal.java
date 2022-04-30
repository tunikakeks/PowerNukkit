package cn.nukkit.item;

/**
 * @author CreeperFace
 */
public class ItemRecordStal extends ItemRecord {

    public ItemRecordStal() {
        this(0, 1);
    }

    public ItemRecordStal(Integer meta) {
        this(meta, 1);
    }

    public ItemRecordStal(Integer meta, int count) {
        super(RECORD_STAL, meta, count);
        name = "Music Disc Stal";
    }

    @Override
    public String getDiscName() {
        return "C418 - stal";
    }

    @Override
    public String getSoundId() {
        return "record.stal";
    }
}
