package no.nav.portal.rest.api.v3.controllers;

import nav.portal.core.repositories.ServiceRepository;
import no.nav.portal.rest.api.Helpers.OpeningHoursHelper;
import no.nav.portal.rest.api.Helpers.ServiceControllerHelper;
import no.nav.portal.rest.api.Helpers.Util;
import no.portal.web.generated.api.OpeningHoursRuleDto;

import org.actioncontroller.HttpRequestException;
import org.actioncontroller.POST;
import org.actioncontroller.PUT;
import org.actioncontroller.json.JsonBody;
import org.fluentjdbc.DbContext;

public class OpeningHoursController {

    private OpeningHoursHelper openingHoursHelper;

    public OpeningHoursController(DbContext dbContext) {
        this.openingHoursHelper = new OpeningHoursHelper(dbContext);
    }

    @POST("/OpeningHours/Rule")
    @JsonBody
    public OpeningHoursRuleDto newRule(@JsonBody OpeningHoursRuleDto openingHoursRuleDto) {
        if(!openingHoursHelper.isValidRule(openingHoursRuleDto)){
            throw new HttpRequestException("Rule not valid: "+ openingHoursRuleDto.getRule());
        }
        return openingHoursHelper.saveNewRule(openingHoursRuleDto);
    }

    @PUT("/OpeningHours/Rule")
    @JsonBody
    public void updateRule(@JsonBody OpeningHoursRuleDto openingHoursRuleDto) {
        if(!openingHoursHelper.isValidRule(openingHoursRuleDto)){
            throw new HttpRequestException("Rule not valid: "+ openingHoursRuleDto.getRule());
        }
        openingHoursHelper.updateRule(openingHoursRuleDto);
    }


}
