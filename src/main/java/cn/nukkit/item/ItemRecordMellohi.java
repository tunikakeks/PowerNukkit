package cn.nukkit.item;

/**
 * @author CreeperFace
 */
public class ItemRecordMellohi extends ItemRecord {

    public ItemRecordMellohi() {
        this(0, 1);
    }

    public ItemRecordMellohi(Integer meta) {
        this(meta, 1);
    }

    public ItemRecordMellohi(Integer meta, int count) {
        super(RECORD_MELLOHI, meta, count);
        name = "Music Disc Mellohi";
    }

    @Override
    public String getDiscName() {
        return "C418 - mellohi";
    }

    @Override
    public String getSoundId() {
        return "record.mellohi";
    }
}
