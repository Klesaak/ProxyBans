package ua.klesaak.proxybans.utils.command;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import lombok.val;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;
import ua.klesaak.proxybans.config.MessagesFile;
import ua.klesaak.proxybans.manager.PermissionsConstants;
import ua.klesaak.proxybans.manager.ProxyBansManager;
import ua.klesaak.proxybans.rules.PunishType;
import ua.klesaak.proxybans.rules.RuleData;
import ua.klesaak.proxybans.storage.PunishData;
import ua.klesaak.proxybans.utils.NumberUtils;
import ua.klesaak.proxybans.utils.messages.Message;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

import static ua.klesaak.proxybans.config.MessagesFile.*;
import static ua.klesaak.proxybans.manager.PermissionsConstants.*;

public abstract class AbstractPunishCommand extends Command implements TabExecutor {
    private final CooldownExpireNotifier cooldownExpireNotifier;
    protected PunishType punishType = PunishType.UNKNOWN;
    protected final ProxyBansManager proxyBansManager;

    public AbstractPunishCommand(ProxyBansManager proxyBansManager, PunishType punishType) {
        this(proxyBansManager, punishType.getCommand());
        this.punishType = punishType;
    }

    public AbstractPunishCommand(ProxyBansManager proxyBansManager, String commandName) {
        super(commandName, "");
        this.proxyBansManager = proxyBansManager;
        proxyBansManager.getProxyBansPlugin().getProxy().getPluginManager().registerCommand(proxyBansManager.getProxyBansPlugin(), this);
        this.cooldownExpireNotifier = proxyBansManager.getCooldownExpireNotifier();
        this.cooldownExpireNotifier.registerCommand(this);
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        try {
            this.cmdVerifyPermission(commandSender,PREFIX_WILDCARD_PERMISSION + this.getName(), this.proxyBansManager.getMessagesFile().getMessageNoPermission());
            this.checkCooldown(commandSender);
            if (this.onReceiveCommand(commandSender, args) && commandSender instanceof ProxiedPlayer) {
                val senderUUID = ((ProxiedPlayer)commandSender).getUniqueId();
                val configTime = this.proxyBansManager.getConfigFile().getCooldownTime(commandSender, this.getName());
                boolean applyCooldown = !commandSender.hasPermission(PermissionsConstants.IGNORE_COOLDOWN_PERMISSION) && configTime > 0L;
                if (applyCooldown) {
                    this.cooldownExpireNotifier.addCooldown(this.getName(), senderUUID, Instant.now().plusSeconds(configTime));
                }
            }
        } catch (AbstractCommandException e) {
            e.sendMinecraftMessage(commandSender);
        }
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender commandSender, String[] args) {
        if (!commandSender.hasPermission(PREFIX_WILDCARD_PERMISSION + this.getName())) return Collections.emptyList();
        return this.onTabSuggest(commandSender, args);
    }

    public abstract boolean onReceiveCommand(CommandSender sender, String[] args); // если возвращаем - тру то включаеца кулдовн

    public abstract Iterable<String> onTabSuggest(CommandSender commandSender, String[] args);

    public void cmdVerifyArgs(int minimum, String[] args, String usage) {
        if (args.length < minimum) {
            throw new AbstractCommandException(new Message(ChatColor.RED + usage, false, true));
        }
    }

    protected String cmdVerifyNickname(CommandSender commandSender, boolean checkOffline, String[] args) {
        String nickName = args[0].toLowerCase();
        String senderName = commandSender.getName();
        val messagesFile = this.proxyBansManager.getMessagesFile();
        if (nickName.isEmpty()) throw new AbstractCommandException(messagesFile.getMessageWrongNickname());
        if (senderName.equalsIgnoreCase(nickName)) throw new AbstractCommandException(messagesFile.getMessageSelfHarm());
        if (checkOffline) this.checkOffline(commandSender, nickName);
        val configFile = this.proxyBansManager.getConfigFile();
        boolean isProxiedPlayer = commandSender instanceof ProxiedPlayer;
        if (isProxiedPlayer && configFile.isProtected(nickName)) {
            throw new AbstractCommandException(messagesFile.getMessagePlayerIsProtected());
        }
        if (isProxiedPlayer && configFile.isHeavier(nickName, senderName) && !commandSender.hasPermission(IGNORE_PRIORITY_PERMISSION)) {
            throw new AbstractCommandException(messagesFile.getMessagePlayerIsLowPriority());
        }
        return nickName;
    }

