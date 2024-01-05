package ua.klesaak.proxybans.commands.punish;

import lombok.val;
import net.md_5.bungee.api.CommandSender;
import ua.klesaak.proxybans.manager.ProxyBansManager;
import ua.klesaak.proxybans.rules.PunishType;
import ua.klesaak.proxybans.rules.RuleData;
import ua.klesaak.proxybans.utils.command.AbstractPunishCommand;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;

import static ua.klesaak.proxybans.config.MessagesFile.*;

public final class KickCommand extends AbstractPunishCommand {

    public KickCommand(ProxyBansManager proxyBansManager) {
        super(proxyBansManager, PunishType.KICK);
    }

    @Override
    public boolean onReceiveCommand(CommandSender sender, String[] args) {
        val messagesFile = this.proxyBansManager.getMessagesFile();
        this.cmdVerifyArgs(3, args, messagesFile.getUsageKickCommand());
        String nickName = this.cmdVerifyNickname(sender, true, args);
        RuleData rule = this.parseRule(sender,1, args);
        String punisherName = this.cmdVerifySender(sender);
        String comment = this.parseComment(sender,2, args);
        String punishServer = this.parseServer(sender);
        String playerServer = this.parseServer(nickName);
        String date = this.proxyBansManager.getConfigFile().parseDate(Instant.now());
        messagesFile.getMessageKickSuccess().tag(PLAYER_NAME_PATTERN, nickName).send(sender);
        messagesFile.getBroadcastKicked()
                .tag(PUNISHER_NAME_PATTERN, punisherName)
                .tag(RULE_PATTERN, rule.getRule())
                .tag(PLAYER_NAME_PATTERN, nickName)
                .tag(RULE_TEXT_PATTERN, rule.getText())
                .tag(COMMENT_TEXT_PATTERN, comment)
                .tag(PUNISH_SERVER_PATTERN, punishServer)
                .tag(PLAYER_SERVER_PATTERN, playerServer)
                .tag(DATE_PATTERN, date).broadcast();
        this.disconnect(nickName, messagesFile.getMessageKicked()
                .tag(PUNISHER_NAME_PATTERN, punisherName)
                .tag(RULE_PATTERN, rule.getRuleData())
                .tag(COMMENT_TEXT_PATTERN, comment)
                .tag(PUNISH_SERVER_PATTERN, punishServer)
                .tag(PLAYER_SERVER_PATTERN, playerServer)
                .tag(DATE_PATTERN, date));
        return true;
    }

    @Override
    public Iterable<String> onTabSuggest(CommandSender commandSender, String[] args) {
        switch (args.length) {
            case 1: {
                return this.copyPartialMatches(args[0].toLowerCase(), this.getOnlinePlayers(), new ArrayList<>());
            }
            case 2: {
                return this.copyPartialMatches(args[1].toLowerCase(), this.getActualRules(), new ArrayList<>());
            }
        }
        return Collections.emptyList();
    }
}
