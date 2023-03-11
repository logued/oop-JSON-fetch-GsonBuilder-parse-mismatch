package org.example;

// Class that represents the Position of the ISS at a specific Time (Timestamped)

import java.time.LocalDateTime;

public class IssPositionAtTime {
    private LocalDateTime dateTime ;    // field name and type does NOT match JSON field name
    private String message;
    private double latitude;        // latitude and longitude are individual fields here,
    private double longitude;       // and although they have the same names, they do NOT match
                                    // the JSON structure which declares them in an iss_position object

    // The ISS API returns the timestamp in Unix Time
    // so, it is the number of seconds since 1st January 1970.
    //
    public IssPositionAtTime(LocalDateTime ldt, String message, double latitude, double longitude) {
        //this.dateTime = LocalDateTime.ofEpochSecond(unixTimeInSeconds,0, ZoneOffset.of("Europe/Dublin"));
        this.dateTime = ldt;
        this.message = message;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "IssPositionAtTime{" +
                "Date&Time=" + dateTime.toString()+
                ", message='" + message + '\'' +
                ", latitude=" + latitude  +
                ", longitude=" + longitude +
                '}';
    }
}
