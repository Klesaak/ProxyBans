package ua.klesaak.proxybans.manager;

import lombok.val;
import net.md_5.bungee.api.connection.ProxiedPlayer;
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
            if (storage.unBanIsExpired(nickName)) return;
            val cancelReason = this.tagPunishMessage(punishData);
            event.setCancelled(true);
            event.setCancelReason(cancelReason.getMessageComponent());
        }
    }

    private Message tagPunishMessage(PunishData punishData) {
        Message cancelReason = Message.create("deff", false, true);
        val messagesFile = this.manager.getMessagesFile();
        switch (punishData.getPunishType()) {
            case BAN:{
                cancelReason = messagesFile.getMessageBanned();
                break;
            }
            case TEMP_BAN: {
                cancelReason = messagesFile.getMessageTempBanned();
                break;
            }
            case IP_BAN: {
                cancelReason = messagesFile.getMessageBannedIp();
                break;
            }
            case OP_BAN: {
                cancelReason = messagesFile.getMessageOpBanned();
                break;
            }
            case OP_TEMP_BAN: {
                cancelReason = messagesFile.getMessageOpTempBanned();
                break;
            }
            case OP_IP_BAN: {
                cancelReason = messagesFile.getMessageOpIpBanned();
                break;
            }
            case MUTE: {
                cancelReason = messagesFile.getMessageMuted();
                break;
            }
            case TEMP_MUTE: {
                cancelReason = messagesFile.getMessageTempMuted();
                break;
            }
            case OP_MUTE: {
                cancelReason = messagesFile.getMessageOpMuted();
                break;
            }
            case OP_TEMP_MUTE: {
                cancelReason = messagesFile.getMessageOpTempMuted();
                break;
            }
        }
        return cancelReason.tag(PUNISHER_NAME_PATTERN, punishData.getPunisherName())
                .tag(RULE_PATTERN, punishData.getRule())
                .tag(COMMENT_TEXT_PATTERN, punishData.getComment())
                .tag(PUNISH_SERVER_PATTERN, punishData.getPunisherServer())
                .tag(PLAYER_SERVER_PATTERN, punishData.getServer())
                .tag(TIME_PATTERN, punishData.getPunishExpireDate())
                .tag(DATE_PATTERN, punishData.getPunishDate());
    }


    @EventHandler
    public void onPlayerChat(ChatEvent event) {
        if (!(event.getSender() instanceof ProxiedPlayer)) return;
        ProxiedPlayer proxiedPlayer = (ProxiedPlayer) event.getSender();
        val storage = this.manager.getPunishStorage();
        val nickName = proxiedPlayer.getName().toLowerCase();
        val punishData = storage.getMuteData(nickName);
        if (punishData != null) {
            if (storage.unMuteIsExpired(nickName)) return;
            if (event.isCommand() && !this.manager.getConfigFile().checkBlackListCommandOnMute(event)) return; // TODO: TEST this shiiiiiiiiitt
            val cancelReason = this.tagPunishMessage(punishData);
            cancelReason.send(proxiedPlayer);
            event.setCancelled(true);
        }
    }
}
