package ua.klesaak.proxybans.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import lombok.SneakyThrows;
import lombok.val;
import ua.klesaak.proxybans.manager.ProxyBansManager;
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
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import static ua.klesaak.proxybans.config.MessagesFile.*;

public class ConfigFile extends PluginConfig {
    private LinkedList<RuleData> rules;
    private final DateFormat dateFormat;
    private final Map<Integer, String> rulesPages = new ConcurrentHashMap<>(128);

    public ConfigFile(ProxyBansManager manager) {
        super(manager.getProxyBansPlugin(), "config.yml");
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
        this.loadRulesPages(manager.getMessagesFile());
    }

    private void loadRulesPages(MessagesFile messagesFile) {
        int pageSize = this.rules.size() / 5;
        for (int i = 0; i <= pageSize; i++) {
            val pageData = this.getPage(this.rules, i, 5);
            List<String> ruleList = new ArrayList<>();
            for (val dat : pageData) {
                val applicablePunishments = dat.getApplicablePunishments();
                List<String> allowedCommands = new ArrayList<>();
                for (val obj : applicablePunishments) {
                    allowedCommands.add(obj.getCommand());
                }
                String allowedCommandsString = Joiner.on(", ").join(allowedCommands);
                ruleList.add(messagesFile.getMessageRuleFormat()
                        .tag(RULE_PATTERN, dat.getRule())
                        .tag(RULE_TEXT_PATTERN, dat.getText())
                        .tag(APPLICABLE_PUNISHMENTS_PATTERN, allowedCommandsString).getMessageString().replace("\"", ""));
            }
            String ruleListString = Joiner.on("\n").join(ruleList);
            this.rulesPages.put(i, ruleListString);
        }
    }

    public String getRulesPage(int pageIndex) {
        return this.rulesPages.get(pageIndex - 1);
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

    public long getCooldownTime(String group, String command) {
        return NumberUtils.parseTimeFromString(this.getTime(group, "cooldown", command), TimeUnit.MILLISECONDS);
    }

    public long getMaxPunishTime(String group, String command) {
        return NumberUtils.parseTimeFromString(this.getTime(group, "maxPunishTime", command), TimeUnit.MILLISECONDS);
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

    public int getRulesPagesSize() {
        return this.rulesPages.size();
    }

    private <T> List<T> getPage(List<T> sourceList, int page, int pageSize) {
        Preconditions.checkArgument(pageSize > 0, ("invalid page size: " + pageSize));
        Preconditions.checkArgument(page >= 0, ("invalid page: " + page));
        int fromIndex = page * pageSize;
        return (sourceList != null && sourceList.size() >= fromIndex) ? sourceList.subList(fromIndex, Math.min(fromIndex + pageSize, sourceList.size())) : Collections.emptyList();
    }
}
