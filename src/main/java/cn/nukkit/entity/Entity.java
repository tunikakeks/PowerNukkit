package cn.nukkit.entity;

import cn.nukkit.AdventureSettings;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.api.DeprecationDetails;
import cn.nukkit.api.PowerNukkitDifference;
import cn.nukkit.api.PowerNukkitOnly;
import cn.nukkit.api.Since;
import cn.nukkit.block.*;
import cn.nukkit.blockentity.BlockEntityPistonArm;
import cn.nukkit.blockstate.BlockState;
import cn.nukkit.entity.data.*;
import cn.nukkit.event.Event;
import cn.nukkit.event.entity.*;
import cn.nukkit.event.entity.EntityDamageEvent.DamageCause;
import cn.nukkit.event.entity.EntityPortalEnterEvent.PortalType;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.event.player.PlayerInteractEvent.Action;
import cn.nukkit.event.player.PlayerTeleportEvent;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemID;
import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.item.enchantment.sideeffect.SideEffect;
import cn.nukkit.level.*;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.*;
import cn.nukkit.metadata.MetadataValue;
import cn.nukkit.metadata.Metadatable;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.DoubleTag;
import cn.nukkit.nbt.tag.FloatTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.network.protocol.*;
import cn.nukkit.network.protocol.types.EntityLink;
import cn.nukkit.plugin.Plugin;
import cn.nukkit.potion.Effect;
import cn.nukkit.scheduler.Task;
import cn.nukkit.utils.ChunkException;
import cn.nukkit.utils.TextFormat;
import cn.nukkit.utils.Utils;
import co.aikar.timings.Timing;
import co.aikar.timings.Timings;
import co.aikar.timings.TimingsHistory;
import com.google.common.collect.Iterables;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntCollection;
import lombok.extern.log4j.Log4j2;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Constructor;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.BiPredicate;

import static cn.nukkit.network.protocol.SetEntityLinkPacket.*;
import static cn.nukkit.utils.Utils.dynamic;

/**
 * @author MagicDroidX
 */
@Log4j2
@PowerNukkitDifference(since = "1.4.0.0-PN",
        info = "All DATA constants were made dynamic because they have tendency to change on Minecraft updates, " +
                "these dynamic calls will avoid the need of plugin recompilations after Minecraft updates that " +
                "shifts the data values")
public abstract class Entity extends Location implements Metadatable {
    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    public static final Entity[] EMPTY_ARRAY = new Entity[0];

    public static final int NETWORK_ID = -1;

    public abstract int getNetworkId();

    public static final int DATA_TYPE_BYTE = 0;
    public static final int DATA_TYPE_SHORT = 1;
    public static final int DATA_TYPE_INT = 2;
    public static final int DATA_TYPE_FLOAT = 3;
    public static final int DATA_TYPE_STRING = 4;
    public static final int DATA_TYPE_NBT = 5;
    public static final int DATA_TYPE_POS = 6;
    public static final int DATA_TYPE_LONG = 7;
    public static final int DATA_TYPE_VECTOR3F = 8;

    public static final int DATA_FLAGS = dynamic(0);
    public static final int DATA_HEALTH = dynamic(1); //int (minecart/boat)
    public static final int DATA_VARIANT = dynamic(2); //int
    public static final int DATA_COLOR = dynamic(3); //byte
    public static final int DATA_COLOUR = DATA_COLOR;
    public static final int DATA_NAMETAG = dynamic(4); //string
    public static final int DATA_OWNER_EID = dynamic(5); //long
    public static final int DATA_TARGET_EID = dynamic(6); //long
    public static final int DATA_AIR = dynamic(7); //short
    public static final int DATA_POTION_COLOR = dynamic(8); //int (ARGB!)
    public static final int DATA_POTION_AMBIENT = dynamic(9); //byte
    public static final int DATA_JUMP_DURATION = dynamic(10); //long
    public static final int DATA_HURT_TIME = dynamic(11); //int (minecart/boat)
    public static final int DATA_HURT_DIRECTION = dynamic(12); //int (minecart/boat)
    public static final int DATA_PADDLE_TIME_LEFT = dynamic(13); //float
    public static final int DATA_PADDLE_TIME_RIGHT = dynamic(14); //float
    public static final int DATA_EXPERIENCE_VALUE = dynamic(15); //int (xp orb)
    public static final int DATA_DISPLAY_ITEM = dynamic(16); //int (id | (data << 16))
    public static final int DATA_DISPLAY_OFFSET = dynamic(17); //int
    public static final int DATA_HAS_DISPLAY = dynamic(18); //byte (must be 1 for minecart to show block inside)
    @Since("1.2.0.0-PN") public static final int DATA_SWELL = dynamic(19);
    @Since("1.2.0.0-PN") public static final int DATA_OLD_SWELL = dynamic(20);
    @Since("1.2.0.0-PN") public static final int DATA_SWELL_DIR = dynamic(21);
    @Since("1.2.0.0-PN") public static final int DATA_CHARGE_AMOUNT = dynamic(22);
    public static final int DATA_ENDERMAN_HELD_RUNTIME_ID = dynamic(23); //short
    @PowerNukkitOnly @Since("1.4.0.0-PN") public static final int DATA_CLIENT_EVENT = dynamic(24); //byte

    @Deprecated @DeprecationDetails(since = "1.4.0.0-PN", by = "PowerNukkit",
            reason = "Apparently this the ID 24 was reused to represent CLIENT_EVENT but Cloudburst Nukkit is still mapping it as age")
    public static final int DATA_ENTITY_AGE = dynamic(DATA_CLIENT_EVENT); //short

    @PowerNukkitOnly @Since("1.4.0.0-PN") public static final int DATA_USING_ITEM = dynamic(25); //byte
    public static final int DATA_PLAYER_FLAGS = dynamic(26); //byte
    @Since("1.2.0.0-PN") public static final int DATA_PLAYER_INDEX = dynamic(27);
    public static final int DATA_PLAYER_BED_POSITION = dynamic(28); //block coords
    public static final int DATA_FIREBALL_POWER_X = dynamic(29); //float
    public static final int DATA_FIREBALL_POWER_Y = dynamic(30); //float
    public static final int DATA_FIREBALL_POWER_Z = dynamic(31); //float
    @Since("1.2.0.0-PN") public static final int DATA_AUX_POWER = dynamic(32); //???
    @Since("1.2.0.0-PN") public static final int DATA_FISH_X = dynamic(33); //float
    @Since("1.2.0.0-PN") public static final int DATA_FISH_Z = dynamic(34); //float
    @Since("1.2.0.0-PN") public static final int DATA_FISH_ANGLE = dynamic(35); //float
    public static final int DATA_POTION_AUX_VALUE = dynamic(36); //short
    public static final int DATA_LEAD_HOLDER_EID = dynamic(37); //long
    public static final int DATA_SCALE = dynamic(38); //float
    @Since("1.2.0.0-PN") public static final int DATA_HAS_NPC_COMPONENT = dynamic(39); //byte
    public static final int DATA_NPC_SKIN_ID = dynamic(40); //string
    public static final int DATA_URL_TAG = dynamic(41); //string
    public static final int DATA_MAX_AIR = dynamic(42); //short
    public static final int DATA_MARK_VARIANT = dynamic(43); //int
    public static final int DATA_CONTAINER_TYPE = dynamic(44); //byte
    public static final int DATA_CONTAINER_BASE_SIZE = dynamic(45); //int
    public static final int DATA_CONTAINER_EXTRA_SLOTS_PER_STRENGTH = dynamic(46); //int
    public static final int DATA_BLOCK_TARGET = dynamic(47); //block coords (ender crystal)
    public static final int DATA_WITHER_INVULNERABLE_TICKS = dynamic(48); //int
    public static final int DATA_WITHER_TARGET_1 = dynamic(49); //long
    public static final int DATA_WITHER_TARGET_2 = dynamic(50); //long
    public static final int DATA_WITHER_TARGET_3 = dynamic(51); //long
    @Since("1.2.0.0-PN") public static final int DATA_AERIAL_ATTACK = dynamic(52);
    public static final int DATA_BOUNDING_BOX_WIDTH = dynamic(53); //float
    public static final int DATA_BOUNDING_BOX_HEIGHT = dynamic(54); //float
    public static final int DATA_FUSE_LENGTH = dynamic(55); //int
    public static final int DATA_RIDER_SEAT_POSITION = dynamic(56); //vector3f
    public static final int DATA_RIDER_ROTATION_LOCKED = dynamic(57); //byte
    public static final int DATA_RIDER_MAX_ROTATION = dynamic(58); //float
    public static final int DATA_RIDER_MIN_ROTATION = dynamic(59); //float
    @Since("1.4.0.0-PN") public static final int DATA_RIDER_ROTATION_OFFSET = dynamic(60);
    public static final int DATA_AREA_EFFECT_CLOUD_RADIUS = dynamic(61); //float
    public static final int DATA_AREA_EFFECT_CLOUD_WAITING = dynamic(62); //int
    public static final int DATA_AREA_EFFECT_CLOUD_PARTICLE_ID = dynamic(63); //int
    @Since("1.2.0.0-PN") public static final int DATA_SHULKER_PEEK_ID = dynamic(64); //int
    public static final int DATA_SHULKER_ATTACH_FACE = dynamic(65); //byte
    @Since("1.2.0.0-PN") public static final int DATA_SHULKER_ATTACHED = dynamic(66); //short
    public static final int DATA_SHULKER_ATTACH_POS = dynamic(67); //block coords
    public static final int DATA_TRADING_PLAYER_EID = dynamic(68); //long
    @Since("1.2.0.0-PN") public static final int DATA_TRADING_CAREER = dynamic(69);
    @Since("1.2.0.0-PN") public static final int DATA_HAS_COMMAND_BLOCK = dynamic(70); //byte
    @Since("1.2.0.0-PN") public static final int DATA_COMMAND_BLOCK_COMMAND = dynamic(71); //string
    public static final int DATA_COMMAND_BLOCK_LAST_OUTPUT = dynamic(72); //string
    public static final int DATA_COMMAND_BLOCK_TRACK_OUTPUT = dynamic(73); //byte
    public static final int DATA_CONTROLLING_RIDER_SEAT_NUMBER = dynamic(74); //byte
    public static final int DATA_STRENGTH = dynamic(75); //int
    public static final int DATA_MAX_STRENGTH = dynamic(76); //int
    @Since("1.2.0.0-PN") public static final int DATA_SPELL_CASTING_COLOR = dynamic(77); //int
    public static final int DATA_LIMITED_LIFE = dynamic(78); //int
    public static final int DATA_ARMOR_STAND_POSE_INDEX = dynamic(79); //int
    public static final int DATA_ENDER_CRYSTAL_TIME_OFFSET = dynamic(80); //int
    public static final int DATA_ALWAYS_SHOW_NAMETAG = dynamic(81); //byte
    public static final int DATA_COLOR_2 = dynamic(82); //byte
    @Since("1.2.0.0-PN") public static final int DATA_NAME_AUTHOR = dynamic(83);
    public static final int DATA_SCORE_TAG = dynamic(84); //String
    public static final int DATA_BALLOON_ATTACHED_ENTITY = dynamic(85); //long
    public static final int DATA_PUFFERFISH_SIZE = dynamic(86); //byte
    @Since("1.2.0.0-PN") public static final int DATA_BUBBLE_TIME = dynamic(87); //int
    @Since("1.2.0.0-PN") public static final int DATA_AGENT = dynamic(88); //long
    @Since("1.2.0.0-PN") public static final int DATA_SITTING_AMOUNT = dynamic(89); //??
    @Since("1.2.0.0-PN") public static final int DATA_SITTING_AMOUNT_PREVIOUS = dynamic(90); //??
    @Since("1.2.0.0-PN") public static final int DATA_EATING_COUNTER = dynamic(91); //int
    public static final int DATA_FLAGS_EXTENDED = dynamic(92); //flags
    @Since("1.2.0.0-PN") public static final int DATA_LAYING_AMOUNT = dynamic(93); //??
    @Since("1.2.0.0-PN") public static final int DATA_LAYING_AMOUNT_PREVIOUS = dynamic(94); //??
    @Since("1.2.0.0-PN") public static final int DATA_DURATION = dynamic(95); //int
    @Since("1.2.0.0-PN") public static final int DATA_SPAWN_TIME = dynamic(96); //int
    @Since("1.2.0.0-PN") public static final int DATA_CHANGE_RATE = dynamic(97); //float
    @Since("1.2.0.0-PN") public static final int DATA_CHANGE_ON_PICKUP = dynamic(98); //float
    @Since("1.2.0.0-PN") public static final int DATA_PICKUP_COUNT = dynamic(99); //int
    @Since("1.4.0.0-PN") public static final int DATA_INTERACTIVE_TAG = dynamic(100); //string (button text)

    @PowerNukkitOnly("Removed from Cloudburst Nukkit")
    @Deprecated
    @DeprecationDetails(by = "Cloudburst Nukkit", reason = "Duplicated and removed", replaceWith = "DATA_INTERACTIVE_TAG", since = "1.6.0.0-PN")
    @Since("1.2.0.0-PN")
    public static final int DATA_INTERACT_TEXT = dynamic(DATA_INTERACTIVE_TAG); //string

