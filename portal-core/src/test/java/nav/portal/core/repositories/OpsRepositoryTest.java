package nav.portal.core.repositories;

import nav.portal.core.entities.AreaEntity;
import nav.portal.core.entities.OpsMessageEntity;
import nav.portal.core.entities.ServiceEntity;
import org.fluentjdbc.DbContext;
import org.fluentjdbc.DbContextConnection;
import org.junit.jupiter.api.AfterEach;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import javax.sql.DataSource;

import java.util.*;
import java.util.stream.Collectors;

class OpsRepositoryTest {

    private final DataSource dataSource = TestDataSource.create();
    private final DbContext dbContext = new DbContext();
    private final OpsRepository opsRepository = new OpsRepository(dbContext);
    private final DashboardRepository dashboardRepository = new DashboardRepository(dbContext);
    private final AreaRepository areaRepository = new AreaRepository(dbContext);
    private final ServiceRepository serviceRepository = new ServiceRepository(dbContext);
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

    @Test
    void save() {
        //Arrange
        List<ServiceEntity> services = SampleData.getRandomLengthNonEmptyListOfServiceEntity();
        List<UUID> serviceIds = new ArrayList<>();
        services.forEach(service -> {
            service.setId(serviceRepository.save(service));
            serviceIds.add(service.getId());
        });
        OpsMessageEntity opsMessageEntity = SampleData.getRandomOpsMessageEntity();
        //Act
        opsMessageEntity.setId(opsRepository.save(opsMessageEntity, serviceIds));
        UUID opsId = opsMessageEntity.getId();
        //Assert
        Map.Entry<OpsMessageEntity, List<ServiceEntity>> messageOnServices = opsRepository.retrieveOne(opsId);
        Assertions.assertThat(messageOnServices.getKey()).isEqualTo(opsMessageEntity);
        Assertions.assertThat(messageOnServices.getValue()).containsAll(services);
    }

    @Test
    void deleteOps() {
        //Arrange
        List<ServiceEntity> services = SampleData.getRandomLengthNonEmptyListOfServiceEntity();
        List<UUID> serviceIds = new ArrayList<>();
        services.forEach(service -> {
            service.setId(serviceRepository.save(service));
            serviceIds.add(service.getId());
        });
        OpsMessageEntity opsMessageToBeDeleted = SampleData.getRandomOpsMessageEntity();
        UUID opsId = opsRepository.save(opsMessageToBeDeleted, serviceIds);
        opsMessageToBeDeleted.setId(opsId);
        Map.Entry<OpsMessageEntity, List<ServiceEntity>> retrievedOpsMessageBeforeDelete
                = opsRepository.retrieveOne(opsId);
        //Act
        opsRepository.deleteOps(opsId);
        boolean isDeleted = opsRepository.isEntryDeleted(opsId);
        //Assert
        Assertions.assertThat(retrievedOpsMessageBeforeDelete.getKey()).isEqualTo(opsMessageToBeDeleted);
        Assertions.assertThat(retrievedOpsMessageBeforeDelete.getValue()).containsAll(services);
        Assertions.assertThat(isDeleted).isTrue();
    }

    @Test
    void setServicesOnOpsMessage() {
        //Arrange
        List<ServiceEntity> services = SampleData.getRandomLengthNonEmptyListOfServiceEntity();
        List<UUID> serviceIds = new ArrayList<>();
        services.forEach(service -> {
            service.setId(serviceRepository.save(service));
            serviceIds.add(service.getId());
        });
        OpsMessageEntity opsMessageEntity = SampleData.getRandomOpsMessageEntity();
        UUID opsId = opsRepository.save(opsMessageEntity, new ArrayList<>());
        //Act
        opsRepository.setServicesOnOpsMessage(opsId, serviceIds);
        //Assert
        Map.Entry<OpsMessageEntity, List<ServiceEntity>> messageOnServices = opsRepository.retrieveOne(opsId);
        Assertions.assertThat(messageOnServices.getKey().getId()).isEqualTo(opsId);
        Assertions.assertThat(messageOnServices.getKey().getExternalHeader()).isEqualTo(opsMessageEntity.getExternalHeader());
        Assertions.assertThat(messageOnServices.getValue()).containsAll(services);
    }

