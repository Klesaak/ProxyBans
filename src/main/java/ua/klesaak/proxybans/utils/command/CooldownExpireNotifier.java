package ua.klesaak.proxybans.utils.command;

import ua.klesaak.proxybans.manager.ProxyBansManager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CooldownExpireNotifier {
    private final ProxyBansManager proxyBansManager;
    private final Map<String, ConcurrentHashMap<String, Long>> commandCooldowns = new ConcurrentHashMap<>(64);
    // TODO: 26.06.2023 task


    public CooldownExpireNotifier(ProxyBansManager proxyBansManager) {
        this.proxyBansManager = proxyBansManager;
    }

    public void registerCommand(AbstractPunishCommand command) {
        this.commandCooldowns.put(command.getName(), new ConcurrentHashMap<>());
    }

    public void addCooldown(String commandName, String playerName, Long time) {
        this.commandCooldowns.get(commandName).put(playerName, time);
    }

    public ProxyBansManager getProxyBansManager() {
        return proxyBansManager;
    }
}
