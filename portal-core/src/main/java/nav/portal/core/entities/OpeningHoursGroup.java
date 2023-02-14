package nav.portal.core.entities;

import nav.portal.core.enums.RuleType;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class OpeningHoursGroup implements OpeningRule {
    private UUID id;
    private String name;
    private List<OpeningRule> rules;

    public OpeningHoursGroup() {
    }

    public OpeningHoursGroup(UUID id, String name, List<OpeningRule> rules) {
        this.id = id;
        this.name = name;
        this.rules = rules;
    }

    @Override
    public RuleType getRuleType() {
        return RuleType.REGEL_GRUPPE;
    }
    @Override
    public UUID getId() {
        return id;
    }

    public OpeningHoursGroup setId(UUID id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public OpeningHoursGroup setName(String name) {
        this.name = name;
        return this;
    }

    public List<OpeningRule> getRules() {
        return rules;
    }

    public OpeningHoursGroup setRules(List<OpeningRule> rules) {
        this.rules = rules;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OpeningHoursGroup that = (OpeningHoursGroup) o;
        return id.equals(that.id) && name.equals(that.name) && rules.equals(that.rules);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, rules);
    }
}
