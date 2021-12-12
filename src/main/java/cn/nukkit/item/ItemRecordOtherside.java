package cn.nukkit.item;

import cn.nukkit.api.Since;

/**
 * @author PetteriM1
 */
@Since("1.4.0.0-PN")
public class ItemRecordOtherside extends ItemRecord {

    @Since("1.4.0.0-PN")
    public ItemRecordOtherside() {
        this(0, 1);
    }

    @Since("1.4.0.0-PN")
    public ItemRecordOtherside(Integer meta) {
        this(meta, 1);
    }

    @Since("1.4.0.0-PN")
    public ItemRecordOtherside(Integer meta, int count) {
        super(RECORD_OTHERSIDE, meta, count);
    }

    @Override
    public String getSoundId() {
        return "record.otherside";
    }
}
