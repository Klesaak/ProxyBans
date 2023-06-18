package ua.klesaak.proxybans.config;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.val;
import net.md_5.bungee.api.plugin.Plugin;
import ua.klesaak.proxybans.rules.PunishType;
import ua.klesaak.proxybans.rules.RuleData;
import ua.klesaak.proxybans.utils.JacksonAPI;
import ua.klesaak.proxybans.utils.yml.PluginConfig;

import java.io.File;
import java.nio.file.Files;
import java.util.EnumSet;
import java.util.LinkedHashSet;

@Getter
public class ConfigFile extends PluginConfig {
    private LinkedHashSet<RuleData> rules = new LinkedHashSet<>();

    public ConfigFile(Plugin plugin) {
        super(plugin, "config.yml");
        this.loadRules(plugin);
    }

    @SneakyThrows
    private void loadRules(Plugin plugin) {
        val file = new File(plugin.getDataFolder(), "rules.json");
        if (!file.getParentFile().exists()) Files.createDirectory(file.getParentFile().toPath());
        if (!file.exists()) {
            Files.createFile(file.toPath());
        }
        if (file.length() <= 0L) {
            LinkedHashSet<RuleData> ruleData = new LinkedHashSet<>();
            ruleData.add(new RuleData("1.0", "Бан за матюки", EnumSet.of(PunishType.BAN, PunishType.MUTE)));
            ruleData.add(new RuleData("1.1", "Бан за оскорбление негров в чате", EnumSet.of(PunishType.BAN, PunishType.OP_BAN, PunishType.IP_BAN)));
            JacksonAPI.writeFile(file, ruleData);
        }
        LinkedHashSet<RuleData> ruleData = JacksonAPI.readFile(file, new TypeReference<LinkedHashSet<RuleData>>() {});
        this.rules = new LinkedHashSet<>(ruleData);
    }
}
