package ua.klesaak.proxybans.rules;

public enum PunishType {
    BAN,
    IP_BAN,
    TEMP_BAN,
    MUTE,
    TEMP_MUTE,
    OP_MUTE,
    OP_TEMP_MUTE,
    OP_BAN,
    OP_IP_BAN,
    OP_TEMP_BAN,
    KICK,
    UNKNOWN;

    public static final PunishType[] BANS = {BAN, IP_BAN, TEMP_BAN, OP_BAN, OP_IP_BAN, OP_TEMP_BAN};
    public static final PunishType[] MUTES = {MUTE, TEMP_MUTE, OP_MUTE, OP_TEMP_MUTE};
}
