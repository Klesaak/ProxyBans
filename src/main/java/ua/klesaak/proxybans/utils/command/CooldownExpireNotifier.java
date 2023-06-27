package ua.klesaak.proxybans.utils.command;

import lombok.val;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CooldownExpireNotifier {
    private final Map<String, ConcurrentHashMap<String, Long>> commandCooldowns = new ConcurrentHashMap<>(64);
    // TODO: 26.06.2023 task


    public CooldownExpireNotifier() {

    }

    public void registerCommand(AbstractPunishCommand command) {
        this.commandCooldowns.put(command.getName(), new ConcurrentHashMap<>());
    }

    public void addCooldown(String commandName, String playerName, Long time) {
        this.commandCooldowns.put(playerName, );
    }
}
