package no.nav.portal.rest.api.v3.controllers;

import nav.portal.core.repositories.ServiceRepository;
import no.nav.portal.rest.api.Helpers.OpeningHoursHelper;
import no.nav.portal.rest.api.Helpers.ServiceControllerHelper;
import no.nav.portal.rest.api.Helpers.Util;
import no.portal.web.generated.api.DashboardDto;
import no.portal.web.generated.api.OpeningHoursGroupDto;
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

    @GET("/OpeningHours/Rule/:Rule_id")
    @JsonBody
    public OpeningHoursRuleDto getRule(@PathParam("Rule_id") UUID rule_id) {
        return openingHoursHelper.getRule(rule_id);
    }

    @POST("/OpeningHours/Group")
    @JsonBody
    public OpeningHoursGroupDto newGroup(@JsonBody OpeningHoursGroupDto openingHoursGroupDto) {
        return openingHoursHelper.saveGroup(openingHoursGroupDto);
    }

    @DELETE("/OpeningHours/Group/:Group_id")
    @JsonBody
    public void deleteGroup(@PathParam("Group_id") UUID group_id){
        openingHoursHelper.deleteGroup(group_id);
    }

    @GET("/OpeningHours/Group/:Group_id")
    @JsonBody
    public OpeningHoursGroupDto getGroup(@PathParam("Group_id") UUID group_id) {
        return openingHoursHelper.getGroup(group_id);
    }
}
