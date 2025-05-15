package com.keli.Controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.keli.Utilities.CSVEntry;
import com.keli.Utilities.GraphQLResponse;
import com.keli.Utilities.Utils;
import com.keli.Utilities.RequestObject;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

public class FASolutionsApiConsumer {
    private final RequestObject requestObject;
    private RestTemplate restTemplate = new RestTemplate();

    public FASolutionsApiConsumer(RequestObject requestObject) {
        this.requestObject = requestObject;
    }

    public byte[] getReport() {
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
            byte[] csv = getCsvFile(response.getBody());
            return csv;
        } catch (Exception e) {
            System.err.println("Request failed: " + e.getMessage());
        }

        return null;
    }

    private byte[] getCsvFile(GraphQLResponse body) {
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
        byte[] fileByte = null;
        try {
            Path file = Files.write(Paths.get("exampleFile.csv"), content.toString().getBytes(StandardCharsets.UTF_8));
            fileByte = Files.readAllBytes(file);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return fileByte;
    }

    private String getAccessToken() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String username = "recruitment-assignment";
            String password = "vbv981t!sG(a";
            String url = "https://tryme.fasolutions.com/auth/realms/fa/protocol/openid-connect/token";

            // Prepare form data
            String formData = "client_id=" + URLEncoder.encode("external-api", StandardCharsets.UTF_8.toString())
                    + "&username=" + URLEncoder.encode(username, StandardCharsets.UTF_8.toString())
                    + "&password=" + URLEncoder.encode(password, StandardCharsets.UTF_8.toString())
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
