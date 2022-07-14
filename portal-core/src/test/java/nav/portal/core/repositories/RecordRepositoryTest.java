package nav.portal.core.repositories;


import nav.portal.core.entities.DailyStatusAggregationForServiceEntity;
import nav.portal.core.entities.ServiceEntity;
import nav.portal.core.entities.RecordEntity;
import org.assertj.core.api.AbstractLocalDateAssert;
import org.assertj.core.api.Assert;
import org.fluentjdbc.DbContext;
import org.fluentjdbc.DbContextConnection;
import org.junit.jupiter.api.AfterEach;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.*;

import java.util.UUID;

class RecordRepositoryTest {

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

    private final RecordRepository recordRepository = new RecordRepository(dbContext);
    private final ServiceRepository serviceRepository = new ServiceRepository(dbContext);

   @Test
    void save() {
        //Arrange
        ServiceEntity service = SampleData.getRandomizedServiceEntity();
        UUID serviceId = serviceRepository.save(service);
        service.setId(serviceId);
        RecordEntity record = SampleData.getRandomizedRecordEntity();
        record.setServiceId(service.getId());
        //Act
        record.setId(recordRepository.save(record));
        Optional<RecordEntity>retrievedEntity = recordRepository.getLatestRecord(serviceId);
        record.setCreated_at(retrievedEntity.orElseThrow().getCreated_at());
        //Assert
        Assertions.assertThat(retrievedEntity.get()).isEqualTo(record);
     }

    @Test
    void getLatestRecord() {
        //Arrange
        ServiceEntity service = SampleData.getRandomizedServiceEntity();
        UUID serviceId = serviceRepository.save(service);
        service.setId(serviceId);
        RecordEntity record = SampleData.getRandomizedRecordEntity();
        record.setServiceId(service.getId());
        record.setId(recordRepository.save(record));
        //Act
        Optional<RecordEntity> retrievedRecord = recordRepository.getLatestRecord(service.getId());
        record.setCreated_at(retrievedRecord.orElseThrow().getCreated_at());
        //Assert
        Assertions.assertThat(retrievedRecord.get()).isEqualTo(record);
    }


    @Test
    void getServiceHistoryForNumberOfDays() {
        //Arrange
        ServiceEntity serviceEntity = SampleData.getRandomizedServiceEntity();
        serviceEntity.setId(serviceRepository.save(serviceEntity));
        DailyStatusAggregationForServiceEntity aggregation = SampleData.getRandomizedDailyStatusAggregationForService(serviceEntity);
        aggregation.setAggregation_date(LocalDate.now().minusDays(5));

        recordRepository.saveAggregatedRecords(aggregation);

        UUID serviceID = serviceEntity.getId();
        //Act
        List<DailyStatusAggregationForServiceEntity> shouldBeEmpty = recordRepository.getServiceHistoryForNumberOfDays(4,serviceID);
        List<DailyStatusAggregationForServiceEntity> shouldContainOne = recordRepository.getServiceHistoryForNumberOfDays(10,serviceID);

        //Assert
        Assertions.assertThat(shouldBeEmpty).isEmpty();
        Assertions.assertThat(shouldContainOne.size()).isEqualTo(1);

    }

    @Test
    void getServiceHistoryForServiceByDate() {
        //Arrange
        ServiceEntity serviceEntity = SampleData.getRandomizedServiceEntity();
        serviceEntity.setId(serviceRepository.save(serviceEntity));
        DailyStatusAggregationForServiceEntity aggregation = SampleData.getRandomizedDailyStatusAggregationForService(serviceEntity);
        aggregation.setAggregation_date(LocalDate.now().minusDays(5));

        recordRepository.saveAggregatedRecords(aggregation);
        UUID serviceId = serviceEntity.getId();
        //Act
        Optional<DailyStatusAggregationForServiceEntity> shouldBeEmpty =
                recordRepository.getServiceHistoryForServiceByDate(serviceId, ZonedDateTime.now().minusDays(4));
        Optional<DailyStatusAggregationForServiceEntity> shouldContainOne =
                recordRepository.getServiceHistoryForServiceByDate(serviceId, ZonedDateTime.now().minusDays(5));
        //Assert
        Assertions.assertThat(shouldBeEmpty).isEmpty();
        Assertions.assertThat(shouldContainOne).isPresent();
   }


