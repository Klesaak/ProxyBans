package ua.klesaak.proxybans.storage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ua.klesaak.proxybans.rules.PunishType;

import java.time.Instant;

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
    private long punishTimeExpire; //Время до которого забанили (Instant#getEpochSecond())
    private String punishExpireDate; //Время до которого забанили (Date format)
    private String punishDate; //Время когда забанили (Date format)

    public boolean isIPBan() {
        return this.ip != null;
    }

    public boolean isExpired() {
        if (!this.punishType.isTemporary()) return false;
        return Instant.now().isAfter(Instant.ofEpochSecond(this.punishTimeExpire));
    }
}
