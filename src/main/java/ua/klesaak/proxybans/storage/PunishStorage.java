package ua.klesaak.proxybans.storage;

import ua.klesaak.proxybans.manager.ProxyBansManager;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class PunishStorage implements AutoCloseable {
    private final Map<String, PunishData> bansCache = new ConcurrentHashMap<>();
    private final Map<String, PunishData> mutesCache = new ConcurrentHashMap<>();
    private final Map<String, List<PunishData>> historyCache = new ConcurrentHashMap<>();
    private final ProxyBansManager proxyBansManager;

    public PunishStorage(ProxyBansManager proxyBansManager) {
        this.proxyBansManager = proxyBansManager;
    }

    public abstract void saveBan(PunishData punishData);
    public abstract void saveMute(PunishData punishData);
    public abstract void addHistory(PunishData punishData);

    public abstract void unBan(String nickName);
    public abstract void unMute(String nickName);
    public abstract void clearHistory(String nickName);

    public abstract void unBanAllBy(String nickName);
    public abstract void unMuteAllBy(String nickName);

    public abstract PunishData getBanData(String nickName);
    public abstract PunishData getMuteData(String nickName);
    public abstract List<PunishData> getHistoryData(String nickName);
    @Override
    public abstract void close();
}
