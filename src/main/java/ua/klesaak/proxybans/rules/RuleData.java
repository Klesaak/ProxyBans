package ua.klesaak.proxybans.rules;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

public class RuleData {
    private final String rule;
    private final String ruleText;
    private final EnumSet<PunishType> applicablePunishments;

    public RuleData(String rule, String ruleText, EnumSet<PunishType> applicablePunishments) {
        this.rule = rule;
        this.ruleText = ruleText;
        this.applicablePunishments = applicablePunishments;
    }

    public Set<PunishType> getApplicablePunishments() {
        return Collections.unmodifiableSet(this.applicablePunishments);
    }

    public String getRule() {
        return this.rule;
    }

    public String getText() {
        return this.ruleText;
    }
}
