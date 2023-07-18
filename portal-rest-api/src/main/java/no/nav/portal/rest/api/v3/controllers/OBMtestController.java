package no.nav.portal.rest.api.v3.controllers;


import org.actioncontroller.GET;


public class OBMtestController {


    @GET("/OBM")
    public String obmTest() {
        return "OBM test";
    }
}
