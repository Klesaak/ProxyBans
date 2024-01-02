package ua.klesaak.proxybans.rules;

public enum PunishType {
    BAN("ban"),
    IP_BAN("ban-ip"),
    TEMP_BAN("tempban"),
    MUTE("mute"),
    TEMP_MUTE("tempmute"),
    OP_MUTE("op-mute"),
    OP_TEMP_MUTE("op-tempmute"),
    OP_BAN("op-ban"),
    OP_IP_BAN("op-banip"),
    OP_TEMP_BAN("op-tempban"),
    KICK("kick"),
    UNKNOWN("");

    final String command;
    PunishType(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }

    public boolean isOPBan() {
        return this == OP_BAN || this == OP_TEMP_BAN || this == OP_IP_BAN;
    }

    public boolean isBan() {
        return this == BAN || this == TEMP_BAN || this == IP_BAN || this == OP_BAN || this == OP_TEMP_BAN || this == OP_IP_BAN;
    }

    public boolean isIPBan() {
        return this == IP_BAN || this == OP_IP_BAN;
    }

    public boolean isKick() {
        return this == KICK;
    }

    public boolean isOPMute() {
        return this == OP_MUTE || this == OP_TEMP_MUTE;
    }

    public boolean isTemporary() {
        return this == TEMP_BAN || this == TEMP_MUTE || this == OP_TEMP_MUTE || this == OP_TEMP_BAN;
    }
}
