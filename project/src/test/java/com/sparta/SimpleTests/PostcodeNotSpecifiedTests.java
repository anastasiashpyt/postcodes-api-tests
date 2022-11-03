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

public class PostcodeNotSpecifiedTests {

    private static HttpResponse<String> httpResponse = null;
    private static JSONObject jsonObject = null;

    @BeforeAll
    public static void oneTimeSetup() {
        HttpClient httpClient = HttpClient.newBuilder().build();

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create("https://api.postcodes.io/postcodes/"))
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
    @DisplayName("Error message contains \"No postcode query submitted. Remember to include query parameter\"")
    public void errorMessage() {
        Assertions.assertTrue(jsonObject.get("error").toString().contains("No postcode query submitted. Remember to include query parameter"));
    }

    @Test
    @DisplayName("status code")
    public void testResponseStatusCode() {
        Assertions.assertEquals(400, httpResponse.statusCode());
    }

    @Test
    @DisplayName("URI path")
    public void testResponsePath() {
        Assertions.assertEquals("/postcodes/", httpResponse.uri().getPath());
    }


    @Test
    @DisplayName("Full URI")
    public void testFullURI() {
        Assertions.assertEquals("https://api.postcodes.io/postcodes/", httpResponse.uri().toString());
    }
}


