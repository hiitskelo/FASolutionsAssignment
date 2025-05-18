package com.keli.Controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.keli.Utilities.*;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

public class FASolutionsApiConsumer {
    private final RequestObject requestObject;
    private RestTemplate restTemplate = new RestTemplate();
    private final AuthProperties authProperties;

    public FASolutionsApiConsumer(RequestObject requestObject, AuthProperties authProperties) {
        this.requestObject = requestObject;
        this.authProperties = authProperties;
    }

    public String getReport() {
        String accessToken = getAccessToken();
        String url = "https://tryme.fasolutions.com/graphql";

        // Create GraphQL query
        String queryCall = "{ portfolio(id: \"" + requestObject.getId() + "\") \n\t";
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
        headers.set("Authorization", "Bearer " + accessToken);

        // Build request
        HttpEntity<String> request = new HttpEntity<>(json.toString(), headers);
        try {
            ResponseEntity<GraphQLResponse> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    request,
                    GraphQLResponse.class
            );

            System.out.println("Status code: " + response.getStatusCode());
            System.out.println("Response body: " + response.getBody());
            return getCsvFile(response.getBody());
        } catch (Exception e) {
            System.err.println("Request failed: " + e.getMessage());
        }

        return null;
    }

    private String getCsvFile(GraphQLResponse body) {
        StringBuilder content = new StringBuilder("Short name;Security name;ISIN code;Currency code;Amount;Unit price;Trade amount;Type name;Transaction date;Settlement date;\n");
        body.getData().getPortfolio().getTransactions().forEach(t -> {
            content.append(body.getData().getPortfolio().getShortName()).append(";");
            if (null != t.getSecurity()) {
                content.append(t.getSecurity().getName()).append(";");
                content.append(t.getSecurity().getIsinCode()).append(";");
            } else
                content.append(";;");
            content.append(t.getCurrencyCode()).append(";");
            content.append(t.getAmount()).append(";");
            content.append(t.getUnitPrice()).append(";");
            content.append(t.getTradeAmount()).append(";");
            content.append(t.getTypeName()).append(";");
            content.append(t.getTransactionDate()).append(";");
            content.append(t.getSettlementDate()).append(";\n");
        });
        return content.toString();
    }

    private String getAccessToken() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String url = "https://tryme.fasolutions.com/auth/realms/fa/protocol/openid-connect/token";

            // Prepare form data
            String formData = "client_id=" + URLEncoder.encode("external-api", StandardCharsets.UTF_8.toString())
                    + "&username=" + URLEncoder.encode(authProperties.getUsername(), StandardCharsets.UTF_8.toString())
                    + "&password=" + URLEncoder.encode(authProperties.getPassword(), StandardCharsets.UTF_8.toString())
                    + "&grant_type=" + URLEncoder.encode("password", StandardCharsets.UTF_8.toString());

            // Set headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

            HttpEntity<String> request = new HttpEntity<>(formData, headers);

            // Send POST request
            ResponseEntity<String> response = restTemplate.exchange(
                    URI.create(url),
                    HttpMethod.POST,
                    request,
                    String.class
            );

            // Parse JSON response to extract access_token
            JsonNode json = objectMapper.readTree(response.getBody());
            String accessToken = json.get("access_token").asText();

            System.out.println("Access Token: " + accessToken);
            return accessToken;
        } catch (Exception e) {
            System.err.println("Error fetching access token: " + e.getMessage());
            return null;
        }
    }
}
