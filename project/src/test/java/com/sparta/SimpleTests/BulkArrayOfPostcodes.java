package com.sparta.SimpleTests;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;

public class BulkArrayOfPostcodes {
    private static HttpResponse<String> httpResponse = null;
    private static JSONObject jsonObject = null;

    @BeforeAll
    public static void oneTimeSetup() {
        HttpClient httpClient = HttpClient.newBuilder().build();

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString("{\"postcodes\" : [\"OX49 5NU\", \"M32 0JG\", \"NE30 1DP\"]}"))
                .uri(URI.create("https://api.postcodes.io/postcodes"))
                .setHeader("Content-type", "application/json")
                .build();

        try {
            httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        JSONParser jsonParser = new JSONParser();
        try {
            jsonObject = (JSONObject) jsonParser.parse(httpResponse.body());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("URI Path")
    public void testURIPath() {
        Assertions.assertEquals("/postcodes", httpResponse.uri().getPath());
    }

    @Test
    @DisplayName("Full URI")
    public void testFullURI() {
        Assertions.assertEquals("https://api.postcodes.io/postcodes", httpResponse.uri().toString());
    }

    @Test
    @DisplayName("Status code is 200")
    public void testStatusCode() {
        Assertions.assertEquals(200L, httpResponse.statusCode());
    }

    @Test
    @DisplayName("Response body contains requested postcodes")
    public void testBody() {
        JSONArray result = (JSONArray)jsonObject.get("result");
        JSONObject obj = (JSONObject) result.get(0);
        Assertions.assertEquals("OX49 5NU", obj.get("query"));
        obj = (JSONObject) result.get(1);
        Assertions.assertEquals("M32 0JG", obj.get("query"));
        obj = (JSONObject) result.get(2);
        Assertions.assertEquals("NE30 1DP", obj.get("query"));
    }
}
