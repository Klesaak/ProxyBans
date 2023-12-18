package ua.klesaak.proxybans.config;

import lombok.Getter;
import ua.klesaak.proxybans.utils.messages.LocaleConfigData;
import ua.klesaak.proxybans.utils.messages.Message;
import ua.klesaak.proxybans.utils.messages.MessageField;

import java.util.regex.Pattern;

@Getter
public class MessagesFile implements LocaleConfigData<MessagesFile> {
    public static final Pattern PLAYER_NAME_PATTERN = Pattern.compile("(player)", Pattern.LITERAL);
    public static final Pattern PUNISHER_NAME_PATTERN = Pattern.compile("(punisher)", Pattern.LITERAL);
    public static final Pattern PUNISH_SERVER_PATTERN = Pattern.compile("(punishServer)", Pattern.LITERAL);
    public static final Pattern PLAYER_SERVER_PATTERN = Pattern.compile("(server)", Pattern.LITERAL);
    public static final Pattern RULE_PATTERN = Pattern.compile("(rule)", Pattern.LITERAL);
    public static final Pattern RULE_TEXT_PATTERN = Pattern.compile("(ruleText)", Pattern.LITERAL);
    public static final Pattern RULES_PATTERN = Pattern.compile("(rules)", Pattern.LITERAL);
    public static final Pattern COMMENT_TEXT_PATTERN = Pattern.compile("(comment)", Pattern.LITERAL);
    public static final Pattern DATE_PATTERN = Pattern.compile("(date)", Pattern.LITERAL);
    public static final Pattern TIME_PATTERN = Pattern.compile("(time)", Pattern.LITERAL);
    public static final Pattern COMMAND_PATTERN = Pattern.compile("(command)", Pattern.LITERAL);
    public static final Pattern APPLICABLE_PUNISHMENTS_PATTERN = Pattern.compile("(applicablePunishments)", Pattern.LITERAL);
    public static final Pattern PAGE_PATTERN = Pattern.compile("(page)", Pattern.LITERAL);
    public static final Pattern PAGES_PATTERN = Pattern.compile("(pages)", Pattern.LITERAL);


