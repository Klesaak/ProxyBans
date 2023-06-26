package ua.klesaak.proxybans.commands.ban;

import net.md_5.bungee.api.CommandSender;
import ua.klesaak.proxybans.manager.PermissionsConstants;
import ua.klesaak.proxybans.manager.ProxyBansManager;
import ua.klesaak.proxybans.utils.command.AbstractPunishCommand;

public class BanCommand extends AbstractPunishCommand {
    private final ProxyBansManager proxyBansManager;

    public BanCommand(ProxyBansManager proxyBansManager) {
        super(proxyBansManager.getProxyBansPlugin(), "ban", PermissionsConstants.BAN_PERMISSION);
        this.proxyBansManager = proxyBansManager;
    }

    @Override
    public void onReceiveCommand(CommandSender sender, String label, String[] args) {

    }

    @Override
    public Iterable<String> onTabCompleteCommand(CommandSender commandSender, String[] args) {
        return null;
    }
}
