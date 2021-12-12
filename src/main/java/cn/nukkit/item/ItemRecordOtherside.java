package cn.nukkit.item;

import cn.nukkit.api.Since;

@Since("FUTURE")
public class ItemRecordOtherside extends ItemRecord {

    @Since("FUTURE")
    public ItemRecordOtherside() {
        this(0, 1);
    }

    @Since("FUTURE")
    public ItemRecordOtherside(Integer meta) {
        this(meta, 1);
    }

    @Since("FUTURE")
    public ItemRecordOtherside(Integer meta, int count) {
        super(RECORD_OTHERSIDE, meta, count);
    }

    @Override
    public String getSoundId() {
        return "record.otherside";
    }
}