    @Test
    void retrieveOne() {
        //Arrange
        List<ServiceEntity> services = SampleData.getRandomLengthNonEmptyListOfServiceEntity();
        List<UUID> serviceIds = new ArrayList<>();
        services.forEach(service -> {
            service.setId(serviceRepository.save(service));
            serviceIds.add(service.getId());
        });
        OpsMessageEntity opsMessageEntity = SampleData.getRandomOpsMessageEntity();
        opsMessageEntity.setId(opsRepository.save(opsMessageEntity, serviceIds));
        UUID opsId = opsMessageEntity.getId();
        //Act
        Map.Entry<OpsMessageEntity, List<ServiceEntity>> retrievedMessagesOnServices = opsRepository.retrieveOne(opsId);
        //Assert
        Assertions.assertThat(retrievedMessagesOnServices.getKey()).isEqualTo(opsMessageEntity);
        Assertions.assertThat(retrievedMessagesOnServices.getValue()).containsAll(services);
    }

    @Test
    void getAllOpsMessagesForDashboard() {

        //------------------ Arrange ------------------------------------
        //Lager dashboard:
        UUID dashboardId = dashboardRepository.save("Mitt nye fantastiske dashboard!");

        //Lager område og legger til på dashboard:
        AreaEntity area = SampleData.getRandomizedAreaEntity();
        UUID areaId = areaRepository.save(area);
        dashboardRepository.settAreasOnDashboard(dashboardId, areaId);

        //Lager tjeneste og legger den på område:
        ServiceEntity serviceEntity = SampleData.getRandomizedServiceEntity();
        UUID serviceId = serviceRepository.save(serviceEntity);
        areaRepository.addServiceToArea(areaId, serviceId);


        //Lager ops Message som IKKE er knyttet til noen tjeneste:
        OpsMessageEntity opsMessageEntity = SampleData.getRandomOpsMessageEntity();
        opsMessageEntity.setId(opsRepository.save(opsMessageEntity, new ArrayList<>()));

        //------------------ Act ------------------------------------

        //Henter først alle opsmeldinger på dashboardet. Nå skal ingen meldinger ligge på dashboardet.
        List<OpsMessageEntity> shouldBeEmpty = opsRepository.getAllForDashboard(dashboardId);
        //Kobler opsmeldingen til tjenesten
        opsRepository.setServicesOnOpsMessage(opsMessageEntity.getId(), List.of(serviceId));
        //Nå skal opsmeldingen være koblet mot dashboardet via tjenesten:
        List<OpsMessageEntity> shouldContainOne = opsRepository.getAllForDashboard(dashboardId);

        //------------------ Assert ------------------------------------
        Assertions.assertThat(shouldBeEmpty).isEmpty();
        Assertions.assertThat(shouldContainOne.size()).isEqualTo(1);
        Assertions.assertThat(shouldContainOne.get(0)).isEqualTo(opsMessageEntity);

    }

    @Test
    void retrieveAll() {
        //Arrange
        List<ServiceEntity> services = SampleData.getRandomLengthNonEmptyListOfServiceEntity();
        List<UUID> serviceIds = new ArrayList<>();
        services.forEach(service -> {
            service.setId(serviceRepository.save(service));
            serviceIds.add(service.getId());
        });
        OpsMessageEntity opsMessageEntity = SampleData.getRandomOpsMessageEntity();
        UUID opsId = opsRepository.save(opsMessageEntity, serviceIds);
        opsMessageEntity.setId(opsId);
        //Act
        Map<OpsMessageEntity, List<ServiceEntity>> retrievedAllMessagesOnServices
                = opsRepository.retrieveAll();
        Map.Entry<OpsMessageEntity, List<ServiceEntity>> retrievedMessagesOnServices = opsRepository.retrieveOne(opsId);
        //Assert
        Assertions.assertThat(retrievedAllMessagesOnServices).containsKey(retrievedMessagesOnServices.getKey());
        Assertions.assertThat(retrievedAllMessagesOnServices).containsValue(retrievedMessagesOnServices.getValue());
    }

    @Test
    void retrieveAllForServices(){
        //Arrange
        List<ServiceEntity> services = SampleData.getNonEmptyListOfServiceEntity(1);
        List<UUID> serviceIds = new ArrayList<>();
        services.forEach(service -> {
            service.setId(serviceRepository.save(service));
            serviceIds.add(service.getId());
        });
        List<OpsMessageEntity>opsMessageEntities = SampleData.getNonEmptyListOfOpsMessageEntity(1);

        for (OpsMessageEntity opsMessageEntity : opsMessageEntities) {
            opsMessageEntity.setId(opsRepository.save(opsMessageEntity, serviceIds));
            opsRepository.setServicesOnOpsMessage(opsMessageEntity.getId(), serviceIds);
        }

        //Act
        List<OpsMessageEntity>retrievedOpsMessages = opsRepository.retrieveAllForServices(serviceIds);
        //Assert
        Assertions.assertThat(retrievedOpsMessages.size()).isEqualTo(1);
        Assertions.assertThat(retrievedOpsMessages).containsAll(opsMessageEntities);
    }



}