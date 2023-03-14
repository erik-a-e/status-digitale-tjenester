package no.nav.portal.rest.api.Helpers;

import nav.portal.core.entities.OpeningHoursGroup;
import nav.portal.core.entities.OpeningHoursRuleEntity;
import nav.portal.core.openingHours.OpeningHoursLogic;
import nav.portal.core.repositories.OpeningHoursRepository;
import no.nav.portal.rest.api.EntityDtoMappers;
import no.portal.web.generated.api.DashboardDto;
import no.portal.web.generated.api.DashboardUpdateDto;
import no.portal.web.generated.api.OpeningHoursGroupDto;
import no.portal.web.generated.api.OpeningHoursRuleDto;
import org.fluentjdbc.DbContext;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class OpeningHoursHelper {
    private OpeningHoursRepository openingHoursRepository;
   // private OpeningHoursLogic openingHoursLogic;

    public OpeningHoursHelper(DbContext dbContext) {
        this.openingHoursRepository = new OpeningHoursRepository(dbContext);
    }


    public boolean isValidRule(OpeningHoursRuleDto openingHoursRuleDto) {
        return OpeningHoursLogic.isAValidRule(openingHoursRuleDto.getRule());
    }

    public OpeningHoursRuleDto saveNewRule(OpeningHoursRuleDto openingHoursRuleDto) {
       UUID id = openingHoursRepository.save(EntityDtoMappers.toOpeningHoursRuleEntity(openingHoursRuleDto));
       openingHoursRuleDto.setId(id);
       return openingHoursRuleDto;
    }

    public void updateRule(OpeningHoursRuleDto openingHoursRuleDto) {
        openingHoursRepository.update(EntityDtoMappers.toOpeningHoursRuleEntity(openingHoursRuleDto));
    }

    public void deleteRule(UUID rule_id) {
        openingHoursRepository.deleteOpeninghours(rule_id);
    }

    public OpeningHoursRuleDto getRule(UUID rule_id) {
        return EntityDtoMappers.toOpeningHoursRuleDto(openingHoursRepository.retriveRule(rule_id));
    }

    public OpeningHoursGroupDto saveGroup(OpeningHoursGroupDto openingHoursGroupDto) {
        UUID id = openingHoursRepository.saveGroup(EntityDtoMappers.toOpeningHoursGroup(openingHoursGroupDto));
        openingHoursGroupDto.setId(id);
        return openingHoursGroupDto;
    }

    public void deleteGroup(UUID group_id) {
        openingHoursRepository.deleteOpeninghourGroup(group_id);
    }

    public OpeningHoursGroupDto getGroup(UUID group_id) {
        return EntityDtoMappers.toOpeningHoursGroupDto(openingHoursRepository.retrieveOneGroup(group_id));
    }

    public void updateGroup(UUID group_id, OpeningHoursGroupDto openingHoursGroupDto) {
        openingHoursGroupDto.setId(group_id);
        openingHoursRepository.updateGroup(EntityDtoMappers.toOpeningHoursGroup(openingHoursGroupDto));
    }
}
