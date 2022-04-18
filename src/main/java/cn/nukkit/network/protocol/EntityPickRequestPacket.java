package cn.nukkit.network.protocol;

import lombok.ToString;

@ToString
public class EntityPickRequestPacket extends DataPacket {

    public static final byte NETWORK_ID = ProtocolInfo.ENTITY_PICK_REQUEST_PACKET;

    public long eid;
    public boolean addUserData;
    public int selectedSlot;

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    @Override
    public void decode() {
        this.eid = this.getLLong();
        this.selectedSlot = this.getByte();
        this.addUserData = this.getBoolean();
    }

    @Override
    public void encode() {

    }
}
