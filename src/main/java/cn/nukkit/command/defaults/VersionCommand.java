package cn.nukkit.command.defaults;

import cn.nukkit.Nukkit;
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
                new String[]{}
        );
        this.commandParameters.clear();
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!this.testPermission(sender)) {
            return true;
        }

        sender.sendMessage(Nukkit.PREFIX + "Version " + sender.getServer().getName() + sender.getServer().getNukkitVersion() + "(" + sender.getServer().getGitCommit() + ") [" + sender.getServer().getCodename() + "]" +
                "\n" + TextFormat.DARK_GRAY + "» " + TextFormat.GRAY + "API: " + TextFormat.YELLOW + sender.getServer().getApiVersion() +
                "\n" + TextFormat.DARK_GRAY + "» " + TextFormat.GRAY + "Minecraft-Version: " + TextFormat.YELLOW + sender.getServer().getVersion() + TextFormat.GRAY + " (Protokoll " + ProtocolInfo.CURRENT_PROTOCOL + ")");
        return true;
    }
}
