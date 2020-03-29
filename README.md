# pHD
An intelligent manager for HolographicDisplays

### Overview
**pHD** manages the display of holograms defined by **[HolographicDisplays](https://dev.bukkit.org/projects/holographic-displays)**.
It adds fine control over hologram visibility and duration, plus permission-based restrictions.
**pHD** does not modify the content of the holograms themselves.
See [Technical Details](https://github.com/SlimeDog/pHD/wiki/Technical-Details) for more information.

**pHD** manages hologram visibility based on a combination of display types and options.
Types include per-player limitation on the number of views, and activation at specific Minecraft or real-world times of day. Options include the visibility duration, acquisition distance, flashing, and permissions restrictions.
Multiple types and options may be associated with any hologram.
See [Types & Options](https://github.com/SlimeDog/pHD/wiki/Types-&-Options) for supported types and options.

All **pHD** commands are accessible at the console, and in-game with appropriate permissions (default OP). Tab-completion is supported for all commands.
If **LuckPerms** (5.0 or higher) is installed, tab-completion is supported for permissions in **pHD** commands.

All **pHD** messages may be localized. Message changes take effect on plugin reload or server restart.

Supported data storage types include SQLITE (default) and YAML. Conversion between storage types is supported.

***

### Version Support
**pHD 1.0.x** is certified for **Spigot** **1.13.2**, **1.14.4**, and **1.15.2**; with **[HolographicDisplays 2.4.1](https://dev.bukkit.org/projects/holographic-displays/files)**.

Based on _ad hoc_ usage,
**HolographicDisplays 2.4.2 development** releases are also compatible, but they are not certified.

**pHD** may or may not work on previous Spigot releases or non-Spigot variants.

**HolographicDisplays 2.3.2** and **HolographicDisplays 2.4.0** should work with compatible Spigot releases,
but only **HolographicDisplays 2.4.1** has been certified.

&#128681; Only certified releases are supported.
