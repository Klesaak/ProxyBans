package ua.klesaak.proxybans.permshook;

import com.google.common.collect.Iterables;
import net.md_5.bungee.api.ProxyServer;
import ru.Den_Abr.SimplePerms.SimplePermsCommon;

public class SimplePermsHook implements IPermHook {

    public SimplePermsHook() {
    }

    @Override
    public String getUserGroup(String nickName) {
        return ProxyServer.getInstance().getPlayer(nickName) != null ? Iterables.getFirst(ProxyServer.getInstance().getPlayer(nickName).getGroups(), "default")
                : SimplePermsCommon.getInstance().getPermissionManager().getUser(nickName).getUserGroup().getId();
    }

    @Override
    public boolean hasPermission(String nickName, String permission) {
        return SimplePermsCommon.getInstance().getPermissionManager().has(SimplePermsCommon.getInstance().getPermissionManager().getUser(nickName), permission);
    }
}
