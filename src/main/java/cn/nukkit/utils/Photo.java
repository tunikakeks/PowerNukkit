package cn.nukkit.utils;

import cn.nukkit.network.protocol.PhotoTransferPacket;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Photo {

    private String name;
    private byte[] data;
    private String bookId;

    private PhotoTransferPacket.PhotoType photoType;

    private PhotoTransferPacket.PhotoType sourceType;

    private long ownerId;

    private String newPhotoName;
}
