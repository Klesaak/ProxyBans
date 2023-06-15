package ua.klesaak.proxybans.utils.yml;

import lombok.Getter;
import lombok.SneakyThrows;
import lombok.val;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.nio.file.Files;
import java.util.List;
import java.util.logging.Level;

@Getter
public abstract class PluginConfig {
    private Configuration config;
    private final transient File file;

    public PluginConfig(Plugin plugin, String fileName) {
        this.file = new File(plugin.getDataFolder(), fileName);
        this.load(plugin);
    }

    @SneakyThrows
    public void load(Plugin plugin) {
        val pluginDataFolderPath = plugin.getDataFolder().toPath();
        if (!Files.exists(pluginDataFolderPath)) {
            Files.createDirectory(pluginDataFolderPath);
            plugin.getLogger().log(Level.WARNING, "successfully create file: " + pluginDataFolderPath);
        }
        if (!this.file.exists()) Files.copy(plugin.getResourceAsStream(this.file.getName()), file.toPath());
        this.config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(this.file);
    }

    public void reload(Plugin plugin) {
        this.load(plugin);
    }

    @SneakyThrows
    public void save() {
        ConfigurationProvider.getProvider(YamlConfiguration.class).save(this.config, this.file);
    }

    public boolean isString(String path) {
        Object val = this.config.get(path);
        return val instanceof String;
    }

    public boolean isList(String path) {
        Object val = this.config.get(path);
        return val instanceof List;
    }

    public int getInt(String path) {
        return this.config.getInt(path);
    }

    public boolean getBoolean(String path) {
        return this.config.getBoolean(path);
    }

    public String getString(String path) {
        return this.config.getString(path);
    }

    public List<String> getStringList(String path) {
        return this.config.getStringList(path);
    }

    public String color(String string) {
        return ChatColor.translateAlternateColorCodes('&', String.valueOf(string));
    }
}
