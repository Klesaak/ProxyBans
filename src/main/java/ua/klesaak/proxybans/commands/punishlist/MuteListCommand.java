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

public final class MuteListCommand extends AbstractPunishCommand {
    private static final int MAX_PAGE_SIZE = 7;

    public MuteListCommand(ProxyBansManager proxyBansManager) {
        super(proxyBansManager, "mute-list");
    }

    @Override
    public boolean onReceiveCommand(CommandSender sender, String[] args) throws AbstractCommandException {
        val messagesFile = this.proxyBansManager.getMessagesFile();
        val messageHeader = messagesFile.getMessageMuteListHeader();
        val messageMuteListEntry = messagesFile.getMessageMuteListFormatEntry();
        val messageFooter = messagesFile.getMessageMuteListFooter();
        List<PunishData> actualMutes = new ArrayList<>(this.getPunishedPlayersBy(punishData -> !punishData.getPunishType().isBan()));
        Paginated<PunishData> mutes = new Paginated<>(actualMutes);
        if (mutes.getContent().isEmpty()) {
            messagesFile.getMessageMuteListEmpty().send(sender);
            return false;
        }
        int pageIndex = 1;
        if (args.length != 0) {
            pageIndex = this.parsePage(args[0]);
        }
        int maxPages = mutes.getMaxPages(MAX_PAGE_SIZE);
        if (pageIndex > maxPages) pageIndex = maxPages;
        val punishPage = mutes.getPage(pageIndex, MAX_PAGE_SIZE);
        if (punishPage == null) this.parsePage(null);

        messageHeader
                .tag(PAGE_PATTERN, String.valueOf(pageIndex))
                .tag(PAGES_PATTERN, String.valueOf(maxPages))
                .send(sender);
        assert punishPage != null;
        punishPage.forEach(punishDataEntry -> {
            val punishData = punishDataEntry.value();
            String isOpMute = punishData.getPunishType().isOPMute() ? "Да." : "Нет.";
            String time = punishData.getPunishExpireDate() == null ? "Навсегда." : punishData.getPunishExpireDate();

            messageMuteListEntry
                    .tag(INDEX_PATTERN, String.valueOf(punishDataEntry.position()))
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
        });
        messageFooter.send(sender);

        return false;//возвращаем именно фолс, потому что не нужно включать кд
    }

    @Override
    public Iterable<String> onTabSuggest(CommandSender commandSender, String[] args) {
        return Collections.emptyList();
    }
}