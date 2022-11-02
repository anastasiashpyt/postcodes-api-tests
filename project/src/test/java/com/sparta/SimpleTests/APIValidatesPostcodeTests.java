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

public class APIValidatesPostcodeTests {
    private static HttpResponse<String> httpResponse = null;
    private static JSONObject jsonObject = null;

    @BeforeAll
    public static void oneTimeSetup() {
        HttpClient httpClient = HttpClient.newBuilder().build();

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create("https://api.postcodes.io/postcodes/OX495NU/validate"))
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
        Assertions.assertEquals("/postcodes/OX495NU/validate", httpResponse.uri().getPath());
    }

    @Test
    @DisplayName("Full URI")
    public void testFullURI() {
        Assertions.assertEquals("https://api.postcodes.io/postcodes/OX495NU/validate", httpResponse.uri().toString());
    }

    @Test
    @DisplayName("Status code is 200")
    public void testStatusCode() {
        Assertions.assertEquals(200L, httpResponse.statusCode());
    }

    @Test
    @DisplayName("Header \"Content-Length\" equals 28")
    public void testHeader() {
        Assertions.assertEquals("28", httpResponse.headers().map().get("Content-Length").get(0));
    }

    @Test
    @DisplayName("Valid postcode returns result = true")
    public void testResultTrue() {
        Assertions.assertEquals(true, jsonObject.get("result"));
    }
}