    public static final int DATA_TRADE_TIER = dynamic(101); //int
    public static final int DATA_MAX_TRADE_TIER = dynamic(102); //int
    @Since("1.2.0.0-PN") public static final int DATA_TRADE_EXPERIENCE = dynamic(103); //int
    @Since("1.1.1.0-PN") public static final int DATA_SKIN_ID = dynamic(104); //int
    @Since("1.2.0.0-PN") public static final int DATA_SPAWNING_FRAMES = dynamic(105); //??
    @Since("1.2.0.0-PN") public static final int DATA_COMMAND_BLOCK_TICK_DELAY = dynamic(106); //int
    @Since("1.2.0.0-PN") public static final int DATA_COMMAND_BLOCK_EXECUTE_ON_FIRST_TICK = dynamic(107); //byte
    @Since("1.2.0.0-PN") public static final int DATA_AMBIENT_SOUND_INTERVAL = dynamic(108); //float
    @Since("1.3.0.0-PN") public static final int DATA_AMBIENT_SOUND_INTERVAL_RANGE = dynamic(109); //float
    @Since("1.2.0.0-PN") public static final int DATA_AMBIENT_SOUND_EVENT_NAME = dynamic(110); //string
    @Since("1.2.0.0-PN") public static final int DATA_FALL_DAMAGE_MULTIPLIER = dynamic(111); //float
    @Since("1.2.0.0-PN") public static final int DATA_NAME_RAW_TEXT = dynamic(112); //??
    @Since("1.2.0.0-PN") public static final int DATA_CAN_RIDE_TARGET = dynamic(113); //byte
    @Since("1.3.0.0-PN") public static final int DATA_LOW_TIER_CURED_DISCOUNT = dynamic(114); //int
    @Since("1.3.0.0-PN") public static final int DATA_HIGH_TIER_CURED_DISCOUNT = dynamic(115); //int
    @Since("1.3.0.0-PN") public static final int DATA_NEARBY_CURED_DISCOUNT = dynamic(116); //int
    @Since("1.3.0.0-PN") public static final int DATA_NEARBY_CURED_DISCOUNT_TIMESTAMP = dynamic(117); //int
    @Since("1.3.0.0-PN") public static final int DATA_HITBOX = dynamic(118); //NBT
    @Since("1.3.0.0-PN") public static final int DATA_IS_BUOYANT = dynamic(119); //byte
    @Since("1.5.0.0-PN") public static final int DATA_BASE_RUNTIME_ID = dynamic(120); // ???
    @Since("1.4.0.0-PN") public static final int DATA_FREEZING_EFFECT_STRENGTH = dynamic(121); // ???
    @Since("1.3.0.0-PN") public static final int DATA_BUOYANCY_DATA = dynamic(122); //string
    @Since("1.4.0.0-PN") public static final int DATA_GOAT_HORN_COUNT = dynamic(123); // ???
    @Since("1.5.0.0-PN") public static final int DATA_UPDATE_PROPERTIES = dynamic(124); // ???

    // Flags
    public static final int DATA_FLAG_ONFIRE = dynamic(0);
    public static final int DATA_FLAG_SNEAKING = dynamic(1);
    public static final int DATA_FLAG_RIDING = dynamic(2);
    public static final int DATA_FLAG_SPRINTING = dynamic(3);
    public static final int DATA_FLAG_ACTION = dynamic(4);
    public static final int DATA_FLAG_INVISIBLE = dynamic(5);
    public static final int DATA_FLAG_TEMPTED = dynamic(6);
    public static final int DATA_FLAG_INLOVE = dynamic(7);
    public static final int DATA_FLAG_SADDLED = dynamic(8);
    public static final int DATA_FLAG_POWERED = dynamic(9);
    public static final int DATA_FLAG_IGNITED = dynamic(10);
    public static final int DATA_FLAG_BABY = dynamic(11); //disable head scaling
    public static final int DATA_FLAG_CONVERTING = dynamic(12);
    public static final int DATA_FLAG_CRITICAL = dynamic(13);
    public static final int DATA_FLAG_CAN_SHOW_NAMETAG = dynamic(14);
    public static final int DATA_FLAG_ALWAYS_SHOW_NAMETAG = dynamic(15);
    public static final int DATA_FLAG_IMMOBILE = dynamic(16);
    public static final int DATA_FLAG_NO_AI = DATA_FLAG_IMMOBILE;
    public static final int DATA_FLAG_SILENT = dynamic(17);
    public static final int DATA_FLAG_WALLCLIMBING = dynamic(18);
    public static final int DATA_FLAG_CAN_CLIMB = dynamic(19);
    public static final int DATA_FLAG_SWIMMER = dynamic(20);
    public static final int DATA_FLAG_CAN_FLY = dynamic(21);
    public static final int DATA_FLAG_WALKER = dynamic(22);
    public static final int DATA_FLAG_RESTING = dynamic(23);
    public static final int DATA_FLAG_SITTING = dynamic(24);
    public static final int DATA_FLAG_ANGRY = dynamic(25);
    public static final int DATA_FLAG_INTERESTED = dynamic(26);
    public static final int DATA_FLAG_CHARGED = dynamic(27);
    public static final int DATA_FLAG_TAMED = dynamic(28);
    public static final int DATA_FLAG_ORPHANED = dynamic(29);
    public static final int DATA_FLAG_LEASHED = dynamic(30);
    public static final int DATA_FLAG_SHEARED = dynamic(31);
    public static final int DATA_FLAG_GLIDING = dynamic(32);
    public static final int DATA_FLAG_ELDER = dynamic(33);
    public static final int DATA_FLAG_MOVING = dynamic(34);
    public static final int DATA_FLAG_BREATHING = dynamic(35);
    public static final int DATA_FLAG_CHESTED = dynamic(36);
    public static final int DATA_FLAG_STACKABLE = dynamic(37);
    public static final int DATA_FLAG_SHOWBASE = dynamic(38);
    public static final int DATA_FLAG_REARING = dynamic(39);
    public static final int DATA_FLAG_VIBRATING = dynamic(40);
    public static final int DATA_FLAG_IDLING = dynamic(41);
    public static final int DATA_FLAG_EVOKER_SPELL = dynamic(42);
    public static final int DATA_FLAG_CHARGE_ATTACK = dynamic(43);
    public static final int DATA_FLAG_WASD_CONTROLLED = dynamic(44);
    public static final int DATA_FLAG_CAN_POWER_JUMP = dynamic(45);
    public static final int DATA_FLAG_LINGER = dynamic(46);
    public static final int DATA_FLAG_HAS_COLLISION = dynamic(47);
    public static final int DATA_FLAG_GRAVITY = dynamic(48);
    public static final int DATA_FLAG_FIRE_IMMUNE = dynamic(49);
    public static final int DATA_FLAG_DANCING = dynamic(50);
    public static final int DATA_FLAG_ENCHANTED = dynamic(51);
    public static final int DATA_FLAG_SHOW_TRIDENT_ROPE = dynamic(52); // tridents show an animated rope when enchanted with loyalty after they are thrown and return to their owner. To be combined with DATA_OWNER_EID
    public static final int DATA_FLAG_CONTAINER_PRIVATE = dynamic(53); //inventory is private, doesn't drop contents when killed if true
    @Since("1.2.0.0-PN") public static final int DATA_FLAG_IS_TRANSFORMING = dynamic(54);
    public static final int DATA_FLAG_SPIN_ATTACK = dynamic(55);
    public static final int DATA_FLAG_SWIMMING = dynamic(56);
    public static final int DATA_FLAG_BRIBED = dynamic(57); //dolphins have this set when they go to find treasure for the player
    public static final int DATA_FLAG_PREGNANT = dynamic(58);
    public static final int DATA_FLAG_LAYING_EGG = dynamic(59);
    @Since("1.2.0.0-PN") public static final int DATA_FLAG_RIDER_CAN_PICK = dynamic(60);
    @PowerNukkitOnly @Since("1.2.0.0-PN") public static final int DATA_FLAG_TRANSITION_SITTING = dynamic(61); // PowerNukkit but without typo

    /**
     * @deprecated This is from NukkitX but it has a typo which we can't remove unless NukkitX removes from their side.
     * @see #DATA_FLAG_TRANSITION_SITTING
     */
    @Deprecated @DeprecationDetails(
            reason = "This is from NukkitX but it has a typo which we can't remove unless NukkitX removes from their side.",
            since = "1.2.0.0-PN",
            replaceWith = "DATA_FLAG_TRANSITION_SITTING")
    @Since("1.2.0.0-PN") public static final int DATA_FLAG_TRANSITION_SETTING = DATA_FLAG_TRANSITION_SITTING; // NukkitX with the same typo

    public static final int DATA_FLAG_EATING = dynamic(62);
    public static final int DATA_FLAG_LAYING_DOWN = dynamic(63);
    public static final int DATA_FLAG_SNEEZING = dynamic(64);
    public static final int DATA_FLAG_TRUSTING = dynamic(65);
    public static final int DATA_FLAG_ROLLING = dynamic(66);
    public static final int DATA_FLAG_SCARED = dynamic(67);
    public static final int DATA_FLAG_IN_SCAFFOLDING = dynamic(68);
    public static final int DATA_FLAG_OVER_SCAFFOLDING = dynamic(69);
    public static final int DATA_FLAG_FALL_THROUGH_SCAFFOLDING = dynamic(70);
    public static final int DATA_FLAG_BLOCKING = dynamic(71); //shield
    @Since("1.2.0.0-PN") public static final int DATA_FLAG_TRANSITION_BLOCKING = dynamic(72);
    @Since("1.2.0.0-PN") public static final int DATA_FLAG_BLOCKED_USING_SHIELD = dynamic(73);
    @Since("1.2.0.0-PN") public static final int DATA_FLAG_BLOCKED_USING_DAMAGED_SHIELD = dynamic(74);
    @Since("1.2.0.0-PN") public static final int DATA_FLAG_SLEEPING = dynamic(75);
    @Since("1.6.0.0-PN") public static final int DATA_FLAG_ENTITY_GROW_UP = dynamic(76);
    @Since("1.2.0.0-PN") public static final int DATA_FLAG_TRADE_INTEREST = dynamic(77);
    @Since("1.2.0.0-PN") public static final int DATA_FLAG_DOOR_BREAKER = dynamic(78);
    @Since("1.2.0.0-PN") public static final int DATA_FLAG_BREAKING_OBSTRUCTION = dynamic(79);
    @Since("1.2.0.0-PN") public static final int DATA_FLAG_DOOR_OPENER = dynamic(80);
    @Since("1.2.0.0-PN") public static final int DATA_FLAG_IS_ILLAGER_CAPTAIN = dynamic(81);
    @Since("1.2.0.0-PN") public static final int DATA_FLAG_STUNNED = dynamic(82);
    @Since("1.2.0.0-PN") public static final int DATA_FLAG_ROARING = dynamic(83);
    @Since("1.2.0.0-PN") public static final int DATA_FLAG_DELAYED_ATTACK = dynamic(84);
    @Since("1.2.0.0-PN") public static final int DATA_FLAG_IS_AVOIDING_MOBS = dynamic(85);
    @Since("1.3.0.0-PN") public static final int DATA_FLAG_IS_AVOIDING_BLOCKS = dynamic(86);
    @Since("1.2.0.0-PN") public static final int DATA_FLAG_FACING_TARGET_TO_RANGE_ATTACK = dynamic(87);
    @Since("1.2.0.0-PN") public static final int DATA_FLAG_HIDDEN_WHEN_INVISIBLE = dynamic(88);
    @Since("1.2.0.0-PN") public static final int DATA_FLAG_IS_IN_UI = dynamic(89);
    @Since("1.2.0.0-PN") public static final int DATA_FLAG_STALKING = dynamic(90);
    @Since("1.2.0.0-PN") public static final int DATA_FLAG_EMOTING = dynamic(91);
    @Since("1.2.0.0-PN") public static final int DATA_FLAG_CELEBRATING = dynamic(92);
    @Since("1.3.0.0-PN") public static final int DATA_FLAG_ADMIRING = dynamic(93);
    @Since("1.3.0.0-PN") public static final int DATA_FLAG_CELEBRATING_SPECIAL = dynamic(94);
    @Since("1.4.0.0-PN") public static final int DATA_FLAG_RAM_ATTACK = dynamic(96);
    @Since("1.5.0.0-PN") public static final int DATA_FLAG_PLAYING_DEAD = dynamic(97);
    @Since("1.6.0.0-PN") public static final int DATA_FLAG_IN_ASCENDABLE_BLOCK = dynamic(98);
    @Since("1.6.0.0-PN") public static final int DATA_FLAG_OVER_DESCENDABLE_BLOCK = dynamic(99);
    public static final int DATA_FLAG_CROAKING = 100;
    public static final int DATA_FLAG_EAT_MOB = 101;

    public static long entityCount = 1;

