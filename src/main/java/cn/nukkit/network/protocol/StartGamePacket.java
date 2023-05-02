package cn.nukkit.network.protocol;

import cn.nukkit.Server;
import cn.nukkit.api.Since;
import cn.nukkit.item.RuntimeItems;
import cn.nukkit.level.GameRules;
import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.CompoundTag;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.util.UUID;

/**
 * @since 15-10-13
 */
@Log4j2
@ToString
public class StartGamePacket extends DataPacket {

    public static final byte NETWORK_ID = ProtocolInfo.START_GAME_PACKET;

    public static final int GAME_PUBLISH_SETTING_NO_MULTI_PLAY = 0;
    public static final int GAME_PUBLISH_SETTING_INVITE_ONLY = 1;
    public static final int GAME_PUBLISH_SETTING_FRIENDS_ONLY = 2;
    public static final int GAME_PUBLISH_SETTING_FRIENDS_OF_FRIENDS = 3;
    public static final int GAME_PUBLISH_SETTING_PUBLIC = 4;

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    public long entityUniqueId;
    public long entityRuntimeId;
    public int playerGamemode;
    public float x;
    public float y;
    public float z;
    public float yaw;
    public float pitch;
    public int seed;
    public byte dimension;
    public int generator = 1;
    public int worldGamemode;
    public int difficulty;
    public int spawnX;
    public int spawnY;
    public int spawnZ;
    public boolean hasAchievementsDisabled = true;
    public boolean worldEditor;
    public int dayCycleStopTime = -1; //-1 = not stopped, any positive value = stopped at that time
    public int eduEditionOffer = 0;
    public boolean hasEduFeaturesEnabled = Server.getInstance().isEducationEditionEnabled();
    public float rainLevel;
    public float lightningLevel;
    public boolean hasConfirmedPlatformLockedContent = false;
    public boolean multiplayerGame = true;
    public boolean broadcastToLAN = true;
    public int xblBroadcastIntent = GAME_PUBLISH_SETTING_PUBLIC;
    public int platformBroadcastIntent = GAME_PUBLISH_SETTING_PUBLIC;
    public boolean commandsEnabled;
    public boolean isTexturePacksRequired = false;
    public GameRules gameRules;
    public boolean bonusChest = false;
    public boolean hasStartWithMapEnabled = false;
    @Since("1.3.0.0-PN")
    public boolean trustingPlayers;
    public int permissionLevel = 1;
    public int serverChunkTickRange = 4;
    public boolean hasLockedBehaviorPack = false;
    public boolean hasLockedResourcePack = false;
    public boolean isFromLockedWorldTemplate = false;
    public boolean isUsingMsaGamertagsOnly = false;
    public boolean isFromWorldTemplate = false;
    public boolean isWorldTemplateOptionLocked = false;
    public boolean isOnlySpawningV1Villagers = false;
    public String vanillaVersion = ProtocolInfo.MINECRAFT_VERSION_NETWORK;
    public String levelId = ""; //base64 string, usually the same as world folder name in vanilla
    public String worldName;
    public String premiumWorldTemplateId = "";
    public boolean isTrial = false;
    public boolean isMovementServerAuthoritative;
    @Since("1.3.0.0-PN")
    public boolean isInventoryServerAuthoritative;
    public long currentTick;
    public long blockRegistryChecksum = 0;

    public int enchantmentSeed;

    public String multiplayerCorrelationId = "";
    public byte chatRestrictionLevel;
    
    /**
     * Whether block runtime IDs should be replaced by 32-bit integer hashes of the NBT block state.
     * Unlike runtime IDs, this hashes should be persistent across versions and should make support for data-driven/custom blocks easier.
     *
     * @since v582
     */
    public boolean blockNetworkIdsHashed;
    /**
     * @since v582
     */
    public boolean createdInEditor;
    /**
     * @since v582
     */
    public boolean exportedFromEditor;

    @Override
    public void decode() {

    }

