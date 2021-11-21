package cn.nukkit.level.generator;

import cn.nukkit.level.ChunkManager;
import cn.nukkit.math.NukkitRandom;
import cn.nukkit.math.Vector3;
import lombok.extern.log4j.Log4j2;

import java.util.HashMap;
import java.util.Map;

@Log4j2
public class Empty extends Generator {

    @Override
    public int getId() {
        return TYPE_EMPTY;
    }

    private ChunkManager level;

    @Override
    public ChunkManager getChunkManager() {
        return level;
    }

    @Override
    public Map<String, Object> getSettings() {
        return null;
    }

    @Override
    public String getName() {
        return "empty";
    }

    public Empty() {
        this(new HashMap<>());
    }

    public Empty(Map<String, Object> options) {

    }

    @Override
    public void init(ChunkManager level, NukkitRandom random) {
        this.level = level;
    }

    @Override
    public void generateChunk(int chunkX, int chunkZ) {
        level.getChunk(chunkX, chunkZ).setGenerated();
    }

    @Override
    public void populateChunk(int chunkX, int chunkZ) {

    }

    @Override
    public Vector3 getSpawn() {
        return new Vector3(0, 0, 0);
    }
}