    private static final Map<String, Class<? extends Entity>> knownEntities = new HashMap<>();
    private static final Map<String, String> shortNames = new HashMap<>();

    protected final Map<Integer, Player> hasSpawned = new ConcurrentHashMap<>();

    protected final Map<Integer, Effect> effects = new ConcurrentHashMap<>();

    protected long id;

    protected final EntityMetadata dataProperties = new EntityMetadata()
            .putLong(DATA_FLAGS, 0)
            .putByte(DATA_COLOR, 0)
            .putShort(DATA_AIR, 400)
            .putShort(DATA_MAX_AIR, 400)
            .putString(DATA_NAMETAG, "")
            .putLong(DATA_LEAD_HOLDER_EID, -1)
            .putFloat(DATA_SCALE, 1f);

    public final List<Entity> passengers = new ArrayList<>();

    public Entity riding = null;

    public FullChunk chunk;

    protected EntityDamageEvent lastDamageCause = null;

    public List<Block> blocksAround = new ArrayList<>();
    public List<Block> collisionBlocks = new ArrayList<>();

    public double lastX;
    public double lastY;
    public double lastZ;

    public boolean firstMove = true;

    public double motionX;
    public double motionY;
    public double motionZ;

    public Vector3 temporalVector;
    public double lastMotionX;
    public double lastMotionY;
    public double lastMotionZ;

    public double lastPitch;
    @Since("1.6.0.0-PN") public double lastYaw;
    @Since("1.6.0.0-PN") public double lastHeadYaw;

    public double pitchDelta;
    @Since("1.6.0.0-PN") public double yawDelta;
    @Since("1.6.0.0-PN") public double headYawDelta;

    public double entityCollisionReduction = 0; // Higher than 0.9 will result a fast collisions
    public AxisAlignedBB boundingBox;
    public boolean onGround;
    public boolean inBlock = false;
    public boolean positionChanged;
    public boolean motionChanged;
    public int deadTicks = 0;
    protected int age = 0;

    protected float health = 20;
    private int maxHealth = 20;

    protected float absorption = 0;

    protected float ySize = 0;
    public boolean keepMovement = false;

    public float fallDistance = 0;
    public int ticksLived = 0;
    public int lastUpdate;
    public int maxFireTicks;
    public int fireTicks = 0;
    public int inPortalTicks = 0;

    @PowerNukkitOnly
    @Since("1.2.1.0-PN")
    protected boolean inEndPortal;

    public float scale = 1;

    public CompoundTag namedTag;

    protected boolean isStatic = false;

    public boolean isCollided = false;
    public boolean isCollidedHorizontally = false;
    public boolean isCollidedVertically = false;

    public int noDamageTicks;
    public boolean justCreated;
    public boolean fireProof;
    public boolean invulnerable;

    protected Server server;

    public double highestPosition;

    public boolean closed = false;

    protected Timing timing;

    protected boolean isPlayer = false;

    private volatile boolean initialized;

    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    public boolean noClip = false;

    public float getHeight() {
        return 0;
    }

    @PowerNukkitOnly
    @Since("1.5.1.0-PN")
    public float getCurrentHeight() {
        if (isSwimming()) {
            return getSwimmingHeight();
        } else {
            return getHeight();
        }
    }

    public float getEyeHeight() {
        return getCurrentHeight() / 2 + 0.1f;
    }

    public float getWidth() {
        return 0;
    }

    public float getLength() {
        return 0;
    }

    protected double getStepHeight() {
        return 0;
    }

    public boolean canCollide() {
        return true;
    }

    protected float getGravity() {
        return 0;
    }

    protected float getDrag() {
        return 0;
    }

    protected float getBaseOffset() {
        return 0;
    }

    public Entity(FullChunk chunk, CompoundTag nbt) {
        if (this instanceof Player) {
            return;
        }

        this.init(chunk, nbt);
    }

    protected void initEntity() {
        if (this.namedTag.contains("ActiveEffects")) {
            ListTag<CompoundTag> effects = this.namedTag.getList("ActiveEffects", CompoundTag.class);
            for (CompoundTag e : effects.getAll()) {
                Effect effect = Effect.getEffect(e.getByte("Id"));
                if (effect == null) {
                    continue;
                }

                effect.setAmplifier(e.getByte("Amplifier")).setDuration(e.getInt("Duration")).setVisible(e.getBoolean("ShowParticles"));

                this.addEffect(effect);
            }
        }

        if (this.namedTag.contains("CustomName")) {
            this.setNameTag(this.namedTag.getString("CustomName"));
            if (this.namedTag.contains("CustomNameVisible")) {
                this.setNameTagVisible(this.namedTag.getBoolean("CustomNameVisible"));
            }
            if(this.namedTag.contains("CustomNameAlwaysVisible")){
                this.setNameTagAlwaysVisible(this.namedTag.getBoolean("CustomNameAlwaysVisible"));
            }
        }

        this.setDataFlag(DATA_FLAGS, DATA_FLAG_HAS_COLLISION, true);
        this.dataProperties.putFloat(DATA_BOUNDING_BOX_HEIGHT, this.getHeight());
        this.dataProperties.putFloat(DATA_BOUNDING_BOX_WIDTH, this.getWidth());
        this.dataProperties.putInt(DATA_HEALTH, (int) this.getHealth());

        this.scheduleUpdate();
    }

    protected final void init(FullChunk chunk, CompoundTag nbt) {
        if ((chunk == null || chunk.getProvider() == null)) {
            throw new ChunkException("Invalid garbage Chunk given to Entity");
        }

        if (this.initialized) {
            // We've already initialized this entity
            return;
        }
        this.initialized = true;

        this.timing = Timings.getEntityTiming(this);

        this.isPlayer = this instanceof Player;
        this.temporalVector = new Vector3();

        this.id = Entity.entityCount++;
        this.justCreated = true;
        this.namedTag = nbt;

        this.chunk = chunk;
        this.setLevel(chunk.getProvider().getLevel());
        this.server = chunk.getProvider().getLevel().getServer();

        this.boundingBox = new SimpleAxisAlignedBB(0, 0, 0, 0, 0, 0);

        ListTag<DoubleTag> posList = this.namedTag.getList("Pos", DoubleTag.class);
        ListTag<FloatTag> rotationList = this.namedTag.getList("Rotation", FloatTag.class);
        ListTag<DoubleTag> motionList = this.namedTag.getList("Motion", DoubleTag.class);
        this.setPositionAndRotation(
                this.temporalVector.setComponents(
                        posList.get(0).data,
                        posList.get(1).data,
                        posList.get(2).data
                ),
                rotationList.get(0).data,
                rotationList.get(1).data
        );

        this.setMotion(this.temporalVector.setComponents(
                motionList.get(0).data,
                motionList.get(1).data,
                motionList.get(2).data
        ));

        if (!this.namedTag.contains("FallDistance")) {
            this.namedTag.putFloat("FallDistance", 0);
        }
        this.fallDistance = this.namedTag.getFloat("FallDistance");
        this.highestPosition = this.y + this.namedTag.getFloat("FallDistance");

        if (!this.namedTag.contains("Fire") || this.namedTag.getShort("Fire") > 32767) {
            this.namedTag.putShort("Fire", 0);
        }
        this.fireTicks = this.namedTag.getShort("Fire");

        if (!this.namedTag.contains("Air")) {
            this.namedTag.putShort("Air", 300);
        }
        this.setDataProperty(new ShortEntityData(DATA_AIR, this.namedTag.getShort("Air")), false);

        if (!this.namedTag.contains("OnGround")) {
            this.namedTag.putBoolean("OnGround", false);
        }
        this.onGround = this.namedTag.getBoolean("OnGround");

        if (!this.namedTag.contains("Invulnerable")) {
            this.namedTag.putBoolean("Invulnerable", false);
        }
        this.invulnerable = this.namedTag.getBoolean("Invulnerable");

        if (!this.namedTag.contains("Scale")) {
            this.namedTag.putFloat("Scale", 1);
        }
        this.scale = this.namedTag.getFloat("Scale");
        this.setDataProperty(new FloatEntityData(DATA_SCALE, scale), false);
        this.setDataProperty(new ByteEntityData(DATA_COLOR, 0), false);

        try {
            this.chunk.addEntity(this);
            this.level.addEntity(this);

            this.initEntity();

            this.lastUpdate = this.server.getTick();

            EntitySpawnEvent event = new EntitySpawnEvent(this);

            this.server.getPluginManager().callEvent(event);

            if (event.isCancelled()) {
                this.close(false);
            } else {
                this.scheduleUpdate();
            }
        } catch(Exception e) {
            this.close(false);
            throw e;
        }
    }

    public boolean hasCustomName() {
        return !this.getNameTag().isEmpty();
    }

    public String getNameTag() {
        return this.getDataPropertyString(DATA_NAMETAG);
    }

    public boolean isNameTagVisible() {
        return this.getDataFlag(DATA_FLAGS, DATA_FLAG_CAN_SHOW_NAMETAG);
    }

    public boolean isNameTagAlwaysVisible() {
        return this.getDataPropertyByte(DATA_ALWAYS_SHOW_NAMETAG) == 1;
    }

    public void setNameTag(String name) {
        this.setDataProperty(new StringEntityData(DATA_NAMETAG, name));
    }

    public void setNameTagVisible() {
        this.setNameTagVisible(true);
    }

    public void setNameTagVisible(boolean value) {
        this.setDataFlag(DATA_FLAGS, DATA_FLAG_CAN_SHOW_NAMETAG, value);
    }

    public void setNameTagAlwaysVisible() {
        this.setNameTagAlwaysVisible(true);
    }

    public void setNameTagAlwaysVisible(boolean value) {
        this.setDataProperty(new ByteEntityData(DATA_ALWAYS_SHOW_NAMETAG, value ? 1 : 0));
    }

    public void setScoreTag(String score) {
        this.setDataProperty(new StringEntityData(DATA_SCORE_TAG, score));
    }

    public String getScoreTag() {
        return this.getDataPropertyString(DATA_SCORE_TAG);
    }

    public boolean isSneaking() {
        return this.getDataFlag(DATA_FLAGS, DATA_FLAG_SNEAKING);
    }

    public void setSneaking() {
        this.setSneaking(true);
    }

    public void setSneaking(boolean value) {
        this.setDataFlag(DATA_FLAGS, DATA_FLAG_SNEAKING, value);
    }

    public boolean isSwimming() {
        return this.getDataFlag(DATA_FLAGS, DATA_FLAG_SWIMMING);
    }

    @PowerNukkitOnly
    @Since("1.5.1.0-PN")
    public float getSwimmingHeight() {
        return getHeight();
    }

    public void setSwimming() {
        this.setSwimming(true);
    }

    public void setSwimming(boolean value) {
        if (isSwimming() == value) {
            return;
        }
        this.setDataFlag(DATA_FLAGS, DATA_FLAG_SWIMMING, value);
        if (Float.compare(getSwimmingHeight(), getHeight()) != 0) {
            recalculateBoundingBox(true);
        }
    }

    public boolean isSprinting() {
        return this.getDataFlag(DATA_FLAGS, DATA_FLAG_SPRINTING);
    }

    public void setSprinting() {
        this.setSprinting(true);
    }

    public void setSprinting(boolean value) {
        this.setDataFlag(DATA_FLAGS, DATA_FLAG_SPRINTING, value);
    }

    public boolean isGliding() {
        return this.getDataFlag(DATA_FLAGS, DATA_FLAG_GLIDING);
    }

    public void setGliding() {
        this.setGliding(true);
    }

    public void setGliding(boolean value) {
        this.setDataFlag(DATA_FLAGS, DATA_FLAG_GLIDING, value);
    }

    public boolean isImmobile() {
        return this.getDataFlag(DATA_FLAGS, DATA_FLAG_IMMOBILE);
    }

    public void setImmobile() {
        this.setImmobile(true);
    }

    public void setImmobile(boolean value) {
        this.setDataFlag(DATA_FLAGS, DATA_FLAG_IMMOBILE, value);
    }

    public boolean canClimb() {
        return this.getDataFlag(DATA_FLAGS, DATA_FLAG_CAN_CLIMB);
    }

    public void setCanClimb() {
        this.setCanClimb(true);
    }

    public void setCanClimb(boolean value) {
        this.setDataFlag(DATA_FLAGS, DATA_FLAG_CAN_CLIMB, value);
    }

    public boolean canClimbWalls() {
        return this.getDataFlag(DATA_FLAGS, DATA_FLAG_WALLCLIMBING);
    }

    public void setCanClimbWalls() {
        this.setCanClimbWalls(true);
    }

    public void setCanClimbWalls(boolean value) {
        this.setDataFlag(DATA_FLAGS, DATA_FLAG_WALLCLIMBING, value);
    }

    public void setScale(float scale) {
        this.scale = scale;
        this.setDataProperty(new FloatEntityData(DATA_SCALE, this.scale));
        this.recalculateBoundingBox();
    }

    public float getScale() {
        return this.scale;
    }

    public List<Entity> getPassengers() {
        return passengers;
    }

    public Entity getPassenger() {
        return Iterables.getFirst(this.passengers, null);
    }

