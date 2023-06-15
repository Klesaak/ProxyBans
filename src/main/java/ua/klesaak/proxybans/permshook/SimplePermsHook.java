package ua.klesaak.proxybans.permshook;

public class SimplePermsHook implements IPermHook {


    public SimplePermsHook() {

    }

    @Override
    public String getUserGroup(String nickName) {
        return null;
    }

    @Override
    public boolean hasPermission(String nickName, String permission) {
        return false;
    }
}
