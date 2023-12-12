package ua.klesaak.proxybans.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.base.Joiner;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.val;
import net.md_5.bungee.api.CommandSender;
import ua.klesaak.proxybans.manager.ProxyBansManager;
import ua.klesaak.proxybans.rules.PunishType;
import ua.klesaak.proxybans.rules.RuleData;
import ua.klesaak.proxybans.utils.JacksonAPI;
import ua.klesaak.proxybans.utils.NumberUtils;
import ua.klesaak.proxybans.utils.Paginated;
import ua.klesaak.proxybans.utils.yml.PluginConfig;

import java.io.File;
import java.nio.file.Files;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static ua.klesaak.proxybans.config.MessagesFile.*;

@Getter
public class ConfigFile extends PluginConfig {
    private LinkedList<RuleData> rules;
    private final DateFormat dateFormat;
    private Paginated<RuleData> rulePages;
    private final ProxyBansManager proxyBansManager;

    public ConfigFile(ProxyBansManager manager) {
        super(manager.getProxyBansPlugin(), "config.yml");
        this.proxyBansManager = manager;
        this.loadRules(manager);
        SimpleDateFormat dateFormat = new SimpleDateFormat(this.getString("dateFormat.format"), new Locale(this.getString("dateFormat.locale")));
        dateFormat.setTimeZone(TimeZone.getTimeZone(this.getString("dateFormat.timeZone")));
        this.dateFormat = dateFormat;
    }

    @SneakyThrows
    private void loadRules(ProxyBansManager manager) {
        val file = new File(manager.getProxyBansPlugin().getDataFolder(), "rules.json");
        if (!file.getParentFile().exists()) Files.createDirectory(file.getParentFile().toPath());
        if (!file.exists()) Files.createFile(file.toPath());
        if (file.length() <= 0L) {
            LinkedList<RuleData> ruleData = new LinkedList<>();
            ruleData.add(new RuleData("1.0", "Бан за матюки", EnumSet.of(PunishType.BAN, PunishType.MUTE)));
            ruleData.add(new RuleData("1.1", "Бан за оскорбление негров в чате", EnumSet.of(PunishType.BAN, PunishType.OP_BAN, PunishType.IP_BAN)));
            JacksonAPI.writeFile(file, ruleData);
        }
        LinkedList<RuleData> ruleData = JacksonAPI.readFile(file, new TypeReference<LinkedList<RuleData>>() {});
        this.rules = new LinkedList<>(ruleData);
        this.rulePages = new Paginated<>(this.rules);
    }

    public String getRulesPage(MessagesFile messagesFile, int pageIndex) {
        val pageData = this.rulePages.getPage(pageIndex, 5);
        List<String> ruleList = new ArrayList<>();
        for (val dat : pageData) {
            val applicablePunishments = dat.value().getApplicablePunishments();
            List<String> allowedCommands = new ArrayList<>();
            for (val obj : applicablePunishments) {
                allowedCommands.add("/" + obj.getCommand());
            }
            String allowedCommandsString = Joiner.on(", ").join(allowedCommands);
            ruleList.add(messagesFile.getMessageRuleFormat()
                    .tag(RULE_PATTERN, dat.value().getRule())
                    .tag(RULE_TEXT_PATTERN, dat.value().getText())
                    .tag(APPLICABLE_PUNISHMENTS_PATTERN, allowedCommandsString).getMessageString());
        }
        return Joiner.on("\n").join(ruleList);
    }

    public int getMaxRulesPages() {
        return this.rulePages.getMaxPages(5);
    }

    public RuleData getRule(String rule) {
        for (val ruleData : this.rules) {
            if (ruleData.getRule().equalsIgnoreCase(rule)) return ruleData;
        }
        return null;
    }

    /**
     * @param group - группа игрока.
     * @param category - категория(maxPunishTime or cooldown)
     * @param command - команда, которая применяется
     */
    private String getTime(String group, String category, String command) {
        return this.getConfig().getString("groups." + group + "." + category + "." + command, "0s");
    }

    private long getCooldownTime(String group, String command) {
        return NumberUtils.parseTimeFromString(this.getTime(group, "cooldown", command), TimeUnit.SECONDS);
    }

    public long getCooldownTime(CommandSender commandSender, String command) {
        return this.getCooldownTime(this.proxyBansManager.getPermHook().getUserGroup(commandSender.getName()), command);
    }

    private long getMaxPunishTime(String group, String command) {
        return NumberUtils.parseTimeFromString(this.getTime(group, "maxPunishTime", command), TimeUnit.MILLISECONDS);
    }

    public long getMaxPunishTime(CommandSender commandSender, String command) {
        return this.getMaxPunishTime(this.proxyBansManager.getPermHook().getUserGroup(commandSender.getName()), command);
    }

    public boolean isProtected(String playerName) {
        return this.getStringList("protectedPlayers").contains(playerName);
    }

    public boolean isHeavier(String fromGroup, String toGroup) {
        return this.getWeight(fromGroup) > this.getWeight(toGroup);
    }

    private int getWeight(String fromGroup) {
        return this.getConfig().getInt("groups." + fromGroup + ".weight", 0);
    }

    public String parseDate(long time) {
        return this.dateFormat.format(new Date(time));
    }
}
