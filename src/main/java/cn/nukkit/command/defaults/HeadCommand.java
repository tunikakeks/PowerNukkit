package cn.nukkit.command.defaults;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.entity.EntityHuman;
import cn.nukkit.entity.data.Skin;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemID;
import cn.nukkit.nbt.tag.CompoundTag;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class HeadCommand extends VanillaCommand {
    public HeadCommand(String name) {
        super(name, "Creates a player head", "/head");
        setPermission("nukkit.command.head");
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!(sender instanceof EntityHuman)) {
            sender.sendMessage("This command can only be sent by players");
            return false;
        }
        if (sender instanceof Player) {
            clonePlayer((Player) sender);
            return true;
        }
        EntityHuman human = (EntityHuman) sender; 
        human.saveNBT();
        Item item = Item.get(ItemID.SKULL, 3);
        item.setNamedTag(new CompoundTag().putCompound("PowerNukkit", new CompoundTag().putCompound("Skin", human.namedTag.getCompound("Skin").copy())));
        for (Item drop: human.getInventory().addItem(item)) {
            human.getLevel().dropItem(human, drop);
        }
        return true;
    }

    void clonePlayer(Player player) {
        player.saveNBT();
        new EntityHuman(player.getChunk(), new CompoundTag()
                .put("Pos", player.namedTag.getList("Pos").copy())
                .put("Rotation", player.namedTag.getList("Rotation").copy())
                .put("Motion", player.namedTag.getList("Motion").copy())
                //.putString("NameTag", player.getName())
                .put("Skin", decapitate(player.getSkin()).saveSkin())
        ).spawnTo(player);
    }
    
    Skin decapitate(Skin skin) {
        //Skin decapitated = skin.copy();
        Skin decapitated = new Skin();
        byte[] skinData = skin.getSkinData().data;
        BufferedImage skinBuffer = new BufferedImage(skin.getSkinData().width, skin.getSkinData().height, BufferedImage.TYPE_INT_ARGB);
        int index;
        for (int x = 0; x < skinBuffer.getWidth(); x++) {
            for (int y = 0; y < skinBuffer.getHeight(); y++) {
                index = (x + y * skinBuffer.getWidth()) * 4;
                skinBuffer.setRGB(x, y, new Color(skinData[index] & 0xFF, skinData[index + 1] & 0xFF,
                        skinData[index + 2] & 0xFF, skinData[index + 3] & 0xFF).getRGB());
            }
        }

        try {
            ImageIO.write(skinBuffer, "png", new File("skin.png"));
        } catch (IOException ex) {
            //Ignore
        }
        decapitated.setGeometryData("{\n" +
                "  \"format_version\" : \"1.14.0\",\n" +
                "  \"minecraft:geometry\" : [\n" +
                "    {\n" +
                "      \"description\" : {\n" +
                "        \"identifier\" : \"geometry.head\",\n" +
                "        \"texture_height\" : 64,\n" +
                "        \"texture_width\" : 64\n" +
                "      },\n" +
                "      \"bones\":[{\"name\":\"head\",\"pivot\":[0,24,0],\"cubes\":[{\"origin\":[-4,0,-4],\"size\":[8,8,8],\"uv\":[0,0]}]}]\n" +
                "    }]}");
        decapitated.setSkinResourcePatch("{\n" +
                "   \"geometry\" : {\n" +
                "      \"default\" : \"geometry.head\"\n" +
                "   }\n" +
                "}\n");
        //decapitated.setGeometryName("head");
        //decapitated.setSkinId("head");
        decapitated.setSkinColor(skin.getSkinColor());
        decapitated.setSkinData(skin.getSkinData());
        /*JsonObject rootGeometry = new Gson().fromJson(decapitated.getGeometryData(), JsonObject.class);
        rootGeometry.getAsJsonArray("minecraft:geometry").forEach(geometry -> {
            JsonArray bones = geometry.getAsJsonObject().getAsJsonArray("bones");
            //List<JsonElement> elementsToRemove = new ArrayList<>(bones.size() - 2);
            bones.forEach(bone -> {
                JsonObject boneObj = bone.getAsJsonObject();
                String boneName = boneObj.get("name").getAsString();
                if (!boneName.equals("root") && !boneName.equals("head") && (!boneObj.has("parent") || !boneObj.get("parent").getAsString().equals("head"))) {
                    //boneObj.remove("poly_mesh");
                    //elementsToRemove.add(bone);
                    boneObj.addProperty("neverRender", true);
                }
            });
            //elementsToRemove.forEach(bones::remove);
        });
        decapitated.setGeometryData(rootGeometry.toString());*/
        
        //decapitated.setPersona(false);
        
        /*List<String> whitelist = Arrays.asList("persona_head", "persona_eyes", "persona_hair", "persona_mouth", "persona_facial_hair", 
                "persona_top", "persona_high_pants");
        decapitated.getPersonaPieces().removeIf(piece-> !whitelist.contains(piece.type));*/
        /*int remove = 7;
        while (remove-- > 0 && decapitated.getPersonaPieces().size() > whitelist.size()) {
            PersonaPiece removed = decapitated.getPersonaPieces().remove(decapitated.getPersonaPieces().size() - 1);
            if (whitelist.contains(removed.type)) {
                decapitated.getPersonaPieces().add(0, removed);
                remove++;
                continue;
            }
            System.out.println("Removed: "+removed.type);
        }*/
        
        return decapitated;
    }
}
