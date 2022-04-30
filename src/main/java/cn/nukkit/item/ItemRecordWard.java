package cn.nukkit.item;

/**
 * @author CreeperFace
 */
public class ItemRecordWard extends ItemRecord {

    public ItemRecordWard() {
        this(0, 1);
    }

    public ItemRecordWard(Integer meta) {
        this(meta, 1);
    }

    public ItemRecordWard(Integer meta, int count) {
        super(RECORD_WARD, meta, count);
        name = "Music Disc Ward";
    }

    @Override
    public String getDiscName() {
        return "C418 - ward";
    }

    @Override
    public String getSoundId() {
        return "record.ward";
    }
}
