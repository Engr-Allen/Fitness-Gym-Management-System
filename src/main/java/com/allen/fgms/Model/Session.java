package com.allen.fgms.Model;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Session {
    private String date;
    private String startTime;
    private String endTime;
    private String session;
    private String facilitator;
    private String location;

    public Session(String date, String startTime, String endTime, String session, String facilitator, String location) {
        this.date = date;
        this.startTime = formatTime(startTime);
        this.endTime = formatTime(endTime);
        this.session = session;
        this.facilitator = facilitator;
        this.location = location;
    }

    private String formatTime(String time) {
        DateTimeFormatter inputFormat = DateTimeFormatter.ofPattern("HH:mm:ss.SSSSSSS");
        DateTimeFormatter outputFormat = DateTimeFormatter.ofPattern("h:mm a");
        LocalTime localTime = LocalTime.parse(time, inputFormat);
        return localTime.format(outputFormat);
    }

    public String getDate() {
        return date;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getSession() {
        return session;
    }

    public String getFacilitator() {
        return facilitator;
    }

    public String getLocation() {
        return location;
    }
}
