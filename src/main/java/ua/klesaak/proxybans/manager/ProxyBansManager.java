package ua.klesaak.proxybans.manager;

import lombok.Getter;
import ua.klesaak.proxybans.ProxyBansPlugin;
import ua.klesaak.proxybans.commands.RulesCommand;
import ua.klesaak.proxybans.commands.ban.BanCommand;
import ua.klesaak.proxybans.config.ConfigFile;
import ua.klesaak.proxybans.config.MessagesFile;
import ua.klesaak.proxybans.permshook.IPermHook;
import ua.klesaak.proxybans.permshook.MinePermsHook;
import ua.klesaak.proxybans.permshook.SimplePermsHook;
import ua.klesaak.proxybans.utils.command.CooldownExpireNotifier;

import java.io.File;

@Getter
public class ProxyBansManager {
    private final ProxyBansPlugin proxyBansPlugin;
    private ConfigFile configFile;
    private MessagesFile messagesFile;
    private final CooldownExpireNotifier cooldownExpireNotifier;
    private IPermHook permHook;

    public ProxyBansManager(ProxyBansPlugin proxyBansPlugin) {
        this.proxyBansPlugin = proxyBansPlugin;
        this.reloadConfigFiles();
        new PunishListener(this);
        this.cooldownExpireNotifier = new CooldownExpireNotifier(this);
        this.hookPerms();
        /////commands/////
        new BanCommand(this);
        new RulesCommand(this);
    }

    private void hookPerms() {
        if (this.proxyBansPlugin.getProxy().getPluginManager().getPlugin("SimplePerms") != null) {
            this.permHook = new SimplePermsHook();
            return;
        }
        this.permHook = new MinePermsHook();
    }

    public void reloadConfigFiles() {
        this.messagesFile = new MessagesFile().load(new File(this.proxyBansPlugin.getDataFolder(), "messages.json"));
        this.configFile = new ConfigFile(this);
    }

    public void disable() {
        this.cooldownExpireNotifier.stop();
    }
}
