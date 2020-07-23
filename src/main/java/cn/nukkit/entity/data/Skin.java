package cn.nukkit.entity.data;

import cn.nukkit.api.PowerNukkitOnly;
import cn.nukkit.api.Since;
import cn.nukkit.nbt.stream.FastByteArrayOutputStream;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.nbt.tag.StringTag;
import cn.nukkit.utils.*;
import com.google.common.base.Preconditions;
import lombok.NoArgsConstructor;
import lombok.ToString;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
@NoArgsConstructor
@ToString(exclude = {"geometryData", "animationData"})
public class Skin {
    private static final int PIXEL_SIZE = 4;

    public static final int SINGLE_SKIN_SIZE = 64 * 32 * PIXEL_SIZE;
    public static final int DOUBLE_SKIN_SIZE = 64 * 64 * PIXEL_SIZE;
    public static final int SKIN_128_64_SIZE = 128 * 64 * PIXEL_SIZE;
    public static final int SKIN_128_128_SIZE = 128 * 128 * PIXEL_SIZE;

    public static final String GEOMETRY_CUSTOM = convertLegacyGeometryName("geometry.humanoid.custom");
    public static final String GEOMETRY_CUSTOM_SLIM = convertLegacyGeometryName("geometry.humanoid.customSlim");

    private final String fullSkinId = UUID.randomUUID().toString();
    private String skinId;
    private String skinResourcePatch = GEOMETRY_CUSTOM;
    private SerializedImage skinData;
    private final List<SkinAnimation> animations = new ArrayList<>();
    private final List<PersonaPiece> personaPieces = new ArrayList<>();
    private final List<PersonaPieceTint> tintColors = new ArrayList<>();
    private SerializedImage capeData;
    private String geometryData;
    private String animationData;
    private boolean premium;
    private boolean persona;
    private boolean capeOnClassic;
    private String capeId;
    private String skinColor = "#0";
    private String armSize = "wide";
    private boolean trusted = false;
    
    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    public Skin(CompoundTag skinTag) {
        if (!skinTag.contains("Transparent")) {
            skinTag.putBoolean("Transparent", false);
        }
        
        if (skinTag.contains("ModelId")) {
            this.setSkinId(skinTag.getString("ModelId"));
        }
        if (skinTag.contains("Data")) {
            byte[] data = skinTag.getByteArray("Data");
            if (skinTag.contains("SkinImageWidth") && skinTag.contains("SkinImageHeight")) {
                int width = skinTag.getInt("SkinImageWidth");
                int height = skinTag.getInt("SkinImageHeight");
                this.setSkinData(new SerializedImage(width, height, data));
            } else {
                this.setSkinData(data);
            }
        }
        if (skinTag.contains("CapeId")) {
            this.setCapeId(skinTag.getString("CapeId"));
        }
        if (skinTag.contains("CapeData")) {
            byte[] data = skinTag.getByteArray("CapeData");
            if (skinTag.contains("CapeImageWidth") && skinTag.contains("CapeImageHeight")) {
                int width = skinTag.getInt("CapeImageWidth");
                int height = skinTag.getInt("CapeImageHeight");
                this.setCapeData(new SerializedImage(width, height, data));
            } else {
                this.setCapeData(data);
            }
        }
        if (skinTag.contains("GeometryName")) {
            this.setGeometryName(skinTag.getString("GeometryName"));
        }
        if (skinTag.contains("SkinResourcePatch")) {
            this.setSkinResourcePatch(new String(skinTag.getByteArray("SkinResourcePatch"), StandardCharsets.UTF_8));
        }
        if (skinTag.contains("GeometryData")) {
            this.setGeometryData(new String(skinTag.getByteArray("GeometryData"), StandardCharsets.UTF_8));
        }
        if (skinTag.contains("AnimationData")) {
            this.setAnimationData(new String(skinTag.getByteArray("AnimationData"), StandardCharsets.UTF_8));
        }
        if (skinTag.contains("PremiumSkin")) {
            this.setPremium(skinTag.getBoolean("PremiumSkin"));
        }
        if (skinTag.contains("PersonaSkin")) {
            this.setPersona(skinTag.getBoolean("PersonaSkin"));
        }
        if (skinTag.contains("CapeOnClassicSkin")) {
            this.setCapeOnClassic(skinTag.getBoolean("CapeOnClassicSkin"));
        }
        if (skinTag.contains("AnimatedImageData")) {
            ListTag<CompoundTag> list = skinTag.getList("AnimatedImageData", CompoundTag.class);
            for (CompoundTag animationTag : list.getAll()) {
                float frames = animationTag.getFloat("Frames");
                int type = animationTag.getInt("Type");
                byte[] image = animationTag.getByteArray("Image");
                int width = animationTag.getInt("ImageWidth");
                int height = animationTag.getInt("ImageHeight");
                this.getAnimations().add(new SkinAnimation(new SerializedImage(width, height, image), type, frames));
            }
        }
        if (skinTag.contains("ArmSize")) {
            this.setArmSize(skinTag.getString("ArmSize"));
        }
        if (skinTag.contains("SkinColor")) {
            this.setSkinColor(skinTag.getString("SkinColor"));
        }
        if (skinTag.contains("PersonaPieces")) {
            ListTag<CompoundTag> pieces = skinTag.getList("PersonaPieces", CompoundTag.class);
            for (CompoundTag piece : pieces.getAll()) {
                this.getPersonaPieces().add(new PersonaPiece(
                        piece.getString("PieceId"),
                        piece.getString("PieceType"),
                        piece.getString("PackId"),
                        piece.getBoolean("IsDefault"),
                        piece.getString("ProductId")
                ));
            }
        }
        if (skinTag.contains("PieceTintColors")) {
            ListTag<CompoundTag> tintColors = skinTag.getList("PieceTintColors", CompoundTag.class);
            for (CompoundTag tintColor : tintColors.getAll()) {
                this.getTintColors().add(new PersonaPieceTint(
                        tintColor.getString("PieceType"),
                        tintColor.getList("Colors", StringTag.class).getAll().stream()
                                .map(stringTag -> stringTag.data).collect(Collectors.toList())
                ));
            }
        }
        if (skinTag.contains("IsTrustedSkin")) {
            this.setTrusted(skinTag.getBoolean("IsTrustedSkin"));
        }
    }

