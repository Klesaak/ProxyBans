package ua.klesaak.proxybans.manager;

import lombok.val;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

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
            val messagesFile = this.manager.getMessagesFile();
            BaseComponent[] cancelReason = null;
            switch (punishData.getPunishType()) {
                case BAN: {
                    cancelReason = messagesFile.getMessageBanned()
                            .tag(PUNISHER_NAME_PATTERN, punishData.getPunisherName())
                            .tag(RULE_PATTERN, punishData.getRule())
                            .tag(COMMENT_TEXT_PATTERN, punishData.getComment())
                            .tag(PUNISH_SERVER_PATTERN, punishData.getPunisherServer())
                            .tag(PLAYER_SERVER_PATTERN, punishData.getServer())
                            .tag(DATE_PATTERN, punishData.getPunishDate())
                            .getMessageComponent();
                    break;
                }
                case TEMP_BAN: {

                    break;
                }
                case IP_BAN: {

                    break;
                }
                case OP_BAN: {
                    cancelReason = messagesFile.getMessageOpBanned()
                            .tag(PUNISHER_NAME_PATTERN, punishData.getPunisherName())
                            .tag(RULE_PATTERN, punishData.getRule())
                            .tag(COMMENT_TEXT_PATTERN, punishData.getComment())
                            .tag(PUNISH_SERVER_PATTERN, punishData.getPunisherServer())
                            .tag(PLAYER_SERVER_PATTERN, punishData.getServer())
                            .tag(DATE_PATTERN, punishData.getPunishDate())
                            .getMessageComponent();
                    break;
                }
                case OP_TEMP_BAN: {

                    break;
                }
                case OP_IP_BAN: {

                    break;
                }
            }
            event.setCancelled(true);
            event.setCancelReason(cancelReason);
        }
    }


    @EventHandler
    public void onPlayerChat(ChatEvent event) {
    }
}
