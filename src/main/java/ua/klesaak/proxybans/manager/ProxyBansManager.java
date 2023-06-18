package ua.klesaak.proxybans.manager;

import lombok.Getter;
import ua.klesaak.proxybans.ProxyBansPlugin;
import ua.klesaak.proxybans.config.ConfigFile;
import ua.klesaak.proxybans.config.MessagesFile;

import java.io.File;

@Getter
public class ProxyBansManager {
    private final ProxyBansPlugin proxyBansPlugin;
    private ConfigFile configFile;
    private MessagesFile messagesFile;

    public ProxyBansManager(ProxyBansPlugin proxyBansPlugin) {
        this.proxyBansPlugin = proxyBansPlugin;
        this.reloadConfigFiles();
        new PunishListener(this);
    }

    public void reloadConfigFiles() {
        this.configFile = new ConfigFile(this.proxyBansPlugin);
        this.messagesFile = new MessagesFile().load(new File(this.proxyBansPlugin.getDataFolder(), "messages.json"));
    }

    public void disable() {

    }
}
