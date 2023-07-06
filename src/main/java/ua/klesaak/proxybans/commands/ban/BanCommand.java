package ua.klesaak.proxybans.commands.ban;

import net.md_5.bungee.api.CommandSender;
import ua.klesaak.proxybans.manager.PermissionsConstants;
import ua.klesaak.proxybans.manager.ProxyBansManager;
import ua.klesaak.proxybans.utils.command.AbstractPunishCommand;

public class BanCommand extends AbstractPunishCommand {
    private final ProxyBansManager proxyBansManager;

    public BanCommand(ProxyBansManager proxyBansManager) {
        super(proxyBansManager, "ban", PermissionsConstants.BAN_PERMISSION);
        this.proxyBansManager = proxyBansManager;
    }

    @Override
    public boolean onReceiveCommand(CommandSender sender, String[] args) {

        return true;
    }

    @Override
    public Iterable<String> onTabCompleteCommand(CommandSender commandSender, String[] args) {
        return null;
    }
}
