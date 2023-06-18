package ua.klesaak.proxybans.manager;

import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PunishListener implements Listener {
    private final ProxyBansManager manager;

    public PunishListener(ProxyBansManager manager) {
        this.manager = manager;
        manager.getProxyBansPlugin().getProxy().getPluginManager().registerListener(manager.getProxyBansPlugin(), this);
    }

    @EventHandler
    public void onPlayerJoin(LoginEvent event) {
        event.setCancelReason(manager.getMessagesFile().getMessageKicked().getMessage());
        event.setCancelled(true);
    }


    @EventHandler
    public void onPlayerChat(ChatEvent event) {
    }
}
