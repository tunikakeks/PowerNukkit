package cn.nukkit.command.defaults;

import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.lang.TranslationContainer;
import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.plugin.Plugin;
import cn.nukkit.plugin.PluginDescription;
import cn.nukkit.utils.TextFormat;

import java.util.List;

/**
 * @author xtypr
 * @since 2015/11/12
 */
public class VersionCommand extends VanillaCommand {

    public VersionCommand(String name) {
        super(name,
                "",
                "%nukkit.command.version.usage",
                new String[]{"ver", "about"}
        );
        this.commandParameters.clear();
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!this.testPermission(sender)) {
            return true;
        }
        sender.sendMessage(new TranslationContainer("nukkit.server.info.extended", sender.getServer().getName(),
                sender.getServer().getNukkitVersion() + " ("+sender.getServer().getGitCommit()+")",
                sender.getServer().getCodename(),
                sender.getServer().getApiVersion(),
                sender.getServer().getVersion(),
                String.valueOf(ProtocolInfo.CURRENT_PROTOCOL)));
        return true;
    }
}
