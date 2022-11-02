package com.sparta.SimpleTests;

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

public class GetInfoAboutPostcodeTests {
    private static HttpResponse<String> httpResponse = null;
    private static JSONObject jsonObject = null;

    @BeforeAll
    public static void oneTimeSetup() {
        HttpClient httpClient = HttpClient.newBuilder().build();

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create("https://api.postcodes.io/postcodes/OX495NU"))
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
        Assertions.assertEquals("/postcodes/OX495NU", httpResponse.uri().getPath());
    }

    @Test
    @DisplayName("Full URI")
    public void testFullURI() {
        Assertions.assertEquals("https://api.postcodes.io/postcodes/OX495NU", httpResponse.uri().toString());
    }

    @Test
    @DisplayName("Status code is 200")
    public void testStatusCode() {
        Assertions.assertEquals(200L, httpResponse.statusCode());
    }

    @Test
    @DisplayName("Header \"Accept-ranges\" is \"bytes\"")
    public void testHeader() {
        Assertions.assertTrue(httpResponse.headers().map().get("Accept-ranges").contains("bytes"));
    }

    @Test
    @DisplayName("\"postcode\" is \"OX495NU\"")
    public void testPostcode() {
        var result = (JSONObject)jsonObject.get("result");
        Assertions.assertEquals("OX49 5NU", result.get("postcode"));
    }
}
