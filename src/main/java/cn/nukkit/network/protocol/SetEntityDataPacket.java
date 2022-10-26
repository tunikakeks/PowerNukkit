package cn.nukkit.network.protocol;

import cn.nukkit.api.Since;
import cn.nukkit.entity.data.EntityMetadata;
import cn.nukkit.network.protocol.types.PropertyData;
import cn.nukkit.utils.Binary;
import lombok.ToString;

/**
 * @author MagicDroidX (Nukkit Project)
 */
@ToString
public class SetEntityDataPacket extends DataPacket {
    public static final byte NETWORK_ID = ProtocolInfo.SET_ENTITY_DATA_PACKET;

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    public long eid;
    public EntityMetadata metadata;
    public PropertyData properties = new PropertyData(new int[0], new float[0]);
    @Since("1.4.0.0-PN") public long frame;

    @Override
    public void decode() {

    }

    @Override
    public void encode() {
        this.reset();
        this.putEntityRuntimeId(this.eid);
        this.put(Binary.writeMetadata(this.metadata));
        this.putUnsignedVarInt(this.properties.getIntProperties().length);
        int intLength = this.properties.getIntProperties().length;
        for (int i = 0; i < intLength; i++) {
            this.putUnsignedVarInt(i);
            this.putVarInt(this.properties.getIntProperties()[i]);
        }
        this.putUnsignedVarInt(this.properties.getFloatProperties().length);
        int floatLength = this.properties.getFloatProperties().length;
        for (int i = 0; i < floatLength; i++) {
            this.putUnsignedVarInt(i);
            this.putLFloat(this.properties.getFloatProperties()[i]);
        }
        this.putUnsignedVarLong(this.frame);
    }
}
