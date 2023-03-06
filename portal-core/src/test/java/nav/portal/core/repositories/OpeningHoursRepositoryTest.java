package nav.portal.core.repositories;

import nav.portal.core.entities.AreaEntity;
import nav.portal.core.entities.OpeningHoursGroup;
import nav.portal.core.entities.OpeningHoursRule;
import nav.portal.core.entities.OpeningHoursRuleEntity;
import org.fluentjdbc.DbContext;
import org.fluentjdbc.DbContextConnection;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.assertj.core.api.Assertions;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

class OpeningHoursRepositoryTest {

    private final DataSource dataSource = TestDataSource.create();

    private final DbContext dbContext = new DbContext();
    private DbContextConnection connection;

    @BeforeEach
    void startConnection() {
        connection = dbContext.startConnection(dataSource);
        TestUtil.clearAllTableData(dbContext);
    }

    @AfterEach
    void endConnection() {
        connection.close();
    }

    private final OpeningHoursRepository openingHoursRepository = new OpeningHoursRepository(dbContext);


    @Test
    void save() {
        //Arrange
        OpeningHoursRuleEntity rule = SampleData.getRandomizedOpeningRule();
        //Act
        UUID id = openingHoursRepository.save(rule);
        //Assert
        Assertions.assertThat(id).isNotNull();

    }

    @Test
    void update() {
        //Arrange
        List<OpeningHoursRuleEntity> rules = SampleData.getNonEmptyListOfOpeningRules(2);
        rules.forEach(rule -> {
            rule.setId(openingHoursRepository.save(rule));
        });
        UUID ruleId = rules.get(0).getId();
        Optional<OpeningHoursRuleEntity> before = openingHoursRepository.retriveRule(ruleId);
        OpeningHoursRuleEntity retrievedBefore = before.get();
        //Act
        rules.get(0).setName(rules.get(1).getName());
        rules.get(0).setRule(rules.get(1).getRule());
        rules.get(0).setId(rules.get(1).getId());
        openingHoursRepository.update(rules.get(0));
        Optional<OpeningHoursRuleEntity> after = openingHoursRepository.retriveRule(rules.get(0).getId());
        OpeningHoursRuleEntity retrievedAfter = after.get();
        //Assert
        Assertions.assertThat(retrievedBefore.getName()).isNotEqualToIgnoringCase(rules.get(0).getName());
        Assertions.assertThat(retrievedAfter.getName()).isEqualTo(rules.get(1).getName());
    }

    @Test
    void deleteOpeninghours() {
        //Assign
        OpeningHoursRuleEntity ruleToBeDeleted = SampleData.getRandomizedOpeningRule();
        ruleToBeDeleted.setId(openingHoursRepository.save(ruleToBeDeleted));
        Optional<OpeningHoursRuleEntity> beforeDelete = openingHoursRepository.retriveRule(ruleToBeDeleted.getId());
        //Act
        boolean isDeleted = openingHoursRepository.deleteOpeninghours(ruleToBeDeleted.getId());
        Optional<OpeningHoursRuleEntity> afterDelete = openingHoursRepository.retriveRule(ruleToBeDeleted.getId());
        //Assert
        Assertions.assertThat(beforeDelete).contains(ruleToBeDeleted);
        Assertions.assertThat(isDeleted).isTrue();
        Assertions.assertThat(afterDelete).isEmpty();
    }

    @Test
    void saveGroup() {
        //Arrange
        OpeningHoursRuleEntity rule = SampleData.getRandomizedOpeningRule();
        UUID rule_id = openingHoursRepository.save(rule);
        rule.setId(rule_id);
        OpeningHoursGroup group = new OpeningHoursGroup().setName("Ny gruppe").setRules(List.of(rule));
        //Act
        UUID group_id = openingHoursRepository.saveGroup(group);
        group.setId(group_id);
        Optional<OpeningHoursGroup> retrievedGroup = openingHoursRepository.retrieveOneGroup(group_id);
        //Assert
        Assertions.assertThat(retrievedGroup).isPresent();
        Assertions.assertThat(retrievedGroup.get().getRules()).contains(rule);
        Assertions.assertThat(retrievedGroup.get()).isEqualTo(group);
    }

