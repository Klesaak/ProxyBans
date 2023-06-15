package ua.klesaak.proxybans.rules;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

public class RuleData {
    private final String rule;
    private final String ruleText;
    private final EnumSet<PunishType> allowedPunishments;

    public RuleData(String rule, String ruleText, EnumSet<PunishType> allowedPunishments) {
        this.rule = rule;
        this.ruleText = ruleText;
        this.allowedPunishments = allowedPunishments;
    }

    public Set<PunishType> getAllowedPunishments() {
        return Collections.unmodifiableSet(this.allowedPunishments);
    }

    public String getRule() {
        return this.rule;
    }

    public String getText() {
        return this.ruleText;
    }
}
