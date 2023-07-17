package ua.klesaak.proxybans.commands.ban;

import net.md_5.bungee.api.CommandSender;
import ua.klesaak.proxybans.manager.PermissionsConstants;
import ua.klesaak.proxybans.manager.ProxyBansManager;
import ua.klesaak.proxybans.rules.PunishType;
import ua.klesaak.proxybans.rules.RuleData;
import ua.klesaak.proxybans.utils.command.AbstractPunishCommand;

public final class BanCommand extends AbstractPunishCommand {

    public BanCommand(ProxyBansManager proxyBansManager) {
        super(proxyBansManager, "ban", PermissionsConstants.BAN_PERMISSION);
    }

    @Override
    public boolean onReceiveCommand(CommandSender sender, String[] args) {
        this.cmdVerifyArgs(3, args, this.proxyBansManager.getMessagesFile().getUsageBanCommand());
        String nickName = this.cmdVerifyNickname(sender, args);
        this.checkOffline(sender, nickName);
        RuleData rule = this.parseRule(sender, PunishType.BAN, 2, args);
        String punisherName = this.cmdVerifyPunisher(sender);
        String comment = this.parseComment(3, args);
        String punishServer = this.parseServer(sender);
        String playerServer = this.parseServer(nickName);
        String date = this.proxyBansManager.getConfigFile().parseDate(System.currentTimeMillis());


        return true;
    }

    @Override
    public Iterable<String> onTabCompleteCommand(CommandSender commandSender, String[] args) {
        return null;
    }
}
