package ua.klesaak.proxybans.utils.command;

import lombok.val;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;
import net.md_5.bungee.chat.ComponentSerializer;
import ua.klesaak.proxybans.config.MessagesFile;
import ua.klesaak.proxybans.manager.PermissionsConstants;
import ua.klesaak.proxybans.manager.ProxyBansManager;
import ua.klesaak.proxybans.rules.PunishType;
import ua.klesaak.proxybans.rules.RuleData;
import ua.klesaak.proxybans.utils.NumberUtils;
import ua.klesaak.proxybans.utils.messages.Message;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import static ua.klesaak.proxybans.manager.PermissionsConstants.*;

public abstract class AbstractPunishCommand extends Command implements TabExecutor {
    private final CooldownExpireNotifier cooldownExpireNotifier;
    protected final ProxyBansManager proxyBansManager;

    public AbstractPunishCommand(ProxyBansManager proxyBansManager, String commandName, String permission, String... aliases) {
        super(commandName, permission, aliases);
        this.proxyBansManager = proxyBansManager;
        proxyBansManager.getProxyBansPlugin().getProxy().getPluginManager().registerCommand(proxyBansManager.getProxyBansPlugin(), this);
        this.cooldownExpireNotifier = proxyBansManager.getCooldownExpireNotifier();
        this.cooldownExpireNotifier.registerCommand(this);
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        try {
            this.cmdVerifyPermission(commandSender,PREFIX_WILDCARD_PERMISSION + this.getName(), this.proxyBansManager.getMessagesFile().getMessageNoPermission().getMessageString());
            this.checkCooldown(commandSender);
            if (this.onReceiveCommand(commandSender, args)) {
                val senderName = commandSender.getName();
                val configTime = this.proxyBansManager.getConfigFile().getCooldownTime(this.proxyBansManager.getPermHook().getUserGroup(senderName), this.getName());
                boolean applyCooldown = !commandSender.hasPermission(PermissionsConstants.IGNORE_COOLDOWN_PERMISSION) && configTime > 0L;
                if (applyCooldown && !this.cooldownExpireNotifier.isCooldown(this.getName(), senderName)) {
                    this.cooldownExpireNotifier.addCooldown(this.getName(), senderName, System.currentTimeMillis() + configTime);
                }
            }
        } catch (RuntimeException exception) {
            commandSender.sendMessage(ComponentSerializer.parse(exception.getMessage()));
        }
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender commandSender, String[] args) {
        return this.onTabCompleteCommand(commandSender, args);
    }

    public abstract boolean onReceiveCommand(CommandSender sender, String[] args); // если возвращаем - тру то включаеца кулдовн

    public abstract Iterable<String> onTabCompleteCommand(CommandSender commandSender, String[] args);

    public void cmdVerifyArgs(int minimum, String[] args, String usage) {
        if (args.length < minimum) {
            throw new RuntimeException(ChatColor.RED + usage);
        }
    }

    protected String cmdVerifyNickname(CommandSender commandSender, String[] args) {
        String nickName = args[0].toLowerCase();
        if (nickName.isEmpty()) throw new RuntimeException(this.proxyBansManager.getMessagesFile().getMessageWrongNickname().getMessageString());
        if (commandSender.getName().equalsIgnoreCase(nickName)) throw new RuntimeException(this.proxyBansManager.getMessagesFile().getMessageSelfHarm().getMessageString());
        return nickName;
    }

    protected String cmdVerifyPunisher(CommandSender commandSender) {
        if (commandSender instanceof ProxiedPlayer) {
            return commandSender.getName();
        }
        return this.getConsoleName();
    }

    public void cmdVerifyArgs(int minimum, String[] args, Message usage) {
        if (args.length < minimum) {
            throw new RuntimeException(usage.getMessageString());
        }
    }

    public ProxiedPlayer cmdVerifyPlayer(CommandSender sender) {
        if (!(sender instanceof ProxiedPlayer)) {
            throw new RuntimeException(ChatColor.RED + "Must be player");
        }
        return (ProxiedPlayer)sender;
    }

    public void cmdVerifyPermission(CommandSender sender, String permission) {
        if (!sender.hasPermission(permission)) {
            throw new RuntimeException(ChatColor.RED + "Do not have permission");
        }
    }

    private void cmdVerifyPermission(CommandSender sender, String permission, String errorMessage) {
        if (!sender.hasPermission(permission)) {
            throw new RuntimeException(errorMessage);
        }
    }

    protected void disconnect(ProxiedPlayer proxiedPlayer, Message message) {
        if (proxiedPlayer != null) {
            proxiedPlayer.disconnect(message.getMessageComponent());
        }
    }

    protected void disconnect(String playerName, Message message) {
        val proxiedPlayer = this.proxyBansManager.getProxyBansPlugin().getProxy().getPlayer(playerName);
        this.disconnect(proxiedPlayer, message);
    }

    private void checkCooldown(CommandSender sender) {
        long cooldown = this.cooldownExpireNotifier.getCooldown(sender, this.getName());
        if (cooldown > 0L) {
            throw new RuntimeException(this.proxyBansManager.getMessagesFile().getMessageCooldown().tag(MessagesFile.TIME_PATTERN, ()-> NumberUtils.getTime(cooldown)).getMessageString());
        }
    }

    protected String getConsoleName() {
        return this.proxyBansManager.getMessagesFile().getMessageIsConsoleName().getMessageString();
    }

    protected RuleData parseRule(int argIndex, String[] args) {
        String rule = args[argIndex].toLowerCase();
        RuleData ruleData = this.proxyBansManager.getConfigFile().getRule(rule);
        if (ruleData == null) throw new RuntimeException(this.proxyBansManager.getMessagesFile().getMessageRuleNotFound().getMessageString());
        return ruleData;
    }

    protected String parseServer(CommandSender commandSender) {
        if (commandSender != null) return ((ProxiedPlayer)commandSender).getServer().getInfo().getMotd();
        return this.proxyBansManager.getMessagesFile().getMessageEmptyData().getMessageString();
    }

    protected String parseServer(String playerName) {
        return this.parseServer(ProxyServer.getInstance().getPlayer(playerName));
    }

    protected long parseTimme(String time) {
        return NumberUtils.parseTimeFromString(time, TimeUnit.MICROSECONDS);
    }

    protected void checkOffline(CommandSender commandSender, String targetName) {
        if (ProxyServer.getInstance().getPlayer(targetName) == null && !commandSender.hasPermission(IGNORE_OFFLINE_PERMISSION)) {
            throw new RuntimeException(this.proxyBansManager.getMessagesFile().getMessagePlayerIsOffline().getMessageString());
        }
    }

    protected void verifyPunish(CommandSender commandSender, RuleData ruleData, PunishType punishType) {
        if (!ruleData.isAllow(punishType) && !commandSender.hasPermission(USER_ANY_PUNISHMENTS_PERMISSION)) throw new RuntimeException(vxfg);
    }

    protected String parseComment(int start, String[] args) {
        StringBuilder builder = new StringBuilder();
        for (int i = start; i < args.length; ++i) {
            if (i != start) {
                builder.append(" ");
            }
            builder.append(args[i]);
        }
        if (Arrays.stream(builder.toString().split("\\s+")).filter(s -> s.length() >= 2).count() < 3L) {
            throw new RuntimeException(this.proxyBansManager.getMessagesFile().getMessageTooFewInfoAboutPunish().getMessageString());
        }
        return builder.toString();
    }

    // TODO: 15.07.2023 check protected method


}
