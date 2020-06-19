# InvUnload

Automatically puts your stuff into the right chests

You don't have time to open every chest in your storage room to tidy up your inventory?
Fear no more! You have no more excuses for having chests full of random garbage!

<p align="center">
  <img src="https://api.jeff-media.de/invunload/spigotmc/img/invunload128.png"/>
</p>

InvUnload does two brilliant things:

When you enter /unload, it checks if there are chests nearby. For each chest, the player's inventory (except hotbar) will be searched for matching items. If there are any, they will be put into the chest.

When you enter /dump, it will put all items from the player's inventory (except hotbar) into nearby chests. If possible, they will be put into chests already containing matching items.

## Build

You will need maven to build InvUnload. You will also have to download these dependencies:

- PlotSquared 4 ([LINK](https://www.spigotmc.org/resources/plotsquared-v4-v5-out-now.1177/))
- Spartan API ([LINK](https://vagdedes.com/spartan/api/SpartanAPI.jar)).

Just create a folder called "lib" inside the maven project and put those two .jar-files inside. No you can run ``mvn install``
