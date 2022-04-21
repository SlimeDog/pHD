package me.ford.periodicholographicdisplays.holograms.wrap.visibility;

import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import eu.decentsoftware.holograms.api.holograms.Hologram;
import eu.decentsoftware.holograms.api.holograms.enums.EnumFlag;

public class DecentHologramVisibilitySettings implements VisibilitySettings {
    private static final org.bukkit.plugin.java.JavaPlugin PLUGIN = org.bukkit.plugin.java.JavaPlugin
            .getProvidingPlugin(DecentHologramVisibilitySettings.class);
    private static final PermissionHandler PERM_HANDLER = new PermissionHandler();
    private static final String PERM_BASE = "PhD.PERM.";
    private final Hologram delegate;
    private final String permName;
    private final Permission permission;
    private final boolean phdPerm;

    public DecentHologramVisibilitySettings(Hologram delegate) {
        this.delegate = delegate;
        if (delegate.getPermission() == null) {
            delegate.setPermission(permName = getDefaultPermission());
        } else {
            permName = delegate.getPermission();
        }
        Permission perm = PLUGIN.getServer().getPluginManager().getPermission(permName);
        phdPerm = perm == null || perm.getName().startsWith(PERM_BASE);
        if (perm != null) {
            permission = perm;
        } else {
            permission = new Permission(permName, PermissionDefault.FALSE);
        }
        delegate.addFlags(EnumFlag.DISABLE_UPDATING);
    }

    @Override
    public void setGlobalVisibility(VisibilityState setting) {
        if (setting == VisibilityState.VISIBLE) {
            // delegate.showAll();
            this.permission.setDefault(PermissionDefault.TRUE);
        } else {
            // delegate.hideAll();
            this.permission.setDefault(PermissionDefault.FALSE);
        }
        delegate.updateAll();
    }

    @Override
    public void clearIndividualVisibilities() {
        delegate.showAll(); // by default
        permission.setDefault(PermissionDefault.TRUE);
        delegate.updateAll();
    }

    @Override
    public void setIndividualVisibility(Player player, VisibilityState setting) {
        PERM_HANDLER.setPermission(player, permission, setting == VisibilityState.VISIBLE);
    }

    public boolean hasPhdPerm() {
        return phdPerm;
    }

    private String getDefaultPermission() {
        return PERM_BASE + delegate.getName();
    }

}
