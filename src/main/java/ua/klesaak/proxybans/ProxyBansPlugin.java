package ua.klesaak.proxybans;

import net.md_5.bungee.api.plugin.Plugin;
import ua.klesaak.proxybans.manager.ProxyBansManager;

public final class ProxyBansPlugin extends Plugin {
    private ProxyBansManager proxyBansManager;

    @Override
    public void onEnable() {
        this.proxyBansManager = new ProxyBansManager(this);
    }

    @Override
    public void onDisable() {
        this.proxyBansManager.disable();
    }
}
