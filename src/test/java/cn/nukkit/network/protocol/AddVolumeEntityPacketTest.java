/*
 * https://PowerNukkit.org - The Nukkit you know but Powerful!
 * Copyright (C) 2021  José Roberto de Araújo Júnior
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cn.nukkit.network.protocol;

import cn.nukkit.api.PowerNukkitOnly;
import cn.nukkit.api.Since;
import cn.nukkit.nbt.tag.CompoundTag;
import lombok.val;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author joserobjr
 * @since 2021-07-13
 */
@PowerNukkitOnly
@Since("1.5.2.0-PN")
class AddVolumeEntityPacketTest {
    @Test
    void encodeDecode() {
        val packet = new AddVolumeEntityPacket();
        packet.setId(1L);
        packet.setData(new CompoundTag("A"));
        packet.encode();
        
        val packet2 = new AddVolumeEntityPacket();
        packet2.setBuffer(packet.getBuffer());
        packet2.getUnsignedVarInt();
        packet2.decode();
        
        assertEquals(1L, packet2.getId());
        assertEquals(new CompoundTag("A"), packet2.getData());
        assertTrue(packet2.feof());
    }
}
