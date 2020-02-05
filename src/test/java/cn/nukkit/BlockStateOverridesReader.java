package cn.nukkit;

import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.stream.NBTOutputStream;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.nbt.tag.Tag;

import java.io.*;
import java.nio.ByteOrder;
import java.util.LinkedHashMap;
import java.util.Map;

public class BlockStateOverridesReader {
    public static void main(String[] args) {
        Map<String, int[]> metaOverrides = new LinkedHashMap<>();
        try (InputStream stream = Server.class.getClassLoader().getResourceAsStream("runtime_block_states_overrides.dat")) {
            if (stream == null) {
                throw new AssertionError("Unable to locate block state nbt");
            }

            ListTag<CompoundTag> states;
            try (BufferedInputStream buffered = new BufferedInputStream(stream)) {
                states = NBTIO.read(buffered).getList("Overrides", CompoundTag.class);
            }

            for (CompoundTag override : states.getAll()) {
                if (override.contains("block") && override.contains("meta")) {
                    metaOverrides.put(stringifyBlockState(override), override.getIntArray("meta"));
                }
            }

        } catch (IOException e) {
            throw new AssertionError(e);
        }

        ListTag<CompoundTag> tags;
        try (InputStream stream = Server.class.getClassLoader().getResourceAsStream("runtime_block_states.dat")) {
            if (stream == null) {
                throw new AssertionError("Unable to locate block state nbt");
            }

            //noinspection unchecked
            tags = (ListTag<CompoundTag>) NBTIO.readTag(stream, ByteOrder.LITTLE_ENDIAN, false);
        } catch (IOException e) {
            throw new AssertionError(e);
        }

        for (CompoundTag state : tags.getAll()) {
            String stringfied = stringifyBlockState(state);

            int[] overrides = metaOverrides.remove(stringfied);
            if (overrides != null) {
                state.putIntArray("meta", overrides);
            }
        }

        try (FileOutputStream fos = new FileOutputStream("runtime_block_states.dat");
                BufferedOutputStream bos = new BufferedOutputStream(fos);
                NBTOutputStream out = new NBTOutputStream(bos, ByteOrder.LITTLE_ENDIAN, false)) {
            Tag.writeNamedTag(tags, out);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Remaining: "+metaOverrides.size());
    }

    private static String stringifyBlockState(CompoundTag state) {
        CompoundTag block = state.getCompound("block");
        StringBuilder builder = new StringBuilder(block.getString("name"));
        for (Tag tag : block.getCompound("states").getAllTags()) {
            builder.append(';').append(tag.getName()).append('=').append(tag.parseValue());
        }
        return builder.toString();
    }
}
