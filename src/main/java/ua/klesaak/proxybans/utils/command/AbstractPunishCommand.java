package ua.klesaak.proxybans.utils.command;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.TabExecutor;

public abstract class AbstractPunishCommand extends Command implements TabExecutor {

    public AbstractPunishCommand(Plugin plugin, String commandName, String permission, String... aliases) {
        super(commandName, permission, aliases);
        plugin.getProxy().getPluginManager().registerCommand(plugin, this);
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        try {
            this.onReceiveCommand(commandSender, this.getName(), args);
        } catch (RuntimeException exception) {
            commandSender.sendMessage(TextComponent.fromLegacyText(exception.getMessage()));
        }
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender commandSender, String[] args) {
        return this.onTabCompleteCommand(commandSender, args);
    }

    public abstract void onReceiveCommand(CommandSender sender, String label, String[] args);

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