    /*@Test
    void updateGroup() {
        //Arrange
        String updatedOpeningHoursName = "Any other group";
        OpeningHoursRuleEntity rule = SampleData.getRandomizedOpeningRule();
        UUID rule_id = openingHoursRepository.save(rule);
        rule.setId(rule_id);
        OpeningHoursGroup group = new OpeningHoursGroup().setName("Ny gruppe").setRules(List.of(rule));
        UUID group_id = openingHoursRepository.saveGroup(group);
        group.setId(group_id);
        Optional<OpeningHoursGroup> retrievedGroupBefore = openingHoursRepository.retrieveOneGroup(group_id);
        group.setName(updatedOpeningHoursName);
        //Act
        openingHoursRepository.update(group);
        //Assert
    }*/

    @Test
    void retrieveRule() {
        //Arrange
        List<OpeningHoursRuleEntity> rules = SampleData.getNonEmptyListOfOpeningRules(2);
        rules.forEach(rule -> {
            rule.setId(openingHoursRepository.save(rule));
        });
        UUID ruleId = rules.get(0).getId();
        OpeningHoursRuleEntity ruleForRetrieving = rules.get(0);
        //Act
        Optional<OpeningHoursRuleEntity> retrievedRule = openingHoursRepository.retriveRule(ruleForRetrieving.getId());
        //Assert
        Assertions.assertThat(retrievedRule).contains(ruleForRetrieving);
    }

    @Test
    void deleteOpeninghourGroup() {
        //Arrange
        OpeningHoursRuleEntity rule = SampleData.getRandomizedOpeningRule();
        UUID rule_id = openingHoursRepository.save(rule);
        rule.setId(rule_id);
        OpeningHoursGroup group = new OpeningHoursGroup().setName("Ny gruppe").setRules(List.of(rule));
        UUID group_id = openingHoursRepository.saveGroup(group);
        group.setId(group_id);
        Optional<OpeningHoursGroup> retrievedGroupBefore = openingHoursRepository.retrieveOneGroup(group_id);
        //Act
        boolean isDeleted = openingHoursRepository.deleteOpeninghourGroup(group_id);
        Optional<OpeningHoursGroup> retrievedGroupAfter = openingHoursRepository.retrieveOneGroup(group_id);
        //Assert
        Assertions.assertThat(isDeleted).isTrue();
        Assertions.assertThat(retrievedGroupAfter).isEmpty( );
    }

    @Test
    void retrieveOneGroupSimple() {
        //Arrange
        OpeningHoursRuleEntity rule = SampleData.getRandomizedOpeningRule();
        UUID rule_id = openingHoursRepository.save(rule);
        rule.setId(rule_id);
        OpeningHoursGroup group = new OpeningHoursGroup().setName("Ny gruppe").setRules(List.of(rule));
        UUID group_id = openingHoursRepository.saveGroup(group);
        //Act
        Optional<OpeningHoursGroup> retrievedGroup = openingHoursRepository.retrieveOneGroup(group_id);
        //Assert
        Assertions.assertThat(retrievedGroup).isPresent();
        Assertions.assertThat(retrievedGroup.get().getRules()).containsExactly(rule);
    }

    @Test
    void retrieveOneGroupComplex() {
        //Arrange
        OpeningHoursRuleEntity rule = SampleData.getRandomizedOpeningRule();
        OpeningHoursRuleEntity rule2 = SampleData.getRandomizedOpeningRule();
        UUID rule_id = openingHoursRepository.save(rule);
        UUID rule2_id = openingHoursRepository.save(rule2);
        rule.setId(rule_id);
        rule2.setId(rule2_id);
        OpeningHoursGroup group = new OpeningHoursGroup().setName("Gruppe1").setRules(List.of(rule));
        group.setId(openingHoursRepository.saveGroup(group));

        List<OpeningHoursRule> rulesOfGroup2 = List.of(rule2, group);
        OpeningHoursGroup group2 = new OpeningHoursGroup().setName("Gruppe2").setRules(rulesOfGroup2);
        UUID group2_id = openingHoursRepository.saveGroup(group2);
        //Act
        Optional<OpeningHoursGroup> retrievedGroup = openingHoursRepository.retrieveOneGroup(group2_id);
        //Assert
        Assertions.assertThat(retrievedGroup).isPresent();
        Assertions.assertThat(retrievedGroup.get().getRules()).containsExactlyElementsOf(rulesOfGroup2);
    }

}