    public boolean isValid() {
        return isValidSkin() && isValidResourcePatch();
    }

    private boolean isValidSkin() {
        return skinId != null && !skinId.trim().isEmpty() &&
                skinData != null && skinData.width >= 64 && skinData.height >= 32 &&
                skinData.data.length >= SINGLE_SKIN_SIZE;
    }

    private boolean isValidResourcePatch() {
        if (skinResourcePatch == null) {
            return false;
        }
        try {
            JSONObject object = (JSONObject) JSONValue.parse(skinResourcePatch);
            JSONObject geometry = (JSONObject) object.get("geometry");
            return geometry.containsKey("default") && geometry.get("default") instanceof String;
        } catch (ClassCastException | NullPointerException e) {
            return false;
        }
    }

    public SerializedImage getSkinData() {
        if (skinData == null) {
            return SerializedImage.EMPTY;
        }
        return skinData;
    }

    public String getSkinId() {
        if (this.skinId == null) {
            this.generateSkinId("Custom");
        }
        return skinId;
    }

    public void setSkinId(String skinId) {
        if (skinId == null || skinId.trim().isEmpty()) {
            return;
        }
        this.skinId = skinId;
    }

    public void generateSkinId(String name) {
        byte[] data = Binary.appendBytes(getSkinData().data, getSkinResourcePatch().getBytes(StandardCharsets.UTF_8));
        this.skinId = UUID.nameUUIDFromBytes(data) + "." + name;
    }

