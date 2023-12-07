package ua.klesaak.proxybans.storage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ua.klesaak.proxybans.rules.PunishType;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Builder
public class PunishData {
    private String playerName;
    private PunishType punishType;

    private String punisherServer;
    private String server;
    private String punisherName;
    private String rule;
    private String comment;
    private String ip;
    private long time; //Время на которое забанили
    private String punishDate; //Время когда забанили (Date format)

    public boolean isIPBan() {
        return this.ip != null;
    }
}