    //commands'ы
    @MessageField(key = "command.usage.ban", defaultMessage = "&7Использование - &c/ban &7<ник> <правило> <пояснение>")
    private Message usageBanCommand;
    @MessageField(key = "command.usage.ban-ip", defaultMessage = "&7Использование - &c/ban-ip &7<ник/IP-адрес> <правило> <пояснение>")
    private Message usageBanIpCommand;
    @MessageField(key = "command.usage.tempBan", defaultMessage = "&7Использование - &c/tempban &7<ник> <время> <правило> <пояснение>")
    private Message usageTempBanCommand;
    @MessageField(key = "command.usage.op-ban", defaultMessage = "&7Использование - &c/op-ban &7<ник> <правило> <пояснение>")
    private Message usageOpBanCommand;
    @MessageField(key = "command.usage.op-banip", defaultMessage = "&7Использование - &c/op-banip &7<ник/IP-адрес> <правило> <пояснение>")
    private Message usageOpBanIpCommand;
    @MessageField(key = "command.usage.op-tempBan", defaultMessage = "&7Использование - &c/op-tempban &7<ник> <время> <правило> <пояснение>")
    private Message usageOpTempBanCommand;
    @MessageField(key = "command.usage.mute", defaultMessage = "&7Использование - &c/mute &7<ник> <правило> <пояснение>")
    private Message usageMuteCommand;
    @MessageField(key = "command.usage.tempMute", defaultMessage = "&7Использование - &c/tempmute &7<ник> <время> <правило> <пояснение>")
    private Message usageTempMuteCommand;
    @MessageField(key = "command.usage.op-mute", defaultMessage = "&7Использование - &c/op-mute &7<ник> <правило> <пояснение>")
    private Message usageOpMuteCommand;
    @MessageField(key = "command.usage.op-tempMute", defaultMessage = "&7Использование - &c/op-tempmute &7<ник> <время> <правило> <пояснение>")
    private Message usageOpTempMuteCommand;
    @MessageField(key = "command.usage.unban", defaultMessage = "&7Использование - &c/unban &7<ник>")
    private Message usageUnbanCommand;
    @MessageField(key = "command.usage.op-unban", defaultMessage = "&7Использование - &c/op-unban &7<ник>")
    private Message usageOpUnbanCommand;
    @MessageField(key = "command.usage.unmute", defaultMessage = "&7Использование - &c/unmute &7<ник>")
    private Message usageUnmuteCommand;
    @MessageField(key = "command.usage.op-unmute", defaultMessage = "&7Использование - &c/op-unmute &7<ник>")
    private Message usageOpUnmuteCommand;
    @MessageField(key = "command.usage.kick", defaultMessage = "&7Использование - &c/kick &7<ник> <правило> <пояснение>")
    private Message usageKickCommand;
    @MessageField(key = "command.usage.check", defaultMessage = "&7Использование - &c/check &7<ник>")
    private Message usageCheckCommand;
    @MessageField(key = "command.cooldown", defaultMessage = "&7Вы сможете юзать данную команду через &6(time)")
    private Message messageCooldown;
    @MessageField(key = "command.no-permission", defaultMessage = "&cВы не можете юзать данную команду.")
    private Message messageNoPermission;
    @MessageField(key = "command.isCooldownExpired", defaultMessage = "&aКоманда &6/(command)&a снова доступна к использованию.")
    private Message messageIsCooldownExpired;
    @MessageField(key = "command.consoleName", defaultMessage = "&cProxyBansPlugin", withoutQuotes = true)
    private Message messageIsConsoleName;
    @MessageField(key = "command.error.wrongNickname", defaultMessage = "&4Ошибка: &cНеправильный ник.")
    private Message messageWrongNickname;
    @MessageField(key = "command.error.playerIsOffline", defaultMessage = "&4Ошибка: &cИгрок не в сети.")
    private Message messagePlayerIsOffline;
    @MessageField(key = "command.error.playerIsProtected", defaultMessage = "&4Ошибка: &cВы не можете наказать этого игрока.")
    private Message messagePlayerIsProtected;
    @MessageField(key = "command.error.tooFewInfoAboutPunish", defaultMessage = "&4Ошибка: &cВы указали мало информации в пояснении.")
    private Message messageTooFewInfoAboutPunish;
    @MessageField(key = "command.error.pageNotFound", defaultMessage = "Страница не найдена.")
    private Message messagePageNotFound;
    @MessageField(key = "command.error.self", defaultMessage = "&4Ошибка: &cВы не можете взаимодействовать с собой.")
    private Message messageSelfHarm;
    @MessageField(key = "command.error.playerIsNotBanned", defaultMessage = "&4Ошибка: &cИгрок не забанен.")
    private Message playerIsNotBanned;
    @MessageField(key = "command.error.playerIsOPBanned", defaultMessage = "&4Ошибка: &cУ игрока ОП-Бан, вы не можете его разблокировать!")
    private Message playerIsOPBanned;
    @MessageField(key = "command.error.playerIsOPMuted", defaultMessage = "&4Ошибка: &cУ игрока ОП-Мут, вы не можете его размутить!")
    private Message playerIsOPMuted;
    @MessageField(key = "command.error.playerIsNotMuted", defaultMessage = "&4Ошибка: &cИгрок не замучен.")
    private Message playerIsNotMuted;
    @MessageField(key = "command.error.playerSelfUnmute", defaultMessage = "&4Ошибка: &cВы не можете себя размутить :)")
    private Message playerSelfUnmute;
    @MessageField(key = "command.error.invalidFormatTime", defaultMessage = "&4Ошибка: &cВы указали неверный формат времени. Верный: 10s, 10m, 10h, 10d")
    private Message invalidFormatTime;
    @MessageField(key = "command.rule.notFound", defaultMessage = "&4Ошибка: &cПравило не найдено.")
    private Message messageRuleNotFound;
    @MessageField(key = "command.rule.notApplicablePunish", defaultMessage = "&4Ошибка: &cДля данного правила нельзя применить этот тип наказания.")
    private Message messageNotApplicablePunish;
    @MessageField(key = "command.rule.listFormat", defaultMessage = {
            "&6Список правил: &c(page)/(pages)",
            "&7",
            "(rules)",
            "&c&r",
            "&6Следующая страница - /rules [&cномер страницы&6]"})
    private Message messageRuleListFormat;
    @MessageField(key = "command.rule.format", defaultMessage = "&c(rule). &6(ruleText) &a((applicablePunishments))", withoutQuotes = true)
    private Message messageRuleFormat;
    @MessageField(key = "command.emptyData", defaultMessage = "N/A", withoutQuotes = true)
    private Message messageEmptyData;