    @Test
    void getLatestRecord2() {
        //Arrange
        ServiceEntity service = SampleData.getRandomizedServiceEntity();
        UUID serviceId = serviceRepository.save(service);
        service.setId(serviceId);
        RecordEntity record = SampleData.getRandomizedRecordEntity();
        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime fiveDaysBack = now.minusHours(now.getHour()).minusDays(5);
        record.setCreated_at(fiveDaysBack);
        record.setServiceId(service.getId());
        record.setId(TestUtil.saveRecordBackInTime(record,dbContext));
        //Act
        List<RecordEntity> retrievedRecord = recordRepository.getRecordsOlderThan(2);
        record.setCreated_at(retrievedRecord.get(0).getCreated_at());
        //Assert
        List<RecordEntity> allRetrievedRecord = recordRepository.getRecordsOlderThan(5);
        Assertions.assertThat(allRetrievedRecord).containsAll(retrievedRecord);
        Assertions.assertThat(retrievedRecord.get(0).getCreated_at()).isEqualTo(record.getCreated_at());
        Assertions.assertThat(retrievedRecord.get(0).getServiceId()).isEqualTo(service.getId());
        Assertions.assertThat(retrievedRecord.get(0).getId()).isEqualTo(record.getId());
    }

    @Test
    void deleteRecordsOlderThen() {
        //Arrange
        ServiceEntity service = SampleData.getRandomizedServiceEntity();
        UUID serviceId = serviceRepository.save(service);
        service.setId(serviceId);
        RecordEntity record = SampleData.getRandomizedRecordEntity();
        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime fiveDaysBack = now.minusHours(now.getHour()).minusDays(5);
        record.setCreated_at(fiveDaysBack);
        record.setServiceId(service.getId());
        record.setId(TestUtil.saveRecordBackInTime(record, dbContext));
        //Act
        List<RecordEntity> retrievedRecordsBefore = recordRepository.getRecordsOlderThan(5);
        recordRepository.deleteRecordsOlderThen(5);
        //Assert
        List<RecordEntity> retrievedRecordsAfter = recordRepository.getRecordsOlderThan(5);
        Assertions.assertThat(retrievedRecordsBefore).isNotEmpty();
        Assertions.assertThat(retrievedRecordsAfter).isEmpty();
    }
    @Test
    void getServiceHistoryForServiceByDate() {
        //Arrange
        ServiceEntity serviceEntity = SampleData.getRandomizedServiceEntity();
        serviceEntity.setId(serviceRepository.save(serviceEntity));
        DailyStatusAggregationForServiceEntity aggregation = SampleData.getRandomizedDailyStatusAggregationForService(serviceEntity);
        aggregation.setAggregation_date(LocalDate.now().minusDays(5));

        recordRepository.saveAggregatedRecords(aggregation);
        UUID serviceId = serviceEntity.getId();
        //Act
        Optional<DailyStatusAggregationForServiceEntity> shouldBeEmpty =
                recordRepository.getServiceHistoryForServiceByDate(serviceId, LocalDate.now().minusDays(4));
        Optional<DailyStatusAggregationForServiceEntity> shouldContainOne =
                recordRepository.getServiceHistoryForServiceByDate(serviceId, LocalDate.now().minusDays(5));
        //Assert
        recordRepository.getServiceHistoryForNumberOfDays(10, serviceId);
        Assertions.assertThat(shouldBeEmpty).isEmpty();
        Assertions.assertThat(shouldContainOne).isPresent();
    }

}