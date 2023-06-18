package ua.klesaak.proxybans.rules;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

public class RuleData {
    private String rule;
    private String ruleText;
    private EnumSet<PunishType> applicablePunishments;

    public RuleData(String rule, String ruleText, EnumSet<PunishType> applicablePunishments) {
        this.rule = rule;
        this.ruleText = ruleText;
        this.applicablePunishments = applicablePunishments;
    }

    public RuleData() {
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