    public boolean isPassenger(Entity entity) {
        return this.passengers.contains(entity);
    }

    public boolean isControlling(Entity entity) {
        return this.passengers.indexOf(entity) == 0;
    }

    public boolean hasControllingPassenger() {
        return !this.passengers.isEmpty() && isControlling(this.passengers.get(0));
    }

    public Entity getRiding() {
        return riding;
    }

    public Map<Integer, Effect> getEffects() {
        return effects;
    }

    public void removeAllEffects() {
        for (Effect effect : this.effects.values()) {
            this.removeEffect(effect.getId());
        }
    }

    public void removeEffect(int effectId) {
        if (this.effects.containsKey(effectId)) {
            Effect effect = this.effects.get(effectId);
            this.effects.remove(effectId);
            effect.remove(this);

            this.recalculateEffectColor();
        }
    }

    public Effect getEffect(int effectId) {
        return this.effects.getOrDefault(effectId, null);
    }

    public boolean hasEffect(int effectId) {
        return this.effects.containsKey(effectId);
    }

    public void addEffect(Effect effect) {
        if (effect == null) {
            return; //here add null means add nothing
        }

        effect.add(this);

        this.effects.put(effect.getId(), effect);

        this.recalculateEffectColor();

        if (effect.getId() == Effect.HEALTH_BOOST) {
            this.setHealth(this.getHealth() + 4 * (effect.getAmplifier() + 1));
        }

    }

    public void recalculateBoundingBox() {
        this.recalculateBoundingBox(true);
    }

    public void recalculateBoundingBox(boolean send) {
        float entityHeight = getCurrentHeight();
        float height = entityHeight * this.scale;
        double radius = (this.getWidth() * this.scale) / 2d;
        this.boundingBox.setBounds(
                x - radius,
                y,
                z - radius,

                x + radius,
                y + height,
                z + radius
        );

        FloatEntityData bbH = new FloatEntityData(DATA_BOUNDING_BOX_HEIGHT, entityHeight);
        FloatEntityData bbW = new FloatEntityData(DATA_BOUNDING_BOX_WIDTH, this.getWidth());
        this.dataProperties.put(bbH);
        this.dataProperties.put(bbW);
        if (send) {
            sendData(this.hasSpawned.values().toArray(Player.EMPTY_ARRAY), new EntityMetadata().put(bbH).put(bbW));
        }
    }

    protected void recalculateEffectColor() {
        int[] color = new int[3];
        int count = 0;
        boolean ambient = true;
        for (Effect effect : this.effects.values()) {
            if (effect.isVisible()) {
                int[] c = effect.getColor();
                color[0] += c[0] * (effect.getAmplifier() + 1);
                color[1] += c[1] * (effect.getAmplifier() + 1);
                color[2] += c[2] * (effect.getAmplifier() + 1);
                count += effect.getAmplifier() + 1;
                if (!effect.isAmbient()) {
                    ambient = false;
                }
            }
        }

        if (count > 0) {
            int r = (color[0] / count) & 0xff;
            int g = (color[1] / count) & 0xff;
            int b = (color[2] / count) & 0xff;

            this.setDataProperty(new IntEntityData(Entity.DATA_POTION_COLOR, (r << 16) + (g << 8) + b));
            this.setDataProperty(new ByteEntityData(Entity.DATA_POTION_AMBIENT, ambient ? 1 : 0));
        } else {
            this.setDataProperty(new IntEntityData(Entity.DATA_POTION_COLOR, 0));
            this.setDataProperty(new ByteEntityData(Entity.DATA_POTION_AMBIENT, 0));
        }
    }

    @Nullable
    public static Entity createEntity(@Nonnull String name, @Nonnull Position pos, @Nullable Object... args) {
        return createEntity(name, pos.getChunk(), getDefaultNBT(pos), args);
    }

    @Nullable
    public static Entity createEntity(int type, @Nonnull Position pos, @Nullable Object... args) {
        return createEntity(String.valueOf(type), pos.getChunk(), getDefaultNBT(pos), args);
    }

    @Nullable
    public static Entity createEntity(@Nonnull String name, @Nonnull FullChunk chunk, @Nonnull CompoundTag nbt, @Nullable Object... args) {
        Entity entity = null;

        Class<? extends Entity> clazz = knownEntities.get(name);
        if (clazz != null) {
            List<Exception> exceptions = null;

            for (Constructor constructor : clazz.getConstructors()) {
                if (entity != null) {
                    break;
                }

                if (constructor.getParameterCount() != (args == null ? 2 : args.length + 2)) {
                    continue;
                }

                try {
                    if (args == null || args.length == 0) {
                        entity = (Entity) constructor.newInstance(chunk, nbt);
                    } else {
                        Object[] objects = new Object[args.length + 2];

                        objects[0] = chunk;
                        objects[1] = nbt;
                        System.arraycopy(args, 0, objects, 2, args.length);
                        entity = (Entity) constructor.newInstance(objects);

                    }
                } catch (Exception e) {
                    if (exceptions == null) {
                        exceptions = new ArrayList<>();
                    }
                    exceptions.add(e);
                }

            }

            if (entity == null) {
                Exception cause = new IllegalArgumentException("Could not create an entity of type "+name, exceptions != null && exceptions.size() > 0? exceptions.get(0) : null);
                if (exceptions != null && exceptions.size() > 1) {
                    for (int i = 1; i < exceptions.size(); i++) {
                        cause.addSuppressed(exceptions.get(i));
                    }
                }
                log.debug("Could not create an entity of type {} with {} args", name, args == null? 0 : args.length, cause);
            }
        } else {
            log.debug("Entity type {} is unknown", name);
        }

        return entity;
    }

    @Nullable
    public static Entity createEntity(int type, @Nonnull FullChunk chunk, @Nonnull CompoundTag nbt, @Nullable Object... args) {
        return createEntity(String.valueOf(type), chunk, nbt, args);
    }

    public static boolean registerEntity(String name, Class<? extends Entity> clazz) {
        return registerEntity(name, clazz, false);
    }

    public static boolean registerEntity(String name, Class<? extends Entity> clazz, boolean force) {
        if (clazz == null) {
            return false;
        }
        try {
            int networkId = clazz.getField("NETWORK_ID").getInt(null);
            knownEntities.put(String.valueOf(networkId), clazz);
        } catch (Exception e) {
            if (!force) {
                return false;
            }
        }

        knownEntities.put(name, clazz);
        shortNames.put(clazz.getSimpleName(), name);
        return true;
    }

    @Nonnull
    @PowerNukkitOnly
    @Since("1.5.1.0-PN")
    public static IntCollection getKnownEntityIds() {
        return knownEntities.keySet().stream()
                .filter(Utils::isInteger)
                .mapToInt(Integer::parseInt)
                .collect(IntArrayList::new, IntArrayList::add, IntArrayList::addAll);
    }

    @Nonnull
    @PowerNukkitOnly
    @Since("1.5.1.0-PN")
    public static List<String> getSaveIds() {
        return new ArrayList<>(shortNames.values());
    }

    @Nonnull
    @PowerNukkitOnly
    @Since("1.5.1.0-PN")
    public static OptionalInt getSaveId(String id) {
        Class<? extends Entity> entityClass = knownEntities.get(id);
        if (entityClass == null) {
            return OptionalInt.empty();
        }
        return knownEntities.entrySet().stream()
                .filter(entry -> entry.getValue().equals(entityClass))
                .map(Map.Entry::getKey)
                .filter(Utils::isInteger)
                .mapToInt(Integer::parseInt)
                .findFirst();
    }

    @Nullable
    @PowerNukkitOnly
    @Since("1.5.1.0-PN")
    public static String getSaveId(int id) {
        Class<? extends Entity> entityClass = knownEntities.get(Integer.toString(id));
        if (entityClass == null) {
            return null;
        }
        return shortNames.get(entityClass.getSimpleName());
    }

    @Nonnull
    public static CompoundTag getDefaultNBT(@Nonnull Vector3 pos) {
        return getDefaultNBT(pos, null);
    }

    @Nonnull
    public static CompoundTag getDefaultNBT(@Nonnull Vector3 pos, @Nullable Vector3 motion) {
        Location loc = pos instanceof Location ? (Location) pos : null;

        if (loc != null) {
            return getDefaultNBT(pos, motion, (float) loc.getYaw(), (float) loc.getPitch());
        }

        return getDefaultNBT(pos, motion, 0, 0);
    }

    @Nonnull
    public static CompoundTag getDefaultNBT(@Nonnull Vector3 pos, @Nullable Vector3 motion, float yaw, float pitch) {
        return new CompoundTag()
                .putList(new ListTag<DoubleTag>("Pos")
                        .add(new DoubleTag("", pos.x))
                        .add(new DoubleTag("", pos.y))
                        .add(new DoubleTag("", pos.z)))
                .putList(new ListTag<DoubleTag>("Motion")
                        .add(new DoubleTag("", motion != null ? motion.x : 0))
                        .add(new DoubleTag("", motion != null ? motion.y : 0))
                        .add(new DoubleTag("", motion != null ? motion.z : 0)))
                .putList(new ListTag<FloatTag>("Rotation")
                        .add(new FloatTag("", yaw))
                        .add(new FloatTag("", pitch)));
    }

    public void saveNBT() {
        if (!(this instanceof Player)) {
            this.namedTag.putString("id", this.getSaveId());
            if (!this.getNameTag().equals("")) {
                this.namedTag.putString("CustomName", this.getNameTag());
                this.namedTag.putBoolean("CustomNameVisible", this.isNameTagVisible());
                this.namedTag.putBoolean("CustomNameAlwaysVisible", this.isNameTagAlwaysVisible());
            } else {
                this.namedTag.remove("CustomName");
                this.namedTag.remove("CustomNameVisible");
                this.namedTag.remove("CustomNameAlwaysVisible");
            }
        }

        this.namedTag.putList(new ListTag<DoubleTag>("Pos")
                .add(new DoubleTag("0", this.x))
                .add(new DoubleTag("1", this.y))
                .add(new DoubleTag("2", this.z))
        );

        this.namedTag.putList(new ListTag<DoubleTag>("Motion")
                .add(new DoubleTag("0", this.motionX))
                .add(new DoubleTag("1", this.motionY))
                .add(new DoubleTag("2", this.motionZ))
        );

        this.namedTag.putList(new ListTag<FloatTag>("Rotation")
                .add(new FloatTag("0", (float) this.yaw))
                .add(new FloatTag("1", (float) this.pitch))
        );

        this.namedTag.putFloat("FallDistance", this.fallDistance);
        this.namedTag.putShort("Fire", this.fireTicks);
        this.namedTag.putShort("Air", this.getDataPropertyShort(DATA_AIR));
        this.namedTag.putBoolean("OnGround", this.onGround);
        this.namedTag.putBoolean("Invulnerable", this.invulnerable);
        this.namedTag.putFloat("Scale", this.scale);

        if (!this.effects.isEmpty()) {
            ListTag<CompoundTag> list = new ListTag<>("ActiveEffects");
            for (Effect effect : this.effects.values()) {
                list.add(new CompoundTag(String.valueOf(effect.getId()))
                        .putByte("Id", effect.getId())
                        .putByte("Amplifier", effect.getAmplifier())
                        .putInt("Duration", effect.getDuration())
                        .putBoolean("Ambient", false)
                        .putBoolean("ShowParticles", effect.isVisible())
                );
            }

            this.namedTag.putList(list);
        } else {
            this.namedTag.remove("ActiveEffects");
        }
    }

    /**
     * The name that English name of the type of this entity.
     */
    @PowerNukkitOnly
    @Since("1.5.1.0-PN")
    public String getOriginalName() {
        return this.getSaveId();
    }

    /**
     * Similar to {@link #getName()}, but if the name is blank or empty it returns the static name instead.
     */
    @PowerNukkitOnly
    @Since("1.5.1.0-PN")
    public final String getVisibleName() {
        String name = getName();
        if (!TextFormat.clean(name).trim().isEmpty()) {
            return name;
        } else {
            return getOriginalName();
        }
    }

    /**
     * The current name used by this entity in the name tag, or the static name if the entity don't have nametag.
     */
    @Nonnull
    public String getName() {
        if (this.hasCustomName()) {
            return this.getNameTag();
        } else {
            return this.getOriginalName();
        }
    }

    public final String getSaveId() {
        return shortNames.getOrDefault(this.getClass().getSimpleName(), "");
    }

    public void spawnTo(Player player) {

        if (!this.hasSpawned.containsKey(player.getLoaderId()) && this.chunk != null && player.usedChunks.containsKey(Level.chunkHash(this.chunk.getX(), this.chunk.getZ()))) {
            this.hasSpawned.put(player.getLoaderId(), player);
            player.dataPacket(createAddEntityPacket());
        }

        if (this.riding != null) {
            this.riding.spawnTo(player);

            SetEntityLinkPacket pkk = new SetEntityLinkPacket();
            pkk.vehicleUniqueId = this.riding.getId();
            pkk.riderUniqueId = this.getId();
            pkk.type = 1;
            pkk.immediate = 1;

            player.dataPacket(pkk);
        }
    }

