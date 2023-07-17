package ua.klesaak.proxybans.config;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.SneakyThrows;
import lombok.val;
import net.md_5.bungee.api.plugin.Plugin;
import ua.klesaak.proxybans.rules.PunishType;
import ua.klesaak.proxybans.rules.RuleData;
import ua.klesaak.proxybans.utils.JacksonAPI;
import ua.klesaak.proxybans.utils.NumberUtils;
import ua.klesaak.proxybans.utils.yml.PluginConfig;

import java.io.File;
import java.nio.file.Files;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class ConfigFile extends PluginConfig {
    private LinkedHashSet<RuleData> rules;
    private final DateFormat dateFormat;

    public ConfigFile(Plugin plugin) {
        super(plugin, "config.yml");
        this.loadRules(plugin);
        SimpleDateFormat dateFormat = new SimpleDateFormat(this.getString("dateFormat.format"), new Locale(this.getString("dateFormat.locale")));
        dateFormat.setTimeZone(TimeZone.getTimeZone(this.getString("dateFormat.timeZone")));
        this.dateFormat = dateFormat;
    }

    @SneakyThrows
    private void loadRules(Plugin plugin) {
        val file = new File(plugin.getDataFolder(), "rules.json");
        if (!file.getParentFile().exists()) Files.createDirectory(file.getParentFile().toPath());
        if (!file.exists()) Files.createFile(file.toPath());
        if (file.length() <= 0L) {
            LinkedHashSet<RuleData> ruleData = new LinkedHashSet<>();
            ruleData.add(new RuleData("1.0", "Бан за матюки", EnumSet.of(PunishType.BAN, PunishType.MUTE)));
            ruleData.add(new RuleData("1.1", "Бан за оскорбление негров в чате", EnumSet.of(PunishType.BAN, PunishType.OP_BAN, PunishType.IP_BAN)));
            JacksonAPI.writeFile(file, ruleData);
        }
        LinkedHashSet<RuleData> ruleData = JacksonAPI.readFile(file, new TypeReference<LinkedHashSet<RuleData>>() {});
        this.rules = new LinkedHashSet<>(ruleData);
    }

    public RuleData getRule(String rule) {
        for (val ruleData : this.rules) {
            if (ruleData.getRule().equalsIgnoreCase(rule)) return ruleData;
        }
        return null;
    }

    private String getTime(String group, String category, String what) {
        return this.getConfig().getString("groups." + group + "." + category + "." + what, this.getConfig().getString("groups.defaults." + category + "." + what, "0s"));
    }

    public long getCooldownTime(String group, String command) {
        return NumberUtils.parseTimeFromString(this.getTime(group, "cooldown", command), TimeUnit.SECONDS);
    }

    public long getMaxPunishTime(String group, String command) {
        return NumberUtils.parseTimeFromString(this.getTime(group, "maxPunishTime", command), TimeUnit.SECONDS);
    }

    public boolean isProtected(String playerName) {
        return this.getStringList("protectedPlayers").contains(playerName);
    }

    public boolean isHeavier(String fromGroup, String toGroup) {
        return this.getWeight(fromGroup) > this.getWeight(toGroup);
    }

    private int getWeight(String fromGroup) {
        return this.getConfig().getInt("groups." + fromGroup + ".weight", this.getInt("groups.defaults.weight"));
    }

    public String parseDate(long time) {
        return this.dateFormat.format(new Date(time));
    }
}
