# Changelog
## 3.1.0
Because some people wondered in which chests their stuff went into, I have added this:
- Added a "laser" beam that will point to the affected chests for a few second.
- The laser beam can be disabled in the config.yml and the duration can be set.
- You can view the laser later again by running /unloadinfo or /dumpinfo.
- You can also overwrite the default laser default duration with /unloadinfo [duration] or /dumpinfo [duration]
- You can also set laser-moves-with-player to true to have the lasers move when the player moves

Further bugfixes:
- /unload is properly executed before /dump 

## 3.0.1
- Fixed: /unload now properly ignores the hotbar
- Fixed: Prevents Minepacks bagpacks from being put into chests
- Added (almost complete) Spanish translation

## 3.0.0
- Rewrote plugin from scratch. Source code is now muuuuch cleaner and faster
- Unload/dump now works with chests, double chests, shulkerboxes and barrels
- InvUnload should now work with every protection plugin like WorldGuard, GriefPrevention, etc. by calling an InventoryOpenEvent. InvUnload will only put items into chests if that event is not cancelled by other plugins.
- /dump will no longer take items from the player's hotbar
- When using /dump, InvUnload will always run /unload first. The config option has been removed.
- Permissions "invunload.unload" and "invunload.dump" have been renamed to "invunload.use"
- InvUnload can use ChestSort to sort chests that have been affected by unloading/dumping. This can be disabled in the config and only works if the player has automatic chest sorting enabled
- Particle types and sound effects can be set in the config.yml
- Update-Checker interval is now configurable in the config.yml