    @Override
    public void encode() {
        this.reset();
        this.putEntityUniqueId(this.entityUniqueId);
        this.putEntityRuntimeId(this.entityRuntimeId);
        this.putVarInt(this.playerGamemode);
        this.putVector3f(this.x, this.y, this.z);
        this.putLFloat(this.yaw);
        this.putLFloat(this.pitch);

        this.putLLong(this.seed);
        this.putLShort(0x00); // SpawnBiomeType - Default
        this.putString("plains"); // UserDefinedBiomeName
        this.putVarInt(this.dimension);
        this.putVarInt(this.generator);
        this.putVarInt(this.worldGamemode);
        this.putVarInt(this.difficulty);
        this.putBlockVector3(this.spawnX, this.spawnY, this.spawnZ);
        this.putBoolean(this.hasAchievementsDisabled);
        this.putBoolean(this.worldEditor);
        this.putBoolean(this.createdInEditor);
        this.putBoolean(this.exportedFromEditor);
        this.putVarInt(this.dayCycleStopTime);
        this.putVarInt(this.eduEditionOffer);
        this.putBoolean(this.hasEduFeaturesEnabled);
        this.putString(""); // Education Edition Product ID
        this.putLFloat(this.rainLevel);
        this.putLFloat(this.lightningLevel);
        this.putBoolean(this.hasConfirmedPlatformLockedContent);
        this.putBoolean(this.multiplayerGame);
        this.putBoolean(this.broadcastToLAN);
        this.putVarInt(this.xblBroadcastIntent);
        this.putVarInt(this.platformBroadcastIntent);
        this.putBoolean(this.commandsEnabled);
        this.putBoolean(this.isTexturePacksRequired);
        this.putGameRules(this.gameRules);
        this.putLInt(0); // Experiment count
        this.putBoolean(false); // Were experiments previously toggled
        this.putBoolean(this.bonusChest);
        this.putBoolean(this.hasStartWithMapEnabled);
        this.putVarInt(this.permissionLevel);
        this.putLInt(this.serverChunkTickRange);
        this.putBoolean(this.hasLockedBehaviorPack);
        this.putBoolean(this.hasLockedResourcePack);
        this.putBoolean(this.isFromLockedWorldTemplate);
        this.putBoolean(this.isUsingMsaGamertagsOnly);
        this.putBoolean(this.isFromWorldTemplate);
        this.putBoolean(this.isWorldTemplateOptionLocked);
        this.putBoolean(this.isOnlySpawningV1Villagers);
        this.putBoolean(false); // disabling persona skins
        this.putBoolean(false); // disabling custom skins
        this.putBoolean(false); // emoteChatMuted
        this.putString(this.vanillaVersion);
        this.putLInt(16); // Limited world width
        this.putLInt(16); // Limited world height
        this.putBoolean(false); // Nether type
        this.putString(""); // EduSharedUriResource buttonName
        this.putString(""); // EduSharedUriResource linkUri
        this.putBoolean(false); // Experimental Gameplay
        this.putByte(this.chatRestrictionLevel);
        this.putBoolean(false); // disables player interactions

        this.putString(this.levelId);
        this.putString(this.worldName);
        this.putString(this.premiumWorldTemplateId);
        this.putBoolean(this.isTrial);
        this.putUnsignedVarInt(this.isMovementServerAuthoritative ? 1 : 0); // 2 - rewind
        this.putVarInt(0); // RewindHistorySize
        this.putBoolean(false); // isServerAuthoritativeBlockBreaking
        this.putLLong(this.currentTick);
        this.putVarInt(this.enchantmentSeed);
        this.putUnsignedVarInt(0); // Custom blocks
        this.put(RuntimeItems.getRuntimeMapping().getItemDataPalette());
        this.putString(this.multiplayerCorrelationId);
        this.putBoolean(this.isInventoryServerAuthoritative);
        this.putString(""); // Server Engine
        try {
            this.put(NBTIO.writeNetwork(new CompoundTag(""))); // PlayerPropertyData
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.putLLong(0L); // BlockRegistryChecksum
        this.putUUID(UUID.randomUUID()); // WorldTemplateId
        this.putBoolean(this.blockNetworkIdsHashed);
        this.putBoolean(false);
    }
}
