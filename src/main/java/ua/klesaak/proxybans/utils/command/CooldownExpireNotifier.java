package ua.klesaak.proxybans.utils.command;

import net.md_5.bungee.api.CommandSender;
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

    public boolean isCooldown(String commandName, String playerName) {
        return this.commandCooldowns.get(commandName).get(playerName) != null;
    }

    public long getCooldown(CommandSender commandSender, String commandName) {
        if (this.isCooldown(commandName, commandSender.getName())) return this.commandCooldowns.get(commandName).get(commandSender.getName());
        return 0L;
    }

    public ProxyBansManager getProxyBansManager() {
        return proxyBansManager;
    }
}
