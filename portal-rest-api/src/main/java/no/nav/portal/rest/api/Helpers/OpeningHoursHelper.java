package no.nav.portal.rest.api.Helpers;

import nav.portal.core.entities.OpeningHoursRuleEntity;
import nav.portal.core.openingHours.OpeningHoursLogic;
import nav.portal.core.repositories.OpeningHoursRepository;
import no.nav.portal.rest.api.EntityDtoMappers;
import no.portal.web.generated.api.DashboardDto;
import no.portal.web.generated.api.OpeningHoursRuleDto;
import org.fluentjdbc.DbContext;

import java.util.Optional;
import java.util.UUID;

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

}
