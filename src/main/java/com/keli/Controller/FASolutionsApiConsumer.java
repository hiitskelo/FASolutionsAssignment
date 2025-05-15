package com.keli.Controller;

import com.keli.Utilities.Utils;
import com.keli.Utilities.RequestObject;
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
        String url = "https://tryme.fasolutions.com";
        String username = "recruitment-assignment";
        String password = "vbv981t!sG(a";

        // Encode credentials
        String basicAuth = "Basic " + Utils.encodeBasicAuth(username, password);

        // Create GraphQL query
        String queryCall = "{ portfolio(id: \""+ requestObject.getId() + "\", from:\"" + requestObject.getFrom() + "\", to:\"" + requestObject.getTo() + "\")";
        String parameters = "{shortName\n" +
                "    transactions{\n" +
                "      security{\n" +
                "        name\n" +
                "        isinCode\n" +
                "      }\n" +
                "      currencyCode\n" +
                "      amount\n" +
                "      unitPrice\n" +
                "      tradeAmount\n" +
                "      typeName\n" +
                "      transactionDate\n" +
                "      settlementDate\n" +
                "    }\n" +
                "  }\n" +
                "}";
        JSONObject json = new JSONObject();
        json.put("query", queryCall + parameters);

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
