package cn.nukkit.network.protocol;

public class PhotoTransferPacket extends DataPacket {

    public String name;
    public byte[] data;
    public String bookId;

    public PhotoType photoType;

    public PhotoType sourceType;

    public long ownerId;

    public String newPhotoName;

    @Override
    public byte pid() {
        return ProtocolInfo.PHOTO_TRANSFER_PACKET;
    }

    @Override
    public void decode() {
        this.name = this.getString();
        this.data = this.getByteArray();
        this.bookId = this.getString();
        this.photoType = PhotoType.from(this.getByte());
        this.sourceType = PhotoType.from(this.getByte());
        this.ownerId = this.getLLong();
        this.newPhotoName = this.getString();
    }

    @Override
    public void encode() {

    }

    public enum PhotoType {
        PORTFOLIO,
        PHOTO_ITEM,
        BOOK;

        private static final PhotoType[] VALUES = values();

        public static PhotoType from(int id) {
            return id >= 0 && id < VALUES.length ? VALUES[id] : null;
        }
    }
}
