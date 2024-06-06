<table border=1><tr><td>
<h2>🛑 End Of Life for SlimeDog/pHD</h2>

SlimeDog/pHD has reached end-of-life

Completed tasks:
- Updated SlimeDog/pHD to support Paper and Spigot 1.20.6 and 1.21.0
- Posted update to SpigotMC
- Updated `Version Support` page of SlimeDog/pHD wiki on Github
- Posted end-of-life notices
  - `Overview` page of SlimeDog/pHD on SpigotMC
  - `README` for SlimeDog/pHD repository on Github
  - `Home` page of SlimeDog/pHD wiki on Github
- Archived SlimeDog/pHD Github repository
- Removed SlimeDog/pHD from bStats
- Removed SlimeDog/pHD from Hangar

Support for future Minecraft versions:
- SlimeDog/pHD will likely not work on future Minecraft versions without updates
- Anyone may fork pHD to support future Minecraft versions, or for any other purpose permitted under the GPL3 license
</td></tr></table>

# pHD &mdash; Manage holograms with intelligence

### Overview
Vanilla hologram providers display holograms whenever a player is within the standard Minecraft view distance.
pHD adds capabilities to manage individual hologram visibility.
It adds fine control over hologram visibility and duration, plus permission-based restrictions.
pHD does not modify the content of the holograms themselves.

pHD manages hologram visibility based on a combination of display types and options.
Types include per-player limitation on the number of views, and activation at specific Minecraft or real-world times of day. Options include the visibility duration, acquisition distance, flashing, and permissions restrictions.
Multiple types and options may be associated with any hologram.

All pHD commands are accessible at the console, and in-game with appropriate permissions (default OP). Tab-completion is supported for all commands.
If LuckPerms (5.0 or higher) is installed, tab-completion is supported for permissions in pHD commands.

All pHD messages may be localized. Message changes take effect on plugin reload or server restart.

Supported data storage types include SQLITE (default) and YAML. Conversion between storage types is supported.

### The Wiki
pHD is completely documented on The Wiki. Please start there when you have questions.
https://github.com/SlimeDog/pHD/wiki

### How to build with the ever-changing version of HolographicDisplays
```
The main issue is that filoghost keeps releasing the same version (3.0.0-SNAPSHOT) even though they're adding
functionality (i.e the stuff that enabled our implementation to manage their holograms' visibility).
While they do publish them with a different version on dev.bukkit, they do not actually change the version in
their pom.xml, nor do they install it to the maven repository (which is somewhat expected if the version isn't bumped).

So IMHO there's one of two things they can do:
a) Stop releasing "build" versions which change functionality
b) Install the different "build" versions to the maven repository

In the meanwhile, what you can do:
1) Download a "build" version of the plugin
- something that's compatible with our changes
- I'm sure you remember the number better than I do
- looks like I've used build 197, but anything greater than that should work
- and if it doesn't we'd have to revisit the issues since they would also pop up in production
2) Install it to your local maven repository manually
- mvn install:install-file -Dfile=<jar file target> -DgroupId=me.filoghost.holographicdisplays \
        -DartifactId=holographicdisplays-local -Dversion=3.0.0-SNAPSHOT-b197 -Dpackaging=jar -DgeneratePom=true
- Make sure to change <jar file target> to the jar file you're installing
```
