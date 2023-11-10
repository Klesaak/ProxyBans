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


    //usages'ы
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
    @MessageField(key = "command.isCooldownExpired", defaultMessage = "&aКоманда (command) снова доступна к использованию.")
    private Message messageIsCooldownExpired;
    @MessageField(key = "command.consoleName", defaultMessage = "&cProxyBansPlugin")
    private Message messageIsConsoleName;
    @MessageField(key = "command.wrongNickname", defaultMessage = "&4Ошибка: &cНеправильный ник.")
    private Message messageWrongNickname;
    @MessageField(key = "command.playerIsOffline", defaultMessage = "&4Ошибка: &cИгрок не в сети.")
    private Message messagePlayerIsOffline;
    @MessageField(key = "command.playerIsProtected", defaultMessage = "&4Ошибка: &cВы не можете наказать этого игрока.")
    private Message messagePlayerIsProtected;
    @MessageField(key = "command.tooFewInfoAboutPunish", defaultMessage = "&4Ошибка: &cВы указали мало информации в пояснении.")
    private Message messageTooFewInfoAboutPunish;
    @MessageField(key = "command.rule.notFound", defaultMessage = "&4Ошибка: &cПравило не найдено.")
    private Message messageRuleNotFound;
    @MessageField(key = "command.rule.notApplicablePunish", defaultMessage = "&4Ошибка: &cДля данного правила нельзя применить этот тип наказания.")
    private Message messageNotApplicablePunish;
    @MessageField(key = "command.rule.listFormat", defaultMessage = {"&6Список правил: &c(page)/(pages)", "", "(rules)", "", "&6Следующая страница - /rules [&cномер страницы&6]"})
    private Message messageRuleListFormat;
    @MessageField(key = "command.rule.format", defaultMessage = "&c(rule). &6(ruleText) &a((applicablePunishments))")
    private Message messageRuleFormat;
    @MessageField(key = "command.emptyData", defaultMessage = "N/A")
    private Message messageEmptyData;
    @MessageField(key = "command.pageNotFound", defaultMessage = "Страница не найдена.")
    private Message messagePageNotFound;
    @MessageField(key = "command.self", defaultMessage = "&4Ошибка: &cВы не можете взаимодействовать с собой.")
    private Message messageSelfHarm;

    //broadcast'ы
    @MessageField(key = "message.broadcast.banned", defaultMessage = "(punisher) (punishServer) забанил (player) (server). Причина - (rule). (ruleText) ((comment))")
    private Message broadcastBanned;
    @MessageField(key = "message.broadcast.banned-ip", defaultMessage = "(punisher) (punishServer) забанил по-ip (player) (server). Причина - (rule). (ruleText) ((comment))")
    private Message broadcastIpBanned;
    @MessageField(key = "message.broadcast.tempBanned", defaultMessage = "(punisher) (punishServer) забанил (player) на (date) (server). Причина - (rule). (ruleText) ((comment))")
    private Message broadcastTempBanned;
    @MessageField(key = "message.broadcast.op-banned", defaultMessage = "(punisher) (punishServer) забанил (player) (server). Причина - (comment)")
    private Message broadcastOpBanned;
    @MessageField(key = "message.broadcast.op-bannedIp", defaultMessage = "(punisher) (punishServer) забанил по-ip (player) (server). Причина - (rule). (ruleText) ((comment))")
    private Message broadcastOpIpBanned;
    @MessageField(key = "message.broadcast.op-tempBanned", defaultMessage = "(punisher) (punishServer) забанил (player) до (date)  на сервере (server). Причина - (comment)")
    private Message broadcastOpTempBanned;
    @MessageField(key = "message.broadcast.muted", defaultMessage = "(punisher) (punishServer) замутил (player) (server). Причина - (rule). (ruleText) ((comment))")
    private Message broadcastMuted;
    @MessageField(key = "message.broadcast.tempMuted", defaultMessage = "(punisher) (punishServer) замутил (player) на (date) (server). Причина - (rule). (ruleText) ((comment))")
    private Message broadcastTempMuted;
    @MessageField(key = "message.broadcast.op-muted", defaultMessage = "(punisher) (punishServer) замутил (player) (server). Причина - (comment)")
    private Message broadcastOpMuted;
    @MessageField(key = "message.broadcast.op-tempMuted", defaultMessage = "(punisher) (punishServer) замутил (player) на (date) (server). Причина - (comment)")
    private Message broadcastOpTempMuted;
    @MessageField(key = "message.broadcast.unbanned", defaultMessage = "(player) снял бан с (unbanned)")
    private Message broadcastUnbanned;
    @MessageField(key = "message.broadcast.op-unbanned", defaultMessage = "(player) снял бан с (unbanned)")
    private Message broadcastOpUnbanned;
    @MessageField(key = "message.broadcast.unmuted", defaultMessage = "Игрок (player) размутил игрока (unmuted)")
    private Message broadcastUnmuted;
    @MessageField(key = "message.broadcast.op-unmuted", defaultMessage = "Игрок (player) размутил игрока (unmuted)")
    private Message broadcastOpUnmuted;
    @MessageField(key = "message.broadcast.kicked", defaultMessage = "(punisher) (punishServer) кикнул (player) (server). Причина - (rule). (ruleText) ((comment))")
    private Message broadcastKicked;




    @MessageField(key = "message.banned", defaultMessage = {"stroka1", "stroka2" ,"stroka3", "stroka4"})
    private Message messageBanned;
    @MessageField(key = "message.banned-ip", defaultMessage = {"stroka1", "stroka2" ,"stroka3", "stroka4"})
    private Message messageBannedIp;
    @MessageField(key = "message.tempBanned", defaultMessage = {"stroka1", "stroka2" ,"stroka3", "stroka4"})
    private Message messageTempBanned;
    @MessageField(key = "message.kicked", defaultMessage = {"stroka1", "stroka2" ,"stroka3", "stroka4"})
    private Message messageKicked;
    // TODO: 15.06.2023 messages







}
