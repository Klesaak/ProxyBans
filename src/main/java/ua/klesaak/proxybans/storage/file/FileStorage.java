package ua.klesaak.proxybans.storage.file;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.val;
import ua.klesaak.proxybans.manager.ProxyBansManager;
import ua.klesaak.proxybans.storage.PunishData;
import ua.klesaak.proxybans.storage.PunishStorage;
import ua.klesaak.proxybans.utils.JacksonAPI;
import ua.klesaak.proxybans.utils.JsonData;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class FileStorage extends PunishStorage {
    public static final TypeReference<Collection<PunishData>> PUNISH_DATA_REFERENCE = new TypeReference<Collection<PunishData>>() {};
    private final JsonData bansFile, mutesFile, historyFile;

    public FileStorage(ProxyBansManager proxyBansManager) {
        super(proxyBansManager);
        val pluginDataFolder = proxyBansManager.getProxyBansPlugin().getDataFolder();
        this.bansFile = new JsonData(new File(pluginDataFolder, "bans.json"));
        this.mutesFile = new JsonData(new File(pluginDataFolder, "mutes.json"));
        this.historyFile = new JsonData(new File(pluginDataFolder, "history.json"));
        if (this.bansFile.getFile().length() > 0L) { //todo remove if expired temp ban
            Collection<PunishData> dataCollection = JacksonAPI.readFile(this.bansFile.getFile(), PUNISH_DATA_REFERENCE);
            dataCollection.forEach(punishData -> this.bansCache.put(punishData.getPlayerName(), punishData));
        }
        if (this.mutesFile.getFile().length() > 0L) { //todo remove if expired temp mute
            Collection<PunishData> dataCollection = JacksonAPI.readFile(this.mutesFile.getFile(), PUNISH_DATA_REFERENCE);
            dataCollection.forEach(punishData -> this.mutesCache.put(punishData.getPlayerName(), punishData));
        }
        //todo history
    }

    private void writeBansCache() {
        CompletableFuture.runAsync(() -> this.bansFile.write(this.bansCache.values())).exceptionally(throwable -> {
            throwable.printStackTrace();
            return null;
        });
    }

    private void writeMutesCache() {
        CompletableFuture.runAsync(() -> this.mutesFile.write(this.mutesCache.values())).exceptionally(throwable -> {
            throwable.printStackTrace();
            return null;
        });
    }

    private void writeHistoryCache() {
        CompletableFuture.runAsync(() -> this.historyFile.write(this.historyCache.values())).exceptionally(throwable -> {
            throwable.printStackTrace();
            return null;
        });
    }

    @Override
    public void saveBan(PunishData punishData) {
        this.bansCache.put(punishData.getPlayerName(), punishData);
        this.writeBansCache();
    }

    @Override
    public void saveMute(PunishData punishData) {
        this.mutesCache.put(punishData.getPlayerName(), punishData);
        this.writeMutesCache();
    }

    @Override
    public void addHistory(String nickName, PunishData punishData) {

    }

    @Override
    public void unBan(String nickName) {
        this.bansCache.remove(nickName);
        this.writeBansCache();
    }

    @Override
    public void unMute(String nickName) {
        this.mutesCache.remove(nickName);
        this.writeMutesCache();
    }

    @Override
    public void clearHistory(String nickName) {

    }

    @Override
    public void unBanAllBy(String nickName) {
        val punishDataList = this.bansCache.values().parallelStream().filter(punishData -> punishData.getPunisherName().equalsIgnoreCase(nickName)).collect(Collectors.toList());
        punishDataList.forEach(punishData -> this.bansCache.remove(punishData.getPlayerName()));
        this.writeBansCache();
    }

    @Override
    public void unMuteAllBy(String nickName) {
        val punishDataList = this.mutesCache.values().parallelStream().filter(punishData -> punishData.getPunisherName().equalsIgnoreCase(nickName)).collect(Collectors.toList());
        punishDataList.forEach(punishData -> this.mutesCache.remove(punishData.getPlayerName()));
        this.writeMutesCache();
    }

    @Override
    public PunishData getBanData(String nickName) {
        return this.bansCache.get(nickName.toLowerCase());
    }

    @Override
    public PunishData getMuteData(String nickName) {
        return this.mutesCache.get(nickName);
    }

    @Override
    public List<PunishData> getHistoryData(String nickName) {
        return null;
    }

    @Override
    public boolean isBanned(String nickName) {
        return this.getBanData(nickName) != null;
    }

    @Override
    public boolean isMuted(String nickName) {
        return this.getMuteData(nickName) != null;
    }

    @Override
    public void close() {
        this.writeBansCache();
        this.writeMutesCache();
    }
}
