package ua.klesaak.proxybans.rules;

import java.util.EnumSet;

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

    public boolean isAllow(PunishType punishType) {
        return this.applicablePunishments.contains(punishType);
    }

    public String getRule() {
        return this.rule;
    }

    public String getText() {
        return this.ruleText;
    }

    public EnumSet<PunishType> getApplicablePunishments() {
        return applicablePunishments;
    }

    public String getRuleData() {
        return this.rule + " " + this.ruleText;
    }
}
