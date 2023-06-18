package ua.klesaak.proxybans.storage;

import lombok.Builder;
import lombok.Getter;
import ua.klesaak.proxybans.rules.PunishType;

@Getter @Builder
public class PunishData {
    private final String playerName;
    private final PunishType punishType;

    private String punisherServer;
    private String server;
    private String punisherName;
    private String reason;
    private String comment;
    private String ip;
    private long time; //Время на которое забанили
    private long punishTime; //Время когда забанили

    public boolean isIPBan() {
        return this.ip != null;
    }
}
