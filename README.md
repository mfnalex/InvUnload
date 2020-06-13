# InvUnload

Automatically puts your stuff into the right chests

You don't have time to open every chest in your storage room to tidy up your inventory?
Fear no more! You have no more excuses for having chests full of random garbage!

<p align="center">
  <img src="https://api.jeff-media.de/invunload/spigotmc/invunload128.png"/>
</p>

InvUnload does two brilliant things:

When you enter /unload, it checks if there are chests nearby. For each chest, the player's inventory (except hotbar) will be searched for matching items. If there are any, they will be put into the chest.

When you enter /dump, it will put all items from the player's inventory (except hotbar) into nearby chests. If possible, they will be put into chests already containing matching items.
## Build

You will need to maven to build InvUnload. Just clone this repo and run ``mvn install``