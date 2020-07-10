# Changelog

## 4.4.0
- Seperate permissions for /unload and /dump
- Updated Spanish translation
- Fixed items being counted twice in double chests when using /search
- Fixed /search not working when "always-show-summary" was set to false

## 4.3.0
- Added CoreProtect logging

## 4.2.2
- Works with 1.16
- Improved PlotSquared Hook: You will no longer get messages that you cannot use a chest, instead InvUnload will just ignore chests protected by PlotSquared

## 4.2.1
- Fixed update checker again (sorry)

## 4.2.0
- Fixed weird config update problem regarding UTF8
- Added /searchitem command (permission: invunload.search, alias: /search)
- Added Material tab complete for /searchitem
- Made stuffPlayerInventoryIntoAnother() public for API access
- Improved UpdateChecker

NOTE: This update includes a new message in the config.yml, so please send me your new translations :)

## 4.1.0
- Added PlotSquared support. Players will only be allowed to unload into their own plot (configurable). Players will also be disallowed to unload outside of their own plots (configurable).
- Added groups.yml file. You can define custom max-radius and default-radius for different player groups. A player will need the invunload.groups.<groupname> permission. See the groups.example.yml file for syntax.
- Added support for Spartan Anti-Cheat

## 4.0.0
Because some people wondered in which chests their stuff went into, I have added this:
- Added a text summary that shows where all your items went. By default, it is shown every time you use /unload or /dump, but you can also disable it so that it is only shown when using /unloadinfo (or /dumpinfo).
- Added a "laser" beam that will point to the affected chests for a few second. You can view the laser by running /unloadinfo or /dumpinfo. You can also set the laser to be always shown when using /unload or /dump.
- You can also overwrite the default laser duration with /unloadinfo [duration] or /dumpinfo [duration]
- You can also set laser-moves-with-player to true to have the lasers move when the player moves
- Added a reload command (/unload reload) that requires the permission "invunload.reload".

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