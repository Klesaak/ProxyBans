package ua.klesaak.proxybans.manager;

import ua.klesaak.proxybans.ProxyBansPlugin;
import ua.klesaak.proxybans.config.MessagesFile;

import java.io.File;

public class ProxyBansManager {
    private final ProxyBansPlugin proxyBansPlugin;
    private MessagesFile messagesFile;

    public ProxyBansManager(ProxyBansPlugin proxyBansPlugin) {
        this.proxyBansPlugin = proxyBansPlugin;
    }

    public void reload() {
        this.messagesFile = new MessagesFile().load(new File(this.proxyBansPlugin.getDataFolder(), "messages.json"));
    }
}