    //broadcast'ы
    @MessageField(key = "message.broadcast.banned", defaultMessage = "[BAN]: (punisher) (punishServer) забанил (player) (server). Причина - (rule). (ruleText) ((comment))")
    private Message broadcastBanned;
    @MessageField(key = "message.broadcast.banned-ip", defaultMessage = "[IP-BAN]: (punisher) (punishServer) забанил по-ip (player) (server). Причина - (rule). (ruleText) ((comment))")
    private Message broadcastIpBanned;
    @MessageField(key = "message.broadcast.tempBanned", defaultMessage = "[TEMP-BAN]: (punisher) (punishServer) забанил (player) до (time) (server). Причина - (rule). (ruleText) ((comment))")
    private Message broadcastTempBanned;
    @MessageField(key = "message.broadcast.op-banned", defaultMessage = "[OP-BAN]: (punisher) (punishServer) забанил (player) (server). Причина - (rule). (ruleText) ((comment))")
    private Message broadcastOpBanned;
    @MessageField(key = "message.broadcast.op-bannedIp", defaultMessage = "[OP-IPBAN]: (punisher) (punishServer) забанил по-ip (player) (server). Причина - (rule). (ruleText) ((comment))")
    private Message broadcastOpIpBanned;
    @MessageField(key = "message.broadcast.op-tempBanned", defaultMessage = "[OP-TEMPBAN]: (punisher) (punishServer) забанил (player) до (time)  на сервере (server). Причина - (comment)")
    private Message broadcastOpTempBanned;
    @MessageField(key = "message.broadcast.muted", defaultMessage = "[MUTE]: (punisher) (punishServer) замутил (player) (server). Причина - (rule). (ruleText) ((comment))")
    private Message broadcastMuted;
    @MessageField(key = "message.broadcast.tempMuted", defaultMessage = "[TEMP-MUTE]: (punisher) (punishServer) замутил (player) до (time) (server). Причина - (rule). (ruleText) ((comment))")
    private Message broadcastTempMuted;
    @MessageField(key = "message.broadcast.op-muted", defaultMessage = "[OP-MUTE]: (punisher) (punishServer) замутил (player) (server). Причина - (comment)")
    private Message broadcastOpMuted;
    @MessageField(key = "message.broadcast.op-tempMuted", defaultMessage = "[OP-TEMPMUTE]: (punisher) (punishServer) замутил (player) до (time) (server). Причина - (comment)")
    private Message broadcastOpTempMuted;
    @MessageField(key = "message.broadcast.unbanned", defaultMessage = "[UNBAN]: (punisher) снял бан с (player)")
    private Message broadcastUnbanned;
    @MessageField(key = "message.broadcast.op-unbanned", defaultMessage = "[OP-UNBAN]: (punisher) снял бан с (player)")
    private Message broadcastOpUnbanned;
    @MessageField(key = "message.broadcast.unmuted", defaultMessage = "[UNMUTE]: (punisher) размутил игрока (player)")
    private Message broadcastUnmuted;
    @MessageField(key = "message.broadcast.op-unmuted", defaultMessage = "[OP-UNMUTE]: (punisher) размутил игрока (player)")
    private Message broadcastOpUnmuted;
    @MessageField(key = "message.broadcast.kicked", defaultMessage = "[KICK]: (punisher) (punishServer) кикнул (player) (server). Причина - (rule). (ruleText) ((comment))")
    private Message broadcastKicked;