    protected DataPacket createAddEntityPacket() {
        AddEntityPacket addEntity = new AddEntityPacket();
        addEntity.type = this.getNetworkId();
        addEntity.entityUniqueId = this.getId();
        addEntity.entityRuntimeId = this.getId();
        addEntity.yaw = (float) this.yaw;
        addEntity.headYaw = (float) this.yaw;
        addEntity.pitch = (float) this.pitch;
        addEntity.x = (float) this.x;
        addEntity.y = (float) this.y;
        addEntity.z = (float) this.z;
        addEntity.speedX = (float) this.motionX;
        addEntity.speedY = (float) this.motionY;
        addEntity.speedZ = (float) this.motionZ;
        addEntity.metadata = this.dataProperties;

        addEntity.links = new EntityLink[this.passengers.size()];
        for (int i = 0; i < addEntity.links.length; i++) {
            addEntity.links[i] = new EntityLink(this.getId(), this.passengers.get(i).getId(), i == 0 ? EntityLink.TYPE_RIDER : TYPE_PASSENGER, false, false);
        }

        return addEntity;
    }

    public Map<Integer, Player> getViewers() {
        return hasSpawned;
    }

    public void sendPotionEffects(Player player) {
        for (Effect effect : this.effects.values()) {
            MobEffectPacket pk = new MobEffectPacket();
            pk.eid = this.getId();
            pk.effectId = effect.getId();
            pk.amplifier = effect.getAmplifier();
            pk.particles = effect.isVisible();
            pk.duration = effect.getDuration();
            pk.eventId = MobEffectPacket.EVENT_ADD;

            player.dataPacket(pk);
        }
    }

    public void sendData(Player player) {
        this.sendData(player, null);
    }

    public void sendData(Player player, EntityMetadata data) {
        SetEntityDataPacket pk = new SetEntityDataPacket();
        pk.eid = this.getId();
        pk.metadata = data == null ? this.dataProperties : data;

        player.dataPacket(pk);
    }

    public void sendData(Player[] players) {
        this.sendData(players, null);
    }

    public void sendData(Player[] players, EntityMetadata data) {
        SetEntityDataPacket pk = new SetEntityDataPacket();
        pk.eid = this.getId();
        pk.metadata = data == null ? this.dataProperties : data;

        for (Player player : players) {
            if (player == this) {
                continue;
            }
            player.dataPacket(pk.clone());
        }
        if (this instanceof Player) {
            ((Player) this).dataPacket(pk);
        }
    }

    public void despawnFrom(Player player) {
        if (this.hasSpawned.containsKey(player.getLoaderId())) {
            RemoveEntityPacket pk = new RemoveEntityPacket();
            pk.eid = this.getId();
            player.dataPacket(pk);
            this.hasSpawned.remove(player.getLoaderId());
        }
    }

    public boolean attack(EntityDamageEvent source) {
        if (hasEffect(Effect.FIRE_RESISTANCE)
                && (source.getCause() == DamageCause.FIRE
                || source.getCause() == DamageCause.FIRE_TICK
                || source.getCause() == DamageCause.LAVA)) {
            return false;
        }

        getServer().getPluginManager().callEvent(source);
        if (source.isCancelled()) {
            return false;
        }

        // Make fire aspect to set the target in fire before dealing any damage so the target is in fire on death even if killed by the first hit
        if (source instanceof EntityDamageByEntityEvent) {
            Enchantment[] enchantments = ((EntityDamageByEntityEvent) source).getWeaponEnchantments();
            if (enchantments != null) {
                for (Enchantment enchantment : enchantments) {
                    enchantment.doAttack(((EntityDamageByEntityEvent) source).getDamager(), this);
                }
            }
        }

        if (this.absorption > 0) {  // Damage Absorption
            this.setAbsorption(Math.max(0, this.getAbsorption() + source.getDamage(EntityDamageEvent.DamageModifier.ABSORPTION)));
        }
        setLastDamageCause(source);

        float newHealth = getHealth() - source.getFinalDamage();
        if (newHealth < 1 && this instanceof Player) {
            if (source.getCause() != DamageCause.VOID && source.getCause() != DamageCause.SUICIDE) {
                Player p = (Player) this;
                boolean totem = false;
                if (p.getOffhandInventory().getItem(0).getId() == ItemID.TOTEM) {
                    p.getOffhandInventory().clear(0);
                    totem = true;
                } else if (p.getInventory().getItemInHand().getId() == ItemID.TOTEM) {
                    p.getInventory().clear(p.getInventory().getHeldItemIndex());
                    totem = true;
                }
                if (totem) {
                    this.getLevel().addLevelEvent(this, LevelEventPacket.EVENT_SOUND_TOTEM);
                    this.getLevel().addParticleEffect(this, ParticleEffect.TOTEM);

                    this.extinguish();
                    this.removeAllEffects();
                    this.setHealth(1);

                    this.addEffect(Effect.getEffect(Effect.REGENERATION).setDuration(800).setAmplifier(1));
                    this.addEffect(Effect.getEffect(Effect.FIRE_RESISTANCE).setDuration(800));
                    this.addEffect(Effect.getEffect(Effect.ABSORPTION).setDuration(100).setAmplifier(1));

                    EntityEventPacket pk = new EntityEventPacket();
                    pk.eid = this.getId();
                    pk.event = EntityEventPacket.CONSUME_TOTEM;
                    p.dataPacket(pk);

                    source.setCancelled(true);
                    return false;
                }
            }
        }
        Entity attacker = source instanceof EntityDamageByEntityEvent? ((EntityDamageByEntityEvent) source).getDamager() : null;
        for (SideEffect sideEffect : source.getSideEffects()) {
            sideEffect.doPreHealthChange(this, source, attacker);
        }
        setHealth(newHealth);
        return true;
    }

    public boolean attack(float damage) {
        return this.attack(new EntityDamageEvent(this, DamageCause.CUSTOM, damage));
    }

    public void heal(EntityRegainHealthEvent source) {
        this.server.getPluginManager().callEvent(source);
        if (source.isCancelled()) {
            return;
        }
        this.setHealth(this.getHealth() + source.getAmount());
    }

    public void heal(float amount) {
        this.heal(new EntityRegainHealthEvent(this, amount, EntityRegainHealthEvent.CAUSE_REGEN));
    }

    public float getHealth() {
        return health;
    }

    public boolean isAlive() {
        return this.health > 0;
    }

    public boolean isClosed() {
        return closed;
    }

    public void setHealth(float health) {
        if (this.health == health) {
            return;
        }

        if (health < 1) {
            if (this.isAlive()) {
                this.kill();
            }
        } else if (health <= this.getMaxHealth() || health < this.health) {
            this.health = health;
        } else {
            this.health = this.getMaxHealth();
        }

        setDataProperty(new IntEntityData(DATA_HEALTH, (int) this.health));
    }

    public void setLastDamageCause(EntityDamageEvent type) {
        this.lastDamageCause = type;
    }

    public EntityDamageEvent getLastDamageCause() {
        return lastDamageCause;
    }

