package cn.nukkit.network.protocol;

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

    }

    @Override
    protected void encode(ByteBuf buffer) {
        //TODO
    }
}
