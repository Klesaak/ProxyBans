package ua.klesaak.proxybans.commands.punish.unban;

import lombok.val;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import ua.klesaak.proxybans.manager.ProxyBansManager;
import ua.klesaak.proxybans.rules.PunishType;
import ua.klesaak.proxybans.utils.command.AbstractPunishCommand;

import java.util.ArrayList;
import java.util.Collections;

public final class UnBanAllCommand extends AbstractPunishCommand {

    public UnBanAllCommand(ProxyBansManager proxyBansManager) {
        super(proxyBansManager, "unban-all-by");
    }

    @Override
    public boolean onReceiveCommand(CommandSender sender, String[] args) {
        this.cmdVerifyArgs( 1, args, "/unban-all-by <nickname> - remove all bans by this user.");
        val nickName = args[0].toLowerCase();
        this.proxyBansManager.getPunishStorage().unBanAllBy(nickName);
        sender.sendMessage(TextComponent.fromLegacyText(ChatColor.GREEN + "[ProxyBans]: All bans by " + nickName + ChatColor.GREEN + " was removed!"));
        return false;//возвращаем именно фолс, потому что не нужно включать кд
    }

    @Override
    public Iterable<String> onTabSuggest(CommandSender commandSender, String[] args) {
        if (args.length == 1) {
            val players = new ArrayList<String>();
            players.addAll(this.getPunishedPlayersBy(PunishType.BAN));
            players.addAll(this.getPunishedPlayersBy(PunishType.TEMP_BAN));
            players.addAll(this.getPunishedPlayersBy(PunishType.IP_BAN));

            players.addAll(this.getPunishedPlayersBy(PunishType.OP_BAN));
            players.addAll(this.getPunishedPlayersBy(PunishType.OP_TEMP_BAN));
            players.addAll(this.getPunishedPlayersBy(PunishType.OP_IP_BAN));
            return this.copyPartialMatches(args[0].toLowerCase(), players, new ArrayList<>());
        }
        return Collections.emptyList();
    }
}
