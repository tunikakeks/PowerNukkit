# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html) 
with an added upstream's major version number in front of the major version so we have an better distinction from
Nukkit 1.X and 2.X.

## [Unreleased]
Click the link above to see the future.

### Added
- [#136] This change log file
-Implementation for the entities (without AI):
    - [#128] Lingering potion
    - [#128] Area effect cloud
- Implementation for the blocks:
    - [#146] Barrel
    - [#135] Barrier 
    - [#143] Blue Ice
    - [#162] Dried Kelp Block
    - [#149] Smooth Stone
    - [#160] Kelp
    - [#154] Acacia, Birch, Dark Oak, Jungle, Spruce Trapdoors
    - [#163] Acacia, Birch, Dark Oak, Jungle, Spruce Buttons

### Fixes
- [#131] Instant damage and instant health are now inverted when applied to undead entities
- [#132] A collision detection issue on Area Effect Cloud which could make it wears off way quicker than it should
- [#147] Block entities not being saved before being serialized to the LevelDB format
- [#155] The condition which ice blocks becomes water when broken
- [#165] The dropped button's damage value when a button is broken
- [#166] The stone_button break time and dropping without pickaxe
- [#158] Trapdoors behaving incorrectly when they receive redstone signal

### Changed
- [#136] The versioning convention now follows this pattern:<br>`upstream.major.minor.patch-PN`<br>[Click here for details.](https://github.com/GameModsBR/PowerNukkit/blob/7912aa4be68e94a52762361c2d5189b7bbc58d2a/pom.xml#L8-L14)
- [#136] The codename to PowerNukkit to distinct from [NukkitX]'s implementation

### 1.X to 2.X
A lot has been changed from 1.X to 2.X, please refer to [this topic](https://nukkitx.com/threads/nukkit-2-0-alpha.602/)
for details.

[Unreleased]: https://github.com/GameModsBR/PowerNukkit/compare/477db9d7c3dfa4182c3e73d0aec9744ccd7fb658...2.0-migration

[NukkitX]: https://github.com/NukkitX/Nukkit

[#128]: https://github.com/GameModsBR/PowerNukkit/pull/128
[#131]: https://github.com/GameModsBR/PowerNukkit/pull/131
[#132]: https://github.com/GameModsBR/PowerNukkit/pull/132
[#135]: https://github.com/GameModsBR/PowerNukkit/pull/135
[#136]: https://github.com/GameModsBR/PowerNukkit/pull/136
[#143]: https://github.com/GameModsBR/PowerNukkit/pull/143
[#146]: https://github.com/GameModsBR/PowerNukkit/pull/146
[#147]: https://github.com/GameModsBR/PowerNukkit/pull/147
[#149]: https://github.com/GameModsBR/PowerNukkit/pull/149
[#154]: https://github.com/GameModsBR/PowerNukkit/pull/154
[#155]: https://github.com/GameModsBR/PowerNukkit/pull/155
[#158]: https://github.com/GameModsBR/PowerNukkit/pull/158
[#160]: https://github.com/GameModsBR/PowerNukkit/pull/160
[#162]: https://github.com/GameModsBR/PowerNukkit/pull/162
[#163]: https://github.com/GameModsBR/PowerNukkit/pull/163
[#165]: https://github.com/GameModsBR/PowerNukkit/pull/165
[#166]: https://github.com/GameModsBR/PowerNukkit/pull/166
