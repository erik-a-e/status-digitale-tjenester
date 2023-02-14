package nav.portal.core.entities;

import nav.portal.core.enums.RuleType;

import java.util.UUID;

public interface OpeningRule {

     RuleType getRuleType();

     UUID getId();

}
