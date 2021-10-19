package cn.nukkit.command.defaults;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.lang.TranslationContainer;
import cn.nukkit.level.Sound;
import cn.nukkit.utils.TextFormat;

import java.util.HashMap;
import java.util.Map;

public class PlaySoundCommand extends VanillaCommand {

    private final Map<String, Sound> sounds = new HashMap<>();

    public PlaySoundCommand(String name) {
        super(name,
                "%nukkit.command.plugins.description",
                "%nukkit.command.playsound.usage"
        );
        this.setPermission("nukkit.command.playsound");
        for(Sound sound : Sound.values()) {
            this.sounds.put(sound.getSound().toLowerCase(), sound);
        }
        this.commandParameters.clear();
        this.commandParameters.put("default", new CommandParameter[]{
                CommandParameter.newType("player", false, CommandParamType.TARGET),
                CommandParameter.newEnum("sound", false, this.sounds.keySet().toArray(new String[0])),
                CommandParameter.newType("volume", true, CommandParamType.INT),
                CommandParameter.newType("pitch", true, CommandParamType.INT)
        });
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!this.testPermission(sender)) {
            return true;
        }
        if(args.length < 2) {
            sender.sendMessage(new TranslationContainer("commands.generic.usage", this.usageMessage));
            return true;
        }
        Player target = Server.getInstance().getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage(TextFormat.RED + "Can't find player " + args[0]);
            return true;
        }
        Sound sound = sounds.get(args[1].toLowerCase());
        if(sound == null) {
            sender.sendMessage(TextFormat.RED + "Can't find sound " + args[1]);
            return true;
        }
        float volume = 1;
        float pitch = 1;
        if(args.length >= 3) {
            try {
                volume = Float.parseFloat(args[2]);
                if(volume > 1 || volume < 0) {
                    sender.sendMessage(TextFormat.RED + "Volume must be between 0 and 1");
                    return true;
                }
            } catch (NumberFormatException exception) {
                sender.sendMessage(new TranslationContainer("commands.generic.usage", this.usageMessage));
                return true;
            }
        }
        if(args.length >= 4) {
            try {
                pitch = Float.parseFloat(args[3]);
            } catch (NumberFormatException exception) {
                sender.sendMessage(new TranslationContainer("commands.generic.usage", this.usageMessage));
                return true;
            }
        }
        target.getLevel().addSound(target, sound, volume, pitch, target);
        Command.broadcastCommandMessage(sender, new TranslationContainer("commands.playsound.success", target.getName(), sound.getSound(), volume + "", pitch + ""));
        return true;
    }
}
