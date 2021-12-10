package cn.nukkit.resourcepacks;

import cn.nukkit.Server;
import cn.nukkit.api.PowerNukkitOnly;
import cn.nukkit.api.Since;

import com.google.common.io.Files;

import lombok.extern.log4j.Log4j2;

import java.io.File;
import java.util.*;

@Log4j2
public class ResourcePackManager {

    private int maxChunkSize = 102400;
    
    private final Map<UUID, ResourcePack> resourcePacksById = new HashMap<>();
    private ResourcePack[] resourcePacks;

    public ResourcePackManager(File path) {
        if (!path.exists()) {
            path.mkdirs();
        } else if (!path.isDirectory()) {
            throw new IllegalArgumentException(Server.getInstance().getLanguage()
                    .translateString("nukkit.resources.invalid-path", path.getName()));
        }

        List<ResourcePack> loadedResourcePacks = new ArrayList<>();
        for (File pack : path.listFiles()) {
            try {
                ResourcePack resourcePack = null;

                if (!pack.isDirectory()) { //directory resource packs temporarily unsupported
                    switch (Files.getFileExtension(pack.getName())) {
                        case "zip":
                        case "mcpack":
                            resourcePack = new ZippedResourcePack(pack);
                            break;
                        default:
                            log.warn(Server.getInstance().getLanguage()
                                    .translateString("nukkit.resources.unknown-format", pack.getName()));
                            break;
                    }
                }

                if (resourcePack != null) {
                    loadedResourcePacks.add(resourcePack);
                    this.resourcePacksById.put(resourcePack.getPackId(), resourcePack);
                }
            } catch (IllegalArgumentException e) {
                log.warn(Server.getInstance().getLanguage().translateString("nukkit.resources.fail", pack.getName(), e.getMessage()), e);
            }
        }

        this.resourcePacks = loadedResourcePacks.toArray(ResourcePack.EMPTY_ARRAY);
        log.info(Server.getInstance().getLanguage()
                .translateString("nukkit.resources.success", String.valueOf(this.resourcePacks.length)));
    }

    public ResourcePack[] getResourceStack() {
        return this.resourcePacks;
    }

    public ResourcePack getPackById(UUID id) {
        return this.resourcePacksById.get(id);
    }
    
    @PowerNukkitOnly
    @Since("1.5.2.0-PN")
    public int getMaxChunkSize() {
        return this.maxChunkSize;
    }
    
    @PowerNukkitOnly
    @Since("1.5.2.0-PN")
    public void setMaxChunkSize(int size) {
        this.maxChunkSize = size;
    }
}
