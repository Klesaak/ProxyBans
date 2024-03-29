package ua.klesaak.proxybans.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.base.Joiner;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.val;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import ua.klesaak.proxybans.manager.ProxyBansManager;
import ua.klesaak.proxybans.rules.PunishType;
import ua.klesaak.proxybans.rules.RuleData;
import ua.klesaak.proxybans.utils.NumberUtils;
import ua.klesaak.proxybans.utils.Paginated;
import ua.klesaak.proxybans.utils.jackson.JacksonAPI;
import ua.klesaak.proxybans.utils.yml.PluginConfig;

import java.io.File;
import java.nio.file.Files;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static ua.klesaak.proxybans.config.MessagesFile.*;

@Getter
public class ConfigFile extends PluginConfig {
    public static final TypeReference<LinkedList<RuleData>> RULES_DATA_REFERENCE = new TypeReference<LinkedList<RuleData>>() {};

    private LinkedList<RuleData> rules;
    private final DateFormat dateFormat;
    private Paginated<RuleData> rulePages;
    private final ProxyBansManager proxyBansManager;
    private final List<String> blockedCommandsOnMute;

    public ConfigFile(ProxyBansManager manager) {
        super(manager.getProxyBansPlugin(), "config.yml");
        this.proxyBansManager = manager;
        this.loadRules(manager);
        SimpleDateFormat dateFormat = new SimpleDateFormat(this.getString("dateFormat.format"), new Locale(this.getString("dateFormat.locale")));
        dateFormat.setTimeZone(TimeZone.getTimeZone(this.getString("dateFormat.timeZone")));
        this.dateFormat = dateFormat;
        this.blockedCommandsOnMute = this.getStringList("blockedCommandsOnMute");
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
            ruleData.add(new RuleData("1.2", "Мут просто так", EnumSet.of(PunishType.MUTE, PunishType.TEMP_MUTE, PunishType.OP_MUTE, PunishType.OP_TEMP_MUTE)));
            JacksonAPI.writePath(file.toPath(), JacksonAPI.readAsEmptyMapperString(ruleData));
        }
        LinkedList<RuleData> ruleData = JacksonAPI.readFile(file, RULES_DATA_REFERENCE);
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
        if (commandSender instanceof ProxiedPlayer) {
            return this.getCooldownTime(this.proxyBansManager.getPermHook().getUserGroup(commandSender.getName()), command);
        }
        return 0L;
    }

    private long getMaxPunishTime(String group, String command) {
        return NumberUtils.parseTimeFromString(this.getTime(group, "maxPunishTime", command), TimeUnit.SECONDS);
    }

    public long getMaxPunishTime(CommandSender commandSender, String command) {
        return this.getMaxPunishTime(this.proxyBansManager.getPermHook().getUserGroup(commandSender.getName()), command);
    }

    public boolean isProtected(String playerName) {
        return this.getStringList("protectedPlayers").contains(playerName);
    }

    public boolean isHeavier(String fromNickname, String toNickname) {
        val permHook = this.proxyBansManager.getPermHook();
        return this.getWeight(permHook.getUserGroup(fromNickname)) > this.getWeight(permHook.getUserGroup(toNickname));
    }

    private int getWeight(String fromGroup) {
        return this.getConfig().getInt("groups." + fromGroup + ".weight", 0);
    }

    public String parseDate(Instant instant) {
        return this.dateFormat.format(Date.from(instant));
    }

    public boolean checkBlackListCommandOnMute(ChatEvent event) {
        String cmd = event.getMessage().split(" ")[0].toLowerCase();
        if (cmd.contains(":") && !cmd.endsWith(":")) {
            StringBuilder slashes = new StringBuilder();
            for (int i = 0; i < cmd.length() && cmd.charAt(i) == '/'; ++i) {
                slashes.append('/');
            }
            slashes.append(cmd.split(":")[1]);
            cmd = slashes.toString();
        }
        return this.blockedCommandsOnMute.contains(cmd);
    }
}
