package ua.klesaak.proxybans.storage.mysql;

import ua.klesaak.proxybans.manager.ProxyBansManager;
import ua.klesaak.proxybans.storage.PunishData;
import ua.klesaak.proxybans.storage.PunishStorage;

import java.util.List;

public class MySQLStorage extends PunishStorage {

    public MySQLStorage(ProxyBansManager proxyBansManager) {
        super(proxyBansManager);
    }

    @Override
    public void saveBan(PunishData punishData) {

    }

    @Override
    public void saveMute(PunishData punishData) {

    }

    @Override
    public void addHistory(String nickName, PunishData punishData) {

    }

    @Override
    public void unBan(String nickName) {

    }

    @Override
    public void unMute(String nickName) {

    }

    @Override
    public void clearHistory(String nickName) {

    }

    @Override
    public void unBanAllBy(String nickName) {

    }

    @Override
    public void unMuteAllBy(String nickName) {

    }

    @Override
    public PunishData getBanData(String nickName) {
        return null;
    }

    @Override
    public PunishData getMuteData(String nickName) {
        return null;
    }

    @Override
    public List<PunishData> getHistoryData(String nickName) {
        return null;
    }

    @Override
    public boolean isBanned(String nickName) {
        return false;
    }

    @Override
    public boolean isMuted(String nickName) {
        return false;
    }

    @Override
    public void close() {

    }
}
