package cn.nukkit.network.protocol;

import lombok.ToString;

@ToString
public class ModalFormResponsePacket extends DataPacket {

    public int formId;
    public String data;
    public String cancelReason;

    @Override
    public byte pid() {
        return ProtocolInfo.MODAL_FORM_RESPONSE_PACKET;
    }

    @Override
    public void decode() {
        this.formId = this.getVarInt();
        this.data = this.getString(); //Data will be null if player close form without submit (by cross button or ESC)
        this.cancelReason = this.getString();
    }

    @Override
    public void encode() {

    }
}
