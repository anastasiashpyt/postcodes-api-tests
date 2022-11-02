package com.sparta.SimpleTests;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


public class randomUriTests {

    private static HttpResponse<String> httpResponse = null;
    private static JSONObject jsonObject = null;

    @BeforeAll
    public static void oneTimeSetUp() {
        HttpClient httpClient = HttpClient.newBuilder().build();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create("https://api.postcodes.io/random/postcodes"))
                .setHeader("Content-type", "application/json")
                .build();
        try {
            httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }


        try {
            JSONParser jsonParser = new JSONParser();
            jsonObject = (JSONObject)jsonParser.parse(httpResponse.body());
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("URI path")
    public void testResponsePath() {
        Assertions.assertEquals("/random/postcodes", httpResponse.uri().getPath());
    }

    @Test
    @DisplayName("status code")
    public void testResponseStatusCode() {
        Assertions.assertEquals(200, httpResponse.statusCode());
    }



}
