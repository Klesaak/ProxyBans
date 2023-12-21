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
import ua.klesaak.proxybans.utils.NumberUtils;
import ua.klesaak.proxybans.utils.messages.Message;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.TimeUnit;

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
            if (this.onReceiveCommand(commandSender, args)) {
                val senderName = commandSender.getName();
                val configTime = this.proxyBansManager.getConfigFile().getCooldownTime(commandSender, this.getName());
                boolean applyCooldown = !commandSender.hasPermission(PermissionsConstants.IGNORE_COOLDOWN_PERMISSION) && configTime > 0L;
                if (applyCooldown && !this.cooldownExpireNotifier.isCooldown(this.getName(), senderName)) {
                    this.cooldownExpireNotifier.addCooldown(this.getName(), senderName, Instant.now().plusSeconds(configTime));
                }
            }
        } catch (AbstractCommandException e) {
            val mes = e.getMinecraftMessage();
            if (mes != null) mes.send(commandSender);
        }
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender commandSender, String[] args) {
        if (!commandSender.hasPermission(PREFIX_WILDCARD_PERMISSION + this.getName())) return Collections.emptyList();
        return this.onTabSuggest(commandSender, args);
    }

    public abstract boolean onReceiveCommand(CommandSender sender, String[] args) throws AbstractCommandException; // если возвращаем - тру то включаеца кулдовн

    public abstract Iterable<String> onTabSuggest(CommandSender commandSender, String[] args);

    public void cmdVerifyArgs(int minimum, String[] args, String usage) throws AbstractCommandException {
        if (args.length < minimum) {
            throw new AbstractCommandException(new Message(ChatColor.RED + usage, false, false));
        }
    }

    protected String cmdVerifyNickname(CommandSender commandSender, boolean checkOffline, String[] args) throws AbstractCommandException {
        String nickName = args[0].toLowerCase();
        String senderName = commandSender.getName();
        val messagesFile = this.proxyBansManager.getMessagesFile();
        if (nickName.isEmpty()) throw new AbstractCommandException(messagesFile.getMessageWrongNickname());
        if (senderName.equalsIgnoreCase(nickName)) throw new AbstractCommandException(messagesFile.getMessageSelfHarm());
        if (checkOffline) this.checkOffline(commandSender, nickName);
        val configFile = this.proxyBansManager.getConfigFile();
        if (commandSender instanceof ProxiedPlayer && (configFile.isProtected(nickName) || (configFile.isHeavier(nickName, senderName) && !commandSender.hasPermission(IGNORE_PRIORITY)))) {
            throw new AbstractCommandException(messagesFile.getMessagePlayerIsProtected());
        }
        return nickName;
    }

    protected void cmdVerifyTryUnban(String senderName, String nickname, boolean isOpUnban) throws AbstractCommandException {
        val storage = this.proxyBansManager.getPunishStorage();
        val nicknameLC = nickname.toLowerCase();
        val punishData = storage.getBanData(nicknameLC);
        MessagesFile messageFile = this.proxyBansManager.getMessagesFile();
        if (punishData == null) {
            throw new AbstractCommandException(messageFile.getPlayerIsNotBanned());
        }
        boolean playerHaveOpBan = punishData.getPunishType().isOPBan();
        if (playerHaveOpBan && !isOpUnban) {
            throw new AbstractCommandException(messageFile.getPlayerIsOPBanned());
        }
        val message = playerHaveOpBan ? messageFile.getBroadcastOpUnbanned() : messageFile.getBroadcastUnbanned();
        message.tag(PUNISHER_NAME_PATTERN, senderName)
                .tag(PLAYER_NAME_PATTERN, nickname).broadcast();
        storage.unBan(nicknameLC);
    }

    protected void cmdVerifyTryUnmute(String senderName, String nickname, boolean isOpUnmute) throws AbstractCommandException {
        val storage = this.proxyBansManager.getPunishStorage();
        val nicknameLC = nickname.toLowerCase();
        val punishData = storage.getMuteData(nicknameLC);
        MessagesFile messageFile = this.proxyBansManager.getMessagesFile();
        if (punishData == null) {
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
        message.tag(PUNISHER_NAME_PATTERN, senderName)
                .tag(PLAYER_NAME_PATTERN, nickname).broadcast();
        storage.unMute(nicknameLC);
    }

    protected String cmdVerifySender(CommandSender commandSender) {
        if (commandSender instanceof ProxiedPlayer) {
            return commandSender.getName();
        }
        return this.getConsoleName();
    }

    public void cmdVerifyArgs(int minimum, String[] args, Message usage) throws AbstractCommandException {
        if (args.length < minimum) {
            throw new AbstractCommandException(usage);
        }
    }

    public ProxiedPlayer cmdVerifyPlayer(CommandSender sender) throws AbstractCommandException {
        if (!(sender instanceof ProxiedPlayer)) {
            throw new AbstractCommandException(new Message(ChatColor.RED + "Must be player", false, false));
        }
        return (ProxiedPlayer)sender;
    }

    public void cmdVerifyPermission(CommandSender sender, String permission) throws AbstractCommandException {
        if (!sender.hasPermission(permission)) {
            throw new AbstractCommandException(new Message(ChatColor.RED + "Do not have permission", false, false));
        }
    }

    private void cmdVerifyPermission(CommandSender sender, String permission, String errorMessage) throws AbstractCommandException {
        if (!sender.hasPermission(permission)) {
            throw new AbstractCommandException(new Message(errorMessage, false, false));
        }
    }

    private void cmdVerifyPermission(CommandSender sender, String permission, Message errorMessage) throws AbstractCommandException {
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

    private void checkCooldown(CommandSender sender) throws AbstractCommandException {
        Instant cooldown = this.cooldownExpireNotifier.getCooldown(sender, this.getName());
        if (cooldown != null) {
            throw new AbstractCommandException(this.proxyBansManager.getMessagesFile().getMessageCooldown()
                    .tag(MessagesFile.TIME_PATTERN, NumberUtils.getTime((int)cooldown.minusSeconds(Instant.now().getEpochSecond()).getEpochSecond())));
        }
    }

    protected String getConsoleName() {
        return this.proxyBansManager.getMessagesFile().getMessageIsConsoleName().getMessageString();
    }

    protected RuleData parseRule(CommandSender commandSender, int argIndex, String[] args) throws AbstractCommandException {
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

    protected int parsePage(String message) throws AbstractCommandException {
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

    protected long parseTime(CommandSender sender, int argIndex, String[] args) throws AbstractCommandException { //return's Instant seconds, когда время наказания истечет
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
        if (timeForWhichPunish > maxConfigTime && !sender.hasPermission(IGNORE_MAXTIME)) return Instant.now().plusSeconds(maxConfigTime).getEpochSecond();
        return Instant.now().plusSeconds(timeForWhichPunish).getEpochSecond();
    }

    private void checkOffline(CommandSender commandSender, String targetName) throws AbstractCommandException {
        val targetIsOffline = ProxyServer.getInstance().getPlayer(targetName) == null;
        val messagesFile = this.proxyBansManager.getMessagesFile();
        if (this.punishType.isIPBan() && targetIsOffline) {
            throw new AbstractCommandException(messagesFile.getMessageBanIpOfflinePlayer());
        }
        if ((targetIsOffline && !commandSender.hasPermission(IGNORE_OFFLINE_PERMISSION)) || (this.punishType.isKick() && targetIsOffline)) {
            throw new AbstractCommandException(this.proxyBansManager.getMessagesFile().getMessagePlayerIsOffline());
        }
    }

    protected String parseComment(CommandSender commandSender, int start, String[] args) throws AbstractCommandException {
        val comm = Arrays.copyOfRange(args, start, args.length);
        if (!commandSender.hasPermission(SMART_COMMENT_PERMISSION) && Arrays.stream(comm).filter(s -> s.length() >= 2).count() < 3L) {
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
        for (val punishData : this.proxyBansManager.getPunishStorage().getBansCache().values()) {
            if (punishData.getPunishType() == punishType) {
                players.add(punishData.getPlayerName());
            }
        }
        for (val punishData : this.proxyBansManager.getPunishStorage().getMutesCache().values()) {
            if (punishData.getPunishType() == punishType) {
                players.add(punishData.getPlayerName());
            }
        }
        return players;
    }

    protected List<String> getActualRules() {
        List<String> actualRules = new ArrayList<>();
        for (RuleData ruleData : this.proxyBansManager.getConfigFile().getRules()) {
            if (ruleData.getApplicablePunishments().contains(this.punishType)) {
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
