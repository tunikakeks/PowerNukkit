package cn.nukkit.plugin;

import cn.nukkit.api.PowerNukkitOnly;
import cn.nukkit.api.Since;
import lombok.NoArgsConstructor;

@PowerNukkitOnly
@Since("1.3.0.0-PN")
@NoArgsConstructor(onConstructor = @__(@PowerNukkitOnly))
public class PowerNukkitPlugin extends PluginBase {
    private static final PowerNukkitPlugin INSTANCE = new PowerNukkitPlugin();
    
    @PowerNukkitOnly
    @Since("1.3.0.0-PN")
    public static PowerNukkitPlugin getInstance() {
        return INSTANCE;
    }
}
