package no.nav.portal.rest.api.v3.controllers;

import nav.portal.core.repositories.ServiceRepository;
import no.nav.portal.rest.api.Helpers.OpeningHoursHelper;
import no.nav.portal.rest.api.Helpers.ServiceControllerHelper;
import no.nav.portal.rest.api.Helpers.Util;
import no.portal.web.generated.api.OpeningHoursRuleDto;

import org.actioncontroller.*;
import org.actioncontroller.json.JsonBody;
import org.fluentjdbc.DbContext;

import java.util.UUID;

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

    @DELETE("/OpeningHours/Rule/:Rule_id")
    @JsonBody
    public void deleteRule(@PathParam("Rule_id") UUID rule_id){
        openingHoursHelper.deleteRule(rule_id);
    }


}
