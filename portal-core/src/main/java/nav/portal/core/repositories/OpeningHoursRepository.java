package nav.portal.core.repositories;

import nav.portal.core.entities.OpeningRuleEntity;
import nav.portal.core.entities.OpeningHoursGroup;
import nav.portal.core.exceptionHandling.ExceptionUtil;
import org.actioncontroller.HttpRequestException;
import org.fluentjdbc.*;

import java.sql.SQLException;
import java.util.*;

public class OpeningHoursRepository {


        private final DbContextTable openingRuleTable;
        private final DbContextTable openingHoursGroupTable;
        private final DbContextTable openingHoursConnectedTable;



        public OpeningHoursRepository(DbContext dbContext) {
            openingRuleTable = dbContext.table("opening_rule");
            openingHoursGroupTable = dbContext.table("opening_rule_group");
            openingHoursConnectedTable = dbContext.table("opening_hours_connection");
        }

        public UUID save(OpeningRuleEntity entity) {
            //Sjekk på navn
            if(openingRuleTable.where("name",entity.getName()).getCount()>0) {
                throw new HttpRequestException("Åpningstid med navn: "+ entity.getName() +" finnes allerede");
            }
            DatabaseSaveResult<UUID> result = openingRuleTable.newSaveBuilderWithUUID("id", entity.getId())
                    .setField("name",entity.getName())
                    .setField("rule", entity.getRule())
                    .execute();
            return result.getId();
        }

        public void update(OpeningRuleEntity entity) {
           openingRuleTable.where("id", entity.getId())
                    .update()
                    .setField("name",entity.getName())
                    .setField("rule", entity.getRule())
                    .execute();
        }

        public boolean deleteOpeninghours(UUID openingHoursId){
            if(openingRuleTable.where("id",openingHoursId).singleObject(OpeningHoursRepository::toOpeningHours).isEmpty()){
                return false;
            }
            openingRuleTable.where("id", openingHoursId).executeDelete();
            return true;
        }

    public UUID saveGroup(OpeningHoursGroup group) {
        //Sjekk på navn
        if(openingHoursGroupTable.where("name",group.getName()).getCount()>0) {
            throw new HttpRequestException("Åpningstidsgruppe med navn: "+ group.getName() +" finnes allerede");
        }
        DatabaseSaveResult<UUID> result = openingHoursGroupTable
                .newSaveBuilderWithUUID("id", group.getId())
                .setField("name",group.getName())
                .execute();

        group.getRules().stream().forEach(rule ->
                openingHoursConnectedTable
                        .insert()
                        .setField("group_id", result.getId())
                        .setField("rule_id", rule.getId())
                        .execute()
                );
        return result.getId();
    }

    public void updateGroup(OpeningHoursGroup group) {
        openingHoursGroupTable.where("id", group.getId())
                .update()
                .setField("name",group.getName())
                .execute();

        openingHoursConnectedTable
                        .where("group_id", group.getId())
                        .executeDelete();

        group.getRules().stream().forEach(rule ->
                openingHoursConnectedTable
                        .insert()
                        .setField("group_id", group.getId())
                        .setField("rule_id", rule.getId())
                        .execute()
        );
    }
    public Optional<OpeningRuleEntity> retrieve(UUID id) {
        return openingRuleTable.where("id", id).singleObject(OpeningHoursRepository::toOpeningHours);
    }




    public boolean deleteOpeninghoursGroup(UUID openinghoursGroupId){
        if(openingHoursGroupTable.where("id",openinghoursGroupId).singleObject(OpeningHoursRepository::toOpeningHoursGroup).isEmpty()){
            return false;
        }
        openingHoursGroupTable.where("id", openinghoursGroupId).executeDelete();
        openingHoursConnectedTable.where("group_id", openinghoursGroupId).executeDelete();
        return true;
    }

    public OpeningHoursGroup retrieveOneGroup(UUID group_id) {
        DbContextTableAlias group = openingHoursGroupTable.alias("group");
        DbContextTableAlias g2r = openingHoursConnectedTable.alias("g2r");
        DbContextTableAlias rule = openingRuleTable.alias("rule");

        Map<OpeningHoursGroup, List<OpeningRuleEntity>> result = new HashMap<>();
        group
                .where("id" , group_id)
                .leftJoin(group.column("id"), g2r.column("group_id"))
                .leftJoin(g2r.column("rule_id"), rule.column("id"))
                .list(row -> {
//                    List<OpeningRuleEntity> ruleList = result
//                            .computeIfAbsent(toOpeningHoursGroup(row.table(group)), ignored -> new ArrayList<>());
//
//                    DatabaseRow ruleRow = row.table(rule);
//                    Optional.ofNullable(row.getUUID("rule_id"))
//                            .ifPresent(ruleId -> ruleList.add(OpeningHoursRepository.toOpeningHours(ruleRow)));
                    return null;
                });
        OpeningHoursGroup resultGroup = result.keySet().stream()
                .findFirst().orElseThrow(() -> new IllegalArgumentException("Not found: group with id " + group_id));

        return resultGroup.setRules(result.get(resultGroup));
    }

    static OpeningRuleEntity toOpeningHours(DatabaseRow row){
        try {
            return new OpeningRuleEntity(row.getUUID("id"),
                    row.getString("name"),
                    row.getString("rule"));
        } catch (SQLException e) {
            throw ExceptionUtil.soften(e);
        }
    }

    static OpeningHoursGroup toOpeningHoursGroup(DatabaseRow row){
        try {
            return new OpeningHoursGroup(row.getUUID("id"),
                    row.getString("name"), Collections.emptyList());
        } catch (SQLException e) {
            throw ExceptionUtil.soften(e);
        }
    }



    }
