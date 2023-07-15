package ua.klesaak.proxybans.commands.ban;

import net.md_5.bungee.api.CommandSender;
import ua.klesaak.proxybans.manager.PermissionsConstants;
import ua.klesaak.proxybans.manager.ProxyBansManager;
import ua.klesaak.proxybans.utils.command.AbstractPunishCommand;

public class BanCommand extends AbstractPunishCommand {

    public BanCommand(ProxyBansManager proxyBansManager) {
        super(proxyBansManager, "ban", PermissionsConstants.BAN_PERMISSION);
    }

    @Override
    public boolean onReceiveCommand(CommandSender sender, String[] args) {
        this.cmdVerifyArgs(3, args, this.proxyBansManager.getMessagesFile().getUsageBanCommand());
        String nickName = this.cmdVerifyNickname(args);
        String rule = this.parseRule(args);
        String punisherName = this.cmdVerifyPunisher(sender);
        String comment = this.parseComment(3, args);

        return true;
    }

    @Override
    public Iterable<String> onTabCompleteCommand(CommandSender commandSender, String[] args) {
        return null;
    }
}
