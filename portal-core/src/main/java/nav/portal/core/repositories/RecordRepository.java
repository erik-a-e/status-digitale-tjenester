package nav.portal.core.repositories;

import nav.portal.core.entities.RecordDeltaEntity;
import nav.portal.core.entities.RecordEntity;
import nav.portal.core.entities.ServiceEntity;
import nav.portal.core.enums.RecordSource;
import nav.portal.core.enums.ServiceStatus;
import org.fluentjdbc.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RecordRepository {
    private final DbContextTable recordTable;
    private final DbContextTable recordDeltaTable;
    private final DbContextTable serviceTable;


    public RecordRepository(DbContext dbContext) {
        serviceTable = dbContext.table(new DatabaseTableWithTimestamps("service"));
        recordTable = dbContext.table(new DatabaseTableWithTimestamps("service_status"));
        recordDeltaTable = dbContext.table(new DatabaseTableWithTimestamps("service_status_delta"));

    }

    public UUID save(RecordEntity entity) {
        RecordSource source = entity.getRecordSource() != null? entity.getRecordSource(): RecordSource.UNKNOWN;
        DatabaseSaveResult<UUID> result = recordTable.newSaveBuilderWithUUID("id", entity.getId())
                .setField("service_id", entity.getServiceId())
                .setField("status", entity.getStatus())
                .setField("description", entity.getDescription())
                .setField("logglink", entity.getLogglink())
                .setField("response_time", entity.getResponsetime())
                .setField("source", source)
                .execute();

        return result.getId();
    }

    //UUIDen som settes her skal IKKE generes, men settes fra uid fra orginal record.
    public UUID saveNewStatusDiff(RecordEntity entity) {
        DatabaseSaveResult<UUID> result = recordDeltaTable.newSaveBuilderWithUUID("id", entity.getId())
                .setField("service_id", entity.getServiceId())
                .setField("status", entity.getStatus())
                .setField("active", entity.getActive())
                .setField("counter", 1) // Når denne metoden brukes, skal det være første gang diff lagres, og counter skal være 1
                .execute();
        return result.getId();
    }

    public void setOldStatusDiffInactive(RecordDeltaEntity entity) {

        recordDeltaTable.where("service_id", entity.getServiceId())
                .where("active",true).update()
                .setField("active", false)
                .execute();

    }

    public void increaseCountOnStatusDiff(RecordDeltaEntity entity) {
        recordDeltaTable.newSaveBuilderWithUUID("id", entity.getId())
                .setField("counter", entity.getCounter() + 1)
                .execute();

    }

    public Optional<RecordDeltaEntity> getLatestRecordDiff(UUID serviceId) {
        return recordDeltaTable.where("service_id", serviceId)
                .orderBy("created_at DESC")
                .limit(1)
                .singleObject(RecordRepository::toRecordDelta);
    }

    public Optional<RecordDeltaEntity> getLatestRecordDiffBeforeDate(UUID serviceId, LocalDate date) {
        return recordDeltaTable.where("service_id", serviceId)
                .whereExpression("created_at <= ?", LocalDateTime.of(date, LocalTime.of(0,0)))
                .orderBy("created_at DESC")
                .limit(1)
                .singleObject(RecordRepository::toRecordDelta);
    }

    public Optional<RecordDeltaEntity> getActiveRecordDelta(UUID serviceId){
        return recordDeltaTable.where("service_id", serviceId)
                .where("active",true)
                .singleObject(RecordRepository::toRecordDelta);
    }



    public Optional<RecordEntity> getLatestRecord(UUID serviceId) {
        return recordTable.where("service_id", serviceId)
                .orderBy("created_at DESC")
                .limit(1)
                .singleObject(RecordRepository::toRecord);
    }

    //TODO denne skal bli paginert
    public List<RecordEntity> getRecordHistory(UUID serviceId, int maxNumberOfRecords) {
        return recordTable.where("service_id", serviceId)
                .orderBy("created_at DESC")
                .limit(maxNumberOfRecords)
                .list(RecordRepository::toRecord);
    }


    public Map<UUID, Map<LocalDate,List<RecordEntity>>> getAllRecordsOrderedByServiceIdAndDate(){

        List<RecordEntity> allRecords = recordTable.unordered().list(RecordRepository::toRecord);
        List<UUID> serviceUUIDs = allRecords.stream().map(RecordEntity::getServiceId).distinct().collect(Collectors.toList());
        Map<UUID, Map<LocalDate,List<RecordEntity>>> result = new HashMap<>();
        serviceUUIDs.forEach(uuid -> {
            List<RecordEntity> recordsForService = allRecords.stream().filter(r -> r.getServiceId().equals(uuid)).collect(Collectors.toList());
            List<LocalDate> dates = recordsForService.stream().map(r -> r.getCreated_at().toLocalDate()).distinct().collect(Collectors.toList());
            Map<LocalDate,List<RecordEntity>> resultForOneService = new HashMap<>();
            dates.forEach(date -> {
                resultForOneService.put(date,recordsForService.stream().filter(r -> r.getCreated_at().toLocalDate().equals(date)).collect(Collectors.toList()));
            });
            result.put(uuid,resultForOneService);

        });
        return result;

    }


    public List<RecordEntity> getAllRecordsFromYesterday(){
        ZonedDateTime yesterdayMidnight = ZonedDateTime.now().minusDays(1).truncatedTo(ChronoUnit.DAYS);
        ZonedDateTime todayMidnight = ZonedDateTime.now().truncatedTo(ChronoUnit.DAYS);

        return recordTable
                .whereExpression("created_at <= ?", todayMidnight )
                .whereExpression("created_at >= ?", yesterdayMidnight)
                .list(RecordRepository::toRecord);
    }
    public ZonedDateTime getLatestGcpPollTime(){
        Optional<ServiceEntity> optionalServiceEntity = serviceTable.where("polling_on_prem", false)
                .whereExpression("polling_url is NOT null").stream(ServiceRepository::toService).findFirst();
        if(optionalServiceEntity.isPresent()){
            Optional<RecordEntity> latest = getLatestRecord(optionalServiceEntity.get().getId());
            return latest.map(RecordEntity::getCreated_at).orElse(null);
        }
        return null;
    }

    public ZonedDateTime getLatestFssPollTime(){
        Optional<ServiceEntity> optionalServiceEntity = serviceTable.where("polling_on_prem", true)
                .whereExpression("polling_url is NOT null").stream(ServiceRepository::toService).findFirst();
        if(optionalServiceEntity.isPresent()){
            Optional<RecordEntity> latest = getLatestRecord(optionalServiceEntity.get().getId());
            return latest.map(RecordEntity::getCreated_at).orElse(null);
        }
        return null;
    }

    public List<RecordEntity> getRecordsOlderThan(int daysOld){
        return recordTable
                .whereExpression("created_at <= ?", ZonedDateTime.now().minusDays(daysOld))
                .list(RecordRepository::toRecord);
    }
    public void deleteRecordsOlderThen(int daysOld) {
        recordTable.whereExpression("created_at <= ?", ZonedDateTime.now().minusDays(daysOld))
                .executeDelete();
    }
    public void deleteRecordsForDate(UUID serviceID,LocalDate date) {
        recordTable.where("id", serviceID)
                .whereExpression("created_at >= ?", date.minusDays(1))
                .whereExpression("created_at <= ?", date)
                .executeDelete();
    }


    public static UUID saveRecordBackInTime(RecordEntity entity, DbContext dbContext) {
        DbContextTable recordTable = dbContext.table(new DatabaseTableImpl("service_status"));
        DatabaseSaveResult<UUID> result = recordTable.newSaveBuilderWithUUID("id", entity.getId())
                .setField("service_id", entity.getServiceId())
                .setField("created_at", entity.getCreated_at())
                .setField("status", entity.getStatus())
                .setField("description", entity.getDescription())
                .setField("logglink", entity.getLogglink())
                .setField("response_time", entity.getResponsetime())
                .execute();
        return result.getId();
    }



    private static RecordEntity toRecord(DatabaseRow row) throws SQLException {
        return new RecordEntity()
                    .setId(row.getUUID("id"))
                    .setServiceId(row.getUUID("service_id"))
                    .setDescription(row.getString("description"))
                    .setLogglink(row.getString("logglink"))
                    .setStatus(ServiceStatus.fromDb(row.getString("status")).orElse(ServiceStatus.ISSUE))
                    .setCreated_at(row.getZonedDateTime("created_at"))
                    .setResponsetime(row.getInt("response_time"))
                    .setRecordSource(RecordSource.fromDb(row.getString("source")).orElse(RecordSource.UNKNOWN));
    }


    private static RecordDeltaEntity toRecordDelta(DatabaseRow row) throws SQLException {
        return new RecordDeltaEntity()
                .setId(row.getUUID("id"))
                .setServiceId(row.getUUID("service_id"))
                .setCounter(row.getInt("counter"))
                .setActive(row.getBoolean("active"))
                .setStatus(ServiceStatus.fromDb(row.getString("status")).orElse(ServiceStatus.ISSUE))
                .setUpdated_at(row.getZonedDateTime("updated_at"))
                .setCreated_at(row.getZonedDateTime("created_at"));
    }

    public void deleteRecords(List<RecordEntity> records) {
        recordTable.whereIn("id", records.stream().map(RecordEntity::getId).collect(Collectors.toList()))
                .executeDelete();

    }

    public void deleteRecordsOlderThan48hours() {
        recordTable.whereExpression("created_at <= ?", ZonedDateTime.now().minusHours(48))
                .executeDelete();
    }


    public static class Query {

        private final DbContextSelectBuilder query;

        public Query(DbContextSelectBuilder query) {
            this.query = query;
        }

        public Stream<RecordEntity> stream() {
            return query.stream(RecordRepository::toRecord);
        }

        private RecordRepository.Query query(DbContextSelectBuilder query) {
            return this;
        }
    }

}