    public int getMaxHealth() {
        return maxHealth + (this.hasEffect(Effect.HEALTH_BOOST) ? 4 * (this.getEffect(Effect.HEALTH_BOOST).getAmplifier() + 1) : 0);
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    public boolean canCollideWith(Entity entity) {
        return !this.justCreated && this != entity;
    }

    protected boolean checkObstruction(double x, double y, double z) {
        if (this.level.getCollisionCubes(this, this.getBoundingBox(), false).length == 0 || this.noClip) {
            return false;
        }

        int i = NukkitMath.floorDouble(x);
        int j = NukkitMath.floorDouble(y);
        int k = NukkitMath.floorDouble(z);

        double diffX = x - i;
        double diffY = y - j;
        double diffZ = z - k;

        if (!Block.isTransparent(this.level.getBlockIdAt(i, j, k))) {
            boolean flag = Block.isTransparent(this.level.getBlockIdAt(i - 1, j, k));
            boolean flag1 = Block.isTransparent(this.level.getBlockIdAt(i + 1, j, k));
            boolean flag2 = Block.isTransparent(this.level.getBlockIdAt(i, j - 1, k));
            boolean flag3 = Block.isTransparent(this.level.getBlockIdAt(i, j + 1, k));
            boolean flag4 = Block.isTransparent(this.level.getBlockIdAt(i, j, k - 1));
            boolean flag5 = Block.isTransparent(this.level.getBlockIdAt(i, j, k + 1));

            int direction = -1;
            double limit = 9999;

            if (flag) {
                limit = diffX;
                direction = 0;
            }

            if (flag1 && 1 - diffX < limit) {
                limit = 1 - diffX;
                direction = 1;
            }

            if (flag2 && diffY < limit) {
                limit = diffY;
                direction = 2;
            }

            if (flag3 && 1 - diffY < limit) {
                limit = 1 - diffY;
                direction = 3;
            }

            if (flag4 && diffZ < limit) {
                limit = diffZ;
                direction = 4;
            }

            if (flag5 && 1 - diffZ < limit) {
                direction = 5;
            }

            double force = ThreadLocalRandom.current().nextDouble() * 0.2 + 0.1;

            if (direction == 0) {
                this.motionX = -force;

                return true;
            }

            if (direction == 1) {
                this.motionX = force;

                return true;
            }

            if (direction == 2) {
                this.motionY = -force;

                return true;
            }

            if (direction == 3) {
                this.motionY = force;

                return true;
            }

            if (direction == 4) {
                this.motionZ = -force;

                return true;
            }

            if (direction == 5) {
                this.motionZ = force;

                return true;
            }
        }

        return false;
    }

    public boolean entityBaseTick() {
        return this.entityBaseTick(1);
    }

    public boolean entityBaseTick(int tickDiff) {
        Timings.entityBaseTickTimer.startTiming();

        if (!this.isPlayer) {
            this.blocksAround = null;
            this.collisionBlocks = null;
        }
        this.justCreated = false;

        if (!this.isAlive()) {
            this.removeAllEffects();
            this.despawnFromAll();
            if (!this.isPlayer) {
                this.close();
            }
            Timings.entityBaseTickTimer.stopTiming();
            return false;
        }
        if (riding != null && !riding.isAlive() && riding instanceof EntityRideable) {
            ((EntityRideable) riding).mountEntity(this);
        }

        updatePassengers();

        if (!this.effects.isEmpty()) {
            for (Effect effect : this.effects.values()) {
                if (effect.canTick()) {
                    effect.applyEffect(this);
                }
                effect.setDuration(effect.getDuration() - tickDiff);

                if (effect.getDuration() <= 0) {
                    this.removeEffect(effect.getId());
                }
            }
        }

        boolean hasUpdate = false;

        this.checkBlockCollision();

        if (this.y <= -16 && this.isAlive()) {
            if (this instanceof Player) {
                Player player = (Player) this;
                if (!player.isCreative()) this.attack(new EntityDamageEvent(this, DamageCause.VOID, 10));
            } else {
                this.attack(new EntityDamageEvent(this, DamageCause.VOID, 10));
                hasUpdate = true;
            }
        }

        if (this.fireTicks > 0) {
            if (this.fireProof) {
                this.fireTicks -= 4 * tickDiff;
                if (this.fireTicks < 0) {
                    this.fireTicks = 0;
                }
            } else {
                if (!this.hasEffect(Effect.FIRE_RESISTANCE) && ((this.fireTicks % 20) == 0 || tickDiff > 20)) {
                    this.attack(new EntityDamageEvent(this, DamageCause.FIRE_TICK, 1));
                }
                this.fireTicks -= tickDiff;
            }
            if (this.fireTicks <= 0) {
                this.extinguish();
            } else if (!this.fireProof && (!(this instanceof Player) || !((Player) this).isSpectator())) {
                this.setDataFlag(DATA_FLAGS, DATA_FLAG_ONFIRE, true);
                hasUpdate = true;
            }
        }

        if (this.noDamageTicks > 0) {
            this.noDamageTicks -= tickDiff;
            if (this.noDamageTicks < 0) {
                this.noDamageTicks = 0;
            }
        }

        if (this.inPortalTicks == 80) {
            EntityPortalEnterEvent ev = new EntityPortalEnterEvent(this, PortalType.NETHER);
            getServer().getPluginManager().callEvent(ev);

            if (!ev.isCancelled() && (level == EnumLevel.OVERWORLD.getLevel() || level == EnumLevel.NETHER.getLevel())) {
                Position newPos = EnumLevel.moveToNether(this);
                if (newPos != null) {
                    /*for (int x = -1; x < 2; x++) {
                        for (int z = -1; z < 2; z++) {
                            int chunkX = (newPos.getFloorX() >> 4) + x, chunkZ = (newPos.getFloorZ() >> 4) + z;
                            FullChunk chunk = newPos.level.getChunk(chunkX, chunkZ, false);
                            if (chunk == null || !(chunk.isGenerated() || chunk.isPopulated())) {
                                newPos.level.generateChunk(chunkX, chunkZ, true);
                            }
                        }
                    }*/
                    Position nearestPortal = getNearestValidPortal(newPos);
                    if (nearestPortal != null) {
                        teleport(nearestPortal.add(0.5, 0, 0.5), PlayerTeleportEvent.TeleportCause.NETHER_PORTAL);
                    } else {
                        final Position finalPos = newPos.add(1.5, 1, 1.5);
                        if (teleport(finalPos, PlayerTeleportEvent.TeleportCause.NETHER_PORTAL)) {
                            server.getScheduler().scheduleDelayedTask(new Task() {
                                @Override
                                public void onRun(int currentTick) {
                                    // dirty hack to make sure chunks are loaded and generated before spawning
                                    // player
                                    inPortalTicks = 81;
                                    teleport(finalPos, PlayerTeleportEvent.TeleportCause.NETHER_PORTAL);
                                    BlockNetherPortal.spawnPortal(newPos);
                                }
                            }, 5);
                        }
                    }
                }
            }
        }

        this.age += tickDiff;
        this.ticksLived += tickDiff;
        TimingsHistory.activatedEntityTicks++;

        Timings.entityBaseTickTimer.stopTiming();
        return hasUpdate;
    }

    private Position getNearestValidPortal(Position currentPos) {
        AxisAlignedBB axisAlignedBB = new SimpleAxisAlignedBB(
                new Vector3(currentPos.getFloorX() - 128.0, 1.0, currentPos.getFloorZ() - 128.0),
                new Vector3(currentPos.getFloorX() + 128.0, currentPos.level.getDimension() == Level.DIMENSION_NETHER? 128 : 256, currentPos.getFloorZ() + 128.0));
        BiPredicate<BlockVector3, BlockState> condition = (pos, state) -> state.getBlockId() == BlockID.NETHER_PORTAL;
        List<Block> blocks = currentPos.level.scanBlocks(axisAlignedBB, condition);

        if (blocks.isEmpty()) {
            return null;
        }

        final Vector2 currentPosV2 = new Vector2(currentPos.getFloorX(), currentPos.getFloorZ());
        final double by = currentPos.getFloorY();
        Comparator<Block> euclideanDistance = Comparator.comparingDouble(block -> currentPosV2.distanceSquared(block.getFloorX(), block.getFloorZ()));
        Comparator<Block> heightDistance = Comparator.comparingDouble(block-> {
            double ey = by - block.y;
            return ey * ey;
        });

        Block nearestPortal = blocks.stream()
                .filter(block-> block.down().getId() != BlockID.NETHER_PORTAL)
                .min(euclideanDistance.thenComparing(heightDistance))
                .orElse(null);

        if (nearestPortal == null) {
            return null;
        }

        return nearestPortal;
    }

    public void updateMovement() {
        double diffPosition = (this.x - this.lastX) * (this.x - this.lastX) + (this.y - this.lastY) * (this.y - this.lastY) + (this.z - this.lastZ) * (this.z - this.lastZ);
        double diffRotation = (this.yaw - this.lastYaw) * (this.yaw - this.lastYaw) + (this.pitch - this.lastPitch) * (this.pitch - this.lastPitch);

        double diffMotion = (this.motionX - this.lastMotionX) * (this.motionX - this.lastMotionX) + (this.motionY - this.lastMotionY) * (this.motionY - this.lastMotionY) + (this.motionZ - this.lastMotionZ) * (this.motionZ - this.lastMotionZ);

        if (diffPosition > 0.0001 || diffRotation > 1.0) { //0.2 ** 2, 1.5 ** 2
            this.lastX = this.x;
            this.lastY = this.y;
            this.lastZ = this.z;

            this.lastPitch = this.pitch;
            this.lastYaw = this.yaw;
            this.lastHeadYaw = this.headYaw;

            // If you want to achieve headYaw in movement. You can override it by yourself. Changing would break some mob plugins.
            this.addMovement(this.x, this.y + this.getBaseOffset(), this.z, this.yaw, this.pitch, this.yaw);
            this.positionChanged = true;
        } else {
            this.positionChanged = false;
        }

        if (diffMotion > 0.0025 || (diffMotion > 0.0001 && this.getMotion().lengthSquared() <= 0.0001)) { //0.05 ** 2
            this.lastMotionX = this.motionX;
            this.lastMotionY = this.motionY;
            this.lastMotionZ = this.motionZ;

            this.addMotion(this.motionX, this.motionY, this.motionZ);
        }
    }

    public void addMovement(double x, double y, double z, double yaw, double pitch, double headYaw) {
        this.level.addEntityMovement(this, x, y, z, yaw, pitch, headYaw);
    }

    public void addMotion(double motionX, double motionY, double motionZ) {
        SetEntityMotionPacket pk = new SetEntityMotionPacket();
        pk.eid = this.id;
        pk.motionX = (float) motionX;
        pk.motionY = (float) motionY;
        pk.motionZ = (float) motionZ;

        Server.broadcastPacket(this.hasSpawned.values(), pk);
    }

    @Override
    public Vector3 getDirectionVector() {
        Vector3 vector = super.getDirectionVector();
        return this.temporalVector.setComponents(vector.x, vector.y, vector.z);
    }

    public Vector2 getDirectionPlane() {
        return (new Vector2((float) (-Math.cos(Math.toRadians(this.yaw) - Math.PI / 2)), (float) (-Math.sin(Math.toRadians(this.yaw) - Math.PI / 2)))).normalize();
    }

    public BlockFace getHorizontalFacing() {
        return BlockFace.fromHorizontalIndex(NukkitMath.floorDouble((this.yaw * 4.0F / 360.0F) + 0.5D) & 3);
    }

    public boolean onUpdate(int currentTick) {
        if (this.closed) {
            return false;
        }

        if (!this.isAlive()) {
            ++this.deadTicks;
            if (this.deadTicks >= 10) {
                this.despawnFromAll();
                if (!this.isPlayer) {
                    this.close();
                }
            }
            return this.deadTicks < 10;
        }

        int tickDiff = currentTick - this.lastUpdate;

        if (tickDiff <= 0) {
            return false;
        }

        this.lastUpdate = currentTick;

        boolean hasUpdate = this.entityBaseTick(tickDiff);

        this.updateMovement();

        return hasUpdate;
    }

    public boolean mountEntity(Entity entity) {
        return mountEntity(entity, TYPE_RIDE);
    }

    /**
     * Mount or Dismounts an Entity from a/into vehicle
     *
     * @param entity The target Entity
     * @return {@code true} if the mounting successful
     */
    public boolean mountEntity(Entity entity, byte mode) {
        Objects.requireNonNull(entity, "The target of the mounting entity can't be null");

        if (entity.riding != null) {
            dismountEntity(entity);
        } else {
            if (isPassenger(entity)) {
                return false;
            }

            // Entity entering a vehicle
            EntityVehicleEnterEvent ev = new EntityVehicleEnterEvent(entity, this);
            server.getPluginManager().callEvent(ev);
            if (ev.isCancelled()) {
                return false;
            }

            broadcastLinkPacket(entity, mode);

            // Add variables to entity
            entity.riding = this;
            entity.setDataFlag(DATA_FLAGS, DATA_FLAG_RIDING, true);
            passengers.add(entity);

            entity.setSeatPosition(getMountedOffset(entity));
            updatePassengerPosition(entity);
        }
        return true;
    }

    public boolean dismountEntity(Entity entity) {
        // Run the events
        EntityVehicleExitEvent ev = new EntityVehicleExitEvent(entity, this);
        server.getPluginManager().callEvent(ev);
        if (ev.isCancelled()) {
            return false;
        }

        broadcastLinkPacket(entity, TYPE_REMOVE);

        // Refurbish the entity
        entity.riding = null;
        entity.setDataFlag(DATA_FLAGS, DATA_FLAG_RIDING, false);
        passengers.remove(entity);

        entity.setSeatPosition(new Vector3f());
        updatePassengerPosition(entity);

        return true;
    }

    protected void broadcastLinkPacket(Entity rider, byte type) {
        SetEntityLinkPacket pk = new SetEntityLinkPacket();
        pk.vehicleUniqueId = getId();         // To the?
        pk.riderUniqueId = rider.getId(); // From who?
        pk.type = type;

        Server.broadcastPacket(this.hasSpawned.values(), pk);
    }

    public void updatePassengers() {
        if (this.passengers.isEmpty()) {
            return;
        }

        for (Entity passenger : new ArrayList<>(this.passengers)) {
            if (!passenger.isAlive()) {
                dismountEntity(passenger);
                continue;
            }

            updatePassengerPosition(passenger);
        }
    }

    protected void updatePassengerPosition(Entity passenger) {
        passenger.setPosition(this.add(passenger.getSeatPosition().asVector3()));
    }

    public void setSeatPosition(Vector3f pos) {
        this.setDataProperty(new Vector3fEntityData(DATA_RIDER_SEAT_POSITION, pos));
    }

    public Vector3f getSeatPosition() {
        return this.getDataPropertyVector3f(DATA_RIDER_SEAT_POSITION);
    }

    public Vector3f getMountedOffset(Entity entity) {
        return new Vector3f(0, getHeight() * 0.75f);
    }

    public final void scheduleUpdate() {
        this.level.updateEntities.put(this.id, this);
    }

    public boolean isOnFire() {
        return this.fireTicks > 0;
    }

    public void setOnFire(int seconds) {
        int ticks = seconds * 20;
        if (ticks > this.fireTicks) {
            this.fireTicks = ticks;
        }
    }

    public float getAbsorption() {
        return absorption;
    }

    public void setAbsorption(float absorption) {
        if (absorption != this.absorption) {
            this.absorption = absorption;
            if (this instanceof Player)
                ((Player) this).setAttribute(Attribute.getAttribute(Attribute.ABSORPTION).setValue(absorption));
        }
    }

    @PowerNukkitOnly
    public boolean canBePushed() {
        return true;
    }

    public BlockFace getDirection() {
        double rotation = this.yaw % 360;
        if (rotation < 0) {
            rotation += 360.0;
        }
        if ((0 <= rotation && rotation < 45) || (315 <= rotation && rotation < 360)) {
            return BlockFace.SOUTH;
        } else if (45 <= rotation && rotation < 135) {
            return BlockFace.WEST;
        } else if (135 <= rotation && rotation < 225) {
            return BlockFace.NORTH;
        } else if (225 <= rotation && rotation < 315) {
            return BlockFace.EAST;
        } else {
            return null;
        }
    }

    public void extinguish() {
        this.fireTicks = 0;
        this.setDataFlag(DATA_FLAGS, DATA_FLAG_ONFIRE, false);
    }

    public boolean canTriggerWalking() {
        return true;
    }

    public void resetFallDistance() {
        this.highestPosition = 0;
    }

    protected void updateFallState(boolean onGround) {
        if (onGround) {
            fallDistance = (float) (this.highestPosition - this.y);

            if (fallDistance > 0) {
                // check if we fell into at least 1 block of water
                if (this instanceof EntityLiving && !(this.getLevelBlock() instanceof BlockWater)) {
                    this.fall(fallDistance);
                }
                this.resetFallDistance();
            }
        }
    }

    public AxisAlignedBB getBoundingBox() {
        return this.boundingBox;
    }

    public void fall(float fallDistance) {
        if (this.hasEffect(Effect.SLOW_FALLING)) {
            return;
        }

        Location floorLocation = this.floor();
        Block down = this.level.getBlock(floorLocation.down());

        if (!this.isPlayer || level.getGameRules().getBoolean(GameRule.FALL_DAMAGE)) {
            int jumpBoost = this.hasEffect(Effect.JUMP_BOOST)? (getEffect(Effect.JUMP_BOOST).getAmplifier() + 1) : 0;
            float damage = (float) Math.floor(fallDistance - 3 - jumpBoost);

            if(down instanceof BlockHayBale || down instanceof BlockHoney) {
                damage *= 0.2F;
            }

            if (damage > 0) {
                this.attack(new EntityDamageEvent(this, DamageCause.FALL, damage));
            }
        }

        if (fallDistance > 0.75) {

            if (down.getId() == Block.FARMLAND) {
                if (onPhysicalInteraction(down, false)) {
                    return;
                }
                this.level.setBlock(down, new BlockDirt(), false, true);
                return;
            }

            Block floor = this.level.getBlock(floorLocation);

            if (floor instanceof BlockTurtleEgg) {
                if (onPhysicalInteraction(floor, ThreadLocalRandom.current().nextInt(10) >= 3)) {
                    return;
                }
                this.level.useBreakOn(this, null, null, true);
            }
        }
    }

    private boolean onPhysicalInteraction(Block block, boolean cancelled) {
        Event ev;

        if (this instanceof Player) {
            ev = new PlayerInteractEvent((Player) this, null, block, null, Action.PHYSICAL);
        } else {
            ev = new EntityInteractEvent(this, block);
        }

        ev.setCancelled(cancelled);

        this.server.getPluginManager().callEvent(ev);
        return ev.isCancelled();
    }

    public void handleLavaMovement() {
        //todo
    }

    public void moveFlying(float strafe, float forward, float friction) {
        // This is special for Nukkit! :)
        float speed = strafe * strafe + forward * forward;
        if (speed >= 1.0E-4F) {
            speed = MathHelper.sqrt(speed);
            if (speed < 1.0F) {
                speed = 1.0F;
            }
            speed = friction / speed;
            strafe *= speed;
            forward *= speed;
            float nest = MathHelper.sin((float) (this.yaw * 3.1415927F / 180.0F));
            float place = MathHelper.cos((float) (this.yaw * 3.1415927F / 180.0F));
            this.motionX += strafe * place - forward * nest;
            this.motionZ += forward * place + strafe * nest;
        }
    }

    public void onCollideWithPlayer(EntityHuman entityPlayer) {

    }

    public void applyEntityCollision(Entity entity) {
        if (entity.riding != this && !entity.passengers.contains(this)) {
            double dx = entity.x - this.x;
            double dy = entity.z - this.z;
            double dz = NukkitMath.getDirection(dx, dy);

            if (dz >= 0.009999999776482582D) {
                dz = MathHelper.sqrt((float) dz);
                dx /= dz;
                dy /= dz;
                double d3 = 1.0D / dz;

                if (d3 > 1.0D) {
                    d3 = 1.0D;
                }

                dx *= d3;
                dy *= d3;
                dx *= 0.05000000074505806;
                dy *= 0.05000000074505806;
                dx *= 1F + entityCollisionReduction;

                if (this.riding == null) {
                    motionX -= dx;
                    motionZ -= dy;
                }
            }
        }
    }

    public void onStruckByLightning(Entity entity) {
        if (this.attack(new EntityDamageByEntityEvent(entity, this, DamageCause.LIGHTNING, 5))) {
            if (this.fireTicks < 8 * 20) {
                this.setOnFire(8);
            }
        }
    }

    @PowerNukkitOnly
    public void onPushByPiston(BlockEntityPistonArm piston) {

    }

    public boolean onInteract(Player player, Item item, Vector3 clickedPos) {
        return onInteract(player, item);
    }

    public boolean onInteract(Player player, Item item) {
        return false;
    }

    protected boolean switchLevel(Level targetLevel) {
        if (this.closed) {
            return false;
        }

        if (this.isValid()) {
            EntityLevelChangeEvent ev = new EntityLevelChangeEvent(this, this.level, targetLevel);
            this.server.getPluginManager().callEvent(ev);
            if (ev.isCancelled()) {
                return false;
            }

            this.level.removeEntity(this);
            if (this.chunk != null) {
                this.chunk.removeEntity(this);
            }
            this.despawnFromAll();
        }

        this.setLevel(targetLevel);
        this.level.addEntity(this);
        this.chunk = null;

        return true;
    }

    public Position getPosition() {
        return new Position(this.x, this.y, this.z, this.level);
    }

    @Override
    @Nonnull
    public Location getLocation() {
        return new Location(this.x, this.y, this.z, this.yaw, this.pitch, this.headYaw, this.level);
    }

    @PowerNukkitOnly
    public boolean isTouchingWater() {
        return hasWaterAt(0) || hasWaterAt(this.getEyeHeight());
    }

    public boolean isInsideOfWater() {
        return hasWaterAt(this.getEyeHeight());
    }

    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    protected boolean hasWaterAt(float height) {
        double y = this.y + height;
        Block block = this.level.getBlock(this.temporalVector.setComponents(NukkitMath.floorDouble(this.x), NukkitMath.floorDouble(y), NukkitMath.floorDouble(this.z)));

        boolean layer1 = false;
        if (!(block instanceof BlockBubbleColumn) && (
                block instanceof BlockWater
                        || (layer1 = block.getLevelBlockAtLayer(1) instanceof BlockWater))) {
            BlockWater water = (BlockWater) (layer1? block.getLevelBlockAtLayer(1) : block);
            double f = (block.y + 1) - (water.getFluidHeightPercent() - 0.1111111);
            return y < f;
        }

        return false;
    }

    public boolean isInsideOfSolid() {
        double y = this.y + this.getEyeHeight();
        Block block = this.level.getBlock(
                this.temporalVector.setComponents(
                        NukkitMath.floorDouble(this.x),
                        NukkitMath.floorDouble(y),
                        NukkitMath.floorDouble(this.z))
        );

        AxisAlignedBB bb = block.getBoundingBox();

        return bb != null && block.isSolid() && !block.isTransparent() && bb.intersectsWith(this.getBoundingBox());

    }

    public boolean isInsideOfFire() {
        for (Block block : this.getCollisionBlocks()) {
            if (block instanceof BlockFire) {
                return true;
            }
        }

        return false;
    }

    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    public <T extends Block> boolean collideWithBlock(Class<T> classType){
        for(Block block : this.getCollisionBlocks()){
            if(classType.isInstance(block)){
                return true;
            }
        }
        return false;
    }

    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    public boolean isInsideOfLava() {
        for (Block block : this.getCollisionBlocks()) {
            if (block instanceof BlockLava) {
                return true;
            }
        }

        return false;
    }

    public boolean isOnLadder() {
        Block b = this.getLevelBlock();

        return b.getId() == Block.LADDER;
    }

    public boolean fastMove(double dx, double dy, double dz) {
        if (dx == 0 && dy == 0 && dz == 0) {
            return true;
        }

        Timings.entityMoveTimer.startTiming();

        AxisAlignedBB newBB = this.boundingBox.getOffsetBoundingBox(dx, dy, dz);

        if (server.getAllowFlight()
                || isPlayer && ((Player) this).getAdventureSettings().get(AdventureSettings.Type.NO_CLIP)
                || !this.level.hasCollision(this, newBB, false)) {
            this.boundingBox = newBB;
        }

        this.x = (this.boundingBox.getMinX() + this.boundingBox.getMaxX()) / 2;
        this.y = this.boundingBox.getMinY() - this.ySize;
        this.z = (this.boundingBox.getMinZ() + this.boundingBox.getMaxZ()) / 2;

        this.checkChunks();

        if ((!this.onGround || dy != 0) && !this.noClip) {
            AxisAlignedBB bb = this.boundingBox.clone();
            bb.setMinY(bb.getMinY() - 0.75);

            this.onGround = this.level.getCollisionBlocks(bb).length > 0;
        }
        this.isCollided = this.onGround;
        this.updateFallState(this.onGround);
        Timings.entityMoveTimer.stopTiming();
        return true;
    }

    public boolean move(double dx, double dy, double dz) {
        if (dx == 0 && dz == 0 && dy == 0) {
            return true;
        }

        if (this.keepMovement) {
            this.boundingBox.offset(dx, dy, dz);
            this.setPosition(this.temporalVector.setComponents((this.boundingBox.getMinX() + this.boundingBox.getMaxX()) / 2, this.boundingBox.getMinY(), (this.boundingBox.getMinZ() + this.boundingBox.getMaxZ()) / 2));
            this.onGround = this.isPlayer;
            return true;
        } else {

            Timings.entityMoveTimer.startTiming();

            this.ySize *= 0.4;

            double movX = dx;
            double movY = dy;
            double movZ = dz;

            AxisAlignedBB axisalignedbb = this.boundingBox.clone();

            AxisAlignedBB[] list = this.noClip ? AxisAlignedBB.EMPTY_ARRAY : this.level.getCollisionCubes(this, this.boundingBox.addCoord(dx, dy, dz), false);

            for (AxisAlignedBB bb : list) {
                dy = bb.calculateYOffset(this.boundingBox, dy);
            }

            this.boundingBox.offset(0, dy, 0);

            boolean fallingFlag = (this.onGround || (dy != movY && movY < 0));

            for (AxisAlignedBB bb : list) {
                dx = bb.calculateXOffset(this.boundingBox, dx);
            }

            this.boundingBox.offset(dx, 0, 0);

            for (AxisAlignedBB bb : list) {
                dz = bb.calculateZOffset(this.boundingBox, dz);
            }

            this.boundingBox.offset(0, 0, dz);

            if (this.getStepHeight() > 0 && fallingFlag && this.ySize < 0.05 && (movX != dx || movZ != dz)) {
                double cx = dx;
                double cy = dy;
                double cz = dz;
                dx = movX;
                dy = this.getStepHeight();
                dz = movZ;

                AxisAlignedBB axisalignedbb1 = this.boundingBox.clone();

                this.boundingBox.setBB(axisalignedbb);

                list = this.level.getCollisionCubes(this, this.boundingBox.addCoord(dx, dy, dz), false);

                for (AxisAlignedBB bb : list) {
                    dy = bb.calculateYOffset(this.boundingBox, dy);
                }

                this.boundingBox.offset(0, dy, 0);

                for (AxisAlignedBB bb : list) {
                    dx = bb.calculateXOffset(this.boundingBox, dx);
                }

                this.boundingBox.offset(dx, 0, 0);

                for (AxisAlignedBB bb : list) {
                    dz = bb.calculateZOffset(this.boundingBox, dz);
                }

                this.boundingBox.offset(0, 0, dz);

                this.boundingBox.offset(0, 0, dz);

                if ((cx * cx + cz * cz) >= (dx * dx + dz * dz)) {
                    dx = cx;
                    dy = cy;
                    dz = cz;
                    this.boundingBox.setBB(axisalignedbb1);
                } else {
                    this.ySize += 0.5;
                }

            }

            this.x = (this.boundingBox.getMinX() + this.boundingBox.getMaxX()) / 2;
            this.y = this.boundingBox.getMinY() - this.ySize;
            this.z = (this.boundingBox.getMinZ() + this.boundingBox.getMaxZ()) / 2;

            this.checkChunks();

            this.checkGroundState(movX, movY, movZ, dx, dy, dz);
            this.updateFallState(this.onGround);

            if (movX != dx) {
                this.motionX = 0;
            }

            if (movY != dy) {
                this.motionY = 0;
            }

            if (movZ != dz) {
                this.motionZ = 0;
            }

            //TODO: vehicle collision events (first we need to spawn them!)
            Timings.entityMoveTimer.stopTiming();
            return true;
        }
    }

    @PowerNukkitDifference(since = "1.4.0.0-PN", info = "Will do nothing if the entity is on ground and all args are 0")
    protected void checkGroundState(double movX, double movY, double movZ, double dx, double dy, double dz) {
        if (onGround && movX == 0 && movY == 0 && movZ == 0 && dx == 0 && dy == 0 && dz == 0) {
            return;
        }

        if (this.noClip) {
            this.isCollidedVertically = false;
            this.isCollidedHorizontally = false;
            this.isCollided = false;
            this.onGround = false;
        } else {
            this.isCollidedVertically = movY != dy;
            this.isCollidedHorizontally = (movX != dx || movZ != dz);
            this.isCollided = (this.isCollidedHorizontally || this.isCollidedVertically);
            this.onGround = (movY != dy && movY < 0);
        }
    }

    public List<Block> getBlocksAround() {
        if (this.blocksAround == null) {
            int minX = NukkitMath.floorDouble(this.boundingBox.getMinX());
            int minY = NukkitMath.floorDouble(this.boundingBox.getMinY());
            int minZ = NukkitMath.floorDouble(this.boundingBox.getMinZ());
            int maxX = NukkitMath.ceilDouble(this.boundingBox.getMaxX());
            int maxY = NukkitMath.ceilDouble(this.boundingBox.getMaxY());
            int maxZ = NukkitMath.ceilDouble(this.boundingBox.getMaxZ());

            this.blocksAround = new ArrayList<>();

            for (int z = minZ; z <= maxZ; ++z) {
                for (int x = minX; x <= maxX; ++x) {
                    for (int y = minY; y <= maxY; ++y) {
                        Block block = this.level.getBlock(this.temporalVector.setComponents(x, y, z));
                        this.blocksAround.add(block);
                    }
                }
            }
        }

        return this.blocksAround;
    }

    public List<Block> getCollisionBlocks() {
        if (this.collisionBlocks == null) {
            this.collisionBlocks = new ArrayList<>();

            for (Block b : getBlocksAround()) {
                if (b.collidesWithBB(this.getBoundingBox(), true)) {
                    this.collisionBlocks.add(b);
                }
            }
        }

        return this.collisionBlocks;
    }

    /**
     * Returns whether this entity can be moved by currents in liquids.
     *
     * @return boolean
     */
    public boolean canBeMovedByCurrents() {
        return true;
    }

    protected void checkBlockCollision() {
        if (this.noClip) {
            return;
        }

        Vector3 vector = new Vector3(0, 0, 0);
        boolean portal = false;
        boolean scaffolding = false;
        boolean endPortal = false;
        for (Block block : this.getCollisionBlocks()) {
            switch (block.getId()) {
                case Block.NETHER_PORTAL:
                    portal = true;
                    break;
                case BlockID.SCAFFOLDING:
                    scaffolding = true;
                    break;
                case BlockID.END_PORTAL:
                    endPortal = true;
                    break;
            }

            block.onEntityCollide(this);
            block.getLevelBlockAtLayer(1).onEntityCollide(this);
            block.addVelocityToEntity(this, vector);
        }

        setDataFlag(DATA_FLAGS_EXTENDED, DATA_FLAG_IN_SCAFFOLDING, scaffolding);

        AxisAlignedBB scanBoundingBox = boundingBox.getOffsetBoundingBox(0, -0.125, 0);
        scanBoundingBox.setMaxY(boundingBox.getMinY());
        Block[] scaffoldingUnder = level.getCollisionBlocks(
                scanBoundingBox,
                true, true,
                b-> b.getId() == BlockID.SCAFFOLDING
        );
        setDataFlag(DATA_FLAGS_EXTENDED, DATA_FLAG_OVER_SCAFFOLDING, scaffoldingUnder.length > 0);

        if (endPortal) {
            if (!inEndPortal) {
                inEndPortal = true;
                EntityPortalEnterEvent ev = new EntityPortalEnterEvent(this, PortalType.END);
                getServer().getPluginManager().callEvent(ev);
            }
        } else {
            inEndPortal = false;
        }

        if (portal) {
            if (this.inPortalTicks <= 80) {
                // 81 means the server won't try to teleport
                this.inPortalTicks++;
            }
        } else {
            this.inPortalTicks = 0;
        }

        if (vector.lengthSquared() > 0) {
            vector = vector.normalize();
            double d = 0.014d;
            this.motionX += vector.x * d;
            this.motionY += vector.y * d;
            this.motionZ += vector.z * d;
        }
    }

    public boolean setPositionAndRotation(Vector3 pos, double yaw, double pitch) {
        if (this.setPosition(pos)) {
            this.setRotation(yaw, pitch);
            return true;
        }

        return false;
    }

    @Since("1.6.0.0-PN")
    public boolean setPositionAndRotation(Vector3 pos, double yaw, double pitch, double headYaw) {
        if (this.setPosition(pos)) {
            this.setRotation(yaw, pitch, headYaw);
            return true;
        }

        return false;
    }

    public void setRotation(double yaw, double pitch) {
        this.yaw = yaw;
        this.pitch = pitch;
        this.scheduleUpdate();
    }

    @Since("1.6.0.0-PN")
    public void setRotation(double yaw, double pitch, double headYaw) {
        this.yaw = yaw;
        this.pitch = pitch;
        this.headYaw = headYaw;
        this.scheduleUpdate();
    }

    /**
     * Whether the entity can active pressure plates.
     * Used for {@link cn.nukkit.entity.passive.EntityBat}s only.
     *
     * @return triggers pressure plate
     */
    public boolean doesTriggerPressurePlate() {
        return true;
    }

    public boolean canPassThrough() {
        return true;
    }

    protected void checkChunks() {
        if (this.chunk == null || (this.chunk.getX() != ((int) this.x >> 4)) || this.chunk.getZ() != ((int) this.z >> 4)) {
            if (this.chunk != null) {
                this.chunk.removeEntity(this);
            }
            this.chunk = this.level.getChunk((int) this.x >> 4, (int) this.z >> 4, true);

            if (!this.justCreated) {
                Map<Integer, Player> newChunk = this.level.getChunkPlayers((int) this.x >> 4, (int) this.z >> 4);
                for (Player player : new ArrayList<>(this.hasSpawned.values())) {
                    if (!newChunk.containsKey(player.getLoaderId())) {
                        this.despawnFrom(player);
                    } else {
                        newChunk.remove(player.getLoaderId());
                    }
                }

                for (Player player : newChunk.values()) {
                    this.spawnTo(player);
                }
            }

            if (this.chunk == null) {
                return;
            }

            this.chunk.addEntity(this);
        }
    }

    public boolean setPosition(Vector3 pos) {
        if (this.closed) {
            return false;
        }

        if (pos instanceof Position && ((Position) pos).level != null && ((Position) pos).level != this.level) {
            if (!this.switchLevel(((Position) pos).getLevel())) {
                return false;
            }
        }

        this.x = pos.x;
        this.y = pos.y;
        this.z = pos.z;

        this.recalculateBoundingBox(false); // Don't need to send BB height/width to client on position change

        this.checkChunks();

        return true;
    }

    public Vector3 getMotion() {
        return new Vector3(this.motionX, this.motionY, this.motionZ);
    }

    public boolean setMotion(Vector3 motion) {
        if (!this.justCreated) {
            EntityMotionEvent ev = new EntityMotionEvent(this, motion);
            this.server.getPluginManager().callEvent(ev);
            if (ev.isCancelled()) {
                return false;
            }
        }

        this.motionX = motion.x;
        this.motionY = motion.y;
        this.motionZ = motion.z;

        if (!this.justCreated) {
            this.updateMovement();
        }

        return true;
    }

    public boolean isOnGround() {
        return onGround;
    }

    public void kill() {
        this.health = 0;
        this.scheduleUpdate();

        for (Entity passenger : new ArrayList<>(this.passengers)) {
            dismountEntity(passenger);
        }
    }

    public boolean teleport(Vector3 pos) {
        return this.teleport(pos, PlayerTeleportEvent.TeleportCause.PLUGIN);
    }

    public boolean teleport(Vector3 pos, PlayerTeleportEvent.TeleportCause cause) {
        return this.teleport(Location.fromObject(pos, this.level, this.yaw, this.pitch, this.headYaw), cause);
    }

    public boolean teleport(Position pos) {
        return this.teleport(pos, PlayerTeleportEvent.TeleportCause.PLUGIN);
    }

    public boolean teleport(Position pos, PlayerTeleportEvent.TeleportCause cause) {
        return this.teleport(Location.fromObject(pos, pos.level, this.yaw, this.pitch, this.headYaw), cause);
    }

    public boolean teleport(Location location) {
        return this.teleport(location, PlayerTeleportEvent.TeleportCause.PLUGIN);
    }

    public boolean teleport(Location location, PlayerTeleportEvent.TeleportCause cause) {
        double yaw = location.yaw;
        double pitch = location.pitch;

        Location from = this.getLocation();
        Location to = location;
        if (cause != null) {
            EntityTeleportEvent ev = new EntityTeleportEvent(this, from, to);
            this.server.getPluginManager().callEvent(ev);
            if (ev.isCancelled()) {
                return false;
            }
            to = ev.getTo();
        }

        Entity currentRide = getRiding();
        if (currentRide != null && !currentRide.dismountEntity(this)) {
            return false;
        }

        this.ySize = 0;

        this.setMotion(this.temporalVector.setComponents(0, 0, 0));

        if (this.setPositionAndRotation(to, yaw, pitch)) {
            this.resetFallDistance();
            this.onGround = this.noClip ? false : true;

            this.updateMovement();

            return true;
        }

        return false;
    }

    public long getId() {
        return this.id;
    }

    public void respawnToAll() {
        Player[] players = this.hasSpawned.values().toArray(Player.EMPTY_ARRAY);
        this.hasSpawned.clear();

        for (Player player : players) {
            this.spawnTo(player);
        }
    }

    public void spawnToAll() {
        if (this.chunk == null || this.closed) {
            return;
        }

        for (Player player : this.level.getChunkPlayers(this.chunk.getX(), this.chunk.getZ()).values()) {
            if (player.isOnline()) {
                this.spawnTo(player);
            }
        }
    }

    public void despawnFromAll() {
        for (Player player : new ArrayList<>(this.hasSpawned.values())) {
            this.despawnFrom(player);
        }
    }

    public void close() {
        if (!this.closed) {
            this.closed = true;

            try {
                EntityDespawnEvent event = new EntityDespawnEvent(this);

                this.server.getPluginManager().callEvent(event);

                if (event.isCancelled()) {
                    this.closed = false;
                    return;
                }
            } catch (Throwable e) {
                this.closed = false;
                throw e;
            }

            try {
                this.despawnFromAll();
            } finally {
                try {
                    if (this.chunk != null) {
                        this.chunk.removeEntity(this);
                    }
                } finally {
                    if (this.level != null) {
                        this.level.removeEntity(this);
                    }
                }
            }
        }
    }

    private void close(Boolean despawn) {
        if (!this.closed) {
            this.closed = true;

            if (despawn) {
                EntityDespawnEvent event = new EntityDespawnEvent(this);

                this.server.getPluginManager().callEvent(event);

                if (event.isCancelled()) return;
            }

            this.despawnFromAll();

            if (this.chunk != null) {
                this.chunk.removeEntity(this);
            }

            if (this.level != null) {
                this.level.removeEntity(this);
            }
        }
    }

    public boolean setDataProperty(EntityData data) {
        return setDataProperty(data, true);
    }

    public boolean setDataProperty(EntityData data, boolean send) {
        if (Objects.equals(data, this.dataProperties.get(data.getId()))) {
            return false;
        }

        this.dataProperties.put(data);
        if (send) {
            EntityMetadata metadata = new EntityMetadata();
            metadata.put(this.dataProperties.get(data.getId()));
            if (data.getId() == DATA_FLAGS_EXTENDED) {
                metadata.put(this.dataProperties.get(DATA_FLAGS));
            }
            this.sendData(this.hasSpawned.values().toArray(Player.EMPTY_ARRAY), metadata);
        }
        return true;
    }

    public EntityMetadata getDataProperties() {
        return this.dataProperties;
    }

    public EntityData getDataProperty(int id) {
        return this.getDataProperties().get(id);
    }

    public int getDataPropertyInt(int id) {
        return this.getDataProperties().getInt(id);
    }

    public int getDataPropertyShort(int id) {
        return this.getDataProperties().getShort(id);
    }

    public int getDataPropertyByte(int id) {
        return this.getDataProperties().getByte(id);
    }

    public boolean getDataPropertyBoolean(int id) {
        return this.getDataProperties().getBoolean(id);
    }

    public long getDataPropertyLong(int id) {
        return this.getDataProperties().getLong(id);
    }

    public String getDataPropertyString(int id) {
        return this.getDataProperties().getString(id);
    }

    public float getDataPropertyFloat(int id) {
        return this.getDataProperties().getFloat(id);
    }

    public CompoundTag getDataPropertyNBT(int id) {
        return this.getDataProperties().getNBT(id);
    }

    public Vector3 getDataPropertyPos(int id) {
        return this.getDataProperties().getPosition(id);
    }

    public Vector3f getDataPropertyVector3f(int id) {
        return this.getDataProperties().getFloatPosition(id);
    }

    public int getDataPropertyType(int id) {
        return this.getDataProperties().exists(id) ? this.getDataProperty(id).getType() : -1;
    }

    public void setDataFlag(int propertyId, int id) {
        this.setDataFlag(propertyId, id, true);
    }

    public void setDataFlag(int propertyId, int id, boolean value) {
        if (this.getDataFlag(propertyId, id) != value) {
            if (propertyId == EntityHuman.DATA_PLAYER_FLAGS) {
                byte flags = (byte) this.getDataPropertyByte(propertyId);
                flags ^= 1 << id;
                this.setDataProperty(new ByteEntityData(propertyId, flags));
            } else {
                long flags = this.getDataPropertyLong(propertyId);
                flags ^= 1L << id;
                this.setDataProperty(new LongEntityData(propertyId, flags));
            }

        }
    }

    public boolean getDataFlag(int propertyId, int id) {
        return (((propertyId == EntityHuman.DATA_PLAYER_FLAGS ? this.getDataPropertyByte(propertyId) & 0xff : this.getDataPropertyLong(propertyId))) & (1L << id)) > 0;
    }

    @Override
    public void setMetadata(String metadataKey, MetadataValue newMetadataValue) {
        this.server.getEntityMetadata().setMetadata(this, metadataKey, newMetadataValue);
    }

    @Override
    public List<MetadataValue> getMetadata(String metadataKey) {
        return this.server.getEntityMetadata().getMetadata(this, metadataKey);
    }

    @Override
    public boolean hasMetadata(String metadataKey) {
        return this.server.getEntityMetadata().hasMetadata(this, metadataKey);
    }

    @Override
    public void removeMetadata(String metadataKey, Plugin owningPlugin) {
        this.server.getEntityMetadata().removeMetadata(this, metadataKey, owningPlugin);
    }

    public Server getServer() {
        return server;
    }

    @PowerNukkitOnly
    public boolean isUndead() {
        return false;
    }

    @PowerNukkitOnly
    @Since("1.2.1.0-PN")
    public boolean isInEndPortal() {
        return inEndPortal;
    }

    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    public boolean isPreventingSleep(Player player) {
        return false;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Entity other = (Entity) obj;
        return this.getId() == other.getId();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = (int) (29 * hash + this.getId());
        return hash;
    }

    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    public boolean isSpinAttacking() {
        return this.getDataFlag(DATA_FLAGS, DATA_FLAG_SPIN_ATTACK);
    }

    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    public void setSpinAttacking() {
        this.setSpinAttacking(true);
    }

    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    public void setSpinAttacking(boolean value) {
        this.setDataFlag(DATA_FLAGS, DATA_FLAG_SPIN_ATTACK, value);
    }

    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    public boolean isNoClip() {
        return noClip;
    }

    @PowerNukkitOnly
    @Since("1.4.0.0-PN")
    public void setNoClip(boolean noClip) {
        this.noClip = noClip;
        this.setDataFlag(DATA_FLAGS, DATA_FLAG_HAS_COLLISION, noClip);
    }
}
