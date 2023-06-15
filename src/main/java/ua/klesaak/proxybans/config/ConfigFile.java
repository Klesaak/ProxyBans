package ua.klesaak.proxybans.config;

import net.md_5.bungee.api.plugin.Plugin;
import ua.klesaak.proxybans.utils.yml.PluginConfig;

public class ConfigFile extends PluginConfig {


    public ConfigFile(Plugin plugin) {
        super(plugin, "config.yml");
    }
}
