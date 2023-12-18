package ua.klesaak.proxybans.storage;

import lombok.Getter;
import ua.klesaak.proxybans.manager.ProxyBansManager;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public abstract class PunishStorage implements AutoCloseable {
    protected final Map<String, PunishData> bansCache = new ConcurrentHashMap<>();
    protected final Map<String, PunishData> mutesCache = new ConcurrentHashMap<>();
    protected final Map<String, List<PunishData>> historyCache = new ConcurrentHashMap<>();
    protected final ProxyBansManager proxyBansManager;

    public PunishStorage(ProxyBansManager proxyBansManager) {
        this.proxyBansManager = proxyBansManager;
    }

    public abstract void saveBan(PunishData punishData);
    public abstract void saveMute(PunishData punishData);
    public abstract void addHistory(String nickName, PunishData punishData);

    public abstract void unBan(String nickName);
    public abstract boolean unBanIsExpired(String nickName);
    public abstract void unMute(String nickName);
    public abstract boolean unMuteIsExpired(String nickName);
    public abstract void clearHistory(String nickName);

    public abstract void unBanAllBy(String nickName);
    public abstract void unMuteAllBy(String nickName);

    public abstract PunishData getBanData(String nickName);
    public abstract PunishData getMuteData(String nickName);
    public abstract List<PunishData> getHistoryData(String nickName);
    public abstract boolean isBanned(String nickName);
    public abstract boolean isMuted(String nickName);
    @Override
    public abstract void close();
}
