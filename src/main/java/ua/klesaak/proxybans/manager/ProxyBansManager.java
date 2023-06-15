package ua.klesaak.proxybans.manager;

import ua.klesaak.proxybans.ProxyBansPlugin;
import ua.klesaak.proxybans.config.ConfigFile;
import ua.klesaak.proxybans.config.MessagesFile;

import java.io.File;

public class ProxyBansManager {
    private final ProxyBansPlugin proxyBansPlugin;
    private ConfigFile configFile;
    private MessagesFile messagesFile;

    public ProxyBansManager(ProxyBansPlugin proxyBansPlugin) {
        this.proxyBansPlugin = proxyBansPlugin;
    }

    public void reload() {
        this.configFile = new ConfigFile(this.proxyBansPlugin);
        this.messagesFile = new MessagesFile().load(new File(this.proxyBansPlugin.getDataFolder(), "messages.json"));
    }

    public void disable() {

    }
}
