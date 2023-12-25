package ua.klesaak.proxybans.commands.punishlist;

import lombok.val;
import net.md_5.bungee.api.CommandSender;
import ua.klesaak.proxybans.manager.ProxyBansManager;
import ua.klesaak.proxybans.storage.PunishData;
import ua.klesaak.proxybans.utils.Paginated;
import ua.klesaak.proxybans.utils.command.AbstractCommandException;
import ua.klesaak.proxybans.utils.command.AbstractPunishCommand;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static ua.klesaak.proxybans.config.MessagesFile.*;

public final class BanListCommand extends AbstractPunishCommand {
    private static final int MAX_PAGE_SIZE = 7;

    public BanListCommand(ProxyBansManager proxyBansManager) {
        super(proxyBansManager, "ban-list");
    }

    @Override
    public boolean onReceiveCommand(CommandSender sender, String[] args) throws AbstractCommandException {
        val messagesFile = this.proxyBansManager.getMessagesFile();
        val messageHeader = messagesFile.getMessageBanListHeader();
        val messageBanListEntry = messagesFile.getMessageBanListFormatEntry();
        val messageFooter = messagesFile.getMessageBanListFooter();
        List<PunishData> actualBans = new ArrayList<>(this.getPunishedPlayersBy(punishData -> punishData.getPunishType().isBan()));
        Paginated<PunishData> bans = new Paginated<>(actualBans);
        if (bans.getContent().isEmpty()) {
            messagesFile.getMessageBanListEmpty().send(sender);
            return false;
        }
        int pageIndex = 1;
        if (args.length != 0) {
            pageIndex = this.parsePage(args[0]);
        }
        int maxPages = bans.getMaxPages(MAX_PAGE_SIZE);
        if (pageIndex > maxPages) pageIndex = maxPages;
        val punishPage = bans.getPage(pageIndex, MAX_PAGE_SIZE);
        if (punishPage == null) this.parsePage(null);

        messageHeader
                .tag(PAGE_PATTERN, String.valueOf(pageIndex))
                .tag(PAGES_PATTERN, String.valueOf(maxPages))
                .send(sender);
        assert punishPage != null;
        punishPage.forEach(punishDataEntry -> {
            val punishData = punishDataEntry.value();
            String isOpBan = punishData.getPunishType().isOPBan() ? "Да." : "Нет.";
            String isIpBan = punishData.getIp() == null ? "Нет." : "Да.";
            String time = punishData.getPunishExpireDate() == null ? "Навсегда." : punishData.getPunishExpireDate();

            messageBanListEntry
                    .tag(INDEX_PATTERN, String.valueOf(punishDataEntry.position()))
                    .tag(PUNISHER_NAME_PATTERN, punishData.getPunisherName())
                    .tag(PLAYER_NAME_PATTERN, punishData.getPlayerName())
                    .tag(PLAYER_SERVER_PATTERN, punishData.getServer())
                    .tag(PUNISH_SERVER_PATTERN, punishData.getPunisherServer())
                    .tag(RULE_PATTERN, punishData.getRule())
                    .tag(COMMENT_TEXT_PATTERN, punishData.getComment())
                    .tag(DATE_PATTERN, punishData.getPunishDate())
                    .tag(IS_OP_PUNISH_PATTERN, isOpBan)
                    .tag(IS_IP_BAN_PATTERN, isIpBan)
                    .tag(TIME_PATTERN, time)
                    .send(sender);
        });
        messageFooter.send(sender);

        return false;//возвращаем именно фолс, потому что не нужно включать кд
    }

    @Override
    public Iterable<String> onTabSuggest(CommandSender commandSender, String[] args) {
        return Collections.emptyList();
    }
}
