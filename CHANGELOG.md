# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html) 
with an added upstream's major version number in front of the major version, so we have a better distinction from
Nukkit 1.X and 2.X.

## [Unreleased 1.6.1.0-PN] - Future ([Check the milestone](https://github.com/PowerNukkit/PowerNukkit/milestone/25?closed=1))
Click the link above to see the future.

## [1.6.0.1-PN] - 2022-04-21 ([Check the milestone](https://github.com/PowerNukkit/PowerNukkit/milestone/27?closed=1))
Fixes backward compatibility with some important plugins, also pull some changes from NukkitX

### Fixes
- [#1343] Fix player data properties being sent twice
- [#1343] Client side issues caused by normal data flags not being sent with extended flags
- [#1343] Item Net ID should be 1 if the item is not air
- [#1343] Accuracy of player movements
- [#1343] Maximum chat message size was too low
- [#1344] Backward compatibility with PowerNukkit port of Wode's AntiXRay plugin

### Changed
- [#1344] Log4J version to `2.17.1` (seriously, stop hacking it people)
- [#1343] Cornflower and Lily of the Valley will now be generated in flower forest biomes

## [1.6.0.0-PN] - 2022-04-20 ([Check the milestone](https://github.com/PowerNukkit/PowerNukkit/milestone/29?closed=1))
Major version change adding support to Minecraft `1.18.30`.

### Breaking changes
- [#1267] Changed Nimbus Jose JWT library from `7.9` to `9.13`
- [#1267] Removed some deprecated APIs, check the JDiff for details.
- [#1267] Changed the method signature to customize the boss bar color
- [#1267] `ItemArmor.TIER_OTHER` is not a constant anymore.

### Depreciation
- [#1266] Some APIs become deprecated, check the JDiff for details.
- [#1266] `ItemTrident.setCreative` and `getCreative` are now deprecated.
- [#1341] Some items had their name changed and their ID on `ItemID` are now deprecated

### Added
- [#1266] API to get the potion names, level in roman string and tipped arrow potion.
- [#1266] API for the banner pattern snout (Piglin)
- [#1341] New sounds to the sound enum
- [#1341] New particles effects to the relevant enums

### Changed
- [#1258] Changed supported version to Minecraft Bedrock Edition `1.18.0`.

### Fixes
- [#267] Regression of: Fishing hooks without players, loaded from the level save.
- [#1267] Network decoding of the `MoveEntityDeltaPacket`
- [#1267] `isOp` param of the `CapturingCommandSender` constructors were not being used
- [#1267] Boats placed by dispenser could have the wrong wood type
- [#1267] Falling anvil was not dealing damage to the entities correctly
- [#1267] Some randomizers could pick the same number over and over again.
- [#1267] Bowl and Crossbow fuel time
- [#1267] The durability of some items

### Documentation
- [#1267] Added all missing `@PowerNukkitOnly` annotations
- [#1267] Added all missing `@Override` annotations
- [#1267] Removed all incorrect `@PowerNukkitOnly` annotations

### Others
There are many other fixes, additions and changes not documented in this file due to the size of this update.

Check the [git logs][1.6.0.0-PN] for details on what has changed since 1.5.2.1-PN

## [1.5.2.1-PN] - 2021-12-21 ([Check the milestone](https://github.com/PowerNukkit/PowerNukkit/milestone/30?closed=1))

### CRITICAL SECURITY FIX
- [#1266], [#1270] Changed Log4J library from `2.13.3` to `2.17.0`

## [1.5.2.0-PN] - 2021-12-01 ([Check the milestone](https://github.com/PowerNukkit/PowerNukkit/milestone/28?closed=1))
This new version adds protocol support for Minecraft `1.17.40` as if it was `1.16.221` with some new features and fixes.

We are still working on `1.17` and `1.18` new features, but we plain to release them in December 2021.

`1.18` support will be added on `1.6.0.0-PN` and it will be released as soon as possible.

Thank you for the translations!
Help us to translate PowerNukkit at https://translate.powernukkit.org

Want to talk?
Talk to us at https://discuss.powernukkit.org and/or https://powernukkit.org/discord

### Added
- [#1233] New API classes and methods were added, check the [JDiff](https://devs.powernukkit.org/jdiff/1.5.2.0-PN_x_1.5.1.0-PN/changes.html) for details.
- [#1193] Add more damage causes to the API and improve magma block death message
- [#1233] French translations (thank you for the translations!)

### Changed
- [#1244] Changed the `recipes.json` and `creativeitems.json` format for easier changes, updates, and maintenance (backward compatible)
- [#1233] Updated Deutsche, Indonesian, Korean, Poland, Russian, Spanish, Turkish, Vietnamese, Brazilian Portuguese, and Simplified Chinese translations. (thank you!)

### Fixes
- [#1187] Fixes powered rails do not update in a row
- [#1191] `SimpleChunkManager.setBlockAtLayer` ignoring the layer
- [#1174] Fixes Infinite loop with double chest and comparator
- [#1202] Improves unknown item handling, shows unknown block instead of disconnections
- [#982] Populator error due to corruption on compressed light data
- [#1214] Fixed the names for BlockConcrete and BlockConcretePowder
- [#1172] Fix and improve resource pack related packets

## [1.5.1.0-PN] - 2021-07-05 ([Check the milestone](https://github.com/PowerNukkit/PowerNukkit/milestone/22?closed=1))
Our goal on this version was to fix bugs, and we did it, we fixed a lot of them!

Thank you for the translations!
Help us to translate PowerNukkit at https://translate.powernukkit.org

Want to talk? 
Talk to us at https://discuss.powernukkit.org and/or https://powernukkit.org/discord

### Changed
- [#1107] Guava version from `29.0` to `30.1.1`
- [#1107] SnakeYAML version from `1.26` to `1.28`
- [#1134] Update the Chinese, Russian, and Turkish translations. Thank you for your contributions!
- [#1149] Update the Spanish, and Russian translations. Also improved the message when a plugin is not found. Thank you for your contributions!
- [#1177] Update the Portuguese, Chinese, and Polish translations. Also added the key `language.locale` to allow plugin devs to build a `Locale` object
- [#1150] The `show_death_message` gamerule was renamed to `show_death_messages`. A backward compatibility code will keep the old one working, but it's now deprecated.
- [#1151] Improved `/setworldspaw` auto completion
- [#1153] Deprecate BlockNetherBrick in favor of BlockBricksNether
- [#783] Campfire now drop 2 charcoal always
- [#783] Soul campfire now drops 1 soul sand
- [#783] Soul campfire now deal double the damage that normal campfires deals
- [#783] Campfire and Soul campfire now deal damage even the entity is sneaking
- [#783] Campfire and Soul campfire now breaks when pushed by piston
- [#669] Improved the output of the `/kill @e` command

### Added
- [#1146] Added implementation for `AnimateEntityPacket`
- [#1150] The `freeze_damage` gamerule 
- [#1150] Mappings for Goat, Glow Squid, and Axolotl entities and spawn eggs
- [#783] Campfire and Soul Campfire can now be lit by burning entities stepping on it
- [#783] Campfire and Soul Campfire can now be unlit by throwing a splash water bottle on it
- [#783] Campfire and Soul Campfire can now lit by using an item enchanted with fire aspect
- [#669] New API methods to get the name of the entity for display

### Fixes
- [#1119] `TickSyncPacket` was not registered 
- [#1120] Entities sometimes gets invisible for some players
- [#1122] Backward compatibility with plugins setting full bark logs with 17:13
- [#1132] You don't dismount the vehicle when you teleport, causing you to glitch
- [#1103] The output message of the `/enchant` command
- [#1100] Abrupt Time Change
- [#1130] Soul Campfire and End Crystal were rendering as other items in the inventory
- [#1139] Backward compatibility with some custom world generators
- [#1147] Sharpness damage calculation
- [#1153] Some code quality issues reported by sonar
- [#1170] Cobwebs are now breakable by using shears
- [#702] Burning arrow and rain will make a lot of particles
- [#625] If you instant kill a mob with fire aspect enchant tool, it will not give fire aspect drops
- [#979] Fixes an issue where the players could not hear each other walking
- [#576] Swimming in a 1x1 tunnel of water was causing suffocation damage by the block above the player

## [1.5.0.0-PN] - 2021-06-11 ([Check the milestone](https://github.com/PowerNukkit/PowerNukkit/milestone/26?closed=1))
This was quick! This new version add protocol support for Minecraft `1.17.0` as if it was `1.16.221`.

The new changes will be implemented in `1.5.1.0-PN` and onwards.

This version works with Minecraft `1.16.221`!

### Breaking change!
***This version supports a new major Minecraft version, some plugin sources might need to be updated or recompiled!***

- `BlockWall.WallType.END_STONE_BRICK` was renamed to `END_BRICK` to match the property
- Custom blocks now have to implement `Block.getProperties()` if they need to have custom meta.
- `BlockCauldron.getFillLevel()` and it's setter now range from 0 to 6. Glass bottle remove/add 2 levels instead of one now.
- The creative inventory file format has changed
- The recipes file format has changed
- `BellAttachmentType` was renamed to `AttachmentType`
- `BlockBell.getBellAttachmentType` and `BlockBell.setBellAttachmentType` were renamed to `get/setAttachment`.
- `DoublePlantType` enum had the entries changed to match the property values.
- `BlockMeta`, `BlockSolidMeta`, and `BlockFallableMeta` now have `getProperties` abstract.
- `CommonBlockProperties.LEGACY_PROPERTY_NAME`, `LEGACY_PROPERTIES`, and `LEGACY_BIG_PROPERTIES` were removed.
- `MinecraftItemID.DEBUG_STICK` was removed.
- All deprecated stuff marked to be removed at this version was removed. Except `AnvilDamageEvent.getDamage()`.

### Deprecated
- This is a reminder that numeric block meta are deprecated. Use the specifc block API to make modifications. Come to Discord if you have questions.
- A lot of duplicated BlockIDs are being deprecated, follow the `replaceBy` instructions to use the right ones.

### Changed
- All blocks are now using the new block state system.
- We are no longer using `runtime_block_states.dat` and `runtime+block_states_overrides.dat`, we are now using `canonical_block_states.nbt` from [pmmp/BedrockData](https://github.com/pmmp/BedrockData)
- `BlockProperties.requireRegisteredProperty` now throws `BlockPropertyNotFoundException` instead of `NoSuchElementException` when the prop is not found.
- Some `Entity` magic values have changed
- Game rules now have a flag to determine if it can be changed.

#### Added
- Event to handle player fishing by plugins. `PlayerFishEvent`.
- 3 new packets: `AddVolumeEntityPacket`, `RemoveVolumeEntityPacket`, and `SyncEntityPropertyPacket`

### Fixes
- Issues with crafting recipes involving charcoal and dyes and ink_sac related items

## [1.4.0.0-PN] - 2021-05-31 ([Check the milestone](https://github.com/PowerNukkit/PowerNukkit/milestone/15?closed=1))
It's finally here! A stable version of the Nether update! Supporting almost all blocks and items!

It works with Minecraft `1.16.221`!

### Breaking change!
***This version supports a new major Minecraft version, some plugin sources might need to be updated or recompiled!***

- Many `final` constants are no longer constants, they are now marked with `dynamic` due to constant changes on updates
- The size of the block data bits changed back from `6` to `4` to fix backward compatibility with Nukkit plugins
- New chunk content versioning! Don't keep changing versions back and forth, or you will end up with having some odd block states!

### Deprecated
- All usage of the numeric block damage system is now deprecated, new code should use the new block state system
- Direct usage of static mutable arrays in the Block class are now deprecated, use the getters and API methods instead
- Avoid using `Item.get` to get ItemBlocks! Use `Item.getBlock` or use `MinecraftItemID.<the-id>.get` instead!

### Fixes
- [#857] Items in wrong tabs of the creative inventory and at the side of crafting grid screens
- [#959] Give command not working correctly when using a namespace, like in `/give minecraft:dirt`
- [#902] NetherPortal block can't be destroyed by liquid flow
- [#902] Lava doesn't turn concrete powder into concrete
- [#770] Bamboo not dropping when broken, were also affecting blocks with id > 255
- [#765] Unsafe level.dat writes could lead to world corruption
- [#766] Error saving region files with content over 2 GB
- [#777] Falling block falling though scaffolds
- [#778] Unable to get `minecraft:mob_spawner` with `/give Nick mob_spawner`
- Snowballs not damaging blazes
- Issues with the geometry of player and human entities
- Hay bale not reducing fall damage
- Lapis ore drops with enchanted pickaxes
- Break time calculations
- A lot of block placement rules
- A lot of item drop rules
- Mixing potions, water, lava, and dyes in cauldrons
- Many boat issues
- Many dispenser issues
- Some duplication issues
- Enchantment level of the enchantments
- Many other issues not listed here

### Added
- Block state system and API with backward compatibility to the legacy numeric block damage system
- [#917] Adds automatic bug reports using Sentry, can be opted out in `server.properties`
- API to get how long the player has been awake
- New APIs to detect the type of bucket, dye, spawn egg, coal, and a few others
- A `MinecraftItemID` API for simpler version independent vanilla item creation
- Shield mechanics
- Trident mechanics
- Many new API classes and methods not listed here
- Emerald ore generation

#### Blocks
- Allow
- Deny
- Structure Void
- Nether Reactor Core
- Structure Block
- Lodestone
- Crimson Roots
- Warped Woots
- Warped Wart Block
- Crimson Fungus
- Warped Fungus
- Shroomlight
- Weeping Vines
- Crimson Nylium
- Warped Nylium
- Basalt
- Polished Basalt
- Soul Soil
- Soul Fire
- Nether Sprouts Block
- Target
- Stripped Crimson/Warped Stem
- Crimson/Warped Planks
- Crimson/Warped Door
- Crimson/Warped Trapdoor
- Crimson/Warped Sign
- Crimson/Warped Stairs
- Crimson/Warped Fences
- Crimson/Warped Fence Gate
- Crimson/Warped Button
- Crimson/Warped Pressure Plate
- Crimson/Warped Slab
- Soul Torch
- Soul Lantern
- Netherite Block
- Ancient Derbirs
- Respawn Anchor
- Blackstone
- Polished Blackstone Bricks
- Polished Blackstone Bricks Stairs
- Blackstone Stairs
- Blackstone Wall
- Polished Blackstone Bricks Wall
- Chiseled Polished Blackstone
- Cracked Polished Blackstone Bricks
- Gilded Blackstone
- Blackstone Slab
- Polished Blackstone Brick Slab
- Chain Block
- Twisting Vines
- Nether Gold Ore
- Crying Obsidian
- Soul Campfire
- Polished Blackstone
- Polished Blackstone Stairs
- Polished Blackstone Slab
- Polished Blackstone Pressure Plate
- Polished Blackstone Button
- Polished Blackstone Wall
- Warped/Crimson Hyphae
- Stripped Warped/Crimson Hyphae
- Chiseled Nether Bricks
- Cracked Nether Bricks
- Quartz Bricks

#### Items
- Rabbit Hide
- Lead
- Popped Chorus Fruit
- Dragon Breath
- Iron Nugget
- Crossbow (shooting is not implemented)
- Lodestone
- Netherite Ingot
- Netherite Sword
- Netherite Shovel
- Netherite Pickaxe
- Netherite Axe
- Netherite Hoe
- Netherite Helmet
- Netherite Chestplate
- Netherite Leggings
- Netherite Boots
- Netherite Scrap
- Warped Fungus On A Stick
- Record Pigstep
- Nether Sprouts

#### Entities
- Armor Stand
- Iron Golem
- Snow Golem
- Piglin Brute
- Fox
- NPC (Edu)

#### Enchantments
- Multishot
- Piercing
- Quick Charge
- Soul Speed

#### Effects
- Bad Omen
- Village Hero

#### Potions
- Slowness II Extended
- Slowness IV

### Changed
- Translations updated. Help us to translate PowerNukkit at https://translate.powernukkit.org
- The block system was revamped
- Optimized the RAM memory usage
- Many hard-coded block, item, and entity instantiation were replaced to dynamic calls, allowing plugins to use custom classes
- [#765] The `ServerBrand` tag in the `level.dat` file will be set to `PowerNukkit` now
- [#776] Grindstone won't reset the repair cost anymore
- Packet batching is now handled near the RakNet layer
- Removed extra data from chunk encoding
- The sound enum has been updated
- Bucket with fish can no longer interact with cauldrons
- The /give command now support all current vanilla namespaced ids
- Updated the raknet dependency from 1.6.15-PN2 to 1.6.25-PN
- Improved the `/debugpaste` command, it saves the paste locally now, to upload the paste use `/debugpaste upload` or `/debugpaste upload last`
- Many commands were improved
- Improved javadocs
- Improved the bed behaviour

## [1.3.1.5-PN] - 2020-09-01
Fixes a critical duplication exploit.

### Fixes
- [#544] Duplication exploit by packet manipulation

### Changed
- Translations updated

## [1.3.1.4-PN] - 2020-08-14  ([Check the milestone](https://github.com/PowerNukkit/PowerNukkit/milestone/20?closed=1))
Fixes beehives, client crashes in Nether and improves some error handling

### Fixes
- [#467] Players crash when reconnecting in the Nether
- [#469] Players who don't crash when reconnecting in the Nether, see overworld sky
- [#462] Beehives and bee nest getting rendered as an "UPDATE!" block
- [#475] If middle packet inside a batch packet fails processing, the other packets in the batch gets ignored

### Changed
- [#475] Improved error log whilst loading a config file
- [#475] Improved error log when a batch packet decoding or processing fails
- [#462] The beehive and bee_nest block data have been changed from `[3-bits BlockFace index, 3-bits honey level]` to `[2-bits BlockFace horizontal index, 3-bits honey level]`
- [#462] The chunk's content version got increased to 5
- [#464] The German and the Simplified Chinese translations have been updated

## [1.3.1.3-PN] - 2020-08-11 ([Check the milestone](https://github.com/PowerNukkit/PowerNukkit/milestone/19?closed=1))
A quick update that adds support to 1.16.20 and updates the translations

### Fixes
- [#298] Having the gamemode changed by another player shows a `%s` in the chat

### Changed
- Changed the protocol version to support Minecraft Bedrock Edition 1.16.20
- The translations have been updated

## [1.3.1.2-PN] - 2020-08-10 ([Check the milestone](https://github.com/PowerNukkit/PowerNukkit/milestone/18?closed=1))
Very important fixes that you must have. Make a backup before upgrading.

### Fixes
- [#404] Issues with inventories, click events, and cursor
- [#365] Client crashing when FakeInventories
- [#339] Client crashes when closing some inventories
- [#287] Campfire does not extinguish when it touches flowing water
- [#287] Grindstone placement rule on vines, bubble, liquid, and replaceable blocks
- [#287] `BlockGrowEvent` being fired when using bone meal on dead sea pickle
- [#287] Mesa biome generating a wrong block instead of red sandstone
- [#366] Block disappears when making bridges
- [#261] Bamboo and bamboo sampling bone meal, placement, and breaking behaviours
- [#359] Piston causing tile entities to invalidate
- [#340] Brewing stand placement rules, recipes, and processing
- [#397] Firework effects getting overridden by a black creeper face
- [#400] OP players in spectator gamemode could break blocks in client-side
- [#403] Right-clicking some blocks while sneaking were not opening the block
- [#407] Server could be stuck and not shutdown even after Watchdog detects the an issue
- [#412] Daylight detector's tile entity wasn't being removed
- [#440] Predefined long world seeds wasn't loading correctly
- [#414] Minecart names could return null can cause unexpected NullPointerExceptions
- [#436] Chorus plant and flowers could be placed anywhere and could keep floating
- [#436] Chorus plant and flowers had wrong blast resistance values
- [#437] Nether dimension having overworld sky
- [#427] Campfire was moving with pistons
- [#422] Can't ignite leaves with flint and steel directly
- [#450] Can't ignite leaves with fireball directly
- [#450] Flowerpot placement and support rules
- [#430] Redstone repearter not causing redstone update to the block right in front of it
- [#445] Some languages had different default nukkit.yml settings values
- [#443] Boats and minecarts were not checking if they were already 'killed' and could drop itself more than once
- [#404] Minecarts trying to make death animations when it don't have
- [#404] Comparator not causing redstone updates correcty
- [#404] Fixed three duplication glitches
- [#430] Fire not fading sometimes
- [#430] Iron door not dropping when you break the block under it
- [#449] Honey block couldn't be used to make a note on noteblock

### Added
- [#287] You can now set yaw and pitch when using the teleport command: `/tp <x> <y> <z> <yaw> <pitch>`
- [#445] New translation site. Help us to translate PowerNukkit at https://translate.powernukkit.org

### Changes
- [#390] If a compression issue happens, an IOException will be thrown now
- [#287] Removed the teleport limitation in y-axis with the `/tp` command
- [#287] Campfire does not allow flowing allow passing through it anymore
- [#287] Improved the lantern placement rules code
- [#287] Improved the liquid flow logic
- [#287] Prevents placing blocks in water if the block would break itself in the next tick
- [#287] Narrow down the logic to prevent the right-click spam bug
- [#404] Grindstone will not be forced to face up when replacing vines anymore
- [#445] The translation system have been improved
- [#433] Improved snowball particle performance
- [#404] Chunk content version bumped to 4

## [1.3.1.1-PN] - 2020-07-19
Fixes an important stability issue and improves resource pack compatibility

### Fixes
- [#390] Server stop responding due to a compression issue
- [#368] Improves resource pack compatibility

## [1.3.1.0-PN] - 2020-07-09 ([Check the milestone](https://github.com/PowerNukkit/PowerNukkit/milestone/13?closed=1))
Security, stability and enchanting table fixes alongside with few additions.

PowerNukkit now has its own [discord guild], click the link below to join and have fun!  
💬 https://powernukkit.org/discord 💬  
[![Discord](https://img.shields.io/discord/728280425255927879)](https://powernukkit.org/discord)

### Fixes
- [#326] Enchantment table not working
- [#297] Using the hoe or shovel doesn't emit any sound
- [#328] ClassCastException and some logic errors while processing the chunk backward compatibility method
- [#344] Sticky pistons not pulling other sticky piston
- [#344] The technical block names weren't being saved in memory when `GlobalBlockPalette` was loaded
- [#338] The Dried Kelp Block was not burnable as fuel
- [#232] The enchanting table level cost is now managed by the server

### Added
- [#330] The [discord guild] link to the readme
- [#352] The library jsr305 library at version `3.0.2` to add `@Nullable`, `@Nonnull` and related annotations
- [#326] A couple of new classes, methods and fields to interact with the enchanting table transactions
- [#326] The entities without AI: Hoglin, Piglin, Zoglin, Strider
- [#352] Adds default runtime id to the new blocks with meta `0`

### Changed
- [#348] Updated the guava library from `21.0` to `24.1.1`
- [#347] Updated the JWT library from `4.39.2` to `7.9`
- [#346] Updated the Log4J library from `2.11.1` to `2.13.3`
- [#326] Changed the Nukkit API version from `1.0.10` to `1.0.11`
- [#335] The chunk content version from `1` to `2`, all cobblestone walls will be reprocessed on the chunk first load after the update
- [#352] The `runtime_block_states_overrides.dat` file has been updated

## [1.3.0.1-PN] - 2020-07-01 ([Check the milestone](https://github.com/PowerNukkit/PowerNukkit/milestone/14?closed=1))
Improves plugin compatibility and downgrade the RakNet lib to solve a memory leak

### Fixes
- [#320] Multiple output crafting, cake for example
- [#323] Compatibility issue with the regular version of GAC

### Added
- [#315] Hoglin, Piglin, Zoglin and Strider entities without AI

### Changed
- [#319] The RakNet library were downgraded to 1.6.15 due to a potential memory leak issue

## [1.3.0.0-PN] - 2020-07-01 ([Check the milestone](https://github.com/PowerNukkit/PowerNukkit/milestone/11?closed=1))
Added support for Bedrock Edition 1.16.0 and 1.16.1

### Breaking change!
***This version supports a new major Minecraft version, some plugin sources might need to be updated or recompiled!*** 

The following breaking change will be pulled in `1.3.0.0-PN`
- [8a09f93](https://github.com/PowerNukkit/PowerNukkit/commit/8a09f933f83c9a52531ff8a184a58c6d733c9174) Quick craft implementation. ([NukkitX#1473](https://github.com/NukkitX/Nukkit/pull/1473)) Jedrzej* 05/06/2020

### Binary incompatibility!
- [#293] A few `Entity` data constant values were changed, plugins which uses them might need to be recompiled, no code change required

### Save format changed!
The save format has been changed to accommodate very high block data values. **Make a world backup before updating!**

### Incomplete changelog warning!
Due to the high amount of changes, and the urgency of this update, this changelog file will be released with outdated information,
please check the current changelog file in the [updated changelog] online for further details about this update.

### Disabled features warning!
* Enchanting table GUI has been temporarily disabled due to an incompatible change to the Bedrock protocol,
it's planned to be fixed on 1.3.1.0-PN
* End portal formation has been disabled due to reported crashes, it's planned to be reviewed on 1.3.1.0-PN

### Experimental warning!
This is the first release of a huge set of changes to accommodate the new Bedrock Edition 1.16.0/1.16.1 release,
please take extra cautions with this version, make constant backups and report any issues you find. 

### Deprecation warnings!
- [#293] Many `Entity` constants are deprecated and might be removed on `1.4.0.0-PN`
- [#293] `Entity.DATA_FLAG_TRANSITION_SITTING` and `DATA_FLAG_TRANSITION_SETTING` only one of them is correct, the incorrect will be removed
- [#293] `Network.inflate_raw` and `deflate_raw` does not follow the correct naming convention and will be removed. Use `inflateRaw` and `deflateRaw` instead. 
- [#293] `HurtArmorPacket.health` was renamed to `damage` and will be removed on `1.4.0.0-PN`. A backward compatibility code has been added.
- [#293] `SetSpawnPositionPacket.spawnForce` is now unused and will be removed on `1.4.0.0-PN`
- [#293] `TextPacket.TYPE_JSON` was renamed to `TYPE_OBJECT` and will be removed on `1.4.0.0-PN`
- [#293] `riderInitiated` argument was added to the `EntityLink` constructor. The old constructor will be removed on `1.4.0.0-PN`

### Fixes
- [#293] Spectator colliding with vehicles
- [#293] Ice melting into water in the Nether
- [#293] `Player.removeWindow` was able to remove permanent windows

### Added
- [#293] End portals can now be formed using Eye of Ender
- [#293] Setting to make the server ignore specific packets
- [#293] New compression/decompression methods
- [#293] Trace logging to outbound packets when trace is enabled
- [#293] The server now logs a warning when a packet violation warning is correctly received
- [#293] 12 new packets, please see the pull request file changes for details
- [#293] Many new entity data constants, please see the `Entity.java` file in the PR for details
 
### Changed
- [#293] Thorns can now be applied to any armor while enchanting
- [#293] The server now requires the clients to playing on Bedrock Edition 1.16.0
- [#293] Updated RakNet to `1.6.18`
- [#293] RakNet protocol version changed from `9` to `10`
- [#293] 10 packets, please see the pull request file changes for details
- [#293] The server have more control over the player UI now
- [#293] New entity data constants
- [#293] `FakeBlockUIComponent` now fires `InventoryCloseEvent` when the inventory is closed
- [#293] The `runtime_block_states.dat`, `recipes.json`, `entity_identifiers.dat` and `biome_definitions.dat` files have been updated
- [#293] Grindstone now clears only the enchantments and sets the repair cost to `0`, it used to clear all NBT tags


## [1.2.1.0-PN] - 2020-06-07 ([Check the milestone](https://github.com/PowerNukkit/PowerNukkit/milestone/9?closed=1))
Adds new methods to be used by plugins and fixes many issues. 

### Fixes
- [#224] Enchantment compatibility rules when merging enchanted items in an anvil
- [#113] Beehives not dropping in creative when it has bees
- [#270] Replacing sugarcane's water don't break the sugarcane immediately
- [#272] `EntityPortalEnterEvent` not being fired when entering end portals
- [#279] `BlockEndPortal` missing collision bounding box
- [#279] `Entity.checkBlockCollision()`'s over scaffolding logic outdated
- [#281] Levers and buttons don't replace the snow layers
- [#285] Chicken, cow, pig, rabbit and sheep not dropping cooked food when on fire
- [#285] Chorus plant and flower not dropping
- [#285] Item string placing tripwire hooks instead of tripwires
- [#285] Wrong block name and color for dark prismarine block and prismarine bricks
- [#285] Nether bricks fence were burnable and flammable
- [#285] Item on hands disappear (looses one from the stack) when interacting with chest minecarts and hopper minecarts

### Added
- [#227] PlayerJumpEvent called when jump packets are received.
- [#242] `Item.equalsIgnoringEnchantmentOrder` method for public usage.
- [#244] `Enchantment.getPowerNukkit().isItemAcceptable(Item)` to check if an enchantment can exist 
         in a given item stack by any non-hack means.
- [#256] `CapturingCommandSender` intended to capture output of commands which don't require players.
- [#259] `Hash.hashBlock(Vector3)` method for public usage.
- [#261] `Player.isCheckingMovement()` method for public usage.
- [#261] Protected field `EntityEndCrystal.detonated` to disable the `EndCrystal.explode()` method.
- [#275] New annotations to document when elements get added and when deprecated elements will be removed
- [#123] Adds and register the banner pattern items
- [#276] `Block.afterRemoval()` called automatically when the block is replaced using any `Level.setBlock()`
- [#277] `Block.mustSilkTouch()` and `Block.mustDrop()` to allow blocks to force the dropping behaviour when being broken
- [#279] `Entity.isInEndPortal()` for public usage
- [#285] `LoginChainData.getRawData()` for public usage

### Changed
- [#227] Sugar canes now fires BlockGrowEvent when growing naturally.
- [#261] Kicked players can now view the kick reason on kick.
- [#285] Limit the maximum size of `BookEditPacket`'s text to 256, ignoring the packet if it exceeds the limit
- [#285] Ender pearls will now be unable to teleport players across different dimensions
- [#285] `ShortTag.load(NBTInputStream)` now reads a signed short. Used to read an unsigned short.

## [1.2.0.2-PN] - 2020-05-18 ([Check the milestone](https://github.com/PowerNukkit/PowerNukkit/milestone/10?closed=1))
Contains several fixes, including issues which cause item losses and performance issues

### Fixes
- [#239] Anvil fails to merge some enchantments because the ordering mismatches
- [#240] Anvils were charging fewer levels to merge thorn books
- [#243] Anvils were charging more levels to merge punch books
- [#246] Anvil checking the enchantment table property instead of the enchantment id
- [#246] Compatibility rules for unbreaking, fortune, mending, riptide, loyalty and channeling enchantments
- [#248] Air blocks with metadata were being rendered as "UPDATE!" block (backward compatibility fix)
- [#212] The `/tp player 0 1 2` command doesn't work
- [#220] Stripping old full bark log results in a wrong block
- [#157] Wrong Packed and Blue Ice break time with the hands
- [#193] Wrong explosion behaviour with waterlogged block
- [#103] Fixes BlockLeaves's random update logic spamming packets and consuming CPU unnecessarily
- [#253] Fixes `LeavesDecayEvent` also being called when leaves wouldn't decay
- [#254] Fixes BlockLeaves not checking for log connectivity, was checking only if it had a log block nearby
- [#255] Fix /status information in /debugpaste not being collected
- [#260] Fix a stack overflow when setting off end crystals near to each other
- [#260] Fix drops of block entity inventory contents on explosion
- [#260] Check SUPPORTED_PROTOCOLS instead of CURRENT_PROTOCOL in `LoginPacket.decode()`
- [#79] Sugarcane can grow without water
- [#262] Removing the water don't break the sugarcane (using empty bucket or breaking water flow)
- [#263] Fixes disconnect messages not reaching the player sometimes
- [#116] Fishing hooks don't attach to entities and damages multiples entities
- [#95] The Level Up sound is not centered
- [#267] Fishing hooks without players, loaded from the level save. They are now removed on load
- [#266] Loosing connection with items in an open anvil makes you loose the items
- [#273] Loosing connection with items in an open grindstone, enchanting table, stone cutter  makes you loose the items
- [#273] Loosing connection with items in an open crafting table, 2x2 crafting grid makes you loose the items

### Changed
- [#247] Invalid BlockId:Meta combinations now log an error when found. It logs only once
- [#255] The report issues link has been changed to point to the PowerNukkit repository
- [#268] The `/xp` command now makes level up sound every 5 levels
- [#273] If an anvil, grindstone, enchanting, stonecutter, crafting GUI closes, the items will try to go to the player's inventory
- [#273] `FakeBlockUIComponent.close(Player)` now calls `onClose(Player)`
- [#274] `Player.checkInteractNearby()` is now called once every 10 ticks, it was called every tick

## [1.2.0.1-PN] - 2020-05-08 ([Check the milestone](https://github.com/PowerNukkit/PowerNukkit/milestone/8?closed=1))
Fixes several anvil issues.

### Added
- [#224] Added option to disable watchdog with `-DdisableWatchdog=true`. 
  This should be used **only by developers** to debug the server without interruptions by the crash detection system.

### Fixes
- [#224] Anvil not merging enchanted items correctly and destroying the items.
- [#228] Invalid enchantment order on anvil's results causing the crafting transaction to fail.
- [#226] Anvil cost calculation not applying bedrock edition reductions
- [#222] Anvil changes the level twice and fails the transaction if the player doesn't have enough.
- [#235] Wrong flags in MoveEntityAbsolutePacket
- [#234] Failed anvil transactions caused all involved items to be destroyed
- [#234] Visual desync in the player's experience level when an anvil transaction fails or is cancelled. 

### Changed
- [#234] Anvil's result is no longer stored in the PlayerUIInventory at slot 50 as 
         it was vulnerable to heavy duplication exploits.
- [#234] `setResult` methods in `AnvilInventory` are now deprecated and marked for removal at 1.3.0.0-PN
         because it's not supported by the client and changing it will fail the transaction.

## [1.2.0.0-PN] - 2020-05-03 ([Check the milestone](https://github.com/PowerNukkit/PowerNukkit/milestone/6?closed=1))
**Note:** Effort has been made to keep this list accurate but some bufixes and new features might be missing here, specially those made by the NukkitX team and contributors.

### Added
- This change log file
- [#108] EntityMoveByPistonEvent
- [#140] `isUndead()` method to the entities

### Fixes
- [#129] A typo in the BlockBambooSapling class name **(breaking change)**
- [#102] Leaves decay calculation
- [#87] Arrows in offhand are black in the first person view
- [#46] checked if ProjectileHitEvent is cancelled before the action execution
- [#108] Lever sounds
- [#108] Incorrect sponge particles
- [#12] Wrong redstone signal from levers
- [#129] You can now shift to climb down while you are in the edges of a scaffold
- [#129] Fixes a turtle_egg placement validation
- [#129] Campfire can no longer be placed over an other campfire directly
- [#129] The sound that campfire does when it extinguishes
- [#140] Instant damage and instant health are now inverted when applied to undead entities
- [#140] A collision detection issue on Area Effect Cloud which could make it wears off way quicker than it should
- [#152] Changes the blue_ice blast resistance from 2.5 to 14
- [#170] Trapdoors behaving incorrectly when they receive redstone signal
- [#219] Button and door sounds
- [#44] Different daytime from Android and Windows 10 Edition
- [#93] Nukkit sends a rain time that doesn't matches the server
- [#210] Issues with old blocks from old NukkitX worlds, specially fully barked logs (log:15 for example)

### Changed
- Make BlockLectern implements Faceable
- The versioning convention now follows this pattern:<br/>`upstream.major.minor.patch-PN`<br/>[Click here for details.](https://github.com/PowerNukkit/PowerNukkit/blob/7912aa4be68e94a52762361c2d5189b7bbc58d2a/pom.xml#L8-L14)

## [1.1.1.0-PN] - 2020-01-21
### Fixes
- Piston heads not rendering
- Cauldron implementation, should be closer to vanilla now
- Implements hashCode in the NBT Tags, fixes usage with Set and HashMap
- Fixes BaseInventory ignoring it's own max stack size
- Fix cauldron's lightFilter value
- Fix the project throwing sound effect
- No particles when snow hits something
- Fixes projectile sounds
- Fixes egg particles and exp sounds
- The anvil block implementation
- Plants now requires light to grow
- Fix player does not get update for own skin
- Fix ~ operator in teleport command
- Fix ~ operator in /particle command
- Fall damage with slow falling effect
- Fishing Hook drag and gravity values
- [a8247360] Crops, grass, leaves, tallgrass growth and population
- Fixes fuzzy spawn radius calculation
- [#49] noDamageTicks should make the entity completely invulnerable while active
- [#54] Fixes movement issues on heavy server load
- [#57] Fixes block placement of Bone Block, End Portal Frame, Jukebox and Observer

### Changed
- Unregistered block states will be shown as 248:0 (minecraft:info_update) now
- Improves the UI inventories
- The codename to PowerNukkit to distinct from [NukkitX]'s implementation
- [#50] The kick message is now more descriptive
- [#80] Merged the "New RakNet Implementation" pull request which greatly improves the server performance and connections

### Added 
- Waterlogging support
- Support with blocks ID higher then 255 to the Anvil save format
- Support for blocks with 6 bits data value (used to support only 4 bits)
- [#51] Support for the offhand slot
- [#52] Merge the "More redstone components" pull request which fixes and implements many redstone related blocks
- [#53] Merge the "Vehicle event fix" pull request which add new events and fixes damage issues related to vehicles
- [#55] Minecart (chest and hopper) inventories
- [#56] ServerStopEvent
- Shield block animation (without damage calculation)
- New gamerules
- The /setblock command
- Dyeing leather support to cauldrons
- Color mixing support to cauldron
- Implementation for the entities (without AI):
    - Bees
    - Lingering Potions
    - Area Effect Clouds
- Implementation for the items:
    - Honey
    - Honey Bottle
    - Honeycomb
    - Suspicious Stew
    - Totem of Undying (without functionality)
    - Name Tags
    - Shulker Shell
- Implementation for the blocks:
    - [#58] Daylight Sensor
    - Lectern
    - Smoker
    - Blast Furnace
    - Light Block
    - Honeycomb Block
    - Wither Roses
    - Honey Block
    - Acacia, Birch, Dark Oak, Jungle, Spruce signs
    - Composter
    - Andesite, Polished Andesite, Diorite, Polished Diorite, End Brick, Granited, Polished Granite, Mossy Cobblestone stairs
    - Mossy Stone Brick, Prismarine Brick, Red Nether Brick stairs, Smooth Quartz, Red Sandstone, Smooth Sandstone stairs
    - Beehive and Bee Nests
    - Sticky Piston Head
    - Lava Cauldron
    - Wood (barks)
    - Jigsaw
    - Stripped Acacia, Birch, Dark Oak, Jungle, Oak and Spruce logs and barks
    - Blue Ice
    - Seagrass
    - Coral
    - Coral Fans
    - Coral Blocks
    - Dried Kelp Block
    - Kelp
    - Carved Pumpkin
    - Smooth Stone
    - Acacia, Birch, Dark Oak, Jungle, Spruce Button
    - Acacia, Birch, Dark Oak, Jungle, Spruce Pressure Plate
    - Acacia, Birch, Dark Oak, Jungle, Spruce Trapdoor
    - Bubble Column
    - Scaffolding
    - Sweet Berry Bush
    - Conduit
    - All stone type slabs
    - Lantern
    - Barrel
    - Campfire
    - Cartography Table
    - Fletching Table
    - Smithing Table
    - Bell
    - Turtle Eggs
    - Grindstone
    - Stonecutter
    - Loom
    - Bamboo

[updated changelog]:https://github.com/PowerNukkit/PowerNukkit/blob/bleeding/CHANGELOG.md
[discord guild]: https://powernukkit.org/discord

[Unreleased 1.6.1.0-PN]: https://github.com/PowerNukkit/PowerNukkit/compare/v1.6.0.1-PN...bleeding
[1.6.0.1-PN]: https://github.com/PowerNukkit/PowerNukkit/compare/v1.6.0.0-PN...v1.6.0.1-PN
[1.6.0.0-PN]: https://github.com/PowerNukkit/PowerNukkit/compare/v1.5.2.1-PN...v1.6.0.0-PN
[1.5.2.1-PN]: https://github.com/PowerNukkit/PowerNukkit/compare/v1.5.2.0-PN...v1.5.2.1-PN
[1.5.2.0-PN]: https://github.com/PowerNukkit/PowerNukkit/compare/v1.5.1.0-PN...v1.5.2.0-PN
[1.5.1.0-PN]: https://github.com/PowerNukkit/PowerNukkit/compare/v1.5.0.0-PN...v1.5.1.0-PN
[1.5.0.0-PN]: https://github.com/PowerNukkit/PowerNukkit/compare/v1.4.0.0-PN...v1.5.0.0-PN
[1.4.0.0-PN]: https://github.com/PowerNukkit/PowerNukkit/compare/v1.3.1.5-PN...v1.4.0.0-PN
[1.3.1.5-PN]: https://github.com/PowerNukkit/PowerNukkit/compare/v1.3.1.4-PN...v1.3.1.5-PN
[1.3.1.4-PN]: https://github.com/PowerNukkit/PowerNukkit/compare/v1.3.1.3-PN...v1.3.1.4-PN
[1.3.1.3-PN]: https://github.com/PowerNukkit/PowerNukkit/compare/v1.3.1.2-PN...v1.3.1.3-PN
[1.3.1.2-PN]: https://github.com/PowerNukkit/PowerNukkit/compare/v1.3.1.1-PN...v1.3.1.2-PN
[1.3.1.1-PN]: https://github.com/PowerNukkit/PowerNukkit/compare/v1.3.1.0-PN...v1.3.1.1-PN
[1.3.1.0-PN]: https://github.com/PowerNukkit/PowerNukkit/compare/v1.3.0.1-PN...v1.3.1.0-PN
[1.3.0.1-PN]: https://github.com/PowerNukkit/PowerNukkit/compare/v1.3.0.0-PN...v1.3.0.1-PN
[1.3.0.0-PN]: https://github.com/PowerNukkit/PowerNukkit/compare/v1.2.1.0-PN...v1.3.0.1-PN
[1.3.0.0-PN]: https://github.com/PowerNukkit/PowerNukkit/compare/v1.2.1.0-PN...v1.3.0.0-PN
[1.2.1.0-PN]: https://github.com/PowerNukkit/PowerNukkit/compare/v1.2.0.2-PN...v1.2.1.0-PN
[1.2.0.2-PN]: https://github.com/PowerNukkit/PowerNukkit/compare/v1.2.0.1-PN...v1.2.0.2-PN
[1.2.0.1-PN]: https://github.com/PowerNukkit/PowerNukkit/compare/v1.2.0.0-PN...v1.2.0.1-PN
[1.2.0.0-PN]: https://github.com/PowerNukkit/PowerNukkit/compare/v1.1.1.0-PN...v1.2.0.0-PN
[1.1.1.0-PN]: https://github.com/PowerNukkit/PowerNukkit/compare/1ac6d50d36f07b6f1a02df299d9591d78c379db9...v1.1.1.0-PN#files_bucket

[a8247360]: https://github.com/PowerNukkit/PowerNukkit/commit/a8247360

[NukkitX]: https://github.com/CloudburstMC/Nukkit

[#12]: https://github.com/PowerNukkit/PowerNukkit/issues/12
[#44]: https://github.com/PowerNukkit/PowerNukkit/issues/44
[#46]: https://github.com/PowerNukkit/PowerNukkit/issues/46
[#49]: https://github.com/PowerNukkit/PowerNukkit/pull/49
[#50]: https://github.com/PowerNukkit/PowerNukkit/pull/50
[#51]: https://github.com/PowerNukkit/PowerNukkit/pull/51
[#52]: https://github.com/PowerNukkit/PowerNukkit/pull/52
[#53]: https://github.com/PowerNukkit/PowerNukkit/pull/53
[#54]: https://github.com/PowerNukkit/PowerNukkit/pull/54
[#55]: https://github.com/PowerNukkit/PowerNukkit/pull/55
[#56]: https://github.com/PowerNukkit/PowerNukkit/pull/56
[#57]: https://github.com/PowerNukkit/PowerNukkit/pull/57
[#58]: https://github.com/PowerNukkit/PowerNukkit/pull/58
[#79]: https://github.com/PowerNukkit/PowerNukkit/issues/79
[#80]: https://github.com/PowerNukkit/PowerNukkit/pull/80
[#87]: https://github.com/PowerNukkit/PowerNukkit/issues/87
[#93]: https://github.com/PowerNukkit/PowerNukkit/issues/93
[#95]: https://github.com/PowerNukkit/PowerNukkit/issues/95
[#102]: https://github.com/PowerNukkit/PowerNukkit/pull/102
[#103]: https://github.com/PowerNukkit/PowerNukkit/issues/103
[#108]: https://github.com/PowerNukkit/PowerNukkit/pull/108
[#113]: https://github.com/PowerNukkit/PowerNukkit/issues/113
[#116]: https://github.com/PowerNukkit/PowerNukkit/issues/116
[#123]: https://github.com/PowerNukkit/PowerNukkit/issues/123
[#129]: https://github.com/PowerNukkit/PowerNukkit/pull/129
[#140]: https://github.com/PowerNukkit/PowerNukkit/pull/140
[#152]: https://github.com/PowerNukkit/PowerNukkit/pull/152
[#157]: https://github.com/PowerNukkit/PowerNukkit/issues/157
[#170]: https://github.com/PowerNukkit/PowerNukkit/pull/170
[#193]: https://github.com/PowerNukkit/PowerNukkit/issues/193
[#210]: https://github.com/PowerNukkit/PowerNukkit/issues/210
[#212]: https://github.com/PowerNukkit/PowerNukkit/issues/212
[#220]: https://github.com/PowerNukkit/PowerNukkit/issues/220
[#219]: https://github.com/PowerNukkit/PowerNukkit/pull/219
[#222]: https://github.com/PowerNukkit/PowerNukkit/issues/223
[#224]: https://github.com/PowerNukkit/PowerNukkit/pull/224
[#226]: https://github.com/PowerNukkit/PowerNukkit/issues/226
[#227]: https://github.com/PowerNukkit/PowerNukkit/pull/227
[#228]: https://github.com/PowerNukkit/PowerNukkit/issues/228
[#232]: https://github.com/PowerNukkit/PowerNukkit/issues/232
[#234]: https://github.com/PowerNukkit/PowerNukkit/issues/234
[#235]: https://github.com/PowerNukkit/PowerNukkit/issues/235
[#239]: https://github.com/PowerNukkit/PowerNukkit/issues/239
[#240]: https://github.com/PowerNukkit/PowerNukkit/issues/240
[#242]: https://github.com/PowerNukkit/PowerNukkit/pull/242
[#243]: https://github.com/PowerNukkit/PowerNukkit/issues/243
[#244]: https://github.com/PowerNukkit/PowerNukkit/pull/244
[#246]: https://github.com/PowerNukkit/PowerNukkit/issues/246
[#247]: https://github.com/PowerNukkit/PowerNukkit/pull/247
[#248]: https://github.com/PowerNukkit/PowerNukkit/pull/248
[#253]: https://github.com/PowerNukkit/PowerNukkit/pull/253
[#254]: https://github.com/PowerNukkit/PowerNukkit/issues/254
[#255]: https://github.com/PowerNukkit/PowerNukkit/pull/255
[#256]: https://github.com/PowerNukkit/PowerNukkit/pull/256
[#259]: https://github.com/PowerNukkit/PowerNukkit/pull/259
[#260]: https://github.com/PowerNukkit/PowerNukkit/pull/260
[#261]: https://github.com/PowerNukkit/PowerNukkit/pull/261
[#262]: https://github.com/PowerNukkit/PowerNukkit/pull/262
[#263]: https://github.com/PowerNukkit/PowerNukkit/pull/263
[#266]: https://github.com/PowerNukkit/PowerNukkit/issues/266
[#267]: https://github.com/PowerNukkit/PowerNukkit/issues/267
[#268]: https://github.com/PowerNukkit/PowerNukkit/pull/268
[#270]: https://github.com/PowerNukkit/PowerNukkit/issues/270
[#272]: https://github.com/PowerNukkit/PowerNukkit/issues/272
[#273]: https://github.com/PowerNukkit/PowerNukkit/pull/273
[#274]: https://github.com/PowerNukkit/PowerNukkit/pull/274
[#275]: https://github.com/PowerNukkit/PowerNukkit/pull/275
[#276]: https://github.com/PowerNukkit/PowerNukkit/pull/276
[#277]: https://github.com/PowerNukkit/PowerNukkit/pull/277
[#279]: https://github.com/PowerNukkit/PowerNukkit/pull/279
[#281]: https://github.com/PowerNukkit/PowerNukkit/pull/281
[#285]: https://github.com/PowerNukkit/PowerNukkit/pull/285
[#287]: https://github.com/PowerNukkit/PowerNukkit/issues/287
[#293]: https://github.com/PowerNukkit/PowerNukkit/pull/293
[#297]: https://github.com/PowerNukkit/PowerNukkit/pull/297
[#298]: https://github.com/PowerNukkit/PowerNukkit/issues/298
[#315]: https://github.com/PowerNukkit/PowerNukkit/pull/315
[#319]: https://github.com/PowerNukkit/PowerNukkit/pull/319
[#320]: https://github.com/PowerNukkit/PowerNukkit/pull/320
[#323]: https://github.com/PowerNukkit/PowerNukkit/issues/323
[#326]: https://github.com/PowerNukkit/PowerNukkit/pull/326
[#328]: https://github.com/PowerNukkit/PowerNukkit/issues/326
[#330]: https://github.com/PowerNukkit/PowerNukkit/issues/330
[#335]: https://github.com/PowerNukkit/PowerNukkit/issues/335
[#338]: https://github.com/PowerNukkit/PowerNukkit/issues/338
[#339]: https://github.com/PowerNukkit/PowerNukkit/issues/339
[#340]: https://github.com/PowerNukkit/PowerNukkit/issues/340
[#344]: https://github.com/PowerNukkit/PowerNukkit/issues/344
[#346]: https://github.com/PowerNukkit/PowerNukkit/issues/346
[#347]: https://github.com/PowerNukkit/PowerNukkit/issues/347
[#348]: https://github.com/PowerNukkit/PowerNukkit/issues/348
[#352]: https://github.com/PowerNukkit/PowerNukkit/issues/352
[#359]: https://github.com/PowerNukkit/PowerNukkit/issues/359
[#365]: https://github.com/PowerNukkit/PowerNukkit/issues/365
[#366]: https://github.com/PowerNukkit/PowerNukkit/issues/366
[#368]: https://github.com/PowerNukkit/PowerNukkit/issues/368
[#390]: https://github.com/PowerNukkit/PowerNukkit/issues/390
[#397]: https://github.com/PowerNukkit/PowerNukkit/issues/397
[#400]: https://github.com/PowerNukkit/PowerNukkit/issues/400
[#403]: https://github.com/PowerNukkit/PowerNukkit/issues/403
[#404]: https://github.com/PowerNukkit/PowerNukkit/issues/404
[#407]: https://github.com/PowerNukkit/PowerNukkit/issues/407
[#412]: https://github.com/PowerNukkit/PowerNukkit/issues/412
[#414]: https://github.com/PowerNukkit/PowerNukkit/issues/414
[#422]: https://github.com/PowerNukkit/PowerNukkit/issues/422
[#427]: https://github.com/PowerNukkit/PowerNukkit/issues/427
[#430]: https://github.com/PowerNukkit/PowerNukkit/issues/430
[#433]: https://github.com/PowerNukkit/PowerNukkit/issues/433
[#436]: https://github.com/PowerNukkit/PowerNukkit/issues/436
[#437]: https://github.com/PowerNukkit/PowerNukkit/issues/437
[#440]: https://github.com/PowerNukkit/PowerNukkit/issues/440
[#443]: https://github.com/PowerNukkit/PowerNukkit/issues/443
[#445]: https://github.com/PowerNukkit/PowerNukkit/issues/445
[#449]: https://github.com/PowerNukkit/PowerNukkit/issues/449
[#450]: https://github.com/PowerNukkit/PowerNukkit/issues/450
[#462]: https://github.com/PowerNukkit/PowerNukkit/issues/462
[#464]: https://github.com/PowerNukkit/PowerNukkit/issues/464
[#467]: https://github.com/PowerNukkit/PowerNukkit/issues/467
[#469]: https://github.com/PowerNukkit/PowerNukkit/issues/469
[#475]: https://github.com/PowerNukkit/PowerNukkit/issues/475
[#544]: https://github.com/PowerNukkit/PowerNukkit/issues/544
[#576]: https://github.com/PowerNukkit/PowerNukkit/issues/576
[#625]: https://github.com/PowerNukkit/PowerNukkit/issues/625
[#669]: https://github.com/PowerNukkit/PowerNukkit/issues/669
[#702]: https://github.com/PowerNukkit/PowerNukkit/issues/702
[#765]: https://github.com/PowerNukkit/PowerNukkit/issues/765
[#766]: https://github.com/PowerNukkit/PowerNukkit/issues/766
[#770]: https://github.com/PowerNukkit/PowerNukkit/issues/770
[#776]: https://github.com/PowerNukkit/PowerNukkit/issues/776
[#777]: https://github.com/PowerNukkit/PowerNukkit/issues/777
[#778]: https://github.com/PowerNukkit/PowerNukkit/issues/778
[#783]: https://github.com/PowerNukkit/PowerNukkit/issues/783
[#857]: https://github.com/PowerNukkit/PowerNukkit/issues/857
[#882]: https://github.com/PowerNukkit/PowerNukkit/issues/882
[#902]: https://github.com/PowerNukkit/PowerNukkit/issues/902
[#917]: https://github.com/PowerNukkit/PowerNukkit/issues/917
[#959]: https://github.com/PowerNukkit/PowerNukkit/issues/959
[#960]: https://github.com/PowerNukkit/PowerNukkit/issues/960
[#979]: https://github.com/PowerNukkit/PowerNukkit/issues/979
[#982]: https://github.com/PowerNukkit/PowerNukkit/issues/982
[#990]: https://github.com/PowerNukkit/PowerNukkit/issues/990
[#1100]: https://github.com/PowerNukkit/PowerNukkit/issues/1100
[#1103]: https://github.com/PowerNukkit/PowerNukkit/issues/1103
[#1107]: https://github.com/PowerNukkit/PowerNukkit/issues/1107
[#1119]: https://github.com/PowerNukkit/PowerNukkit/issues/1119
[#1120]: https://github.com/PowerNukkit/PowerNukkit/issues/1120
[#1122]: https://github.com/PowerNukkit/PowerNukkit/issues/1122
[#1130]: https://github.com/PowerNukkit/PowerNukkit/issues/1130
[#1132]: https://github.com/PowerNukkit/PowerNukkit/issues/1132
[#1134]: https://github.com/PowerNukkit/PowerNukkit/issues/1134
[#1139]: https://github.com/PowerNukkit/PowerNukkit/issues/1139
[#1146]: https://github.com/PowerNukkit/PowerNukkit/issues/1146
[#1147]: https://github.com/PowerNukkit/PowerNukkit/issues/1147
[#1149]: https://github.com/PowerNukkit/PowerNukkit/issues/1149
[#1150]: https://github.com/PowerNukkit/PowerNukkit/issues/1150
[#1151]: https://github.com/PowerNukkit/PowerNukkit/issues/1151
[#1120]: https://github.com/PowerNukkit/PowerNukkit/issues/1120
[#1153]: https://github.com/PowerNukkit/PowerNukkit/issues/1153
[#1170]: https://github.com/PowerNukkit/PowerNukkit/issues/1170
[#1172]: https://github.com/PowerNukkit/PowerNukkit/issues/1172
[#1174]: https://github.com/PowerNukkit/PowerNukkit/issues/1174
[#1177]: https://github.com/PowerNukkit/PowerNukkit/issues/1177
[#1187]: https://github.com/PowerNukkit/PowerNukkit/issues/1187
[#1191]: https://github.com/PowerNukkit/PowerNukkit/issues/1191
[#1193]: https://github.com/PowerNukkit/PowerNukkit/issues/1193
[#1202]: https://github.com/PowerNukkit/PowerNukkit/issues/1202
[#1214]: https://github.com/PowerNukkit/PowerNukkit/issues/1214
[#1233]: https://github.com/PowerNukkit/PowerNukkit/issues/1233
[#1244]: https://github.com/PowerNukkit/PowerNukkit/issues/1244
[#1216]: https://github.com/PowerNukkit/PowerNukkit/issues/1216
[#1258]: https://github.com/PowerNukkit/PowerNukkit/issues/1258
[#1266]: https://github.com/PowerNukkit/PowerNukkit/issues/1266
[#1267]: https://github.com/PowerNukkit/PowerNukkit/issues/1267
[#1270]: https://github.com/PowerNukkit/PowerNukkit/issues/1270
[#1341]: https://github.com/PowerNukkit/PowerNukkit/issues/1341
[#1343]: https://github.com/PowerNukkit/PowerNukkit/issues/1343
[#1344]: https://github.com/PowerNukkit/PowerNukkit/issues/1344
