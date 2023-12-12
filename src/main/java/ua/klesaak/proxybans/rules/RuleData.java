package ua.klesaak.proxybans.rules;

import lombok.Getter;

import java.util.EnumSet;

@Getter
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

    public String getText() {
        return this.ruleText;
    }

    public String getRuleData() {
        return this.rule + " " + this.ruleText;
    }
}
