package org.example;

// Class that represents the Location of the ISS at a specific Time (Timestamped)

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

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

    public IssPositionAtTime() {
        this.dateTime = null;
        this.message=null;
        this.latitude=0.0;
        this.longitude=0.0;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "IssPositionAtTime{" +
                "Date & Time =" + dateTime.toString()+
                ", message='" + message + '\'' +
                ", latitude=" + latitude  +      //"[" + latitude + ", " + longitude + "]" +
                ",longitude=" + longitude +
                '}';
    }
}
