package ua.klesaak.proxybans.commands.info;

import lombok.val;
import net.md_5.bungee.api.CommandSender;
import ua.klesaak.proxybans.manager.ProxyBansManager;
import ua.klesaak.proxybans.rules.PunishType;
import ua.klesaak.proxybans.utils.command.AbstractPunishCommand;

import java.util.ArrayList;
import java.util.Collections;

import static ua.klesaak.proxybans.config.MessagesFile.*;

public final class MuteInfoCommand extends AbstractPunishCommand {

    public MuteInfoCommand(ProxyBansManager proxyBansManager) {
        super(proxyBansManager, "mute-info");
    }

    @Override
    public boolean onReceiveCommand(CommandSender sender, String[] args) {
        val messagesFile = this.proxyBansManager.getMessagesFile();
        this.cmdVerifyArgs(1, args, messagesFile.getUsageMuteInfo());
        val nickName = args[0].toLowerCase();
        val storage = this.proxyBansManager.getPunishStorage();
        val punishData = storage.getMuteData(nickName);
        if (punishData == null || storage.unMuteIsExpired(nickName)) {
            messagesFile.getMessageInfoNotFound().send(sender);
            return false;
        }
        String isOpMute = punishData.getPunishType().isOPMute() ? "Да." : "Нет.";
        String time = punishData.getPunishExpireDate() == null ? "Навсегда." : punishData.getPunishExpireDate();

        messagesFile.getMessageMuteInfoFormat()
                .tag(PUNISHER_NAME_PATTERN, punishData.getPunisherName())
                .tag(PLAYER_NAME_PATTERN, punishData.getPlayerName())
                .tag(PLAYER_SERVER_PATTERN, punishData.getServer())
                .tag(PUNISH_SERVER_PATTERN, punishData.getPunisherServer())
                .tag(RULE_PATTERN, punishData.getRule())
                .tag(COMMENT_TEXT_PATTERN, punishData.getComment())
                .tag(DATE_PATTERN, punishData.getPunishDate())
                .tag(IS_OP_PUNISH_PATTERN, isOpMute)
                .tag(TIME_PATTERN, time)
                .send(sender);
        return false;
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
