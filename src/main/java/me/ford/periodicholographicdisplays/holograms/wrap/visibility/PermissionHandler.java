package me.ford.periodicholographicdisplays.holograms.wrap.visibility;

import org.bukkit.permissions.Permissible;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;

public class PermissionHandler {
    private static final org.bukkit.plugin.java.JavaPlugin PLUGIN = org.bukkit.plugin.java.JavaPlugin
            .getProvidingPlugin(DecentHologramVisibilitySettings.class);

    public void setPermission(Permissible permissible, Permission permission, boolean value) {
        PermissionAttachment perm = getOrCreateAttachment(permissible, permission, value);
        perm.setPermission(permission, value);
    }

    private PermissionAttachment getOrCreateAttachment(Permissible permissible, Permission permission, boolean value) {
        for (PermissionAttachmentInfo info : permissible.getEffectivePermissions()) {
            if (info.getPermission().equals(permission.getName())) {
                return info.getAttachment();
            }
        }
        return permissible.addAttachment(PLUGIN, permission.getName(), value);
    }

}
