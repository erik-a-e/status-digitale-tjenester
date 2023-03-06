package nav.portal.core.repositories;

import nav.portal.core.entities.OpeningHoursGroupEntity;
import nav.portal.core.entities.OpeningHoursRule;
import nav.portal.core.entities.OpeningHoursRuleEntity;
import nav.portal.core.entities.OpeningHoursGroup;
import nav.portal.core.enums.RuleType;
import nav.portal.core.exceptionHandling.ExceptionUtil;
import org.actioncontroller.HttpRequestException;
import org.fluentjdbc.*;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class OpeningHoursRepository {


        private final DbContextTable ohRuleTable;
        private final DbContextTable ohGroupTable;




        public OpeningHoursRepository(DbContext dbContext) {
            ohRuleTable = dbContext.table("oh_rule");
            ohGroupTable = dbContext.table("oh_group");
        }

        public UUID save(OpeningHoursRuleEntity entity) {
            //Sjekk på navn
            if(ohRuleTable.where("name",entity.getName()).getCount()>0) {
                throw new HttpRequestException("Åpningstid med navn: "+ entity.getName() +" finnes allerede");
            }
            DatabaseSaveResult<UUID> result = ohRuleTable.newSaveBuilderWithUUID("id", entity.getId())
                    .setField("name",entity.getName())
                    .setField("rule", entity.getRule())
                    .execute();
            return result.getId();
        }

        public void update(OpeningHoursRuleEntity entity) {
           ohRuleTable.where("id", entity.getId())
                    .update()
                    .setField("name",entity.getName())
                    .setField("rule", entity.getRule())
                    .execute();
        }

        public boolean deleteOpeninghours(UUID openingHoursId){
            if(ohRuleTable.where("id",openingHoursId).singleObject(OpeningHoursRepository::toOpeningRule).isEmpty()){
                return false;
            }
            ohRuleTable.where("id", openingHoursId).executeDelete();
            return true;
        }

    public boolean deleteOpeninghourGroup(UUID openingHoursGroupId){
        if(ohGroupTable.where("id",openingHoursGroupId).singleObject(OpeningHoursRepository::toOpeningRule).isEmpty()){
            return false;
        }
        ohGroupTable.where("id", openingHoursGroupId).executeDelete();
        return true;
    }

    public UUID saveGroup(OpeningHoursGroup group) {
        //Sjekk på navn
        if(ohGroupTable.where("name",group.getName()).getCount()>0) {
            throw new HttpRequestException("Åpningstidsgruppe med navn: "+ group.getName() +" finnes allerede");
        }
        if(containsCircularGroupDependency(group)){
            throw new HttpRequestException("Åpningsgruppe inneholder sirkuler avhengighet");
        }
        DatabaseSaveResult<UUID> result = ohGroupTable
                .newSaveBuilderWithUUID("id", group.getId())
                .setField("name",group.getName())
                .setField("rule_group_ids",group.getRules().stream().map(OpeningHoursRule::getId).map(String::valueOf).collect(Collectors.toList()))
                .execute();


        return result.getId();
    }

    private boolean containsCircularGroupDependency(OpeningHoursGroup group){
            UUID groupId = group.getId();
            List<OpeningHoursGroup> subGroups = getAllSubGroups(group);
            List<UUID> subGroupsIds = subGroups.stream().map(OpeningHoursGroup::getId).collect(Collectors.toList());
            if(subGroupsIds.contains(groupId)){
                return true;
            }
            for(OpeningHoursGroup subGroup : subGroups){
                if(containsCircularGroupDependency(subGroup)){
                    return true;
                }
            }
            return false;
    }

    private ArrayList<OpeningHoursGroup> getAllSubGroups(OpeningHoursGroup group){
            ArrayList<OpeningHoursGroup> subgroupsDirectlyUnderGroup = (ArrayList<OpeningHoursGroup>) group.getRules()
                    .stream()
                    .filter(rule -> rule.getRuleType().equals(RuleType.RULE_GROUP))
                    .map(r -> (OpeningHoursGroup) r )
                    .collect(Collectors.toList());
            ArrayList<OpeningHoursGroup> result = new ArrayList<>(subgroupsDirectlyUnderGroup);

            subgroupsDirectlyUnderGroup.forEach(g -> result.addAll(getAllSubGroups(g)));
            return result;

    }



    public Optional<OpeningHoursGroup> retrieveOneGroup(UUID group_id) {
        Optional<OpeningHoursGroupEntity> optionalOfGroupEntity = ohGroupTable.where("id", group_id).singleObject(OpeningHoursRepository::toOpeningHoursGroupEntity);
        if(optionalOfGroupEntity.isEmpty()){
            return Optional.empty() ;
        }
        OpeningHoursGroupEntity openingHoursGroup = optionalOfGroupEntity.get();

        List<OpeningHoursRule> rules = openingHoursGroup.getRules().stream()
                .map(this::retriveGroupOrRule)
                .filter(Optional::isPresent)
                .map(Optional::get).collect(Collectors.toList());

        OpeningHoursGroup result = new OpeningHoursGroup()
                .setId(openingHoursGroup.getId())
                .setName(openingHoursGroup.getName())
                .setRules(rules);


    return Optional.of(result);
    }

    public Optional<OpeningHoursRuleEntity> retriveRule(UUID id){
            return ohGroupTable.where("id", id).singleObject(OpeningHoursRepository::toOpeningRule);
    }


    private Optional<OpeningHoursRule> retriveGroupOrRule(UUID id) {
        Optional<OpeningHoursRuleEntity> ruleEntity = ohRuleTable.where("id", id).singleObject(OpeningHoursRepository::toOpeningRule);
        if(ruleEntity.isPresent()){
            return Optional.of(ruleEntity.get());
        }
        return  Optional.of(retrieveOneGroup(id).get());


    }

    static OpeningHoursRuleEntity toOpeningRule(DatabaseRow row){
        try {
            return new OpeningHoursRuleEntity(row.getUUID("id"),
                    row.getString("name"),
                    row.getString("rule"));
        } catch (SQLException e) {
            throw ExceptionUtil.soften(e);
        }
    }

    static OpeningHoursGroupEntity toOpeningHoursGroupEntity(DatabaseRow row){
        try {
            return new OpeningHoursGroupEntity(row.getUUID("id"),
                    row.getString("name"),
                    row.getStringList("rule_group_ids").stream()
                            .map(UUID::fromString)
                            .collect(Collectors.toList()));
        } catch (SQLException e) {
            throw ExceptionUtil.soften(e);
        }
    }



    }
