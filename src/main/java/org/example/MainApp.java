package org.example;                     // March 2024

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import static java.time.temporal.ChronoUnit.SECONDS;

/**
 * Using Gson Deserializer and GsonBuilder to parse JSON into Java Objects
 * that DO NOT have a matching structure and/or matching field names.
 */

// This sample uses the following API:
// API Request:   http://api.open-notify.org/iss-now.json
//
//  Response from API request: (as at March 2024)
/*
    {
            "message": "success",
            "iss_position": {
            "latitude": "-47.1606",
            "longitude": "34.2791"
            },
            "timestamp": 1709547901
            }
  */


public class MainApp {
    public static void main(String[] args) {
        MainApp mainApp = new MainApp();
        mainApp.start();
    }

    public void start() {
        System.out.println("GSON - GsonBuilder demonstration on ISS API data");
        final String url = "http://api.open-notify.org/iss-now.json";
        String jsonString=null;
        try {
            jsonString = fetchJsonFromAPI(url);
            System.out.println("JSON has been fetched successfully.");
        }
        catch ( IOException ex ) {
            System.out.println("API Access problem: " + ex.getMessage());
        }

        if (jsonString == null) {
            System.out.println("Connection failed, exiting.");
            return;
        }

        // Instantiate a GsonBuilder and register the TypeAdapter (to adapt types!)
        // passing in the IssPositionAtTime class definition,
        // the name of the deserialization object (containing the deserialize() method)
        // The Deserializer contains teh code we write to map the data from Json
        // to the structure in our IssPositionAtTime class/object.
        //
        Gson gsonBuilder = new GsonBuilder()
                .registerTypeAdapter(IssPositionAtTime.class,
                        new JsonDeserializerIssPosition())
                .serializeNulls()
                .create();

        // Now that we have set up the Adapter, we call the fromJson() method
        // to parse the JSON string and create and populate
        // the Java IssPositionAtTime object.
        //
        IssPositionAtTime issPositionAtTime =
                gsonBuilder.fromJson( jsonString,
                                      new TypeToken<IssPositionAtTime>(){}.getType()
                );

        System.out.println(issPositionAtTime);
    }

    /**
     * Using the supplied URL, perform a Http GET request to retrieve the JSON data
     * from the API Endpoint.
     * @param url
     * @return a Json String
     * @throws IOException
     */
    private String fetchJsonFromAPI(String url) throws IOException
    {
        // Ref:  https://www.baeldung.com/java-9-http-client
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()  // build an HTTP request
                .uri(URI.create(url))
                .timeout(Duration.of(10,SECONDS))
                .GET()
                .build();

        HttpResponse<String> response = null;

        // client.send() throws a Checked Exceptions, so we need to provide a try-catch block
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Check the response code returned from the API endpoint
        // A code of 200 indicates success
        if (response.statusCode() != 200) {
            System.out.println("HTTP Request failed - Status code returned = " + response.statusCode());
            return null;
        }
        // get the body (the data payload) from the HTTP response object
        String jsonResponseString = response.body();

        if (jsonResponseString == null) {
            System.out.println("Json String was empty.");
            return null;
        }
        return jsonResponseString;
    }
}