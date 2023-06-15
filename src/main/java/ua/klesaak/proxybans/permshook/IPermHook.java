package ua.klesaak.proxybans.permshook;

public interface IPermHook {
    String getUserGroup(String nickName);
    boolean hasPermission(String nickName, String permission);
}