    public void setSkinData(byte[] skinData) {
        setSkinData(SerializedImage.fromLegacy(skinData));
    }

    public void setSkinData(BufferedImage image) {
        setSkinData(parseBufferedImage(image));
    }

    public void setSkinData(SerializedImage skinData) {
        Objects.requireNonNull(skinData, "skinData");
        this.skinData = skinData;
    }

    public void setSkinResourcePatch(String skinResourcePatch) {
        if (skinResourcePatch == null || skinResourcePatch.trim().isEmpty()) {
            skinResourcePatch = GEOMETRY_CUSTOM;
        }
        this.skinResourcePatch = skinResourcePatch;
    }

    public void setGeometryName(String geometryName) {
        if (geometryName == null || geometryName.trim().isEmpty()) {
            skinResourcePatch = GEOMETRY_CUSTOM;
            return;
        }

        this.skinResourcePatch = "{\"geometry\" : {\"default\" : \"" + geometryName + "\"}}";
    }

    public String getSkinResourcePatch() {
        if (this.skinResourcePatch == null) {
            return "";
        }
        return skinResourcePatch;
    }

    public SerializedImage getCapeData() {
        if (capeData == null) {
            return SerializedImage.EMPTY;
        }
        return capeData;
    }

    public String getCapeId() {
        if (capeId == null) {
            return "";
        }
        return capeId;
    }

    public void setCapeId(String capeId) {
        if (capeId == null || capeId.trim().isEmpty()) {
            capeId = null;
        }
        this.capeId = capeId;
    }

    public void setCapeData(byte[] capeData) {
        Objects.requireNonNull(capeData, "capeData");
        Preconditions.checkArgument(capeData.length == SINGLE_SKIN_SIZE || capeData.length == 0, "Invalid legacy cape");
        setCapeData(new SerializedImage(64, 32, capeData));
    }

    public void setCapeData(BufferedImage image) {
        setCapeData(parseBufferedImage(image));
    }

    public void setCapeData(SerializedImage capeData) {
        Objects.requireNonNull(capeData, "capeData");
        this.capeData = capeData;
    }

    public String getGeometryData() {
        if (geometryData == null) {
            return "";
        }
        return geometryData;
    }

    public void setGeometryData(String geometryData) {
        Preconditions.checkNotNull(geometryData, "geometryData");
        if (!geometryData.equals(this.geometryData)) {
            this.geometryData = geometryData;
        }
    }

    public String getAnimationData() {
        if (animationData == null) {
            return "";
        }
        return animationData;
    }

    public void setAnimationData(String animationData) {
        Preconditions.checkNotNull(animationData, "animationData");
        if (!animationData.equals(this.animationData)) {
            this.animationData = animationData;
        }
    }

    public List<SkinAnimation> getAnimations() {
        return animations;
    }

    public List<PersonaPiece> getPersonaPieces() {
        return personaPieces;
    }

    public List<PersonaPieceTint> getTintColors() {
        return tintColors;
    }

    public boolean isPremium() {
        return premium;
    }

    public void setPremium(boolean premium) {
        this.premium = premium;
    }

    public boolean isPersona() {
        return persona;
    }

    public void setPersona(boolean persona) {
        this.persona = persona;
    }

    public boolean isCapeOnClassic() {
        return capeOnClassic;
    }

    public void setCapeOnClassic(boolean capeOnClassic) {
        this.capeOnClassic = capeOnClassic;
    }

    public boolean isTrusted() {
        return trusted;
    }

    public void setTrusted(boolean trusted) {
        this.trusted = trusted;
    }

    public String getSkinColor() {
        return skinColor;
    }

    public void setSkinColor(String skinColor) {
        this.skinColor = skinColor;
    }

    public String getArmSize() {
        return armSize;
    }

    public void setArmSize(String armSize) {
        this.armSize = armSize;
    }

