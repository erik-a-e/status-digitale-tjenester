package nav.portal.core.entities;

import java.util.Objects;
import java.util.UUID;

public class OpeningRuleEntity {

    private UUID id;
    private String name;
    private String rule;

    public OpeningRuleEntity() {
    }

    public OpeningRuleEntity(UUID id, String name, String rule) {
        this.id = id;
        this.name = name;
        this.rule = rule;
    }

    public UUID getId() {
        return id;
    }

    public OpeningRuleEntity setId(UUID id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public OpeningRuleEntity setName(String name) {
        this.name = name;
        return this;
    }

    public String getRule() {
        return rule;
    }

    public OpeningRuleEntity setRule(String rule) {
        this.rule = rule;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OpeningRuleEntity)) return false;
        OpeningRuleEntity that = (OpeningRuleEntity) o;
        return getId().equals(that.getId()) && getName().equals(that.getName()) && getRule().equals(that.getRule());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getRule());
    }

}



