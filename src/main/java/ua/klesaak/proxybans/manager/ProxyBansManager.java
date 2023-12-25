package ua.klesaak.proxybans.manager;

import lombok.Getter;
import lombok.val;
import net.md_5.bungee.api.ChatColor;
import ua.klesaak.proxybans.ProxyBansPlugin;
import ua.klesaak.proxybans.commands.ReloadCommand;
import ua.klesaak.proxybans.commands.RulesCommand;
import ua.klesaak.proxybans.commands.info.BanInfoCommand;
import ua.klesaak.proxybans.commands.info.MuteInfoCommand;
import ua.klesaak.proxybans.commands.punish.KickCommand;
import ua.klesaak.proxybans.commands.punish.ban.*;
import ua.klesaak.proxybans.commands.punish.mute.MuteCommand;
import ua.klesaak.proxybans.commands.punish.mute.OpMuteCommand;
import ua.klesaak.proxybans.commands.punish.mute.OpTempMuteCommand;
import ua.klesaak.proxybans.commands.punish.mute.TempMuteCommand;
import ua.klesaak.proxybans.commands.punish.unban.OpUnBanCommand;
import ua.klesaak.proxybans.commands.punish.unban.UnBanAllCommand;
import ua.klesaak.proxybans.commands.punish.unban.UnBanCommand;
import ua.klesaak.proxybans.commands.punish.unmute.OpUnMuteCommand;
import ua.klesaak.proxybans.commands.punish.unmute.UnMuteAllCommand;
import ua.klesaak.proxybans.commands.punish.unmute.UnMuteCommand;
import ua.klesaak.proxybans.commands.punishlist.BanListCommand;
import ua.klesaak.proxybans.commands.punishlist.MuteListCommand;
import ua.klesaak.proxybans.config.ConfigFile;
import ua.klesaak.proxybans.config.MessagesFile;
import ua.klesaak.proxybans.permshook.IPermHook;
import ua.klesaak.proxybans.permshook.LuckPermsHook;
import ua.klesaak.proxybans.permshook.MinePermsHook;
import ua.klesaak.proxybans.permshook.SimplePermsHook;
import ua.klesaak.proxybans.storage.PunishStorage;
import ua.klesaak.proxybans.storage.file.FileStorage;
import ua.klesaak.proxybans.utils.command.CooldownExpireNotifier;

import java.io.File;

@Getter
public class ProxyBansManager {
    private final ProxyBansPlugin proxyBansPlugin;
    private ConfigFile configFile;
    private MessagesFile messagesFile;
    private final CooldownExpireNotifier cooldownExpireNotifier;
    private IPermHook permHook;
    private PunishStorage punishStorage;

    public ProxyBansManager(ProxyBansPlugin proxyBansPlugin) {
        this.proxyBansPlugin = proxyBansPlugin;
        this.reloadConfigFiles();
        this.hookPerms();
        this.initStorage();
        new PunishListener(this);
        this.cooldownExpireNotifier = new CooldownExpireNotifier(this);
        /////commands/////
        new BanCommand(this);
        new TempBanCommand(this);
        new BanIPCommand(this);
        new OpBanCommand(this);
        new OpTempBanCommand(this);
        new OpBanIPCommand(this);
        new UnBanCommand(this);
        new OpUnBanCommand(this);
        new UnBanAllCommand(this);

        new MuteCommand(this);
        new TempMuteCommand(this);
        new UnMuteCommand(this);
        new UnMuteAllCommand(this);
        new OpMuteCommand(this);
        new OpTempMuteCommand(this);
        new OpUnMuteCommand(this);

        new RulesCommand(this);
        new KickCommand(this);

        new BanListCommand(this);
        new MuteListCommand(this);

        new BanInfoCommand(this);
        new MuteInfoCommand(this);
        new ReloadCommand(this);
    }

    private void hookPerms() {
        val pluginManager = this.proxyBansPlugin.getProxy().getPluginManager();
        if (pluginManager.getPlugin("SimplePerms") != null) {
            this.permHook = new SimplePermsHook(this.proxyBansPlugin);
            return;
        }
        if (pluginManager.getPlugin("LuckPerms") != null) {
            this.permHook = new LuckPermsHook(this.proxyBansPlugin);
            return;
        }
        if (pluginManager.getPlugin("MinePermsBungee") != null) {
            this.permHook = new MinePermsHook(this.proxyBansPlugin);
            return;
        }
        throw new RuntimeException("You must be install MinePerms or SimplePerms(By Den_Abr) or LuckPermsBungee");
    }

    private void initStorage() {
        val storageType = this.configFile.getString("storage").toLowerCase();
        switch (storageType) {
            case "redis": {
               // this.punishStorage = new RedisStorage(this);
                break;
            }
            case "file": {
                this.punishStorage = new FileStorage(this);
                break;
            }
            default: {
                this.punishStorage = new FileStorage(this);
            }
        }
        this.proxyBansPlugin.getLogger().info(ChatColor.GREEN + "Loaded " + storageType + " storage type!");
    }

    public void reloadConfigFiles() {
        this.messagesFile = new MessagesFile().load(new File(this.proxyBansPlugin.getDataFolder(), "messages.json"));
        this.configFile = new ConfigFile(this);
    }

    public void disable() {
        this.cooldownExpireNotifier.stop();
    }
}
