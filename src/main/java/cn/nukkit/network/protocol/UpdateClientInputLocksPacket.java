package cn.nukkit.network.protocol;

import cn.nukkit.math.Vector3f;

public class UpdateClientInputLocksPacket extends DataPacket {
    public int lockComponentData;
    public Vector3f position;

    @Override
    public byte pid() {
        return ProtocolInfo.UPDATE_CLIENT_INPUT_LOCKS_PACKET;
    }

    @Override
    public void decode() {
        this.lockComponentData = this.getVarInt();
        this.position = this.getVector3f();
    }

    @Override
    public void encode() {
        this.reset();
        this.putVarInt(lockComponentData);
        this.putVector3f(position);
    }
}
