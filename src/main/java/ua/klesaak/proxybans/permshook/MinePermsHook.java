package ua.klesaak.proxybans.permshook;

import ua.klesaak.mineperms.api.MinePermsAPI;

public class MinePermsHook implements IPermHook {

    public MinePermsHook() {
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
