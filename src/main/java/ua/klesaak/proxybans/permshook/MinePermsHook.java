package ua.klesaak.proxybans.permshook;

import ua.klesaak.mineperms.api.MinePermsAPI;
import ua.klesaak.proxybans.ProxyBansPlugin;

public final class MinePermsHook implements IPermHook {

    public MinePermsHook(ProxyBansPlugin proxyBansPlugin) {
        proxyBansPlugin.getLogger().info("Loaded permission provider: " + this.getClass().getSimpleName());
    }

    @Override
    public String getUserGroup(String nickName) {
        return MinePermsAPI.getUserGroup(nickName);
    }

    @Override
    public boolean hasPermission(String nickName, String permission) {
        return MinePermsAPI.hasPermission(nickName, permission);
    }
}
