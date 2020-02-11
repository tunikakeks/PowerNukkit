package cn.nukkit.network.protocol;

import cn.nukkit.utils.Binary;
import io.netty.buffer.ByteBuf;
import lombok.ToString;

@ToString
public class BookEditPacket extends DataPacket {

    public static final int NETWORK_ID = ProtocolInfo.BOOK_EDIT_PACKET;

    public static final int TYPE_REPLACE_PAGE = 0;
    public static final int TYPE_ADD_PAGE = 1;
    public static final int TYPE_DELETE_PAGE = 2;
    public static final int TYPE_SWAP_PAGES = 3;
    public static final int TYPE_SIGN_BOOK = 4;

    public int type;
    public int inventorySlot;
    public int pageNumber;
    public int secondaryPageNumber;
    public String text;
    public String photoName;
    public String title;
    public String author;
    public String xUID;

    @Override
    public short pid() {
        return NETWORK_ID;
    }

    @Override
    protected void decode(ByteBuf buffer) {
        type = buffer.readByte();
        inventorySlot = buffer.readByte();
        switch (type) {
            case TYPE_REPLACE_PAGE:
            case TYPE_ADD_PAGE:
                pageNumber = buffer.readByte();
                text = Binary.readString(buffer);
                photoName = Binary.readString(buffer);
                break;
            case TYPE_DELETE_PAGE:
                pageNumber = buffer.readByte();
                break;
            case TYPE_SWAP_PAGES:
                pageNumber = buffer.readByte();
                secondaryPageNumber = buffer.readByte();
                break;
            case TYPE_SIGN_BOOK:
                title = Binary.readString(buffer);
                author = Binary.readString(buffer);
                xUID = Binary.readString(buffer);
                break;
            default:
                // Instead of the exception, ignoring it.
                //throw new IllegalStateException("Unknown book edit type " + type + "!");
        }
    }

    @Override
    protected void encode(ByteBuf buffer) {
        buffer.writeByte(type);
        buffer.writeByte(inventorySlot);
        switch (type) {
            case TYPE_REPLACE_PAGE:
            case TYPE_ADD_PAGE:
                buffer.writeByte(pageNumber);
                Binary.writeString(buffer, text);
                Binary.writeString(buffer, photoName);
                break;
            case TYPE_DELETE_PAGE:
                buffer.writeByte(pageNumber);
                break;
            case TYPE_SWAP_PAGES:
                buffer.writeByte(pageNumber);
                buffer.writeByte(secondaryPageNumber);
                break;
            case TYPE_SIGN_BOOK:
                Binary.writeString(buffer, title);
                Binary.writeString(buffer, author);
                Binary.writeString(buffer, xUID);
            default:
                // Just letting the packet corrupt and get ignored by the client
                //throw new IllegalStateException("Unknown book edit type " + type + "!");
        }
    }
}
