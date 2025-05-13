package com.keli.Controller;

import com.keli.Utilities.Utils;
import com.keli.Utilities.RequestObject;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

public class FASolutionsApiConsumer {
    private final RequestObject requestObject;
    private  RestTemplate restTemplate = new RestTemplate();

    public FASolutionsApiConsumer(RequestObject requestObject) {
        this.requestObject = requestObject;
    }

    public RequestObject getReport() {
        String url = "https://example.com/graphql";
        String username = "myUser";
        String password = "myPassword";

        // Encode credentials
        String basicAuth = "Basic " + Utils.encodeBasicAuth(username, password);

        // Create GraphQL query
        String query = "{ portfolio(id: \""+ requestObject + "\", from:\"" + requestObject.getFrom() + "\", to:\"" + requestObject.getTo() + "\") { name email } }";
        JSONObject json = new JSONObject();
        try {
            json.put("query", query);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        // Set headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", basicAuth);

        // Build request
        HttpEntity<String> request = new HttpEntity<>(json.toString(), headers);

        // Send POST request
        restTemplate.postForEntity(url, request, String.class);
        return null;
    }
}
