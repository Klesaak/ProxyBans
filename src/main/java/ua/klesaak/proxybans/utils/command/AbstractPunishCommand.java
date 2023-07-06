package ua.klesaak.proxybans.utils.command;

import lombok.val;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.TabExecutor;

public abstract class AbstractPunishCommand extends Command implements TabExecutor {
    private final CooldownExpireNotifier cooldownExpireNotifier;

    public AbstractPunishCommand(Plugin plugin, CooldownExpireNotifier cooldownExpireNotifier, String commandName, String permission, String... aliases) {
        super(commandName, permission, aliases);
        plugin.getProxy().getPluginManager().registerCommand(plugin, this);
        this.cooldownExpireNotifier = cooldownExpireNotifier;
        this.cooldownExpireNotifier.registerCommand(this);
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        try {
            if (this.onReceiveCommand(commandSender, args)) {
                val senderName = commandSender.getName();
                val manager = this.cooldownExpireNotifier.getProxyBansManager();
                val time = manager.getConfigFile().getCooldownTime(manager.getPermHook().getUserGroup(senderName), this.getName());
                if (time > 0L) this.cooldownExpireNotifier.addCooldown(this.getName(), senderName, time);
            }
        } catch (RuntimeException exception) {
            commandSender.sendMessage(TextComponent.fromLegacyText(exception.getMessage()));
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

    protected String parseRule() {
        return ""; // TODO: 26.06.2023
    }

    protected long parseTimme() {
        return 0L; // TODO: 26.06.2023
    }


}
