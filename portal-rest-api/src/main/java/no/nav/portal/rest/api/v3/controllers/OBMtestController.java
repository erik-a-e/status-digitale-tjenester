package no.nav.portal.rest.api.v3.controllers;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.actioncontroller.GET;
import org.actioncontroller.json.JsonBody;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;


public class OBMtestController {


    @GET("/OBM")
    @JsonBody
    public String obmTest() throws Exception {

        JSONObject obj=new JSONObject();
        obj.put("name","sonoo");
        obj.put("age",27);
        obj.put("salary",600000);

        post();
        return  obj.toString();
    }


    public static void post() throws Exception {
        URL url = new URL ("www.obm.no");
        HttpsURLConnection con = (HttpsURLConnection)url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty ("Authorization", ""); //Denne må
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);

        JSONObject obj=new JSONObject();
        obj.put("name","sonoo");
        obj.put("age",27);
        obj.put("salary",600000);



        String jsonInputString = obj.toString();;
            try(OutputStream os = con.getOutputStream()) {

                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            try(BufferedReader br = new BufferedReader(
                    new InputStreamReader(con.getInputStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine = null;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                System.out.println(response);
                System.out.println("Postet status ok");
            }
        }
}
