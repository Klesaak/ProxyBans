package ua.klesaak.proxybans.commands.punish.unmute;

import lombok.val;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import ua.klesaak.proxybans.manager.ProxyBansManager;
import ua.klesaak.proxybans.rules.PunishType;
import ua.klesaak.proxybans.utils.command.AbstractCommandException;
import ua.klesaak.proxybans.utils.command.AbstractPunishCommand;

import java.util.ArrayList;
import java.util.Collections;

public final class UnMuteAllCommand extends AbstractPunishCommand {

    public UnMuteAllCommand(ProxyBansManager proxyBansManager) {
        super(proxyBansManager, "unmute-all-by");
    }

    @Override
    public boolean onReceiveCommand(CommandSender sender, String[] args) throws AbstractCommandException {
        this.cmdVerifyArgs( 1, args, "/unmute-all-by <nickname> - remove all mutes by this user.");
        val nickName = args[0].toLowerCase();
        this.proxyBansManager.getPunishStorage().unMuteAllBy(nickName);
        sender.sendMessage(TextComponent.fromLegacyText(ChatColor.GREEN + "[ProxyBans]: All mutes by " + nickName + ChatColor.GREEN + " was removed!"));
        return false;//возвращаем именно фолс, потому что не нужно включать кд
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