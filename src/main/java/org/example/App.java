package org.example;                     // March 2023

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Using GSON GsonBuilder to parse JSON into Java Objects that DO NOT have a
 * matching structure.
 */

// Demonstrates how to fetch JSON from a remote API, and
// parse the returned JSON into Java Objects, that have
// a corresponding structure.

// This sample uses the following API:
// API Request:   http://api.open-notify.org/iss-now.json
//
//  Response from API request: (as at March 2023)
// { "timestamp": 1678523947,                   // UNIX Time (seconds since 1st Jan 1970)
//   "iss_position": { "latitude": "-47.8432",
//                     "longitude": "93.5798"},
//   "message": "success"
// }

public class App {
    public static void main(String[] args) {
        App app = new App();
        app.start();
    }

    public void start() {

        System.out.println("GSON - GsonBuilder demonstration on ISS API data");

        String url = "http://api.open-notify.org/iss-now.json";

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

    private String fetchJsonFromAPI(String uri) throws IOException
    {
        final int CONNECTION_OK = 200;  // code returned if connection is successful

        URL url = new URL(uri);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        int responseCode = connection.getResponseCode();

        if (responseCode == CONNECTION_OK) {
            // we have connected successfully, so now
            // create an input buffer to read from the API stream
            BufferedReader inBuffer = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));

            String inputLine;

            // create a String buffer to build up the JSON String
            // that will be returned from the stream
            StringBuffer strBuffer = new StringBuffer();

            // read in all lines from stream until the stream
            // has been emptied.
            while ((inputLine = inBuffer.readLine()) != null) {
                strBuffer.append(inputLine);
            }
            inBuffer.close();

            return strBuffer.toString();  // return the JSON String
        }
        return null; // if connection failed
    }
}