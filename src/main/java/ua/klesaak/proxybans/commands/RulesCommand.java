package ua.klesaak.proxybans.commands;

import lombok.val;
import net.md_5.bungee.api.CommandSender;
import ua.klesaak.proxybans.manager.ProxyBansManager;
import ua.klesaak.proxybans.utils.command.AbstractCommandException;
import ua.klesaak.proxybans.utils.command.AbstractPunishCommand;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static ua.klesaak.proxybans.config.MessagesFile.*;

public final class RulesCommand extends AbstractPunishCommand {

    public RulesCommand(ProxyBansManager proxyBansManager) {
        super(proxyBansManager, "rules");
    }

    @Override
    public boolean onReceiveCommand(CommandSender sender, String[] args) throws AbstractCommandException {
        val messagesFile = this.proxyBansManager.getMessagesFile();
        val message = messagesFile.getMessageRuleListFormat();
        val configFile = this.proxyBansManager.getConfigFile();
        int pageIndex = 1;
        if (args.length != 0) {
            pageIndex = this.parsePage(args[0]);
        }
        if (pageIndex > configFile.getMaxRulesPages()) pageIndex = configFile.getMaxRulesPages();
        val rulePage = configFile.getRulesPage(messagesFile, pageIndex);
        if (rulePage == null) this.parsePage(null);
        message.tag(PAGE_PATTERN, String.valueOf(pageIndex))
                .tag(RULES_PATTERN, rulePage)
                .tag(PAGES_PATTERN, String.valueOf(configFile.getMaxRulesPages()))
                .send(sender);
        return false;//возвращаем именно фолс, потому что не нужно включать кд
    }

    @Override
    public Iterable<String> onTabSuggest(CommandSender commandSender, String[] args) {
        if (args.length == 1) {
            List<String> pagesIndexes = new ArrayList<>();
            int pages = this.proxyBansManager.getConfigFile().getRulePages().getMaxPages(5);
            for (int i = 1; i <= pages; i++) {
                pagesIndexes.add(String.valueOf(i));
            }
            return this.copyPartialMatches(args[0].toLowerCase(), pagesIndexes, new ArrayList<>());
        }
        return Collections.emptyList();
    }
}