    protected void cmdVerifyTryUnban(CommandSender commandSender, String nickname, boolean isOpUnban) {
        val storage = this.proxyBansManager.getPunishStorage();
        val senderName = this.cmdGetSenderName(commandSender);
        val nicknameLC = nickname.toLowerCase();
        val punishData = storage.getBanData(nicknameLC);
        MessagesFile messageFile = this.proxyBansManager.getMessagesFile();
        if (punishData == null || punishData.isExpired()) {
            throw new AbstractCommandException(messageFile.getPlayerIsNotBanned());
        }
        boolean playerHaveOpBan = punishData.getPunishType().isOPBan();
        if (playerHaveOpBan && !isOpUnban) {
            throw new AbstractCommandException(messageFile.getPlayerIsOPBanned());
        }
        val message = playerHaveOpBan ? messageFile.getBroadcastOpUnbanned() : messageFile.getBroadcastUnbanned();
        messageFile.getMessageUnbanSuccess().tag(PLAYER_NAME_PATTERN, nickname).send(commandSender);
        message.tag(PUNISHER_NAME_PATTERN, senderName)
                .tag(PLAYER_NAME_PATTERN, nickname).broadcast();
        storage.unBan(nicknameLC);
    }

    protected void cmdVerifyTryUnmute(CommandSender commandSender, String nickname, boolean isOpUnmute) {
        val storage = this.proxyBansManager.getPunishStorage();
        val senderName = this.cmdGetSenderName(commandSender);
        val nicknameLC = nickname.toLowerCase();
        val punishData = storage.getMuteData(nicknameLC);
        MessagesFile messageFile = this.proxyBansManager.getMessagesFile();
        if (punishData == null || punishData.isExpired()) {
            throw new AbstractCommandException(messageFile.getPlayerIsNotMuted());
        }
        if (senderName.equalsIgnoreCase(nicknameLC)) {
            throw new AbstractCommandException(messageFile.getPlayerSelfUnmute());
        }
        boolean playerHaveOpMute = punishData.getPunishType().isOPMute();
        if (playerHaveOpMute && !isOpUnmute) {
            throw new AbstractCommandException(messageFile.getPlayerIsOPMuted());
        }
        val message = playerHaveOpMute ? messageFile.getBroadcastOpUnmuted() : messageFile.getBroadcastUnmuted();
        messageFile.getMessageUnmuteSuccess().tag(PLAYER_NAME_PATTERN, nickname).send(commandSender);
        message.tag(PUNISHER_NAME_PATTERN, senderName)
                .tag(PLAYER_NAME_PATTERN, nickname).broadcast();
        storage.unMute(nicknameLC);
    }

    protected String cmdGetSenderName(CommandSender commandSender) {
        if (commandSender instanceof ProxiedPlayer) {
            return commandSender.getName();
        }
        return this.getConsoleName();
    }

    public void cmdVerifyArgs(int minimum, String[] args, Message usage) {
        if (args.length < minimum) {
            throw new AbstractCommandException(usage);
        }
    }

    public ProxiedPlayer cmdVerifyPlayer(CommandSender sender) {
        if (!(sender instanceof ProxiedPlayer)) {
            throw new AbstractCommandException(new Message(ChatColor.RED + "Must be player", false, false));
        }
        return (ProxiedPlayer)sender;
    }

