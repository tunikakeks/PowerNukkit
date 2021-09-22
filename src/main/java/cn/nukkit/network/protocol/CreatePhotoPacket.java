package cn.nukkit.network.protocol;

public class CreatePhotoPacket extends DataPacket {

    public static final byte NETWORK_ID = ProtocolInfo.CREATE_PHOTO_PACKET;

    public long id;

    public String photoName;

    public String photoItemName;

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    @Override
    public void decode() {
        this.id = this.getLLong();
        this.photoName = this.getString();
        this.photoItemName = this.getString();
    }

    @Override
    public void encode() {
        this.reset();
        this.putLLong(id);
        this.putString(this.photoName);
        this.putString(this.photoItemName);
    }
}
