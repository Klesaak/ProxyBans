package ua.klesaak.proxybans.permshook;

public class MinePermsHook implements IPermHook {


    public MinePermsHook() {

    }

    @Override
    public String getUserGroup(String nickName) {
        return "default";
    }

    @Override
    public boolean hasPermission(String nickName, String permission) {
        return false;
    }
}
