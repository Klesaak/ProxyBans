package ua.klesaak.proxybans.commands;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;
import ua.klesaak.proxybans.manager.PermissionsConstants;
import ua.klesaak.proxybans.manager.ProxyBansManager;

public final class ReloadCommand extends Command {
    private final ProxyBansManager proxyBansManager;

    public ReloadCommand(ProxyBansManager proxyBansManager) {
        super("proxybans-reload", PermissionsConstants.PREFIX_WILDCARD_PERMISSION + "reload");
        this.proxyBansManager = proxyBansManager;
        proxyBansManager.getProxyBansPlugin().getProxy().getPluginManager().registerCommand(proxyBansManager.getProxyBansPlugin(), this);
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        this.proxyBansManager.reloadConfigFiles();
        commandSender.sendMessage(TextComponent.fromLegacyText(ChatColor.GREEN + "ProxyBans config's successfully reloaded!"));
    }
}
