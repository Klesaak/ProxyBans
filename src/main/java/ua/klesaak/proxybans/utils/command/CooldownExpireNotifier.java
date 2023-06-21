package ua.klesaak.proxybans.utils.command;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CooldownExpireNotifier {
    public final Map<String, ConcurrentHashMap<String, Long>> commandCooldowns = new ConcurrentHashMap<>(64);

    public CooldownExpireNotifier() {

    }
}
