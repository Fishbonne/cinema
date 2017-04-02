package com.gpsolutions.rest.entities;

public class Reservation {
    private int id;
    private int sessionId;
    private String surName;
    private int seats;

    public Reservation(int id, int sessionId, String surName, int seats) {
        this.id = id;
        this.sessionId = sessionId;
        this.surName = surName;
        this.seats = seats;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    public String getSurName() {
        return surName;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }
}
