package ua.klesaak.proxybans.manager;

import lombok.val;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import ua.klesaak.proxybans.storage.PunishData;
import ua.klesaak.proxybans.utils.messages.Message;

import static ua.klesaak.proxybans.config.MessagesFile.*;

public class PunishListener implements Listener {
    private final ProxyBansManager manager;

    public PunishListener(ProxyBansManager manager) {
        this.manager = manager;
        manager.getProxyBansPlugin().getProxy().getPluginManager().registerListener(manager.getProxyBansPlugin(), this);
    }

    @EventHandler
    public void onPlayerJoin(LoginEvent event) {
        val storage = this.manager.getPunishStorage();
        val nickName = event.getConnection().getName().toLowerCase();
        val punishData = storage.getBanData(nickName);
        if (punishData != null) {
            BaseComponent[] cancelReason = this.tagPunishMessage(punishData);
            event.setCancelled(true);
            event.setCancelReason(cancelReason);
        }
    }

    private BaseComponent[] tagPunishMessage(PunishData punishData) {
        Message cancelReason = null;
        val messagesFile = this.manager.getMessagesFile();
        switch (punishData.getPunishType()) {
            case BAN: cancelReason = messagesFile.getMessageBanned();
            case TEMP_BAN: {

                break;
            }
            case IP_BAN: {

                break;
            }
            case OP_BAN: {
                cancelReason = messagesFile.getMessageOpBanned();
                break;
            }
            case OP_TEMP_BAN: {

                break;
            }
            case OP_IP_BAN: {

                break;
            }
        }
        cancelReason.tag(PUNISHER_NAME_PATTERN, punishData.getPunisherName())
                .tag(RULE_PATTERN, punishData.getRule())
                .tag(COMMENT_TEXT_PATTERN, punishData.getComment())
                .tag(PUNISH_SERVER_PATTERN, punishData.getPunisherServer())
                .tag(PLAYER_SERVER_PATTERN, punishData.getServer())
                .tag(DATE_PATTERN, punishData.getPunishDate());
        return cancelReason.getMessageComponent();
    }


    @EventHandler
    public void onPlayerChat(ChatEvent event) {
    }
}