    public void cmdVerifyPermission(CommandSender sender, String permission) {
        if (!sender.hasPermission(permission)) {
            throw new AbstractCommandException(new Message(ChatColor.RED + "Do not have permission", false, false));
        }
    }

    private void cmdVerifyPermission(CommandSender sender, String permission, String errorMessage) {
        if (!sender.hasPermission(permission)) {
            throw new AbstractCommandException(new Message(errorMessage, false, false));
        }
    }

    private void cmdVerifyPermission(CommandSender sender, String permission, Message errorMessage) {
        if (!sender.hasPermission(permission)) {
            throw new AbstractCommandException(errorMessage);
        }
    }

    protected void disconnect(ProxiedPlayer proxiedPlayer, Message message) {
        message.disconnect(proxiedPlayer);
    }

    protected void disconnect(String playerName, Message message) {
        val proxiedPlayer = ProxyServer.getInstance().getPlayer(playerName);
        this.disconnect(proxiedPlayer, message);
    }

    private void checkCooldown(CommandSender sender) {
        Instant cooldown = this.cooldownExpireNotifier.getCooldown(sender, this.getName());
        int cooldownTime = 0;
        if (cooldown != null) cooldownTime = (int)cooldown.minusSeconds(Instant.now().getEpochSecond()).getEpochSecond();
        if (cooldownTime > 0) {
            throw new AbstractCommandException(this.proxyBansManager.getMessagesFile().getMessageCooldown()
                    .tag(MessagesFile.TIME_PATTERN, NumberUtils.getTime(cooldownTime)));
        }
    }

    protected String getConsoleName() {
        return this.proxyBansManager.getMessagesFile().getMessageIsConsoleName().getMessageString();
    }

    protected RuleData parseRule(CommandSender commandSender, int argIndex, String[] args) {
        String rule = args[argIndex].toLowerCase();
        RuleData ruleData = this.proxyBansManager.getConfigFile().getRule(rule);
        val messagesFile = this.proxyBansManager.getMessagesFile();
        if (ruleData == null) throw new AbstractCommandException(messagesFile.getMessageRuleNotFound());
        if (!ruleData.isAllow(this.punishType) && !commandSender.hasPermission(USER_ANY_PUNISHMENTS_PERMISSION)) {
            throw new AbstractCommandException(messagesFile.getMessageNotApplicablePunish());
        }
        return ruleData;
    }

    protected String parseServer(CommandSender commandSender) {
        if (commandSender instanceof ProxiedPlayer) return ((ProxiedPlayer)commandSender).getServer().getInfo().getMotd();
        return this.proxyBansManager.getMessagesFile().getMessageEmptyData().getMessageString();
    }

    protected String parseServer(String playerName) {
        return this.parseServer(ProxyServer.getInstance().getPlayer(playerName));
    }

    protected int parsePage(String message) {
        int i;
        try {
            i = Integer.parseInt(message);
            if (i <= 0) {
                throw new Exception();
            }
        } catch (Exception e) {
            throw new AbstractCommandException(this.proxyBansManager.getMessagesFile().getMessagePageNotFound());
        }
        return i;
    }

    @SuppressWarnings("deprecation")
    protected String getIpAddress(String nickName) {
        return ProxyServer.getInstance().getPlayer(nickName).getAddress().getAddress().getHostAddress();
    }

    protected long parseTime(CommandSender sender, int argIndex, String[] args) { //return's Instant seconds, когда время наказания истечет
        String time = args[argIndex];
        val messagesFile = this.proxyBansManager.getMessagesFile();
        long timeForWhichPunish;
        try {
            timeForWhichPunish = NumberUtils.parseTimeFromString(time, TimeUnit.SECONDS);
        } catch (IllegalArgumentException e) {
            throw new AbstractCommandException(messagesFile.getInvalidFormatTime());
        }
        if (timeForWhichPunish <= 0) throw new AbstractCommandException(messagesFile.getInvalidTime());
        long maxConfigTime = this.proxyBansManager.getConfigFile().getMaxPunishTime(sender, this.getName());
        if (timeForWhichPunish > maxConfigTime && !sender.hasPermission(IGNORE_MAXTIME_PERMISSION)) return Instant.now().plusSeconds(maxConfigTime).getEpochSecond();
        return Instant.now().plusSeconds(timeForWhichPunish).getEpochSecond();
    }