    public String getFullSkinId() {
        return fullSkinId;
    }

    private static SerializedImage parseBufferedImage(BufferedImage image) {
        FastByteArrayOutputStream outputStream = new FastByteArrayOutputStream();
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                Color color = new Color(image.getRGB(x, y), true);
                outputStream.write(color.getRed());
                outputStream.write(color.getGreen());
                outputStream.write(color.getBlue());
                outputStream.write(color.getAlpha());
            }
        }
        image.flush();
        return new SerializedImage(image.getWidth(), image.getHeight(), outputStream.toByteArray());
    }

    private static String convertLegacyGeometryName(String geometryName) {
        return "{\"geometry\" : {\"default\" : \"" + geometryName + "\"}}";
    }
    
    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    public CompoundTag saveSkin() {
        CompoundTag skinTag = new CompoundTag()
                .putByteArray("Data", this.getSkinData().data)
                .putInt("SkinImageWidth", this.getSkinData().width)
                .putInt("SkinImageHeight", this.getSkinData().height)
                .putString("ModelId", this.getSkinId())
                .putString("CapeId", this.getCapeId())
                .putByteArray("CapeData", this.getCapeData().data)
                .putInt("CapeImageWidth", this.getCapeData().width)
                .putInt("CapeImageHeight", this.getCapeData().height)
                .putByteArray("SkinResourcePatch", this.getSkinResourcePatch().getBytes(StandardCharsets.UTF_8))
                .putByteArray("GeometryData", this.getGeometryData().getBytes(StandardCharsets.UTF_8))
                .putByteArray("AnimationData", this.getAnimationData().getBytes(StandardCharsets.UTF_8))
                .putBoolean("PremiumSkin", this.isPremium())
                .putBoolean("PersonaSkin", this.isPersona())
                .putBoolean("CapeOnClassicSkin", this.isCapeOnClassic())
                .putString("ArmSize", this.getArmSize())
                .putString("SkinColor", this.getSkinColor())
                .putBoolean("IsTrustedSkin", this.isTrusted());
        List<SkinAnimation> animations = this.getAnimations();
        if (!animations.isEmpty()) {
            ListTag<CompoundTag> animationsTag = new ListTag<>("AnimatedImageData");
            for (SkinAnimation animation : animations) {
                animationsTag.add(new CompoundTag()
                        .putFloat("Frames", animation.frames)
                        .putInt("Type", animation.type)
                        .putInt("ImageWidth", animation.image.width)
                        .putInt("ImageHeight", animation.image.height)
                        .putByteArray("Image", animation.image.data));
            }
            skinTag.putList(animationsTag);
        }
        List<PersonaPiece> personaPieces = this.getPersonaPieces();
        if (!personaPieces.isEmpty()) {
            ListTag<CompoundTag> piecesTag = new ListTag<>("PersonaPieces");
            for (PersonaPiece piece : personaPieces) {
                piecesTag.add(new CompoundTag().putString("PieceId", piece.id)
                        .putString("PieceType", piece.type)
                        .putString("PackId", piece.packId)
                        .putBoolean("IsDefault", piece.isDefault)
                        .putString("ProductId", piece.productId));
            }
            skinTag.putList(piecesTag);
        }
        List<PersonaPieceTint> tints = this.getTintColors();
        if (!tints.isEmpty()) {
            ListTag<CompoundTag> tintsTag = new ListTag<>("PieceTintColors");
            for (PersonaPieceTint tint : tints) {
                ListTag<StringTag> colors = new ListTag<>("Colors");
                colors.setAll(tint.colors.stream().map(s -> new StringTag("", s)).collect(Collectors.toList()));
                tintsTag.add(new CompoundTag()
                        .putString("PieceType", tint.pieceType)
                        .putList(colors));
            }
        }
        return skinTag;
    }
    
    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    public Skin copy() {
        return new Skin(saveSkin());
    }
}
