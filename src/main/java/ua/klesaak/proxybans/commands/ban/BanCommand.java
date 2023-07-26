package ua.klesaak.proxybans.commands.ban;

import lombok.val;
import net.md_5.bungee.api.CommandSender;
import ua.klesaak.proxybans.manager.ProxyBansManager;
import ua.klesaak.proxybans.rules.PunishType;
import ua.klesaak.proxybans.rules.RuleData;
import ua.klesaak.proxybans.storage.PunishData;
import ua.klesaak.proxybans.utils.command.AbstractPunishCommand;

import static ua.klesaak.proxybans.config.MessagesFile.*;

public final class BanCommand extends AbstractPunishCommand {

    public BanCommand(ProxyBansManager proxyBansManager) {
        super(proxyBansManager, "ban", "");
    }

    @Override
    public boolean onReceiveCommand(CommandSender sender, String[] args) {
        this.cmdVerifyArgs(3, args, this.proxyBansManager.getMessagesFile().getUsageBanCommand());
        String nickName = this.cmdVerifyNickname(sender, args);
        this.checkOffline(sender, nickName);
        RuleData rule = this.parseRule(sender, PunishType.BAN, 1, args);
        String punisherName = this.cmdVerifyPunisher(sender);
        String comment = this.parseComment(2, args);
        String punishServer = this.parseServer(sender);
        String playerServer = this.parseServer(nickName);
        String date = this.proxyBansManager.getConfigFile().parseDate(System.currentTimeMillis());
        val messagesFile = this.proxyBansManager.getMessagesFile();
        messagesFile.getBroadcastBanned()
                .tag(PUNISHER_NAME_PATTERN, punisherName)
                .tag(RULE_PATTERN, rule.getRule())
                .tag(RULE_TEXT_PATTERN, rule.getText())
                .tag(COMMENT_TEXT_PATTERN, comment)
                .tag(PUNISH_SERVER_PATTERN, punishServer)
                .tag(PLAYER_SERVER_PATTERN, playerServer)
                .tag(DATE_PATTERN, date).broadcast();
        this.disconnect(nickName, messagesFile.getMessageBanned()
                .tag(PUNISHER_NAME_PATTERN, punisherName)
                .tag(RULE_PATTERN, rule.getRule())
                .tag(RULE_TEXT_PATTERN, rule.getText())
                .tag(COMMENT_TEXT_PATTERN, comment)
                .tag(PUNISH_SERVER_PATTERN, punishServer)
                .tag(PLAYER_SERVER_PATTERN, playerServer)
                .tag(DATE_PATTERN, date));

        PunishData.builder()
                .playerName(nickName)
                .punishType(PunishType.BAN)
                .rule(rule.getRuleData())
                .punisherName(punisherName)
                .comment(comment)
                .punisherServer(punishServer)
                .server(playerServer)
                .punishDate(date).build();

        // TODO: 18.07.2023 save to storage Method
        return true;
    }

    @Override
    public Iterable<String> onTabCompleteCommand(CommandSender commandSender, String[] args) {
        return null;
    }
}
