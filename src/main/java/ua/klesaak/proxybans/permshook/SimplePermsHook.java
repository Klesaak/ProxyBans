package ua.klesaak.proxybans.permshook;

import lombok.val;
import ru.Den_Abr.SimplePerms.SimplePermsCommon;
import ua.klesaak.proxybans.ProxyBansPlugin;

public final class SimplePermsHook implements IPermHook {

    public SimplePermsHook(ProxyBansPlugin proxyBansPlugin) {
        proxyBansPlugin.getLogger().info("Loaded permission provider: " + this.getClass().getSimpleName());
    }

    @Override
    public String getUserGroup(String nickName) {
        return SimplePermsCommon.getInstance().getPermissionManager().getUser(nickName).getUserGroup().getId();
    }

    @Override
    public boolean hasPermission(String nickName, String permission) {
        val manager = SimplePermsCommon.getInstance().getPermissionManager();
        return manager.has(manager.getUser(nickName), permission);
    }
}