    //баны
    @MessageField(key = "message.banned", defaultMessage = {
            "Вас забанили навсегда!",
            "Забанил: (punisher)" ,
            "Правило: (rule)",
            "Комментарий: (comment)",
            "Вы были на сервере: (server)",
            "Забанивший был на сервере: (punishServer)",
            "Дата: (date)"
    })
    private Message messageBanned;
    @MessageField(key = "message.tempBanned", defaultMessage = {
            "Вас временно забанили!",
            "Забанил: (punisher)" ,
            "Правило: (rule)",
            "Комментарий: (comment)",
            "Вы были на сервере: (server)",
            "Забанивший был на сервере: (punishServer)",
            "Бан продлится до: (time)",
            "Дата: (date)"
    })
    private Message messageTempBanned;
    @MessageField(key = "message.banned-ip", defaultMessage = {
            "Вас забанили навсегда по IP!",
            "Забанил: (punisher)" ,
            "Правило: (rule)",
            "Комментарий: (comment)",
            "Вы были на сервере: (server)",
            "Забанивший был на сервере: (punishServer)",
            "Дата: (date)"
    })
    private Message messageBannedIp;
    //op-баны
    @MessageField(key = "message.op-banned", defaultMessage = {
            "[OP-БАН]",
            "Вас забанили навсегда!",
            "Забанил: (punisher)" ,
            "Правило: (rule)",
            "Комментарий: (comment)",
            "Вы были на сервере: (server)",
            "Забанивший был на сервере: (punishServer)",
            "Дата: (date)"
    })
    private Message messageOpBanned;
    @MessageField(key = "message.op-tempBanned", defaultMessage = {
            "[OP-БАН]",
            "Вас временно забанили!",
            "Забанил: (punisher)" ,
            "Правило: (rule)",
            "Комментарий: (comment)",
            "Вы были на сервере: (server)",
            "Забанивший был на сервере: (punishServer)",
            "Бан продлится до: (time)",
            "Дата: (date)"
    })
    private Message messageOpTempBanned;
    @MessageField(key = "message.op-IpBanned", defaultMessage = {
            "[OP-БАН]",
            "Вас забанили навсегда!",
            "Забанил: (punisher)" ,
            "Правило: (rule)",
            "Комментарий: (comment)",
            "Вы были на сервере: (server)",
            "Забанивший был на сервере: (punishServer)",
            "Дата: (date)"
    })
    private Message messageOpIpBanned;
    //муты
    @MessageField(key = "message.muted", defaultMessage = {"Вас замутили навсегда! Замутил: (punisher) Правило: (rule) Комментарий: (comment)" +
            " Вы были на сервере: (server) Заткнувший был на сервере: (punishServer) Дата: (date)"
    })
    private Message messageMuted;
    @MessageField(key = "message.tempMuted", defaultMessage = {"Вас временно замутили! Замутил: (punisher) Правило: (rule) Комментарий: (comment)" +
            " Вы были на сервере: (server) Заткнувший был на сервере: (punishServer) Мут продлится до: (time) Дата: (date)"
    })
    private Message messageTempMuted;
    @MessageField(key = "message.op-muted", defaultMessage = {"[OP-МУТ] Вас замутили навсегда! Замутил: (punisher) Правило: (rule) Комментарий: (comment)" +
            " Вы были на сервере: (server) Заткнувший был на сервере: (punishServer) Дата: (date)"
    })
    private Message messageOpMuted;
    @MessageField(key = "message.op-tempMuted", defaultMessage = {"[OP-МУТ] Вас временно замутили! Замутил: (punisher) Правило: (rule) Комментарий: (comment)" +
            " Вы были на сервере: (server) Заткнувший был на сервере: (punishServer) Мут продлится до: (time) Дата: (date)"
    })
    private Message messageOpTempMuted;
    //кик
    @MessageField(key = "message.kicked", defaultMessage = {
            "Вас кикнули с сервера!",
            "Кикнул: (punisher)" ,
            "Правило: (rule)",
            "Комментарий: (comment)",
            "Вы были на сервере: (server)",
            "Кикнувший был на сервере: (punishServer)",
            "Дата: (date)"
    })
    private Message messageKicked;







}
