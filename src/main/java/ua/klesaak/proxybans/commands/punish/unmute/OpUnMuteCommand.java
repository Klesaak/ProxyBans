package ua.klesaak.proxybans.commands.punish.unmute;

import lombok.val;
import net.md_5.bungee.api.CommandSender;
import ua.klesaak.proxybans.manager.ProxyBansManager;
import ua.klesaak.proxybans.rules.PunishType;
import ua.klesaak.proxybans.utils.command.AbstractCommandException;
import ua.klesaak.proxybans.utils.command.AbstractPunishCommand;

import java.util.ArrayList;
import java.util.Collections;

public final class OpUnMuteCommand extends AbstractPunishCommand {

    public OpUnMuteCommand(ProxyBansManager proxyBansManager) {
        super(proxyBansManager, "op-unmute");
    }

    @Override
    public boolean onReceiveCommand(CommandSender sender, String[] args) throws AbstractCommandException {
        val messagesFile = this.proxyBansManager.getMessagesFile();
        this.cmdVerifyArgs(1, args, messagesFile.getUsageOpUnmuteCommand());
        this.cmdVerifyTryUnmute(sender, args[0], true);
        return false;//возвращаем именно фолс, потому что не нужно включать кд
    }

    @Override
    public Iterable<String> onTabSuggest(CommandSender commandSender, String[] args) {
        if (args.length == 1) {
            val players = new ArrayList<String>();
            players.addAll(this.getPunishedPlayersBy(PunishType.MUTE));
            players.addAll(this.getPunishedPlayersBy(PunishType.TEMP_MUTE));

            players.addAll(this.getPunishedPlayersBy(PunishType.OP_MUTE));
            players.addAll(this.getPunishedPlayersBy(PunishType.OP_TEMP_MUTE));
            return this.copyPartialMatches(args[0].toLowerCase(), players, new ArrayList<>());
        }
        return Collections.emptyList();
    }
}