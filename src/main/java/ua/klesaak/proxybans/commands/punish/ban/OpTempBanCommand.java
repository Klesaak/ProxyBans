package ua.klesaak.proxybans.commands.punish.ban;

import lombok.val;
import net.md_5.bungee.api.CommandSender;
import ua.klesaak.proxybans.manager.ProxyBansManager;
import ua.klesaak.proxybans.rules.PunishType;
import ua.klesaak.proxybans.rules.RuleData;
import ua.klesaak.proxybans.storage.PunishData;
import ua.klesaak.proxybans.utils.command.AbstractCommandException;
import ua.klesaak.proxybans.utils.command.AbstractPunishCommand;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;

import static ua.klesaak.proxybans.config.MessagesFile.*;

public final class OpTempBanCommand extends AbstractPunishCommand {

    public OpTempBanCommand(ProxyBansManager proxyBansManager) {
        super(proxyBansManager, PunishType.OP_TEMP_BAN);
    }

    @Override
    public boolean onReceiveCommand(CommandSender sender, String[] args) throws AbstractCommandException {
        val messagesFile = this.proxyBansManager.getMessagesFile();
        val configFile = this.proxyBansManager.getConfigFile();
        this.cmdVerifyArgs(4, args, messagesFile.getUsageOpTempBanCommand());
        String nickName = this.cmdVerifyNickname(sender, true, args);
        long punishTime = this.parseTime(sender, 1, args);
        RuleData rule = this.parseRule(sender, 2, args);
        String punisherName = this.cmdVerifySender(sender);
        String comment = this.parseComment(sender, 3, args);
        String punishServer = this.parseServer(sender);
        String playerServer = this.parseServer(nickName);
        String punishExpireDate = configFile.parseDate(Instant.ofEpochSecond(punishTime));
        String date = configFile.parseDate(Instant.now());
        messagesFile.getBroadcastOpTempBanned()
                .tag(PUNISHER_NAME_PATTERN, punisherName)
                .tag(RULE_PATTERN, rule.getRule())
                .tag(PLAYER_NAME_PATTERN, nickName)
                .tag(RULE_TEXT_PATTERN, rule.getText())
                .tag(COMMENT_TEXT_PATTERN, comment)
                .tag(PUNISH_SERVER_PATTERN, punishServer)
                .tag(PLAYER_SERVER_PATTERN, playerServer)
                .tag(TIME_PATTERN, punishExpireDate)
                .tag(DATE_PATTERN, date).broadcast();
        this.disconnect(nickName, messagesFile.getMessageOpTempBanned()
                .tag(PUNISHER_NAME_PATTERN, punisherName)
                .tag(RULE_PATTERN, rule.getRuleData())
                .tag(COMMENT_TEXT_PATTERN, comment)
                .tag(PUNISH_SERVER_PATTERN, punishServer)
                .tag(PLAYER_SERVER_PATTERN, playerServer)
                .tag(TIME_PATTERN, punishExpireDate)
                .tag(DATE_PATTERN, date));

        val punishData = PunishData.builder()
                .playerName(nickName)
                .punishType(this.punishType)
                .rule(rule.getRuleData())
                .punisherName(punisherName)
                .comment(comment)
                .punisherServer(punishServer)
                .server(playerServer)
                .punishTimeExpire(punishTime)
                .punishExpireDate(punishExpireDate)
                .punishDate(date).build();

        this.proxyBansManager.getPunishStorage().saveBan(punishData);
        return true;
    }

    @Override
    public Iterable<String> onTabSuggest(CommandSender commandSender, String[] args) {
        switch (args.length) {
            case 1: {
                return this.copyPartialMatches(args[0].toLowerCase(), this.getOnlinePlayers(), new ArrayList<>());
            }
            case 3: {
                return this.copyPartialMatches(args[2].toLowerCase(), this.getActualRules(), new ArrayList<>());
            }
        }
        return Collections.emptyList();
    }
}
