package no.nav.portal.rest.api.v3.controllers;

import nav.portal.core.repositories.ServiceRepository;
import no.nav.portal.rest.api.Helpers.OpeningHoursHelper;
import no.nav.portal.rest.api.Helpers.ServiceControllerHelper;
import no.nav.portal.rest.api.Helpers.Util;
import no.portal.web.generated.api.AreaDto;
import no.portal.web.generated.api.DashboardDto;
import no.portal.web.generated.api.OHGroupThinDto;
import no.portal.web.generated.api.OHRuleDto;

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
    public OHRuleDto newRule(@JsonBody OHRuleDto oHRuleDto) {
        if(!openingHoursHelper.isValidRule(oHRuleDto)){
            throw new HttpRequestException("Rule not valid: "+ oHRuleDto.getRule());
        }
        return openingHoursHelper.saveNewRule(oHRuleDto);
    }

    @PUT("/OpeningHours/Rule")
    @JsonBody
    public void updateRule(@JsonBody OHRuleDto oHRuleDto) {
        if(!openingHoursHelper.isValidRule(oHRuleDto)){
            throw new HttpRequestException("Rule not valid: "+ oHRuleDto.getRule());
        }
        openingHoursHelper.updateRule(oHRuleDto);
    }

    @DELETE("/OpeningHours/Rule/:Rule_id")
    @JsonBody
    public void deleteRule(@PathParam("Rule_id") UUID rule_id){
        openingHoursHelper.deleteRule(rule_id);
    }

    @GET("/OpeningHours/Rule/:Rule_id")
    @JsonBody
    public OHRuleDto getRule(@PathParam("Rule_id") UUID rule_id) {
        return openingHoursHelper.getRule(rule_id);
    }

    @POST("/OpeningHours/Group")
    @JsonBody
    public OHGroupThinDto newGroup(@JsonBody OHGroupThinDto oHGroupThinDto) {
        return openingHoursHelper.saveGroup(oHGroupThinDto);
    }

    @PUT("/OpeningHours/Group")
    @JsonBody
    public void updateGroup(@JsonBody OHGroupThinDto oHGroupThinDto) {
        openingHoursHelper.updateGroup(oHGroupThinDto);
    }

    @DELETE("/OpeningHours/Group/:Group_id")
    @JsonBody
    public void deleteGroup(@PathParam("Group_id") UUID group_id){
        openingHoursHelper.deleteGroup(group_id);
    }

//    @GET("/OpeningHours/Group/:Group_id")
//    @JsonBody
//    public OHGroupDto getGroup(@PathParam("Group_id") UUID group_id) {
//        return openingHoursHelper.getGroup(group_id);
//    }
}
