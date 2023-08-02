package ua.klesaak.proxybans.storage;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class PunishStorage implements AutoCloseable {
    private final Map<String, PunishData> bansCache = new ConcurrentHashMap<>();
    private final Map<String, PunishData> mutesCache = new ConcurrentHashMap<>();
    private final Map<String, List<PunishData>> historyCache = new ConcurrentHashMap<>();

    public PunishStorage() {

    }
}
