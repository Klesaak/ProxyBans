package ua.klesaak.proxybans.commands.unban;

import lombok.val;
import net.md_5.bungee.api.CommandSender;
import ua.klesaak.proxybans.manager.ProxyBansManager;
import ua.klesaak.proxybans.rules.PunishType;
import ua.klesaak.proxybans.utils.command.AbstractPunishCommand;

import java.util.ArrayList;
import java.util.Collections;

import static ua.klesaak.proxybans.config.MessagesFile.*;

public final class UnBanCommand extends AbstractPunishCommand {

    public UnBanCommand(ProxyBansManager proxyBansManager) {
        super(proxyBansManager, "unban");
    }

    @Override
    public boolean onReceiveCommand(CommandSender sender, String[] args) {
        val messagesFile = this.proxyBansManager.getMessagesFile();
        val message = messagesFile.getBroadcastUnbanned();
        val senderName = this.cmdVerifySender(sender);
        this.cmdVerifyArgs(1, args, messagesFile.getUsageUnbanCommand());
        val nickName = args[0];
        this.cmdVerifyTryUnban(nickName);
        message.tag(PUNISHER_NAME_PATTERN, senderName)
                .tag(PLAYER_NAME_PATTERN, nickName).broadcast();
        return false;//возвращаем именно фолс, потому что не нужно включать кд
    }

    @Override
    public Iterable<String> onTabSuggest(CommandSender commandSender, String[] args) {
        if (args.length == 1) {
            val players = new ArrayList<String>();
            players.addAll(this.getPunishedPlayersBy(PunishType.BAN));
            players.addAll(this.getPunishedPlayersBy(PunishType.TEMP_BAN));
            players.addAll(this.getPunishedPlayersBy(PunishType.IP_BAN));
            return this.copyPartialMatches(args[0].toLowerCase(), players, new ArrayList<>());
        }
        return Collections.emptyList();
    }
}
