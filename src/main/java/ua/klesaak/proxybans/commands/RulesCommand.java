package ua.klesaak.proxybans.commands;

import lombok.val;
import net.md_5.bungee.api.CommandSender;
import ua.klesaak.proxybans.manager.ProxyBansManager;
import ua.klesaak.proxybans.utils.command.AbstractPunishCommand;

import java.util.Collections;

import static ua.klesaak.proxybans.config.MessagesFile.*;

public class RulesCommand extends AbstractPunishCommand {

    public RulesCommand(ProxyBansManager proxyBansManager) {
        super(proxyBansManager, "rules");
    }

    @Override
    public boolean onReceiveCommand(CommandSender sender, String[] args) {
        val message = this.proxyBansManager.getMessagesFile().getMessageRuleListFormat();
        val configFile = this.proxyBansManager.getConfigFile();
        if (args.length == 0) {
            message.tag(PAGE_PATTERN, String.valueOf(1))
                    .tag(RULES_PATTERN, configFile.getRulesPage(1))
                    .tag(PAGES_PATTERN, String.valueOf(configFile.getRulesPagesSize())).send(sender);
            return false;
        }
        int pageIndex = this.parsePage(args[0]);
        val rulePage = configFile.getRulesPage(pageIndex);
        if (rulePage == null) this.parsePage(null);
        message.tag(PAGE_PATTERN, String.valueOf(pageIndex))
                .tag(RULES_PATTERN, configFile.getRulesPage(pageIndex))
                .tag(PAGES_PATTERN, String.valueOf(configFile.getRulesPagesSize())).send(sender);
        return false;//возвращаем именно фолс, потому что не нужно включать кд
    }

    @Override
    public Iterable<String> onTabSuggest(CommandSender commandSender, String[] args) {
        return Collections.emptyList();
    }
}
