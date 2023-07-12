package ua.klesaak.proxybans.utils.command;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.scheduler.ScheduledTask;
import ua.klesaak.proxybans.config.MessagesFile;
import ua.klesaak.proxybans.manager.ProxyBansManager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class CooldownExpireNotifier {
    private final ProxyBansManager proxyBansManager;
    private final Map<String, ConcurrentHashMap<String, Long>> commandCooldowns = new ConcurrentHashMap<>(64);
    private ScheduledTask cooldownExpireTask;


    public CooldownExpireNotifier(ProxyBansManager proxyBansManager) {
        this.proxyBansManager = proxyBansManager;
        this.cooldownExpireTask = ProxyServer.getInstance().getScheduler().schedule(this.proxyBansManager.getProxyBansPlugin(), () -> {
            for (String commandKey : this.commandCooldowns.keySet()) {
                for (String playerKey : this.commandCooldowns.get(commandKey).keySet()) {
                    if (this.commandCooldowns.get(commandKey).get(playerKey) <= System.currentTimeMillis()) {
                        this.commandCooldowns.remove(commandKey).get(playerKey);
                        ProxiedPlayer proxiedPlayer = ProxyServer.getInstance().getPlayer(playerKey);
                        if (proxiedPlayer != null) {
                            this.proxyBansManager.getMessagesFile().getMessageIsCooldownExpired().tag(MessagesFile.COMMAND_PATTERN, ()-> commandKey).send(proxiedPlayer);
                        }
                    }
                }
            }
        }, 0L, 1L, TimeUnit.SECONDS);
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

    public void stop() {
        this.cooldownExpireTask.cancel();
        this.cooldownExpireTask = null;
    }

    public ProxyBansManager getProxyBansManager() {
        return proxyBansManager;
    }
}
