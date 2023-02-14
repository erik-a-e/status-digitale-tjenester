package nav.portal.core.repositories;

import nav.portal.core.entities.OpeningHoursGroup;
import nav.portal.core.entities.OpeningRuleEntity;
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
        OpeningRuleEntity rule = SampleData.getRandomizedOpeningRule();
        //Act
        UUID id = openingHoursRepository.save(rule);
        //Assert
        Assertions.assertThat(id).isNotNull();

    }

    @Test
    void update() {
    }

    @Test
    void deleteOpeninghours() {
    }

    @Test
    void saveGroup() {
    }

    @Test
    void updateGroup() {
    }

    @Test
    void retrieve() {
    }

    @Test
    void deleteOpeninghoursGroup() {
    }

    @Test
    void retrieveOneGroupSimple() {
        //Arrange
        OpeningRuleEntity rule = SampleData.getRandomizedOpeningRule();
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
        OpeningRuleEntity rule = SampleData.getRandomizedOpeningRule();
        OpeningRuleEntity rule2 = SampleData.getRandomizedOpeningRule();
        UUID rule_id = openingHoursRepository.save(rule);
        UUID rule2_id = openingHoursRepository.save(rule2);
        rule.setId(rule_id);
        rule2.setId(rule2_id);
        OpeningHoursGroup group = new OpeningHoursGroup().setName("Gruppe1").setRules(List.of(rule));
        group.setId(openingHoursRepository.saveGroup(group));

        OpeningHoursGroup group2 = new OpeningHoursGroup().setName("Gruppe2").setRules(List.of(rule2, group));
        UUID group2_id = openingHoursRepository.saveGroup(group2);
        //Act
        Optional<OpeningHoursGroup> retrievedGroup = openingHoursRepository.retrieveOneGroup(group2_id);
        //Assert
        Assertions.assertThat(retrievedGroup).isPresent();
        Assertions.assertThat(retrievedGroup.get().getRules()).containsExactly(rule);

    }

}