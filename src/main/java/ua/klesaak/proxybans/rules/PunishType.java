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

    public static final PunishType[] BANS = {BAN, IP_BAN, TEMP_BAN, OP_BAN, OP_IP_BAN, OP_TEMP_BAN};
    public static final PunishType[] MUTES = {MUTE, TEMP_MUTE, OP_MUTE, OP_TEMP_MUTE};
    final String command;
    PunishType(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }
}
