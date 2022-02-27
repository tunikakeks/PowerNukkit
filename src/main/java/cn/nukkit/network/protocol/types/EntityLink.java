package cn.nukkit.network.protocol.types;

import cn.nukkit.api.PowerNukkitOnly;
import cn.nukkit.api.Since;

public class EntityLink {
    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    public static final EntityLink[] EMPTY_ARRAY = new EntityLink[0];

    public static final byte TYPE_REMOVE = 0;
    public static final byte TYPE_RIDER = 1;
    public static final byte TYPE_PASSENGER = 2;

    public long fromEntityUniquieId;
    public long toEntityUniquieId;
    public byte type;
    public boolean immediate;
    
    @Since("1.3.0.0-PN")
    public boolean riderInitiated;

    @Since("1.3.0.0-PN")
    public EntityLink(long fromEntityUniquieId, long toEntityUniquieId, byte type, boolean immediate, boolean riderInitiated) {
        this.fromEntityUniquieId = fromEntityUniquieId;
        this.toEntityUniquieId = toEntityUniquieId;
        this.type = type;
        this.immediate = immediate;
        this.riderInitiated = riderInitiated;
    }
}
