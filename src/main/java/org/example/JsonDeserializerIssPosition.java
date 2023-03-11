package org.example;

// Class that defines the deserialize() method that will be used by Gson to
// extract data from the JSON string, and put that data into the corresponding fields
// in the Java Objects (IssPositionAtTime object).
// This is where we MAP the structure of the JSON String onto the Java object.

import com.google.gson.*;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.TimeZone;

public class JsonDeserializerIssPosition implements JsonDeserializer<IssPositionAtTime> {

    public IssPositionAtTime deserialize(JsonElement json,
                                         Type typeOfT,
                                         JsonDeserializationContext context) throws JsonParseException {

        JsonObject jsonObject = json.getAsJsonObject(); // the JSON object representing the ISS Position

        // Extract the field data from the JSON object into temporary variables
        long timestamp = jsonObject.get("timestamp").getAsLong();
        String message = jsonObject.get("message").getAsString();

        // need to get the location as an Object as structured in JSON
        JsonObject locationObject = jsonObject.getAsJsonObject("iss_position");
        double latitude = locationObject.get("latitude").getAsDouble();
        double longitude = locationObject.get("longitude").getAsDouble();

        LocalDateTime localDateTime;

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy, hh:mm:ss a", Locale.ENGLISH);

        localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp*1000),
                                                        TimeZone.getDefault().toZoneId());

//        IssPositionAtTime issPosition = new IssPositionAtTime(localDateTime, message, latitude, longitude);

        IssPositionAtTime issPosition = new IssPositionAtTime();
        issPosition.setDateTime(localDateTime);
        issPosition.setMessage(message);
        issPosition.setLatitude(latitude);
        issPosition.setLongitude(longitude);

//
// employee = new Employee(); // can we call parameterised constructor?
//        employee.setId(id);
//        employee.setFirstName(firstName);
//        employee.setLastName(lastName);
//        employee.setDate(date);
//        employee.setPhoto(photoPath);
//        employee.setMarried(married);

        return issPosition;
    }
}