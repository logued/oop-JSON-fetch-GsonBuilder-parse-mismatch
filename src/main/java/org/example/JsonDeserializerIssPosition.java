package org.example;

// Class that implements the JsonDeserializer interface defined in Gson library
// and implements the deserialize() method that will be used by the Gson parser to
// extract data from the JSON string, and put that data into the desired fields
// in the user defined Java Objects ( i.e. IssPositionAtTime object).
// This is used when our Java classes do NOT exactly match the structure and key names
// used in the JSON data. A mapping must be performed using code.
//
// This is where we MAP the structure of the JSON String onto the Java object.

import com.google.gson.*;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Create our own Deserializer class that implements the JsonDeserializer for
 * a specific class type (here, the IssPositionAtTime class).
 *
 * The deserialize() method will be called by the Gson parser when it needs to
 * parse a JSON String.  In this method, we 'get' the JSON as an JsonObject, and
 * we use appropriate JsonObject methods to extract the required fields.
 * We then instantiate a new Java object (ISSPositionAtTime) and populate it with
 * the data extracted from the JsonObject.
 *
 * JSON String --> JsonObject --> Java Object
 *
 */
public class JsonDeserializerIssPosition implements JsonDeserializer<IssPositionAtTime> {

    public IssPositionAtTime deserialize(JsonElement json,
                                         Type typeOfT,
                                         JsonDeserializationContext context) throws JsonParseException {

        // get Json String as a JSON object representing the ISS Position
        JsonObject jsonObject = json.getAsJsonObject();

        // Extract individual field values from the JSON object into temporary variables
        long timestamp = jsonObject.get("timestamp").getAsLong();
        String message = jsonObject.get("message").getAsString();

        // The "iss_position" key identifies a Json object (that contains lat and lng)
        // so, we extract it using getAsJsonObject().  (it is a nested object)
        // We then extract the latitude and longitude from this object.
        JsonObject locationObject = jsonObject.getAsJsonObject("iss_position");
        double latitude = locationObject.get("latitude").getAsDouble();
        double longitude = locationObject.get("longitude").getAsDouble();

        LocalDateTime localDateTime;

        localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp*1000),
                                                        TimeZone.getDefault().toZoneId());
        // construct a new object using the retrieved values
        IssPositionAtTime issPosition = new IssPositionAtTime(localDateTime, message, latitude, longitude);

        return issPosition;
    }
}