package nav.portal.core.entities;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class OpeningHoursGroup {
    private UUID id;
    private String name;
    private List<OpeningRuleEntity> rules;

    public OpeningHoursGroup() {
    }

    public OpeningHoursGroup(UUID id, String name, List<OpeningRuleEntity> rule) {
        this.id = id;
        this.name = name;
        this.rules = rule;
    }

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

    public List<OpeningRuleEntity> getRules() {
        return rules;
    }

    public OpeningHoursGroup setRules(List<OpeningRuleEntity> rules) {
        this.rules = rules;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OpeningHoursGroup)) return false;
        OpeningHoursGroup that = (OpeningHoursGroup) o;
        return getId().equals(that.getId()) && getName().equals(that.getName()) && Objects.equals(getRules(), that.getRules());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getRules());
    }
}
