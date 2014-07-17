Dungeon-Crawler
===============

A randomly generated dungeon crawler made in Java (Requires Java 1.7).

===============

*Note: The term mob includes the player, as well as all other creatures.*

**Current Features:**
+ Random dungeon generation.
+ Place custom, user made rooms and settlements in dungeon.
+ Mobs specific to settlement will occupy settlement.
+ Mobs wander dungeon.
+ Check to see if dungeon is completable (I don't care if its not a word).
+ Tiles imported from .xml file.
+ Entity data imported from .xml file.
+ Buggy feild of view. 
  + Saves explored tiles.
  + Unexplored tiles are in shadow.

**Todo List:**
- [ ] Implement seeds into worldgen.
- [ ] Improve custom room/settlement creation/save/import.
  - [ ] Custom room editor/exporter.
  - [ ] Separate to main game.
- [ ] Improve settlement AI.
  - [ ] Mobs need to "live" in settlements.
  - [ ] Claim beds/sleep.
  - [ ] Defend against attackers.
  - [ ] Meeding rooms.
- [ ] Combat.
- [ ] Player stats.
- [ ] Implement objects.
- [ ] Mobs can decide to join/follow player, become allies.
  - [ ] Allies defend player.
- [ ] Shadow falloff from FOV.
- [ ] Basic Fluids
  - [ ] Fluids make floor wet/slippery.
  - [ ] Can push items/mobs.
  - [ ] Slow mobs down.
  - [ ] Lava/water.
  - [ ] Fluid temperature.
- [ ] Cave systems.  
  - [ ] Specific mobs live/spawn in caves.
  - [ ] Plants could grow.
- [ ] Karma.
  - [ ] Good or evil meter.
  - [ ] All mobs affected.
  - [ ] Karma level determines mobs actions.
  - [ ] Karma is affected by mob actions.
  - [ ] Mobs will dislike mobs with opposite karma.
- [ ] Quests

===============

**Objects:**
+ Things like doors, tables, chests, beds, chairs, wells.
+ Object placement in settlements is based of .json file, not randomly generated.
+ Objects not in settlements are randomly generated.
+ Doors are placed at room connection points. 
  + Not all connection points will have doors.
  + Some doors could be locked?
    + May cause problems by creating unpathable dungeons.
    + Locked doors could be in settlements.
    + Keys may be scattered around dungeon.
    + Some doors could be picked.
+ Mobs can interact with objects.
+ Some objects are moveable. Tables, chairs, etc.
+ Loading object details same as loading entity details.

**Combat:**
+ I don't have a clue on how to combat.
+ Don't want to have: walk up to mob, mash keys till dead.
+ Don't want to have insane, Dwarf Fortress style, detailed combat.
+ HELP!
+ Mobs with vastly different karma values will fight eachother. 
  + Combat wont be as detailed as player v mob combat.
+ Mob combat stats come from entity.xml file.

**Fluids:**
+ Fluid has height, type & temperature.
  + Hight determines how much of the fluid there is in a single tile.
  + Type is what type of fluid is is.
  + Temperature is self explanatory.
+ Height:
  + Levels 1-5.
    + 1 is a small puddle & 5 is an entire tile of the fluid.
  + If there is an empty tile or a tile with less than 5 adjacent to a fluid tile. And the fluid tile has more than 1. Then the fluid can spread to an adjacent tile.
  + Fluid will continue to spread until there is nowhere else to go or the fluid tiles all become 1.
+ Type:
  + Depending on the type of fluid. It will evaporate over time.
  + If lava and water connect, they could form a solid block that mobs can break.
+ Temperature:
  + Hot water will become steam. (No need for steam physics, just have evaporate)
  + Fluid on the temperature, all tiles adjacent to fluid tile will be heated or cooled down.
  + Items, objects, mobs will burn if fluid is too hot.
+ Preasure?
 
===============
