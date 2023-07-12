package ua.klesaak.proxybans.utils.command;

import lombok.val;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;
import net.md_5.bungee.chat.ComponentSerializer;
import ua.klesaak.proxybans.config.MessagesFile;
import ua.klesaak.proxybans.manager.PermissionsConstants;
import ua.klesaak.proxybans.manager.ProxyBansManager;
import ua.klesaak.proxybans.utils.NumberUtils;
import ua.klesaak.proxybans.utils.messages.Message;

public abstract class AbstractPunishCommand extends Command implements TabExecutor {
    private final CooldownExpireNotifier cooldownExpireNotifier;

    public AbstractPunishCommand(ProxyBansManager proxyBansManager, String commandName, String permission, String... aliases) {
        super(commandName, permission, aliases);
        proxyBansManager.getProxyBansPlugin().getProxy().getPluginManager().registerCommand(proxyBansManager.getProxyBansPlugin(), this);
        this.cooldownExpireNotifier = proxyBansManager.getCooldownExpireNotifier();
        this.cooldownExpireNotifier.registerCommand(this);
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        try {
            val manager = this.cooldownExpireNotifier.getProxyBansManager();
            this.cmdVerifyPermission(commandSender,"proxybans." + this.getName(), manager.getMessagesFile().getMessageNoPermission().getMessageString());
            this.checkCooldown(commandSender);
            if (this.onReceiveCommand(commandSender, args)) {
                val senderName = commandSender.getName();
                val configTime = manager.getConfigFile().getCooldownTime(manager.getPermHook().getUserGroup(senderName), this.getName());
                boolean applyCooldown = !commandSender.hasPermission(PermissionsConstants.IGNORE_COOLDOWN_PERMISSION) && configTime > 0L;
                if (applyCooldown && !this.cooldownExpireNotifier.isCooldown(this.getName(), senderName)) {
                    this.cooldownExpireNotifier.addCooldown(this.getName(), senderName, configTime);
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

    public void cmdVerifyPermission(CommandSender sender, String permission, String errorMessage) {
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
        val proxiedPlayer = this.cooldownExpireNotifier.getProxyBansManager().getProxyBansPlugin().getProxy().getPlayer(playerName);
        this.disconnect(proxiedPlayer, message);
    }

    protected void checkCooldown(CommandSender sender) {
        long cooldown = this.cooldownExpireNotifier.getCooldown(sender, this.getName());
        if (cooldown > 0L) {
            throw new RuntimeException(this.cooldownExpireNotifier.getProxyBansManager().getMessagesFile().getMessageCooldown().tag(MessagesFile.TIME_PATTERN, ()-> NumberUtils.getTime(cooldown)).getMessageString());
        }
    }

    protected String parseRule() {
        return ""; // TODO: 26.06.2023
    }

    protected long parseTimme() {
        return 0L; // TODO: 26.06.2023
    }


}
