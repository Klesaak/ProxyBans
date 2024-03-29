package ua.klesaak.proxybans.commands.punish.ban;

import lombok.val;
import net.md_5.bungee.api.CommandSender;
import ua.klesaak.proxybans.manager.ProxyBansManager;
import ua.klesaak.proxybans.rules.PunishType;
import ua.klesaak.proxybans.rules.RuleData;
import ua.klesaak.proxybans.storage.PunishData;
import ua.klesaak.proxybans.utils.command.AbstractPunishCommand;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;

import static ua.klesaak.proxybans.config.MessagesFile.*;

public final class OpBanIPCommand extends AbstractPunishCommand {

    public OpBanIPCommand(ProxyBansManager proxyBansManager) {
        super(proxyBansManager, PunishType.OP_IP_BAN);
    }

    @Override
    public boolean onReceiveCommand(CommandSender sender, String[] args) {
        val messagesFile = this.proxyBansManager.getMessagesFile();
        this.cmdVerifyArgs(3, args, messagesFile.getUsageOpBanIpCommand());
        String nickName = this.cmdVerifyNickname(sender, true, args);
        RuleData rule = this.parseRule(sender,1, args);
        String punisherName = this.cmdGetSenderName(sender);
        String comment = this.parseComment(sender,2, args);
        String punishServer = this.parseServer(sender);
        String playerServer = this.parseServer(nickName);
        String blockedIp = this.getIpAddress(nickName);
        String date = this.proxyBansManager.getConfigFile().parseDate(Instant.now());
        messagesFile.getMessageBanSuccess().tag(PLAYER_NAME_PATTERN, nickName).send(sender);
        messagesFile.getBroadcastOpIpBanned()
                .tag(PUNISHER_NAME_PATTERN, punisherName)
                .tag(RULE_PATTERN, rule.getRule())
                .tag(PLAYER_NAME_PATTERN, nickName)
                .tag(RULE_TEXT_PATTERN, rule.getText())
                .tag(COMMENT_TEXT_PATTERN, comment)
                .tag(PUNISH_SERVER_PATTERN, punishServer)
                .tag(PLAYER_SERVER_PATTERN, playerServer)
                .tag(DATE_PATTERN, date).broadcast();
        this.disconnect(nickName, messagesFile.getMessageOpIpBanned()
                .tag(PUNISHER_NAME_PATTERN, punisherName)
                .tag(RULE_PATTERN, rule.getRuleData())
                .tag(COMMENT_TEXT_PATTERN, comment)
                .tag(PUNISH_SERVER_PATTERN, punishServer)
                .tag(PLAYER_SERVER_PATTERN, playerServer)
                .tag(DATE_PATTERN, date));

        val punishData = PunishData.builder()
                .playerName(nickName)
                .ip(blockedIp)
                .punishType(this.punishType)
                .rule(rule.getRuleData())
                .punisherName(punisherName)
                .comment(comment)
                .punisherServer(punishServer)
                .server(playerServer)
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
            case 2: {
                return this.copyPartialMatches(args[1].toLowerCase(), this.getActualRules(commandSender), new ArrayList<>());
            }
        }
        return Collections.emptyList();
    }
}