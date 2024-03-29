package ua.klesaak.proxybans.utils.command;

import lombok.val;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.scheduler.ScheduledTask;
import ua.klesaak.proxybans.config.MessagesFile;
import ua.klesaak.proxybans.manager.ProxyBansManager;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import static ua.klesaak.proxybans.manager.PermissionsConstants.PREFIX_WILDCARD_PERMISSION;

public final class CooldownExpireNotifier {
    private final ProxyBansManager proxyBansManager;
    private final Map<String, ConcurrentHashMap<UUID, Instant>> commandCooldowns = new ConcurrentHashMap<>(64);
    private ScheduledTask cooldownExpireTask;


    public CooldownExpireNotifier(ProxyBansManager proxyBansManager) {
        this.proxyBansManager = proxyBansManager;
        this.cooldownExpireTask = ProxyServer.getInstance().getScheduler().schedule(this.proxyBansManager.getProxyBansPlugin(), () -> {
            for (String commandKey : this.commandCooldowns.keySet()) {
                val playersMap = this.commandCooldowns.get(commandKey);
                for (UUID playerUUIDKey : playersMap.keySet()) {
                    if (Instant.now().isAfter(playersMap.get(playerUUIDKey))) {
                        playersMap.remove(playerUUIDKey);
                        ProxiedPlayer proxiedPlayer = ProxyServer.getInstance().getPlayer(playerUUIDKey);
                        if (proxiedPlayer != null && proxiedPlayer.hasPermission(PREFIX_WILDCARD_PERMISSION + commandKey)) {
                            this.proxyBansManager.getMessagesFile().getMessageIsCooldownExpired().tag(MessagesFile.COMMAND_PATTERN, commandKey).send(proxiedPlayer);
                        }
                    }
                }
            }
        }, 0L, 1L, TimeUnit.SECONDS);
    }

    public void registerCommand(AbstractPunishCommand command) {
        this.commandCooldowns.put(command.getName(), new ConcurrentHashMap<>());
    }

    public void addCooldown(String commandName, UUID playerUUID, Instant instant) {
        this.commandCooldowns.get(commandName).put(playerUUID, instant);
    }

    public boolean isCooldown(String commandName, UUID playerUUID) {
        return this.commandCooldowns.get(commandName).get(playerUUID) != null;
    }

    public Instant getCooldown(CommandSender commandSender, String commandName) {
        if (!(commandSender instanceof ProxiedPlayer)) return null;
        val senderUUID = ((ProxiedPlayer)commandSender).getUniqueId();
        return this.commandCooldowns.get(commandName).get(senderUUID);
    }

    public void stop() {
        this.cooldownExpireTask.cancel();
        this.cooldownExpireTask = null;
    }
}