    private void checkOffline(CommandSender commandSender, String targetName) {
        val targetIsOffline = ProxyServer.getInstance().getPlayer(targetName) == null;
        val messagesFile = this.proxyBansManager.getMessagesFile();
        if (this.punishType.isIPBan() && targetIsOffline) {
            throw new AbstractCommandException(messagesFile.getMessageBanIpOfflinePlayer());
        }
        if ((targetIsOffline && !commandSender.hasPermission(IGNORE_OFFLINE_PERMISSION)) || (this.punishType.isKick() && targetIsOffline)) {
            throw new AbstractCommandException(this.proxyBansManager.getMessagesFile().getMessagePlayerIsOffline());
        }
    }

    protected String parseComment(CommandSender commandSender, int start, String[] args) {
        val comm = Arrays.copyOfRange(args, start, args.length);
        if (!commandSender.hasPermission(SMART_COMMENT_PERMISSION) && Arrays.stream(comm).filter(s -> s.length() >= 2).count() < 2L) {
            throw new AbstractCommandException(this.proxyBansManager.getMessagesFile().getMessageTooFewInfoAboutPunish());
        }
        return Joiner.on(" ").join(comm);
    }

    protected List<String> getOnlinePlayers() {
        List<String> onlinePlayers = new ArrayList<>();
        ProxyServer.getInstance().getPlayers().forEach(pp -> onlinePlayers.add(pp.getName()));
        return onlinePlayers;
    }

    protected List<String> getPunishedPlayersBy(PunishType punishType) {
        List<String> players = new ArrayList<>();
        this.getPunishedPlayersBy(punishData -> punishData.getPunishType() == punishType).forEach(punishData -> players.add(punishData.getPlayerName()));
        return players;
    }

    protected List<PunishData> getPunishedPlayersBy(Predicate<PunishData> predicate) {
        List<PunishData> players = new ArrayList<>();
        val storage = this.proxyBansManager.getPunishStorage();
        for (val punishData : storage.getBansCache().values()) {
            if (predicate.test(punishData) && !punishData.isExpired()) {
                players.add(punishData);
            }
        }
        for (val punishData : storage.getMutesCache().values()) {
            if (predicate.test(punishData) && !punishData.isExpired()) {
                players.add(punishData);
            }
        }
        return players;
    }

    protected List<String> getActualRules(CommandSender commandSender) {
        List<String> actualRules = new ArrayList<>();
        for (RuleData ruleData : this.proxyBansManager.getConfigFile().getRules()) {
            if (ruleData.getApplicablePunishments().contains(this.punishType) || commandSender.hasPermission(USER_ANY_PUNISHMENTS_PERMISSION)) {
                actualRules.add(ruleData.getRule());
            }
        }
        return actualRules;
    }

    protected <T extends Collection<? super String>> T copyPartialMatches(String token, Iterable<String> originals, T collection) {
        Preconditions.checkNotNull(token, "Search token cannot be null");
        Preconditions.checkNotNull(collection, "Collection cannot be null");
        Preconditions.checkNotNull(originals, "Originals cannot be null");
        originals.forEach(string -> { if (startsWithIgnoreCase(string, token)) collection.add(string);});
        return collection;
    }

    private boolean startsWithIgnoreCase(String string, String prefix) {
        Preconditions.checkNotNull(string, "Cannot check a null string for a match");
        return string.length() >= prefix.length() && string.regionMatches(true, 0, prefix, 0, prefix.length());
    }
}
